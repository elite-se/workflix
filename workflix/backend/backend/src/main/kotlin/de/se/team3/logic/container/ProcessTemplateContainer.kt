package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.persistence.daos.ProcessTemplateDAO
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface

object ProcessTemplateContainer : ProcessTemplateContainerInterface {

    override fun getAllProcessTemplates(): List<ProcessTemplate> {
        return ProcessTemplateDAO.getAllProcessTemplates()
    }

    override fun getProcessTemplate(templateId: Int): ProcessTemplate {
        return ProcessTemplateDAO.getProcessTemplate(templateId)
    }

    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        return ProcessTemplateDAO.createProcessTemplate(processTemplate)
    }

    override fun updateProcessTemplate(processTemplate: ProcessTemplate): Int? {
        return ProcessTemplateDAO.updateProcessTemplate(processTemplate)
    }

    override fun deleteProcessTemplate(processTemplateId: Int) {
        return ProcessTemplateDAO.deleteProcessTemplate(processTemplateId)
    }
}
