package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.container.UserRoleContainer
import de.se.team3.logic.domain.UserRole
import io.javalin.http.Context
import org.json.JSONArray
import java.util.NoSuchElementException
import org.json.JSONException
import org.json.JSONObject

object UserRoleHandler {
    fun getAll(ctx: Context) {
        try {
            val roles = UserRoleContainer.getAllUserRoles()

            val rolesArray = JSONArray(roles.map { it.toJson() })
            val groupsJSON = JSONObject().put("roles", rolesArray)

            ctx.result(groupsJSON.toString())
                .contentType("application/json")
        } catch (e: IllegalArgumentException) {
            ctx.status(404).result("page not found")
        }
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
        try {
            val content = ctx.body()
            val userRoleJsonObject = JSONObject(content)

            val name = userRoleJsonObject.getString("name")
            val description = userRoleJsonObject.getString("description")

            val role = UserRoleContainer.getUserRole(userRoleID)
            role.name = name
            role.description = description
        } catch (e: JSONException) {
            ctx.status(400).result(e.toString())
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user role not found")
        }
    }

    fun delete(ctx: Context, userRoleID: Int) {
        try {
            UserRoleContainer.deleteUserRole(userRoleID)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user role not found")
        }
    }

    fun addUserToRole(ctx: Context) {
        try {
            val content = ctx.body()
            val relationshipJSON = JSONObject(content)

            val userID = relationshipJSON.getString("userId")
            val userRoleID = relationshipJSON.getInt("userRoleId")
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user role or user not found")
        }
    }

    fun deleteUserFromRole(ctx: Context, userID: String, userRoleID: Int) {
        try {
            UserRoleContainer.deleteUserFromRole(userID, userRoleID)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user role or user not found")
        }
    }
}
