package unittests

import dbmocks.ProcessTemplatesMock
import de.se.team3.logic.domain.TaskAssignment
import domainmocks.ProcessTemplatesMocks
import domainmocks.ProcessesMocks
import domainmocks.UsersAndRolesMocks
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProcessTest {

    @Test
    fun testGetProgress0() {
        val process = ProcessesMocks.getProcess1()

        assertEquals(0, process.getProgress())
    }

    @Test
    fun testGetProgress30() {
        val process = ProcessesMocks.getProcess1()

        val task1 = process.tasks.get(1)!!
        val taskAssignment1 = TaskAssignment(task1, UsersAndRolesMocks.marvinBrieger, true)
        task1.addTaskAssignment(taskAssignment1)

        val task3 = process.tasks.get(3)!!
        val taskAssignment3 = TaskAssignment(task3, UsersAndRolesMocks.eliasKeis, true)
        task3.addTaskAssignment(taskAssignment3)

        assertEquals(30, process.getProgress())
    }

    @Test
    fun testGetProgress70() {
        val process = ProcessesMocks.getProcess1()

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

    @Test
    fun testGetProgress100() {
        val process = ProcessesMocks.getProcess1()

        for ((id, task) in process.tasks) {
            val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.marvinBrieger, true)
            task.addTaskAssignment(taskAssignment)
        }

        assertEquals(100, process.getProgress())
    }

    @Test
    fun testCloseableFalse() {
        val process = ProcessesMocks.getProcess1()

        for ((id, task) in process.tasks) {
            assertFalse { process.closeable() }
            val taskAssignment = TaskAssignment(task, UsersAndRolesMocks.eliasKeis, true)
            task.addTaskAssignment(taskAssignment)
        }

        assertTrue { process.closeable() }
    }

}