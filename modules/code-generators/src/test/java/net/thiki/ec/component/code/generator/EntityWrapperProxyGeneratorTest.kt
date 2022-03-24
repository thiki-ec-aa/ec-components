package net.thiki.ec.component.code.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EntityWrapperProxyGeneratorTest{


    data class Person(
        val firstName: String,
        val lastName: String,
        val age: Int
    ){
        fun getName(): String{
            return "$firstName $lastName"
        }
    }

    /**
     * SampleCode in kt:
     */
    class PersonDo(

    )

    val javaCode =
"""
    class PersonDo extends Person{

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

    }
""".trimIndent()

    @Test
    fun testEntityProxyGenerator(){
        println(javaCode);
        val cut = EntityWrapperProxyGenerator();
        val generatedJavaCode = cut.generateJavaCode(EntityClassMeta(Person::class.java))

        assertEquals(javaCode, generatedJavaCode)

    }
}