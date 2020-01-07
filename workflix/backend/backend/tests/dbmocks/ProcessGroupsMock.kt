package dbmocks

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.ProcessGroup

object ProcessGroupsMock: ProcessGroupDAOInterface {
    override fun getAllProcessGroups(): List<ProcessGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcessGroup(processGroupId: Int): ProcessGroup? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateProcessGroup(processGroup: ProcessGroup): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteProcessGroup(processGroupId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}