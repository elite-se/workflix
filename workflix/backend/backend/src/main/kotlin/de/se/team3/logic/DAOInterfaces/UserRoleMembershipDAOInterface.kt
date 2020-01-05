package de.se.team3.logic.DAOInterfaces

interface UserRoleMembershipDAOInterface {

    fun addUserToRole(userID: String, userRoleID: Int): Int

    fun deleteUserFromRole(userID: String, userRoleID: Int): Boolean

}