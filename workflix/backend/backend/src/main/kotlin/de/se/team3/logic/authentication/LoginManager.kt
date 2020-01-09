package de.se.team3.logic.authentication

import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.exceptions.InvalidInputException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object LoginManager {
    val tokensInUse = ArrayList<AuthenticationToken>()

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
            return tokensInUse.first { it.user == user }
        if (!BCryptPasswordEncoder().matches(password, user.password))
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
        tokensInUse.remove(tokensInUse.firstOrNull() { it.token == token })
    }
}
