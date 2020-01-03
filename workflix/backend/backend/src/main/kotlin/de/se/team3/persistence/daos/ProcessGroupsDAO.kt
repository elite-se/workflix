package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessGroupsMembersTable
import de.se.team3.persistence.meta.ProcessGroupsTable
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

// TODO(test) the entire class

object ProcessGroupsDAO : ProcessGroupDAOInterface {

    override fun getAllProcessGroups(): List<ProcessGroup> {
        val processGroupResult = ProcessGroupsTable
            .select()

        val processGroups = ArrayList<ProcessGroup>()

        for (row in processGroupResult) {
            val processGroupId = row[ProcessGroupsTable.id]!!
            val groupMembersResult = ProcessGroupsMembersTable.select()
                .where { ProcessGroupsMembersTable.processGroupId eq processGroupId }

            val members = ArrayList<User>()
            for (memberRow in groupMembersResult) {
                members.add(UserDAO.getUser(memberRow[ProcessGroupsMembersTable.memberId]!!))
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
        for (memberRow in ProcessGroupsMembersTable.select().where { ProcessGroupsMembersTable.processGroupId eq processGroupId }) {
            members.add(UserDAO.getUser(memberRow[ProcessGroupsMembersTable.memberId]!!))
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
     * Updates the given process template.
     *
     * @return True if and only if the given process group exists.
     */
    override fun updateProcessGroup(processGroup: ProcessGroup): Boolean {
        val affectedRows = ProcessGroupsTable.update { row ->
            row.title to processGroup.title
            row.description to processGroup.description
            row.ownerId to processGroup.owner.id

            where {
                (row.id eq processGroup.id!!) and
                        (row.deleted notEq true)
            }
        }
        return affectedRows != 0
    }

    /**
     * Sets the deleted flag for the given process template.
     *
     * @return true if the process group do be deleted existed
     */
    override fun deleteProcessGroup(processGroupId: Int): Boolean {
        return ProcessGroupsTable.update {
            it.deleted to true
            where {
                (it.id eq processGroupId) and
                        (it.deleted notEq true)
            }
        } != 0
    }
}
