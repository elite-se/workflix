package de.se.team3.logic.container

import de.se.team3.logic.domain.Task
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.TasksDAO

object TasksContainer {

    /**
     * Returns the specified task.
     *
     * @throws NotFoundException Is thrown if the specified task does not exist.
     */
    fun getTask(taskId: Int): Task {
        val processId = TasksDAO.getProcessIdForTask(taskId)
            ?: throw NotFoundException("task not found") // a process does not exist only if the task does not exist

        val process = ProcessContainer.getProcess(processId)
        return process.findTask(taskId)
    }
}
