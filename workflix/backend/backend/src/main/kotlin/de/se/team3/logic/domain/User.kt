package de.se.team3.logic.domain

class User(id: String, name: String, displayname: String, email: String) {
    val id = id
    val name = name
    val displayname = displayname
    val email = email

    constructor(name: String, displayname: String, email: String): this("", name, displayname, email) {
        if (name.length == 0 || displayname.length == 0 || email.length == 0)
            throw IllegalArgumentException("all arugments must not be empty")
    }

}