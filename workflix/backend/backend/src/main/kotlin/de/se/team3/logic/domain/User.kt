package de.se.team3.logic.domain

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

    // TODO get all groups the user is a member of from the UserContainer
}
