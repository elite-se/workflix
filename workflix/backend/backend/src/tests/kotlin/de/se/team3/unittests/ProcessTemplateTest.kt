package de.se.team3.unittests

import de.se.team3.domainmocks.ProcessTemplatesMocks
import de.se.team3.domainmocks.UsersAndRolesMocks
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class ProcessTemplateTest {

    @Test
    fun testEstimatedDurationSum() {
        val processTemplate1 = ProcessTemplatesMocks.getProcessTemplate1()
        assertEquals(10, processTemplate1.getEstimatedDurationSum())

        val processTemplate2 = ProcessTemplatesMocks.getProcessTemplate2()
        assertEquals(20, processTemplate2.getEstimatedDurationSum())
    }

    @Test
    fun testUsesUserRole() {
        val processTemplate = ProcessTemplatesMocks.getProcessTemplate1()
        val accountant = UsersAndRolesMocks.getAccountant()
        val investmentManager = UsersAndRolesMocks.getInvestmentManager()
        val unusedUser = UsersAndRolesMocks.getUnusedUserRole()

        assertTrue { processTemplate.usesUserRole(accountant) }
        assertTrue { processTemplate.usesUserRole(investmentManager) }
        assertFalse { processTemplate.usesUserRole(unusedUser) }
    }

    @Test
    fun testIsOneResponsible() {
        val processTemplate = ProcessTemplatesMocks.getProcessTemplate1()

        val responsibles1 = ArrayList<Int>()
        responsibles1.add(1)
        responsibles1.add(2)

        assertTrue { processTemplate.isOneResponsible(responsibles1) }

        val responsibles2 = ArrayList<Int>()
        responsibles2.add(3)
        responsibles2.add(4)

        assertTrue { processTemplate.isOneResponsible(responsibles2) }

        val responsibles3 = ArrayList<Int>()
        responsibles2.add(4)

        assertFalse { processTemplate.isOneResponsible(responsibles3) }
    }
}
