package net.thiki.ec.component.exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class AssertionExceptionTest{
    @Test
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
}