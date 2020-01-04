package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupsMembershipDAOInterface
import de.se.team3.logic.domain.ProcessGroupMembership
import de.se.team3.persistence.meta.ProcessGroupsMembersTable
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey

object ProcessGroupsMembershipDAO : ProcessGroupsMembershipDAOInterface {

    /**
     * Creates the specified membership.
     */
    override fun createProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Int {
        val generatedMembershipId = ProcessGroupsMembersTable.insertAndGenerateKey { row ->
            row.processGroupId to processGroupMembership.processGroupId
            row.memberId to processGroupMembership.memberId
        }
        return generatedMembershipId as Int
    }

    /**
     * Deletes the specified membership.
     *
     * @return True if and only if the specified membership existed.
     */
    override fun deleteProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Boolean {
        val affectedRows = ProcessGroupsMembersTable.delete { row ->
            (row.processGroupId eq processGroupMembership.processGroupId) and
                    (row.memberId eq processGroupMembership.memberId)
        }
        return affectedRows != 0
    }

}