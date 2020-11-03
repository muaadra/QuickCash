package com.softeng.quickcash;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Test cases for InputValidation class
 *
 * @author Muaad Alrawhani (B00538563)
 *
 */
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

    /**
     * This test checks if email has a valid format
     * email will be accepted if it's of a "i@i.i" format
     * where i is at least one character (i.e. any character including special chars)
     */
    @Test
    public void emailFormatTest() {
        InputValidation input = new InputValidation();

        assertFalse(input.isEmailFormatValid("email.c"));
        assertFalse(input.isEmailFormatValid("email."));
        assertFalse(input.isEmailFormatValid("@email"));
        assertFalse(input.isEmailFormatValid("a@aaa"));
        assertFalse(input.isEmailFormatValid("aa@a."));
        assertTrue(input.isEmailFormatValid("!@%.5"));
        assertTrue(input.isEmailFormatValid("jojo@me.com"));
    }
    /**
     * This test checks if user enter number.
     */
    @Test
    public void numberEnterTest() {
        InputValidation input = new InputValidation();
        assertTrue(input.isNumber("5"));
        assertTrue(input.isNumber("50"));
        assertTrue(input.isNumber("500"));
        assertFalse(input.isNumber("aaaaa"));
    }

}
