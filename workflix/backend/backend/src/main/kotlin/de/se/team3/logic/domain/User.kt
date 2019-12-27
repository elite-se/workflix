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
                throw IllegalArgumentException("not all arguments may be empty")
        }

    /**
     * Returns all process groups the user is a member of.
     * Using the try-catch-block here probably is kinda hacky...
     */
    fun getMemberships(): MutableList<ProcessGroup> {
        val memberGroups = ArrayList<ProcessGroup>()
        try {
            var pageNo = 1
            var pageGroups = ProcessGroupContainer.getAllProcessGroups(pageNo).first
            while (!pageGroups.isEmpty()) {
                memberGroups.addAll(pageGroups.filter { it.members.contains(this) })
            }
        } catch (e: Error) {
            return memberGroups
        }
        return memberGroups
    }
}
