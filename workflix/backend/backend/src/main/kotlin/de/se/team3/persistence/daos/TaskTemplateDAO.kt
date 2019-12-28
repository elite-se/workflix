package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.TaskTemplateDAOInterface
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.persistence.meta.TaskTemplatesTable
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.iterator
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where

object TaskTemplateDAO : TaskTemplateDAOInterface {

    /**
     * Makes a single task template from the given row.
     */
    fun makeTaskTemplate(row: QueryRowSet): TaskTemplate {
        return TaskTemplate(
            row[TaskTemplatesTable.id]!!,
            row[TaskTemplatesTable.name]!!,
            row[TaskTemplatesTable.description]!!,
            row[TaskTemplatesTable.estimatedDuration],
            row[TaskTemplatesTable.durationLimit],
            row[TaskTemplatesTable.necessaryClosings]!!
        )
    }

    /**
     * Returns the single task template specified by the given task template id.
     */
    override fun getTaskTemplate(taskTemplateId: Int): TaskTemplate {
        val result = TaskTemplatesTable.select()
            .where { TaskTemplatesTable.id eq taskTemplateId }

        val row = result.rowSet.iterator().next()

        return makeTaskTemplate(row)
    }
}
