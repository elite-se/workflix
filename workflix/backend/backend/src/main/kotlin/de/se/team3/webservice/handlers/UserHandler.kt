package de.se.team3.webservice.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.se.team3.logic.container.UserContainer
import de.se.team3.webservice.util.JsonHelper
import io.javalin.http.Context
import org.json.JSONObject

object UserHandler {

    val mapper = ObjectMapper().registerModule(KotlinModule())

    fun getAll(ctx: Context) {
        val users = UserContainer.getAllUsers()

        val usersJsonArray = JsonHelper.toJsonArray(users)
        val responseJsonObject = JSONObject().put("users", usersJsonArray)

        ctx.result(responseJsonObject.toString())
            .contentType("application/json")
    }

    fun getOne(ctx: Context, userID: String) {
        val user = UserContainer.getUser(userID)
        val userJsonObject = JsonHelper.toJsonObject(user)

        ctx.result(userJsonObject.toString())
            .contentType("application/json")
    }

    fun createFrom***REMOVED***(ctx: Context) {
        val content = ctx.body()
        val contentJSON = JSONObject(content)

        val email = contentJSON.getString("email")
        val password = contentJSON.getString("password")

        val user = UserContainer.create***REMOVED***User(email, password)
        val resultJSON = JSONObject().put("newId", user.id)

        ctx.result(resultJSON.toString())
            .contentType("application/json")
    }
}
