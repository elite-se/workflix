package de.se.team3.logic.domain

import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.exceptions.NotFoundException
import java.time.Instant
import org.json.JSONArray
import org.json.JSONObject

data class UserRole(
    val id: Int?,
    val name: String,
    val description: String,
    val createdAt: Instant,
    private val members: ArrayList<User>
) {

    fun getMembers() = members

    /**
     * Create-Constructor
     */
    constructor(name: String, description: String) :
        this(null, name, description, Instant.now(), ArrayList<User>()) {

        if (name.isEmpty())
            throw InvalidInputException("name must not be empty")
    }

    /**
     * Update-Constructor
     */
    constructor(id: Int, name: String, description: String)
            : this(id, name, description, Instant.now(), ArrayList<User>()) {

        if (name.isEmpty())
            throw InvalidInputException("name must not be empty")
    }

    /**
     * Checks whether the specified user is a member of this group.
     *
     * @return True if and only if the user is member of this group.
     */
    fun hasMember(memberId: String): Boolean {
        return members.find { it.id == memberId } != null
    }

    /**
     * Adds the given member.
     *
     * @throws AlreadyExistsException Is thrown if the given user is already a member of this user role.
     */
    fun addMember(user: User) {
        if (hasMember(user.id))
            throw AlreadyExistsException("the given user is already a member of this user role")

        members.add(user)
    }

    /**
     * Removes the specified member.
     *
     * @throws NotFoundException Is thrown if the specified member does not exist.
     */
    fun removeMember(memberId: String) {
        val existed = members.removeIf { it.id == memberId }
        if (!existed)
            throw NotFoundException("member does not exist")
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
