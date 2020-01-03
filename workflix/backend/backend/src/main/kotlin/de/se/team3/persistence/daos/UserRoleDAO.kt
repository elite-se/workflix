package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.UserRoleDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.UserRole
import de.se.team3.persistence.meta.ProcessGroupMembers
import de.se.team3.persistence.meta.ProcessGroupsTable
import de.se.team3.persistence.meta.ProcessesTable
import de.se.team3.persistence.meta.UserRolesTable
import me.liuwj.ktorm.dsl.*

object UserRoleDAO : UserRoleDAOInterface {
    override fun getAllUserRoles(): List<UserRole> {
        val userRoleResult = UserRolesTable
            .select()

        val userRoles = ArrayList<UserRole>()

        for (row in userRoleResult) {
            //TODO: members
            val members = ArrayList<User>()
            //for (row in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq processGroupId }) {
            //    members.add(UserDAO.getUser(row[ProcessGroupMembers.userID]!!))
            //}

            val row = userRoleResult.rowSet.iterator().next()

            userRoles.add(UserRole(row[UserRolesTable.ID]!!,
                row[UserRolesTable.name]!!,
                row[UserRolesTable.description]!!,
                row[UserRolesTable.createdAt]!!,
                members))
        }

        return userRoles
    }

    override fun getUserRole(userRoleID: Int): UserRole {
        val userRoleResult = UserRolesTable
            .select()
            .where { UserRolesTable.ID eq userRoleID }

        //TODO: members
        val members = ArrayList<User>()
        //for (row in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq processGroupId }) {
        //    members.add(UserDAO.getUser(row[ProcessGroupMembers.userID]!!))
        //}

        val row = userRoleResult.rowSet.iterator().next()

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

    override fun deleteUserRole(userRoleID: Int) {
        val affectedRows = UserRolesTable.update {
            it.deleted to true
            where { it.ID eq userRoleID }
        }
        if (affectedRows == 0)
            throw NoSuchElementException()
    }
}
