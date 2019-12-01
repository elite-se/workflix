package de.se.team3.webservice

import de.se.team3.persistence.Users
import io.javalin.Javalin
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.select
import org.json.JSONArray
import org.json.JSONObject
import java.lang.NumberFormatException

const val ENV_PORT = "PORT"
const val DEFAULT_PORT = 7000

fun main(args: Array<String>) {
    val port = try {
        System.getenv(ENV_PORT)?.toInt()
    } catch (e: NumberFormatException) {
        null
    } ?: DEFAULT_PORT
    val app = Javalin.create { config -> config.enableCorsForAllOrigins() }.start(port)

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

    app.get("/users") { ctx ->
        ctx.contentType("application/json")
                .result(users.toString())
    }
}