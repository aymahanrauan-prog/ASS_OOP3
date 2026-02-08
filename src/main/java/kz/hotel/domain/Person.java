package kz.hotel.domain;

import java.util.Objects;

public abstract class Person {
    protected int id;
    protected String firstName;
    protected String lastName;

    protected Person(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", fullName='" + getFullName() + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
