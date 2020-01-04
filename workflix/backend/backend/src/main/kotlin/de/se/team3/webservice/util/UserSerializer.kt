package de.se.team3.webservice.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import de.se.team3.logic.domain.User

class UserSerializer : JsonSerializer<User>() {

    override fun serialize(user: User, generator: JsonGenerator, serializers: SerializerProvider) {
        generator?.writeObject(user?.id)
    }

}