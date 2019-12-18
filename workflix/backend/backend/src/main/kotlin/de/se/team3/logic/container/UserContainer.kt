package de.se.team3.logic.container

import de.se.team3.logic.domain.User
import de.se.team3.persistence.daos.UserDAO
import de.se.team3.webservice.containerInterfaces.UserContainerInterface

object UserContainer : UserContainerInterface {

    const val PAGESIZE = 20

    override fun getUsers(page: Int): Pair<List<User>, Int> {
        if (page < 1)
            throw IllegalArgumentException()

        val offset = (page - 1) * PAGESIZE
        val limit = PAGESIZE

        val result = UserDAO.getAllUsers(offset, limit)

        val lastPage = result.second / PAGESIZE + 1
        return Pair(result.first, lastPage)
    }
}
