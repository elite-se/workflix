package de.se.team3.webservice.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.se.team3.logic.container.UserContainer
import io.javalin.http.Context
import java.lang.IllegalArgumentException
import org.json.JSONArray
import org.json.JSONObject

object UserHandler {

    val mapper = ObjectMapper().registerModule(KotlinModule())

    fun getAll(ctx: Context, page: Int) {
        try {
            val users = UserContainer.getAllUsers()

            val userArray = JSONArray(users)
            val usersJSON = JSONObject(userArray)

            ctx.result(usersJSON.toString())
                .contentType("application/json")
        } catch (e: IllegalArgumentException) {
            ctx.status(404).result("page not found")
        }
    }

    fun getOne(ctx: Context, userID: String) {
        try {
            val content = ctx.body()
            val contentJSON = JSONObject(content)

            val userID = contentJSON.getString("userId")
            val user = UserContainer.getUser(userID)
            val userJSON = JSONObject(mapper.writeValueAsString(user))

            ctx.result(userJSON.toString()).contentType("application/json")
        } catch (e: IllegalArgumentException) {
            ctx.status(404).result("user not found")
        }
    }

    fun getAllRoles(ctx: Context, page: Int) {
        TODO()
    }

    fun createRole(ctx: Context) {
        TODO()
    }

    fun updateRole(ctx: Context, userRoleID: Int) {
        TODO()
    }

    fun deleteRole(ctx: Context, userRoleID: Int) {
        TODO()
    }

    fun addUserToRole(ctx: Context) {
        TODO()
    }

    fun deleteUserFromRole(ctx: Context, userID: String, userRoleID: Int) {
        TODO()
    }
}
