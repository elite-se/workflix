package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessGroupMembers
import de.se.team3.persistence.meta.ProcessGroupsTable
import de.se.team3.persistence.meta.ProcessesTable
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

// TODO(test) the entire class

object ProcessGroupDAO : ProcessGroupDAOInterface {
    override fun getAllProcessGroups(): List<ProcessGroup> {
        val processGroupResult = ProcessGroupsTable
            .select()

        val processGroups = ArrayList<ProcessGroup>()

        for (row in processGroupResult) {
            val members = ArrayList<User>()
            for (memberRow in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq ProcessGroupsTable.id }) {
                members.add(UserDAO.getUser(memberRow[ProcessGroupMembers.userID]!!))
            }

            val owner = UserDAO.getUser(row[ProcessGroupsTable.ownerId]!!)

            processGroups.add(ProcessGroup(
                row[ProcessGroupsTable.id]!!,
                owner,
                row[ProcessGroupsTable.title]!!,
                row[ProcessGroupsTable.description]!!,
                row[ProcessGroupsTable.createdAt]!!,
                members
            ))
        }

        return processGroups
    }

    override fun getProcessGroup(processGroupId: Int): ProcessGroup? {
        val processGroupResult = ProcessGroupsTable
            .select()
            .where { ProcessGroupsTable.id eq processGroupId }

        val row = processGroupResult.rowSet
        if (!row.next())
            return null

        val members = ArrayList<User>()
        for (memberRow in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq processGroupId }) {
            members.add(UserDAO.getUser(memberRow[ProcessGroupMembers.userID]!!))
        }

        val owner = UserDAO.getUser(row[ProcessGroupsTable.ownerId]!!)

        return ProcessGroup(row[ProcessGroupsTable.id]!!,
                            owner,
                            row[ProcessGroupsTable.title]!!,
                            row[ProcessGroupsTable.description]!!,
                            row[ProcessGroupsTable.createdAt]!!,
                            members)
    }

    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        return ProcessGroupsTable.insertAndGenerateKey {
            it.ownerId to processGroup.owner.id
            it.title to processGroup.title
            it.description to processGroup.description
            it.createdAt to processGroup.createdAt
            it.deleted to false
        } as Int
    }

    /**
     * Sets the deleted flag for the given process template.
     *
     * @return true if the process group do be deleted existed
     */
    override fun deleteProcessGroup(processGroupId: Int): Boolean {
        return ProcessGroupsTable.update {
            it.deleted to true
            where { it.id eq processGroupId }
        } != 0
    }
}
