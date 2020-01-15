package de.se.team3.logic.domain.mocks

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.domain.TaskComment
import java.time.Instant

object ProcessesMocks {

    /**
     * Process based on process template 1.
     *
     * Each task template has 1 necessary closings.
     */
    fun getProcessExtorel(): Process {
        val groupExtorel =
            ProcessGroupsMocks.getTestProcessGroupExtorel()
        val starter = UsersAndRolesMocks.karlCustomerAdvisor
        val processTemplate =
            ProcessTemplatesMocks.getProcessTemplate1()

        // generate tasks without ids from task template like in constructor of process
        val tasksWithoutIds = Process.createTasks(processTemplate)
        // enrich them with ids
        val tasksWithIds = HashMap<Int, Task>()
        for ((key, task) in tasksWithoutIds)
            tasksWithIds.put(key, Task(key, key, task.startedAt, ArrayList<TaskComment>(), ArrayList<TaskAssignment>(), null))

        return Process(1, starter, groupExtorel, processTemplate, "Testprocess 1", "Description", ProcessStatus.RUNNING, Instant.now(), Instant.now(), tasksWithIds)
    }

    /**
     * Process based on process template 2.
     *
     * Each task template has 2 necessary closings.
     */
    fun getProcessFugger(): Process {
        val groupFugger =
            ProcessGroupsMocks.getTestProcessGroupFugger()
        val starter = UsersAndRolesMocks.kunigundeCustomerAdvisor
        val processTemplate =
            ProcessTemplatesMocks.getProcessTemplate2()

        // generate tasks without ids from task template like in constructor of process
        val tasksWithoutIds = Process.createTasks(processTemplate)
        // enrich them with ids
        val tasksWithIds = HashMap<Int, Task>()
        for ((key, task) in tasksWithoutIds)
            tasksWithIds.put(key, Task(key, key, task.startedAt, ArrayList<TaskComment>(), ArrayList<TaskAssignment>(), null))

        return Process(2, starter, groupFugger, processTemplate, "Testprocess 2", "Description", ProcessStatus.RUNNING, Instant.now(), Instant.now(), tasksWithIds)
    }

    /**
     * Process based on process template 3.
     *
     * Each task template has 1 necessary closings.
     */
    fun getSimpleProcess(): Process {
        val groupFugger =
            ProcessGroupsMocks.getTestProcessGroupFugger()
        val starter = UsersAndRolesMocks.kunigundeCustomerAdvisor
        val processTemplate =
            ProcessTemplatesMocks.getProcessTemplate3()

        // generate tasks without ids from task template like in constructor of process
        val tasksWithoutIds = Process.createTasks(processTemplate)
        // enrich them with ids
        val tasksWithIds = HashMap<Int, Task>()
        for ((key, task) in tasksWithoutIds)
            tasksWithIds.put(key, Task(key, key, task.startedAt, ArrayList<TaskComment>(), ArrayList<TaskAssignment>(), null))

        return Process(3, starter, groupFugger, processTemplate, "Testprocess 3", "Description", ProcessStatus.RUNNING, Instant.now(), Instant.now(), tasksWithIds)
    }
}
