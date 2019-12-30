package de.se.team3.logic.container

import de.se.team3.logic.domain.Task

object TasksContainer {

    fun getTask(taskId: Int): Task {
        val process = ProcessContainer.getProcessForTask(taskId)
        return process.findTask(taskId)
    }
}
