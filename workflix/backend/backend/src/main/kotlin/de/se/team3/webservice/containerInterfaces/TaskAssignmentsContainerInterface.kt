package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.TaskAssignment

interface TaskAssignmentsContainerInterface {

    fun createTaskAssignment(taskAssignment: TaskAssignment): Int

    fun closeTaskAssignment(taskAssignment: TaskAssignment)

    fun deleteTaskAssignment(taskAssignment: TaskAssignment)
}
