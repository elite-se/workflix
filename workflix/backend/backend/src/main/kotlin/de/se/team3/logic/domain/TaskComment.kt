package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import de.se.team3.logic.container.UserContainer
import java.lang.IllegalArgumentException
import java.time.Instant

/**
 * Represents a task comment.
 */
class TaskComment(
    id: Int?,
    creatorId: String,
    title: String,
    content: String,
    createdAt: Instant
) {

    @get:JsonIgnore
    val creator by lazy { UserContainer.getUser(creatorId) }

    /**
     * Create-Constructor
     */
    constructor(creatorId: String, title: String, content: String) :
            this(null, creatorId, title, content, Instant.now()) {

        if (title.isEmpty())
            throw IllegalArgumentException("title must be empty")
    }
}
