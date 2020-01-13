package de.se.team3.domainmocks

import de.se.team3.logic.domain.User
import de.se.team3.logic.domain.UserRole
import java.time.Instant

object UsersAndRolesMocks {

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
}
