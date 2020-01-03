package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.TasksContainer
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant

/**
 * Represents a task assignment.
 */
class TaskAssignment(
    val id: Int?,
    @JsonIgnore
    val taskId: Int,
    val asigneeId: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    @JsonSerialize(using = InstantSerializer::class)
    val doneAt: Instant?
) {

    val closed = if (doneAt == null) false else true

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
}
