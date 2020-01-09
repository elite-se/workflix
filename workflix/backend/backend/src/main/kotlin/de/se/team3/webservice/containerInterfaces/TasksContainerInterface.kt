package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.Task

interface TasksContainerInterface {

    fun getTask(taskId: Int): Task

}