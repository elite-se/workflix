package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessGroupsMembersTable
import de.se.team3.persistence.meta.ProcessGroupsTable
import de.se.team3.webservice.containerInterfaces.UserContainerInterface
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

// TODO(test) the entire class

object ProcessGroupsDAO : ProcessGroupDAOInterface {

    private val usersContainer: UserContainerInterface = UserContainer

    /**
     * Makes a process group form the given row and members.
     */
    private fun makeProcessGroup(row: QueryRowSet, members: MutableList<User>): ProcessGroup {
        val owner = usersContainer.getUser(row[ProcessGroupsTable.ownerId]!!)
        return ProcessGroup(
            row[ProcessGroupsTable.id]!!,
            owner,
            row[ProcessGroupsTable.title]!!,
            row[ProcessGroupsTable.description]!!,
            row[ProcessGroupsTable.createdAt]!!,
            row[ProcessGroupsTable.deleted]!!,
            members
        )
    }

    /**
     * Returns a list of all process groups.
     */
    override fun getAllProcessGroups(): List<ProcessGroup> {
        val processGroupResult = ProcessGroupsTable.select()
            .where { ProcessGroupsTable.deleted notEq true }

        val processGroups = ArrayList<ProcessGroup>()

        for (row in processGroupResult) {
            val processGroupId = row[ProcessGroupsTable.id]!!
            val groupMembersResult = ProcessGroupsMembersTable.select()
                .where { ProcessGroupsMembersTable.processGroupId eq processGroupId }

            val members = ArrayList<User>()
            for (memberRow in groupMembersResult) {
                members.add(usersContainer.getUser(memberRow[ProcessGroupsMembersTable.memberId]!!))
            }

            processGroups.add(makeProcessGroup(row, members))
        }

        return processGroups
    }

    /**
     * Returns the specified process Group.
     *
     * @return The specified process group or null if the specified group does not exist.
     */
    override fun getProcessGroup(processGroupId: Int): ProcessGroup? {
        val processGroupResult = ProcessGroupsTable
            .select()
            .where { ProcessGroupsTable.id eq processGroupId }

        val row = processGroupResult.rowSet
        if (!row.next())
            return null

        val members = ArrayList<User>()
        for (memberRow in ProcessGroupsMembersTable.select().where { ProcessGroupsMembersTable.processGroupId eq processGroupId }) {
            members.add(UserContainer.getUser(memberRow[ProcessGroupsMembersTable.memberId]!!))
        }

        return makeProcessGroup(row, members)
    }

    /**
     * Creates the given process group.
     *
     * @return The generated id of the process group.
     */
    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        return ProcessGroupsTable.insertAndGenerateKey {
            it.ownerId to processGroup.getOwner().id
            it.title to processGroup.getTitle()
            it.description to processGroup.getDescription()
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
            row.title to processGroup.getTitle()
            row.description to processGroup.getDescription()
            row.ownerId to processGroup.getOwner().id

            where {
                (row.id eq processGroup.id!!) and
                        (row.deleted notEq true)
            }
        }
        return affectedRows != 0
    }

    /**
     * Deletes the specified process group.
     *
     * @return True if and only if the specified process group existed.
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
