package de.se.team3.logic.authentification

import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.User
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.exceptions.NotAuthorizedException

object LoginManager {
    val tokensInUse = ArrayList<AuthenticationToken>()
    // authorized user currently performing a request, or null if none is performed
    private var activeUser: User? = null

    /**
     * Logs a user in if email and password match the saved information.
     *
     * @param email The users email address. Note that these email addresses are considered unique.
     * @param password user's password
     *
     * @return User's authentification token if successful, null otherwise.
     *
     * @throws InvalidInputException if there is no or more than one user with this email address, or the user
     * is already logged in.
     */
    fun login(email: String, password: String): AuthenticationToken {
        val userList = UserContainer.getAllUsers().filter { it.email == email }
        // if there are more than two users with the same email address, something went terribly wrong during user creation
        if (userList.size < 1)
            throw InvalidInputException("There is no user with this email address.")
        if (userList.size > 1)
            throw InvalidInputException("There is more than one user with the same email address.")
        val user = userList.first()
        if (tokensInUse.map { it.user }.contains(user))
            throw InvalidInputException("This user is already logged in.")
        if (user.password != password)
            throw InvalidInputException("Wrong password.")
        val token = AuthenticationToken(user)
        tokensInUse.add(token)
        return token
    }

    /**
     * Logs a user out.
     *
     * @param bearerToken bearer token of the user to be logged out
     */
    fun logout(bearerToken: String) {
        val token = bearerToken.substringAfter(' ')
        if (!tokensInUse.remove(tokensInUse.firstOrNull() { it.token == token }))
            throw NotAuthorizedException("This user is not logged in.")
    }

    fun getActiveUser(): User? {
        return activeUser
    }

    /**
     * Sets the owner of the token as the active user.
     *
     * @param token Token of the user to be set as active.
     */
    fun setActiveUser(token: String) {
        activeUser = tokensInUse.firstOrNull { it.token == token }?.user
    }

    /**
     * Resets the active user to null if the token is valid.
     *
     * @param token Token used for authorization
     * @return true iff the token is valid
     */
    fun removeActiveUser(token: String): Boolean {
        return if (tokensInUse.firstOrNull { it.token == token }?.user == activeUser) {
            activeUser = null
            true
        } else
            false
    }
}
