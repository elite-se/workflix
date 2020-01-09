package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.TasksContainer
import de.se.team3.logic.exceptions.AlreadyClosedException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.logic.exceptions.UnsatisfiedPreconditionException
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
    @JsonIgnore
    val assignee: User,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    @JsonSerialize(using = InstantSerializer::class)
    private var doneAt: Instant?
) {

    val assigneeId = assignee.id

    fun getTask() = task!!

    @get:JsonIgnore
    val taskId by lazy { task!!.id!! }

    fun getDoneAt() = doneAt

    /**
     * Create-Constructor
     */
    constructor(
        task: Task,
        assignee: User,
        immediateClosing: Boolean
    ) : this(
        null,
        task,
        assignee,
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
     * @throws UnsatisfiedPreconditionException Is thrown if the predecessors of the given task are not already closed.
     * @throws AlreadyClosedException Is thrown if the given task is already closed.
     */
    fun close(closingTime: Instant) {
        if (isClosed())
            throw AlreadyClosedException("task assignment is already closed")
        if (task!!.isBlocked())
            throw UnsatisfiedPreconditionException("the assignment can only be closed if all predecessors have been closed")
        if (task!!.isClosed())
            throw AlreadyClosedException("task is already closed")

        doneAt = closingTime
    }
}
