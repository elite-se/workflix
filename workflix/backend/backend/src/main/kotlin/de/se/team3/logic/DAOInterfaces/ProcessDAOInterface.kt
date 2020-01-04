package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessQueryPredicate

interface ProcessDAOInterface {

    fun getAllProcesses(predicate: ProcessQueryPredicate): List<Process>

    fun getProcess(processId: Int): Process?

    fun createProcess(process: Process): Int

    fun closeProcess(processId: Int): Boolean

    fun abortProcess(processId: Int): Boolean
}
