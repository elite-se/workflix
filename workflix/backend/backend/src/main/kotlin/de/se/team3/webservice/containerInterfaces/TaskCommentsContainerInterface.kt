package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskComment
import de.se.team3.logic.domain.User

interface TaskCommentsContainerInterface {

    fun getTaskByTaskCommentId(taskCommentId: Int): Task

    fun getCreatorByTaskCommentId(taskCommentId: Int): User

    fun createTaskComment(taskComment: TaskComment): Int

    fun updateTaskComment(taskComment: TaskComment)

    fun deleteTaskComment(taskCommentId: Int)

}
