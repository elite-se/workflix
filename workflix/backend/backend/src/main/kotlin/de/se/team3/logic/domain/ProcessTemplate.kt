package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import de.se.team3.logic.container.UserContainer
import java.lang.IllegalArgumentException

/**
 * Represents a process template.
 */
class ProcessTemplate(
    val id: Int?,
    val title: String,
    val durationLimit: Int?,
    val owner: User,
    @JsonIgnore val taskTemplates: Map<Int, TaskTemplate>?
) {

    /**
     * Create-Constructor
     */
    constructor(title: String, durationLimit: Int?, ownerId: String, taskTemplates: Map<Int, TaskTemplate>) :
            this(null, title, durationLimit, UserContainer.getUser(ownerId), taskTemplates) {

        if (title.isEmpty())
            throw IllegalArgumentException("title must not be empty")
        if (durationLimit != null && durationLimit <= 0)
            throw IllegalArgumentException("duration limit must be positive")
        if (taskTemplates == null)
            throw NullPointerException()
        if (taskTemplates.isEmpty())
            throw IllegalArgumentException("must contain at least one task template")
        if (!ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
            throw IllegalArgumentException("connection between task templates must be acyclic")
    }

    /**
     * Simple-Constructor that does not consider all details.
     */
    constructor(id: Int, title: String, durationLimit: Int?, owner: User) :
            this(id, title, durationLimit, owner, null)
}