package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.webservice.util.JsonHelper
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import java.time.Instant
import java.util.NoSuchElementException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object ProcessGroupHandler {

    fun getAll(ctx: Context) {
        val groups = ProcessGroupContainer.getAllProcessGroups()

        val groupsArray = JsonHelper.toJsonArray(groups)
        val groupsJSON = JSONObject().put("groups", groupsArray)

        ctx.result(groupsJSON.toString())
            .contentType("application/json")
    }

    fun create(ctx: Context) {
        val content = ctx.body()
        val processGroupJsonObject = JSONObject(content)

        val title = processGroupJsonObject.getString("title")
        val description = processGroupJsonObject.getString("description")
        val ownerID = processGroupJsonObject.getString("ownerId")

        try {
            val processGroup = ProcessGroup(title, description, ownerID, Instant.now())
            val newId = ProcessGroupContainer.createProcessGroup(processGroup)
            processGroup.id = newId
            val newIdObject = JSONObject()
            newIdObject.put("newId", newId)

            ctx.result(newIdObject.toString())
        } catch (e: IllegalArgumentException) {
            ctx.status(400).result(e.toString())
        }
    }

    fun update(ctx: Context, processGroupId: Int) {
        try {
            val content = ctx.body()
            val processGroupJsonObject = JSONObject(content)

            val title = processGroupJsonObject.getString("title")
            val description = processGroupJsonObject.getString("description")
            val ownerID = processGroupJsonObject.getString("ownerId")

            val group = ProcessGroupContainer.getProcessGroup(processGroupId)
            group.title = title
            group.description = description
            group.owner = UserContainer.getUser(ownerID)
        } catch (e: JSONException) {
            ctx.status(400).result(e.toString())
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process group not found")
        }
    }

    fun delete(ctx: Context, processGroupId: Int) {
        try {
            ProcessGroupContainer.deleteProcessGroup(processGroupId)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process group not found")
        }
    }
}
