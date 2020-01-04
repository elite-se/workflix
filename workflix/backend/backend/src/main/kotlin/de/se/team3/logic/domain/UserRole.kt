package de.se.team3.logic.domain

import java.time.Instant
import org.json.JSONArray
import org.json.JSONObject

data class UserRole(
    var id: Int,
    var name: String,
    var description: String,
    val createdAt: Instant,
    val members: ArrayList<User>
) {
    constructor(name: String, description: String) :
        this(0, name, description, Instant.now(), ArrayList<User>())

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put("id", this.id)
        json.put("name", this.name)
        json.put("description", this.description)
        json.put("createdAt", this.createdAt)
        json.put("memberIds", JSONArray(members.map { id }))
        return json
    }
}
