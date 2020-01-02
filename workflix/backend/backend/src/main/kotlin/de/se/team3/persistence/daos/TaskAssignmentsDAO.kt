package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.TaskAssigmentsDAOInterface
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.meta.TaskAssignmentsTable
import de.se.team3.persistence.meta.TasksTable
import de.se.team3.persistence.meta.UsersTable
import java.time.Instant
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.isNull
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

/**
 * DAO for task assignments.
 */
object TaskAssignmentsDAO : TaskAssigmentsDAOInterface {

    /**
     * Creates a task assignment.
     *
     * @throws NotFoundException Is thrown if the underlying task or assignee
     * could not be found.
     * @throws AlreadyExistsException Is thrown if this assignment already exists,
     * i.d. if there is an assignment with the same user to the same task.
     */
    override fun createTaskAssigment(taskAssignment: TaskAssignment): Int {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try {
            checkConstraints(taskAssignment)

            val generatedProcessId = TaskAssignmentsTable.insertAndGenerateKey { row ->
                row.taskId to taskAssignment.taskId
                row.assigneeId to taskAssignment.asigneeId
                row.createdAt to taskAssignment.createdAt
                row.doneAt to taskAssignment.doneAt
                row.deleted to false
            }

            transaction.commit()
            return generatedProcessId as Int
        } catch (e: Throwable) {
            transaction.rollback()
            throw e
        }
    }

    /**
     * Checks some task assignment constraints.
     */
    private fun checkConstraints(taskAssignment: TaskAssignment) {
        val assigneeResult = UsersTable.select().where { UsersTable.ID eq taskAssignment.asigneeId }
        if (!assigneeResult.rowSet.iterator().hasNext())
            throw NotFoundException("assignee not found")

        val taskResult = TasksTable.select().where { TasksTable.id eq taskAssignment.taskId }
        if (!taskResult.rowSet.iterator().hasNext())
            throw NotFoundException("task not found")

        val result = TaskAssignmentsTable.select()
            .where {
                (TaskAssignmentsTable.taskId eq taskAssignment.taskId) and
                (TaskAssignmentsTable.assigneeId eq taskAssignment.asigneeId)
            }

        if (result.rowSet.iterator().hasNext())
            throw AlreadyExistsException("task assignment already exists")
    }

    /**
     * Closes the given task assignment.
     *
     * Note that a task assignment is not assumed to exist if it is already closed.
     *
     * @throws NotFoundException Is thrown if the given task assignment does not exist.
     */
    override fun closeTaskAssigment(taskId: Int, assigneeId: String) {
        val affectedRows = TaskAssignmentsTable.update { row ->
            row.doneAt to Instant.now()
            where {
                (row.taskId eq taskId) and
                (row.assigneeId eq assigneeId) and
                (row.deleted notEq true) and // The non existence of a task assignment is assumed if the assignment was deleted before.
                (row.doneAt.isNull())
            }
        }
        if (affectedRows == 0)
            throw NotFoundException("task assignment not found")
    }

    /**
     * Deletes the specified task assignment.
     *
     * Note that a task assignment is not assumed to exist if it is already closed.
     *
     * @throws NotFoundException Is thrown if the given task assignment does not exist.
     */
    override fun deleteTaskAssigment(taskId: Int, assigneeId: String) {
        val affectedRows = TaskAssignmentsTable.update { row ->
            row.deleted to true
            where {
                (row.taskId eq taskId) and
                (row.assigneeId eq assigneeId) and
                (row.doneAt.isNull()) and
                (row.deleted notEq true) //  The non existence of a task assignment is assumed if the assignment was deleted before.
            }
        }
        if (affectedRows == 0)
            throw NotFoundException("task assignment not found")
    }
}
