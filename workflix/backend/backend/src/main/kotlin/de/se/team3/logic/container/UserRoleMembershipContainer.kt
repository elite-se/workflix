package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.UserRoleMembershipDAOInterface
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.UserRoleMembershipDAO
import de.se.team3.webservice.containerInterfaces.UserRoleMembershipContainerInfterface

object UserRoleMembershipContainer : UserRoleMembershipContainerInfterface {

    private val userRoleMembershipDAO: UserRoleMembershipDAOInterface = UserRoleMembershipDAO

    /**
     * Creates the specified membership of a user in a user role.
     *
     * @throws NotFoundException Is thrown if the specified user role does not exist.
     * @throws AlreadyExistsException Is thrown if the specified membership already exists.
     */
    override fun addUserToRole(userID: String, userRoleID: Int) {
        val userRole = UserRoleContainer.getUserRole(userRoleID) // throws not found exception
        val user = UserContainer.getUser(userID) // throws NotFoundException

        if (userRole.isDeleted())
            throw NotFoundException("user role does not exist")
        if (userRole.hasMember(user.id))
            throw AlreadyExistsException("the membership already exists")

        userRoleMembershipDAO.addUserToRole(userID, userRoleID)

        userRole.addMember(user)
    }

    /**
     * Deletes the specified membership of a user in a user role.
     *
     * @throws NotFoundException Is thrown if the specified user role does not exist.
     * @throws NotFoundException Is thrown if the specified membership does not exist.
     */
    override fun deleteUserFromRole(userID: String, userRoleID: Int) {
        val userRole = UserRoleContainer.getUserRole(userRoleID)
        val user = UserContainer.getUser(userID)

        if (userRole.isDeleted())
            throw NotFoundException("user role does not exist")

        val existed = userRoleMembershipDAO.deleteUserFromRole(userID, userRoleID)
        if (!existed)
            throw NotFoundException("membership does not exist")

        userRole.removeMember(user.id)
    }
}
