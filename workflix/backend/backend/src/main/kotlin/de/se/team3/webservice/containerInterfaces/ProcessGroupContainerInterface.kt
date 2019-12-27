package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.ProcessGroup

interface ProcessGroupContainerInterface {

    fun getAllProcessGroups(page: Int): Pair<List<ProcessGroup>, Int>

    fun getProcessGroup(processGroupId: Int): ProcessGroup

    fun createProcessGroup(processGroup: ProcessGroup): Int

    fun deleteProcessGroup(processGroupId: Int)
}
