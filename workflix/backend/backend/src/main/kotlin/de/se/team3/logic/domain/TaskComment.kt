package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.webservice.util.InstantSerializer
import java.lang.IllegalStateException
import java.lang.NullPointerException
import java.time.Instant

/**
 * Represents a task comment.s
 */
data class TaskComment(
    val id: Int?,
    @JsonIgnore
    private var task: Task?,
    @JsonIgnore
    val creator: User,
    private var content: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant
) {

    @get:JsonIgnore
    val taskId by lazy { task!!.id }

    val creatorId = creator.id

    fun getContent() = content

    init {
        if (content.isEmpty())
            throw InvalidInputException("content must not be empty")
    }

    /**
     * Create-Constructor
     */
    constructor(task: Task, creator: User, content: String) :
            this(null, task, creator, content, Instant.now())

    /**
     * Update-Constructor
     */
    constructor(id: Int, task: Task, creator: User, content: String) :
            this(id, task, creator, content, Instant.now())

    /**
     * Sets the task.
     */
    fun setTask(task: Task) {
        if (this.task != null)
            throw IllegalStateException("task is already set")

        this.task = task
    }

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
