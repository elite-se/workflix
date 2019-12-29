package de.se.team3.logic.container

import de.se.team3.logic.domain.User
import de.se.team3.persistence.daos.UserDAO
import de.se.team3.webservice.containerInterfaces.UserContainerInterface
import java.lang.IllegalArgumentException

object UserContainer : UserContainerInterface {

    const val PAGESIZE = 20

    override fun getAllUsers(page: Int): Pair<List<User>, Int> {
        if (page < 1)
            throw IllegalArgumentException()

        val offset = (page - 1) * PAGESIZE
        val limit = PAGESIZE

        val result = UserDAO.getAllUsers(offset, limit)

        val lastPage = result.second / PAGESIZE + 1
        if (page > lastPage)
            throw IllegalArgumentException()

        return Pair(result.first, lastPage)
    }

    override fun getAllUsers(): List<User> {
        return UserDAO.getAllUsers()
    }

    override fun getUser(userId: String): User {
        return UserDAO.getUser(userId)
    }
}
