package de.se.team3.webservice

import de.se.team3.persistence.meta.ConnectionManager
import de.se.team3.webservice.handlers.ProcessHandler
import de.se.team3.webservice.handlers.ProcessTemplateHandler
import de.se.team3.webservice.handlers.UserHandler
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
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

    app.get("users") { ctx ->
        UserHandler.getAll(ctx, ctx.queryParam<Int>("page").check({ it > 0}).get())
    }

    app.get("processTemplates") {ctx ->
        ProcessTemplateHandler.getAll(ctx, ctx.queryParam<Int>("page").check({ it > 0}).get())
    }
    app.get("processTemplates/:processTemplateId") { ctx ->
        try {
            ProcessTemplateHandler.getOne(ctx, ctx.pathParam("processTemplateId").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }
    app.post("processTemplates") { ctx -> ProcessTemplateHandler.create(ctx) }
    app.delete("processTemplates/:processTemplateId") { ctx ->
        try {
            ProcessTemplateHandler.delete(ctx, ctx.pathParam("processTemplateId").toInt())
        } catch(e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }

    app.get("processes") { ctx ->
        ProcessHandler.getAll(ctx, ctx.queryParam<Int>("processTemplateId").check({ it > 0 }).get())
    }
    app.get("processes/:processId") { ctx ->
        try {
            ProcessHandler.getOne(ctx, ctx.pathParam("processId").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }
    app.get("processes/running/:ownerId") { ctx ->

    }
}
