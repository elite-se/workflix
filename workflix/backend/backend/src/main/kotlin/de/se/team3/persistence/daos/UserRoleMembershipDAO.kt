package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.UserRoleMembershipDAOInterface
import de.se.team3.persistence.meta.UserRoleMembersTable
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey

object UserRoleMembershipDAO : UserRoleMembershipDAOInterface {

    /**
     * Creates the specified membership of a user in a user role.
     *
     * @return The generated id of the membership.
     */
    override fun addUserToRole(userID: String, userRoleID: Int): Int {
        val newId = UserRoleMembersTable.insertAndGenerateKey {
            it.userID to userID
            it.userRoleID to userRoleID
        }
        return newId as Int
    }

    /**
     * Deletes the specified membership of a user in a user role.
     *
     * @return True if and only if the specified user role existed.
     */
    override fun deleteUserFromRole(userID: String, userRoleID: Int): Boolean {
        val affectedRows = UserRoleMembersTable.delete {
            (it.userID eq userID) and
                    (it.userRoleID eq userRoleID)
        }
        return affectedRows != 0
    }
}
