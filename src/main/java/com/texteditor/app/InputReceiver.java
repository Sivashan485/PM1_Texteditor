package com.texteditor.app;

import java.util.Scanner;

/**
 * Class for receiving user input and filtering the input with a regex.
 */
public class InputReceiver {

    private static final String DISALLOWED_CHARACTERS_REGEX = "[^A-Za-zäöüÄÖÜ 0-9 / .,:;\\-!?'\\\\()\\\"%@+*{}\\\\\\\\&#$\\[\\]]";
    private final Scanner userInput;
    private String userCommand;
    private Integer userIndex;
    private String restPart;
    private boolean isIndexValid;

    /**
     * Constructor for InputReceiver.
     */
    public InputReceiver() {
        userInput = new Scanner(System.in);
        resetValues();
    }

    /**
     * Resets the values of the instance variables associated with this class.
     * This method sets userIndex to null, userCommand to an empty string and
     * restPart to an empty string.
     */
    void resetValues() {
        userIndex = null;
        userCommand = "";
        restPart = "";
    }

    /**
     * Reads the user input, removes all characters that are not allowed and
     * returns the filtered input.
     *
     * @return The filtered input text.
     */
    String readAndFilterUserInput() {
        String inputText = userInput.nextLine();
        return inputText.replaceAll(DISALLOWED_CHARACTERS_REGEX, "");
    }

    /**
     * Splits the user input into a command and its arguments. If the command
     * requires an index and it's not provided, it sets the command to "unknown".
     */
    public void parseInput() {
        String input = readAndFilterUserInput();
        userCommand = extractCommand(input);
        restPart = input.substring(userCommand.length()).trim();
        if (!validateAndSetIndex(userCommand, restPart)) {
            userCommand = "unknown";
        }
    }

    /**
     * Extracts the command from the input. If the command is not recognized, it
     * tries to combine the first two words and parse them as a command.
     *
     * @param userInput The user's input.
     * @return The recognized command, or an empty string if no command is
     *         recognized.
     */
    String extractCommand(String userInput) {
        String[] userInputPartition = userInput.toLowerCase().split(" ");
        ApplicationCommand command = ApplicationCommand.parseCommand(userInputPartition[0]);
        if (command == ApplicationCommand.UNKNOWN && userInputPartition.length > 1) {
            command = ApplicationCommand.parseCommand(userInputPartition[0] + " " + userInputPartition[1]);
        }
        if (command != ApplicationCommand.UNKNOWN) {
            return command.getCommand();
        }
        return "";
    }

    /**
     * Validates the command and sets the index if required. If the command does not
     * require an index, it checks if the rest part of the input is empty.
     *
     * @param command  The command to validate.
     * @param restPart The rest part of the user input.
     * @return True if the command does not require an index and the rest part is
     *         empty, false otherwise.
     */
    private boolean validateAndSetIndex(String command, String restPart) {
        if (ApplicationCommand.parseCommand(command).getCommandHasIndex()) {
            if (restPart != null && !restPart.isEmpty()) {
                handleIndexCommand();
            } else {
                setUserIndex();
            }
        } else {
            return "".equals(restPart);
        }
        return true;
    }

    /**
     * Handles the index command.
     * If the rest part of the command matches a numeric pattern, it sets the user
     * index and validates it.
     * If the rest part does not match the numeric pattern, it invalidates the
     * index.
     */
    private void handleIndexCommand() {
        if (restPart.matches("^[0-9]+$")) {
            setUserIndex();
            isIndexValid = true;
        } else {
            isIndexValid = false;
        }
    }

    /**
     * Sets the user index from the rest part of the command.
     * If the rest part is not null and can be parsed as an integer, it sets the
     * user index to that integer.
     * If the rest part is not an integer, it catches the NumberFormatException and
     * leaves the user index as null.
     */
    private void setUserIndex() {
        isIndexValid = true;
        userIndex = null;
        try {
            if (restPart != null) {
                userIndex = Integer.parseInt(restPart);
            }
        } catch (NumberFormatException e) {
            // left empty
        }
    }

    /**
     * Checks if the 'restPart' is empty and sets 'indexIsNull' accordingly.
     * Returns the value of 'indexIsNull' after the check.
     *
     * @return {@code true} if 'restPart' is empty; otherwise, {@code false}.
     */
    public boolean isIndexNull() {
        return userIndex == null;
    }

    /**
     * Returns the validity of the index.
     *
     * @return true if the index is valid, false otherwise.
     */
    public boolean getIsIndexValid() {
        return isIndexValid;
    }

    /**
     * Retrieves the current command.
     *
     * @return The current command as a string.
     */
    public String getUserCommand() {
        return userCommand;
    }

    /**
     * Retrieves the current index.
     *
     * @return The current index as an integer.
     */
    public Integer getUserIndex() {
        return userIndex;
    }

    /**
     * Retrieves the rest part of the command.
     *
     * @return The rest part of the command as a string.
     */
    String getRestPart() {
        return restPart;
    }
}