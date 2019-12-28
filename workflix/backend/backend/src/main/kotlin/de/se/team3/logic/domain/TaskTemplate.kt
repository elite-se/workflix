package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.lang.IllegalArgumentException

/**
 *
 * The attribute id is not null even if the task template is constructed while creation. This is because
 * in this situation there are dummy ids needed for the interconnection with other task templates.
 */
class TaskTemplate(
    val id: Int,
    val name: String,
    val description: String,
    val estimatedDuration: Int?,
    val durationLimit: Int?,
    val necessaryClosings: Int
) {

    @JsonIgnore
    val successors = HashSet<TaskTemplate>()
    @JsonIgnore
    val predecessors = HashSet<TaskTemplate>()

    init {
        if (estimatedDuration != null && durationLimit != null)
            if (estimatedDuration > durationLimit)
                throw IllegalArgumentException("a estimated duration greater than the duration limit makes no sense")
        if (necessaryClosings < 1)
            throw IllegalArgumentException("there must be at least one user who closes a task explicitly")
    }
}
