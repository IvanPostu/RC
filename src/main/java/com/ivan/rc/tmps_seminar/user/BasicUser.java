package com.ivan.rc.tmps_seminar.user;

public class BasicUser extends AbstractUser {

    public BasicUser(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public Role getRole() {
        return new PremiumRole();
    }

}
