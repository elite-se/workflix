package de.se.team3.webservice

import de.se.team3.logic.exceptions.AlreadyClosedException
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.exceptions.NotAuthorizedException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.logic.exceptions.UnsatisfiedPreconditionException
import de.se.team3.persistence.meta.ConnectionManager
import de.se.team3.webservice.handlers.AuthenticationHandler
import de.se.team3.webservice.handlers.CORSHandler
import de.se.team3.webservice.handlers.ProcessGroupsHandler
import de.se.team3.webservice.handlers.ProcessGroupsMembersHandler
import de.se.team3.webservice.handlers.ProcessTemplatesHandler
import de.se.team3.webservice.handlers.ProcessesHandler
import de.se.team3.webservice.handlers.TasksAssignmentsHandler
import de.se.team3.webservice.handlers.TasksCommentsHandler
import de.se.team3.webservice.handlers.UserHandler
import de.se.team3.webservice.handlers.UserRolesHandler
import de.se.team3.webservice.handlers.UserRolesMembersHandler
import io.javalin.Javalin
import java.lang.NumberFormatException
import org.json.JSONException

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

    // webservice exceptions, i.d. exceptions thrown if something is wrong if the input format
    app.exception(NumberFormatException::class.java) { _, ctx ->
        ctx.status(400).result("invalid id format")
    }
    app.exception(JSONException::class.java) { e, ctx ->
        ctx.status(400).result("" + e.message)
    }

    // logical exceptions, i.d. exceptions thrown if a logical illegal request was made
    app.exception(InvalidInputException::class.java) { e, ctx ->
        ctx.status(400).result(e.message)
    }
    app.exception(NotFoundException::class.java) { e, ctx ->
        ctx.status(404).result(e.message)
    }
    app.exception(AlreadyExistsException::class.java) { e, ctx ->
        ctx.status(409).result(e.message)
    }
    app.exception(AlreadyClosedException::class.java) { e, ctx ->
        ctx.status(409).result(e.message)
    }
    app.exception(UnsatisfiedPreconditionException::class.java) { e, ctx ->
        ctx.status(404).result(e.message)
    }
    app.exception(NotAuthorizedException::class.java) { e, ctx ->
        ctx.status(401).result(e.message)
    }
    app.exception(Exception::class.java) { e, ctx ->
        ctx.status(500).result(e.message + "")
        throw e
    }

    // authentication handling before every request (excluding login)
    app.before() { ctx ->
        if (ctx.method() == "OPTIONS") {
            CORSHandler.optionsRequest(ctx)
        } else if (ctx.path() != "/login") {
            AuthenticationHandler.authorizeRequest(ctx)
        }
    }

    // login
    app.post("login") { ctx ->
        AuthenticationHandler.login(ctx)
    }
    // logout
    app.delete("login") { ctx ->
        AuthenticationHandler.logout(ctx)
    }

    // users
    app.get("users") { ctx ->
        UserHandler.getAll(ctx)
    }
    app.get("users/:userId") { ctx ->
        UserHandler.getOne(ctx, ctx.pathParam("userId"))
    }
    app.post("users") { ctx ->
        UserHandler.createFrom***REMOVED***(ctx)
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

    // user roles
    app.get("userRoles") { ctx ->
        UserRolesHandler.getAll(ctx)
    }
    app.post("userRoles") { ctx ->
        UserRolesHandler.create(ctx)
    }
    app.patch("userRoles/:userRoleId") { ctx ->
        UserRolesHandler.update(ctx, ctx.pathParam("userRoleId").toInt())
    }
    app.delete("userRoles/:userRoleId") { ctx ->
        UserRolesHandler.delete(ctx, ctx.pathParam("userRoleId").toInt())
    }

    // user role memberships
    app.put("userRoles/:userRoleId/members/:memberId") { ctx ->
        UserRolesMembersHandler.create(
            ctx,
            ctx.pathParam("userRoleId").toInt(),
            ctx.pathParam("memberId")
        )
    }
    app.delete("userRoles/:userRoleId/members/:memberId") { ctx ->
        UserRolesMembersHandler.delete(
            ctx,
            ctx.pathParam("userRoleId").toInt(),
            ctx.pathParam("memberId")
        )
    }

    // processes
    app.get("processes") { ctx -> ProcessesHandler.getAll(ctx) }
    app.get("processes/:processId") { ctx ->
        ProcessesHandler.getOne(ctx, ctx.pathParam("processId").toInt())
    }
    app.post("processes") { ctx -> ProcessesHandler.create(ctx) }
    app.delete("processes/:processId") { ctx ->
        ProcessesHandler.delete(ctx, ctx.pathParam("processId").toInt())
    }

    // process groups
    app.get("processGroups") { ctx ->
        ProcessGroupsHandler.getAll(ctx)
    }
    app.post("processGroups") { ctx ->
        ProcessGroupsHandler.create(ctx)
    }
    app.patch("processGroups/:processGroupID") { ctx ->
        ProcessGroupsHandler.update(ctx, ctx.pathParam("processGroupID").toInt())
    }
    app.delete("processGroups/:processGroupID") { ctx ->
        ProcessGroupsHandler.delete(ctx, ctx.pathParam("processGroupID").toInt())
    }

    // group memberships
    app.put("processGroups/:processGroupId/members/:memberId") { ctx ->
        ProcessGroupsMembersHandler.create(ctx, ctx.pathParam("processGroupId").toInt(), ctx.pathParam("memberId"))
    }
    app.delete("processGroups/:processGroupId/members/:memberId") { ctx ->
        ProcessGroupsMembersHandler.delete(
            ctx,
            ctx.pathParam("processGroupId").toInt(),
            ctx.pathParam("memberId")
        )
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

    // task comments
    app.post("tasks/:taskId/comments") { ctx ->
        TasksCommentsHandler.create(ctx, ctx.pathParam("taskId").toInt())
    }
    app.patch("tasks/comments/:taskCommentId") { ctx ->
        TasksCommentsHandler.update(ctx, ctx.pathParam("taskCommentId").toInt())
    }
    app.delete("tasks/comments/:taskCommentId") { ctx ->
        TasksCommentsHandler.delete(ctx, ctx.pathParam("taskCommentId").toInt())
    }
}
