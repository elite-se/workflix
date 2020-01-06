package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.exceptions.AlreadyClosedException
import de.se.team3.logic.exceptions.AlreadyExistsException
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.exceptions.NotFoundException
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
    private val comments: ArrayList<TaskComment>?,
    private val assignments: ArrayList<TaskAssignment>?,
    @JsonIgnore
    var process: Process?
) {

    fun getComments() = comments

    fun getAssignments() = assignments

    @get:JsonIgnore
    val taskTemplate by lazy {
        process!!.processTemplate.taskTemplates.get(taskTemplateId)
    }

    /**
     * Create-Constructor
     */
    constructor(taskTemplateId: Int, startedAt: Instant?) :
            this(null, taskTemplateId, startedAt, null, null, null) {
    }

    /**
     * Checks whether all predecessors of this task are already closed.
     *
     * @return True if all predecessors are closed.
     */
    private fun arePredecessorsClosed(): Boolean {
        var unclosedFound = false
        taskTemplate!!.predecessors.forEach { taskTemplate ->
            val preTask = process!!.tasks.get(taskTemplate.id)
            if (preTask!!.status() != TaskStatus.CLOSED)
                unclosedFound = true
        }
        return !unclosedFound
    }

    /**
     * Returns the count of closed assignments to this task.
     */
    private fun closedAssignmentsCount(): Int {
        var closings = 0
        assignments!!.forEach { taskAssignment ->
            if (taskAssignment.isClosed())
                closings++
        }
        return closings
    }

    /**
     * Returns the status of the task.
     */
    @JsonProperty("status")
    private fun status(): TaskStatus {
        return if (arePredecessorsClosed()) {
            if (closedAssignmentsCount() == taskTemplate!!.necessaryClosings)
                TaskStatus.CLOSED
            else
                TaskStatus.RUNNING
        } else {
            TaskStatus.BLOCKED
        }
    }

    /**
     * Checks whether the task is blocked or not.
     *
     * @return True if the task is blocked, i.d. not all predecessors are closed.
     */
    @JsonIgnore
    fun isBlocked(): Boolean {
        return status() == TaskStatus.BLOCKED
    }

    /**
     * Checks whether the task is closed or not.
     *
     * @return True if the task is closed.
     */
    @JsonIgnore
    fun isClosed(): Boolean {
        return status() == TaskStatus.CLOSED
    }

    /**
     * Checks whether the task has an assignment to the specified assignee or not.
     *
     * @return True if there is a assignment to the specified assignee.
     */
    fun hasAssignment(assigneeId: String): Boolean {
        return assignments!!.find { it.assigneeId == assigneeId } != null
    }

    /**
     * Adds the given task assignment.
     *
     * @throws InvalidInputException Is thrown if the task id specified in the given task assignment
     * is not equal to the id of this task.
     * @throws AlreadyExistsException Is thrown if the task assignment already exists.
     */
    fun addTaskAssignment(taskAssignment: TaskAssignment) {
        if (taskAssignment.taskId != id)
            throw InvalidInputException("task id specified in the given task assignment must be equal to the id of this task")
        if (hasAssignment(taskAssignment.assigneeId))
            throw AlreadyExistsException("task assignment already exists")

        assignments!!.add(taskAssignment)
    }

    /**
     * Returns the specified task assignment.
     *
     * @throws NotFoundException Is thrown if the specified task assignment does not exist.
     */
    fun getTaskAssignment(assigneeId: String): TaskAssignment {
        for (i in 0 until assignments!!.size) {
            val assignment = assignments.get(i)
            if (assignment.assigneeId == assigneeId)
                return assignment
        }
        throw NotFoundException("task assignment not found")
    }

    /**
     * Deletes the specified task assignment.
     *
     * @throws AlreadyClosedException Is thrown if the specified assignment is already closed.
     * @throws NotFoundException Is thrown if a task assignment with the specified user does not
     * exist for this task.
     */
    fun deleteTaskAssignment(assigneeId: String) {
        for (i in 0 until assignments!!.size) {
            val assignment = assignments.get(i)
            if (assignment.assigneeId == assigneeId) {
                if (assignment.isClosed())
                    throw AlreadyClosedException("a closed assignment could not be deleted")

                assignments.removeAt(i)
                return
            }
        }
        throw NotFoundException("task assignment does not exist")
    }

    /**
     * Add task comment.
     *
     * @throws InvalidInputException Is thrown if the task id specified in the given task comment
     * is not equal to the id of this task.
     */
    fun addTaskComment(taskComment: TaskComment) {
        if (taskComment.taskId != id)
            throw InvalidInputException("task id specified in the given task comment must be equal to the id of this task")

        comments!!.add(taskComment)
    }

    /**
     * Returns the specified task comment.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    fun getTaskComment(taskCommentId: Int): TaskComment {
        return comments!!.find { it.id == taskCommentId }
            ?: throw NotFoundException("task comment does not exist")
    }

    /**
     * Deletes the specified task comment.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    fun deleteTaskComment(taskCommentId: Int) {
        val existed = comments!!.removeIf { it.id == taskCommentId }
        if (!existed)
            throw NotFoundException("task comment does not exist")
    }
}
