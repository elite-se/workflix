package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.ProcessGroupsMembershipDAOInterface
import de.se.team3.logic.domain.ProcessGroupMembership
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessGroupsMembershipDAO
import de.se.team3.webservice.containerInterfaces.ProcessGroupsMembershipContainerInterface

object ProcessGroupsMembershipContainer : ProcessGroupsMembershipContainerInterface {

    private val processGroupsMembershipsDAO: ProcessGroupsMembershipDAOInterface = ProcessGroupsMembershipDAO

    /**
     * Creates the given process group membership.
     *
     * @throws NotFoundException Is thrown if the process group specified in processMembership
     * does not exist.
     * @throws NotFoundException Is thrown if the user specified in processMembership does not exist.
     * @throws AlreadyExistsException Is thrown if the given membership already exists, i.d. if
     * the process group specified in processGroupMembership has the user specified in
     * processGroupMembership as member.
     */
    override fun createProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Int {
        val processGroup = processGroupMembership.processGroup // throws not found exception

        if (processGroup.isDeleted())
            throw NotFoundException("process group does not exist")
        if (!UserContainer.hasUser(processGroupMembership.memberId))
            throw NotFoundException("user does not exist")
        if (processGroup.hasMember(processGroupMembership.memberId))
            throw AlreadyExistsException("the membership already exists")

        val newId = processGroupsMembershipsDAO.createProcessGroupMembership(processGroupMembership)
        processGroup.addMember(processGroupMembership.member)
        return newId
    }

    /**
     * Deletes the specified process group membership.
     *
     * @throws NotFoundException Is thrown if the process group specified in processMembership
     * does not exist.
     * @throws NotFoundException Is thrown if the given process group membership does not exist.
     */
    override fun deleteProcessGroupMembership(processGroupMembership: ProcessGroupMembership) {
        val processGroup = processGroupMembership.processGroup

        if (processGroup.isDeleted())
            throw NotFoundException("process group does not exist")
        if (!processGroup.hasMember(processGroupMembership.memberId))
            throw NotFoundException("the membership does not exist")

        processGroupsMembershipsDAO.deleteProcessGroupMembership(processGroupMembership)
        processGroup.removeMember(processGroupMembership.memberId)
    }
}
