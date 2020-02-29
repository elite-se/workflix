package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant

/**
 * Represents a process.
 */
class Process(
    val id: Int?,
    @JsonIgnore
    val starter: User,
    @JsonIgnore
    val processGroup: ProcessGroup,
    @JsonIgnore
    val processTemplate: ProcessTemplate,
    private var title: String,
    private var description: String,
    private var status: ProcessStatus,
    @JsonSerialize(using = InstantSerializer::class)
    private var deadline: Instant,
    @JsonSerialize(using = InstantSerializer::class)
    val startedAt: Instant,
    @JsonIgnore
    // the tasks lies under the id of the corresponding task template
    val tasks: Map<Int, Task>
) {

    fun getProcessGroupId() = processGroup.id!!

    fun getTitle() = title

    fun getDescription() = description

    fun getStatus() = status

    fun getDeadline() = deadline

    init {
        tasks.forEach { (_, task) -> task.process = this }
    }

    val starterId = starter.id

    val processTemplateId = processTemplate.id!!

    /**
     * Create-Constructor
     */
    constructor(
        starter: User,
        processGroup: ProcessGroup,
        processTemplate: ProcessTemplate,
        title: String,
        description: String,
        deadline: Instant
    ) : this (null,
        starter,
        processGroup,
        processTemplate,
        title,
        description,
        ProcessStatus.RUNNING,
        deadline,
        Instant.now(), // started at
        createTasks(processTemplate)
    ) {
        checkProperties()
    }

    /**
     * Update-Constructor
     */
    constructor(
        id: Int,
        starter: User,
        processGroup: ProcessGroup,
        processTemplate: ProcessTemplate,
        title: String,
        description: String,
        deadline: Instant
    ) : this (
        id,
        starter,
        processGroup,
        processTemplate,
        title,
        description,
        ProcessStatus.RUNNING,
        deadline,
        Instant.now(),
        HashMap<Int, Task>()
    )

    /**
     * Checks property constraints.
     *
     * @throws InvalidInputException Is thrown if the title is empty or if the underlying
     * process template is already deleted.
     */
    fun checkProperties() {
        if (title.isEmpty())
            throw InvalidInputException("title must not be empty")
        if (processTemplate.isDeleted())
            throw InvalidInputException("must not be based on a deleted process template")
    }

    /**
     * Sets the title.
     *
     * @throws InvalidInputException Is thrown if the title is empty.
     */
    fun setTitle(title: String) {
        if (title.isEmpty())
            throw InvalidInputException("title must not be empty")

        this.title = title
    }

    /**
     * Sets the description.
     */
    fun setDescription(description: String) {
        this.description = description
    }

    /**
     * Sets the deadline.
     */
    fun setDeadline(deadline: Instant) {
        this.deadline = deadline
    }

    /**
     * Returns the specified task.
     */
    fun getTask(taskId: Int): Task {
        return tasks.map { it.value }.find { it.id == taskId }!!
    }

    /**
     * Checks whether the process is running or not.
     *
     * @return True iff the process is running.
     */
    fun isRunning(): Boolean {
        return status == ProcessStatus.RUNNING
    }

    /**
     * Checks whether the specified user is assigned to an task of this process.
     *
     * @return True iff the specified user is assigned to at least on task of this process.
     */
    private fun hasAssignee(userId: String): Boolean {
        for ((_, task) in tasks)
            for (assignment in task.getAssignments())
                if (assignment.assigneeId == userId)
                    return true

        return false
    }

    /**
     * Returns the current progress in percent.
     *
     * The progress is the percentage of tasks done weighted by the estimated duration. The estimated
     * duration of a task where the task template does not contain a estimated duration is assumed
     * to be 1.
     *
     * @return The progress of the process in percent from 0 to 100.
     */
    @JsonProperty("progress")
    fun getProgress(): Int {
        var estimatedDurationDone = 0.0
        tasks.forEach { _, task ->
            if (task.isClosed())
                estimatedDurationDone += task.taskTemplate!!.estimatedDuration
        }

        val ratio = estimatedDurationDone / processTemplate.getEstimatedDurationSum()
        return (ratio * 100).toInt()
    }

    /**
     * Decides whether the process could be closed or not.
     *
     * @return True if the process could be closed.
     */
    fun closeable(): Boolean {
        if (status != ProcessStatus.RUNNING)
            return false

        var closeable = true
        tasks.forEach { (_, task) ->
            if (!task.isClosed())
                closeable = false
        }
        return closeable
    }

    /**
     * Closes the process.
     *
     * @throws IllegalStateException Is thrown if the status of the process is not running.
     */
    fun close() {
        if (!closeable())
            throw IllegalStateException("process is not closeable")

        status = ProcessStatus.CLOSED
        processTemplate.decreaseRunningProcesses()
    }

    /**
     * Aborts the process.
     *
     * @throws IllegalStateException Is thrown if the status of the process is not running.
     * @throws IllegalStateException Is thrown if the process is closeable but the close()
     * method was not called yet.
     */
    fun abort() {
        if (status != ProcessStatus.RUNNING)
            throw IllegalStateException("only a running processes could be aborted")
        if (closeable())
            throw IllegalStateException("a closeable process could not be aborted")

        status = ProcessStatus.ABORTED
        processTemplate.decreaseRunningProcesses()
    }

    /**
     * Checks whether the given process is relevant for the user.
     *
     * @return True iff the process is in a process group the specified user is a member of and
     * one of the user's roles is designated as responsible for the process, or the user is
     * assigned to one of the processes tasks.
     */
    @JsonIgnore
    fun isRelevantFor(user: User): Boolean {
        // all relevant information
        val usersGroupIDs = user.getProcessGroupIds()
        val usersRoleIDs = user.getUserRoleIds()

        // user is in process's group and one of his/her roles is assigned to it
        val inUsersGroups = usersGroupIDs.contains(this.getProcessGroupId())
        val designatedAsResponsible = processTemplate.isOneResponsible(usersRoleIDs)
        if (inUsersGroups && designatedAsResponsible)
            return true

        // user is assigned to one of the processes tasks
        if (hasAssignee(user.id))
            return true

        return false
    }

    companion object {

        /**
         * Creates the tasks of the process by looping over the underlying task templates.
         */
        fun createTasks(processTemplate: ProcessTemplate): Map<Int, Task> {
            val tasks = HashMap<Int, Task>()
            val taskTemplates = processTemplate.taskTemplates
            for ((id, taskTemplate) in taskTemplates) {
                val startedAt = if (taskTemplate.getPredecessors().size == 0) Instant.now() else null
                val task = Task(id, startedAt)
                tasks.put(id, task)
            }
            return tasks
        }
    }
}
