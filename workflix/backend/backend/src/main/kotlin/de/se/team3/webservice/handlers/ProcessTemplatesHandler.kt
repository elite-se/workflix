package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessTemplatesContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.container.UserRoleContainer
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface
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

    private val processTemplatesContainer: ProcessTemplateContainerInterface = ProcessTemplatesContainer

    /**
     * Handles requests for all process templates.
     */
    fun getAll(ctx: Context) {
        val processTemplates = processTemplatesContainer.getAllProcessTemplates()

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
        val processTemplate = processTemplatesContainer.getProcessTemplate(processTemplateId)

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
            for (predecessor in taskTemplate.getPredecessors())
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

            val id = taskTemplateJsonObject.getInt("id")
            val responsibleUserRoleId = taskTemplateJsonObject.getInt("responsibleUserRoleId")
            val name = taskTemplateJsonObject.getString("name")
            val description = taskTemplateJsonObject.getString("description")
            val estimatedDuration = taskTemplateJsonObject.getDouble("estimatedDuration")
            val necessaryClosings = taskTemplateJsonObject.getInt("necessaryClosings")

            val responsibleUserRole = UserRoleContainer.getUserRole(responsibleUserRoleId)
            val taskTemplate = TaskTemplate(id, responsibleUserRole, name, description, estimatedDuration, necessaryClosings)
            taskTemplatesMap.put(taskTemplate.id, taskTemplate)

            val predecessorsJsonArray = taskTemplateJsonObject.getJSONArray("predecessors")
            predecessorsPerTask.put(taskTemplate.id, predecessorsJsonArray)
        }

        // Builds the relationship between the task templates
        for ((id, taskTemplate) in taskTemplatesMap) {
            for (entry in predecessorsPerTask.get(id)!!) {
                val predecessorId = entry as Int
                val predecessor = taskTemplatesMap.get(predecessorId)!!
                taskTemplate.addPredecessor(predecessor)
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
        val durationLimit = processTemplateJsonObject.getDouble("durationLimit")
        val ownerId = processTemplateJsonObject.getString("ownerId")

        val owner = UserContainer.getUser(ownerId)
        val taskTemplates = parseTaskTemplates(processTemplateJsonObject.getJSONArray("taskTemplates")!!)

        if (processTemplateId == null)
            return ProcessTemplate(title, description, durationLimit, owner, taskTemplates)

        return ProcessTemplate(processTemplateId, title, description, durationLimit, owner, taskTemplates)
    }

    /**
     * Handles the request for creating a new process template.
     */
    fun create(ctx: Context) {
        try {
            val processTemplate = makeProcessTemplate(null, ctx)
            val newId = processTemplatesContainer.createProcessTemplate(processTemplate)

            val newIdJsonObject = JSONObject()
            newIdJsonObject.put("newId", newId)

            ctx.result(newIdJsonObject.toString())
                .contentType("application/json")
        } catch (e: Throwable) {
            println("" + e.message)
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Handles requests for updating a process template.
     */
    fun update(ctx: Context, processTemplateId: Int) {
        val processTemplate = makeProcessTemplate(processTemplateId, ctx)
        val newId = processTemplatesContainer.updateProcessTemplate(processTemplate)

        val newIdJsonObject = JSONObject()
        newIdJsonObject.put("newId", newId)

        ctx.result(newIdJsonObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for deleting a process template.
     */
    fun delete(ctx: Context, processTemplateId: Int) {
        processTemplatesContainer.deleteProcessTemplate(processTemplateId)
    }
}
