package de.se.team3.webservice.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.Instant
import java.time.format.DateTimeFormatter

class InstantSerializer : JsonSerializer<Instant>() {

    override fun serialize(time: Instant, generator: JsonGenerator, serializers: SerializerProvider) {
        val formatter = DateTimeFormatter.ISO_INSTANT

        generator.writeObject(formatter.format(time))
    }

}