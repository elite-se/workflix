package domainmocks

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.domain.TaskComment
import java.time.Instant

object ProcessesMocks {

    fun getProcess1(): Process {
        val groupExtorel = ProcessGroupsMocks.getTestProcessGroupExtorel()
        val starter = UsersAndRolesMocks.karlCustomerAdvisor
        val processTemplate = ProcessTemplatesMocks.getProcessTemplate1()

        // generate tasks without ids from task template like in constructor of process
        val tasksWithoutIds = Process.createTasks(processTemplate)
        // enrich them with ids
        val tasksWithIds = HashMap<Int, Task>()
        for ((key, task) in tasksWithoutIds)
            tasksWithIds.put(key, Task(key, key, task.startedAt, ArrayList<TaskComment>(), ArrayList<TaskAssignment>(), null))

        return Process(1, starter, groupExtorel, processTemplate, "Testprocess 1", "Description", ProcessStatus.RUNNING, Instant.now(), Instant.now(), tasksWithIds)
    }

}