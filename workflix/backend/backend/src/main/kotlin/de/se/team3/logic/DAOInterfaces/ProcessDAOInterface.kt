package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.Process

interface ProcessDAOInterface {

    fun getAllProcesses(): List<Process>

    fun getProcess(processId: Int): Process

    fun createProcess(process: Process): Int

    fun abortProcess(processId: Int)
}
