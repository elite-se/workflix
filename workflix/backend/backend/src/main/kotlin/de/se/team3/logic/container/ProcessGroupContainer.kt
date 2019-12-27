package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.persistence.daos.ProcessGroupDAO
import de.se.team3.webservice.containerInterfaces.ProcessGroupContainerInterface
import java.lang.IllegalArgumentException

object ProcessGroupContainer : ProcessGroupContainerInterface {

    const val PAGESIZE = 20

    override fun getAllProcessGroups(page: Int): Pair<List<ProcessGroup>, Int> {
        if (page < 1)
            throw IllegalArgumentException()

        val offset = (page - 1) * ProcessGroupContainer.PAGESIZE
        val limit = ProcessGroupContainer.PAGESIZE

        val result = ProcessGroupDAO.getAllProcessGroups(offset, limit)

        val lastPage = result.second / ProcessGroupContainer.PAGESIZE + 1
        if (page > lastPage)
            throw IllegalArgumentException()

        return Pair(result.first, lastPage)
    }

    override fun getProcessGroup(processGroupId: Int): ProcessGroup {
        return ProcessGroupDAO.getProcessGroup(processGroupId)
    }

    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        return ProcessGroupDAO.createProcessGroup(processGroup)
    }

    override fun deleteProcessGroup(processGroupId: Int) {
        return ProcessGroupDAO.deleteProcessGroup(processGroupId)
    }
}
