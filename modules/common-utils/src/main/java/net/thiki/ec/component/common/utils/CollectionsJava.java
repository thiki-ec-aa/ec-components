package net.thiki.ec.component.common.utils;

import java.util.HashSet;
import java.util.Set;

public class CollectionsJava {

    /**
     * wrap the setOf for java code.
     */
   public static <T> Set<T> setOf(T... elements) {
        Set<T> s = new HashSet<T>();
        for (T element : elements) {
            s.add(element);
        }
        return s;
    }

}
