package de.se.team3.logic.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.UserContainer
import de.se.team3.webservice.util.InstantSerializer
import de.se.team3.webservice.util.UserSerializer
import java.time.Instant

data class ProcessGroup(
    val id: Int?,
    @JsonSerialize(using = UserSerializer::class)
    var owner: User,
    var title: String,
    var description: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    val members: MutableList<User>
) {

    /**
     * Create-Constructor
     */
    constructor(ownerID: String, title: String, description: String) :
            this(null, UserContainer.getUser(ownerID), title, description, Instant.now(), ArrayList<User>())

    /**
     * Update-Constructor
     */
    constructor(id: Int, ownerId: String, title: String, description: String) :
            this (id, UserContainer.getUser(ownerId), title, description, Instant.now(), ArrayList<User>())

    /**
     * Checks whether the specified user is member of this process group.
     *
     * @return True if and only if the specified user is member of this process group.
     */
    fun hasMember(memberId: String): Boolean {
        return members.find { it.id == memberId } != null
    }

}
