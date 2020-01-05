package de.se.team3.webservice.handlers

import de.se.team3.logic.container.UserRoleMembershipContainer
import io.javalin.http.Context
import org.json.JSONObject

/**
 * Handles requests to resources of form:
 * /userRoles/:userRoleId/members/:memberId
 */
object UserRolesMembersHandler {

    /**
     * Handles requests for creating the membership of a user in a role.
     */
    fun create(ctx: Context, userRoleId: Int, memberId: String) {
        UserRoleMembershipContainer.addUserToRole(memberId, userRoleId)
    }

    /**
     * Handles requests for deleting the membership of a user in a user role.
     */
    fun delete(ctx: Context, userRoleID: Int, memberId: String) {
        UserRoleMembershipContainer.deleteUserFromRole(memberId, userRoleID)
    }

}