package net.thiki.ec.component.exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Disabled("todo")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AssertionExceptionTest{

    @Test
    @Disabled("todo")
    fun testAssertionExceptionWithParameters() {
        val th2 = assertThrows(
            AssertionException::class.java
        ) {
//            throw new AssertionException("ex thrown.");
            badRequestError(
                1000,
                "ex thrown with parameters. p1={}.",
                Pair("k1", "p1Value")
            )
        }
        assertEquals("ex thrown with parameters. p1={p1Value}.", th2.message)
    }

    @Test
    fun testConstructors() {
        val ae1 = AssertionException(100, "djfsdjk", mutableMapOf(
            "ajdfkjs" to "fjadksfj"
        ))
        assertEquals("500", ae1.systemParams[AssertionException.SystemParams_Key_HttpStatus])


        val ae2 = AssertionException(100, "djfsdjk")
        assertEquals("500", ae2.systemParams[AssertionException.SystemParams_Key_HttpStatus])
        assertEquals(0, ae2.bizParams.size)

    }
}