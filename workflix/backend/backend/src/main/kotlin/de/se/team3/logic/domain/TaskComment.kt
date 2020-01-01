package de.se.team3.logic.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.webservice.util.InstantSerializer
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.time.Instant

/**
 * Represents a task comment.
 */
class TaskComment(
    val id: Int?,
    val taskId: Int?,
    val creatorId: String?,
    val content: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant
) {

    init {
        if (id == null && (taskId == null || creatorId == null))
            throw NullPointerException("either id or task id and creator id must not be null")

        if (content.isEmpty())
            throw IllegalArgumentException("content must not be empty")
    }

    /**
     * Create-Constructor
     */
    constructor(taskId: Int, creatorId: String, content: String) :
            this(null, taskId, creatorId, content, Instant.now())

    /**
     * Update-Constructor
     */
    constructor(id: Int, content: String) :
            this(id, null, null, content, Instant.now())
}
