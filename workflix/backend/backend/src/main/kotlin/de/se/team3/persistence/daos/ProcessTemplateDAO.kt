package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessTemplateDAOInterface
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessTemplatesTable
import de.se.team3.persistence.meta.ProcessTemplatesView
import de.se.team3.persistence.meta.TaskTemplateRelationshipsTable
import de.se.team3.persistence.meta.TaskTemplatesTable
import de.se.team3.persistence.meta.UsersTable
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.toList
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.inList
import me.liuwj.ktorm.dsl.innerJoin
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

/**
 * DAO for process templates.
 */
object ProcessTemplateDAO : ProcessTemplateDAOInterface {

    /**
     * Makes a single process template from the given row.
     */
    private fun makeProcessTemplate(row: QueryRowSet, owner: User, taskTemplates: Map<Int, TaskTemplate>?): ProcessTemplate {
        return ProcessTemplate(
            row[ProcessTemplatesView.id]!!,
            row[ProcessTemplatesView.title]!!,
            row[ProcessTemplatesView.description]!!,
            row[ProcessTemplatesView.durationLimit],
            owner,
            row[ProcessTemplatesView.createdAt]!!,
            row[ProcessTemplatesView.processCount]!!,
            row[ProcessTemplatesView.runningProcesses]!!,
            row[ProcessTemplatesView.deleted]!!,
            taskTemplates
        )
    }

    /**
     * Returns all process templates.
     */
    override fun getAllProcessTemplates(): List<ProcessTemplate> {
        val processTemplates = ArrayList<ProcessTemplate>()
        val result = ProcessTemplatesView
            .innerJoin(UsersTable, on = UsersTable.ID eq ProcessTemplatesView.ownerId)
            .select()

        for (row in result) {
            val owner = User(
                row[UsersTable.ID]!!,
                row[UsersTable.name]!!,
                row[UsersTable.displayname]!!,
                row[UsersTable.email]!!
            )
            processTemplates.add(makeProcessTemplate(row, owner, null))
        }

        return processTemplates.toList()
    }

    /**
     * Returns the task templates for the given process template.
     */
    private fun getTaskTemplates(processTemplateId: Int): Map<Int, TaskTemplate> {
        // Collect all task templates
        val taskTemplatesResult = TaskTemplatesTable
            .select().where { TaskTemplatesTable.processTemplateId eq processTemplateId }

        val taskTemplatesMap = HashMap<Int, TaskTemplate>()
        for (row in taskTemplatesResult) {
            val taskTemplate = TaskTemplateDAO.makeTaskTemplate(row)
            taskTemplatesMap.put(
                taskTemplate.id,
                taskTemplate
            )
        }

        // Add the relationships to the task templates
        val successorsPerTaskResult = TaskTemplatesTable.innerJoin(
            TaskTemplateRelationshipsTable,
            on = TaskTemplateRelationshipsTable.predecessor eq TaskTemplatesTable.id
        ).select().where { TaskTemplatesTable.processTemplateId eq processTemplateId }

        for (row in successorsPerTaskResult) {
            val taskTemplate = taskTemplatesMap.get(row[TaskTemplateRelationshipsTable.predecessor])!!
            val successorTemplate = taskTemplatesMap.get(row[TaskTemplateRelationshipsTable.successor])!!
            taskTemplate.successors.add(successorTemplate)
            successorTemplate.predecessors.add(taskTemplate)
        }

        return taskTemplatesMap
    }

    /**
     * Returns the single process template specified by the given template id.
     */
    override fun getProcessTemplate(templateId: Int): ProcessTemplate {
        val processTemplateResult = ProcessTemplatesView
            .innerJoin(UsersTable, on = UsersTable.ID eq ProcessTemplatesView.ownerId)
            .select().where { ProcessTemplatesView.id eq templateId }

        val row = processTemplateResult.rowSet.iterator().next()
      
        val owner = User(
            row[UsersTable.ID]!!,
            row[UsersTable.name]!!,
            row[UsersTable.displayname]!!,
            row[UsersTable.email]!!
        )
        val taskTemplates = getTaskTemplates(row[ProcessTemplatesView.id]!!)

        return makeProcessTemplate(row, owner, taskTemplates)
    }

