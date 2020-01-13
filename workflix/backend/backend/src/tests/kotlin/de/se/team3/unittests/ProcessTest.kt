package de.se.team3.unittests

import de.se.team3.domainmocks.ProcessesMocks
import de.se.team3.domainmocks.UsersAndRolesMocks
import de.se.team3.logic.domain.TaskAssignment
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class ProcessTest {

    /**
     * Process should have progress 0 if there is no task assignment.
     */
    @Test
    fun testGetProgress0() {
        val process = ProcessesMocks.getProcessExtorel()

        assertEquals(0, process.getProgress())
    }

    /**
     * Process should have progress 30 if the two tasks are closed.
     */
    @Test
    fun testGetProgress30() {
        val process = ProcessesMocks.getProcessExtorel()

        val task1 = process.tasks.get(1)!!
        val taskAssignment1 = TaskAssignment(task1, UsersAndRolesMocks.marvinBrieger, true)
        task1.addTaskAssignment(taskAssignment1)

        val task3 = process.tasks.get(3)!!
        val taskAssignment3 = TaskAssignment(task3, UsersAndRolesMocks.eliasKeis, true)
        task3.addTaskAssignment(taskAssignment3)

        assertEquals(30, process.getProgress())
    }

    /**
     * Process should have progress 70 if four tasks are closed.
     */
    @Test
    fun testGetProgress70() {
        val process = ProcessesMocks.getProcessExtorel()

        val task1 = process.tasks.get(1)!!
        val taskAssignment1 = TaskAssignment(task1, UsersAndRolesMocks.michaelMarkl, true)
        task1.addTaskAssignment(taskAssignment1)

        val task2 = process.tasks.get(2)!!
        val taskAssignment2 = TaskAssignment(task2, UsersAndRolesMocks.erikPallas, true)
        task2.addTaskAssignment(taskAssignment2)

        val task3 = process.tasks.get(3)!!
        val taskAssignment3 = TaskAssignment(task3, UsersAndRolesMocks.karlCustomerAdvisor, true)
        task3.addTaskAssignment(taskAssignment3)

        val task4 = process.tasks.get(4)!!
        val taskAssignment4 = TaskAssignment(task4, UsersAndRolesMocks.kunigundeCustomerAdvisor, true)
        task4.addTaskAssignment(taskAssignment4)

        assertEquals(70, process.getProgress())
    }

    /**
     * Process should have progress 100 if all tasks are closed.
     */
    @Test
    fun testGetProgress100() {
        val process = ProcessesMocks.getProcessExtorel()

        for ((id, task) in process.tasks) {
            val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.marvinBrieger, true)
            task.addTaskAssignment(taskAssignment)
        }

        assertEquals(100, process.getProgress())
    }

    /**
     * Process is closeable if all tasks are closed and not closeable on the way.
     */
    @Test
    fun testCloseable() {
        val process = ProcessesMocks.getProcessExtorel()

        for ((id, task) in process.tasks) {
            assertFalse { process.closeable() } // must not be closeable while there are open tasks
            val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, true)
            task.addTaskAssignment(taskAssignment)
        }

        assertTrue { process.closeable() }
    }

    /**
     * Process is not closeable if it was aborted.
     */
    @Test
    fun testCloseableAfterAbortion() {
        val process = ProcessesMocks.getProcessExtorel()

        val task = process.getTask(1)
        val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.marvinBrieger, true)
        task.addTaskAssignment(taskAssignment)

        process.abort()

        assertFalse { process.closeable() }
    }

    /**
     * Closed process should not be abortable or closeable.
     */
    @Test
    fun testAbortionOrClosingAfterClosing() {
        val process = ProcessesMocks.getProcessExtorel()

        for ((id, task) in process.tasks) {
            assertFalse { process.closeable() } // must not be closeable while there are open tasks
            val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, true)
            task.addTaskAssignment(taskAssignment)
        }

        assertFails { process.abort() }

        process.close()

        assertFails { process.abort() }
        assertFails { process.close() }
    }
}
