package de.se.team3.logic.container

import de.se.team3.logic.domain.UserRole
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.UserRoleDAO
import de.se.team3.webservice.containerInterfaces.UserRoleContainerInterface

object UserRoleContainer : UserRoleContainerInterface {

    // Each user role lays under its id
    private val userRoleCache = HashMap<Int, UserRole>()

    // Indicates whether the cache is already filled with all elements
    private var filled = false

    /**
     * Ensures that all user roles are cached.
     */
    private fun fillCache() {
        val userRoles = UserRoleDAO.getAllUserRoles()
        userRoles.forEach { userRole ->
            userRoleCache.put(userRole.id!!, userRole)
        }
        filled = true
    }

    /**
     * Returns all user roles.
     */
    override fun getAllUserRoles(): List<UserRole> {
        if (!filled)
            fillCache()

        return userRoleCache.map { it.value }.toList()
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
}
