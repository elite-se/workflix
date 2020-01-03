package de.se.team3.logic.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.webservice.util.InstantSerializer
import java.lang.NullPointerException
import java.time.Instant

/**
 * Represents a task comment.
 */
data class TaskComment(
    val id: Int?,
    val taskId: Int?,
    val creatorId: String?,
    private var content: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant
) {

    fun getContent() = content

    init {
        if (id == null && (taskId == null || creatorId == null))
            throw NullPointerException("either id or task id and creator id must not be null")

        if (content.isEmpty())
            throw InvalidInputException("content must not be empty")
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

    /**
     * Sets the given content.
     *
     * @throws InvalidInputException Is thrown if the given content ist empty.
     */
    fun setContent(content: String) {
        if (content.isEmpty())
            throw InvalidInputException("content must not be empty")

        this.content = content
    }
}
