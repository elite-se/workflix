package dbmocks

import de.se.team3.logic.DAOInterfaces.ProcessTemplateDAOInterface
import de.se.team3.logic.domain.ProcessTemplate

object ProcessTemplatesMock: ProcessTemplateDAOInterface {
    override fun getAllProcessTemplates(): List<ProcessTemplate> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProcessTemplate(processTemplateId: Int): ProcessTemplate? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createProcessTemplate(processTemplate: ProcessTemplate): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateProcessTemplate(processTemplate: ProcessTemplate): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteProcessTemplate(processTemplateId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}