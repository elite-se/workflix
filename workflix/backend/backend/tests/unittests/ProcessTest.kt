package unittests

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ProcessTest {

    @Test
    fun testGetProgress0() {
        val process = TestData.getProcess1()

        assertEquals(0, process.getProgress())
    }

}