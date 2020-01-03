package de.se.team3.logic.domain

import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

data class UserRole(
    var id: Int,
    var name: String,
    var description: String,
    val createdAt: Instant,
    val members: List<User>
) {
    constructor(name: String, description: String) :
        this(0, name, description, Instant.now(), ArrayList<User>())

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", this.id)
        json.put("name", this.name)
        json.put("description", this.description)
        json.put("createdAt", this.createdAt)
        json.put("members", JSONArray(members.map { toJson() }))
        return json
    }
}
