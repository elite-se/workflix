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
        this(id, owner, title, "", createdAt, ArrayList<Process>(), members) {
            for (user in members)
                user.addProcessGroup(this)
    }

    /**
     * Adds a user as a member of the process group.
     * To contain integrity, the corresponding method of the respective user is called.
     * Endless cyclic calls are stopped by checking whether the user was already added.
    */
    fun addMember(user: User) {
        if (!members.contains(user)) {
            members.add(user)
            user.addProcessGroup(this)
        }
    }

    /**
     * Adds a process to the group.
     * So far, his should be only called from the Process constructor.
     */
    fun addProcess(process: Process) {
        processes.add(process)
    }
}
