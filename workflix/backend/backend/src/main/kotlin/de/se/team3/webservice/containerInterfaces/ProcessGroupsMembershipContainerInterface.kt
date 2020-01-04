package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.ProcessGroupMembership

interface ProcessGroupsMembershipContainerInterface {

    fun createProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Int

    fun deleteProcessGroupMembership(processGroupId: Int, memberId: String)
}
