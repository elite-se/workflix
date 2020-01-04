package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupContainer
import de.se.team3.logic.container.UserContainer
import io.javalin.http.Context
import java.util.NoSuchElementException
import org.json.JSONObject

/**
 * Handles requests to resources of form:
 * /processGroups/:processGroupId/members/:memberId
 */
object ProcessGroupsMembersHandler {

    fun add(ctx: Context, processGroupId: Int, memberId: String) {

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

    fun revoke(ctx: Context, processGroupID: Int, userID: String) {
        try {
            val group = ProcessGroupContainer.getProcessGroup(processGroupID)
            val member = UserContainer.getUser(userID)

            group.members.remove(member)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("user or process group not found")
        }
    }
}
