package de.se.team3.logic.domain

import de.se.team3.logic.exceptions.InvalidInputException
import java.time.Instant
import org.json.JSONArray
import org.json.JSONObject

data class UserRole(
    val id: Int?,
    private var name: String,
    private var description: String,
    val createdAt: Instant,
    val members: ArrayList<User>
) {

    fun getName() = name

    fun getDescription() = description

    /**
     * Create-Constructor
     */
    constructor(name: String, description: String) :
        this(null, name, description, Instant.now(), ArrayList<User>()) {

        setName(name)
        setDescription(description)
    }

    /**
     * Update-Constructor
     */
    constructor(id: Int, name: String, description: String)
            : this(id, name, description, Instant.now(), ArrayList<User>())

    /**
     * Sets the name.
     *
     * @throws InvalidInputException Is thrown if the given name is empty.
     */
    fun setName(name: String) {
        if (name.isEmpty())
            throw InvalidInputException("name must not be empty")

        this.name = name
    }

    /**
     * Sets the description.
     */
    fun setDescription(description: String) {
        this.description = description
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put("id", this.id)
        json.put("name", this.name)
        json.put("description", this.description)
        json.put("createdAt", this.createdAt)
        json.put("memberIds", JSONArray(members.map { it.id }))
        return json
    }
}
