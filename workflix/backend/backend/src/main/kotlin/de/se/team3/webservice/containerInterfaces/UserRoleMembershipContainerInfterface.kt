package de.se.team3.webservice.containerInterfaces

interface UserRoleMembershipContainerInfterface {

    fun addUserToRole(userID: String, userRoleID: Int)

    fun deleteUserFromRole(userID: String, userRoleID: Int)

}