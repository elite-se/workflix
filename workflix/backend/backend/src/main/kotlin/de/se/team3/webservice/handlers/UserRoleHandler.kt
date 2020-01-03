package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupContainer
import de.se.team3.logic.container.UserRoleContainer
import de.se.team3.logic.domain.UserRole
import io.javalin.http.Context
import org.json.JSONException
import org.json.JSONObject
import java.util.NoSuchElementException

object UserRoleHandler {
    fun getAllRoles(ctx: Context, page: Int) {
        TODO()
    }

    fun create(ctx: Context) {
        try {
            val content = ctx.body()
            val userRoleJsonObject = JSONObject(content)

            val name = userRoleJsonObject.getString("name")
            val description = userRoleJsonObject.getString("description")

            try {
                val userRole = UserRole(name, description)
                val newId = UserRoleContainer.createUserRole(userRole)
                userRole.id = newId
                val newIdObject = JSONObject()
                newIdObject.put("newId", newId)

                ctx.result(newIdObject.toString())
            } catch (e: IllegalArgumentException) {
                ctx.status(400).result(e.toString())
            }
        } catch (e: JSONException) {
            ctx.status(400).result(e.toString())
        }
    }

    fun update(ctx: Context, userRoleID: Int) {
        TODO()
    }

    fun delete(ctx: Context, userRoleID: Int) {
        try {
            UserRoleContainer.deleteUserRole(userRoleID)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user role not found")
        }
    }

    fun addUserToRole(ctx: Context) {
        TODO()
    }

    fun deleteUserFromRole(ctx: Context, userID: String, userRoleID: Int) {
        TODO()
    }
}
