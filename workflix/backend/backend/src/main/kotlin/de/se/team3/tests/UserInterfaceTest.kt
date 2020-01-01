package de.se.team3.tests

import de.se.team3.logic.container.UserContainer
import de.se.team3.persistence.meta.ConnectionManager
import java.net.CookieHandler
import java.net.CookieManager
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.json.JSONArray
import org.json.JSONObject

const val ENV_PORT = "PORT"
const val DEFAULT_PORT = 7000

fun main(args: Array<String>) {
    ConnectionManager.connect()

    val users = UserContainer.getAllUsers()
    for (user in users)
        println(user.name)

    val cm = CookieManager()
    CookieHandler.setDefault(cm)

    val client = HttpClient.newBuilder()
        .cookieHandler(CookieHandler.getDefault())
        .version(HttpClient.Version.HTTP_2)
        .build()
    val getUserDetails = HttpRequest.newBuilder()
        .uri(URI.create("https://wf-backend.herokuapp.com/users?page=1"))
        .GET()
        .build()
    val responseGetUserDetails = client.send(getUserDetails, HttpResponse.BodyHandlers.ofString())

    println(JSONObject(responseGetUserDetails.body()))

    println(users.map { it.toJSON() })

    val userArray = JSONArray(users.map { it.toJSON() })
    val usersJSON = JSONObject().put("users", userArray)

    println(usersJSON)

    val user = UserContainer.getUser("58c120552c94decf6cf3b701")
    val userJSON = user.toJSON()

    println(userJSON)
}
