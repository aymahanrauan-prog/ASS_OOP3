package kz.hotel.domain;

import java.util.Objects;

public class Guest extends Person {

    private String phone;
    private String email;

    public Guest(int id, String firstName, String lastName, String phone, String email) {
        super(id, firstName, lastName);
        this.phone = phone;
        this.email = email;
    }

    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Guest{id=" + id +
                ", firstName='" + firstName +
                ", lastName='" + lastName +
                ", phone='" + phone +
                ", email='" + email +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guest)) return false;
        Guest guest = (Guest) o;
        return id == guest.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
