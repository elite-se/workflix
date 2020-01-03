package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessGroupDAO
import de.se.team3.webservice.containerInterfaces.ProcessGroupContainerInterface

object ProcessGroupContainer : ProcessGroupContainerInterface {

    //caches process groups using their id
    private val processGroupsCache = HashMap<Int, ProcessGroup>()

    /**
     * @return All process groups currently saved in the database.
     * @throws NotFoundException The process group does not exist.
     */
    override fun getAllProcessGroups(): List<ProcessGroup> {
        return ProcessGroupDAO.getAllProcessGroups()
    }

    /**
     * @return Process group specified by its ID.
     * @throws NotFoundException The process group does not exist.
     */
    override fun getProcessGroup(processGroupId: Int): ProcessGroup {
        return if (processGroupsCache.containsKey(processGroupId)) {
            processGroupsCache[processGroupId]!!
        } else {
            val processGroup = ProcessGroupDAO.getProcessGroup(processGroupId)
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
        val ID = ProcessGroupDAO.createProcessGroup(processGroup)
        processGroupsCache[ID] = processGroup
        return ID
    }

    /**
     * Deletes a process group specified by its ID, i.e. sets its deleted-flag.
     *
     * @throws NotFoundException The process group does not exist.
     */
    override fun deleteProcessGroup(processGroupId: Int) {
        val existed = ProcessGroupDAO.deleteProcessGroup(processGroupId)
        if (!existed)
            throw NotFoundException("process group not found")
        processGroupsCache.remove(processGroupId)
    }
}
