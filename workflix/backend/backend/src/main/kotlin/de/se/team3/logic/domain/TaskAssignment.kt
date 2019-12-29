package de.se.team3.logic.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant

/**
 * Represents a task assignment.
 */
class TaskAssignment(
    val id: Int?,
    val taskId: Int,
    val asigneeId: String,
    val closed: Boolean,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    val doneAt: Instant?
) {

    /**
     * Create-Constructor
     */
    constructor(taskId: Int, asigneeId: String, immediateClosing: Boolean) :
            this(
                null,
                taskId,
                asigneeId,
                if (immediateClosing) true else false,
                Instant.now(),
                if (immediateClosing) Instant.now() else null
            ) {


    }
}
