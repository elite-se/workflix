package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.ProcessGroup

interface ProcessGroupDAOInterface {
    // TODO consider implementing addMember, getMembers, addProcess, getProcess

    fun getAllProcessGroups(offset: Int, limit: Int): Pair<List<ProcessGroup>, Int>

    fun getProcessGroup(processGroupId: Int): ProcessGroup

    fun createProcessGroup(processGroup: ProcessGroup): Int

    fun deleteProcessGroup(processGroupId: Int)
}
