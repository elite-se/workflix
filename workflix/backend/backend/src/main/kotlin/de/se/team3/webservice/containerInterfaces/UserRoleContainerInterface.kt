package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.UserRole

interface UserRoleContainerInterface {
    fun createUserRole(userRole: UserRole): Int

    fun deleteUserRole(userRoleID: Int)
}
