import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class messageapp {
    private static final Path JSON_PATH = Path.of("src", "main", "resources", "messages.json");
    private final List<StoredMessage> storedMessages = new ArrayList<>();

    public static void main(String[] args) {
        messageapp app = new messageapp();
        app.loadStoredMessages();
        app.storedMessagesMenu();
    }

    public void storedMessagesMenu() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n=== STORED MESSAGES MENU ===");
            System.out.println("1. Display sender and recipient of all stored messages");
            System.out.println("2. Display longest stored message");
            System.out.println("3. Search by message ID");
            System.out.println("4. Search all messages for a recipient");
            System.out.println("5. Delete message by hash");
            System.out.println("6. Display full report");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            option = parseOption(input, 1, 7);

            switch (option) {
                case 1 -> displaySenderAndRecipient();
                case 2 -> displayLongestStoredMessage();
                case 3 -> searchByMessageID(scanner);
                case 4 -> searchByRecipient(scanner);
                case 5 -> deleteByHash(scanner);
                case 6 -> displayFullReport();
                case 7 -> System.out.println("Exiting stored messages menu.");
                default -> System.out.println("Invalid choice.");
            }
        } while (option != 7);

        scanner.close();
    }

    private int parseOption(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input);
            return (value >= min && value <= max) ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void displaySenderAndRecipient() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages are available.");
            return;
        }

        System.out.println("Sender and recipient for all stored messages:");
        for (StoredMessage message : storedMessages) {
            System.out.printf("Sender: %s | Recipient: %s\n", message.sender, message.recipient);
        }
    }

    private void displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages are available.");
            return;
        }

        StoredMessage longest = storedMessages.stream()
                .max(Comparator.comparingInt(m -> m.messageText.length()))
                .orElse(null);

        if (longest == null) {
            System.out.println("No stored messages are available.");
            return;
        }

        System.out.println("Longest stored message:");
        displayMessageDetails(longest);
    }

    private void searchByMessageID(Scanner scanner) {
        System.out.print("Enter message ID to search: ");
        String id = scanner.nextLine().trim();

        StoredMessage message = storedMessages.stream()
                .filter(m -> m.messageID.equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        if (message == null) {
            System.out.println("No message found with ID: " + id);
            return;
        }

        displayMessageDetails(message);
    }

    private void searchByRecipient(Scanner scanner) {
        System.out.print("Enter recipient number to search: ");
        String recipient = scanner.nextLine().trim();

        List<StoredMessage> results = new ArrayList<>();
        for (StoredMessage message : storedMessages) {
            if (message.recipient.equalsIgnoreCase(recipient)) {
                results.add(message);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No messages found for recipient: " + recipient);
            return;
        }

        System.out.println("Messages for recipient " + recipient + ":");
        results.forEach(this::displayMessageDetails);
    }

    private void deleteByHash(Scanner scanner) {
        System.out.print("Enter message hash to delete: ");
        String hash = scanner.nextLine().trim();

        boolean removed = storedMessages.removeIf(message -> message.messageHash.equalsIgnoreCase(hash));
        if (removed) {
            saveStoredMessages();
            System.out.println("Message deleted successfully.");
        } else {
            System.out.println("No message found with hash: " + hash);
        }
    }

    private void displayFullReport() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages are available.");
            return;
        }

        System.out.println("Full stored messages report:");
        System.out.println("Total stored messages: " + storedMessages.size());
        for (StoredMessage message : storedMessages) {
            displayMessageDetails(message);
        }
    }

    private void displayMessageDetails(StoredMessage message) {
        System.out.println("---");
        System.out.println("Message ID: " + message.messageID);
        System.out.println("Sender: " + message.sender);
        System.out.println("Recipient: " + message.recipient);
        System.out.println("Message Hash: " + message.messageHash);
        System.out.println("Message: " + message.messageText);
    }

    private void loadStoredMessages() {
        if (!Files.exists(JSON_PATH)) {
            return;
        }

        try {
            String content = Files.readString(JSON_PATH);
            Pattern pattern = Pattern.compile("\\\"sender\\\"\\s*:\\s*\\\"(.*?)\\\".*?\\\"messageID\\\"\\s*:\\s*\\\"(.*?)\\\".*?\\\"messageHash\\\"\\s*:\\s*\\\"(.*?)\\\".*?\\\"recipient\\\"\\s*:\\s*\\\"(.*?)\\\".*?\\\"message\\\"\\s*:\\s*\\\"(.*?)\\\"",
                    Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                storedMessages.add(new StoredMessage(
                        unescapeJson(matcher.group(1)),
                        unescapeJson(matcher.group(2)),
                        unescapeJson(matcher.group(4)),
                        unescapeJson(matcher.group(5)),
                        unescapeJson(matcher.group(3))
                ));
            }
        } catch (IOException e) {
            System.out.println("Could not load stored messages: " + e.getMessage());
        }
    }

    private void saveStoredMessages() {
        try {
            Files.createDirectories(JSON_PATH.getParent());
            Files.writeString(JSON_PATH, buildStoredMessagesJson());
        } catch (IOException e) {
            System.out.println("Could not save stored messages: " + e.getMessage());
        }
    }

    private String buildStoredMessagesJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"appName\": \"QuickChat\",\n");
        json.append("  \"recentMessagesStatus\": \"stored messages available\",\n");
        json.append("  \"messages\": [\n");

        for (int i = 0; i < storedMessages.size(); i++) {
            StoredMessage message = storedMessages.get(i);
            json.append("    {\n");
            json.append("      \"sender\": \"").append(escapeJson(message.sender)).append("\",\n");
            json.append("      \"messageID\": \"").append(escapeJson(message.messageID)).append("\",\n");
            json.append("      \"messageHash\": \"").append(escapeJson(message.messageHash)).append("\",\n");
            json.append("      \"recipient\": \"").append(escapeJson(message.recipient)).append("\",\n");
            json.append("      \"message\": \"").append(escapeJson(message.messageText)).append("\"\n");
            json.append("    }");
            if (i < storedMessages.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}\n");
        return json.toString();
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeJson(String value) {
        return value
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    private static class StoredMessage {
        private final String sender;
        private final String messageID;
        private final String recipient;
        private final String messageText;
        private final String messageHash;

        public StoredMessage(String sender, String messageID, String recipient, String messageText, String messageHash) {
            this.sender = sender;
            this.messageID = messageID;
            this.recipient = recipient;
            this.messageText = messageText;
            this.messageHash = messageHash;
        }
    }
}
