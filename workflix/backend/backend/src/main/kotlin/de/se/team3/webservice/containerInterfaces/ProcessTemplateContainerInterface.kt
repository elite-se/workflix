package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.ProcessTemplate

interface ProcessTemplateContainerInterface {

    fun getAllProcessTemplates(): List<ProcessTemplate>

    fun getProcessTemplate(processTemplateId: Int): ProcessTemplate

    fun createProcessTemplate(processTemplate: ProcessTemplate): Int

    fun updateProcessTemplate(processTemplate: ProcessTemplate): Int

    fun deleteProcessTemplate(processTemplateId: Int)
}
