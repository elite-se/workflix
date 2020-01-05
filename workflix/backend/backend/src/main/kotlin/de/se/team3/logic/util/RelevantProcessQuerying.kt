package de.se.team3.logic.util

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.logic.container.ProcessGroupsContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ConnectionManager
import java.time.Instant

object RelevantProcessQuerying {

    /**
     * @return List of all processes which are processes of the given user's process groups and the user's role is
     * assigned, or to which the user is assigned.
     */
    fun get(userID: String?): List<Int> {
        if (userID == null)
            return ArrayList()
        val processesInUsersGroup = ProcessContainer
            .getAllProcesses()
            .filter { ProcessGroupsContainer
                .getProcessGroup(it.processGroupId)
                .getMembersIds()
                .contains(userID) }
        val processesInUsersGroupItsRoleIsAssignedTo = processesInUsersGroup
            .filter { process ->
                process.tasks.values.any { task ->
                    UserContainer
                        .getUser(userID)
                        .getUserRoleIds()
                        .contains(task.taskTemplate?.responsibleUserRoleId)
                }
            }
        val processesUserIsAssignedTo = ProcessContainer
            .getAllProcesses()
            .filter { process -> process.getAssignees().map { it.id }.contains(userID) }
        return (processesInUsersGroupItsRoleIsAssignedTo  + processesUserIsAssignedTo).toSet().map { it.id!! }
    }

    /**
     * @return List of all processes which are processes of the given user's process groups, or to which the user is
     * assigned.
     */
    fun getAllInProcessGroups(userID: String?): List<Int> {
        if (userID == null)
            return ArrayList()
        val processesInUsersGroup = ProcessContainer
            .getAllProcesses()
            .filter { ProcessGroupsContainer
                .getProcessGroup(it.processGroupId)
                .getMembersIds()
                .contains(userID) }
        val processesUserIsAssignedTo = ProcessContainer
            .getAllProcesses()
            .filter { process -> process.getAssignees().map { it.id }.contains(userID) }
        return (processesInUsersGroup + processesUserIsAssignedTo).toSet().map { it.id!! }
    }
}
