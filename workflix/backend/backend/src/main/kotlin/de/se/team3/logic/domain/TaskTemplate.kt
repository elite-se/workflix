package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 *
 * The attribute id is not null even if the task template is constructed while creation. This is because
 * in this situation there are dummy ids needed for the interconnection with other task templates.
 */
class TaskTemplate(val id: Int, val name: String, val estimatedDuration: Int?, val durationLimit: Int?) {

    @JsonIgnore
    val successors = HashSet<TaskTemplate>()
    @JsonIgnore
    val predecessors = HashSet<TaskTemplate>()

}