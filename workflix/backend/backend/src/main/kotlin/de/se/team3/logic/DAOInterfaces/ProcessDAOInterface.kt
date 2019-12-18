package de.se.team3.logic.DAOInterfaces

import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.Process

interface ProcessDAOInterface {

    fun getAllProcessTemplates(offset: Int, limit: Int, owner: Set<User>?, status: Set<ProcessStatus>?): Pair<List<Process>, Int>

    fun getProcess(processId: Int): Process

    fun createProcess(process: Process): Int

    fun abortProcess(process: Process)

}