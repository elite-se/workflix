package de.se.team3.logic.container

import de.se.team3.logic.domain.User
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.UserDAO
import de.se.team3.webservice.containerInterfaces.UserContainerInterface
import java.lang.IllegalArgumentException

object UserContainer : UserContainerInterface {

    //caches users using UserIDs
    private val userCache = HashMap<String, User>()

    override fun getAllUsers(): List<User> {
        return UserDAO.getAllUsers()
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

    override fun create***REMOVED***User(email: String, password: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
