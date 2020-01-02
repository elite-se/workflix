package de.se.team3.logic.container

import de.se.team3.logic.domain.UserRole
import de.se.team3.persistence.daos.UserRoleDAO
import de.se.team3.webservice.containerInterfaces.UserRoleContainerInterface

object UserRoleContainer : UserRoleContainerInterface {
    override fun createUserRole(userRole: UserRole): Int {
        return UserRoleDAO.createUserRole(userRole)
    }
}
