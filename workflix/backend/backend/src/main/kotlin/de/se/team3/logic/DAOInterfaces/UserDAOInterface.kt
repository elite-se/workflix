package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.User

interface UserDAOInterface {

    /**
     * Returns a list of all users.
     */
    fun getAllUsers(): List<User>

    fun getUser(userId: String): User?

    fun createUser(user: User)

    fun create***REMOVED***User(email: String, password: String)

    fun updateUser(user: User)

    fun deleteUser(user: User): Boolean
}
