package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant

/**
 * Represents a process.
 */
class Process(
    val id: Int?,
    val starterId: String,
    val processGroupId: Int,
    val processTemplateId: Int,
    val title: String,
    val description: String,
    private var status: ProcessStatus,
    @JsonSerialize(using = InstantSerializer::class)
    val deadline: Instant?,
    @JsonSerialize(using = InstantSerializer::class)
    val startedAt: Instant,
    @JsonIgnore
    // the tasks lies under the id of the corresponding task template
    val tasks: Map<Int, Task>?
) {

    fun getStatus() = status

    init {
        if (tasks != null)
            tasks.forEach { i, task -> task.process = this }
    }

    @get:JsonIgnore
    val starter by lazy { UserContainer.getUser(starterId) }
    @get:JsonIgnore
    val processTemplate by lazy { ProcessTemplateContainer.getProcessTemplate(processTemplateId) }

    /**
     * Create-Constructor
     */
    constructor(
        starterId: String,
        processGroupId: Int,
        processTemplateId: Int,
        title: String,
        description: String,
        deadline: Instant?
    ) : this (null,
        starterId,
        processGroupId,
        processTemplateId,
        title,
        description,
        ProcessStatus.RUNNING,
        deadline,
        Instant.now(), // started at
        createTasks(processTemplateId)) {

        if (title.isEmpty())
            throw InvalidInputException("title must not be empty")
        if (processTemplate.deleted)
            throw InvalidInputException("must not be based on a deleted process template")
    }

    /**
     * Returns the specified task.
     */
    fun findTask(taskId: Int): Task {
        return tasks!!.map { it.value }.find { it.id == taskId }!!
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
        var estimatedDurationDone = 0
        tasks?.forEach { id, task ->
            if (task.isClosed())
                estimatedDurationDone += task.taskTemplate!!.estimatedDuration ?: 1
        }

        val ratio = estimatedDurationDone / processTemplate.estimatedDurationSum
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
        tasks!!.forEach { i, task ->
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
        if (status != ProcessStatus.RUNNING)
            throw IllegalStateException("only a running processes could be closed")

        status = ProcessStatus.CLOSED
        processTemplate.decreaseRunningProcesses()
    }

    /**
     * Aborts the process.
     *
     * @throws IllegalStateException Is thrown if the status of the process is not running.
     */
    fun abort() {
        if (status != ProcessStatus.RUNNING)
            throw IllegalStateException("only a running processes could be aborted")

        status = ProcessStatus.ABORTED
        processTemplate.decreaseRunningProcesses()
    }

    companion object {

        /**
         * Creates the tasks of the process by looping over the underlying task templates.
         */
        fun createTasks(processTemplateId: Int): Map<Int, Task> {
            val tasks = HashMap<Int, Task>()
            val processTemplate = ProcessTemplateContainer.getProcessTemplate(processTemplateId)
            val taskTemplates = processTemplate.taskTemplates!!
            for ((id, taskTemplate) in taskTemplates) {
                val startedAt = if (taskTemplate.predecessors.size == 0) Instant.now() else null
                val task = Task(id, startedAt)
                tasks.put(id, task)
            }
            return tasks
        }
    }
}
