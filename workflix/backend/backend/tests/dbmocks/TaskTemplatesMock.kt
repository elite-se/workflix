package dbmocks

import de.se.team3.logic.DAOInterfaces.TaskTemplateDAOInterface
import de.se.team3.logic.domain.TaskTemplate

object TaskTemplatesMock: TaskTemplateDAOInterface {
    override fun getTaskTemplate(taskTemplateId: Int): TaskTemplate {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}