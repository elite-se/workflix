package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessGroupMembers
import de.se.team3.persistence.meta.ProcessGroupsTable
import de.se.team3.persistence.meta.ProcessToGroup
import me.liuwj.ktorm.dsl.*

// TODO(test) the entire class

object ProcessGroupDAO : ProcessGroupDAOInterface {
    override fun getAllProcessGroups(offset: Int, limit: Int): Pair<List<ProcessGroup>, Int> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcessGroup(processGroupId: Int): ProcessGroup {
        val processGroupResult = ProcessGroupsTable
            .select()
            .where { ProcessGroupsTable.ID eq processGroupId }

        var members = ArrayList<User>()
        for (row in ProcessGroupMembers.select().where { ProcessGroupMembers.processGroupID eq processGroupId }) {
            members.add(UserDAO.getUser(row[ProcessGroupMembers.userID]!!))
        }

        var processes = ArrayList<Process>()
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
