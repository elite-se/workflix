package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.logic.container.ProcessGroupsContainer
import de.se.team3.logic.container.ProcessTemplatesContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessQueryPredicate
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.webservice.containerInterfaces.ProcessContainerInterface
import de.se.team3.webservice.containerInterfaces.ProcessGroupsContainerInterface
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface
import de.se.team3.webservice.containerInterfaces.UserContainerInterface
import de.se.team3.webservice.util.JsonHelper
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import org.json.JSONObject

/**
 * Handles requests due to the general properties of processes.
 */
object ProcessesHandler {

    private val processesContainer: ProcessContainerInterface = ProcessContainer

    private val usersContainer: UserContainerInterface = UserContainer

    private val processGroupsContainer: ProcessGroupsContainerInterface = ProcessGroupsContainer

    private val processTemplatesContainer: ProcessTemplateContainerInterface = ProcessTemplatesContainer

    /**
     * Parses the selection predicate that is specified via query parameters in the request url.
     */
    private fun parseSelectionPredicate(ctx: Context): ProcessQueryPredicate {
        val statuses = ArrayList<ProcessStatus>()
        val groupIds = ArrayList<Int>()
        var involvingUserId: String? = null

        for (param in ctx.queryParamMap()) {
            if (param.key == "status") // loops over all query parameters named status
                for (statusEntry in param.value)
                    statuses.add(ProcessStatus.valueOf(statusEntry))

            if (param.key == "processGroupId") // loops over all query parameters named groupId
                for (groupIdEntry in param.value)
                    groupIds.add(groupIdEntry.toInt())

            if (param.key == "involving") {
                if (param.value.size != 1)
                    ctx.status(400).result("query must not contain more than one involving parameter")

                involvingUserId = param.value[0]
            }
        }

        return ProcessQueryPredicate(statuses, groupIds, involvingUserId)
    }

    /**
     * Handles the request for all processes.
     */
    fun getAll(ctx: Context) {
        try {
            val predicate = parseSelectionPredicate(ctx)

            val processes = processesContainer.getAllProcesses(predicate)

            val pagingContainer = PagingHelper.getPagingContainer(1, 1)
            val processesJsonArray = JsonHelper.toJsonArray(processes)
            pagingContainer.put("processes", processesJsonArray)

            ctx.result(pagingContainer.toString())
                .contentType("application/json")
        } catch (e: IllegalArgumentException) {
        ctx.status(400).result("predicate parsing error: unknown or malformed status, group ID or user ID")
        }
    }

    /**
     * Handles the request for a single process.
     */
    fun getOne(ctx: Context, processId: Int) {
        val process = processesContainer.getProcess(processId)

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

        val starter = usersContainer.getUser(starterId)
        val processGroup = processGroupsContainer.getProcessGroup(processGroupId)
        val processTemplate = processTemplatesContainer.getProcessTemplate(processTemplateId)
        val process = Process(starter, processGroup, processTemplate, title, description, deadline)

        val newId = processesContainer.createProcess(process)
        val newIdJsonObject = JSONObject()
        newIdJsonObject.put("newId", newId)

        ctx.result(newIdJsonObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for updating a process.
     */
    fun update(ctx: Context, processId: Int) {
        val content = ctx.body()
        val processJsonObject = JSONObject(content)

        val title = processJsonObject.getString("title")
        val description = processJsonObject.getString("description")
        val deadline = JsonHelper.getInstantFromString(processJsonObject.getString("deadline"))

        processesContainer.updateProcess(processId, title, description, deadline)
    }

    /**
     * Handles requests for aborting a process.
     *
     * Nevertheless the function is named delete, because of the http method used.
     */
    fun delete(ctx: Context, processId: Int) {
        processesContainer.abortProcess(processId)
    }
}
