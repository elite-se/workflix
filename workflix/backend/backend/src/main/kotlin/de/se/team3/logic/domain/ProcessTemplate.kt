package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.ProcessTemplatesContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.processTemplateUtil.ProcessTemplateCycleDetection
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant

/**
 * Represents a process template.
 */
data class ProcessTemplate(
    val id: Int?,
    val title: String,
    val description: String,
    val durationLimit: Int,
    val ownerId: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    val formerVersionId: Int?,
    private var processCount: Int,
    private var runningProcesses: Int,
    private var deleted: Boolean,
    @JsonIgnore
    val taskTemplates: Map<Int, TaskTemplate>
) {

    fun getProcessCount() = processCount

    fun isDeleted() = deleted

    @get:JsonIgnore
    val owner by lazy { UserContainer.getUser(ownerId) }

    @get:JsonIgnore
    val taskTemplatesList by lazy { taskTemplates!!.map { it.value } }

    /**
     * Create-Constructor
     */
    constructor(title: String, description: String, durationLimit: Int, ownerId: String, taskTemplates: Map<Int, TaskTemplate>) :
            this(null, title, description, durationLimit, ownerId, Instant.now(), null, 0, 0, false, taskTemplates) {

        checkProperties(title, durationLimit, taskTemplates)
    }

    /**
     * Update-Constructor
     */
    constructor(
        id: Int,
        title: String,
        description: String,
        durationLimit: Int,
        ownerId: String,
        taskTemplates: Map<Int, TaskTemplate>
    ) : this(
        id,
        title,
        description,
        durationLimit,
        ownerId,
        ProcessTemplatesContainer.getProcessTemplate(id).createdAt,
        ProcessTemplatesContainer.getProcessTemplate(id).formerVersionId,
        ProcessTemplatesContainer.getProcessTemplate(id).processCount,
        ProcessTemplatesContainer.getProcessTemplate(id).runningProcesses,
        ProcessTemplatesContainer.getProcessTemplate(id).deleted,
        taskTemplates
    ) {

        if (id < 1)
            throw InvalidInputException("id must be positive")

        checkProperties(title, durationLimit, taskTemplates)
    }

    /**
     * Returns the sum of all the estimated durations of all task templates that
     * belongs to this process template.
     */
    @JsonIgnore
    fun getEstimatedDurationSum(): Int {
        var sum = 0
        taskTemplates.forEach { (_, taskTemplate) ->
            sum += taskTemplate.estimatedDuration ?: 1
        }
        return sum
    }

    /**
     * Increases the overall amount and the amount of running processes.
     *
     * Not that a newly generated process is always running at the beginning.
     */
    fun increaseProcessCounters() {
        processCount++
        runningProcesses++
    }

    /**
     * Decreases the amount of running processes.
     *
     * The overall amount of processes could never shrink.
     */
    fun decreaseRunningProcesses() {
        runningProcesses--
    }

    /**
     * Sets the deleted flag.
     */
    fun delete() {
        deleted = true
    }

    companion object {

        /**
         * Checks property constraints.
         *
         * @throws InvalidInputException Is thrown if title is empty, duration limit is not positive,
         * the list of task templates is or contains a cycle.
         */
        fun checkProperties(title: String, durationLimit: Int?, taskTemplates: Map<Int, TaskTemplate>) {
            if (title.isEmpty())
                throw InvalidInputException("title must not be empty")
            if (durationLimit != null && durationLimit <= 0)
                throw InvalidInputException("duration limit must be positive")
            if (taskTemplates.isEmpty())
                throw InvalidInputException("must contain at least one task template")
            if (!ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
                throw InvalidInputException("connection between task templates must be acyclic")
        }

    }

}
