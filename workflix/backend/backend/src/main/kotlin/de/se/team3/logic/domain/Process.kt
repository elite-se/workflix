package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.webservice.util.InstantSerializer
import java.lang.IllegalArgumentException
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
    val status: ProcessStatus,
    @JsonSerialize(using = InstantSerializer::class)
    val deadline: Instant?,
    @JsonSerialize(using = InstantSerializer::class)
    val startedAt: Instant,
    @JsonIgnore
    // the tasks lies under the id of the corresponding task template
    val tasks: Map<Int, Task>?
) {

    @get:JsonIgnore
    val starter by lazy { UserContainer.getUser(starterId) }
    @get:JsonIgnore
    val processTemplate by lazy { ProcessTemplateContainer.getProcessTemplate(processTemplateId) }

    /**
     * Returns the current progress in percent.
     *
     * The progress is the percentage of tasks done weighted by the estimated duration. The estimated
     * duration of a task where the task template does not contain a estimated duration is assumed
     * to be 1.
     */
    val progress by lazy {
        var estimatedDurationDone = 0
        tasks?.forEach { id, task ->
            if (task.isDone)
                estimatedDurationDone += task.taskTemplate.estimatedDuration ?: 1
        }
        (estimatedDurationDone / processTemplate.estimatedDurationSum * 100).toInt()
    }

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
            throw IllegalArgumentException("title must not be empty")
        if (processTemplate.deleted)
            throw IllegalArgumentException("must not be based on a deleted process template")
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

        // TODO: Was macht das hier?
        // group.processes.add(this)
    }
}
