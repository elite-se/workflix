package unittests

import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.UserRole
import java.time.Instant

object TestData {

    val marvinBrieger = User("58c120552c94decf6cf3b700", "Marvin Brieger", "MB", "mail@marvinbrieger.de", "Passwort123", Instant.now())
    val eliasKeis = User("58c120552c94decf6cf3b701", "Elias Keis", "EK", "ek@gmx.net", "Passwort123", Instant.now())
    val michaelMarkl = User("58c120552c94decf6cf3b702", "Michael Markl", "MK", "mk@gmail.com", "Passwort123", Instant.now())

    fun getSomeUsers(): List<User> {
        val users = ArrayList<User>()

        users.add(marvinBrieger)
        users.add(eliasKeis)
        users.add(michaelMarkl)

        return users


    }

    fun getBuchhalter(): UserRole {
        val accountants = ArrayList<User>()
        accountants.add(marvinBrieger)
        accountants.add(eliasKeis)

        val accountantRole = UserRole(1, "Accountant", "Role for accountants", Instant.now(), false, accountants)
        return accountantRole
    }

}