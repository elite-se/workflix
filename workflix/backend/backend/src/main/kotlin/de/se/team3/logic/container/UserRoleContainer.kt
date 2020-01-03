package de.se.team3.logic.container

import de.se.team3.logic.domain.UserRole
import de.se.team3.persistence.daos.UserRoleDAO
import de.se.team3.webservice.containerInterfaces.UserRoleContainerInterface

object UserRoleContainer : UserRoleContainerInterface {
    override fun getAllUserRoles(): List<UserRole> {
        return UserRoleDAO.getAllUserRoles()
    }

    override fun getUserRole(userRoleID: Int): UserRole {
        return UserRoleDAO.getUserRole(userRoleID)
    }

    override fun createUserRole(userRole: UserRole): Int {
        return UserRoleDAO.createUserRole(userRole)
    }

    override fun updateUserRole(userRole: UserRole) {
        UserRoleDAO.updateUserRole(userRole)
    }

    override fun deleteUserRole(userRoleID: Int) {
        return UserRoleDAO.deleteUserRole(userRoleID)
    }

    override fun addUserToRole(userID: String, userRoleID: Int) {
        return UserRoleDAO.addUserToRole(userID, userRoleID)
    }

    override fun deleteUserFromRole(userID: String, userRoleID: Int) {
        return UserRoleDAO.deleteUserFromRole(userID, userRoleID)
    }
}
