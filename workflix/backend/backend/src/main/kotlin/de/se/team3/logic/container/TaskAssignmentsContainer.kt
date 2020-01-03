package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.exceptions.AlreadyClosedException
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
     * @throws AlreadyClosedException Is thrown if the task is already closed.
     * @throws NotFoundException Is thrown if the predecessors of the task specified in taskAssignment are
     * not already closed and immediate closing is required.
     * @throws NotFoundException Is thrown if the user specified in taskAssignment does not exist.
     * @throws AlreadyExistsException Is thrown it hte task assignment already exists, i.d. there is already
     * an assignment of the task specified in taskAssignment to the user specified in taskAssignment.
     */
    override fun createTaskAssignment(taskAssignment: TaskAssignment): Int {
        val task = taskAssignment.task // throws not found exception

        if (task.isClosed())
            throw AlreadyClosedException("task is already closed")
        if (taskAssignment.isClosed() && task.isBlocked())
            throw NotFoundException("the assignment can only be closed if all predecessors have been closed")

        // TODO check user existence

        if (task.hasAssignment(taskAssignment.assigneeId))
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
     * Note that a task assignment that belongs to a closed task could not be closed even
     * if the task assignment itself is not closed.
     *
     * @throws AlreadyClosedException Is thrown if the specified assignment is already closed.
     * @throws NotFoundException Is thrown if the predecessors of the given task are not already closed.
     * @throws AlreadyClosedException Is thrown if the given task is already closed.
     * @throws NotFoundException Is thrown if the specified task assignment does not exist.
     */
    override fun closeTaskAssignment(taskId: Int, assigneeId: String) {
        val task = TasksContainer.getTask(taskId) // throws NotFound

        // check preconditions
        val taskAssignment = task.getTaskAssignment(assigneeId) // throws NotFound
        if (taskAssignment.isClosed())
            throw AlreadyClosedException("task assignment is already closed")
        if (task.isBlocked())
            throw NotFoundException("the assignment can only be closed if all predecessors have been closed")
        if (task.isClosed())
            throw AlreadyClosedException("task is already closed")

        // close the assignment
        val closingTime = Instant.now()
        val existed = TaskAssignmentsDAO.closeTaskAssignment(taskId, assigneeId, closingTime)
        if (!existed)
            throw NotFoundException("task assignment does not exist")

        taskAssignment.close(closingTime)

        ProcessContainer.mayCloseProcess(task.process!!.id!!)
    }

    /**
     * Deletes the task specified by the given task and user id.
     *
     * Note that a task assignment that belongs to a closed task could not be deleted even
     * if the task assignment itself is not closed.
     *
     * @throws AlreadyClosedException Is thrown if the specified task assignment is already closed.
     * @throws AlreadyClosedException Is thrown if the specified task is already closed.
     * @throws NotFoundException Is thrown if the specified task assignment does not exist.
     */
    override fun deleteTaskAssignment(taskId: Int, assigneeId: String) {
        val task = TasksContainer.getTask(taskId)

        val taskAssignment = task.getTaskAssignment(assigneeId)
        if (taskAssignment.isClosed())
            throw AlreadyClosedException("task assignment is already closed")
        if (task.isClosed())
            throw AlreadyClosedException("task is already closed")

        val existed = TaskAssignmentsDAO.deleteTaskAssignment(taskId, assigneeId)
        if (!existed)
            throw NotFoundException("task assignment does not exist")

        // delete task assignment from task
        task.deleteTaskAssignment(assigneeId)
    }
}
