package de.se.team3.logic.domain
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.ProcessGroupsContainer
import de.se.team3.logic.container.UserRoleContainer
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.***REMOVED***connector.UserQuerying
import de.se.team3.persistence.daos.UserDAO
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant

class User(
    val id: String,
    val name: String,
    val displayname: String,
    val email: String,
    var password: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant
) {

    /**
     * Create-Constructor
     */
    constructor(name: String, displayname: String, email: String, createdAt: Instant) :
        this("", name, displayname, email, "changeme", createdAt) {
            if (name.isEmpty() || displayname.isEmpty() || email.isEmpty())
                throw IllegalArgumentException("none of the arguments may be empty")
        }

    constructor(id: String, name: String, displayname: String, email: String, createdAt: Instant) :
            this(id, name, displayname, email, "changeme", createdAt) {
        if (id.isEmpty() || name.isEmpty() || displayname.isEmpty() || email.isEmpty())
            throw IllegalArgumentException("none of the arguments may be empty")
    }

    /**
     * @return All user roles the user is a member of.
     */
    @JsonIgnore
    fun getUserRoleIds(): List<Int> {
        return UserRoleContainer
            .getAllUserRoles()
            .filter { it.members.contains(this) }
            .map { it.id }
    }

    /**
     * @return All process groups the user is a member of.
     */
    @JsonIgnore // avoids cyclomatic call with process groups
    fun getProcessGroupIds(): List<Int> {
        return ProcessGroupsContainer
            .getAllProcessGroups()
            .filter { it.hasMember(id) }
            .map { it.id!! }
    }

    companion object {
        /**
         * Queries the ***REMOVED*** API for ***REMOVED*** Users registered under the given e-mail address.
         * @return User object corresponding to the ***REMOVED*** user using the given password.
         * @throws InvalidInputException Either the given e-mail address is not of a valid format, no ***REMOVED*** User
         * with this address can be found or the user does already exist.
         */
        fun query***REMOVED***andCreateUser(email: String, password: String): User {
            // checks whether email is a (syntactically) valid e-mail address
            if (!email.matches(Regex("""^\w+@\w+..{2,3}(.{2,3})?$""")))
                throw InvalidInputException("The e-mail address given is not of a valid format.")
            val user = UserQuerying.searchFor***REMOVED***User(email)
                ?: throw InvalidInputException("No user with this e-mail address exists.")
            // checks whether the user already exists
            if (UserDAO.getAllUsers().contains(user))
                throw InvalidInputException("This user already exists!")
            user.password = password
            return user
        }

        /**
         * Creates a user not existing in ***REMOVED*** System by generating a new 24-char-ID.
         * Note: It is theoretically possible that creating a non-***REMOVED*** user while some ***REMOVED*** users are not already added
         * to the system may result in conflicting IDs. The likelihood of this, however, is fairly small.
         */
        fun createNewUser(name: String, displayname: String, email: String, password: String): User {
            // checks whether email is a (syntactically) valid e-mail address
            if (!email.matches(Regex("""^\w+@\w+..{2,3}(.{2,3})?$""")))
                throw java.lang.IllegalArgumentException("The e-mail address given is not of a valid format.")
            var generatedID: String
            do {
                val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
                generatedID = (1..20)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            } while (!userIdAlreadyUsed(generatedID))
            return User("$generatedID-gen", name, displayname, email, password, Instant.now())
        }

        /**
         * checks whether a String is already in use as an user id
         * TODO(improvement) apply a faster searching algorithm; this may however require sorting the data first
         */
        fun userIdAlreadyUsed(id: String): Boolean {
            val userList = UserDAO.getAllUsers()
            for (user in userList)
                if (user.id == id) return true
            return false
        }
    }
}
