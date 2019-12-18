package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 *
 * The attribute id is not null even if the task template is constructed while creation. This is because
 * in this situation there are dummy ids needed for the interconnection with other task templates.
 */
class TaskTemplate(id: Int, name: String, estimatedDuration: Int?, durationLimit: Int?) {

    val id = id
    val name = name
    val estimatedDuration = estimatedDuration
    val durationLimit = durationLimit
    @JsonIgnore
    val successors = HashSet<TaskTemplate>()
    @JsonIgnore
    val predecessors = HashSet<TaskTemplate>()
}
