package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.TasksDAOInterface
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.meta.TasksTable
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where

object TasksDAO : TasksDAOInterface {

    /**
     * Returns the id of the process the given task belongs to.
     */
    override fun getProcessIdForTask(taskId: Int): Int {
        val result = TasksTable.select().where { TasksTable.id eq taskId }

        val iterator = result.rowSet.iterator()
        if (!iterator.hasNext())
            throw NotFoundException("task was not found")

        val row = iterator.next()
        return row[TasksTable.processId]!!
    }
}
