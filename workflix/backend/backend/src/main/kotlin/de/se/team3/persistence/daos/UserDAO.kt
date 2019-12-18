package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.UserDAOInterface
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.Users
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.select

object UserDAO : UserDAOInterface {

    /**
     * {@inheritDoc}
     */
    override fun getAllUsers(offset: Int, limit: Int): Pair<List<User>, Int> {
        val users = ArrayList<User>()
        val result = Users.select().limit(offset, limit)
        for (row in result)
            users.add(User(row[Users.ID]!!, row[Users.name]!!, row[Users.displayname]!!, row[Users.email]!!))

        return Pair(users.toList(), result.totalRecords)
    }

    override fun createUser(user: User) {
    }

    override fun updateUser(user: User) {
    }

    override fun deleteUser(user: User) {
    }
}
