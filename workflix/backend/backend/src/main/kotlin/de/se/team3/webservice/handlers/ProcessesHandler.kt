package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.webservice.util.JsonHelper
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import java.util.NoSuchElementException

/**
 * Handles requests due to the general properties of processes.
 */
object ProcessesHandler {

    /**
     * Handles the request for all processes.
     */
    fun getAll(ctx: Context) {
        val processes = ProcessContainer.getAllProcesses()

        val pagingContainer = PagingHelper.getPagingContainer(1, 1)
        val processesJsonArray = JsonHelper.toJsonArray(processes)
        pagingContainer.put("processes", processesJsonArray)

        ctx.result(pagingContainer.toString())
            .contentType("application/json")
    }

    /**
     * Handles the request for a single process.
     */
    fun getOne(ctx: Context, processId: Int) {
        try {
            val process = ProcessContainer.getProcess(processId)

            // Building json
            val processJsonObject = JsonHelper.toJsonObject(process)
            val tasksJsonArray = JsonHelper.toJsonArray(process.tasks?.map { it.value }!!)
            processJsonObject.put("tasks", tasksJsonArray)

            ctx.result(processJsonObject.toString())
                .contentType("application/json")
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process not found")
        }
    }
}
