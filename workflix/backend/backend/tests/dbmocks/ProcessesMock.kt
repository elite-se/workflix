package dbmocks

import de.se.team3.logic.DAOInterfaces.ProcessDAOInterface
import de.se.team3.logic.domain.Process

object ProcessesMock: ProcessDAOInterface {
    override fun getAllProcesses(): List<Process> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcess(processId: Int): Process? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createProcess(process: Process): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeProcess(processId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun abortProcess(processId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}