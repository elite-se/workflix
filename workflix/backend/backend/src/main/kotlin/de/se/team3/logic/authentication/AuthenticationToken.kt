package de.se.team3.logic.authentication

import de.se.team3.logic.domain.User
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.json.JSONObject

class AuthenticationToken(
    val token: String,
    val user: User
) {
    /**
     * Creates an authentification token for a given user.
     *
     * @param user user whose token will be generated
     */
    constructor(user: User) :
            this(generateToken(user.id), user)

    companion object {
        private const val key = "***REMOVED***"

        /**
         * Generates a JSONWebToken from a given UserID
         *
         * @param userID basis for generated Token
         * @return JWT as a String
         */
        private fun generateToken(userID: String): String {
            val headerJSON = JSONObject()
            headerJSON.put("alg", "HS256")
            headerJSON.put("typ", "JWT")
            val headerB64 = Base64.getEncoder().encodeToString(headerJSON.toString().toByteArray())

            val payloadJSON = JSONObject()
            payloadJSON.put("userId", userID)
            val payloadB64 = Base64.getEncoder().encodeToString(payloadJSON.toString().toByteArray())

            val headerPayloadB64 = "$headerB64.$payloadB64"

            val sha256Hmac = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA256")
            sha256Hmac.init(secretKey)
            val signatureB64 = Base64.getEncoder().encodeToString(sha256Hmac.doFinal(headerPayloadB64.toByteArray()))

            return "$headerB64.$payloadB64.$signatureB64"
        }
    }
}
