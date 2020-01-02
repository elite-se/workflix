package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
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
    @JsonIgnore
    var process: Process?
) {

    @get:JsonIgnore
    val taskTemplate by lazy {
        process!!.processTemplate.taskTemplates!!.get(taskTemplateId)
    }

    @get:JsonIgnore
    val previousClosed: Boolean by lazy {
        var unclosedFound = false
        taskTemplate!!.predecessors.forEach { taskTemplate ->
            val preTask = process!!.tasks!!.get(taskTemplate.id)
            if (preTask!!.status != TaskStatus.CLOSED)
                unclosedFound = true
        }
        !unclosedFound
    }

    @get:JsonIgnore
    val numberOfClosedAssignments by lazy {
        var closings = 0
        assignments!!.forEach { taskAssignment ->
            if (taskAssignment.closed)
                closings++
        }
        closings
    }

    val status by lazy {
        if (previousClosed) {
            if (numberOfClosedAssignments == taskTemplate!!.necessaryClosings)
                TaskStatus.CLOSED
            else
                TaskStatus.RUNNING
        } else {
            TaskStatus.BLOCKED
        }
    }

    /**
     * Create-Constructor
     */
    constructor(taskTemplateId: Int, startedAt: Instant?) :
            this(null, taskTemplateId, startedAt, null, null, null) {
    }

}
