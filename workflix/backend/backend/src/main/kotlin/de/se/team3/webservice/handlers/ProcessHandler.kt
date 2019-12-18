package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import org.json.JSONArray

object ProcessHandler {

    fun getAll(ctx: Context, page: Int) {
        try {
            val processPage = ProcessContainer.getAllProcesses(page)

            val pagingContainer = PagingHelper.getPagingContainer(page, processPage.second)
            val processJsonArray = JSONArray(processPage.first)
            pagingContainer.put("processes", processJsonArray)

            ctx.result(pagingContainer.toString())
                .contentType("application/json")
        } catch (e: IllegalArgumentException) {
            ctx.status(404).result("page not found")
        }
    }

    fun getOne(ctx: Context, processId: Int) {

    }

}