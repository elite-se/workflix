package de.se.team3.logic.domain.processQueryUtil

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.logic.container.ProcessGroupsContainer
import de.se.team3.logic.container.TaskAssignmentsContainer
import de.se.team3.logic.container.TasksContainer
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.Process

object RelevantProcessQuerying {

    /**
     * @return List of all processes which are processes of the given user's process group and the user's role is
     * assigned, or to which the user is assigned.
     */
    fun queryRelevantProcesses(user: User): List<Process> {
        val processesInUsersGroup = ProcessContainer.getAllProcesses()
            .filter { ProcessGroupsContainer
                .getProcessGroup(it.processGroupId)
                .getMembersIds()
                .contains(user.id)}
        val processesInUsersGroupItsRoleIsAssignedTo = processesInUsersGroup
            .filter { process ->
                process.tasks?.values?.filter { task ->
                user
                    .getUserRoleMemberships()
                    .map { it.id }
                    .contains(task.taskTemplate?.responsibleUserRoleId) }?.size ?: 0 != 0 }
        val processesUserIsAssignedTo = ProcessContainer.getAllProcesses().filter { it.getAssignees().contains(user) }
        return processesInUsersGroupItsRoleIsAssignedTo + processesUserIsAssignedTo
    }

    /**
     * @return List of all processes which are processes of the given user's process group, or to which the user is
     * assigned.
     */
    fun queryProcessGroupProcesses(user: User): List<Process> {
        val processesInUsersGroup = ProcessContainer.getAllProcesses()
            .filter { ProcessGroupsContainer
                .getProcessGroup(it.processGroupId)
                .getMembersIds()
                .contains(user.id)}
        val processesUserIsAssignedTo = ProcessContainer.getAllProcesses().filter { it.getAssignees().contains(user) }
        return processesInUsersGroup + processesUserIsAssignedTo
    }

}