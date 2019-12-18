package de.se.team3.logic.domain

import de.se.team3.logic.container.UserContainer
import java.time.Instant

/**
 * Represents a task
 */
class Task(val id: Int?, val taskTemplate: TaskTemplate, val simpleClosing: Boolean, val startedAt: Instant?, personsResposible: Set<User>) {

    companion object {
        private fun getPersonsResponsible(personsResposibleIds: Set<String>): Set<User> {
            val personsResposible = HashSet<User>()
            for (personId in personsResposibleIds)
                personsResposible.add(UserContainer.getUser(personId))
            return personsResposible
        }
    }

    /**
     * Create-Constructor
     */
    constructor(taskTemplate: TaskTemplate, simpleClosing: Boolean, startedAt: Instant?, personsResposibleIds: Set<String>) :
            this(null, taskTemplate, simpleClosing, startedAt, getPersonsResponsible(personsResposibleIds)) {
    }
}
