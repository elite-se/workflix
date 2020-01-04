package de.se.team3.logic.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.webservice.util.InstantSerializer
import de.se.team3.webservice.util.UserSerializer
import java.time.Instant

data class ProcessGroup(
    val id: Int?,
    @JsonSerialize(using = UserSerializer::class)
    private var owner: User,
    private var title: String,
    private var description: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    private val members: MutableList<User>
) {

    fun getOwner() = owner

    fun getTitle() = title

    fun getDescription() = description

    fun getMembers() = members.toList()

    /**
     * Create-Constructor
     */
    constructor(ownerID: String, title: String, description: String) :
            this(null, UserContainer.getUser(ownerID), title, description, Instant.now(), ArrayList<User>()) {

        setTitle(title)
        setOwnerById(ownerID)
    }

    /**
     * Update-Constructor
     */
    constructor(id: Int, ownerId: String, title: String, description: String) :
            this (id, UserContainer.getUser(ownerId), title, description, Instant.now(), ArrayList<User>()) {

        if (id < 1)
            throw InvalidInputException("id must be positive")

        setTitle(title)
        setOwnerById(ownerId)
    }

    /**
     * Sets the title.
     *
     * @throws InvalidInputException Is thrown if the given title is empty.
     */
    fun setTitle(title: String) {
        if (title.isEmpty())
            throw InvalidInputException("title must not be empty")

        this.title = title
    }

    /**
     * Sets the description.
     */
    fun setDescription(description: String) {
        this.description = description
    }

    /**
     * Sets the owner by the given id.
     *
     * @throws InvalidInputException Is thrown if ownerId is not a valid user id.
     */
    fun setOwnerById(ownerId: String) {
        if (ownerId.length != 24)
            throw InvalidInputException("invalid user id")

        owner = UserContainer.getUser(ownerId)
    }

    /**
     * Checks whether the specified user is member of this process group.
     *
     * @return True if and only if the specified user is member of this process group.
     */
    fun hasMember(memberId: String): Boolean {
        return members.find { it.id == memberId } != null
    }

    /**
     * Adds the given user as member to this group.
     *
     * @throws AlreadyExistsException Is thrown if the given user is already a member of this process group.
     */
    fun addMember(user: User) {
        if (hasMember(user.id))
            throw AlreadyExistsException("the given user is already a member of this process group")

        members.add(user)
    }

    /**
     * Deletes the specified member.
     *
     * @throws NotFoundException Is thrown if the specified member does not exist.
     */
    fun removeMember(memberId: String) {
        val existed = members.removeIf { it.id == memberId }
        if (!existed)
            throw NotFoundException("member does not exist")
    }

}
