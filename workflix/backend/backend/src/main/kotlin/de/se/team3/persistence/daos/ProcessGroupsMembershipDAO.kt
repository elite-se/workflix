package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupsMembershiipDAOInterface
import de.se.team3.persistence.meta.ProcessGroupsMembersTable
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey

object ProcessGroupsMembershipDAO : ProcessGroupsMembershiipDAOInterface {

    /**
     * Creates the specified membership.
     */
    override fun createProcessGroupMembership(processGroupId: Int, memberId: String): Int {
        val generatedMembershipId = ProcessGroupsMembersTable.insertAndGenerateKey { row ->
            row.processGroupId to processGroupId
            row.memberId to memberId
        }
        return generatedMembershipId as Int
    }

    /**
     * Deletes the specified membership.
     *
     * @return True if and only if the specified membership existed.
     */
    override fun deleteProcessGroupMembership(processGroupId: Int, memberId: String): Boolean {
        val affectedRows = ProcessGroupsMembersTable.delete { row ->
            (row.processGroupId eq processGroupId) and
                    (row.memberId eq memberId)
        }
        return affectedRows != 0
    }

}