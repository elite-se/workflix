package de.se.team3.webservice.handlers

import de.se.team3.logic.container.TaskAssignmentsContainer
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.webservice.containerInterfaces.TaskAssignmentsContainerInterface
import io.javalin.http.Context
import org.json.JSONObject

/**
 * Handles requests to resources of form:
 * /tasks/:taskId/assignments/:assigneeId
 */
object TasksAssignmentsHandler {

    private val taskAssignmentsContainer: TaskAssignmentsContainerInterface = TaskAssignmentsContainer

    /**
     * Handles requests for creating a new task assignment.
     */
    fun create(ctx: Context, taskId: Int, assigneeId: String) {
        val content = ctx.body()
        val taskAssignmentJsonObject = JSONObject(content)

        val immediateClosing = taskAssignmentJsonObject.getBoolean("immediateClosing")
        val taskAssignment = TaskAssignment(taskId, assigneeId, immediateClosing)

        val newId = taskAssignmentsContainer.createTaskAssignment(taskAssignment)
        val newIdJsonObject = JSONObject()
        newIdJsonObject.put("newId", newId)

        ctx.result(newIdJsonObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for closing a task assignment.
     */
    fun update(ctx: Context, taskId: Int, assigneeId: String) {
        taskAssignmentsContainer.closeTaskAssignment(taskId, assigneeId)
    }

    /**
     * Handles requests for deleting a task assignment.
     */
    fun delete(ctx: Context, taskId: Int, assigneeId: String) {
        taskAssignmentsContainer.deleteTaskAssignment(taskId, assigneeId)
    }
}
