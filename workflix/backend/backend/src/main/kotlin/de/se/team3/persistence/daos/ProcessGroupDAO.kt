package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.persistence.meta.ProcessGroupsTable
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
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
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        try {
            val generatedProcessGroupID = ProcessGroupsTable.insertAndGenerateKey {
                it.ownerID to processGroup.owner.id
                it.title to processGroup.title
                it.description to processGroup.description
                it.createdAt to processGroup.createdAt
                it.deleted to false
            }
        } catch (e: Throwable) {
            transaction.rollback()
            throw StorageException("Storage Exception: " + e.message)
        }
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
