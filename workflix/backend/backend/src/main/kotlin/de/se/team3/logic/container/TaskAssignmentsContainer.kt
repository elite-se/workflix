package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.persistence.daos.TaskAssignmentsDAO
import de.se.team3.webservice.containerInterfaces.TaskAssignmentsContainerInterface

object TaskAssignmentsContainer : TaskAssignmentsContainerInterface {

    override fun createTaskAssignment(taskAssignment: TaskAssignment): Int {
        return TaskAssignmentsDAO.createTaskAssigment(taskAssignment)
    }

    override fun closeTaskAssignment(taskId: Int, assigneeId: String) {
        TaskAssignmentsDAO.closeTaskAssigment(taskId, assigneeId)
    }

    override fun deleteTaskAssignment(taskId: Int, assigneeId: String) {
        TaskAssignmentsDAO.deleteTaskAssigment(taskId, assigneeId)
    }
}
