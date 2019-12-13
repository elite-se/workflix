package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.ProcessTemplate

interface ProcessTemplateDAOInterface {

    fun getAllProcessTemplates(offset: Int, limit: Int): Pair<List<ProcessTemplate>, Int>

    fun getProcessTemplate(templateId: Int): ProcessTemplate

}