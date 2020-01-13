package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.ProcessDAOInterface
import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessQueryPredicate
import de.se.team3.logic.exceptions.AlreadyClosedException
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessDAO
import de.se.team3.webservice.containerInterfaces.ProcessContainerInterface
import java.time.Instant

object ProcessContainer : ProcessContainerInterface {

    private val processesDAO: ProcessDAOInterface = ProcessDAO

    // The cached processes lay under their id.
    private val processesCache = HashMap<Int, Process>()

    // Indicates whether the cache is already filled with all elements
    private var filled = false

    /**
     * Ensures that all processes are cached.
     */
    private fun fillCache() {
        val processes = processesDAO.getAllProcesses()
        processes.forEach { process ->
            processesCache.put(process.id!!, process)
        }
        filled = true
    }

    /**
     * Returns all processes fulfilling the given predicate.
     */
    override fun getAllProcesses(predicate: ProcessQueryPredicate): List<Process> {
        if (!filled)
            fillCache()

        return processesCache
            .map { it.value }
            .filter { predicate.satisfiedBy(it) }
    }

    /**
     * Returns all processes.
     */
    override fun getAllProcesses(): List<Process> {
        if (!filled)
            fillCache()

        return processesCache
            .map { it.value }
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
            val process = processesDAO.getProcess(processId)
                ?: throw NotFoundException("process not found")

            processesCache.put(processId, process)
            process
        }
    }

    /**
     * Creates the given process.
     */
    override fun createProcess(process: Process): Int {
        val newId = processesDAO.createProcess(process)

        // update process count and running processes of process template
        process.processTemplate.increaseProcessCounters()

        processesCache.put(newId, processesDAO.getProcess(newId)!!)
        return newId
    }

    /**
     * Updates the given process.
     *
     * @throws NotFoundException Is thrown if the given process does not exist.
     */
    override fun updateProcess(processId: Int, title: String, description: String, deadline: Instant) {
        val cachedProcess = getProcess(processId)

        // helper process to properly call the DAO
        val updatedProcess = Process(
            cachedProcess.id!!,
            cachedProcess.starter,
            cachedProcess.processGroup,
            cachedProcess.processTemplate,
            title,
            description,
            deadline
        )

        val exists = processesDAO.updateProcess(updatedProcess)
        if (!exists)
            throw NotFoundException("Process does not exist.")

        cachedProcess.setTitle(title)
        cachedProcess.setDescription(description)
        cachedProcess.setDeadline(deadline)
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

        processesDAO.closeProcess(processId)
        process.close()
    }

    /**
     * Aborts the specified process.
     *
     * @throws NotFoundException Is thrown if the specified process does not exist.
     */
    override fun abortProcess(processId: Int) {
        val process = getProcess(processId)

        val existed = processesDAO.abortProcess(processId)
        if (!existed)
            throw NotFoundException("process does not exist")

        // update status of process and running processes of process template
        process.abort()
    }
}
