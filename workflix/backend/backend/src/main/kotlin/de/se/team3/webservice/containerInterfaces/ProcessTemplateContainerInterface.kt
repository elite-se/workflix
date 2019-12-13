package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.ProcessTemplate

interface ProcessTemplateContainerInterface {

    fun getAllProcessTemplates(page: Int): Pair<List<ProcessTemplate>, Int>

    fun getProcessTemplate(templateId: Int): ProcessTemplate

}