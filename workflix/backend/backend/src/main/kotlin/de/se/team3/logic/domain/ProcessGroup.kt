package de.se.team3.logic.domain

import de.se.team3.logic.container.UserContainer
import java.time.LocalDate
import kotlin.collections.ArrayList

class ProcessGroup(
    val id: Int?,
    var owner: User,
    var title: String,
    var description: String,
    val createdAt: LocalDate,
    val processes: MutableList<Process>,
    val members: MutableList<User>
) {

    constructor(id: Int, owner: User, title: String, createdAt: LocalDate) :
        this(id, owner, title, "", createdAt, ArrayList<Process>(), ArrayList<User>())

    /**
     * Creates a new process group with no specified ID.
     */
    constructor(title: String, description: String, ownerID: String, createdAt: LocalDate) :
            this(null, UserContainer.getUser(ownerID), title, "", createdAt, ArrayList<Process>(), ArrayList<User>())

    /**
     * Creates a new process group, and adds a given list of users to it.
     */
    constructor(id: Int, owner: User, title: String, createdAt: LocalDate, members: MutableList<User>) :
        this(id, owner, title, "", createdAt, ArrayList<Process>(), members)
}
