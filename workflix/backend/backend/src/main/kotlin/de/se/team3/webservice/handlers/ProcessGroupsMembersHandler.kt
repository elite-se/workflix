package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupsMembershipContainer
import de.se.team3.logic.domain.ProcessGroupMembership
import de.se.team3.webservice.containerInterfaces.ProcessGroupsMembershipContainerInterface
import io.javalin.http.Context

/**
 * Handles requests to resources of form:
 * /processGroups/:processGroupId/members/:memberId
 */
object ProcessGroupsMembersHandler {

    private val processGroupsMembershipContainer: ProcessGroupsMembershipContainerInterface = ProcessGroupsMembershipContainer

    /**
     * Handles requests for creating a new process group membership.
     */
    fun create(ctx: Context, processGroupId: Int, memberId: String) {
        val processGroupMembership = ProcessGroupMembership(processGroupId, memberId)
        processGroupsMembershipContainer.createProcessGroupMembership(processGroupMembership)
    }

    /**
     * Handles requests for deleting a process group membership.
     */
    fun delete(ctx: Context, processGroupID: Int, memberId: String) {
        val processGroupMembership = ProcessGroupMembership(processGroupID, memberId)
        processGroupsMembershipContainer.deleteProcessGroupMembership(processGroupMembership)
    }
}
