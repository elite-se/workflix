package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.UserRoleDAOInterface
import de.se.team3.logic.domain.UserRole
import de.se.team3.persistence.meta.UserRolesTable
import me.liuwj.ktorm.dsl.insertAndGenerateKey

object UserRoleDAO: UserRoleDAOInterface {
    override fun createUserRole(userRole: UserRole): Int {
        return UserRolesTable.insertAndGenerateKey {
            it.name to userRole.name
            it.description to userRole.description
            it.createdAt to userRole.createdAt
            it.deleted to false
        } as Int
    }
}