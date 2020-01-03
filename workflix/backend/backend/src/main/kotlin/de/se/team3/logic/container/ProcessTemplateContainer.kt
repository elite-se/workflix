package de.se.team3.logic.container

import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.ProcessTemplateDAO
import de.se.team3.webservice.containerInterfaces.ProcessTemplateContainerInterface

object ProcessTemplateContainer : ProcessTemplateContainerInterface {

    // The cached process templates lay under their id.
    private val processTemplatesCache = HashMap<Int, ProcessTemplate>()

    /**
     * Returns a reduced form (without task templates) of all process templates.
     */
    override fun getAllProcessTemplates(): List<ProcessTemplate> {
        return ProcessTemplateDAO.getAllProcessTemplates()
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
            val processTemplate = ProcessTemplateDAO.getProcessTemplate(processTemplateId)
                ?: throw NotFoundException("process template not found")

            processTemplatesCache.put(processTemplateId, processTemplate)
            processTemplate
        }
    }

    /**
     * Creates the given process template.
     */
    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        val newId = ProcessTemplateDAO.createProcessTemplate(processTemplate)
        processTemplatesCache.put(newId, ProcessTemplateDAO.getProcessTemplate(newId)!!)
        return newId
    }

    /**
     * Updates the given process template.
     *
     * @throws NotFoundException Is thrown if the given process template does not exist.
     */
    override fun updateProcessTemplate(processTemplate: ProcessTemplate): Int {
        var newId: Int? = null
        val currentId = processTemplate.id!!

        if (processTemplate.processCount == 0) {
            val exists = ProcessTemplateDAO.updateProcessTemplate(processTemplate)
            if (!exists)
                throw NotFoundException("process template not found")

            newId = currentId
        } else {
            // copying necessary because of the creation timestamp
            val newProcessTemplate = ProcessTemplate(
                processTemplate.title,
                processTemplate.description,
                processTemplate.durationLimit,
                processTemplate.owner.id,
                processTemplate.taskTemplates!!
            )
            newId = ProcessTemplateDAO.createProcessTemplate(processTemplate.copy(formerVersionId = currentId))
        }

        processTemplatesCache.put(newId, ProcessTemplateDAO.getProcessTemplate(newId)!!)
        return newId
    }

    /**
     * Sets the deleted flag of the specified process template.
     *
     * @throws NotFoundException Is thrown if the specified process template does not exist.
     */
    override fun deleteProcessTemplate(processTemplateId: Int) {
        processTemplatesCache.remove(processTemplateId)

        val existed = ProcessTemplateDAO.deleteProcessTemplate(processTemplateId)
        if (!existed)
            throw NotFoundException("process template not found")
    }
}
