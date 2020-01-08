package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.ProcessTemplateDAOInterface
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.UserRole
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessTemplateDAO
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface

object ProcessTemplatesContainer : ProcessTemplateContainerInterface {

    private val processTemplatesDAO: ProcessTemplateDAOInterface = ProcessTemplateDAO

    // The cached process templates lay under their id.
    private val processTemplatesCache = HashMap<Int, ProcessTemplate>()

    // Indicates whether the cache is already filled with all elements
    private var filled = false

    /**
     * Ensures that all process templates are cached.
     */
    private fun fillCache() {
        val processTemplates = processTemplatesDAO.getAllProcessTemplates()
        processTemplates.forEach { processTemplate ->
            processTemplatesCache.put(processTemplate.id!!, processTemplate)
        }
        filled = true
    }

    /**
     * Returns a reduced form (without task templates) of all process templates.
     */
    override fun getAllProcessTemplates(): List<ProcessTemplate> {
        if (!filled)
            fillCache()

        return processTemplatesCache.map { it.value }.toList()
    }

    /**
     * Returns the specified process template.
     *
     * @throws NotFoundException Is thrown if the specified process template does not exist.
     */
    override fun getProcessTemplate(processTemplateId: Int): ProcessTemplate {
        return if (processTemplatesCache.containsKey(processTemplateId)) {
            processTemplatesCache.get(processTemplateId)!!
        } else {
            val processTemplate = processTemplatesDAO.getProcessTemplate(processTemplateId)
                ?: throw NotFoundException("process template not found")

            processTemplatesCache.put(processTemplateId, processTemplate)
            processTemplate
        }
    }

    /**
     * Checks whether the given user role is designated as responsible in any of
     * the active (not deleted) process templates.
     *
     * @throws NullPointerException Is thrown if the id of the given user role is null.
     *
     * @return True if and only if there is any active process template that designates
     * the given user role as responsible.
     */
    fun hasProcessTemplateUsingUserRole(userRole: UserRole): Boolean {
        if (userRole.id == null)
            throw NullPointerException("id of user role must not be null")

        if (!filled)
            fillCache()

        for ((key, processTemplate) in processTemplatesCache)
            if (!processTemplate.isDeleted() && processTemplate.usesUserRole(userRole))
                return true

        return false
    }

    /**
     * Creates the given process template.
     */
    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        val newId = processTemplatesDAO.createProcessTemplate(processTemplate)
        processTemplatesCache.put(newId, ProcessTemplateDAO.getProcessTemplate(newId)!!)
        return newId
    }

    /**
     * Updates the given process template.
     *
     * @throws NotFoundException Is thrown if the given process template does not exist.
     */
    override fun updateProcessTemplate(processTemplate: ProcessTemplate): Int {
        var newId: Int?
        val currentId = processTemplate.id!!

        if (processTemplate.getProcessCount() == 0) {
            val exists = processTemplatesDAO.updateProcessTemplate(processTemplate)
            if (!exists)
                throw NotFoundException("process template not found")

            newId = currentId
        } else {
            // copying necessary because of the creation timestamp
            val newProcessTemplate = ProcessTemplate(
                processTemplate.title,
                processTemplate.description,
                processTemplate.durationLimit,
                processTemplate.owner,
                processTemplate.taskTemplates
            )
            newId = processTemplatesDAO.createProcessTemplate(processTemplate.copy(formerVersionId = currentId))
        }

        processTemplatesCache[newId] = processTemplatesDAO.getProcessTemplate(newId)!!
        return newId
    }

    /**
     * Sets the deleted flag of the specified process template.
     *
     * @throws NotFoundException Is thrown if the specified process template does not exist.
     */
    override fun deleteProcessTemplate(processTemplateId: Int) {
        val processTemplate = getProcessTemplate(processTemplateId)

        val existed = processTemplatesDAO.deleteProcessTemplate(processTemplateId)
        if (!existed)
            throw NotFoundException("process template not found")

        processTemplate.delete()
    }
}
