package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.UserRole

interface UserRoleDAOInterface {
    fun getAllUserRoles(): List<UserRole>

    fun getUserRole(userRoleID: Int): UserRole

    fun createUserRole(userRole: UserRole): Int

    fun updateUserRole(userRole: UserRole)

    fun deleteUserRole(userRoleID: Int)
}
