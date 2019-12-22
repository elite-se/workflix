package de.se.team3.logic.domain

class User(val id: String,
           val name: String,
           val displayname: String,
           val email: String,
           val processGroups: MutableList<ProcessGroup>) {

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

    fun addProcessGroup(processGroup: ProcessGroup) {
        processGroups.add(processGroup)
    }
}
