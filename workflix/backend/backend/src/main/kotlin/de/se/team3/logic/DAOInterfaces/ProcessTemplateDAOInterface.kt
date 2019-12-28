package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.ProcessTemplate

interface ProcessTemplateDAOInterface {

    fun getAllProcessTemplates(): List<ProcessTemplate>

    fun getProcessTemplate(processTemplateId: Int): ProcessTemplate

    fun createProcessTemplate(processTemplate: ProcessTemplate): Int

    fun updateProcessTemplate(processTemplate: ProcessTemplate): Int?

    fun deleteProcessTemplate(processTemplateId: Int)
}
