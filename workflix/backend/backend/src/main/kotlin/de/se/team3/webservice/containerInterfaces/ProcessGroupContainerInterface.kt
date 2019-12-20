package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.ProcessTemplate

interface ProcessGroupContainerInterface {

    fun getAllProcessGroups(page: Int): Pair<List<ProcessGroup>, Int>

    fun getProcessGroup(processGroupId: Int): ProcessTemplate

    fun createProcessGroup(processGroup: ProcessGroup): Int

    fun deleteProcessGroup(processGroupId: Int)
}