    /**
     * Creates the given process template.
     */
    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try {
            val generatedProcessTemplateId = createProcessTemplateDbRequest(processTemplate, null)

            transaction.commit()
            return generatedProcessTemplateId
        } catch (e: Throwable) {
            transaction.rollback()
            throw StorageException("Storage Exception: " + e.message)
        }
    }

    /**
     * Does the actual requests to the db for creating a process template.
     *
     * Is also used by updateProcessTemplate.
     */
    private fun createProcessTemplateDbRequest(processTemplate: ProcessTemplate, formerVersion: Int?): Int {
        // adds the process template
        val generatedProcessTemplateId = ProcessTemplatesTable.insertAndGenerateKey { row ->
            row.ownerId to processTemplate.owner.id
            row.title to processTemplate.title
            row.description to processTemplate.description
            row.durationLimit to processTemplate.durationLimit
            row.createdAt to processTemplate.createdAt
            row.formerVersion to formerVersion
        }

        createTaskTemplatesDbRequest(processTemplate?.taskTemplates?.map { it.value }!!, generatedProcessTemplateId as Int)

        return generatedProcessTemplateId as Int
    }

    /**
     * Does the actual requests to the db for creating task templates and their realtionships.
     */
    private fun createTaskTemplatesDbRequest(taskTemplates: List<TaskTemplate>, processTemplateId: Int) {
        // Note that the task templates of the given process templates have arbitrary ids.
        // This maps them to the db internal ids after creating the task templates in db.
        val idMapping = HashMap<Int, Int>()

        // adds the task templates
        taskTemplates.forEach { taskTemplate ->
            val generatedTaskTemplateId = TaskTemplatesTable.insertAndGenerateKey { row ->
                row.processTemplateId to processTemplateId as Int
                row.name to taskTemplate.name
                row.description to taskTemplate.description
                row.estimatedDuration to taskTemplate.estimatedDuration
                row.durationLimit to taskTemplate.durationLimit
                row.necessaryClosings to taskTemplate.necessaryClosings
            }
            idMapping.put(taskTemplate.id!!, generatedTaskTemplateId as Int)
        }

        // adds the relationships between task templates
        TaskTemplateRelationshipsTable.batchInsert {
            taskTemplates.forEach { taskTemplate ->
                taskTemplate.successors?.forEach { successor ->
                    item { row ->
                        row.predecessor to idMapping.get(taskTemplate.id)
                        row.successor to idMapping.get(successor.id)
                    }
                }
            }
        }
    }

    /**
     * Updates the given process template.
     *
     * The behavior in case of the alteration of an process template depends on the usage of the
     * process template for the creation of processes. If the process template is not used yet for the
     * creation of a process it is really altered. If the process template is already in use then
     * the whole process template will be copied and the former version will be hided.
     */
    override fun updateProcessTemplate(processTemplate: ProcessTemplate): Int? {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try {
            val processCountResult = ProcessTemplatesView.select()
                .where { ProcessTemplatesView.id eq processTemplate.id!! }

            val row = processCountResult.rowSet.iterator().next()
            var newId: Int? = null
            if (row[ProcessTemplatesView.processCount]!! == 0)
                realUpdateProcessTemplate(processTemplate)
            else
                newId = createProcessTemplateDbRequest(processTemplate, processTemplate.id)

            transaction.commit()
            return newId
        } catch (e: NoSuchElementException) {
            transaction.rollback()
            throw e
        } catch (e: Throwable) {
            transaction.rollback()
            throw StorageException("Storage Exception: " + e.message)
        }
    }

    /**
     * Makes an real update of the given process template.
     */
    private fun realUpdateProcessTemplate(processTemplate: ProcessTemplate) {
        ProcessTemplatesTable.update { row ->
            row.ownerId to processTemplate.owner.id
            row.title to processTemplate.title
            row.description to processTemplate.description
            row.durationLimit to processTemplate.durationLimit

            where { row.id eq processTemplate.id!! }
        }

        // delete all relationships of task templates
        TaskTemplateRelationshipsTable.delete { row ->
            row.predecessor inList TaskTemplatesTable.select(TaskTemplatesTable.id)
                .where { TaskTemplatesTable.processTemplateId eq processTemplate.id!! }
        }

        // delete all task templates
        TaskTemplatesTable.delete { row ->
            row.processTemplateId eq processTemplate.id!!
        }

        // create new task templates and their relationships
        createTaskTemplatesDbRequest(processTemplate.taskTemplates?.map { it.value }!!, processTemplate.id!!)
    }

    /**
     * Sets the deleted flag for the given process template.
     */
    override fun deleteProcessTemplate(processTemplateId: Int) {
        val affectedRows = ProcessTemplatesTable.update {
            it.deleted to true
            where { it.id eq processTemplateId }
        }
        if (affectedRows == 0)
            throw NoSuchElementException()
    }
}
