package de.se.team3.logic.authentification

import de.se.team3.logic.domain.User
import java.util.Base64

class AuthentificationToken(val token: String,
                            val user: User) {

    /**
     * Creates an authentification token for a given user.
     *
     * @param user user whose token will be generated
     */
    constructor(user: User):
            this(Base64.getEncoder().encodeToString(user.id.toByteArray()), user)
}