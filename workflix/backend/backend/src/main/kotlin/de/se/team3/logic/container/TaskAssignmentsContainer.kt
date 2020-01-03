package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.domain.TaskStatus
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.TaskAssignmentsDAO
import de.se.team3.webservice.containerInterfaces.TaskAssignmentsContainerInterface

object TaskAssignmentsContainer : TaskAssignmentsContainerInterface {

    /**
     * Creates a new task assignment.
     *
     * @throws AlreadyExistsException Is thrown if the task is already closed.
     */
    override fun createTaskAssignment(taskAssignment: TaskAssignment): Int {
        val task = taskAssignment.task

        if (task.status == TaskStatus.CLOSED)
            throw AlreadyExistsException("task is already closed")

        val taskAssignmentId = TaskAssignmentsDAO.createTaskAssigment(taskAssignment)

        // refresh process
        val processId = task.process!!.id!!
        ProcessContainer.refreshCachedProcess(processId)

        ProcessContainer.mayCloseProcess(task.process!!.id!!)
        return taskAssignmentId
    }

    /**
     * Closes the assignment specified by the given task and user id.
     *
     * @throws NotFoundException Is thrown if the predecessors of the given task are not already closed.
     * @throws AlreadyExistsException Is thrown if the given task is already closed.
     */
    override fun closeTaskAssignment(taskId: Int, assigneeId: String) {
        val task = TasksContainer.getTask(taskId)

        if (task.status == TaskStatus.BLOCKED)
            throw NotFoundException("the assignment can only be closed if all predecessors have been closed")
        if (task.status == TaskStatus.CLOSED)
            throw AlreadyExistsException("task is already closed")

        TaskAssignmentsDAO.closeTaskAssigment(taskId, assigneeId)

        // refresh process
        val processId = task.process!!.id!!
        ProcessContainer.refreshCachedProcess(processId)

        ProcessContainer.mayCloseProcess(task.process!!.id!!)
    }

    /**
     * Deletes the task specified by the given task and user id.
     */
    override fun deleteTaskAssignment(taskId: Int, assigneeId: String) {
        TaskAssignmentsDAO.deleteTaskAssigment(taskId, assigneeId)

        // refresh process
        val task = TasksContainer.getTask(taskId)
        val processId = task.process!!.id!!
        ProcessContainer.refreshCachedProcess(processId)
    }
}
