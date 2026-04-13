package com.mycompany.login;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
        assertEquals("Username successfully captured.", login.getUsernameMessage("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!"));
        assertEquals(
                "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.getUsernameMessage("kyle!!!!!!")
        );
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&8sec@ke99!"));
        assertEquals("Password successfully captured.", login.getPasswordMessage("Ch&8sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
        assertEquals(
                "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.getPasswordMessage("password")
        );
    }

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
        assertEquals("Cell number successfully captured.", login.getCellPhoneMessage("+27838968976"));
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08966553"));
        assertEquals(
                "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                login.getCellPhoneMessage("08966553")
        );
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        login.registerUser("kyl_1", "Ch&8sec@ke99!", "+27838968976");

        assertTrue(login.loginUser("kyl_1", "Ch&8sec@ke99!"));
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus());
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&8sec@ke99!", "+27838968976");

        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus());
    }
}
