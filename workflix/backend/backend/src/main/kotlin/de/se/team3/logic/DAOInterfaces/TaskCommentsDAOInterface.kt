package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.TaskComment

interface TaskCommentsDAOInterface {

    fun getTaskIdByTaskCommentId(taskCommentId: Int): Int?

    fun createTaskComment(taskComment: TaskComment): Int

    fun updateTaskComment(taskComment: TaskComment)

    fun deleteTaskComment(taskCommentId: Int)

}
