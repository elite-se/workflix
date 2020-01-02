package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskComment
import de.se.team3.persistence.daos.TaskCommentsDAO
import de.se.team3.webservice.containerInterfaces.TaskCommentsContainerInterface

object TaskCommentsContainer : TaskCommentsContainerInterface {

    override fun createTaskComment(taskComment: TaskComment): Int {
        return TaskCommentsDAO.createTaskComment(taskComment)
    }

    override fun updateTaskComment(taskComment: TaskComment) {
        TaskCommentsDAO.updateTaskComment(taskComment)
    }

    override fun deleteTaskComment(taskCommentId: Int) {
        TaskCommentsDAO.deleteTaskComment(taskCommentId)
    }
}
