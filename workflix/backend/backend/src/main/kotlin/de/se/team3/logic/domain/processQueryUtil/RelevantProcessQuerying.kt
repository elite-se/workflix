package de.se.team3.logic.domain.processQueryUtil

import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.Process

object RelevantProcessQuerying {

    /**
     * @return List of all processes which are processes of the given user's process group and the user's role is
     * assigned, or to which the user is assigned.
     */
    fun queryRelevantProcesses(user: User): ArrayList<Process> {
        TODO()
    }

    /**
     * @return List of all processes which are processes of the given user's process group, or to which the user is
     * assigned.
     */
    fun queryProcessGroupProcesses(user: User): ArrayList<Process> {
        TODO()
    }

}