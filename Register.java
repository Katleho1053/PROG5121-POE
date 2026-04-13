package com.mycompany.login;

import java.util.Scanner;

public class Register {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        int choice;

        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> registerUser(scanner, login);
                case 2 -> loginUser(scanner, login);
                case 3 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 3);

        scanner.close();
    }

    private static void registerUser(Scanner scanner, Login login) {
        System.out.print("Enter first name: ");
        login.setFirstName(scanner.nextLine());

        System.out.print("Enter last name: ");
        login.setLastName(scanner.nextLine());

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.println(login.getUsernameMessage(username));

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println(login.getPasswordMessage(password));

        System.out.print("Enter cell phone number: ");
        String cellPhoneNumber = scanner.nextLine();
        System.out.println(login.getCellPhoneMessage(cellPhoneNumber));

        String registrationMessage = login.registerUser(username, password, cellPhoneNumber);
        System.out.println(registrationMessage);
    }

    private static void loginUser(Scanner scanner, Login login) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        login.loginUser(username, password);
        System.out.println(login.returnLoginStatus());
    }
}
