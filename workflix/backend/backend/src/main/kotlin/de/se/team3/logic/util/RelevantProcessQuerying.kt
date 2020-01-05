package de.se.team3.logic.util

import de.se.team3.logic.container.ProcessContainer
import de.se.team3.logic.container.ProcessGroupsContainer
import de.se.team3.logic.container.TaskTemplateContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.Process
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

    /**
     * @return true iff the process is in a process group of the user associated with the given userID and
     * one of the user's role is is assigned to the process, or the user is assigned to one of the processes tasks.
     */
    fun isRelevantFor(userID: String?, process: Process): Boolean {
        //no userID given is handled as true so that the AND-clause in ProcessQueryPredicate.satisfiedBy works
        if (userID == null)
            return true

        //all relevant information
        val user = UserContainer.getUser(userID)
        val usersGroupIDs = user.getProcessGroupIds()
        val usersRoleIDs = user.getUserRoleIds()

        //user is in process's group and one of his/her roles is assigned to it
        val inUsersGroups = usersGroupIDs.contains(process.processGroupId)
        val assignedToUsersRoles = process.tasks.values.map { TaskTemplateContainer.getTaskTemplate(it.taskTemplateId).responsibleUserRoleId }.intersect(usersRoleIDs).isNotEmpty()
        if (inUsersGroups && assignedToUsersRoles)
            return true

        //user is assigned to one of the processes tasks
        if (process.getAssignees().map { it.id }.contains(userID))
            return true

        return false
    }
}
