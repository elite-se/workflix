package de.se.team3.logic.domain

import java.time.Instant

/**
 * Represents a task assignment.
 */
class TaskAssignment(
    val id: Int?,
    val asigneeId: String,
    val status: AssignmentStatus,
    val createdAt: Instant,
    val doneAt: Instant?
) {

    /**
     * Create-Constructor
     */
    constructor(asigneeId: String, status: AssignmentStatus, createdAt: Instant, doneAt: Instant?) :
            this(null, asigneeId, status, Instant.now(), null) {
    }
}
