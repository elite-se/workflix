package de.se.team3.logic.util

import de.se.team3.logic.container.TaskTemplateContainer
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.Process

object RelevantProcessQuerying {

    /**
     * @return true iff the process is in a process group of the user associated with the given userID and
     * one of the user's role is is assigned to the process, or the user is assigned to one of the processes tasks.
     */
    fun isRelevantFor(userID: String?, process: Process): Boolean {
        // no userID given is handled as true so that the AND-clause in ProcessQueryPredicate.satisfiedBy works
        if (userID == null)
            return true

        // all relevant information
        val user = UserContainer.getUser(userID)
        val usersGroupIDs = user.getProcessGroupIds()
        val usersRoleIDs = user.getUserRoleIds()

        // user is in process's group and one of his/her roles is assigned to it
        val inUsersGroups = usersGroupIDs.contains(process.processGroupId)
        val assignedToUsersRoles = process.tasks.values.map { TaskTemplateContainer.getTaskTemplate(it.taskTemplateId).responsibleUserRoleId }.intersect(usersRoleIDs).isNotEmpty()
        if (inUsersGroups && assignedToUsersRoles)
            return true

        // user is assigned to one of the processes tasks
        if (process.getAssignees().map { it.id }.contains(userID))
            return true

        return false
    }
}
