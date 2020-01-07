package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.UserRoleDAOInterface
import de.se.team3.logic.domain.UserRole
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.logic.exceptions.UnsatisfiedPreconditionException
import de.se.team3.persistence.daos.UserRoleDAO
import de.se.team3.webservice.containerInterfaces.UserRoleContainerInterface

object UserRoleContainer : UserRoleContainerInterface {

    private val userRoleDAO: UserRoleDAOInterface = UserRoleDAO

    // Each user role lays under its id
    private val userRoleCache = HashMap<Int, UserRole>()

    // Indicates whether the cache is already filled with all elements
    private var filled = false

    /**
     * Ensures that all user roles are cached.
     */
    private fun fillCache() {
        val userRoles = userRoleDAO.getAllUserRoles()
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

        return userRoleCache
            .map { it.value }
            .filter { !it.isDeleted() }
            .toList()
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
            val userRole = userRoleDAO.getUserRole(userRoleID)
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

        val userRole = userRoleDAO.getUserRole(userRoleId)
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
        val newID = userRoleDAO.createUserRole(userRole)
        userRoleCache[newID] = userRole.copy(id = newID)
        return newID
    }

    /**
     * Updates the given user role.
     */
    override fun updateUserRole(userRole: UserRole) {
        val cachedUserRole = getUserRole(userRole.id!!) // throws NotFoundException

        userRoleDAO.updateUserRole(userRole)

        cachedUserRole.setName(userRole.getName())
        cachedUserRole.setDescription(userRole.getDescription())
    }

    /**
     * Deletes the specified user role.
     *
     * @throws NotFoundException Is thrown if the specified user role does not exist.
     * @throws UnsatisfiedPreconditionException Is thrown if the specified user role is in use in an
     * active (not deleted) process template.
     */
    override fun deleteUserRole(userRoleID: Int) {
        val userRole = getUserRole(userRoleID)

        if (userRole.isUsedInActiveProcessTemplate())
            throw UnsatisfiedPreconditionException("user role is in use in an active process template")
        if (!userRoleDAO.deleteUserRole(userRoleID))
            throw NotFoundException("user role $userRoleID does not exist")

        userRole.delete()
    }
}
