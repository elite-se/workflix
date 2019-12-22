package de.se.team3.logic.domain

class ProcessGroup(
    val id: Int,
    val title: String,
    val processes: MutableList<Process>, //TODO(persistence): implement relationship in database
    val members: MutableList<User> //TODO(persistence): implement relationship in database
) {

    constructor(id: Int, title: String) :
        this(id, title, ArrayList<Process>(), ArrayList<User>())

    /**
     * Creates a new process group, and adds a given list of users to it.
     */
    constructor(id: Int, title: String, members: MutableList<User>) :
        this(id, title, ArrayList<Process>(), members) {
            for (user in members)
                user.addProcessGroup(this)
    }


    /**
     * Adds a user as a member of the process group.
     */
    fun addMember(user: User) {
        members.add(user)
    }

    /**
     * Adds a process to the group.
     * So far, his should be only called from the Process constructor.
     */
    fun addProcess(process: Process) {
        processes.add(process)
    }
}
