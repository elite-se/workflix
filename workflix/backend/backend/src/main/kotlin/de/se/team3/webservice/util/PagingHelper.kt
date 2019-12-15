package de.se.team3.webservice.util

import org.json.JSONObject

object PagingHelper {

    fun getPagingContainer(page: Int, lastPage: Int): JSONObject {
        val container = JSONObject()

        val paging = JSONObject()
        paging.put("previous", if (page > 1) page - 1 else JSONObject.NULL)
        paging.put("next", if (page < lastPage) page + 1 else JSONObject.NULL)
        paging.put("last", lastPage)
        container.put("paging", paging)

        return container
    }

}