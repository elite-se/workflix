package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessGroupMembership
import de.se.team3.webservice.containerInterfaces.ProcessGroupsMembershipContainerInterface

object ProcessGroupsMembershipContainer : ProcessGroupsMembershipContainerInterface {

    override fun createProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Int {
        val processGroup = processGroupMembership.processGroup // throws not found exception

        // TODO check user existence

        if (processGroup.hasMember(processGroupMembership.memberId))



        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteProcessGroupMembership(processGroupId: Int, memberId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}