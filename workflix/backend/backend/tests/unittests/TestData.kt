package unittests

import de.se.team3.logic.domain.Process
import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.ProcessTemplate
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskTemplate
import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.UserRole
import java.time.Instant

object TestData {

    val marvinBrieger = User("58c120552c94decf6cf3b700", "Marvin Brieger", "MB", "mail@marvinbrieger.de", "Passwort123", Instant.now())
    val eliasKeis = User("58c120552c94decf6cf3b701", "Elias Keis", "EK", "ek@gmx.net", "Passwort123", Instant.now())
    val michaelMarkl = User("58c120552c94decf6cf3b702", "Michael Markl", "MK", "mk@gmail.com", "Passwort123", Instant.now())
    val erikPallas = User("58c120552c94decf6cf3b703", "Erik Pallas", "EP", "ep@web.de", "Passwort123", Instant.now())
    val karlCustomerAdvisor = User("58c120552c94decf6cf3b704", "Karl Customer Advisor", "KA", "ka@mfo.de", "Passwort123", Instant.now())
    val kunigundeCustomerAdvisor = User("58c120552c94decf6cf3b705", "Kunigunde Customer Advisor", "KuA", "kua@mfo.de", "Passwort123", Instant.now())
    val unusedUser = User("58c120552c94decf6cf3b706", "Ursula Unused", "UU", "uu@notrelevant.com", "Passwort123", Instant.now())


    fun getSomeUsers(): List<User> {
        val users = ArrayList<User>()

        users.add(marvinBrieger)
        users.add(eliasKeis)
        users.add(michaelMarkl)

        return users
    }

    fun getAccountant(): UserRole {
        val accountants = ArrayList<User>()
        accountants.add(marvinBrieger)
        accountants.add(eliasKeis)
        accountants.add(michaelMarkl)

        val accountantRole = UserRole(1, "Accountant", "Role for accountants", Instant.now(), false, accountants)
        return accountantRole
    }

    fun getCustomerAdvisor(): UserRole {
        val customerAdvisors = ArrayList<User>()
        customerAdvisors.add(karlCustomerAdvisor)
        customerAdvisors.add(kunigundeCustomerAdvisor)

        val customerAdvisorRole = UserRole(2, "Customer Advisor", "Role for customer advisors", Instant.now(), false, customerAdvisors)
        return customerAdvisorRole
    }

    fun getInvestmentManager(): UserRole {
        val investmentManagers = ArrayList<User>()
        investmentManagers.add(erikPallas)

        val investementManagerRole = UserRole(3, "Invenstment Manager", "Role for investment managers", Instant.now(), false, investmentManagers)
        return investementManagerRole
    }

    fun getUnusedUserRole(): UserRole {
        val usuedUsers = ArrayList<User>()
        usuedUsers.add(unusedUser)

        val unusedRole = UserRole(4, "Unused", "Unused Role", Instant.now(), false, usuedUsers)
        return unusedRole
    }

    fun getTaskTemplatesSet1(): Map<Int, TaskTemplate> {
        val taskTemplates = HashMap<Int, TaskTemplate>()

        val taskTemplate1 = TaskTemplate(1, getCustomerAdvisor(), "TaskTemplate1", "Description", 1, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2, getInvestmentManager(), "TaskTemplate2", "Description", 1, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, getAccountant(), "TaskTemplate3", "Description", 2, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, getAccountant(), "TaskTemplate4", "Description", 3, 1)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5, getCustomerAdvisor(), "TaskTemplate5", "Description", 3, 1)
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

        val taskTemplate1 = TaskTemplate(1, getCustomerAdvisor(), "TaskTemplate1", "Description", 3, 1)
        taskTemplates.put(1, taskTemplate1)
        val taskTemplate2 = TaskTemplate(2, getInvestmentManager(), "TaskTemplate2", "Description", 4, 1)
        taskTemplates.put(2, taskTemplate2)
        val taskTemplate3 = TaskTemplate(3, getInvestmentManager(), "TaskTemplate3", "Description", 2, 1)
        taskTemplates.put(3, taskTemplate3)
        val taskTemplate4 = TaskTemplate(4, getAccountant(), "TaskTemplate4", "Description", 6, 1)
        taskTemplates.put(4, taskTemplate4)
        val taskTemplate5 = TaskTemplate(5, getCustomerAdvisor(), "TaskTemplate5", "Description", 5, 1)
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

    fun getTestProcessGroupExtorel(): ProcessGroup {
        val members = ArrayList<User>()
        members.add(marvinBrieger)
        members.add(michaelMarkl)
        members.add(erikPallas)
        members.add(karlCustomerAdvisor)

        return ProcessGroup(1, karlCustomerAdvisor, "Extorel", "Group Extorel", Instant.now(), false, members)
    }

    fun getTestProcessGroupFugger(): ProcessGroup {
        val members = ArrayList<User>()
        members.add(eliasKeis)
        members.add(michaelMarkl)
        members.add(erikPallas)
        members.add(kunigundeCustomerAdvisor)

        return ProcessGroup(2, kunigundeCustomerAdvisor, "Fugger", "Group Fugger", Instant.now(), false, members)
    }

    fun getProcess1(): Process {
        val groupExtorel = getTestProcessGroupExtorel()
        val starter = karlCustomerAdvisor
        val processTemplate = getProcessTemplate1()

        return Process(starter, groupExtorel, processTemplate, "Testprocess 1", "Desription", Instant.now())
    }

}