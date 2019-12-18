package de.se.team3.logic.domain

import java.time.Instant

class TaskResponsibility(val responsibleUser: User, val done: Boolean?, val doneAt: Instant?) {

    /**
     * Create Constructor
     */
    constructor(responsibleUser: User): this(responsibleUser, null, null)

}