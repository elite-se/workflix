package de.se.team3.logic.domain.processQueryUtil

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.logic.container.ProcessGroupContainer
import de.se.team3.logic.container.TaskAssignmentsContainer
import de.se.team3.logic.container.TasksContainer
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.Process

object RelevantProcessQuerying {

    /**
     * @return List of all processes which are processes of the given user's process group and the user's role is
     * assigned, or to which the user is assigned.
     */
    fun queryRelevantProcesses(user: User): ArrayList<Process> {
        val processesInUsersGroup = ProcessContainer.getAllProcesses()
            .filter { ProcessGroupContainer
                .getProcessGroup(it.processGroupId)
                .members.contains(user)}
        //TODO processesUsersGroupIsAssignedTo
        val processesUserIsAssignedTo = ProcessContainer.getAllProcesses().filter { it.getAssignees().contains(user) }
        TODO()
    }

    /**
     * @return List of all processes which are processes of the given user's process group, or to which the user is
     * assigned.
     */
    fun queryProcessGroupProcesses(user: User): List<Process> {
        val processesInUsersGroup = ProcessContainer.getAllProcesses()
            .filter { ProcessGroupContainer
                .getProcessGroup(it.processGroupId)
                .members.contains(user)}
        val processesUserIsAssignedTo = ProcessContainer.getAllProcesses().filter { it.getAssignees().contains(user) }
        return processesInUsersGroup + processesUserIsAssignedTo
    }

}