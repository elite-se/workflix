package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.webservice.util.JsonHelper
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import java.util.NoSuchElementException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Handles requests due to process templates.
 */
object ProcessTemplatesHandler {

    /**
     * Handles the request for all process templates.
     */
    fun getAll(ctx: Context) {
        val processTemplates = ProcessTemplateContainer.getAllProcessTemplates()

        val pagingContainer = PagingHelper.getPagingContainer(1, 1)
        val processTemplatesJsonArray = JsonHelper.toJsonArray(processTemplates)
        pagingContainer.put("templates", processTemplatesJsonArray)

        ctx.result(pagingContainer.toString())
            .contentType("application/json")
    }

    /**
     * Handles the request for a single process template.
     */
    fun getOne(ctx: Context, processTemplateId: Int) {
        try {
            val processTemplate = ProcessTemplateContainer.getProcessTemplate(processTemplateId)

            // Note that the property taskTemplates of the underlying domain object are excluded by annotation
            val processTemplateJsonObjectWithoutTasks = JsonHelper.toJsonObject(processTemplate)

            val taskTemplatesJsonArray = JSONArray()
            processTemplateJsonObjectWithoutTasks.put("taskTemplates", taskTemplatesJsonArray)

            // Builds the json representation for each task template
            for ((id, taskTemplate) in processTemplate.taskTemplates!!) {
                val taskTemplateObjectWithoutNeighbors = JsonHelper.toJsonObject(taskTemplate)
                taskTemplatesJsonArray.put(taskTemplateObjectWithoutNeighbors)

                // Adds the predecessors to the current task template
                val predecessorJsonArray = JSONArray()
                taskTemplateObjectWithoutNeighbors.put("predecessors", predecessorJsonArray)
                for (predecessor in taskTemplate.predecessors.toList())
                    predecessorJsonArray.put(predecessor.id)
            }

            ctx.result(processTemplateJsonObjectWithoutTasks.toString())
                .contentType("application/json")
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process template not found")
        }
    }

    /**
     * Is responsible for the parsing of the list of task templates while the creation or
     * update of a process template.
     */
    private fun parseTaskTemplates(taskTemplatesJsonArray: JSONArray): Map<Int, TaskTemplate> {
        val taskTemplatesMap = HashMap<Int, TaskTemplate>()
        val predecessorsPerTask = HashMap<Int, JSONArray>()

        // Creates the task templates and collects the predecessors for each task template
        for (entry in taskTemplatesJsonArray) {
            val taskTemplateJsonObject = entry as JSONObject

            val taskTemplate = JsonHelper.fromJson(taskTemplateJsonObject.toString(), TaskTemplate::class.java)
            taskTemplatesMap.put(taskTemplate.id, taskTemplate)

            val predecessorsJsonArray = taskTemplateJsonObject.getJSONArray("predecessors")
            predecessorsPerTask.put(taskTemplate.id, predecessorsJsonArray)
        }

        // Builds the relationship between the task templates
        for ((id, taskTemplate) in taskTemplatesMap) {
            for (entry in predecessorsPerTask.get(id)!!) {
                val predecessorId = entry as Int
                val predecessor = taskTemplatesMap.get(predecessorId)!!
                taskTemplate.predecessors.add(predecessor)
                predecessor.successors.add(taskTemplate)
            }
        }

        return taskTemplatesMap
    }

    /**
     * Handles the request for creating a new process template.
     */
    fun create(ctx: Context) {
        // JsonException is thrown if there is any attribute in json that does not exist but should
        try {
            val content = ctx.body()
            val processTemplateJsonObject = JSONObject(content)

            val title = processTemplateJsonObject.getString("title")
            val description = processTemplateJsonObject.getString("description")
            val durationLimit = processTemplateJsonObject.getInt("durationLimit")
            val ownerId = processTemplateJsonObject.getString("ownerId")

            val taskTemplates = parseTaskTemplates(processTemplateJsonObject.getJSONArray("taskTemplates")!!)

            // IllegalArgumentException is thrown if the creation of the domain object ProcessTemplate fails because of invalid input
            try {
                val processTemplate = ProcessTemplate(title, description, durationLimit, ownerId, taskTemplates)
                val newId = ProcessTemplateContainer.createProcessTemplate(processTemplate)
                val newIdJsonObject = JSONObject()
                newIdJsonObject.put("newId", newId)

                ctx.result(newIdJsonObject.toString())
                    .contentType("application/json")
            } catch (e: IllegalArgumentException) {
                ctx.status(400).result(e.message!!)
            }
        } catch (e: JSONException) {
            ctx.status(400).result(e.message!!)
        }
    }

    /**
     * Handles the request for updating a existing process template.
     */
    fun update(ctx: Context, processTemplateId: Int) {
        // JsonException is thrown if there is any attribute in json that does not exist but should
        try {
            val content = ctx.body()
            val processTemplateJsonObject = JSONObject(content)

            val title = processTemplateJsonObject.getString("title")
            val description = processTemplateJsonObject.getString("description")
            val durationLimit = processTemplateJsonObject.getInt("durationLimit")
            val ownerId = processTemplateJsonObject.getString("ownerId")

            val taskTemplates = parseTaskTemplates(processTemplateJsonObject.getJSONArray("taskTemplates")!!)

            // IllegalArgumentException is thrown if the creation of the domain object ProcessTemplate fails because of invalid input
            try {
                val processTemplate = ProcessTemplate(processTemplateId, title, description, durationLimit, ownerId, taskTemplates)
                val newId = ProcessTemplateContainer.updateProcessTemplate(processTemplate)

                val newIdJsonObject = JSONObject()
                newIdJsonObject.put("newId", newId)

                ctx.result(newIdJsonObject.toString())
            } catch (e: IllegalArgumentException) {
                ctx.status(400).result(e.message!!)
            } catch (e: NoSuchElementException) {
                ctx.status(404).result("process template not found")
            }
        } catch (e: JSONException) {
            ctx.status(400).result(e.message!!)
        }
    }

    /**
     * Handles requests for deleting a process template.
     *
     * The underlying application logic does not really delete a template. Rather it
     * marks the template as deleted.
     */
    fun delete(ctx: Context, processTemplateId: Int) {
        try {
            ProcessTemplateContainer.deleteProcessTemplate(processTemplateId)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process template not found")
        }
    }
}
