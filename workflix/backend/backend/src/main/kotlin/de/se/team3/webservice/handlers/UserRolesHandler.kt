package de.se.team3.webservice.handlers

import de.se.team3.logic.container.UserRoleContainer
import de.se.team3.logic.domain.UserRole
import io.javalin.http.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Handles requests to resources of form:
 * /usersRoles
 * /usersRoles/:userRoleId
 */
object UserRolesHandler {

    /**
     * Handles requests for all user roles.
     */
    fun getAll(ctx: Context) {
        val roles = UserRoleContainer.getAllUserRoles()

        val rolesArray = JSONArray(roles.map { it.toJSON() })
        val groupsJSON = JSONObject().put("roles", rolesArray)

        ctx.result(groupsJSON.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for creating a user role.
     */
    fun create(ctx: Context) {
        val content = ctx.body()
        val userRoleJsonObject = JSONObject(content)

        val name = userRoleJsonObject.getString("name")
        val description = userRoleJsonObject.getString("description")

        val userRole = UserRole(name, description)
        val newId = UserRoleContainer.createUserRole(userRole)

        val newIdObject = JSONObject()
        newIdObject.put("newId", newId)

        ctx.result(newIdObject.toString())
    }

    /**
     * Handles requests for updating a user role.
     */
    fun update(ctx: Context, userRoleID: Int) {
        val content = ctx.body()
        val userRoleJsonObject = JSONObject(content)

        val name = userRoleJsonObject.getString("name")
        val description = userRoleJsonObject.getString("description")

        val userRole = UserRole(userRoleID, name, description)
        UserRoleContainer.updateUserRole(userRole)
    }

    /**
     * Handles requests for deleting a user role.
     */
    fun delete(ctx: Context, userRoleID: Int) {
        UserRoleContainer.deleteUserRole(userRoleID)
    }
}
