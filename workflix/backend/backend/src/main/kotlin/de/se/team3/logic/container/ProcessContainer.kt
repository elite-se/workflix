package de.se.team3.logic.container

import de.se.team3.logic.domain.Process
import de.se.team3.webservice.containerInterfaces.ProcessContainerInterface

object ProcessContainer : ProcessContainerInterface {

    override fun getAllProcesses(page: Int): Pair<Process, Int> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcess(processId: Int): Process {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createProcess(process: Process): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun abortProcess(process: Process) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
