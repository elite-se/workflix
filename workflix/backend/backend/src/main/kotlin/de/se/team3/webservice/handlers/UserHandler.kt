package de.se.team3.webservice.handlers

import de.se.team3.logic.container.UserContainer
import de.se.team3.webservice.util.PagingHelper
import io.javalin.http.Context
import java.lang.IllegalArgumentException
import org.json.JSONArray

object UserHandler {

    fun getAll(ctx: Context, page: Int) {
        try {
            val userPage = UserContainer.getAllUsers(page)

            val pagingContainer = PagingHelper.getPagingContainer(page, userPage.second)
            val userArray = JSONArray(userPage.first)
            pagingContainer.put("users", userArray)

            ctx.result(pagingContainer.toString())
                .contentType("application/json")
        } catch (e: IllegalArgumentException) {
            ctx.status(404).result("page not found")
        }
    }
}
