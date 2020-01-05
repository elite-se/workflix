package dbmocks

import de.se.team3.logic.DAOInterfaces.UserDAOInterface
import de.se.team3.logic.domain.User
import java.time.Instant

object UsersMock: UserDAOInterface {

    private val usersData = HashMap<String, UsersRow>()

    init {
        usersData.put("58c120552c94decf6cf3b700",
            UsersRow(
                "58c120552c94decf6cf3b700",
                "Elias Keis",
                "EK",
                "keis@gmx.net",
                false,
                Instant.now(),
                "Passwort123"
            ))
        usersData.put("58c120552c94decf6cf3b701",
            UsersRow(
                "58c120552c94decf6cf3b701",
                "Michael Markl",
                "MM",
                "markl@gmail.com",
                false,
                Instant.now(),
                "Passwort123"
            )
        )
        usersData.put("58c120552c94decf6cf3b702",
            UsersRow(
                "58c120552c94decf6cf3b701",
                "Erik Pallas",
                "EP",
                "pallas@web.de",
                false,
                Instant.now(),
                "Passwort123"
            )
        )
    }

    override fun getAllUsers(): List<User> {
        return usersData.map { it.value }.map {
            val user = User(
                it.id,
                it.name,
                it.displayname,
                it.email,
                it.password,
                it.createdAt
            )
            user
        }
    }

    override fun getUser(userId: String): User? {
        val row = usersData.map { it.value }.first { it.id == userId }
        return if (row != null)
            User(
                row.id,
                row.name,
                row.displayname,
                row.email,
                row.password,
                row.createdAt
            )
        else
            null
    }

    override fun createUser(user: User) {
        val usersRow = UsersRow(
            user.id,
            user.name,
            user.displayname,
            user.email,
            false,
            user.createdAt,
            user.password
        )
        usersData.put(user.id, usersRow)
    }

    override fun create***REMOVED***User(email: String, password: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUser(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUser(user: User): Boolean {
        val userRow = usersData.map { it.value }.first { it.id == user.id }
        if (userRow == null)
            return false

        userRow.deleted = true
        return true
    }
}