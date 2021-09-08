package net.thiki.ec.component.common.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionsTest {

    @Test
    void testJointToString(){
        Set s = new HashSet<String>();
        s.add("aa");
        s.add("bb");
        s.add("cc");
        String joined = Collections.joinToString(s, "`, `", "`", "`", -1, "...", t -> t.toString());
        assertEquals("`aa`, `bb`, `cc`", joined);
        String s1 = Collections.joinToString(s, ",");
    }

}
