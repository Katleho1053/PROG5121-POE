package com.mycompany.login;

public class Login {
    private static final String USERNAME_SUCCESS = "Username successfully captured.";
    private static final String USERNAME_ERROR = "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    private static final String PASSWORD_SUCCESS = "Password successfully captured.";
    private static final String PASSWORD_ERROR = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    private static final String CELL_SUCCESS = "Cell number successfully captured.";
    private static final String CELL_ERROR = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    private static final String REGISTER_SUCCESS = "User has been registered successfully.";
    private static final String LOGIN_SUCCESS_TEMPLATE = "Welcome %s, %s it is great to see you again.";
    private static final String LOGIN_ERROR = "Username or password incorrect, please try again.";

    private String firstName = "User";
    private String lastName = "Name";
    private String username;
    private String password;
    private String cellPhoneNumber;
    private boolean lastLoginSuccessful;

    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;

        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            } else if (Character.isDigit(character)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercase && hasDigit && hasSpecialCharacter;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        // Regex pattern adapted from a standard South African international-format check:
        // country code +27 followed by exactly 9 digits.
        return cellPhoneNumber != null && cellPhoneNumber.matches("^\\+27\\d{9}$");
    }

    public String registerUser() {
        if (!checkUserName(username)) {
            return USERNAME_ERROR;
        }

        if (!checkPasswordComplexity(password)) {
            return PASSWORD_ERROR;
        }

        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return CELL_ERROR;
        }

        return REGISTER_SUCCESS;
    }

    public String registerUser(String username, String password, String cellPhoneNumber) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        return registerUser();
    }

    public boolean loginUser() {
        return lastLoginSuccessful;
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        lastLoginSuccessful = username != null
                && password != null
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
        return lastLoginSuccessful;
    }

    public String returnLoginStatus() {
        if (lastLoginSuccessful) {
            return String.format(LOGIN_SUCCESS_TEMPLATE, firstName, lastName);
        }
        return LOGIN_ERROR;
    }

    public String getUsernameMessage(String username) {
        return checkUserName(username) ? USERNAME_SUCCESS : USERNAME_ERROR;
    }

    public String getPasswordMessage(String password) {
        return checkPasswordComplexity(password) ? PASSWORD_SUCCESS : PASSWORD_ERROR;
    }

    public String getCellPhoneMessage(String cellPhoneNumber) {
        return checkCellPhoneNumber(cellPhoneNumber) ? CELL_SUCCESS : CELL_ERROR;
    }

    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.isBlank()) {
            this.firstName = firstName;
        }
    }

    public void setLastName(String lastName) {
        if (lastName != null && !lastName.isBlank()) {
            this.lastName = lastName;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }
}
