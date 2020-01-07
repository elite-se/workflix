package dbmocks

import de.se.team3.logic.DAOInterfaces.ProcessGroupsMembershipDAOInterface
import de.se.team3.logic.domain.ProcessGroupMembership

object ProcessGroupMembershipMock: ProcessGroupsMembershipDAOInterface {
    override fun createProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteProcessGroupMembership(processGroupMembership: ProcessGroupMembership): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}