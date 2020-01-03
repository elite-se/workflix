package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.domain.TaskComment
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.meta.ProcessesTable
import de.se.team3.persistence.meta.TaskAssignmentsTable
import de.se.team3.persistence.meta.TaskCommentsTable
import de.se.team3.persistence.meta.TasksTable
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

/**
 * DAO for processes.
 */
object ProcessDAO : ProcessDAOInterface {

    /**
     * Returns all processes.
     */
    override fun getAllProcesses(): List<Process> {
        val processes = ArrayList<Process>()
        val result = ProcessesTable.select()

        for (row in result)
            processes.add(
                makeProcess(row, null))

        return processes.toList()
    }

    /**
     * Makes a single process object from the given row.
     */
    private fun makeProcess(row: QueryRowSet, tasks: MutableMap<Int, Task>?): Process {
        return Process(
            row[ProcessesTable.id]!!,
            row[ProcessesTable.starterId]!!,
            row[ProcessesTable.groupId]!!,
            row[ProcessesTable.processTemplateId]!!,
            row[ProcessesTable.title]!!,
            row[ProcessesTable.description]!!,
            ProcessStatus.valueOf(row[ProcessesTable.status]!!),
            row[ProcessesTable.deadline],
            row[ProcessesTable.startedAt]!!,
            tasks
        )
    }

    /**
     * Returns the comments to the given task.
     */
    private fun getComments(taskId: Int): List<TaskComment> {
        val comments = ArrayList<TaskComment>()
        val commentsResult = TaskCommentsTable.select()
            .where { TaskCommentsTable.taskId eq taskId }

        for (row in commentsResult)
            comments.add(
                TaskComment(
                    row[TaskCommentsTable.id]!!,
                    row[TaskCommentsTable.taskId]!!,
                    row[TaskCommentsTable.creatorId]!!,
                    row[TaskCommentsTable.content]!!,
                    row[TaskCommentsTable.createdAt]!!
                ))

        return comments.toList()
    }

    /**
     * Returns the assignments to the given task.
     */
    private fun getAssignments(taskId: Int): List<TaskAssignment> {
        val assignments = ArrayList<TaskAssignment>()
        val assigmentsResult = TaskAssignmentsTable.select()
            .where { TaskAssignmentsTable.taskId eq taskId }

        for (row in assigmentsResult) {
            assignments.add(
                TaskAssignment(
                    row[TaskAssignmentsTable.id]!!,
                    row[TaskAssignmentsTable.taskId]!!,
                    row[TaskAssignmentsTable.assigneeId]!!,
                    row[TaskAssignmentsTable.createdAt]!!,
                    row[TaskAssignmentsTable.doneAt]
                ))
        }

        return assignments.toList()
    }

    /**
     * Returns the single process specified by the given process id.
     */
    override fun getProcess(processId: Int): Process {
        val tasks = HashMap<Int, Task>()
        val tasksResult = TasksTable.select().where { TasksTable.processId eq processId }

        for (row in tasksResult) {
            val taskId = row[TasksTable.id]!!
            val taskTemplateId = row[TasksTable.taskTemplateId]!!
            val task = Task(
                taskId,
                taskTemplateId,
                row[TasksTable.startedAt],
                getComments(taskId),
                getAssignments(taskId),
                null
            )
            tasks.put(taskTemplateId, task)
        }

        val processResult = ProcessesTable.select().where { ProcessesTable.id eq processId }
        val row = processResult.rowSet.iterator().next()

        return makeProcess(row, tasks)
    }

    /**
     * Creates the given process.
     */
    override fun createProcess(process: Process): Int {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        val processTemplate = process.processTemplate

        try {
            // adds the process to db
            val generatedProcessId = ProcessesTable.insertAndGenerateKey { row ->
                row.processTemplateId to processTemplate.id
                row.starterId to process.starter.id
                row.groupId to process.processGroupId
                row.title to process.title
                row.description to process.description
                row.status to process.status.toString()
                row.deadline to process.deadline
                row.startedAt to process.startedAt
            }

            // adds the taks to db
            val tasks = process.tasks?.map { it.value }!!
            TasksTable.batchInsert {
                process?.tasks.forEach { id, task ->
                    item { row ->
                        row.processId to generatedProcessId
                        row.taskTemplateId to id
                        row.startedAt to task.startedAt
                    }
                }
            }

            transaction.commit()
            return generatedProcessId as Int
        } catch (e: Throwable) {
            transaction.rollback()
            throw e
            throw StorageException("" + e.message)
        }
    }

    /**
     * Sets the status of the given process to aborted.
     *
     * @throws NotFoundException Is thrown if the given process does not exist or is already aborted
     * or already closed. The of course the process could not be closed or closed again respectively.
     */
    override fun closeProcess(processId: Int) {
        val affectedRows = ProcessesTable.update {
            it.status to ProcessStatus.CLOSED.toString()
            where {
                (it.id eq processId) and
                        (it.status notEq ProcessStatus.ABORTED.toString()) and
                        (it.status notEq ProcessStatus.CLOSED.toString())
            }
        }
        if (affectedRows == 0)
            throw NotFoundException("process not found")
    }

    /**
     * Sets the status of the given process to aborted.
     *
     * @throws NoSuchElementException Is thrown if the given process does not exist or is already closed
     * or already aborted. Then of course the process could not be aborted or aborted again respectively.
     */
    override fun abortProcess(processId: Int) {
        val affectedRows = ProcessesTable.update {
            it.status to ProcessStatus.ABORTED.toString()
            where {
                (it.id eq processId) and
                        (it.status notEq ProcessStatus.CLOSED.toString()) and
                        (it.status notEq ProcessStatus.ABORTED.toString())
            }
        }
        if (affectedRows == 0)
            throw NoSuchElementException()
    }
}
