package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.container.UserContainer
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
                members.add(UserContainer.getUser(memberRow[ProcessGroupMembers.userID]!!))
            }

            val processes = ArrayList<Process>()
            for (processRow in ProcessesTable.select().where { ProcessesTable.groupId eq ProcessGroupsTable.id }) {
                processes.add(ProcessDAO.getProcess(processRow[ProcessesTable.id]!!)!!)
            }

            val owner = UserContainer.getUser(row[ProcessGroupsTable.ownerId]!!)

            processGroups.add(ProcessGroup(
                row[ProcessGroupsTable.id]!!,
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
            .where { ProcessGroupsTable.id eq processGroupId }

        val members = ArrayList<User>()
        for (row in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq processGroupId }) {
            members.add(UserContainer.getUser(row[ProcessGroupMembers.userID]!!))
        }

        val processes = ArrayList<Process>()
        for (row in ProcessesTable.select().where { ProcessesTable.groupId eq processGroupId }) {
            processes.add(ProcessDAO.getProcess(row[ProcessesTable.id]!!)!!)
        }

        val row = processGroupResult.rowSet.iterator().next()

        val owner = UserContainer.getUser(row[ProcessGroupsTable.ownerId]!!)

        return ProcessGroup(row[ProcessGroupsTable.id]!!,
                            owner,
                            row[ProcessGroupsTable.title]!!,
                            row[ProcessGroupsTable.description]!!,
                            row[ProcessGroupsTable.createdAt]!!,
                            processes,
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
     */
    override fun deleteProcessGroup(processGroupId: Int) {
            val affectedRows = ProcessGroupsTable.update {
                it.deleted to true
                where { it.id eq processGroupId }
            }
            if (affectedRows == 0)
                throw NoSuchElementException()
    }
}
