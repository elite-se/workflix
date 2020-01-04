package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.ProcessGroupMembership

interface ProcessGroupsMembershipDAOInterface {

    fun createProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Int

    fun deleteProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Boolean

}