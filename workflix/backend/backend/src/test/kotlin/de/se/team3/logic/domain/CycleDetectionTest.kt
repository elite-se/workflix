package de.se.team3.logic.domain

import de.se.team3.logic.domain.mocks.ProcessTemplatesMocks
import de.se.team3.logic.domain.mocks.UsersAndRolesMocks
import de.se.team3.logic.domain.processTemplateUtil.ProcessTemplateCycleDetection
import kotlin.test.assertEquals
import org.junit.Test

class CycleDetectionTest {

    @Test
    fun testCycleDetectionUnconnectedTaskTemplates() {
        val accountantRole = UsersAndRolesMocks.getAccountant()

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
        val taskTemplates = ProcessTemplatesMocks.getTaskTemplatesSet1()

        assertEquals(true, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

    @Test
    fun testCycleDetectionAcyclic2() {
        val taskTemplates = ProcessTemplatesMocks.getTaskTemplatesSet2()

        assertEquals(true, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

    @Test
    fun testCycleDetectionSingleLoop() {
        val accountantRole = UsersAndRolesMocks.getAccountant()

        val taskTemplates = HashMap<Int, TaskTemplate>()
        val taskTemplate1 = TaskTemplate(1, accountantRole, "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)

        taskTemplate1.addPredecessor(taskTemplate1)

        assertEquals(false, ProcessTemplateCycleDetection.isAcyclic(taskTemplates))
    }

    @Test
    fun testCycleDetectionGreaterLoop() {
        val accountantRole = UsersAndRolesMocks.getAccountant()

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
