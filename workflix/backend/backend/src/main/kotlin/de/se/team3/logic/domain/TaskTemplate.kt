package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import de.se.team3.logic.exceptions.InvalidInputException

/**
 * Represents a task template.
 *
 * The attribute id is not null even in the case of creation. This is because in this case
 * there are dummy ids needed for the interconnection with other task templates.
 */
class TaskTemplate(
    val id: Int,
    @JsonIgnore
    val responsibleUserRole: UserRole,
    val name: String,
    val description: String,
    val estimatedDuration: Double,
    val necessaryClosings: Int
) {

    val responsibleUserRoleId = responsibleUserRole.id!!

    @JsonIgnore
    private val successors = HashSet<TaskTemplate>()
    @JsonIgnore
    private val predecessors = HashSet<TaskTemplate>()

    @JsonIgnore
    fun getSuccessors() = successors

    @JsonIgnore
    fun getPredecessors() = predecessors

    /**
     * Checks property constraints.
     *
     * @throws InvalidInputException Is thrown if name is empty of if necessaryClosings or estimatedDuration
     * ist not positive.
     */
    init {
        if (name.isEmpty())
            throw InvalidInputException("name must not be empty")
        if (necessaryClosings < 1)
            throw InvalidInputException("there must be at least one user who closes a task explicitly")
        if (estimatedDuration < 1)
            throw InvalidInputException("estimated duration must be positive")
    }

    /**
     * Adds the given task as predecessor.
     *
     * Note that this method also registers this as successor by each predecessor.
     *
     * @throws InvalidInputException Is thrown if the given task template is already a predecessor of this.
     */
    fun addPredecessor(taskTemplate: TaskTemplate) {
        if (predecessors.find { it.id == taskTemplate.id } != null)
            throw InvalidInputException("given task template is already a predecessor of this")

        predecessors.add(taskTemplate)
        taskTemplate.successors.add(this)
    }
}
