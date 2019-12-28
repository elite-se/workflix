package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.TaskTemplate

interface TaskTemplateDAOInterface {

    fun getTaskTemplate(taskTemplateId: Int): TaskTemplate
}
