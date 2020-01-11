package de.se.team3.webservice.handlers

import de.se.team3.logic.authentication.VerificationMailManager
import de.se.team3.logic.container.UserContainer
import de.se.team3.webservice.containerInterfaces.UserContainerInterface
import de.se.team3.webservice.managerInterfaces.VerificationMailManagerInterface
import de.se.team3.webservice.util.JsonHelper
import io.javalin.http.Context
import org.json.JSONObject

object UserHandler {

    private val usersContainer: UserContainerInterface = UserContainer

    private val verificationMailManager: VerificationMailManagerInterface = VerificationMailManager

    fun getAll(ctx: Context) {
        val users = usersContainer.getAllUsers()

        val usersJsonArray = JsonHelper.toJsonArray(users)
        val responseJsonObject = JSONObject().put("users", usersJsonArray)

        ctx.result(responseJsonObject.toString())
            .contentType("application/json")
    }

    fun getOne(ctx: Context, userID: String) {
        val user = usersContainer.getUser(userID)
        val userJsonObject = JsonHelper.toJsonObject(user)

        ctx.result(userJsonObject.toString())
            .contentType("application/json")
    }

    fun verifyCreateRequest(ctx: Context) {
        val content = ctx.body()
        val contentJSON = JSONObject(content)

        val email = contentJSON.getString("email")
        verificationMailManager.initVerification(email)
    }

    fun createFrom***REMOVED***(ctx: Context, key: String) {
        val content = ctx.body()
        val contentJSON = JSONObject(content)

        val email = contentJSON.getString("email")
        val password = contentJSON.getString("password")

        if (VerificationMailManager.verifyAndInvalidateVerificationKey(email, key)) {
            val user = usersContainer.create***REMOVED***User(email, password)
            val resultJSON = JSONObject().put("newId", user.id)

            ctx.result(resultJSON.toString())
                .contentType("application/json")
        } else
            ctx.status(400).result("Your verification key is either invalid or expired.")
    }
}
