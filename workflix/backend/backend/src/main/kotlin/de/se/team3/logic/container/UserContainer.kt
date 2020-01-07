package de.se.team3.logic.container

import de.se.team3.logic.domain.User
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.UserDAO
import de.se.team3.webservice.containerInterfaces.UserContainerInterface

object UserContainer : UserContainerInterface {

    private val userContainer: UserContainerInterface = UserContainer

    // caches users using UserIDs
    private val userCache = HashMap<String, User>()

    // Indicates whether the cache is already filled with all elements
    private var filled = false

    /**
     * Ensures that all processes are cached.
     */
    private fun fillCache() {
        val users = UserDAO.getAllUsers()
        users.forEach { user ->
            userCache.put(user.id, user)
        }
        filled = true
    }

    override fun getAllUsers(): List<User> {
        if (!filled)
            fillCache()

        return userCache.map { it.value }.toList()
    }

    override fun getUser(userId: String): User {
        return if (userCache.containsKey(userId)) {
            userCache[userId]!!
        } else {
            val user = UserDAO.getUser(userId)
                ?: throw NotFoundException("user does not exist")

            userCache[userId] = user
            user
        }
    }

    /**
     * Checks whether the specified user exists or not.
     *
     * @return True if and only if the specified user exists.
     */
    fun hasUser(userId: String): Boolean {
        if (userCache.containsKey(userId))
            return true

        val user = UserDAO.getUser(userId)
        if (user != null) {
            userCache.put(userId, user)
            return true
        }

        return false
    }

    override fun create***REMOVED***User(email: String, password: String): User {
        val user = UserDAO.create***REMOVED***User(email, password)
        userCache[user.id] = user
        return user
    }
}
