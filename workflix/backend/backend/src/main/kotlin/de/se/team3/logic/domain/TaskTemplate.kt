package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore

class TaskTemplate(id: Int?, name: String, estimatedDuration: Int, durationLimit: Int, successors: MutableSet<TaskTemplate>?, predeccessors: MutableSet<TaskTemplate>?) {

    val id = id
    val name = name
    val estimatedDuration = estimatedDuration
    val durationLimit = durationLimit
    @JsonIgnore
    var successors = successors
    @JsonIgnore
    var predeccessors = predeccessors

    constructor(name: String, estimatedDuration: Int, durationLimit: Int, successors: MutableSet<TaskTemplate>, predeccessors: MutableSet<TaskTemplate>)
            : this(null, name, estimatedDuration, durationLimit, successors, predeccessors)

    constructor(id: Int?, name: String, estimatedDuration: Int, durationLimit: Int)
            : this(id, name, estimatedDuration, durationLimit, null, null)

}