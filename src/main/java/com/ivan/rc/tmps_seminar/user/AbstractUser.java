package com.ivan.rc.tmps_seminar.user;

public abstract class AbstractUser {

    private String firstName;
    private String lastName;

    public AbstractUser(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public abstract Role getRole();

}
