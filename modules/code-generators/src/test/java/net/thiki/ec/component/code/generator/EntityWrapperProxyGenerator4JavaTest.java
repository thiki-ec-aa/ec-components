package net.thiki.ec.component.code.generator;


import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntityWrapperProxyGenerator4JavaTest {

    /**
     * the original entity
     */
    @Data
    static class Person{
        private String firstName;
        private String lastName;
        private int age;

        public String getName(){
            return String.format("%s %s", firstName, lastName);
        }
    }

    /**
     * The generated domain object(DO) wrapping the original entity with a delegate mode
     * and avoid copying the data from the entity for the sake of performance.
     *
     * won't generate following methods:
     * - toString
     * - hashCode
     * - equals
     */

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


    @Test
    void testEntityProxyGenerator(){

    }

}
