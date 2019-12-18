package de.se.team3.webservice

import de.se.team3.persistence.meta.ConnectionManager
import de.se.team3.webservice.handlers.ProcessTemplateHandler
import de.se.team3.webservice.handlers.UserHandler
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
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

    app.routes {
        path("users") {
            get() { ctx ->
                UserHandler.getAll(ctx, ctx.queryParam<Int>("page").check({ it > 0 }).get())
            }
        }

        path("processTemplates") {
            get() { ctx ->
                ProcessTemplateHandler.getAll(ctx, ctx.queryParam<Int>("page").check({ it > 0 }).get())
            }
            get(":processTemplateId") { ctx ->
                try {
                    ProcessTemplateHandler.getOne(ctx, ctx.pathParam("processTemplateId").toInt())
                } catch (e: NumberFormatException) {
                    ctx.status(400).result("invalid id")
                }
            }
            post() { ctx -> ProcessTemplateHandler.create(ctx) }
            delete(":processTemplateId") { ctx ->
                try {
                    ProcessTemplateHandler.delete(ctx, ctx.pathParam("processTemplateId").toInt())
                } catch (e: NumberFormatException) {
                    ctx.status(400).result("invalid id")
                }
            }
        }
    }
}
