package unittests

import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskAssignment
import domainmocks.ProcessesMocks
import domainmocks.UsersAndRolesMocks
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TaskTest {

    /**
     * Tests if the correct tasks are blocked and closed step by step.
     */
    @Test
    fun testBlockedClosed() {
        val process = ProcessesMocks.getProcessExtorel()
        val assignee = UsersAndRolesMocks.eliasKeis

        val task1 = process.getTask(1)

        // before assignment to task 1
        assertFalse { task1.isBlocked() }
        for (k in 1 .. 5) assertFalse { process.getTask(k).isClosed() }
        for (k in 2 .. 5) assertTrue { process.getTask(k).isBlocked() }

        val taskAssignment1 = TaskAssignment(task1, assignee, true)
        task1.addTaskAssignment(taskAssignment1)

        // before assignment to task 2 and after assignment to task 1
        assertTrue { task1.isClosed() }
        for (k in 2 .. 4) assertFalse { process.getTask(k).isClosed() }
        for (k in 1 .. 3) assertFalse { process.getTask(k).isBlocked() }
        for (k in 4 .. 5) assertTrue { process.getTask(k).isBlocked() }

        val task2 = process.getTask(2)
        val taskAssignment2 = TaskAssignment(task2, assignee, true)
        task2.addTaskAssignment(taskAssignment2)

        // before assignment to task 3 and after assignment to task 2
        for (k in 1 .. 2) assertTrue { process.getTask(k).isClosed() }
        for (k in 3 .. 5) assertFalse { process.getTask(k).isClosed() }
        for (k in 1 .. 3) assertFalse { process.getTask(k).isBlocked() }
        for (k in 4 .. 5) assertTrue { process.getTask(k).isBlocked() }

        val task3 = process.getTask(3)
        val taskAssignment3 = TaskAssignment(task3, assignee, true)
        task3.addTaskAssignment(taskAssignment3)

        val task5 = process.getTask(5)

        // before assignment to task 4 and after assignment to task 3
        for (k in 1 .. 3) assertTrue { process.getTask(k).isClosed() }
        for (k in 4 .. 5) assertFalse { process.getTask(k).isClosed() }
        for (k in 1 .. 4) assertFalse { process.getTask(k).isBlocked() }
        assertTrue { task5.isBlocked() }

        val task4 = process.getTask(4)
        val taskAssignment4 = TaskAssignment(task4, assignee, true)
        task4.addTaskAssignment(taskAssignment4)

        // before assignment to task 5 and after assignment to task 4
        for (k in 1 .. 4) assertTrue { process.getTask(k).isClosed() }
        assertFalse { task5.isClosed() }
        for (k in 1 .. 5) assertFalse { process.getTask(k).isBlocked() }

        val taskAssignment5 = TaskAssignment(task5, assignee, true)
        task5.addTaskAssignment(taskAssignment5)

        // after assignment to task 5
        for (k in 1 .. 5) {
            assertTrue { process.getTask(k).isClosed() }
            assertFalse { process.getTask(k).isBlocked() }
        }

        assertTrue { process.closeable() }

        process.close()

        assertEquals(ProcessStatus.CLOSED, process.getStatus())
    }

    /**
     * A task must not be closed before the required number of closings was done.
     *
     * Note that a similar principle applies to blocking. A task is blocked until
     * all predecessors are closed by the required number of closings.
     */
    @Test
    fun testMultipleNecessaryClosings() {
        val process = ProcessesMocks.getProcessFugger()
        val task3 = process.getTask(3)

        val task1 = process.getTask(1)
        val taskAssignment1 = TaskAssignment(task1, UsersAndRolesMocks.marvinBrieger, true)
        task1.addTaskAssignment(taskAssignment1)

        assertFalse { task1.isClosed() }
        assertTrue { task3.isBlocked() }

        val taskAssignment2 = TaskAssignment(task1, UsersAndRolesMocks.eliasKeis, true)
        task1.addTaskAssignment(taskAssignment2)

        assertTrue { task1.isClosed() }
        assertTrue {task3.isBlocked() }

        val task2 = process.getTask(2)
        val taskAssignment3 = TaskAssignment(task2, UsersAndRolesMocks.marvinBrieger, true)
        task2.addTaskAssignment(taskAssignment3)

        assertFalse { task2.isClosed() }
        assertTrue { task3.isBlocked() }

        val taskAssignment4 = TaskAssignment(task2, UsersAndRolesMocks.eliasKeis, true)
        task2.addTaskAssignment(taskAssignment4)

        assertTrue { task2.isClosed() }
        assertFalse { task3.isBlocked() }
    }

    /**
     * Adding of an assignment should fail if it was already added.
     */
    @Test
    fun testAssignmentWasAlreadyAdded() {
        val process = ProcessesMocks.getProcessExtorel()
        val task = process.getTask(1)

        val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, false)
        task.addTaskAssignment(taskAssignment)

        assertFails { task.addTaskAssignment(taskAssignment) }
    }

    /**
     * Adding of an additional assignment should fail if the task is already closed.
     */
    @Test
    fun testAddingAssignmentToClosedTask() {
        val process = ProcessesMocks.getProcessExtorel()
        val task = process.getTask(1)

        val taskAssignmentOpen = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, false)
        task.addTaskAssignment(taskAssignmentOpen)

        // adding of additional assignment should not fail if the task is not already closed
        val taskAssignmentImmediate = TaskAssignment(task, UsersAndRolesMocks.marvinBrieger, true)
        assertDoesNotThrow { task.addTaskAssignment(taskAssignmentImmediate) }

        // adding of additional assignment should fail if the task is already closed
        val taskAssignmentFailing = TaskAssignment(task, UsersAndRolesMocks.erikPallas, true)
        assertFails { task.addTaskAssignment(taskAssignmentFailing) }
    }

    /**
     * The deletion of a assignment from a closed task should fail even if the assignment
     * itself is not done.
     */
    @Test
    fun testDeleteOpenAssignmentFromClosedTask() {
        val process = ProcessesMocks.getProcessExtorel()
        val task = process.getTask(1)

        val taskAssignmentOpen = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, false)
        task.addTaskAssignment(taskAssignmentOpen)

        val taskAssignmentImmediate = TaskAssignment(task, UsersAndRolesMocks.marvinBrieger, true)
        task.addTaskAssignment(taskAssignmentImmediate)

        // removing of assignment should fail if the task is already closed
        assertFails { task.deleteTaskAssignment(taskAssignmentOpen.assigneeId) }
    }

    /**
     * The deletion of a closed assignment of a task should fail.
     *
     * Note that it should even fail if the task itself is not closed yet.
     */
    @Test
    fun testDeleteDoneAssignmentFromTask() {
        val process = ProcessesMocks.getProcessFugger()
        val task = process.getTask(1)

        val taskAssignment1 = TaskAssignment(task, UsersAndRolesMocks.marvinBrieger, true)
        task.addTaskAssignment(taskAssignment1)

        // fails when task is not closed
        assertFails { task.deleteTaskAssignment(taskAssignment1.assigneeId) }

        val taskAssignment2 = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, true)
        task.addTaskAssignment(taskAssignment2)

        // fails when task is closed
        assertFails { task.deleteTaskAssignment(taskAssignment1.assigneeId) }
    }

    /**
     * Explicitly closing of a assignment should fail if the task is already closed.
     */
    @Test
    fun testExplicitlyClosing() {
        val process = ProcessesMocks.getProcessExtorel()
        val task1 = process.getTask(1)

        val taskAssignment1 = TaskAssignment(task1, UsersAndRolesMocks.eliasKeis, false)
        task1.addTaskAssignment(taskAssignment1)

        val taskAssignment2 = TaskAssignment(task1, UsersAndRolesMocks.marvinBrieger, false)
        task1.addTaskAssignment(taskAssignment2)

        taskAssignment1.close(Instant.now())

        // closing a assignment more than once should fail
        assertFails { taskAssignment1.close(Instant.now()) }

        // closing another assignment should fail if the task is already closed
        assertFails { taskAssignment2.close(Instant.now()) }
    }

    @Test
    fun testClosedProcessAssignmentDeletion() {
        val process = ProcessesMocks.getSimpleProcess()

        val task1 = process.getTask(1)
        val taskAssignment1 = TaskAssignment(task1, UsersAndRolesMocks.marvinBrieger, false)
        task1.addTaskAssignment(taskAssignment1)
        val taskAssignment2 = TaskAssignment(task1, UsersAndRolesMocks.eliasKeis, true)
        task1.addTaskAssignment(taskAssignment2)

        val task2 = process.getTask(2)
        val taskAssignment3 = TaskAssignment(task2, UsersAndRolesMocks.eliasKeis, true)
        task2.addTaskAssignment(taskAssignment3)

        assertFails { task1.deleteTaskAssignment(taskAssignment1.assigneeId) }
    }

    @Test
    fun testAbortedProcessAssignmentDeletion() {
        val process = ProcessesMocks.getSimpleProcess()

        val task = process.getTask(1)
        val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, false)
        task.addTaskAssignment(taskAssignment)

        process.abort()

        assertFails { task.deleteTaskAssignment(taskAssignment.assigneeId) }
    }

    @Test
    fun testAbortedProcessAddAssignment() {
        val process = ProcessesMocks.getSimpleProcess()

        val task = process.getTask(1)
        val taskAssignment1 = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, false)
        task.addTaskAssignment(taskAssignment1)

        process.abort()

        val taskAssignment2 = TaskAssignment(task, UsersAndRolesMocks.michaelMarkl, false)
        assertFails { task.addTaskAssignment(taskAssignment2) }
    }

    @Test
    fun testCloseOpenAssignmentAfterProcessAbortion() {
        val process = ProcessesMocks.getSimpleProcess()

        val task = process.getTask(1)
        val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, false)
        task.addTaskAssignment(taskAssignment)

        process.abort()

        assertFails { taskAssignment.close(Instant.now()) }
    }


}