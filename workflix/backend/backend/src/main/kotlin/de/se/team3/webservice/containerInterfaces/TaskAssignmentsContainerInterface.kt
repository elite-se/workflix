package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.TaskAssignment

interface TaskAssignmentsContainerInterface {

    fun createTaskAssignment(taskAssignment: TaskAssignment): Int

    fun closeTaskAssignment(taskId: Int, assigneeId: String)

    fun deleteTaskAssignment(taskId: Int, assigneeId: String)
}
