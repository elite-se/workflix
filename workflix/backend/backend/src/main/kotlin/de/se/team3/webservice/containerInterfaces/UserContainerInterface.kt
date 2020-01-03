package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.User

interface UserContainerInterface {
    fun getAllUsers(): List<User>

    fun getUser(userId: String): User

    fun create***REMOVED***User(email: String, password: String): User
}
