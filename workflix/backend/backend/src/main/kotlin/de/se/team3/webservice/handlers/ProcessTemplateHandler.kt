package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.webservice.PagingHelper
import io.javalin.http.Context
import org.json.JSONArray

object ProcessTemplateHandler {

    fun getAll(ctx: Context) {
        val page = ctx.pathParam("page").toInt()
        val templatePage = ProcessTemplateContainer.getProcessTemplates(page)

        val pagingContainer = PagingHelper.getPagingContainer(page, templatePage.second)
        val userArray = JSONArray(templatePage.first)
        pagingContainer.put("templates", userArray)

        ctx.result(pagingContainer.toString())
            .contentType("application/json")
    }

}