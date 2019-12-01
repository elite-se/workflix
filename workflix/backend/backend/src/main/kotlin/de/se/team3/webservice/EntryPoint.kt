package de.se.team3.webservice

import de.se.team3.persistence.Users
import io.javalin.Javalin
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.select
import org.json.JSONArray
import org.json.JSONObject

fun main(args: Array<String>) {
    val app = Javalin.create{ config -> config.enableCorsForAllOrigins() }.start(7000)

    val server = "***REMOVED***"
    val database = "***REMOVED***"
    val user = "***REMOVED***"
    val password = "***REMOVED***"
    Database.connect("jdbc:postgresql://" + server + "/" + database + "?user=" + user + "&password=" + password,
        driver = "org.postgresql.Driver")

    val users = JSONArray()

    for (row in Users.select()) {
        val user = JSONObject()
        user.put("id", row[Users.id])
        user.put("name", row[Users.name])
        user.put("displayname", row[Users.displayname])
        user.put("email", row[Users.email])
        users.put(user)
    }

    app.get("/users") { ctx -> ctx.contentType("application/json")
        .result(users.toString())}
}