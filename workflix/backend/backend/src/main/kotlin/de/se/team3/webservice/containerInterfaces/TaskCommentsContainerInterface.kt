package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.TaskComment

interface TaskCommentsContainerInterface {

    fun createTaskComment(taskComment: TaskComment): Int

    fun updateTaskComment(taskComment: TaskComment)

    fun deleteTaskComment(taskCommentId: Int)
}
