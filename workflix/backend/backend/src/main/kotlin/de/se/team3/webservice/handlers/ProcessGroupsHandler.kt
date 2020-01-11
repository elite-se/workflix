package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupsContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.webservice.containerInterfaces.ProcessGroupsContainerInterface
import de.se.team3.webservice.containerInterfaces.UserContainerInterface
import de.se.team3.webservice.util.JsonHelper
import io.javalin.http.Context
import org.json.JSONObject

object ProcessGroupsHandler {

    private val processGroupsContainer: ProcessGroupsContainerInterface = ProcessGroupsContainer

    private val usersContainer: UserContainerInterface = UserContainer

    /**
     * Handles requests for getting all process groups.
     */
    fun getAll(ctx: Context) {
        val groups = processGroupsContainer.getAllProcessGroups()

        val groupsArray = JsonHelper.toJsonArray(groups)
        val groupsJSON = JSONObject().put("groups", groupsArray)

        ctx.result(groupsJSON.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for creating a process group.
     */
    fun create(ctx: Context) {
        val content = ctx.body()
        val processGroupJsonObject = JSONObject(content)

        val title = processGroupJsonObject.getString("title")
        val description = processGroupJsonObject.getString("description")
        val ownerID = processGroupJsonObject.getString("ownerId")

        val owner = usersContainer.getUser(ownerID)
        val processGroup = ProcessGroup(owner, title, description)
        val newId = processGroupsContainer.createProcessGroup(processGroup)

        val newIdObject = JSONObject()
        newIdObject.put("newId", newId)

        ctx.result(newIdObject.toString())
            .contentType("application/json")
    }

    /**
     * Handles requests for updating a process group.
     */
    fun update(ctx: Context, processGroupId: Int) {
        val content = ctx.body()
        val processGroupJsonObject = JSONObject(content)

        val title = processGroupJsonObject.getString("title")
        val description = processGroupJsonObject.getString("description")
        val ownerID = processGroupJsonObject.getString("ownerId")

        val owner = usersContainer.getUser(ownerID)
        val processGroup = ProcessGroup(processGroupId, owner, title, description)
        processGroupsContainer.updateProcessGroup(processGroup)
    }

    /**
     * Handles requests for deleting a process group.
     */
    fun delete(ctx: Context, processGroupId: Int) {
        processGroupsContainer.deleteProcessGroup(processGroupId)
    }
}
