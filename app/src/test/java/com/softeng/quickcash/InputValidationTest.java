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

}
