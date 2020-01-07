package de.se.team3.webservice.handlers

import de.se.team3.logic.container.UserRoleMembershipContainer
import de.se.team3.webservice.containerInterfaces.UserRoleMembershipContainerInfterface
import io.javalin.http.Context

/**
 * Handles requests to resources of form:
 * /userRoles/:userRoleId/members/:memberId
 */
object UserRolesMembersHandler {

    private val userRolesMembershipContainer: UserRoleMembershipContainerInfterface = UserRoleMembershipContainer

    /**
     * Handles requests for creating the membership of a user in a role.
     */
    fun create(ctx: Context, userRoleId: Int, memberId: String) {
        userRolesMembershipContainer.addUserToRole(memberId, userRoleId)
    }

    /**
     * Handles requests for deleting the membership of a user in a user role.
     */
    fun delete(ctx: Context, userRoleID: Int, memberId: String) {
        userRolesMembershipContainer.deleteUserFromRole(memberId, userRoleID)
    }
}
