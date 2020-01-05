package de.se.team3.logic.container

import de.se.team3.logic.domain.UserRole
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.UserRoleDAO
import de.se.team3.webservice.containerInterfaces.UserRoleContainerInterface

object UserRoleContainer : UserRoleContainerInterface {

    private val userRoleCache = HashMap<Int, UserRole>()

    override fun getAllUserRoles(): List<UserRole> {
        return UserRoleDAO.getAllUserRoles()
    }

    /**
     * Returns the specified user role.
     *
     * @throws NotFoundException Is thrown if the specified user role does not exist.
     */
    override fun getUserRole(userRoleID: Int): UserRole {
        return if (userRoleCache.containsKey(userRoleID)) {
            userRoleCache[userRoleID]!!
        } else {
            val userRole = UserRoleDAO.getUserRole(userRoleID)
                ?: throw NotFoundException("user role $userRoleID does not exist")

            userRoleCache[userRoleID] = userRole
            userRole
        }
    }

    /**
     * Checks whether the specified user role exists or not.
     */
    fun hasUserRole(userRoleId: Int): Boolean {
        if (userRoleCache.containsKey(userRoleId))
            return true

        val userRole = UserRoleDAO.getUserRole(userRoleId)
        if (userRole != null) {
            userRoleCache.put(userRoleId, userRole)
            return true
        }

        return false
    }

    /**
     * Creates the given user role.
     *
     * @return The generated id of the user role.
     */
    override fun createUserRole(userRole: UserRole): Int {
        val newID = UserRoleDAO.createUserRole(userRole)
        userRoleCache[newID] = userRole.copy(id = newID)
        return newID
    }

    /**
     * Updates the given user role.
     *
     * @throws NotFoundException Is thrown if the given user role does not exist.
     */
    override fun updateUserRole(userRole: UserRole) {
        if (!hasUserRole(userRole.id!!))
            throw NotFoundException("user role does not exist")

        UserRoleDAO.updateUserRole(userRole)
        userRoleCache[userRole.id] = userRole
    }

    /**
     * Deletes the specified user role.
     *
     * @throws NotFoundException Is thrown if the specified user role does not exist.
     */
    override fun deleteUserRole(userRoleID: Int) {
        if (!UserRoleDAO.deleteUserRole(userRoleID))
            throw NotFoundException("user role $userRoleID does not exist")

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
