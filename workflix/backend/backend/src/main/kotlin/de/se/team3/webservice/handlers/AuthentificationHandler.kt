package de.se.team3.webservice.handlers

import de.se.team3.logic.authentification.LoginManager
import de.se.team3.logic.authentification.AuthorizationManager
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.logic.exceptions.NotVerifiedException
import io.javalin.http.Context
import org.json.JSONObject

object AuthentificationHandler {

    /**
     * Handles user verification before every request.
     * If all is well, it runs through.
     * Otherwise, an exception is thrown.
     */
    fun authorizeRequest(ctx: Context) {
        val bearerToken = ctx.header("Authorization")
            ?: throw NotVerifiedException("Every request must be enriched by an authorization token.")

        if (!AuthorizationManager.tokenIsRegistered(bearerToken))
            throw NotVerifiedException("You are not authorized to perform this request.")
    }

    /**
     * Finishes (authorized) requests.
     * Its main functionality is to tell the LoginManager it has to reset the active user.
     * If all is well, it runs through.
     * Otherwise, an exception is thrown.
     */
    fun endAuthorizedRequest(ctx: Context) {
        val bearerToken = ctx.header("Authorization")
            ?: throw InvalidInputException("Every request must be enriched by an authorization token.")

        if (!AuthorizationManager.finishAuthorizedRequest(bearerToken))
            throw NotVerifiedException("You were not verified to perform this request. How did you do this?")
    }

    /**
     * Logs a user in to the system.
     */
    fun login(ctx: Context) {
        val content = ctx.body()
        val loginJSON = JSONObject(content)

        val email = loginJSON.getString("email")
        val password = loginJSON.getString("password")

        val token = LoginManager.login(email, password)

        val tokenJSON = JSONObject().put("token", token.token)

        ctx.result(tokenJSON.toString())
    }

    /**
     * Logs a user out from the system.
     */
    fun logout(ctx: Context) {
        val bearerToken = ctx.header("Authorization")
            ?: throw InvalidInputException("Every request must be enriched by an authorization token.")
        val stringToken = bearerToken.substringAfter(' ')

        LoginManager.logout(stringToken)
    }
}