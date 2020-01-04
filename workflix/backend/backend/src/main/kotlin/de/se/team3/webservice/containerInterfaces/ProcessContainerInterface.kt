package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessQueryPredicate

interface ProcessContainerInterface {

    fun getAllProcesses(predicate: ProcessQueryPredicate): List<Process>

    fun getProcess(processId: Int): Process

    fun createProcess(process: Process): Int

    fun abortProcess(processId: Int)
}
