package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.TaskAssignment

interface TaskAssigmentsDAOInterface {

    fun createTaskAssigment(taskAssignment: TaskAssignment): Int

    fun closeTaskAssigment(taskId: Int, assigneeId: String)

    fun deleteTaskAssigment(taskId: Int, assigneeId: String)
}
