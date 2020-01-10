package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessDAOInterface
import de.se.team3.logic.container.ProcessGroupsContainer
import de.se.team3.logic.container.ProcessTemplatesContainer
import de.se.team3.logic.container.TasksContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.domain.TaskComment
import de.se.team3.persistence.meta.ProcessesTable
import de.se.team3.persistence.meta.TaskAssignmentsTable
import de.se.team3.persistence.meta.TaskCommentsTable
import de.se.team3.persistence.meta.TasksTable
import de.se.team3.webservice.containerInterfaces.ProcessGroupsContainerInterface
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface
import de.se.team3.webservice.containerInterfaces.TasksContainerInterface
import de.se.team3.webservice.containerInterfaces.UserContainerInterface
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

/**
 * DAO for processes.
 */
object ProcessDAO : ProcessDAOInterface {

    private val processTemplatesContainer: ProcessTemplateContainerInterface = ProcessTemplatesContainer

    private val processGroupsContainer: ProcessGroupsContainerInterface = ProcessGroupsContainer

    private val tasksContainer: TasksContainerInterface = TasksContainer

    private val usersContainer: UserContainerInterface = UserContainer

    /**
     * Makes a single process object from the given row.
     */
    private fun makeProcess(row: QueryRowSet, tasks: Map<Int, Task>): Process {
        val starter = usersContainer.getUser(row[ProcessesTable.starterId]!!)
        val processGroup = processGroupsContainer.getProcessGroup(row[ProcessesTable.groupId]!!)
        val processTemplate = processTemplatesContainer.getProcessTemplate(row[ProcessesTable.processTemplateId]!!)

        return Process(
            row[ProcessesTable.id]!!,
            starter,
            processGroup,
            processTemplate,
            row[ProcessesTable.title]!!,
            row[ProcessesTable.description]!!,
            ProcessStatus.valueOf(row[ProcessesTable.status]!!),
            row[ProcessesTable.deadline],
            row[ProcessesTable.startedAt]!!,
            tasks
        )
    }

    /**
     * Returns the comments to the specified task.
     */
    private fun queryComments(taskId: Int): ArrayList<TaskComment> {
        val comments = ArrayList<TaskComment>()
        val commentsResult = TaskCommentsTable.select()
            .where {
                (TaskCommentsTable.taskId eq taskId) and
                        (TaskCommentsTable.deleted notEq true)
            }

        for (row in commentsResult) {
            val creator = usersContainer.getUser(row[TaskCommentsTable.creatorId]!!)
            comments.add(
                TaskComment(
                    row[TaskCommentsTable.id]!!,
                    null,
                    creator,
                    row[TaskCommentsTable.content]!!,
                    row[TaskCommentsTable.createdAt]!!
                ))
        }

        return comments
    }

    /**
     * Returns the assignments to the specified task.
     */
    private fun queryAssignments(taskId: Int): ArrayList<TaskAssignment> {
        val assignments = ArrayList<TaskAssignment>()
        val assigmentsResult = TaskAssignmentsTable.select()
            .where { TaskAssignmentsTable.taskId eq taskId }

        for (row in assigmentsResult) {
            val assignee = usersContainer.getUser(row[TaskAssignmentsTable.assigneeId]!!)
            assignments.add(
                TaskAssignment(
                    row[TaskAssignmentsTable.id]!!,
                    null,
                    assignee,
                    row[TaskAssignmentsTable.createdAt]!!,
                    row[TaskAssignmentsTable.doneAt]
                ))
        }

        return assignments
    }

    /**
     * Returns the tasks for the specified process.
     */
    private fun queryTasks(processId: Int): Map<Int, Task> {
        val tasks = HashMap<Int, Task>()
        val tasksResult = TasksTable.select().where { TasksTable.processId eq processId }

        for (row in tasksResult) {
            val taskId = row[TasksTable.id]!!
            val taskTemplateId = row[TasksTable.taskTemplateId]!!
            val task = Task(
                taskId,
                taskTemplateId,
                row[TasksTable.startedAt],
                queryComments(taskId),
                queryAssignments(taskId),
                null
            )
            tasks.put(taskTemplateId, task)
        }

        return tasks.toMap()
    }

    /**
     * Returns all processes.
     */
    override fun getAllProcesses(): List<Process> {
        val result = ProcessesTable
            .select()

        val processes = ArrayList<Process>()
        for (row in result) {
            val tasks = queryTasks(row[ProcessesTable.id]!!)
            processes.add(makeProcess(row, tasks))
        }

        return processes.toList()
    }

    /**
     * Returns the specified process.
     *
     * @return Null if the specified process does not exist.
     */
    override fun getProcess(processId: Int): Process? {
        val tasks = HashMap<Int, Task>()
        val tasksResult = TasksTable.select().where { TasksTable.processId eq processId }

        for (row in tasksResult) {
            val taskId = row[TasksTable.id]!!
            val taskTemplateId = row[TasksTable.taskTemplateId]!!
            val task = Task(
                taskId,
                taskTemplateId,
                row[TasksTable.startedAt],
                queryComments(taskId),
                queryAssignments(taskId),
                null
            )
            tasks.put(taskTemplateId, task)
        }

        val processResult = ProcessesTable.select().where { ProcessesTable.id eq processId }
        val row = processResult.rowSet
        if (!row.next())
            return null

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
                row.groupId to process.getProcessGroupId()
                row.title to process.title
                row.description to process.description
                row.status to process.getStatus().toString()
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
        } finally {
            transaction.close()
        }
    }

    /**
     * Sets the status of the given process to aborted.
     *
     * @return True if the specified process existed and was running.
     */
    override fun closeProcess(processId: Int): Boolean {
        val affectedRows = ProcessesTable.update {
            it.status to ProcessStatus.CLOSED.toString()
            where {
                (it.id eq processId) and
                        (it.status eq ProcessStatus.RUNNING.toString())
            }
        }
        return affectedRows != 0
    }

    /**
     * Sets the status of the given process to aborted.
     *
     * @return True if the specified process existed and was running.
     */
    override fun abortProcess(processId: Int): Boolean {
        val affectedRows = ProcessesTable.update {
            it.status to ProcessStatus.ABORTED.toString()
            where {
                (it.id eq processId) and
                        (it.status eq ProcessStatus.RUNNING.toString())
            }
        }
        return affectedRows != 0
    }
}
