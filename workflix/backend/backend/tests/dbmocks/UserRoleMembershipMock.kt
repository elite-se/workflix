package dbmocks

import de.se.team3.logic.DAOInterfaces.UserRoleMembershipDAOInterface

object UserRoleMembershipMock: UserRoleMembershipDAOInterface {
    override fun addUserToRole(userID: String, userRoleID: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUserFromRole(userID: String, userRoleID: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}