package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.UserDAOInterface
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.UsersTable
import java.util.Arrays
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.like
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object UserDAO : UserDAOInterface {

    /**
     * {@inheritDoc}
     */
    override fun getAllUsers(): List<User> {
        val users = ArrayList<User>()
        val result = UsersTable.select()
        for (row in result)
            users.add(User(row[UsersTable.ID]!!,
                row[UsersTable.name]!!,
                row[UsersTable.displayname]!!,
                row[UsersTable.email]!!,
                row[UsersTable.password] ?: "N/A",
                row[UsersTable.createdAt]!!)
            )

        return users.toList()
    }

    override fun getUser(userId: String): User? {
        val result = UsersTable
            .select()
            .where { UsersTable.ID eq userId }

        val row = result.rowSet
        if (!row.next())
            return null
        return User(row[UsersTable.ID]!!,
            row[UsersTable.name]!!,
            row[UsersTable.displayname]!!,
            row[UsersTable.email]!!,
            row[UsersTable.password] ?: "N/A",
            row[UsersTable.createdAt]!!)
    }

    override fun createUser(user: User) {
        UsersTable.insert {
            it.ID to user.id
            it.name to user.name
            it.displayname to user.displayname
            it.email to user.email
            it.password to user.password
            it.createdAt to user.createdAt
            it.deleted to false
        }
    }

    /**
     * Updates the user data on basis of the given user's id.
     */
    override fun updateUser(user: User) {
        UsersTable.update {
            it.name to user.name
            it.displayname to user.displayname
            it.email to user.email
            it.password to user.password
            it.deleted to false

            where { it.ID like user.id }
        }
    }

    override fun deleteUser(user: User): Boolean {
        return UsersTable.update {
            it.deleted to true
            where { it.ID like user.id }
        } != 0
    }
}
