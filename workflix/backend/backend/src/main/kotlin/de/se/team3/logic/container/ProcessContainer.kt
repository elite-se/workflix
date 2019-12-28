package de.se.team3.logic.container

import de.se.team3.logic.domain.Process
import de.se.team3.persistence.daos.ProcessDAO
import de.se.team3.webservice.containerInterfaces.ProcessContainerInterface

object ProcessContainer : ProcessContainerInterface {

    override fun getAllProcesses(): List<Process> {
        return ProcessDAO.getAllProcesses()
    }

    override fun getProcess(processId: Int): Process {
        return ProcessDAO.getProcess(processId)
    }

    override fun createProcess(process: Process): Int {
        return ProcessDAO.createProcess(process)
    }

    override fun abortProcess(processId: Int) {
        ProcessDAO.abortProcess(processId)
    }
}
