package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.Processes
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.insertAndGenerateKey

object ProcessDAO : ProcessDAOInterface {

    override fun getAllProcessTemplates(offset: Int, limit: Int, owner: Set<User>?, status: Set<ProcessStatus>?): Pair<List<Process>, Int> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcess(processId: Int): Process {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createProcess(process: Process): Int {
        val transactionManager = Database.global.transactionManager
        val transaction = transactionManager.newTransaction(isolation = TransactionIsolation.REPEATABLE_READ)

        val processTemplate = process.processTemplate!!

        val processId = Processes.insertAndGenerateKey {
            it.templateID to processTemplate.id
            it.starterID to process.starter.id
            it.groupID to 20
            it.title to process.title
            it.status to process.status.toString()
            it.startedAt to process.startedAt
        }

        for ((taskTemplateId, taskTemplate) in processTemplate.taskTemplates!!)

        return 0
    }

    override fun abortProcess(process: Process) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
