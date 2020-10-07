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
}
