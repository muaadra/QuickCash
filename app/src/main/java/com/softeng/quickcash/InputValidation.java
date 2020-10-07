package com.softeng.quickcash;

/**
 * This class validated various user inputs such as password and email
 *
 * @author Muaad Alrawhani (B00538563)
 *
 */
public class InputValidation {

    /**
     * validates password length, max is 6 characters
     * @return
     */
    public boolean isPasswordLengthValid(String password){
        return password.length() >= 6;
    }

    /**
     * This method checks if password contains one uppercase
     * and one lower case character
     */
    public boolean isPasswordCaseValid(String password){
        if(password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*")){
            return true;
        }
        return false;
    }

    /**
     * checks if email length is valid
     * i.e. the shortest accepted email will be at least 5
     * @param email email to be checked
     * @return true if valid
     */
    public boolean isEmailLengthValid(String email){
        return email.length() >= 5;
    }
}
