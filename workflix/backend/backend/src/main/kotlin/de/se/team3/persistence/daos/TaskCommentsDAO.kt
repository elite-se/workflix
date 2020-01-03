package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.TaskCommentsDAOInterface
import de.se.team3.logic.domain.TaskComment
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.meta.TaskCommentsTable
import de.se.team3.persistence.meta.TasksTable
import de.se.team3.persistence.meta.UsersTable
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

/**
 * DAO for task comments.
 */
object TaskCommentsDAO : TaskCommentsDAOInterface {

    /**
     * Returns the id of the task the given comment belongs to.
     */
    override fun getTaskIdByTaskCommentId(taskCommentId: Int): Int? {
        val result = TaskCommentsTable.select()
            .where { TaskCommentsTable.id eq taskCommentId }

        val row = result.rowSet
        if (!row.next())
            return null

        return row[TaskCommentsTable.taskId]
    }

    /**
     * Creates the given task comment.
     *
     * @throws NotFoundException Is thrown if the underlying task or creator
     * could not be found.
     */
    override fun createTaskComment(taskComment: TaskComment): Int {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try {
            checkConstraints(taskComment)

            val generatedProcessId = TaskCommentsTable.insertAndGenerateKey { row ->
                row.taskId to taskComment.taskId
                row.creatorId to taskComment.creatorId
                row.content to taskComment.content
                row.createdAt to taskComment.createdAt
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
     * Checks some task comment constraints.
     */
    private fun checkConstraints(taskComment: TaskComment) {
        val creatorResult = UsersTable.select().where { UsersTable.ID eq taskComment.creatorId!! }
        if (!creatorResult.rowSet.iterator().hasNext())
            throw NotFoundException("creator not found")

        val taskResult = TasksTable.select().where { TasksTable.id eq taskComment.taskId!! }
        if (!taskResult.rowSet.iterator().hasNext())
            throw NotFoundException("task not found")
    }

    /**
     * Updates the given task comment.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    override fun updateTaskComment(taskComment: TaskComment) {
        val affectedRows = TaskCommentsTable.update { row ->
            row.content to taskComment.content
            where {
                (TaskCommentsTable.id eq taskComment.id!!) and
                        (TaskCommentsTable.deleted notEq true)
            }
        }
        if (affectedRows == 0)
            throw NotFoundException("task comment not found")
    }

    /**
     * Deletes the specified task assignment.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    override fun deleteTaskComment(taskCommentId: Int) {
        val affectedRows = TaskCommentsTable.update { row ->
            row.deleted to true
            where {
                (TaskCommentsTable.id eq taskCommentId) and
                        (TaskCommentsTable.deleted notEq true)
            }
        }
        if (affectedRows == 0)
            throw NotFoundException("task comment not found")
    }
}
