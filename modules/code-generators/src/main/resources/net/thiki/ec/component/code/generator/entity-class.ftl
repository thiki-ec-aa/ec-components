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