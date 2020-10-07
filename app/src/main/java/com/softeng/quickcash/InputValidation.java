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
        if(password.length() < 6){
            return false;
        }
        return true;
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
}
