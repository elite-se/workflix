package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupsMembershipContainer
import de.se.team3.logic.container.UserRoleMembershipContainer
import de.se.team3.logic.domain.ProcessGroupMembership
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
    fun create(ctx: Context) {
        val content = ctx.body()
        val relationshipJSON = JSONObject(content)

        val userID = relationshipJSON.getString("userId")
        val userRoleID = relationshipJSON.getInt("userRoleId")

        UserRoleMembershipContainer.addUserToRole(userID, userRoleID)
    }

    /**
     * Handles requests for deleting the membership of a user in a user role.
     */
    fun delete(ctx: Context, userID: String, userRoleID: Int) {
        UserRoleMembershipContainer.deleteUserFromRole(userID, userRoleID)
    }

}