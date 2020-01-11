package dbmocks

import de.se.team3.logic.DAOInterfaces.TaskCommentsDAOInterface
import de.se.team3.logic.domain.TaskComment

object TaskCommentsMock: TaskCommentsDAOInterface {
    override fun getCreatorIdByTaskCommentId(taskCommentId: Int): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTaskIdByTaskCommentId(taskCommentId: Int): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createTaskComment(taskComment: TaskComment): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTaskComment(taskComment: TaskComment): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTaskComment(taskCommentId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}