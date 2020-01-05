package dbmocks

import de.se.team3.logic.DAOInterfaces.UserRoleDAOInterface
import de.se.team3.logic.domain.UserRole

object UserRolesMock: UserRoleDAOInterface {
    override fun getAllUserRoles(): List<UserRole> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserRole(userRoleID: Int): UserRole? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUserRole(userRole: UserRole): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUserRole(userRole: UserRole) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUserRole(userRoleID: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}