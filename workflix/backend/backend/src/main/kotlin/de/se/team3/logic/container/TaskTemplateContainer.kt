package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.persistence.daos.TaskTemplateDAO

object TaskTemplateContainer {

    fun getTaskTemplate(taskTemplateId: Int): TaskTemplate {
        return TaskTemplateDAO.getTaskTemplate(taskTemplateId)
    }

}
