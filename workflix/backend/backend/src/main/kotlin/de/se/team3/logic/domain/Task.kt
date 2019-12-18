package de.se.team3.logic.domain

import de.se.team3.logic.container.UserContainer
import java.time.Instant

/**
 * Represents a task
 */
class Task(
    val id: Int?,
    val taskTemplate: TaskTemplate,
    val simpleClosing: Boolean,
    val startedAt: Instant?,
    val usersResposible: Set<TaskResponsibility>
) {

    companion object {
        private fun getPersonsResponsible(personsResposibleIds: Set<String>): Set<TaskResponsibility> {
            val personsResposible = HashSet<TaskResponsibility>()
            for (personId in personsResposibleIds)
                personsResposible.add(TaskResponsibility(UserContainer.getUser(personId)))
            return personsResposible
        }
    }

    /**
     * Create-Constructor
     */
    constructor(taskTemplate: TaskTemplate, simpleClosing: Boolean, startedAt: Instant?, usersResposibleIds: Set<String>) :
            this(null, taskTemplate, simpleClosing, startedAt, getPersonsResponsible(usersResposibleIds)) {
    }
}
