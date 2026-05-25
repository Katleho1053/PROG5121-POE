package com.mycompany.chattingapp;

/**
 *
 * @author katle
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Message {
    private static int totalMessagesSent = 0;
    private static final List<Message> sentMessages = new ArrayList<>();

    private final String messageID;
    private final String recipient;
    private final String messageText;
    private String messageHash;

    public Message(String messageID, String recipient, String messageText) {
        this.messageID = Objects.requireNonNull(messageID, "messageID cannot be null");
        this.recipient = Objects.requireNonNull(recipient, "recipient cannot be null");
        this.messageText = Objects.requireNonNull(messageText, "messageText cannot be null");
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        return recipient.matches("^\\+\\d{1,10}$");
    }

    public String createMessageHash() {
        String trimmedMessage = messageText.trim();
        String[] words = trimmedMessage.isEmpty() ? new String[0] : trimmedMessage.split("\\s+");
        String firstWord = words.length == 0 ? "" : words[0];
        String lastWord = words.length == 0 ? "" : words[words.length - 1];
        String idPrefix = messageID.length() <= 2 ? messageID : messageID.substring(0, 2);

        messageHash = (idPrefix + ":" + totalMessagesSent + ":" + firstWord + lastWord)
                .toUpperCase(Locale.ROOT);
        return messageHash;
    }

    public String sentMessage() {
        if (!checkMessageID()) {
            return "Message ID is invalid.";
        }

        if (!checkRecipientCell()) {
            return "Recipient cell number is invalid.";
        }

        createMessageHash();
        sentMessages.add(this);
        totalMessagesSent++;
        return "Message sent.";
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public static List<Message> getSentMessages() {
        return Collections.unmodifiableList(sentMessages);
    }

    static void resetMessagesForTesting() {
        totalMessagesSent = 0;
        sentMessages.clear();
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
