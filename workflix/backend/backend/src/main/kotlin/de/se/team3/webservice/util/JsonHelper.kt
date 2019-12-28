package de.se.team3.webservice.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.se.team3.logic.domain.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import org.json.JSONArray
import org.json.JSONObject

object JsonHelper {
    val mapper = ObjectMapper().registerModule(KotlinModule())

    fun toJsonObject(obj: Any): JSONObject {
        val json = mapper.writeValueAsString(obj)
        return JSONObject(json)
    }

    fun toJsonArray(obj: Any): JSONArray {
        val json = mapper.writeValueAsString(obj)
        return JSONArray(json)
    }

    fun <T> fromJson(content: String, valueType: Class<T>): T {
        return mapper.readValue(content, valueType)
    }

    fun getInstantFromString(value: String?): Instant? {
        return if (value == null) null else {
            LocalDateTime.parse(
                value,
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.GERMANY)
            ).toInstant(ZoneOffset.UTC)
        }
    }
}

class UserSerializer : JsonSerializer<User>() {
    override fun serialize(user: User, generator: JsonGenerator, serializers: SerializerProvider) {
        generator?.writeObject(user?.id)
    }
}

/*class UserDeserializer: StdDeserializer<User>(User::class.java) {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): User {
        val ownerId = parser.readValueAs(String::class.java)
        return UserContainer.getUser(ownerId)
    }
}*/

class InstantSerializer : JsonSerializer<Instant>() {
    override fun serialize(time: Instant, generator: JsonGenerator, serializers: SerializerProvider) {
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.GERMANY).withZone(
            ZoneId.systemDefault())

        generator.writeObject(formatter.format(time))
    }
}
