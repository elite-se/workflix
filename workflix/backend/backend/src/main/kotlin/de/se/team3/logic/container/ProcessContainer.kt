package de.se.team3.logic.container

import de.se.team3.logic.domain.Process
import de.se.team3.persistence.daos.ProcessDAO
import de.se.team3.persistence.daos.TasksDAO
import de.se.team3.webservice.containerInterfaces.ProcessContainerInterface

object ProcessContainer : ProcessContainerInterface {

    override fun getAllProcesses(): List<Process> {
        return ProcessDAO.getAllProcesses()
    }

    override fun getProcess(processId: Int): Process {
        return ProcessDAO.getProcess(processId)
    }

    fun getProcessForTask(taskId: Int): Process {
        val processId = TasksDAO.getProcessIdForTask(taskId)
        return ProcessDAO.getProcess(processId)
    }

    override fun createProcess(process: Process): Int {
        val newId = ProcessDAO.createProcess(process)

        val processTemplate = process.processTemplate
        processTemplate.processCount += 1
        processTemplate.runningProcesses += 1

        return newId
    }

    override fun abortProcess(processId: Int) {
        ProcessDAO.abortProcess(processId)

        val process = ProcessDAO.getProcess(processId)

        val processTemplate = process.processTemplate
        processTemplate.runningProcesses -= 1
    }
}
