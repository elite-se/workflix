package dbmocks

import de.se.team3.logic.DAOInterfaces.TasksDAOInterface

object TasksMock: TasksDAOInterface {
    override fun getProcessIdForTask(taskId: Int): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}