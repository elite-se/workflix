package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.persistence.daos.ProcessTemplateDAO
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface

object ProcessTemplateContainer: ProcessTemplateContainerInterface {

    const val PAGESIZE = 20

    override fun getProcessTemplates(page: Int): Pair<List<ProcessTemplate>, Int> {
        if (page < 1)
            throw IllegalArgumentException()

        val offset = (page - 1) * ProcessTemplateContainer.PAGESIZE
        val limit = ProcessTemplateContainer.PAGESIZE

        val result = ProcessTemplateDAO.getAllProcessTemplates(offset, limit)

        val lastPage = result.second / ProcessTemplateContainer.PAGESIZE + 1
        return Pair(result.first, lastPage)
    }



}