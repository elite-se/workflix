package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupsMembershipContainer
import de.se.team3.logic.domain.ProcessGroupMembership
import io.javalin.http.Context

/**
 * Handles requests to resources of form:
 * /processGroups/:processGroupId/members/:memberId
 */
object ProcessGroupsMembersHandler {

    /**
     * Handles requests for creating a new process group membership.
     */
    fun create(ctx: Context, processGroupId: Int, memberId: String) {
        val processGroupMembership = ProcessGroupMembership(processGroupId, memberId)
        ProcessGroupsMembershipContainer.createProcessGroupMembership(processGroupMembership)
    }

    /**
     * Handles requests for deleting a process group membership.
     */
    fun delete(ctx: Context, processGroupID: Int, memberId: String) {
        val processGroupMembership = ProcessGroupMembership(processGroupID, memberId)
        ProcessGroupsMembershipContainer.deleteProcessGroupMembership(processGroupMembership)
    }
}
