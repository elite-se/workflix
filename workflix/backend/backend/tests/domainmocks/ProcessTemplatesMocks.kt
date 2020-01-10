package domainmocks

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.ProcessStatus
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.logic.domain.TaskComment
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.UserRole
import domainmocks.ProcessGroupsMocks.getTestProcessGroupExtorel
import domainmocks.UsersAndRolesMocks.eliasKeis
import domainmocks.UsersAndRolesMocks.erikPallas
import domainmocks.UsersAndRolesMocks.getAccountant
import domainmocks.UsersAndRolesMocks.getCustomerAdvisor
import domainmocks.UsersAndRolesMocks.getInvestmentManager
import domainmocks.UsersAndRolesMocks.karlCustomerAdvisor
import domainmocks.UsersAndRolesMocks.kunigundeCustomerAdvisor
import domainmocks.UsersAndRolesMocks.marvinBrieger
import domainmocks.UsersAndRolesMocks.michaelMarkl
import java.time.Instant

object ProcessTemplatesMocks {

    fun getTaskTemplatesSet1(): Map<Int, TaskTemplate> {
        val taskTemplates = HashMap<Int, TaskTemplate>()

        val taskTemplate1 = TaskTemplate(1,
            getCustomerAdvisor(), "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2,
            getInvestmentManager(), "TaskTemplate2", "Description", 1, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, getAccountant(), "TaskTemplate3", "Description", 2, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, getAccountant(), "TaskTemplate4", "Description", 3, 1)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5,
            getCustomerAdvisor(), "TaskTemplate5", "Description", 3, 1)
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
            getCustomerAdvisor(), "TaskTemplate1", "Description", 3, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2,
            getInvestmentManager(), "TaskTemplate2", "Description", 4, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3,
            getInvestmentManager(), "TaskTemplate3", "Description", 2, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, getAccountant(), "TaskTemplate4", "Description", 6, 1)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5,
            getCustomerAdvisor(), "TaskTemplate5", "Description", 5, 1)
        taskTemplates.put(5, taskTemplate5)

        taskTemplate3.addPredecessor(taskTemplate1)
        taskTemplate3.addPredecessor(taskTemplate2)
        taskTemplate4.addPredecessor(taskTemplate3)
        taskTemplate5.addPredecessor(taskTemplate3)

        return taskTemplates
    }

    fun getProcessTemplate1(): ProcessTemplate {
        return ProcessTemplate(
            1,
            "Testprocess 1",
            "Description",
            100,
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
            1,
            "Testprocess 1",
            "Description",
            100,
            kunigundeCustomerAdvisor,
            Instant.now(),
            null,
            0,
            0,
            false,
            getTaskTemplatesSet2()
        )
    }

}