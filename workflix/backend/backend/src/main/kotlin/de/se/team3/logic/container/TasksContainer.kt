package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.TasksDAOInterface
import de.se.team3.logic.domain.Task
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.TasksDAO
import de.se.team3.webservice.containerInterfaces.TasksContainerInterface

object TasksContainer : TasksContainerInterface {

    private val tasksDAO: TasksDAOInterface = TasksDAO

    /**
     * Returns the specified task.
     *
     * @throws NotFoundException Is thrown if the specified task does not exist.
     */
    override fun getTask(taskId: Int): Task {
        val processId = tasksDAO.getProcessIdForTask(taskId)
            ?: throw NotFoundException("task not found") // a process does not exist only if the task does not exist

        val process = ProcessContainer.getProcess(processId)
        return process.getTask(taskId)
    }
}
