package de.se.team3.logic.container

import de.se.team3.logic.domain.UserRole
import de.se.team3.persistence.daos.UserRoleDAO
import de.se.team3.webservice.containerInterfaces.UserRoleContainerInterface

object UserRoleContainer : UserRoleContainerInterface {

    private val userRoleCache = HashMap<Int, UserRole>()

    override fun getAllUserRoles(): List<UserRole> {
        return UserRoleDAO.getAllUserRoles()
    }

    override fun getUserRole(userRoleID: Int): UserRole {
        return if (userRoleCache.containsKey(userRoleID)) {
            userRoleCache[userRoleID]!!
        } else {
            val userRole = UserRoleDAO.getUserRole(userRoleID)
            userRoleCache[userRoleID] = userRole
            userRole
        }
    }

    override fun createUserRole(userRole: UserRole): Int {
        val newID = UserRoleDAO.createUserRole(userRole)
        userRoleCache[newID] = userRole
        return newID
    }

    override fun updateUserRole(userRole: UserRole) {
        UserRoleDAO.updateUserRole(userRole)
        userRoleCache[userRole.id] = userRole
    }

    override fun deleteUserRole(userRoleID: Int) {
        UserRoleDAO.deleteUserRole(userRoleID)
        userRoleCache.remove(userRoleID)
    }

    override fun addUserToRole(userID: String, userRoleID: Int) {
        UserRoleDAO.addUserToRole(userID, userRoleID)
        if (userRoleCache.containsKey(userRoleID))
            userRoleCache[userRoleID]!!.members.add(UserContainer.getUser(userID))
        else
            userRoleCache[userRoleID] = getUserRole(userRoleID)
    }

    override fun deleteUserFromRole(userID: String, userRoleID: Int) {
        UserRoleDAO.deleteUserFromRole(userID, userRoleID)
        if (userRoleCache.containsKey(userRoleID))
            userRoleCache[userRoleID]!!.members.remove(UserContainer.getUser(userID))
        else
            userRoleCache[userRoleID] = getUserRole(userRoleID)
    }
}
