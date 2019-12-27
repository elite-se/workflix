package de.se.team3.logic.domain

import de.se.team3.logic.container.ProcessTemplateContainer
import de.se.team3.logic.container.UserContainer
import java.lang.IllegalArgumentException
import java.time.Instant

/**
 * Represents a process.
 */
class Process(
    val id: Int?,
    val title: String,
    val processTemplate: ProcessTemplate?,
    val starter: User,
    val status: ProcessStatus,
    val startedAt: Instant,
    val tasks: MutableList<Task>,
    val group: ProcessGroup
) {

    /**
     * Create-Constructor
     */
    constructor(title: String, processTemplateId: Int, starterId: String, simpleClosing: Map<Int, Boolean>, personsResponsibleIds: Map<Int, Set<String>>, group: ProcessGroup) :
            this(null, title, ProcessTemplateContainer.getProcessTemplate(processTemplateId),
                    UserContainer.getUser(starterId), ProcessStatus.RUNNING, Instant.now(), ArrayList<Task>(), group) {

        if (title.isEmpty())
            throw IllegalArgumentException("title must not be empty")
        if (processTemplate?.taskTemplates == null)
            throw NullPointerException()

        val taskTemplates = processTemplate.taskTemplates
        for ((id, taskTemplate) in taskTemplates) {
            if (!simpleClosing.containsKey(id))
                throw IllegalArgumentException("simple closing property mut exist for each task")

            if (!personsResponsibleIds.containsKey(taskTemplate.id)) {
                throw IllegalArgumentException("persons responsible property must exist for each task")
            } else {
                val responsiblesForCurrentTask = personsResponsibleIds.get(id)
                if (responsiblesForCurrentTask == null || responsiblesForCurrentTask.isEmpty())
                    throw IllegalArgumentException("there must be at least on resposible person for each task")
            }

            val startedAt = if (taskTemplate.predecessors.size == 0) Instant.now() else null
            tasks.add(Task(taskTemplate, simpleClosing.get(taskTemplate.id)!!, startedAt, personsResponsibleIds.get(taskTemplate.id)!!))
        }

        group.processes.add(this)
    }

    /**
     * Simple-Constructor that does not consider all details.
     */
    constructor(id: Int, title: String, starter: User, status: ProcessStatus, startedAt: Instant, tasks: MutableList<Task>, group: ProcessGroup) :
            this(id, title, null, starter, status, startedAt, tasks, group)
}
