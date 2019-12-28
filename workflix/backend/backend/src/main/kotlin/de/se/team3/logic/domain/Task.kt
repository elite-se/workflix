package de.se.team3.logic.domain

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
    val assignments: List<TaskAssignment>?
) {

    val taskTemplate by lazy {
        TaskTemplateContainer.getTaskTemplate(taskTemplateId)
    }

    val isDone by lazy {
        // should only be null in case of creation
        // then of course the task could not be already closed
        if (assignments == null) false else {
            var closings = 0
            assignments.forEach { taskAssignment ->
                if (taskAssignment.status == AssignmentStatus.CLOSED)
                    closings++
            }
            closings == taskTemplate.necessaryClosings
        }
    }

    /**
     * Create-Constructor
     */
    constructor(taskTemplateId: Int, startedAt: Instant?) :
            this(null, taskTemplateId, startedAt, null, null) {
    }
}
