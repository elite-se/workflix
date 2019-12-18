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
    val description: String,
    val processTemplate: ProcessTemplate?,
    val starter: User,
    val status: ProcessStatus,
    val startedAt: Instant,
    val finishedAt: Instant?,
    val aborted: Boolean,
    // the tasks lies under the id of the corresponding task template
    val tasks: MutableMap<Int, Task>
) {

    /**
     * Create-Constructor
     */
    constructor(title: String, description: String, processTemplateId: Int, starterId: String, simpleClosing: Map<Int, Boolean>, personsResponsibleIds: Map<Int, Set<String>>) :
            this(null, title, description, ProcessTemplateContainer.getProcessTemplate(processTemplateId),
                    UserContainer.getUser(starterId), ProcessStatus.RUNNING, Instant.now(), null, false, HashMap<Int, Task>()) {

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
            tasks.put(id, Task(taskTemplate, simpleClosing.get(taskTemplate.id)!!, startedAt, personsResponsibleIds.get(taskTemplate.id)!!))
        }
    }

    /**
     * Simple-Constructor that does not consider all details.
     */
    constructor(id: Int, title: String, description: String, starter: User, status: ProcessStatus, startedAt: Instant, finishedAt: Instant?, aborted: Boolean, tasks: MutableMap<Int, Task>) :
            this(id, title, description, null, starter, status, startedAt, finishedAt, aborted, tasks)
}
