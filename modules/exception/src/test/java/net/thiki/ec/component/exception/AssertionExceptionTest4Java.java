package net.thiki.ec.component.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssertionExceptionTest4Java {

    @Test
    void testAssertionException(){
        AssertionException th = assertThrows(AssertionException.class, () -> {
//            throw new AssertionException("ex thrown.");
            AssertionExceptionKt.assertThat(false, "ex thrown.");
        });
        assertEquals("ex thrown.", th.getMessage());

    }

}
