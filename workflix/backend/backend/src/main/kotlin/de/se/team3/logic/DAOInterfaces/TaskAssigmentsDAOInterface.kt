package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.TaskAssignment
import java.time.Instant

interface TaskAssigmentsDAOInterface {

    fun createTaskAssignment(taskAssignment: TaskAssignment): Int

    fun closeTaskAssignment(taskId: Int, assigneeId: String, closingTime: Instant): Boolean

    fun deleteTaskAssignment(taskId: Int, assigneeId: String): Boolean
}
