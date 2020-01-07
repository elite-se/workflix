package de.se.team3.webservice.handlers

import de.se.team3.logic.container.TaskCommentsContainer
import de.se.team3.logic.domain.TaskComment
import de.se.team3.webservice.containerInterfaces.TaskCommentsContainerInterface
import io.javalin.http.Context
import org.json.JSONObject

/**
 * Handles requests to resources of forms:
 * /tasks/:taskId/comments
 * /tasks/comments/:taskCommentId
 */
object TasksCommentsHandler {

    private val taskCommentsContainer: TaskCommentsContainerInterface = TaskCommentsContainer

    /**
     * Handles requests for creating a new task comment.
     */
    fun create(ctx: Context, taskId: Int) {
        val requestContent = ctx.body()
        val taskCommentJsonObject = JSONObject(requestContent)

        val creatorId = taskCommentJsonObject.getString("creatorId")
        val commentContent = taskCommentJsonObject.getString("content")

        val newId = taskCommentsContainer.createTaskComment(TaskComment(taskId, creatorId, commentContent))
        val newIdJSONObject = JSONObject()
        newIdJSONObject.put("newId", newId)

        ctx.result(newIdJSONObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for updating a task comment.
     */
    fun update(ctx: Context, taskCommentId: Int) {
        val requestContent = ctx.body()
        val taskCommentJsonObject = JSONObject(requestContent)

        val commentContent = taskCommentJsonObject.getString("content")

        taskCommentsContainer.updateTaskComment(TaskComment(taskCommentId, commentContent))
    }

    /**
     * Handles requests for deleting a task comment.
     */
    fun delete(ctx: Context, taskCommentId: Int) {
        taskCommentsContainer.deleteTaskComment(taskCommentId)
    }
}
