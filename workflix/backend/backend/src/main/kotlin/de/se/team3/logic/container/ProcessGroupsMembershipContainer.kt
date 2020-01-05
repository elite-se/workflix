package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessGroupMembership
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessGroupsMembershipDAO
import de.se.team3.webservice.containerInterfaces.ProcessGroupsMembershipContainerInterface

object ProcessGroupsMembershipContainer : ProcessGroupsMembershipContainerInterface {

    /**
     * Creates the given process group membership.
     *
     * @throws NotFoundException Is thrown if the user specified in processMembership does not exist.
     * @throws AlreadyExistsException Is thrown if the given membership already exists, i.d. if
     * the process group specified in processGroupMembership has the user specified in
     * processGroupMembership as member.
     */
    override fun createProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Int {
        val processGroup = processGroupMembership.processGroup // throws not found exception

        if (!UserContainer.hasUser(processGroupMembership.memberId))
            throw NotFoundException("the user does not exist")
        if (processGroup.hasMember(processGroupMembership.memberId))
            throw AlreadyExistsException("the membership already exists")

        val newId = ProcessGroupsMembershipDAO.createProcessGroupMembership(processGroupMembership)
        return newId
    }

    /**
     * Deletes the specified process group membership.
     *
     * @throws NotFoundException Is thrown if the given process group membership does not exist.
     */
    override fun deleteProcessGroupMembership(processGroupMembership: ProcessGroupMembership) {
        val processGroup = processGroupMembership.processGroup

        if (!processGroup.hasMember(processGroupMembership.memberId))
            throw NotFoundException("the membership does not exist")

        ProcessGroupsMembershipDAO.deleteProcessGroupMembership(processGroupMembership)
    }
}
