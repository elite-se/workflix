package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskComment
import de.se.team3.webservice.containerInterfaces.TaskCommentsContainerInterface

object TaskCommentsContainer : TaskCommentsContainerInterface {

    override fun createTaskComment(taskComment: TaskComment): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTaskComment(taskComment: TaskComment) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTaskComment(taskCommentId: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
