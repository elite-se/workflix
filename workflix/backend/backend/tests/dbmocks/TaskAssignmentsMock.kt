package dbmocks

import de.se.team3.logic.DAOInterfaces.TaskAssigmentsDAOInterface
import de.se.team3.logic.domain.TaskAssignment
import java.time.Instant

object TaskAssignmentsMock: TaskAssigmentsDAOInterface {
    override fun createTaskAssignment(taskAssignment: TaskAssignment): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeTaskAssignment(taskId: Int, assigneeId: String, closingTime: Instant): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTaskAssignment(taskId: Int, assigneeId: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}