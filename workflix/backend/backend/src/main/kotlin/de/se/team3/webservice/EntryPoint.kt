package de.se.team3.webservice

import de.se.team3.persistence.meta.AlreadyExistsException
import de.se.team3.persistence.meta.ConnectionManager
import de.se.team3.persistence.meta.NotFoundException
import de.se.team3.persistence.meta.TaskAssignmentsTable
import de.se.team3.webservice.handlers.ProcessGroupHandler
import de.se.team3.webservice.handlers.ProcessGroupMembershipHandler
import de.se.team3.webservice.handlers.ProcessTemplatesHandler
import de.se.team3.webservice.handlers.ProcessesHandler
import de.se.team3.webservice.handlers.ProcessesRunningHandler
import de.se.team3.webservice.handlers.TasksAssignmentsHandler
import de.se.team3.webservice.handlers.UserHandler
import io.javalin.Javalin
import org.json.JSONException
import java.lang.IllegalStateException
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

    // exception handling
    app.exception(NumberFormatException::class.java) { e, ctx ->
        ctx.status(400).result("invalid id format")
    }
    app.exception(NotFoundException::class.java) { e, ctx ->
        ctx.status(404).result(e.message)
    }
    app.exception(AlreadyExistsException::class.java) { e, ctx ->
        ctx.status(409).result(e.message)
    }
    app.exception(JSONException::class.java) { e, ctx ->
        ctx.status(400).result("" + e.message)
    }

    // users
    app.get("users") { ctx ->
        UserHandler.getAll(ctx, ctx.queryParam<Int>("page").check({ it > 0 }).get())
    }

    // process templates
    app.get("processTemplates") { ctx -> ProcessTemplatesHandler.getAll(ctx) }
    app.get("processTemplates/:processTemplateId") { ctx ->
        ProcessTemplatesHandler.getOne(ctx, ctx.pathParam("processTemplateId").toInt())
    }
    app.post("processTemplates") { ctx -> ProcessTemplatesHandler.create(ctx) }
    app.patch("processTemplates/:processTemplateId") { ctx ->
        ProcessTemplatesHandler.update(ctx, ctx.pathParam("processTemplateId").toInt())
    }
    app.delete("processTemplates/:processTemplateId") { ctx ->
        ProcessTemplatesHandler.delete(ctx, ctx.pathParam("processTemplateId").toInt())
    }

    // processes
    app.get("processes") { ctx -> ProcessesHandler.getAll(ctx) }
    app.get("processes/:processId") { ctx ->
        ProcessesHandler.getOne(ctx, ctx.pathParam("processId").toInt())
    }

    // running processes
    app.get("processes/running/:ownerId") { ctx -> }
    app.post("processes/running") { ctx -> ProcessesRunningHandler.create(ctx) }
    app.delete("processes/running/:processId") { ctx ->
        ProcessesRunningHandler.delete(ctx, ctx.pathParam("processId").toInt())
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
        }
    }

    // task assignments
    app.put("tasks/:taskId/assignments/:assigneeId") { ctx ->
        TasksAssignmentsHandler.create(ctx, ctx.pathParam("taskId").toInt(), ctx.pathParam("assigneeId"))
    }
    app.patch("tasks/:taskId/assignments/:assigneeId") { ctx ->
        TasksAssignmentsHandler.update(ctx, ctx.pathParam("taskId").toInt(), ctx.pathParam("assigneeId"))
    }
    app.delete("tasks/:taskId/assignments/:assigneeId") { ctx ->
        TasksAssignmentsHandler.delete(ctx, ctx.pathParam("taskId").toInt(), ctx.pathParam("assigneeId"))
    }

}
