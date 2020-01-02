package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.UserRole

interface UserRoleDAOInterface {
    fun createUserRole(userRole: UserRole): Int
}