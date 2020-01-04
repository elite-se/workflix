package de.se.team3.logic.DAOInterfaces

interface ProcessGroupsMembershipDAOInterface {

    fun createProcessGroupMembership(processGroupId: Int, memberId: String): Int

    fun deleteProcessGroupMembership(processGroupId: Int, memberId: String): Boolean
}
