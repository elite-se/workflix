package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.TaskCommentsDAOInterface
import de.se.team3.logic.domain.TaskComment
import de.se.team3.persistence.meta.TaskCommentsTable
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
     * Returns the id of the task the specified comment belongs to.
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
     * Returns the id of the user the specified comment was created by.
     */
    override fun getCreatorIdByTaskCommentId(taskCommentId: Int): String? {
        val result = TaskCommentsTable
            .select()
            .where { TaskCommentsTable.id eq taskCommentId }

        val row = result.rowSet
        if (!row.next())
            return null

        return row[TaskCommentsTable.creatorId]
    }

    /**
     * Creates the given task comment.
     *
     * @return The generated id for the task assignment.
     */
    override fun createTaskComment(taskComment: TaskComment): Int {
        val generatedProcessId = TaskCommentsTable.insertAndGenerateKey { row ->
            row.taskId to taskComment.taskId
            row.creatorId to taskComment.creatorId
            row.content to taskComment.getContent()
            row.createdAt to taskComment.createdAt
            row.deleted to false
        }
        return generatedProcessId as Int
    }

    /**
     * Updates the given task comment.
     *
     * @return True if and only if the given task comment exists.
     */
    override fun updateTaskComment(taskComment: TaskComment): Boolean {
        val affectedRows = TaskCommentsTable.update { row ->
            row.content to taskComment.getContent()
            where {
                (TaskCommentsTable.id eq taskComment.id!!) and
                        (TaskCommentsTable.deleted notEq true)
            }
        }
        return affectedRows != 0
    }

    /**
     * Deletes the specified task comment.
     *
     * @return True if and only if the specified task comment existed.
     */
    override fun deleteTaskComment(taskCommentId: Int): Boolean {
        val affectedRows = TaskCommentsTable.update { row ->
            row.deleted to true
            where {
                (TaskCommentsTable.id eq taskCommentId) and
                        (TaskCommentsTable.deleted notEq true)
            }
        }
        return affectedRows != 0
    }
}
