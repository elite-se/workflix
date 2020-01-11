package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessQueryPredicate
import java.time.Instant

interface ProcessContainerInterface {

    fun getAllProcesses(predicate: ProcessQueryPredicate): List<Process>

    fun getAllProcesses(): List<Process>

    fun getProcess(processId: Int): Process

    fun createProcess(process: Process): Int

    fun updateProcess(processId: Int, title: String, description: String, deadline: Instant?)

    fun abortProcess(processId: Int)
}
