package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessTemplateDAOInterface
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessTemplatesTable
import de.se.team3.persistence.meta.TaskTemplateRelationshipsTable
import de.se.team3.persistence.meta.TaskTemplatesTable
import de.se.team3.persistence.meta.UsersTable
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.forEach
import kotlin.collections.iterator
import kotlin.collections.map
import kotlin.collections.toList
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.innerJoin
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

object ProcessTemplateDAO : ProcessTemplateDAOInterface {

    /**
     * Returns a sub list of all process templates and the total amount of such templates.
     */
    override fun getAllProcessTemplates(offset: Int, limit: Int): Pair<List<ProcessTemplate>, Int> {
        val processTemplates = ArrayList<ProcessTemplate>()
        val result = ProcessTemplatesTable
            .innerJoin(UsersTable, on = UsersTable.ID eq ProcessTemplatesTable.ownerID)
            .select()
            .limit(offset, limit)

        for (row in result) {
            val owner = User(
                row[UsersTable.ID]!!,
                row[UsersTable.name]!!,
                row[UsersTable.displayname]!!,
                row[UsersTable.email]!!
            )
            val template = ProcessTemplate(
                row[ProcessTemplatesTable.ID]!!,
                row[ProcessTemplatesTable.title]!!,
                row[ProcessTemplatesTable.durationLimit],
                owner
            )
            processTemplates.add(template)
        }

        return Pair(processTemplates.toList(), 0)
    }

    /**
     * Returns the ids of the successors of the given task.
     */
    private fun getSuccessorList(taskId: Int): List<Int> {
        val successorsResult = TaskTemplateRelationshipsTable
            .select().where { TaskTemplateRelationshipsTable.predecessor eq taskId }

        val successors = ArrayList<Int>()
        for (row in successorsResult)
            successors.add(row[TaskTemplateRelationshipsTable.successor]!!)

        return successors
    }

    /**
     * Returns the task templates for a given process template as a set.
     */
    private fun getTaskTemplates(processTemplateId: Int): Map<Int, TaskTemplate> {
        val taskTemplatesResult = TaskTemplatesTable
            .select().where { TaskTemplatesTable.templateID eq processTemplateId }

        val taskTemplatesMap = HashMap<Int, TaskTemplate>()
        val successorsPerTask = HashMap<Int, List<Int>>()
        val predecessorPerTask = HashMap<Int, List<Int>>()

        // Collect all task templates and for each task template the successors
        for (row in taskTemplatesResult) {
            val taskId = row[TaskTemplatesTable.ID]!!

            taskTemplatesMap.put(
                taskId,
                TaskTemplate(
                    taskId,
                    row[TaskTemplatesTable.name]!!,
                    row[TaskTemplatesTable.estimatedDuration],
                    row[TaskTemplatesTable.durationLimit]
                )
            )

            successorsPerTask.put(taskId, getSuccessorList(taskId))
        }

        // Add the collected relationships to the task templates
        for (entry in taskTemplatesMap) {
            val taskTemplate = entry.value

            for (successorId in successorsPerTask.get(taskTemplate.id)!!) {
                val successorTaskTemplate = taskTemplatesMap.get(successorId)!!
                taskTemplate.successors?.add(successorTaskTemplate)
                successorTaskTemplate.predecessors?.add(taskTemplate)
            }
        }

        return taskTemplatesMap
    }

    /**
     * Returns the single process template specified by the given template id.
     */
    override fun getProcessTemplate(templateId: Int): ProcessTemplate {
        val processTemplateResult = ProcessTemplatesTable
            .innerJoin(UsersTable, on = UsersTable.ID eq ProcessTemplatesTable.ownerID)
            .select().where { ProcessTemplatesTable.ID eq templateId }

        val row = processTemplateResult.rowSet.iterator().next()

        val owner =
            User(row[UsersTable.ID]!!, row[UsersTable.name]!!, row[UsersTable.displayname]!!, row[UsersTable.email]!!)
        val processId = row[ProcessTemplatesTable.ID]!!
        val taskTemplates = getTaskTemplates(processId)

        val processTemplate = ProcessTemplate(
            processId, row[ProcessTemplatesTable.title]!!,
            row[ProcessTemplatesTable.durationLimit], owner, taskTemplates
        )

        return processTemplate
    }

    /**
     * Adds the given process template.
     */
    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        var newProcessTemplateId: Int

        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try { // adds the process template to db
            val generatedProcessTemplateId = ProcessTemplatesTable.insertAndGenerateKey {
                it.title to processTemplate.title
                it.durationLimit to processTemplate.durationLimit
                it.ownerID to processTemplate.owner.id
            }

            // adds the task templates to db
            val idMapping = HashMap<Int, Int>()
            processTemplate.taskTemplates?.map { it.value }?.forEach { taskTemplate ->
                val generatedTaskTemplateId = TaskTemplatesTable.insertAndGenerateKey { row ->
                    row.name to taskTemplate.name
                    row.estimatedDuration to taskTemplate.estimatedDuration
                    row.durationLimit to taskTemplate.durationLimit
                    row.templateID to generatedProcessTemplateId as Int
                }
                idMapping.put(taskTemplate.id!!, generatedTaskTemplateId as Int)
            }
}

            // adds the relationships between task templates to db
            processTemplate.taskTemplates?.map { it.value }?.forEach { taskTemplate ->
                taskTemplate.successors?.forEach { successor ->
                    TaskTemplateRelationshipsTable.insert { row ->
                        row.predecessor to idMapping.get(taskTemplate.id)
                        row.successor to idMapping.get(successor.id)
                    }
                }
            }

            transaction.commit()
            return generatedProcessTemplateId as Int
        } catch (e: Throwable) {
            transaction.rollback()
            throw StorageException("Storage Exception: " + e.message)
        }
    }

    /**
     * Sets the deleted flag for the given process template.
     */
    override fun deleteProcessTemplate(processTemplateId: Int) {
        val affectedRows = ProcessTemplatesTable.update {
            it.deleted to true
            where { it.ID eq processTemplateId }
        }
        if (affectedRows == 0)
            throw NoSuchElementException()
    }
}
