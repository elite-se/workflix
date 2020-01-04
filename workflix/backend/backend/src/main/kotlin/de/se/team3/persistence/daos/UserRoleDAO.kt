package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.UserRoleDAOInterface
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.UserRole
import de.se.team3.persistence.meta.UserRoleMembersTable
import de.se.team3.persistence.meta.UserRolesTable
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

object UserRoleDAO : UserRoleDAOInterface {
    override fun getAllUserRoles(): List<UserRole> {
        val userRoleResult = UserRolesTable
            .select()

        val userRoles = ArrayList<UserRole>()

        for (roleRow in userRoleResult) {
            val members = ArrayList<User>()
            for (memberRow in UserRoleMembersTable.select().where { UserRoleMembersTable.userRoleID eq roleRow[UserRolesTable.ID]!! }) {
                members.add(UserContainer.getUser(memberRow[UserRoleMembersTable.userID]!!))
            }

            userRoles.add(UserRole(roleRow[UserRolesTable.ID]!!,
                roleRow[UserRolesTable.name]!!,
                roleRow[UserRolesTable.description]!!,
                roleRow[UserRolesTable.createdAt]!!,
                members))
        }

        return userRoles
    }

    override fun getUserRole(userRoleID: Int): UserRole? {
        val userRoleResult = UserRolesTable
            .select()
            .where { UserRolesTable.ID eq userRoleID }

        val members = ArrayList<User>()
        for (row in UserRoleMembersTable.select().where { UserRoleMembersTable.userRoleID eq userRoleID }) {
            members.add(UserContainer.getUser(row[UserRoleMembersTable.userID]!!))
        }

        val row = userRoleResult.rowSet
        if (!row.next())
            return null

        return UserRole(row[UserRolesTable.ID]!!,
            row[UserRolesTable.name]!!,
            row[UserRolesTable.description]!!,
            row[UserRolesTable.createdAt]!!,
            members)
    }

    override fun createUserRole(userRole: UserRole): Int {
        return UserRolesTable.insertAndGenerateKey {
            it.name to userRole.name
            it.description to userRole.description
            it.createdAt to userRole.createdAt
            it.deleted to false
        } as Int
    }

    override fun updateUserRole(userRole: UserRole) {
        UserRolesTable.update {
            it.name to userRole.name
            it.description to userRole.description

            where { it.ID eq userRole.id }
        }
    }

    override fun deleteUserRole(userRoleID: Int): Boolean {
        return UserRolesTable.update {
            it.deleted to true
            where { it.ID eq userRoleID }
        } != 0
    }

    override fun addUserToRole(userID: String, userRoleID: Int) {
        UserRoleMembersTable.insertAndGenerateKey {
            it.userID to userID
            it.userRoleID to userRoleID
        }
    }

    override fun deleteUserFromRole(userID: String, userRoleID: Int) {
        UserRoleMembersTable.delete {
            (it.userID eq userID) and (it.userRoleID eq userRoleID)
        }
    }
}
