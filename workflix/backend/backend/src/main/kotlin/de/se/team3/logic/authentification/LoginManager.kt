package de.se.team3.logic.authentification

object LoginManager {

    /**
     * Logs a user in if username and password match the saved information.
     *
     * @param username currently the users email address
     * @param password user's password
     *
     * @return User's authentification token if successful, null otherwise.
     */
    fun login(username: String, password: String): AuthentificationToken? {
        TODO()
    }

    /**
     * Logs a user out.
     *
     * @param username name of the user to be logged out (i.e., currently the users email address)
     */
    fun logout(username: String) {
        TODO()
    }
}