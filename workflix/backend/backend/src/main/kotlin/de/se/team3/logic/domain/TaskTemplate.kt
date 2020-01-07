package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import de.se.team3.logic.container.UserRoleContainer
import de.se.team3.logic.exceptions.InvalidInputException

/**
 *
 * The attribute id is not null even if the task template is constructed while creation. This is because
 * in this situation there are dummy ids needed for the interconnection with other task templates.
 */
class TaskTemplate(
    val id: Int,
    val responsibleUserRoleId: Int,
    val name: String,
    val description: String,
    val estimatedDuration: Int,
    val necessaryClosings: Int
) {

    @JsonIgnore
    private val successors = ArrayList<TaskTemplate>()
    @JsonIgnore
    private val predecessors = ArrayList<TaskTemplate>()

    @JsonIgnore
    fun getSuccessors() = successors.toList()

    @JsonIgnore
    fun getPredecessors() = predecessors.toList()

    init {
        if (necessaryClosings < 1)
            throw InvalidInputException("there must be at least one user who closes a task explicitly")
        if (name.isEmpty())
            throw InvalidInputException("name must not be empty")
        if (estimatedDuration < 1)
            throw InvalidInputException("estimated duration must be positive")
        if (!UserRoleContainer.hasUserRole(responsibleUserRoleId))
            throw InvalidInputException("user role specified as responsible does not exist")
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
