package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.lang.IllegalArgumentException

class ProcessTemplate(id: Int?, title: String, durationLimit: Int?, owner: User, taskTemplates: Set<TaskTemplate>?) {

    val id = id
    val title = title
    val durationLimit = durationLimit
    val owner = owner
    @JsonIgnore
    val taskTemplates = taskTemplates

    constructor(title: String, durationLimit: Int?, owner: User, taskTemplates: Set<TaskTemplate>?): this(null, title, durationLimit, owner, taskTemplates) {
        if (title.length == 0)
            throw IllegalArgumentException("title must not be empty")
        if (durationLimit != null && durationLimit <= 0)
            throw IllegalArgumentException("duration limit must be positive ")
        if (taskTemplates == null)
            throw NullPointerException()
        if (taskTemplates.size == 0)
            throw IllegalArgumentException("must contain at least one task template")
    }

    constructor(id: Int?, title: String, durationLimit: Int?, owner: User): this(id, title, durationLimit, owner, null)

}