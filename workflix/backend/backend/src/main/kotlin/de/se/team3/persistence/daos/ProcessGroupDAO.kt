package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessGroupMembers
import de.se.team3.persistence.meta.ProcessGroupsTable
import de.se.team3.persistence.meta.ProcessToGroup
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
            for (memberRow in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq ProcessGroupsTable.ID }) {
                members.add(UserDAO.getUser(memberRow[ProcessGroupMembers.userID]!!))
            }

            val processes = ArrayList<Process>()
            for (processRow in ProcessToGroup.select().where { ProcessToGroup.ProcessGroupID eq ProcessGroupsTable.ID }) {
                processes.add(ProcessDAO.getProcess(processRow[ProcessToGroup.ProcessID]!!))
            }

            val owner = UserDAO.getUser(row[ProcessGroupsTable.ownerID]!!)

            processGroups.add(ProcessGroup(
                row[ProcessGroupsTable.ID]!!,
                owner,
                row[ProcessGroupsTable.title]!!,
                row[ProcessGroupsTable.description]!!,
                row[ProcessGroupsTable.createdAt]!!,
                processes,
                members
            ))
        }

        return processGroups
    }

    override fun getProcessGroup(processGroupId: Int): ProcessGroup {
        val processGroupResult = ProcessGroupsTable
            .select()
            .where { ProcessGroupsTable.ID eq processGroupId }

        val members = ArrayList<User>()
        for (row in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq processGroupId }) {
            members.add(UserDAO.getUser(row[ProcessGroupMembers.userID]!!))
        }

        val processes = ArrayList<Process>()
        for (row in ProcessToGroup.select().where { ProcessToGroup.ProcessGroupID eq processGroupId }) {
            processes.add(ProcessDAO.getProcess(row[ProcessToGroup.ProcessID]!!))
        }

        val row = processGroupResult.rowSet.iterator().next()

        val owner = UserDAO.getUser(row[ProcessGroupsTable.ownerID]!!)

        return ProcessGroup(row[ProcessGroupsTable.ID]!!,
                            owner,
                            row[ProcessGroupsTable.title]!!,
                            row[ProcessGroupsTable.description]!!,
                            row[ProcessGroupsTable.createdAt]!!,
                            processes,
                            members)
    }

    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        return ProcessGroupsTable.insertAndGenerateKey {
            it.ownerID to processGroup.owner.id
            it.title to processGroup.title
            it.description to processGroup.description
            it.createdAt to processGroup.createdAt
            it.deleted to false
        } as Int
    }

    /**
     * Sets the deleted flag for the given process template.
     */
    override fun deleteProcessGroup(processGroupId: Int) {
            val affectedRows = ProcessGroupsTable.update {
                it.deleted to true
                where { it.ID eq processGroupId }
            }
            if (affectedRows == 0)
                throw NoSuchElementException()
    }
}
