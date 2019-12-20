package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.persistence.meta.ProcessGroupsTable
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.update

object ProcessGroupDAO : ProcessGroupDAOInterface {
    override fun getAllProcessGroups(offset: Int, limit: Int): Pair<List<ProcessGroup>, Int> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcessGroup(processGroupId: Int): ProcessGroup {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
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
