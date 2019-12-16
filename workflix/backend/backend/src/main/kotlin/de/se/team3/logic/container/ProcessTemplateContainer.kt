package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.persistence.daos.ProcessTemplateDAO
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface
import java.lang.IllegalArgumentException

object ProcessTemplateContainer: ProcessTemplateContainerInterface {

    const val PAGESIZE = 20

    override fun getAllProcessTemplates(page: Int): Pair<List<ProcessTemplate>, Int> {
        if (page < 1)
            throw IllegalArgumentException()

        val offset = (page - 1) * ProcessTemplateContainer.PAGESIZE
        val limit = ProcessTemplateContainer.PAGESIZE

        val result = ProcessTemplateDAO.getAllProcessTemplates(offset, limit)

        val lastPage = result.second / ProcessTemplateContainer.PAGESIZE + 1
        if (page > lastPage)
            throw IllegalArgumentException()

        return Pair(result.first, lastPage)
    }

    override fun getProcessTemplate(templateId: Int): ProcessTemplate {
        return ProcessTemplateDAO.getProcessTemplate(templateId)
    }

    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        return ProcessTemplateDAO.createProcessTemplate(processTemplate)
    }

    override fun deleteProcessTemplate(processTemplateId: Int) {
        return ProcessTemplateDAO.deleteProcessTemplate(processTemplateId)
    }

}