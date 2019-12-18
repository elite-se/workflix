package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.PersonsResponsibleTable
import de.se.team3.persistence.meta.ProcessesTable
import de.se.team3.persistence.meta.TasksTable
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.TransactionIsolation
import me.liuwj.ktorm.dsl.insert
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

        try {
            // adds the process to db
            val generatedProcessId = ProcessesTable.insertAndGenerateKey { row ->
                row.templateID to processTemplate.id
                row.starterID to process.starter.id
                row.groupID to 20
                row.title to process.title
                row.description to process.description
                row.status to process.status.toString()
                row.startedAt to process.startedAt
                row.aborted to process.aborted
            }

            // adds the tasks to db
            processTemplate.taskTemplates?.map { it.value }?.forEach { taskTemplate ->
                val task = process.tasks.get(taskTemplate.id)!!

                val taskId = TasksTable.insertAndGenerateKey {
                    it.processID to generatedProcessId
                    it.templateID to taskTemplate.id
                    it.simpleClosing to task.simpleClosing
                }

                // adds the responsibilities to db
                task.usersResposible?.forEach {taskResponsibility ->
                    PersonsResponsibleTable.insert { row ->
                        row.taskID to taskId as Int
                        row.responsibleUserID to taskResponsibility.responsibleUser.id
                        row.done to false
                        row.doneAt to null
                    }
                }
            }

            transaction.commit()
            return generatedProcessId as Int
        } catch (e: Throwable) {
            transaction.rollback()
            throw e
            throw StorageException("" + e.message)
        }
    }

    override fun abortProcess(process: Process) {

    }
}
