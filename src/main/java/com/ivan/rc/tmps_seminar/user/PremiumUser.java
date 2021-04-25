package com.ivan.rc.tmps_seminar.user;

public class PremiumUser extends AbstractUser {

    public PremiumUser(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public Role getRole() {
        return new PremiumRole();
    }

}
