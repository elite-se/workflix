package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.TasksContainer
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant
import kotlin.IllegalStateException

/**
 * Represents a task assignment.
 */
data class TaskAssignment(
    val id: Int?,
    @JsonIgnore
    val taskId: Int,
    val assigneeId: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    @JsonSerialize(using = InstantSerializer::class)
    private var doneAt: Instant?
) {

    fun getDoneAt() = doneAt

    @get:JsonIgnore
    val task by lazy { TasksContainer.getTask(taskId) }

    /**
     * Create-Constructor
     */
    constructor(
        taskId: Int,
        asigneeId: String,
        immediateClosing: Boolean
    ) : this(
        null,
        taskId,
        asigneeId,
        Instant.now(),
        if (immediateClosing) Instant.now() else null
    )

    /**
     * Checks whether the the task assignment is closed or not.
     *
     * @return True if the task assignment is closed.
     */
    @JsonProperty("closed")
    fun closed() = doneAt != null

    /**
     * Closes the task assignment if possible.
     *
     * @throws IllegalStateException Is thrown if the task assignment is already closed.
     */
    fun close(closingTime: Instant) {
        if (closed())
            throw IllegalStateException("task assignment is already closed")

        doneAt = closingTime
    }

}
