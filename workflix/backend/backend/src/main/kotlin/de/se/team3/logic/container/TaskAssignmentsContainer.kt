package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.domain.TaskStatus
import de.se.team3.persistence.daos.ProcessDAO
import de.se.team3.persistence.daos.TaskAssignmentsDAO
import de.se.team3.persistence.meta.AlreadyExistsException
import de.se.team3.persistence.meta.NotFoundException
import de.se.team3.webservice.containerInterfaces.TaskAssignmentsContainerInterface

object TaskAssignmentsContainer : TaskAssignmentsContainerInterface {

    /**
     * Creates a new task.
     *
     * @throws AlreadyExistsException Is thrown if the task is already closed.
     */
    override fun createTaskAssignment(taskAssignment: TaskAssignment): Int {
        val task = TasksContainer.getTask(taskAssignment.taskId)

        if (task.status == TaskStatus.CLOSED)
            throw AlreadyExistsException("task is already closed")

        val taskAssignmentId = TaskAssignmentsDAO.createTaskAssigment(taskAssignment)
        mayCloseProcess(task.id!!)
        return taskAssignmentId
    }

    /**
     * Closes the assignment given by its task and assignee id.
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
        mayCloseProcess(taskId)
    }

    private fun mayCloseProcess(taskId: Int) {
        val process = ProcessContainer.getProcessForTask(taskId)
        if (process.closeable)
            ProcessDAO.closeProcess(process.id!!)
    }

    override fun deleteTaskAssignment(taskId: Int, assigneeId: String) {
        TaskAssignmentsDAO.deleteTaskAssigment(taskId, assigneeId)
    }
}
