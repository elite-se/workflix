package de.se.team3.webservice.handlers

import de.se.team3.logic.authentification.LoginManager
import io.javalin.http.Context
import org.json.JSONObject

object AuthentificationHandler {

    fun login(ctx: Context) {
        val content = ctx.body()
        val loginJSON = JSONObject(content)

        val email = loginJSON.getString("email")
        val password = loginJSON.getString("password")

        val token = LoginManager.login(email, password)

        val tokenJSON = JSONObject().put("token", token.token)

        ctx.result(tokenJSON.toString())
    }
}