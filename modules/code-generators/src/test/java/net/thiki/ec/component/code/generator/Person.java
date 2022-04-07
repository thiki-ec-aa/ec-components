package net.thiki.ec.component.code.generator;

public class Person {

    public Person() {

    }

    private String firstName;
    private String lastName;
    private int age;
    public int publicInt;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName(){
        return firstName + " " + lastName;
    }
}
