package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.ProcessTemplateDAOInterface
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.ProcessTemplates
import de.se.team3.persistence.meta.TaskTemplateRelationships
import de.se.team3.persistence.meta.TaskTemplates
import de.se.team3.persistence.meta.Users
import me.liuwj.ktorm.dsl.*

object ProcessTemplateDAO: ProcessTemplateDAOInterface {

    override fun getAllProcessTemplates(offset: Int, limit: Int): Pair<List<ProcessTemplate>, Int> {
        val processTemplates = ArrayList<ProcessTemplate>()
        val result = ProcessTemplates
            .innerJoin(Users, on = Users.ID eq ProcessTemplates.ownerID)
            .select()
            .limit(offset, limit)

        for (row in result) {
            val owner = User(row[Users.ID]!!, row[Users.name]!!, row[Users.displayname]!!, row[Users.email]!!)
            val template = ProcessTemplate(row[ProcessTemplates.ID]!!, row[ProcessTemplates.title]!!, row[ProcessTemplates.durationLimit]!!, owner)
            processTemplates.add(template)
        }

        return Pair(processTemplates.toList(), 0)
    }

    /**
     * Returns the ids of the successors of the given task.
     */
    private fun getSuccessorList(taskId: Int): List<Int> {
        val successorsResult = TaskTemplateRelationships
            .select().where { TaskTemplateRelationships.predecessor eq  taskId }

        val successors = ArrayList<Int>()
        for (row in successorsResult)
            successors.add(row[TaskTemplateRelationships.successor]!!)

        return successors
    }

    /**
     * Returns the ids of the predecessor of the given task.
     */
    private fun getPredecessorList(taskId: Int): List<Int> {
        val predecessorsResult = TaskTemplateRelationships
            .select().where { TaskTemplateRelationships.successor eq taskId }

        val predecessors = ArrayList<Int>()
        for (row in predecessorsResult)
            predecessors.add(row[TaskTemplateRelationships.predecessor]!!)

        return predecessors
    }

    /**
     * Returns the task templates for a given process template as a set.
     */
    private  fun getTaskTemplates(processTemplateId: Int): Set<TaskTemplate> {
        val taskTemplatesResult = TaskTemplates
            .select().where { TaskTemplates.templateID eq processTemplateId }

        val taskTemplatesMap = HashMap<Int, TaskTemplate>()
        val successorsPerTask = HashMap<Int, List<Int>>()
        val predecessorPerTask = HashMap<Int, List<Int>>()

        // Collect all task templates and successor/predecessor relationships
        for (row in taskTemplatesResult) {
            val taskId = row[TaskTemplates.ID]!!

            taskTemplatesMap.put(taskId,
                TaskTemplate(taskId, row[TaskTemplates.name]!!, row[TaskTemplates.estimatedDuration]!!, row[TaskTemplates.durationLimit]!!))

            successorsPerTask.put(taskId, getSuccessorList(taskId))
            predecessorPerTask.put(taskId, getPredecessorList(taskId))
        }

        // Add the collected relationships to the task templates
        for (entry in taskTemplatesMap) {
            val taskTemplate = entry.value
            taskTemplate.successors = HashSet<TaskTemplate>()
            taskTemplate.predeccessors = HashSet<TaskTemplate>()

            for (successor in successorsPerTask.get(taskTemplate.id)!!) {
                taskTemplate.successors?.add(taskTemplatesMap.get(successor)!!)
            }
            for (predecessor in predecessorPerTask.get(taskTemplate.id)!!)
                taskTemplate.predeccessors?.add(taskTemplatesMap.get(predecessor)!!)
        }

        return taskTemplatesMap.toList().map { it.second }.toHashSet()
    }

    override fun getProcessTemplate(templateId: Int): ProcessTemplate {
        val processTemplateResult = ProcessTemplates
            .innerJoin(Users, on = Users.ID eq ProcessTemplates.ownerID)
            .select().where { ProcessTemplates.ID eq templateId }

        val row = processTemplateResult.rowSet.iterator().next()

        val owner = User(row[Users.ID]!!, row[Users.name]!!, row[Users.displayname]!!, row[Users.email]!!)
        val processId = row[ProcessTemplates.ID]!!
        val processTemplate = ProcessTemplate(processId, row[ProcessTemplates.title]!!,
            row[ProcessTemplates.durationLimit]!!, owner,
            getTaskTemplates(processId))

        return processTemplate
    }

}