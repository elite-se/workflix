package de.se.team3.webservice.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.se.team3.logic.container.ProcessGroupContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import java.time.LocalDate
import java.util.NoSuchElementException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object ProcessGroupHandler {

    val mapper = ObjectMapper().registerModule(KotlinModule())

    fun getAll(ctx: Context, page: Int) {
        try {
            val groupPage = ProcessGroupContainer.getAllProcessGroups(page)

            val pagingContainer = PagingHelper.getPagingContainer(page, groupPage.second)
            val groupJsonArray = JSONArray(groupPage.first)
            pagingContainer.put("templates", groupJsonArray)

            ctx.result(pagingContainer.toString())
                .contentType("application/json")
        } catch (e: IllegalArgumentException) {
            ctx.status(404).result("page not found")
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
        }
    }

    fun create(ctx: Context) {
        try {
            val content = ctx.body()
            val processGroupJsonObject = JSONObject(content)

            val title = processGroupJsonObject.getString("title")
            val description = processGroupJsonObject.getString("description")
            val ownerID = processGroupJsonObject.getString("ownerId")

            try {
                val processGroup = ProcessGroup(title, description, ownerID, LocalDate.now())
                val newId = ProcessGroupContainer.createProcessGroup(processGroup)
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

    fun delete(ctx: Context, processGroupId: Int) {
        try {
            ProcessGroupContainer.deleteProcessGroup(processGroupId)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process group not found")
        }
    }

    fun addMembership(ctx: Context) {
        try {
            val content = ctx.body()
            val memberJSONObject = JSONObject(content)

            val group = ProcessGroupContainer.getProcessGroup(memberJSONObject.getInt("groupId"))
            val member = UserContainer.getUser(memberJSONObject.getString("userId"))

            group.members.add(member)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user or process group not found")
        }
    }

    fun revokeMembership(ctx: Context, processGroupID: Int, userID: String) {
        try {
            val group = ProcessGroupContainer.getProcessGroup(processGroupID)
            val member = UserContainer.getUser(userID)

            group.members.remove(member)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user or process group not found")
        }
    }
}
