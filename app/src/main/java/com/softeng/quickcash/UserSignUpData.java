package com.softeng.quickcash;

/**
 * New user Sign Up info
 * @author Muaad Alrawhani (B00538563)
 */

public class UserSignUpData {
    private String email;
    private String password;

    public UserSignUpData() {}

    public UserSignUpData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}