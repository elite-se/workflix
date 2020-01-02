package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.persistence.daos.ProcessGroupDAO
import de.se.team3.webservice.containerInterfaces.ProcessGroupContainerInterface

object ProcessGroupContainer : ProcessGroupContainerInterface {

    override fun getAllProcessGroups(): List<ProcessGroup> {
        return ProcessGroupDAO.getAllProcessGroups()
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
