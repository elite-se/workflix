package de.se.team3.logic.authentication

object AuthorizationManager {

    /**
     * Verifies a request and authorizes it if possible.
     *
     * @param bearerToken Token of the form "Bearer $token"
     * @return true iff the verification was successful
     */
    fun authorizeRequest(bearerToken: String): Boolean {
        val stringToken = bearerToken.substringAfter(' ')
        return tokenIsRegistered(stringToken)
    }

    /**
     * @param token token used as verification
     * @return true iff the given token is registered as in use
     */
    private fun tokenIsRegistered(token: String): Boolean {
        return LoginManager.tokensInUse.map { it.token }.contains(token)
    }
}
