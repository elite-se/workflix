package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.User

interface UserContainerInterface {
    fun getAllUsers(): List<User>

    fun getUser(userId: String): User

    fun createUser(name: String, displayname: String, email: String, password: String): User
}
