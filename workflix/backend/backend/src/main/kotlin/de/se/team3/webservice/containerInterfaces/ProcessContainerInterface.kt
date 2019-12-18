package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.Process

interface ProcessContainerInterface {

    fun getAllProcesses(page: Int): Pair<Process, Int>

    fun getProcess(processId: Int): Process

    fun createProcess(process: Process): Int

    fun abortProcess(process: Process)

}