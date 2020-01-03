package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.webservice.util.JsonHelper
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Handles requests to resources of forms:
 * /processTemplates
 * /processTemplates/:processTemplateId
 */
object ProcessTemplatesHandler {

    /**
     * Handles requests for all process templates.
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
     * Handles requests for a single process template.
     */
    fun getOne(ctx: Context, processTemplateId: Int) {
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
     * Builds a process template from the given context.
     *
     * @return The process template is build with different constructors of ProcessTemplate
     * depending on whether processTemplateId is null or not.
     */
    private fun makeProcessTemplate(processTemplateId: Int?, ctx: Context): ProcessTemplate {
        val content = ctx.body()
        val processTemplateJsonObject = JSONObject(content)

        val title = processTemplateJsonObject.getString("title")
        val description = processTemplateJsonObject.getString("description")
        val durationLimit = processTemplateJsonObject.getInt("durationLimit")
        val ownerId = processTemplateJsonObject.getString("ownerId")

        val taskTemplates = parseTaskTemplates(processTemplateJsonObject.getJSONArray("taskTemplates")!!)

        if (processTemplateId == null)
            return ProcessTemplate(title, description, durationLimit, ownerId, taskTemplates)

        return ProcessTemplate(processTemplateId, title, description, durationLimit, ownerId, taskTemplates)
    }

    /**
     * Handles the request for creating a new process template.
     */
    fun create(ctx: Context) {
        val processTemplate = makeProcessTemplate(null, ctx)
        val newId = ProcessTemplateContainer.createProcessTemplate(processTemplate)

        val newIdJsonObject = JSONObject()
        newIdJsonObject.put("newId", newId)

        ctx.result(newIdJsonObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for updating a process template.
     */
    fun update(ctx: Context, processTemplateId: Int) {
        val processTemplate = makeProcessTemplate(processTemplateId, ctx)
        val newId = ProcessTemplateContainer.updateProcessTemplate(processTemplate)

        val newIdJsonObject = JSONObject()
        newIdJsonObject.put("newId", newId)

        ctx.result(newIdJsonObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for deleting a process template.
     */
    fun delete(ctx: Context, processTemplateId: Int) {
        ProcessTemplateContainer.deleteProcessTemplate(processTemplateId)
    }
}
