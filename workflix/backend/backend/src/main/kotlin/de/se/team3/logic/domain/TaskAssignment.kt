package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.TasksContainer
import de.se.team3.logic.exceptions.AlreadyClosedException
import de.se.team3.webservice.util.InstantSerializer
import java.lang.IllegalStateException
import java.time.Instant

/**
 * Represents a task assignment.
 */
data class TaskAssignment(
    val id: Int?,
    @JsonIgnore
    private var task: Task?,
    val assigneeId: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    @JsonSerialize(using = InstantSerializer::class)
    private var doneAt: Instant?
) {

    fun getTask() = task!!

    @JsonIgnore
    val taskId = task!!.id!!

    fun getDoneAt() = doneAt

    /**
     * Create-Constructor
     */
    constructor(
        task: Task,
        asigneeId: String,
        immediateClosing: Boolean
    ) : this(
        null,
        task,
        asigneeId,
        Instant.now(),
        if (immediateClosing) Instant.now() else null
    )

    /**
     * Sets the task.
     *
     * @throws IllegalStateException Is thrown if the task was already set before.
     */
    fun setTask(task: Task) {
        if (this.task != null)
            throw IllegalStateException("task is already set")

        this.task = task
    }

    /**
     * Checks whether the the task assignment is closed or not.
     *
     * @return True if the task assignment is closed.
     */
    @JsonProperty("closed")
    fun isClosed() = doneAt != null

    /**
     * Closes the task assignment if possible.
     *
     * @throws AlreadyClosedException Is thrown if the task assignment is already closed.
     */
    fun close(closingTime: Instant) {
        if (isClosed())
            throw AlreadyClosedException("task assignment is already closed")

        doneAt = closingTime
    }
}
