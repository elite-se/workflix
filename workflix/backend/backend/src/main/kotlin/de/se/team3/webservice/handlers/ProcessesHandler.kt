package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessQueryPredicate
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.webservice.util.JsonHelper
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import org.json.JSONObject

/**
 * Handles requests due to the general properties of processes.
 */
object ProcessesHandler {

    /**
     * Parses the selection predicate that is specified via query parameters in the request url.
     */
    fun parseSelectionPredicate(ctx: Context): ProcessQueryPredicate {
        val statuses = ArrayList<ProcessStatus>()
        val groupIds = ArrayList<Int>()
        var involvingUserId: String? = null

        for (param in ctx.queryParamMap()) {
            if (param.key == "status") // loops over all query parameters named status
                for (statusEntry in param.value)
                    statuses.add(ProcessStatus.valueOf(statusEntry))

            if (param.key == "groupId") // loops over all query parameters named groupId
                for (groupIdEntry in param.value)
                    groupIds.add(groupIdEntry.toInt())

            if (param.key == "involving") {
                if (param.value.size != 1)
                    ctx.status(400).result("query must not contain more than one involving parameter")

                involvingUserId = param.value.get(0)
            }
        }

        return ProcessQueryPredicate(statuses, groupIds, involvingUserId)
    }

    /**
     * Handles the request for all processes.
     */
    fun getAll(ctx: Context) {
        val predicate = parseSelectionPredicate(ctx)

        val processes = ProcessContainer.getAllProcesses(predicate)

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
        val process = ProcessContainer.getProcess(processId)

        // Building json
        val processJsonObject = JsonHelper.toJsonObject(process)
        val tasksJsonArray = JsonHelper.toJsonArray(process.tasks?.map { it.value }!!)
        processJsonObject.put("tasks", tasksJsonArray)

        ctx.result(processJsonObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles the creation of a process.
     */
    fun create(ctx: Context) {
        val content = ctx.body()
        val processJsonObject = JSONObject(content)

        val starterId = processJsonObject.getString("starterId")
        val processGroupId = processJsonObject.getInt("processGroupId")
        val processTemplateId = processJsonObject.getInt("processTemplateId")

        val title = processJsonObject.getString("title")
        val description = processJsonObject.getString("description")
        val deadline = JsonHelper.getInstantFromString(processJsonObject.getString("deadline"))

        val process = Process(starterId, processGroupId, processTemplateId, title, description, deadline)

        val newId = ProcessContainer.createProcess(process)
        val newIdJsonObject = JSONObject()
        newIdJsonObject.put("newID", newId)

        ctx.result(newIdJsonObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for aborting a process.
     *
     * Nevertheless the function is named delete, because of the http method used.
     */
    fun delete(ctx: Context, processId: Int) {
        ProcessContainer.abortProcess(processId)
    }
}
