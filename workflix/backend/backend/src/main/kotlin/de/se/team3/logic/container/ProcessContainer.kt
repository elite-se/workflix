package de.se.team3.logic.container

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessQueryPredicate
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessDAO
import de.se.team3.webservice.containerInterfaces.ProcessContainerInterface

object ProcessContainer : ProcessContainerInterface {

    // The cached processes lay under their id.
    private val processesCache = HashMap<Int, Process>()

    // Indicates whether the cache is already filled with all elements
    private var filled = false

    /**
     * Ensures that all processes are cached.
     */
    private fun fillCache() {
        val processes = ProcessDAO.getAllProcesses()
        processes.forEach { process ->
            processesCache.put(process.id!!, process)
        }
        filled = true
    }

    /**
     * Returns a reduced form (without tasks) of all processes.
     */
    override fun getAllProcesses(predicate: ProcessQueryPredicate): List<Process> {
        if (!filled)
            fillCache()

        val processesFiltered = processesCache.map { it.value }.filter { process ->
            if (predicate.statuses.contains(process.getStatus()))
                true
            if (predicate.processGroupIds.contains(process.processGroupId))
                true
            false
        }

        // TODO filter involving

        return processesFiltered
    }

    /**
     * Returns the specified process.
     *
     * @throws NotFoundException Is thrown if the specified process does not exist.
     */
    override fun getProcess(processId: Int): Process {
        return if (processesCache.containsKey(processId)) {
            processesCache.get(processId)!!
        } else {
            val process = ProcessDAO.getProcess(processId)
                ?: throw NotFoundException("process not found")

            processesCache.put(processId, process)
            process
        }
    }

    /**
     * Creates the given process.
     */
    override fun createProcess(process: Process): Int {
        val newId = ProcessDAO.createProcess(process)

        // update process count and running processes of process template
        process.processTemplate.increaseProcessCounters()

        processesCache.put(newId, ProcessDAO.getProcess(newId)!!)
        return newId
    }

    /**
     * Closes the specified process if possible.
     *
     * @throws NotFoundException Is thrown if the specified process does not exist.
     */
    fun mayCloseProcess(processId: Int) {
        val process = getProcess(processId)
        if (!process.closeable())
            return

        ProcessDAO.closeProcess(processId)
        process.close()
    }

    /**
     * Aborts the specified process.
     *
     * @throws NotFoundException Is thrown if the specified process does not exist.
     */
    override fun abortProcess(processId: Int) {
        val process = getProcess(processId)

        val existed = ProcessDAO.abortProcess(processId)
        if (!existed)
            throw NotFoundException("process does not exist")

        // update status of process and running processes of process template
        process.abort()
    }
}
