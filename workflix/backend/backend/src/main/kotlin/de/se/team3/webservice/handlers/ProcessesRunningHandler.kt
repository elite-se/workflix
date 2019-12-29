package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.logic.domain.Process
import de.se.team3.webservice.util.JsonHelper
import io.javalin.http.Context
import java.util.NoSuchElementException
import org.json.JSONException
import org.json.JSONObject

object ProcessesRunningHandler {

    /**
     * Handles requests for processes of interest for the given user.
     */
    fun getProcessesOfInterest(ctx: Context, userId: String) {
        // TODO
    }

    /**
     * Handles the creation of a process.
     */
    fun create(ctx: Context) {
        try {
            val content = ctx.body()
            val processJsonObject = JSONObject(content)

            val starterId = processJsonObject.getString("starterId")
            val processGroupId = processJsonObject.getInt("processGroupId")
            val processTemplateId = processJsonObject.getInt("processTemplateId")

            val title = processJsonObject.getString("title")
            val description = processJsonObject.getString("description")
            val deadline = JsonHelper.getInstantFromString(processJsonObject.getString("deadline"))

            try {
                val process = Process(starterId, processGroupId, processTemplateId, title, description, deadline)

                val newId = ProcessContainer.createProcess(process)
                val newIdJsonObject = JSONObject()
                newIdJsonObject.put("newID", newId)

                ctx.result(newIdJsonObject.toString())
                    .contentType("application/json")
            } catch (e: NoSuchElementException) {
                ctx.status(400).result("user, process template or process group missing")
            } catch (e: IllegalArgumentException) {
                ctx.status(400).result(e.message!!)
            }
        } catch (e: JSONException) {
            ctx.status(400).result(e.message!!)
        }
    }

    /**
     * Handles requests for aborting a process.
     *
     * Nevertheless the function is named delete, because of the http method used.
     */
    fun delete(ctx: Context, processId: Int) {
        try {
            ProcessContainer.abortProcess(processId)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process not found")
        }
    }
}
