package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.TaskAssignment
import java.time.Instant

interface TaskAssigmentsDAOInterface {

    fun createTaskAssigment(taskAssignment: TaskAssignment): Int

    fun closeTaskAssigment(taskId: Int, assigneeId: String, closingTime: Instant): Boolean

    fun deleteTaskAssigment(taskId: Int, assigneeId: String): Boolean
}
