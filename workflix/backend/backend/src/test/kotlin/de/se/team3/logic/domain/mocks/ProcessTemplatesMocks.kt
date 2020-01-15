package de.se.team3.logic.domain.mocks

import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.logic.domain.mocks.UsersAndRolesMocks.getAccountant
import de.se.team3.logic.domain.mocks.UsersAndRolesMocks.getCustomerAdvisor
import de.se.team3.logic.domain.mocks.UsersAndRolesMocks.getInvestmentManager
import de.se.team3.logic.domain.mocks.UsersAndRolesMocks.karlCustomerAdvisor
import de.se.team3.logic.domain.mocks.UsersAndRolesMocks.kunigundeCustomerAdvisor
import java.time.Instant

object ProcessTemplatesMocks {

    fun getTaskTemplatesSet1(): Map<Int, TaskTemplate> {
        val taskTemplates = HashMap<Int, TaskTemplate>()

        val taskTemplate1 = TaskTemplate(1,
            getCustomerAdvisor(), "TaskTemplate1", "Description", 1.0, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2,
            getInvestmentManager(), "TaskTemplate2", "Description", 1.0, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, getAccountant(), "TaskTemplate3", "Description", 2.0, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, getAccountant(), "TaskTemplate4", "Description", 3.0, 1)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5,
            getCustomerAdvisor(), "TaskTemplate5", "Description", 3.0, 1)
        taskTemplates.put(5, taskTemplate5)

        taskTemplate2.addPredecessor(taskTemplate1)
        taskTemplate3.addPredecessor(taskTemplate1)
        taskTemplate4.addPredecessor(taskTemplate2)
        taskTemplate4.addPredecessor(taskTemplate3)
        taskTemplate5.addPredecessor(taskTemplate4)

        return taskTemplates
    }

    fun getTaskTemplatesSet2(): Map<Int, TaskTemplate> {
        val taskTemplates = HashMap<Int, TaskTemplate>()

        val taskTemplate1 = TaskTemplate(1,
            getCustomerAdvisor(), "TaskTemplate1", "Description", 3.0, 2)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2,
            getInvestmentManager(), "TaskTemplate2", "Description", 4.0, 2)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3,
            getInvestmentManager(), "TaskTemplate3", "Description", 2.0, 2)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, getAccountant(), "TaskTemplate4", "Description", 6.0, 2)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5,
            getCustomerAdvisor(), "TaskTemplate5", "Description", 5.0, 2)
        taskTemplates.put(5, taskTemplate5)

        taskTemplate3.addPredecessor(taskTemplate1)
        taskTemplate3.addPredecessor(taskTemplate2)
        taskTemplate4.addPredecessor(taskTemplate3)
        taskTemplate5.addPredecessor(taskTemplate3)

        return taskTemplates
    }

    fun getTaskTemplatesSet3(): Map<Int, TaskTemplate> {
        val taskTemplates = HashMap<Int, TaskTemplate>()

        val taskTemplate1 = TaskTemplate(1,
            getCustomerAdvisor(), "TaskTemplate1", "Description", 3.0, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2,
            getInvestmentManager(), "TaskTemplate2", "Description", 4.0, 1)
        taskTemplates.put(2, taskTemplate2)

        taskTemplate2.addPredecessor(taskTemplate1)

        return taskTemplates
    }

    fun getProcessTemplate1(): ProcessTemplate {
        return ProcessTemplate(
            1,
            "Testprocess 1",
            "Description",
            100.0,
            karlCustomerAdvisor,
            Instant.now(),
            null,
            0,
            0,
            false,
            getTaskTemplatesSet1()
        )
    }

    fun getProcessTemplate2(): ProcessTemplate {
        return ProcessTemplate(
            2,
            "Testprocess 2",
            "Description",
            100.0,
            kunigundeCustomerAdvisor,
            Instant.now(),
            null,
            0,
            0,
            false,
            getTaskTemplatesSet2()
        )
    }

    fun getProcessTemplate3(): ProcessTemplate {
        return ProcessTemplate(
            3,
            "Testprocess 3",
            "Description",
            100.0,
            kunigundeCustomerAdvisor,
            Instant.now(),
            null,
            0,
            0,
            false,
            getTaskTemplatesSet3()
        )
    }
}
