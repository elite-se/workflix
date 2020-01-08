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
    @JsonIgnore
    val owner: User,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    val formerVersionId: Int?,
    private var processCount: Int,
    private var runningProcesses: Int,
    private var deleted: Boolean,
    @JsonIgnore
    val taskTemplates: Map<Int, TaskTemplate>
) {

    val ownerId = owner.id

    fun getProcessCount() = processCount

    fun getRunningProcesses() = runningProcesses

    fun isDeleted() = deleted

    @get:JsonIgnore
    val taskTemplatesList by lazy { taskTemplates.map { it.value } }

    /**
     * Create-Constructor
     */
    constructor(
        title: String,
        description: String,
        durationLimit: Int,
        owner: User,
        taskTemplates: Map<Int, TaskTemplate>
    ) : this(
        null,
        title,
        description,
        durationLimit,
        owner,
        Instant.now(),
        null,
        0,
        0,
        false,
        taskTemplates
    ) {
        checkProperties()
    }

    /**
     * Update-Constructor
     */
    constructor(
        id: Int,
        title: String,
        description: String,
        durationLimit: Int,
        owner: User,
        taskTemplates: Map<Int, TaskTemplate>
    ) : this(
        id,
        title,
        description,
        durationLimit,
        owner,
        Instant.now(),
        null,
        0,
        0,
        false,
        taskTemplates
    ) {
        if (id < 1)
            throw InvalidInputException("id must be positive")

        checkProperties()
    }

    /**
     * Checks property constraints.
     *
     * @throws InvalidInputException Is thrown if title is empty, duration limit is not positive,
     * the list of task templates is empty or contains a cycle.
     */
    private fun checkProperties() {
        if (title.isEmpty())
            throw InvalidInputException("title must not be empty")
        if (durationLimit <= 0)
            throw InvalidInputException("duration limit must be positive")
        if (taskTemplates.isEmpty())
            throw InvalidInputException("must contain at least one task template")
        if (!ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
            throw InvalidInputException("connection between task templates must be acyclic")
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

    /**
     * Checks whether the given list of user role ids contains one that is designated as responsible
     * by at least one of the task templates.
     *
     * @return True iff at least one task template designates one of the specified user roles
     * as responsible.
     */
    @JsonIgnore
    fun isOneResponsible(userRoleIds: List<Int>): Boolean {
        val responsibleUserRoleIds = taskTemplates.values.map { it.responsibleUserRoleId }
        return responsibleUserRoleIds.intersect(userRoleIds).isNotEmpty()
    }

    /**
     * Checks whether the specified user role is designated as responsible in a
     * task template of this process template.
     *
     * @throws NullPointerException Is thrown if the id of the given user role is null.
     *
     * @return True if and only if the given user role is designated as responsible in
     * a task template of this process template.
     */
    fun usesUserRole(userRole: UserRole): Boolean {
        if (userRole.id == null)
            throw NullPointerException("id of user role must not be null")

        for ((key, taskTemplate) in taskTemplates)
            if (taskTemplate.responsibleUserRoleId == userRole.id)
                return true

        return false
    }
}
