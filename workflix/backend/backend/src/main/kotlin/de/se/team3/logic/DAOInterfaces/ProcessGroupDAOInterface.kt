package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.ProcessGroup

interface ProcessGroupDAOInterface {

    fun getAllProcessGroups(): List<ProcessGroup>

    fun getProcessGroup(processGroupId: Int): ProcessGroup?

    fun createProcessGroup(processGroup: ProcessGroup): Int

    fun updateProcessGroup(processGroup: ProcessGroup): Boolean

    fun deleteProcessGroup(processGroupId: Int): Boolean
}
