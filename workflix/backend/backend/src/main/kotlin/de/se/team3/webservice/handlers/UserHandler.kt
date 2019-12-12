package de.se.team3.webservice.handlers

import de.se.team3.logic.container.UserContainer
import de.se.team3.webservice.PagingHelper
import io.javalin.http.Context
import org.json.JSONArray


object UserHandler {

    fun getAll(ctx: Context) {
        val page = ctx.pathParam("page").toInt()
        val userPage = UserContainer.getUsers(page)

        val pagingContainer = PagingHelper.getPagingContainer(page, userPage.second)
        val userArray = JSONArray(userPage.first)
        pagingContainer.put("users", userArray)

        ctx.result(pagingContainer.toString())
            .contentType("application/json")
    }

}