package de.se.team3.webservice

import de.se.team3.persistence.meta.ConnectionManager
import de.se.team3.webservice.handlers.ProcessGroupHandler
import de.se.team3.webservice.handlers.ProcessGroupMembershipHandler
import de.se.team3.webservice.handlers.ProcessTemplatesHandler
import de.se.team3.webservice.handlers.ProcessesHandler
import de.se.team3.webservice.handlers.ProcessesRunningHandler
import de.se.team3.webservice.handlers.UserHandler
import io.javalin.Javalin
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

    // Users
    app.get("users") { ctx ->
        UserHandler.getAll(ctx, ctx.queryParam<Int>("page").check({ it > 0 }).get())
    }

    app.get("processTemplates") { ctx ->
        ProcessTemplatesHandler.getAll(ctx)
    }
    app.get("processTemplates/:processTemplateId") { ctx ->
        try {
            ProcessTemplatesHandler.getOne(ctx, ctx.pathParam("processTemplateId").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }
    app.post("processTemplates") { ctx -> ProcessTemplatesHandler.create(ctx) }
    app.patch("processTemplates/:processTemplateId") { ctx ->
        try {
            ProcessTemplatesHandler.update(ctx, ctx.pathParam("processTemplateId").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }
    app.delete("processTemplates/:processTemplateId") { ctx ->
        try {
            ProcessTemplatesHandler.delete(ctx, ctx.pathParam("processTemplateId").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }

    app.get("processes") { ctx -> ProcessesHandler.getAll(ctx) }
    app.get("processes/:processId") { ctx ->
        try {
            ProcessesHandler.getOne(ctx, ctx.pathParam("processId").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }

    app.get("processes/running/:ownerId") { ctx ->
    }

    // process groups
    app.get("processGroups") { ctx ->
        ProcessGroupHandler.getAll(ctx)
    }
    app.post("processGroups") { ctx ->
        ProcessGroupHandler.create(ctx)
    }
    app.patch("processGroups/:processGroupID") { ctx ->
        try {
            ProcessGroupHandler.update(ctx, ctx.pathParam("processGroupID").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid process group id")
        }
    }
    app.get("processGroups/:processGroupID") { ctx ->
        try {
            ProcessGroupHandler.delete(ctx, ctx.pathParam("processGroupID").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid process group id")
        }
    }

    // group memberships
    app.post("groupMembership") { ctx ->
        ProcessGroupMembershipHandler.add(ctx)
    }
    app.delete("groupMembership/:processGroupID/:userID") { ctx ->
        try {
            ProcessGroupMembershipHandler.revoke(
                ctx,
                ctx.pathParam("processGroupID").toInt(),
                ctx.pathParam("userID").toString()
            )
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid process group id")

    app.post("processes/running") { ctx -> ProcessesRunningHandler.create(ctx) }
    app.delete("processes/running/:processId") { ctx ->
        try {
            ProcessesRunningHandler.delete(ctx, ctx.pathParam("processId").toInt())
        } catch (e: NumberFormatException) {
            ctx.status(400).result("invalid id")
        }
    }
}
