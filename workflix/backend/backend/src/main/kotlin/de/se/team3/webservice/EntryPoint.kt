package de.se.team3.webservice

import de.se.team3.persistence.ConnectionManager
import de.se.team3.persistence.UserDAO
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

    ConnectionManager.connect()

    app.get("/users") { ctx ->
        ctx.contentType("application/json")
                .result(UserDAO.getUsers())
    }
}