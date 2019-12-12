package de.se.team3.logic.domain

import java.lang.IllegalArgumentException

class ProcessTemplate(id: Int?, title: String, durationLimit: Int?, owner: User) {

    val id = id
    val title = title
    val durationLimit = durationLimit
    val owner = owner

    constructor(title: String, durationLimit: Int?, owner: User): this(null, title, durationLimit, owner) {
        if (title.length == 0)
            throw IllegalArgumentException("title must not be empty")
        if (durationLimit != null && durationLimit <= 0)
            throw IllegalArgumentException("duration limit must be positive ")
    }



}