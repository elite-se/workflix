package de.se.team3.webservice.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.webservice.PagingHelper
import io.javalin.http.Context
import org.json.JSONArray
import org.json.JSONObject

object ProcessTemplateHandler {

    fun getAll(ctx: Context) {
        val page = ctx.pathParam("page").toInt()
        val templatePage = ProcessTemplateContainer.getAllProcessTemplates(page)

        val pagingContainer = PagingHelper.getPagingContainer(page, templatePage.second)
        val userArray = JSONArray(templatePage.first)
        pagingContainer.put("templates", userArray)

        ctx.result(pagingContainer.toString())
            .contentType("application/json")
    }

    fun getOne(ctx: Context, templateId: Int) {
        val processTemplate = ProcessTemplateContainer.getProcessTemplate(templateId)

        val mapper = ObjectMapper()
        // Note that the property taskTemplates of the underlying domain object are excluded by annotation
        val processTemplateObjectWithoutTasks = JSONObject(mapper.writeValueAsString(processTemplate))

        val taskTemplatesArray = JSONArray()
        for (taskTemplate in processTemplate.taskTemplates?.toList()!!) {
            // Note that the properties successors and predecessors of the underlying domain object are excluded by annotation
            val taskTemplateObjectWithoutNeighbors = JSONObject(mapper.writeValueAsString(taskTemplate))

            val successorArray = JSONArray()
            for (successor in taskTemplate.successors?.toList()!!)
                successorArray.put(successor.id)

            val predecessorArray = JSONArray()
            for (predecessor in taskTemplate.predeccessors?.toList()!!)
                predecessorArray.put(predecessor.id)

            taskTemplateObjectWithoutNeighbors.put("successors", successorArray)
            taskTemplateObjectWithoutNeighbors.put("predecessors", predecessorArray)
            taskTemplatesArray.put(taskTemplateObjectWithoutNeighbors)
        }

        processTemplateObjectWithoutTasks.put("taskTemplates", taskTemplatesArray)

        ctx.result(processTemplateObjectWithoutTasks.toString())
            .contentType("application/json")
    }

}