package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.User

interface UserDAOInterface {

    /**
     * Returns a list of users specified by offset and limit as well as the total amount of users in the database.
     */
    fun getAllUsers(offset: Int, limit: Int): Pair<List<User>, Int>

    fun getUser(userId: String): User

    fun createUser(user: User)

    fun updateUser(user: User)

    fun deleteUser(user: User)

}