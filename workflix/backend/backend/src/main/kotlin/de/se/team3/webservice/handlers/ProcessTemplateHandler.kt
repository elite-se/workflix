package de.se.team3.webservice.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import java.util.NoSuchElementException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object ProcessTemplateHandler {

    val mapper = ObjectMapper().registerModule(KotlinModule())

    fun getAll(ctx: Context, page: Int) {
        try {
            val templatePage = ProcessTemplateContainer.getAllProcessTemplates(page)

            val pagingContainer = PagingHelper.getPagingContainer(page, templatePage.second)
            val templateJsonArray = JSONArray(templatePage.first)
            pagingContainer.put("templates", templateJsonArray)

            ctx.result(pagingContainer.toString())
                .contentType("application/json")
        } catch (e: IllegalArgumentException) {
            ctx.status(404).result("page not found")
        }
    }

    fun getOne(ctx: Context, processTemplateId: Int) {
        try {
            val processTemplate = ProcessTemplateContainer.getProcessTemplate(processTemplateId)

            // Note that the property taskTemplates of the underlying domain object are excluded by annotation
            val processTemplateObjectWithoutTasks = JSONObject(mapper.writeValueAsString(processTemplate))

            val taskTemplatesArray = JSONArray()
            for (taskTemplate in processTemplate.taskTemplates?.map { it.value }?.toList()!!) {
                // Note that the properties successors and predecessors of the underlying domain object are excluded by annotation
                val taskTemplateObjectWithoutNeighbors = JSONObject(mapper.writeValueAsString(taskTemplate))

                val predecessorArray = JSONArray()
                taskTemplateObjectWithoutNeighbors.put("predecessors", predecessorArray)
                for (predecessor in taskTemplate.predecessors.toList())
                    predecessorArray.put(predecessor.id)

                taskTemplatesArray.put(taskTemplateObjectWithoutNeighbors)
            }

            processTemplateObjectWithoutTasks.put("taskTemplates", taskTemplatesArray)

            ctx.result(processTemplateObjectWithoutTasks.toString())
                .contentType("application/json")
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process template not found")
        }
    }

    private fun parseTaskTemplates(taskTemplatesArray: JSONArray): Map<Int, TaskTemplate> {
        val taskTemplatesMap = HashMap<Int, TaskTemplate>()
        val predecessorsPerTask = HashMap<Int, JSONArray>()

        for (entry in taskTemplatesArray) {
            val taskTemplateJsonObject = entry as JSONObject

            val taskTemplate = mapper.readValue<TaskTemplate>(taskTemplateJsonObject.toString())
            taskTemplatesMap.put(taskTemplate.id, taskTemplate)

            val predecessorsJsonArray = taskTemplateJsonObject.getJSONArray("predecessors")
            predecessorsPerTask.put(taskTemplate.id, predecessorsJsonArray)
        }

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

    fun create(ctx: Context) {
        try {
            val content = ctx.body()
            val processTemplateJsonObject = JSONObject(content)

            val title = processTemplateJsonObject.getString("title")
            val durationLimit = processTemplateJsonObject.getInt("durationLimit")
            val ownerId = processTemplateJsonObject.getString("ownerId")

            val taskTemplates = parseTaskTemplates(processTemplateJsonObject.getJSONArray("taskTemplates")!!)

            try {
                val processTemplate = ProcessTemplate(title, durationLimit, ownerId, taskTemplates)
                val newId = ProcessTemplateContainer.createProcessTemplate(processTemplate)
                val newIdObject = JSONObject()
                newIdObject.put("newId", newId)

                ctx.result(newIdObject.toString())
            } catch (e: IllegalArgumentException) {
                ctx.status(400).result(e.toString())
            }
        } catch (e: JSONException) {
            ctx.status(400).result(e.toString())
        }
    }

    fun delete(ctx: Context, processTemplateId: Int) {
        try {
            ProcessTemplateContainer.deleteProcessTemplate(processTemplateId)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process template not found")
        }
    }
}
