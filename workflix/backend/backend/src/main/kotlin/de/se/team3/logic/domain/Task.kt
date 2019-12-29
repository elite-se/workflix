package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.TaskTemplateContainer
import de.se.team3.webservice.util.InstantSerializer
import java.time.Instant

/**
 * Represents a task
 */
class Task(
    val id: Int?,
    val taskTemplateId: Int,
    @JsonSerialize(using = InstantSerializer::class)
    val startedAt: Instant?,
    val comments: List<TaskComment>?,
    val assignments: List<TaskAssignment>?,
    val process: Process?
) {

    val taskTemplate by lazy {
        TaskTemplateContainer.getTaskTemplate(taskTemplateId)
    }

    val status by lazy {

    }

    val isDone by lazy {
        // should only be null in case of creation
        // then of course the task could not be already closed
        if (assignments == null) false else {
            var closings = 0
            assignments.forEach { taskAssignment ->
                if (taskAssignment.closed)
                    closings++
            }
            closings == taskTemplate.necessaryClosings
        }
    }

    /**
     * Create-Constructor
     */
    constructor(taskTemplateId: Int, startedAt: Instant?) :
            this(null, taskTemplateId, startedAt, null, null, null) {
    }

    /**
     * Link-Copy-Constructor
     *
     * Is used to link the task with its process while reading from db.
     */
    constructor(task: Task, process: Process): this(task.id, task.taskTemplateId, task.startedAt, task.comments, task.assignments, process)
}
