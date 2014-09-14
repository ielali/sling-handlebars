package com.sling.handlebars.script;

/**
 * Created by imad.elali on 14/09/2014.
 */
public class Bean {
    private String firstName = "FirstName";
    private String lastName = "LastName";
    private int age = 30;

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

    public String printFull() {
        return "Mr: " + firstName + " " + lastName + " age: " + age;
    }

    public String printArg(int argOne) {
        return "Mr: " + firstName + " " + lastName + " age: " + argOne;
    }
}
