package net.thiki.ec.component.exception;

import kotlin.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssertionExceptionTest4Java {

    @Test
    void testAssertionException() {
        AssertionException th = assertThrows(AssertionException.class, () -> {
//            throw new AssertionException("ex thrown.");
            AssertionExceptionKt.assertThat(false, "ex thrown.");
        });
        assertEquals("ex thrown.", th.getMessage());

    }

    @Test
    void testAssertionExceptionWithParameters(){
        AssertionException th2 = assertThrows(AssertionException.class, () -> {
//            throw new AssertionException("ex thrown.");
            AssertionExceptionKt.badRequestError(1000, "ex thrown with parameters. p1={}.", new Pair<>("k1", "p1Value"));
        });
        assertEquals("ex thrown with parameters. p1={k1=p1Value}.", th2.getMessage());

    }

}
