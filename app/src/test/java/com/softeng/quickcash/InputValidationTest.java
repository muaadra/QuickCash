package com.softeng.quickcash;
import org.junit.Test;
import static org.junit.Assert.*;

public class InputValidationTest {

    /**
     * This test checks for password length
     * Minimum allowed is 6 characters
     */
    @Test
    public void passwordLengthTest() {
        InputValidation input = new InputValidation();

        assertFalse(input.isPasswordLengthValid(""));
        assertFalse(input.isPasswordLengthValid("passw"));
        assertTrue(input.isPasswordLengthValid("passwo"));
        assertTrue(input.isPasswordLengthValid("password"));
    }

    /**
     * This test checks if password contains one uppercase
     * and one lower case character
     */
    @Test
    public void passwordCharacterCaseTest() {
        InputValidation input = new InputValidation();

        assertFalse(input.isPasswordCaseValid("password"));
        assertTrue(input.isPasswordCaseValid("Password"));
        assertTrue(input.isPasswordCaseValid("passWoRd"));
        assertFalse(input.isPasswordCaseValid("PASSWORD"));
    }

    /**
     * This test checks if email length is valid
     * i.e. the shortest accepted email will be at least 5
     * characters as in a@b.c
     */
    @Test
    public void emailLengthTest() {
        InputValidation input = new InputValidation();

        assertTrue(input.isEmailLengthValid("a@b.c"));
        assertFalse(input.isEmailLengthValid("a@"));
        assertFalse(input.isEmailLengthValid(""));
        assertTrue(input.isEmailLengthValid("a@b.com"));
    }
}
