package de.se.team3.logic.container

import de.se.team3.logic.domain.UserRole
import de.se.team3.persistence.daos.UserRoleDAO
import de.se.team3.webservice.containerInterfaces.UserRoleContainerInterface

object UserRoleContainer : UserRoleContainerInterface {
    override fun getAllUserRoles(): List<UserRole> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserRole(userRoleID: Int): UserRole {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
}
