package de.se.team3.logic.domain.mocks

import de.se.team3.logic.domain.ProcessGroup
import de.se.team3.logic.domain.User
import java.time.Instant

object ProcessGroupsMocks {

    fun getTestProcessGroupExtorel(): ProcessGroup {
        val members = ArrayList<User>()
        members.add(UsersAndRolesMocks.marvinBrieger)
        members.add(UsersAndRolesMocks.michaelMarkl)
        members.add(UsersAndRolesMocks.erikPallas)
        members.add(UsersAndRolesMocks.karlCustomerAdvisor)

        return ProcessGroup(1,
            UsersAndRolesMocks.karlCustomerAdvisor, "Extorel", "Group Extorel", Instant.now(), false, members)
    }

    fun getTestProcessGroupFugger(): ProcessGroup {
        val members = ArrayList<User>()
        members.add(UsersAndRolesMocks.eliasKeis)
        members.add(UsersAndRolesMocks.michaelMarkl)
        members.add(UsersAndRolesMocks.erikPallas)
        members.add(UsersAndRolesMocks.kunigundeCustomerAdvisor)

        return ProcessGroup(2,
            UsersAndRolesMocks.kunigundeCustomerAdvisor, "Fugger", "Group Fugger", Instant.now(), false, members)
    }
}
