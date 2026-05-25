package com.mycompany.chattingapp;

/**
 *
 * @author katle
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Register {
    private static final List<Message> storedMessages = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        int choice = 0;

        System.out.println("Welcome to QuickChat");

        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Send message");
            System.out.println("4. Show sent message count");
            System.out.println("5. Show recently sent messages");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a number from 1 to 6.");
                continue;
            }

            switch (choice) {
                case 1 -> registerUser(scanner, login);
                case 2 -> loginUser(scanner, login);
                case 3 -> sendMessage(scanner);
                case 4 -> System.out.println("Messages sent: " + Message.returnTotalMessages());
                case 5 -> showRecentlySentMessages();
                case 6 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 6);

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

    private static void sendMessage(Scanner scanner) {
        System.out.print("Enter message ID: ");
        String messageID = scanner.nextLine();

        System.out.print("Enter recipient number: ");
        String recipient = scanner.nextLine();

        System.out.print("Enter message: ");
        String messageText = scanner.nextLine();

        Message message = new Message(messageID, recipient, messageText);
        if (!message.checkMessageID()) {
            System.out.println("Message ID is invalid.");
            return;
        }

        if (!message.checkRecipientCell()) {
            System.out.println("Recipient cell number is invalid.");
            return;
        }

        System.out.println("\nChoose what you want to do with this message:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        System.out.print("Choose an option: ");

        String messageChoice = scanner.nextLine();

        switch (messageChoice) {
            case "1" -> {
                message.sentMessage();
                System.out.println("Message successfully sent");
                displayMessageDetails(message);
            }
            case "2" -> disregardMessage(scanner);
            case "3" -> storeMessage(message);
            default -> System.out.println("Invalid message option.");
        }
    }

    private static void showRecentlySentMessages() {
        System.out.println("coming soon");
    }

    private static void disregardMessage(Scanner scanner) {
        System.out.println("Press 0 to delete the message");
        String deleteChoice = scanner.nextLine();

        if ("0".equals(deleteChoice)) {
            System.out.println("Message deleted.");
        } else {
            System.out.println("Message was not deleted.");
        }
    }

    private static void storeMessage(Message message) {
        message.createMessageHash();
        storedMessages.add(message);

        try {
            Path jsonPath = Path.of("src", "main", "resources", "messages.json");
            Files.createDirectories(jsonPath.getParent());
            Files.writeString(jsonPath, buildStoredMessagesJson());
            System.out.println("Message successfully stored");
        } catch (IOException exception) {
            System.out.println("Message could not be stored.");
        }
    }

    private static String buildStoredMessagesJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"appName\": \"QuickChat\",\n");
        json.append("  \"recentMessagesStatus\": \"coming soon\",\n");
        json.append("  \"messages\": [\n");

        for (int index = 0; index < storedMessages.size(); index++) {
            Message message = storedMessages.get(index);
            json.append("    {\n");
            json.append("      \"messageID\": \"").append(escapeJson(message.getMessageID())).append("\",\n");
            json.append("      \"messageHash\": \"").append(escapeJson(message.getMessageHash())).append("\",\n");
            json.append("      \"recipient\": \"").append(escapeJson(message.getRecipient())).append("\",\n");
            json.append("      \"message\": \"").append(escapeJson(message.getMessageText())).append("\"\n");
            json.append("    }");

            if (index < storedMessages.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}\n");
        return json.toString();
    }

    private static String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static void displayMessageDetails(Message message) {
        System.out.println("Message ID: " + message.getMessageID());
        System.out.println("Message Hash: " + message.getMessageHash());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Message: " + message.getMessageText());
    }
}


    


