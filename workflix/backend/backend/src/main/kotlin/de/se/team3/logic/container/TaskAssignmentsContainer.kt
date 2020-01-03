package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.TaskAssignmentsDAO
import de.se.team3.webservice.containerInterfaces.TaskAssignmentsContainerInterface
import java.time.Instant

object TaskAssignmentsContainer : TaskAssignmentsContainerInterface {

    /**
     * Creates a new task assignment.
     *
     * Note that if the doneAt property of taskAssignment is set the method tries to close the task
     * assignment immediately.
     *
     * @throws NotFoundException Is thrown if the task or user specified in taskAssignment does not exist.
     * @throws NotFoundException Is thrown if the predecessors of the task specified in taskAssignment are
     * not already closed and immediate closing is required.
     * @throws AlreadyExistsException Is thrown if the task is already closed.
     * @throws NotFoundException Is thrown if the user specified in taskAssignment does not exist.
     * @throws AlreadyExistsException Is thrown it hte task assignment already exists, i.d. there is already
     * an assignment of the task specified in taskAssignment to the user specified in taskAssignment.
     */
    override fun createTaskAssignment(taskAssignment: TaskAssignment): Int {
        val task = taskAssignment.task // throws not found exception

        if (task.isClosed())
            throw AlreadyExistsException("task is already closed")

        if (taskAssignment.closed() && task.isBlocked())
            throw NotFoundException("the assignment can only be closed if all predecessors have been closed")

        // TODO check user existence

        if (task.hasAssignmentTo(taskAssignment.assigneeId))
            throw AlreadyExistsException("task assignment already exists")

        val taskAssignmentId = TaskAssignmentsDAO.createTaskAssignment(taskAssignment)

        // add task assignment to the task
        val taskAssignmentWithId = taskAssignment.copy(id = taskAssignmentId)
        task.addTaskAssignment(taskAssignmentWithId)

        ProcessContainer.mayCloseProcess(task.process!!.id!!)
        return taskAssignmentId
    }

    /**
     * Closes the assignment specified by the given task and user id.
     *
     * @throws NotFoundException Is thrown if the predecessors of the given task are not already closed.
     * @throws AlreadyExistsException Is thrown if the given task is already closed.
     * @throws NotFoundException Is thrown if the specified task assignment does not exist.
     */
    override fun closeTaskAssignment(taskId: Int, assigneeId: String) {
        val task = TasksContainer.getTask(taskId)

        // check preconditions
        if (task.isBlocked())
            throw NotFoundException("the assignment can only be closed if all predecessors have been closed")
        if (task.isClosed())
            throw AlreadyExistsException("task is already closed")

        // close the assignment
        val closingTime = Instant.now()
        val existed = TaskAssignmentsDAO.closeTaskAssigment(taskId, assigneeId, closingTime)
        if (!existed)
            throw NotFoundException("task assignment does not exist")

        val taskAssignment = task.getTaskAssignment(assigneeId)
        taskAssignment.close(closingTime)

        ProcessContainer.mayCloseProcess(task.process!!.id!!)
    }

    /**
     * Deletes the task specified by the given task and user id.
     *
     * @throws NotFoundException Is thrown if the specified task assignment does not exist.
     */
    override fun deleteTaskAssignment(taskId: Int, assigneeId: String) {
        val existed = TaskAssignmentsDAO.deleteTaskAssignment(taskId, assigneeId)
        if (!existed)
            throw NotFoundException("task assignment does not exist")

        // delete task assignment from task
        val task = TasksContainer.getTask(taskId)
        task.deleteTaskAssignment(assigneeId)
    }
}
