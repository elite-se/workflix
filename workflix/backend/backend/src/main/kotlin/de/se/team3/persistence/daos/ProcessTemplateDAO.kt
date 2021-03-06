package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessTemplateDAOInterface
import de.se.team3.logic.container.UserContainer
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
import kotlin.collections.toList
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.inList
import me.liuwj.ktorm.dsl.innerJoin
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

/**
 * DAO for process templates.
 */
object ProcessTemplateDAO : ProcessTemplateDAOInterface {

    private val usersContainer: UserContainer = UserContainer

    /**
     * Makes a process template from the given row and the given task templates.
     */
    private fun makeProcessTemplate(row: QueryRowSet, owner: User, taskTemplates: Map<Int, TaskTemplate>): ProcessTemplate {
        return ProcessTemplate(
            row[ProcessTemplatesView.id],
            row[ProcessTemplatesView.title]!!,
            row[ProcessTemplatesView.description]!!,
            row[ProcessTemplatesView.durationLimit]!!,
            owner,
            row[ProcessTemplatesView.createdAt]!!,
            row[ProcessTemplatesView.formerVersion],
            row[ProcessTemplatesView.processCount]!!,
            row[ProcessTemplatesView.runningProcesses]!!,
            row[ProcessTemplatesView.deleted]!!,
            taskTemplates
        )
    }

    /**
     * Returns the task templates for the specified process template.
     */
    private fun queryTaskTemplates(processTemplateId: Int): Map<Int, TaskTemplate> {
        // Collect all task templates
        val taskTemplatesResult = TaskTemplatesTable
            .select()
            .where { TaskTemplatesTable.processTemplateId eq processTemplateId }

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
            successorTemplate.addPredecessor(taskTemplate)
        }

        return taskTemplatesMap
    }

    /**
     * Returns all process templates.
     */
    override fun getAllProcessTemplates(): List<ProcessTemplate> {
        val processTemplates = ArrayList<ProcessTemplate>()
        val result = ProcessTemplatesView
            .select()

        for (row in result) {
            val taskTemplates = queryTaskTemplates(row[ProcessTemplatesView.id]!!)
            val owner = usersContainer.getUser(row[ProcessTemplatesView.ownerId]!!)
            processTemplates.add(makeProcessTemplate(row, owner, taskTemplates))
        }

        return processTemplates.toList()
    }

    /**
     * Returns the specified process template.
     *
     * @return Null if the specified process does not exist.
     */
    override fun getProcessTemplate(processTemplateId: Int): ProcessTemplate? {
        val processTemplateResult = ProcessTemplatesView
            .innerJoin(UsersTable, on = UsersTable.ID eq ProcessTemplatesView.ownerId)
            .select()
            .where { ProcessTemplatesView.id eq processTemplateId }

        val row = processTemplateResult.rowSet
        if (!row.next())
            return null

        val taskTemplates = queryTaskTemplates(row[ProcessTemplatesView.id]!!)
        val owner = usersContainer.getUser(row[ProcessTemplatesView.ownerId]!!)
        return makeProcessTemplate(row, owner, taskTemplates)
    }

    /**
     * Inserts the task templates and their relationships of the given process template.
     */
    private fun insertTaskTemplates(processTemplateId: Int, taskTemplates: List<TaskTemplate>) {
        // Note that the task templates of the given process templates have arbitrary ids.
        // This maps them to the db internal ids after creating the task templates in db.
        val idMapping = HashMap<Int, Int>()

        // adds the task templates
        taskTemplates.forEach { taskTemplate ->
            val generatedTaskTemplateId = TaskTemplatesTable.insertAndGenerateKey { row ->
                row.processTemplateId to processTemplateId
                row.responsibleUserRoleId to taskTemplate.responsibleUserRoleId
                row.name to taskTemplate.name
                row.description to taskTemplate.description
                row.estimatedDuration to taskTemplate.estimatedDuration
                row.necessaryClosings to taskTemplate.necessaryClosings
            }
            idMapping.put(taskTemplate.id, generatedTaskTemplateId as Int)
        }

        // adds the relationships between task templates
        TaskTemplateRelationshipsTable.batchInsert {
            taskTemplates.forEach { taskTemplate ->
                taskTemplate.getSuccessors().forEach { successor ->
                    item { row ->
                        row.predecessor to idMapping[taskTemplate.id]
                        row.successor to idMapping[successor.id]
                    }
                }
            }
        }
    }

    /**
     * Creates the given process template.
     *
     * @return The generated id of the process template.
     */
    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try {
            // creates the actual process template
            val generatedProcessTemplateId = ProcessTemplatesTable.insertAndGenerateKey { row ->
                row.ownerId to processTemplate.ownerId
                row.title to processTemplate.title
                row.description to processTemplate.description
                row.durationLimit to processTemplate.durationLimit
                row.createdAt to processTemplate.createdAt
                row.formerVersion to processTemplate.formerVersionId
            } as Int

            // creates the task templates
            insertTaskTemplates(generatedProcessTemplateId, processTemplate.taskTemplatesList)

            transaction.commit()
            return generatedProcessTemplateId
        } catch (e: Throwable) {
            transaction.rollback()
            throw e
        } finally {
            transaction.close()
        }
    }

    /**
     * Deletes the task templates (including their relationships) of the specified process template.
     */
    private fun deleteTaskTemplatesRequests(processTemplateId: Int) {
        // delete all relationships of task templates
        TaskTemplateRelationshipsTable.delete { row ->
            row.predecessor inList TaskTemplatesTable.select(TaskTemplatesTable.id)
                .where { TaskTemplatesTable.processTemplateId eq processTemplateId }
        }

        // delete all task templates
        TaskTemplatesTable.delete { row ->
            row.processTemplateId eq processTemplateId
        }
    }

    /**
     * Updates the given process template.
     *
     * @return True if and only if the given process template existed.
     */
    override fun updateProcessTemplate(processTemplate: ProcessTemplate): Boolean {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try {
            // updates the actual process template
            val affectedRows = ProcessTemplatesTable.update { row ->
                row.ownerId to processTemplate.ownerId
                row.title to processTemplate.title
                row.description to processTemplate.description
                row.durationLimit to processTemplate.durationLimit

                where {
                    (row.id eq processTemplate.id!!) and
                            (row.deleted notEq true)
                }
            }
            if (affectedRows == 0) {
                transaction.rollback()
                return false
            }

            // updates the task templates
            deleteTaskTemplatesRequests(processTemplate.id!!)
            insertTaskTemplates(processTemplate.id, processTemplate.taskTemplatesList)

            transaction.commit()
            return true
        } catch (e: Throwable) {
            transaction.rollback()
            throw e
        } finally {
            transaction.close()
        }
    }

    /**
     * Sets the deleted flag for the specified process template.
     *
     * @return True if and only if the specified process template existed.
     */
    override fun deleteProcessTemplate(processTemplateId: Int): Boolean {
        val affectedRows = ProcessTemplatesTable.update {
            it.deleted to true
            where {
                (it.id eq processTemplateId) and
                        (it.deleted notEq true)
            }
        }
        return affectedRows != 0
    }
}
