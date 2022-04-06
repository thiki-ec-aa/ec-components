package net.thiki.ec.component.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class StringFormatterTest{

    @Test
    fun testFormatWithSimpleValues(){
        assertEquals("aaa is bbb.", StringFormatter.format("aaa is {}.", "bbb"))
        assertEquals("aaa are bbb and ccc.", StringFormatter.format("aaa are {} and {}.", "bbb", "ccc"))
        assertEquals("aaa are bbb, ccc and ddd.", StringFormatter.format("aaa are {}, {} and {}.", "bbb", "ccc", "ddd"))
        assertEquals("aaa are bbb, ccc and ddd.", StringFormatter.arrayFormat("aaa are {}, {} and {}.", arrayOf("bbb", "ccc", "ddd")))
    }
    @Test
    fun testFormatWithCollectionValues(){
        assertEquals("the map is [{key=value}].", StringFormatter.format("the map is [{}].", mapOf("key" to "value")))


        data class Person(val age: Int, val name: String)
        assertEquals("the object is [Person(age=10, name=zhangsan)].", StringFormatter.format("the object is [{}].", Person(10, "zhangsan")))

        //warning: array is treated as multiple parameters instead of one parameter with array type.
        assertEquals("the object array is [Person(age=10, name=zhangsan)] and [Person(age=15, name=lisi)].", StringFormatter.format("the object array is [{}] and [{}].",
            Person(10, "zhangsan"),
            Person(15, "lisi")
        ))
    }

    /**
     *
     */
    @Test
    fun testSingleValueArray(){
        val m =  mutableMapOf<String, String>()
        m.putAll(arrayOf(Pair("k1", "p1Value")));
        assertEquals("p1=[p1Value].",StringFormatter.format("p1={}.", m.values))
        assertNotEquals("p1=p1Value.",StringFormatter.format("p1={}.", m.values))

        assertEquals("p1=p1Value.",StringFormatter.arrayFormat("p1={}.", m.values.toTypedArray()))

        assertEquals("p1=p1Value.",StringFormatter.format("p1={}.", "p1Value"))

    }


}