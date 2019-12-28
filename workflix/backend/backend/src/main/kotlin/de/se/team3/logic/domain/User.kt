package de.se.team3.logic.domain

import de.se.team3.logic.container.ProcessGroupContainer

class User(
    val id: String,
    val name: String,
    val displayname: String,
    val email: String
) {

    /**
     * Create-Constructor
     */
    constructor(name: String, displayname: String, email: String) :
        this("", name, displayname, email) {
            if (name.length == 0 || displayname.length == 0 || email.length == 0)
                throw IllegalArgumentException("none of the arguments may be empty")
        }

    /**
     * Returns all process groups the user is a member of.
     * Using the try-catch-block here probably is kinda hacky...
     */
    fun getMemberships(): List<ProcessGroup> {
        val allGroups = ProcessGroupContainer.getAllProcessGroups()
        return allGroups.filter { it.members.contains(this) }
    }
}
