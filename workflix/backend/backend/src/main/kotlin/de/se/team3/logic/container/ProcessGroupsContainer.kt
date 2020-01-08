package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.ProcessGroupDAOInterface
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessGroupsDAO
import de.se.team3.webservice.containerInterfaces.ProcessGroupsContainerInterface

object ProcessGroupsContainer : ProcessGroupsContainerInterface {

    private val processGroupsDAO: ProcessGroupDAOInterface = ProcessGroupsDAO

    // caches process groups using their id
    private val processGroupsCache = HashMap<Int, ProcessGroup>()

    // Indicates whether the cache is already filled with all elements
    private var filled = false

    /**
     * Ensures that all process groups are cached.
     */
    private fun fillCache() {
        val processGroups = processGroupsDAO.getAllProcessGroups()
        processGroups.forEach { processGroup ->
            processGroupsCache.put(processGroup.id!!, processGroup)
        }
        filled = true
    }

    /**
     * Returns a list of all process groups.
     *
     * @return All process groups currently saved in the database.
     */
    override fun getAllProcessGroups(): List<ProcessGroup> {
        if (!filled)
            fillCache()

        return processGroupsCache.map { it.value }.toList()
    }

    /**
     * Returns the specified process group.
     *
     * @return Process group specified by its ID.
     * @throws NotFoundException The process group does not exist.
     */
    override fun getProcessGroup(processGroupId: Int): ProcessGroup {
        return if (processGroupsCache.containsKey(processGroupId)) {
            processGroupsCache[processGroupId]!!
        } else {
            val processGroup = processGroupsDAO.getProcessGroup(processGroupId)
                ?: throw NotFoundException("process group not found")

            processGroupsCache[processGroupId] = processGroup
            processGroup
        }
    }

    /**
     * Creates a new process group.
     *
     * @return automatically generated unique ID
     */
    override fun createProcessGroup(processGroup: ProcessGroup): Int {
        val newId = processGroupsDAO.createProcessGroup(processGroup)
        processGroupsCache[newId] = processGroup.copy(id = newId)
        return newId
    }

    /**
     * Updates the given process group.
     *
     * @throws NotFoundException Is thrown if the given process group does not exist.
     */
    override fun updateProcessGroup(processGroup: ProcessGroup) {
        val cachedProcessGroup = getProcessGroup(processGroup.id!!)

        val exists = processGroupsDAO.updateProcessGroup(processGroup)
        if (!exists)
            throw NotFoundException("process group does not exist")

        cachedProcessGroup.setTitle(processGroup.getTitle())
        cachedProcessGroup.setDescription(processGroup.getDescription())
        cachedProcessGroup.setOwner(processGroup.getOwner())
    }

    /**
     * Deletes a process group specified by its ID, i.e. sets its deleted-flag.
     *
     * @throws NotFoundException The process group does not exist.
     */
    override fun deleteProcessGroup(processGroupId: Int) {
        val cachedProcessGroup = getProcessGroup(processGroupId)

        val existed = processGroupsDAO.deleteProcessGroup(processGroupId)
        if (!existed)
            throw NotFoundException("process group not found")

        cachedProcessGroup.delete()
    }
}
