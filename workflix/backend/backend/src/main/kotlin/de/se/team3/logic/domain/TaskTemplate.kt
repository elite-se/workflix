package de.se.team3.logic.domain

class TaskTemplate(id: Int?, name: String, estimatedDuration: Int, durationLimit: Int, successors: Set<TaskTemplate>, predeccessors: Set<TaskTemplate>) {

    val id = id
    val name = name
    val estimatedDuration = estimatedDuration
    val durationLimit = durationLimit
    val successors = successors
    val predeccessors = predeccessors

    constructor(name: String, estimatedDuration: Int, durationLimit: Int, successors: Set<TaskTemplate>, predeccessors: Set<TaskTemplate>)
            : this(null, name, estimatedDuration, durationLimit, successors, predeccessors) {

    }

}