package unittests

import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.UserRole
import de.se.team3.logic.domain.processTemplateUtil.ProcessTemplateCycleDetection
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals

class ProcessTemplateTest {

    @Test
    fun testCycleDetectionUnconnectedTaskTemplates() {
        val accountantRole = TestData.getBuchhalter()

        val taskTemplates = HashMap<Int, TaskTemplate>()
        val taskTemplate1 = TaskTemplate(1, accountantRole, "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2, accountantRole, "TaskTemplate2", "Description", 1, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, accountantRole, "TaskTemplate3", "Description", 1, 1)
        taskTemplates.put(3, taskTemplate3)

        assertEquals(true, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

    @Test
    fun testCycleDetectionAcyclic1() {
        val accountantRole = TestData.getBuchhalter()

        val taskTemplates = HashMap<Int, TaskTemplate>()
        val taskTemplate1 = TaskTemplate(1, accountantRole, "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2, accountantRole, "TaskTemplate2", "Description", 1, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, accountantRole, "TaskTemplate3", "Description", 1, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, accountantRole, "TaskTemplate4", "Description", 1, 1)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5, accountantRole, "TaskTemplate5", "Description", 1, 1)
        taskTemplates.put(5, taskTemplate5)

        taskTemplate2.addPredecessor(taskTemplate1)
        taskTemplate3.addPredecessor(taskTemplate1)
        taskTemplate4.addPredecessor(taskTemplate2)
        taskTemplate4.addPredecessor(taskTemplate3)
        taskTemplate5.addPredecessor(taskTemplate4)

        assertEquals(true, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

    @Test
    fun testCycleDetectionAcyclic2() {
        val accountantRole = TestData.getBuchhalter()

        val taskTemplates = HashMap<Int, TaskTemplate>()
        val taskTemplate1 = TaskTemplate(1, accountantRole, "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2, accountantRole, "TaskTemplate2", "Description", 1, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, accountantRole, "TaskTemplate3", "Description", 1, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, accountantRole, "TaskTemplate4", "Description", 1, 1)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5, accountantRole, "TaskTemplate5", "Description", 1, 1)
        taskTemplates.put(5, taskTemplate5)

        taskTemplate3.addPredecessor(taskTemplate1)
        taskTemplate3.addPredecessor(taskTemplate2)
        taskTemplate4.addPredecessor(taskTemplate3)
        taskTemplate5.addPredecessor(taskTemplate3)

        assertEquals(true, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

    @Test
    fun testCycleDetectionSingleLoop() {
        val accountantRole = TestData.getBuchhalter()

        val taskTemplates = HashMap<Int, TaskTemplate>()
        val taskTemplate1 = TaskTemplate(1, accountantRole, "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)

        taskTemplate1.addPredecessor(taskTemplate1)

        assertEquals(false, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

    @Test
    fun testCycleDetectionGreaterLoop() {
        val accountantRole = TestData.getBuchhalter()

        val taskTemplates = HashMap<Int, TaskTemplate>()
        val taskTemplate1 = TaskTemplate(1, accountantRole, "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2, accountantRole, "TaskTemplate2", "Description", 1, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, accountantRole, "TaskTemplate3", "Description", 1, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, accountantRole, "TaskTemplate4", "Description", 1, 1)
        taskTemplates.put(4, taskTemplate4)

        taskTemplate2.addPredecessor(taskTemplate1)
        taskTemplate3.addPredecessor(taskTemplate2)
        taskTemplate4.addPredecessor(taskTemplate3)
        taskTemplate1.addPredecessor(taskTemplate4)

        assertEquals(false, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

}