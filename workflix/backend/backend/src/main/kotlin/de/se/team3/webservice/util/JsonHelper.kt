package de.se.team3.webservice.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.Instant
import java.time.format.DateTimeFormatter
import org.json.JSONArray
import org.json.JSONObject

object JsonHelper {

    private val mapper = ObjectMapper().registerModule(KotlinModule())

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
            val formatter = DateTimeFormatter.ISO_INSTANT
            Instant.from(formatter.parse(value))
        }
    }
}
