package de.se.team3.logic.domain

import de.se.team3.logic.***REMOVED***connector.UserQuerying
import de.se.team3.persistence.daos.UserDAO

class User(id: String, name: String, displayname: String, email: String) {
    val id = id
    val name = name
    val displayname = displayname
    val email = email

    /**
     * Create-Constructor
     */
    constructor(name: String, displayname: String, email: String) : this("", name, displayname, email) {
        if (name.length == 0 || displayname.length == 0 || email.length == 0)
            throw IllegalArgumentException("not all arguments may be empty")
    }

    companion object {
        /**
         * queries the ***REMOVED*** API for ***REMOVED*** Users registered under the given e-mail address
         * throws exceptions if either the given e-mail address is not of a valid format, or no ***REMOVED*** User
         * with this address can be found
         */
        fun query***REMOVED***andCreateUser(email: String): User {
            // checks whether email is a (syntactically) valid e-mail address
            if (!email.matches(Regex("""^\w+@\w+..{2,3}(.{2,3})?$""")))
                throw java.lang.IllegalArgumentException("The e-mail address given is not of a valid format.")
            val user = UserQuerying.searchFor***REMOVED***User(email)
                ?: throw java.lang.IllegalArgumentException("No user with this e-mail address exists.")
            // checks whether the user already exists
            var i = 0
            var userList = UserDAO.getAllUsers(i, i + 20).first
            while (userList.isNotEmpty()) {
                if (userList.contains(user))
                    throw java.lang.IllegalArgumentException("This user already exists!")
                i += 20
                userList = UserDAO.getAllUsers(i, i + 20).first
            }
            return user
        }

        /**
         * Creates a user not existing in ***REMOVED*** System by generating a new 24-char-ID.
         * Note: It is theoretically possible that creating a non-***REMOVED*** user while some ***REMOVED*** users are not already added
         * to the system may result in conflicting IDs. The likelihood of this, however, is fairly small.
         */
        fun createNewUser(name: String, displayname: String, email: String): User {
            // checks whether email is a (syntactically) valid e-mail address
            if (!email.matches(Regex("""^\w+@\w+..{2,3}(.{2,3})?$""")))
                throw java.lang.IllegalArgumentException("The e-mail address given is not of a valid format.")
            var generatedID = ""
            do {
                val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
                generatedID = (1..24)
                    .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            } while (!userIdAlreadyUsed(generatedID))
            return User(generatedID, name, displayname, email)
        }

        /**
         * checks whether a String is already in use as an user id
         * TODO(improvement) apply a faster searching algorithm; this may however require sorting the data first
         */
        fun userIdAlreadyUsed(id: String): Boolean {
            var i = 0
            var userList = UserDAO.getAllUsers(i, i + 20).first
            while (userList.isNotEmpty()) {
                for (user in userList)
                    if (user.id == id) return true
                userList = UserDAO.getAllUsers(i, i + 20).first
            }
            return false
        }
    }
}
