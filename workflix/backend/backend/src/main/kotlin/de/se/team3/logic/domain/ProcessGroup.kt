package de.se.team3.logic.domain

import java.time.LocalDate
import kotlin.collections.ArrayList

class ProcessGroup(
    val id: Int,
    val owner: User,
    val title: String,
    val description: String,
    val createdAt: LocalDate,
    val processes: MutableList<Process>,
    val members: MutableList<User>
) {

    constructor(id: Int, owner: User, title: String, createdAt: LocalDate) :
        this(id, owner, title, "", createdAt, ArrayList<Process>(), ArrayList<User>())

    /**
     * Creates a new process group, and adds a given list of users to it.
     */
    constructor(id: Int, owner: User, title: String, createdAt: LocalDate, members: MutableList<User>) :
        this(id, owner, title, "", createdAt, ArrayList<Process>(), members)
}
