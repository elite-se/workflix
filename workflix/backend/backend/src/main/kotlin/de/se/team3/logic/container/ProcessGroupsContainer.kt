package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessGroupsDAO
import de.se.team3.webservice.containerInterfaces.ProcessGroupsContainerInterface

object ProcessGroupsContainer : ProcessGroupsContainerInterface {

    // caches process groups using their id
    private val processGroupsCache = HashMap<Int, ProcessGroup>()

    /**
     * Returns a list of all process groups.
     *
     * @return All process groups currently saved in the database.
     */
    override fun getAllProcessGroups(): List<ProcessGroup> {
        return ProcessGroupsDAO.getAllProcessGroups()
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
            val processGroup = ProcessGroupsDAO.getProcessGroup(processGroupId)
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
        val newId = ProcessGroupsDAO.createProcessGroup(processGroup)
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

        val exists = ProcessGroupsDAO.updateProcessGroup(processGroup)
        if (!exists)
            throw NotFoundException("process group does not exist")

        cachedProcessGroup.setTitle(processGroup.getTitle())
        cachedProcessGroup.setDescription(processGroup.getDescription())
        cachedProcessGroup.setOwnerById(processGroup.getOwner().id)
    }

    /**
     * Deletes a process group specified by its ID, i.e. sets its deleted-flag.
     *
     * @throws NotFoundException The process group does not exist.
     */
    override fun deleteProcessGroup(processGroupId: Int) {
        val existed = ProcessGroupsDAO.deleteProcessGroup(processGroupId)
        if (!existed)
            throw NotFoundException("process group not found")

        processGroupsCache.remove(processGroupId)
    }
}
