package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.ProcessGroup

interface ProcessGroupContainerInterface {

    fun getAllProcessGroups(): List<ProcessGroup>

    fun getProcessGroup(processGroupId: Int): ProcessGroup

    fun createProcessGroup(processGroup: ProcessGroup): Int

    fun updateProcessGroup(processGroup: ProcessGroup)

    fun deleteProcessGroup(processGroupId: Int)

}
