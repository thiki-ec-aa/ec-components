package net.thiki.ec.component.code.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.reflect.jvm.javaField

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EntityWrapperProxyGeneratorTest{


    /**
     * SampleCode in kt:
     */
    class PersonDo(

    )

    val javaCode =
"""class PersonDo extends Person{

    private final Person person;

    public PersonDo(Person person){
        this.person = person;
    }

    /* delegate codes begin */
    @Override
    public String getFirstName() {
        return this.person.getFirstName();
    }

    @Override
    public String getLastName() {
        return this.person.getLastName();
    }

    @Override
    public int getAge() {
        return this.person.getAge();
    }

    @Override
    public void setFirstName(String firstName) {
        this.person.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        this.person.setLastName(lastName);
    }

    @Override
    public void setAge(int age) {
        this.person.setAge(age);
    }

    /* delegate codes end */

    /* TODO: write your business logic specified to your application.  */

}""".trimIndent()

    @Test
    @Disabled("not implemented")
    fun testEntityProxyGenerator(){
        println(javaCode);
        val cut = EntityWrapperProxyGenerator()
        val generatedJavaCode = cut.generateJavaCode(EntityClassMeta(Person::class.java))

        assertEquals(
            javaCode.replace("\r\n", "\n"),
            generatedJavaCode.replace("\r\n", "\n")
        )
    }

    class Person1{
        val aaaaaaa: String = ""


    }
    @Test
    fun testString(){
        assertEquals('j', "jdfj"[0])
        assertEquals("j", "jdfj"[0].toString())

    }

    @Test
    fun testToDataModel(){
        val cut = EntityClassMeta(Person::class.java)
        val members = Person::class.members
        val jot  = Person::class.javaObjectType
        val fs = jot.fields
        val ms = jot.methods
        val cs = jot.constructors
//        println(cut.toDataModel());
        Person1::aaaaaaa.javaField
    }

    @Test
    fun testStringBuffer(){
        val cut = StringBuffer();
        cut.append(3);
        cut.append(",hello world")
        assertEquals("3,hello world", cut.toString())
    }
}