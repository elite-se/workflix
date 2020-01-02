package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.processTemplateUtil.ProcessTemplateCycleDetection
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.webservice.util.InstantSerializer
import de.se.team3.webservice.util.UserSerializer
import java.time.Instant

/**
 * Represents a process template.
 */
data class ProcessTemplate(
    val id: Int?,
    val title: String,
    val description: String,
    val durationLimit: Int?,
    @JsonProperty("ownerId")
    @JsonSerialize(using = UserSerializer::class)
    val owner: User,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    val formerVersionId: Int?,
    var processCount: Int,
    var runningProcesses: Int,
    val deleted: Boolean,
    @JsonIgnore
    val taskTemplates: Map<Int, TaskTemplate>?
) {

    @get:JsonIgnore
    val taskTemplatesList by lazy { taskTemplates!!.map { it.value } }

    @get:JsonIgnore
    val estimatedDurationSum by lazy {
        var sum = 0
        taskTemplates?.forEach { id, taskTemplate ->
            sum += taskTemplate.estimatedDuration ?: 1
        }
        sum
    }

    /**
     * Create-Constructor
     */
    constructor(title: String, description: String, durationLimit: Int?, ownerId: String, taskTemplates: Map<Int, TaskTemplate>) :
            this(null, title, description, durationLimit, UserContainer.getUser(ownerId), Instant.now(), null, 0, 0, false, taskTemplates) {

        checkProperties(title, durationLimit, taskTemplates)
    }

    /**
     * Update-Constructor
     */
    constructor(
        id: Int,
        title: String,
        description: String,
        durationLimit: Int?,
        ownerId: String,
        taskTemplates: Map<Int, TaskTemplate>
    ) : this(
        id,
        title,
        description,
        durationLimit,
        UserContainer.getUser(ownerId),
        ProcessTemplateContainer.getProcessTemplate(id).createdAt,
        ProcessTemplateContainer.getProcessTemplate(id).formerVersionId,
        ProcessTemplateContainer.getProcessTemplate(id).processCount,
        ProcessTemplateContainer.getProcessTemplate(id).runningProcesses,
        ProcessTemplateContainer.getProcessTemplate(id).deleted,
        taskTemplates
    ) {

        if (id < 1)
            throw InvalidInputException("id must be positive")

        checkProperties(title, durationLimit, taskTemplates)
    }

    companion object {
        fun checkProperties(title: String, durationLimit: Int?, taskTemplates: Map<Int, TaskTemplate>) {
            if (title.isEmpty())
                throw InvalidInputException("title must not be empty")
            if (durationLimit != null && durationLimit <= 0)
                throw InvalidInputException("duration limit must be positive")
            if (taskTemplates == null)
                throw NullPointerException()
            if (taskTemplates.isEmpty())
                throw InvalidInputException("must contain at least one task template")
            if (!ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
                throw InvalidInputException("connection between task templates must be acyclic")
        }
    }
}
