package de.se.team3.logic.authentification

import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.User
import de.se.team3.logic.exceptions.InvalidInputException

object LoginManager {
    val loggedInUsers = ArrayList<User>()
    val tokensInUse = ArrayList<AuthentificationToken>()

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
    fun login(email: String, password: String): AuthentificationToken {
        val userList = UserContainer.getAllUsers().filter { it.email == email }
        //if there are more than two users with the same email address, something went terribly wrong during user creation
        if (userList.size < 1)
            throw InvalidInputException("There is no user with this email address.")
        if (userList.size > 1)
            throw InvalidInputException("There is more than one user with the same email address.")
        val user = userList.first()
        if (loggedInUsers.contains(user))
            throw InvalidInputException("This user is already logged in.")
        val token = AuthentificationToken(user)
        tokensInUse.add(token)
        return token
    }

    /**
     * Logs a user out.
     *
     * @param username name of the user to be logged out (i.e., currently the users email address)
     */
    fun logout(token: AuthentificationToken) {
        loggedInUsers.remove(token.user)
        tokensInUse.remove(token)
    }
}