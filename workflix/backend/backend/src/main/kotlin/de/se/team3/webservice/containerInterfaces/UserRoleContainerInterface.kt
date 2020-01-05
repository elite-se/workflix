package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.UserRole

interface UserRoleContainerInterface {

    fun getAllUserRoles(): List<UserRole>

    fun getUserRole(userRoleID: Int): UserRole

    fun createUserRole(userRole: UserRole): Int

    fun updateUserRole(userRole: UserRole)

    fun deleteUserRole(userRoleID: Int)
}
