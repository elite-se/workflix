package de.se.team3.persistence

import me.liuwj.ktorm.dsl.select
import org.json.JSONArray
import org.json.JSONObject

object UserDAO {

    fun getUsers(): String {
        val users = JSONArray()

        for (row in Users.select()) {
            val user = JSONObject()
            user.put("id", row[Users.ID])
            user.put("name", row[Users.name])
            user.put("displayname", row[Users.displayname])
            user.put("email", row[Users.email])
            users.put(user)
        }

        return users.toString()
    }
}
