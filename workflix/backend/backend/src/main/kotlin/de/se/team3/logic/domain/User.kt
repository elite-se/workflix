package de.se.team3.logic.domain

class User(
    val id: String,
    val name: String,
    val displayname: String,
    val email: String,
    val processGroups: MutableList<ProcessGroup>
) {

    /**
     * Create-Constructor
     */
    constructor(name: String, displayname: String, email: String, processGroups: MutableList<ProcessGroup>) :
        this("", name, displayname, email, processGroups) {
            if (name.length == 0 || displayname.length == 0 || email.length == 0)
                throw IllegalArgumentException("not all arguments may be empty")
        }

    /**
     * Creates a new user with no assigned process groups.
     */
    constructor(name: String, displayname: String, email: String) :
            this("", name, displayname, email, ArrayList<ProcessGroup>()) {
        if (name.length == 0 || displayname.length == 0 || email.length == 0)
            throw IllegalArgumentException("not all arguments may be empty")
    }

    /**
     * Adds user to a process group.
     * To contain integrity, the corresponding method of the respective process group is called.
     * Endless cyclic calls are stopped by checking whether the group was already added.
     */
    fun addProcessGroup(processGroup: ProcessGroup) {
        if (!processGroups.contains(processGroup)) {
            processGroups.add(processGroup)
            processGroup.addMember(this)
        }
    }
}
