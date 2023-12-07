package com.texteditor.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for managing the text. It contains the text, the
 * glossary and the methods for editing the text. It also contains the methods
 * for formatting the text. It is also responsible for the communication with
 * the user.
 */
public class TextManager {

    private static final String DUMMY_TEXT = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            +
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
            "when an unknown printer took a galley of type and scrambled it to make a type specimen book." +
            " It has survived not only five centuries, but also the leap into electronic typesetting, " +
            "remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset" +
            " sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like " +
            "Aldus PageMaker including versions of Lorem Ipsum.";
    private final List<String> text;
    private Integer maxWidth;
    private String formattedText;
    boolean isFormatterFixSuccessful;
    boolean isFormatterRaw;

    /**
     * Constructor for the TextManager class. It initializes the input, output,
     * glossary, text, isExitTriggered and isFormatterRaw variables.
     */
    TextManager() {
        text = new ArrayList<>();
        formattedText = "";
        isFormatterRaw = true;
        formatTextRaw();
    }

    /**
     * Adds a dummy paragraph to the specified index. If the index is larger than
     * the size of the text, the dummy paragraph is added to the end of the text.
     */
    boolean addDummyParagraph(boolean isIndexNull, Integer paragraphIndex) {
        return addNewParagraph(isIndexNull, TextManager.DUMMY_TEXT, paragraphIndex);
    }

    /**
     * Adds a new paragraph to the text based on user input.
     * If the index is not provided or is invalid, the new paragraph is added at the
     * end.
     * If the index is valid, the new paragraph is inserted at the specified
     * position.
     */
    boolean addNewParagraph(boolean isIndexNull, String enteredText, Integer paragraphIndex) {
        if (isIndexNull) {
            text.add(enteredText);
        } else {
            text.add(paragraphIndex - 1, enteredText);
        }
        return true;
    }

    /**
     * Deletes the paragraph at the specified index.
     */
    boolean deleteParagraph(boolean isIndexNull, Integer paragraphIndex) {
        if (isIndexNull) {
            text.remove(text.size() - 1);
        } else {
            text.remove(paragraphIndex - 1);
        }
        return true;
    }

    /**
     * Formats the given ArrayList of Strings into a single String with each element
     * of the ArrayList
     * preceded by its index in the ArrayList enclosed in angle brackets.
     */
    void formatTextRaw() {
        StringBuilder rawText = new StringBuilder();
        for (int paragraph = 0; paragraph < text.size(); paragraph++) {
            rawText.append((paragraph + 1)).append(": ").append(text.get(paragraph));
            if(paragraph < text.size() -1){
                rawText.append("\n");
            }
        }
        setFormattedText(rawText.toString());
    }

    /**
     * Formats the text by fixing the paragraph width. If maxWidth is not valid, the
     * formatter is unsuccessful.
     * Otherwise, it splits the text into paragraphs and words, handles word
     * wrapping, and sets the formatted text.
     */
    void formatTextFix() {
        if (!isMaxWidthValid()) {
            isFormatterFixSuccessful = false;
        } else {
            StringBuilder fixText = new StringBuilder();
            int currentParagraphWidth = 0;
            int paragraphCount = text.size();
            for (int i = 0; i < paragraphCount; i++) {
                String paragraph = text.get(i);
                String[] words = paragraph.split("\\s+");
                for (String word : words) {
                    currentParagraphWidth = handleWordWrapping(currentParagraphWidth, word, fixText);
                }
                if (i < paragraphCount - 1) {
                    fixText.append("\n\n");
                }
                currentParagraphWidth = 0;
            }
            setFormattedText(fixText.toString());
            isFormatterFixSuccessful = true;
        }
    }

    /**
     * Handles the word wrapping for the given word.
     *
     * @param currentParagraphWidth the current width of the paragraph.
     * @param word                  the word to be wrapped.
     * @param fixText               the StringBuilder to which the word is appended.
     * @return the updated paragraph width.
     */
    private int handleWordWrapping(int currentParagraphWidth, String word, StringBuilder fixText) {
        while (word.length() > maxWidth) {
            if (currentParagraphWidth > 0) {
                currentParagraphWidth = resetParagraphWidth(fixText);
            }
            fixText.append(word, 0, maxWidth);
            word = word.substring(maxWidth);
            if (!word.isEmpty()) {
                currentParagraphWidth = resetParagraphWidth(fixText);
            }
        }
        currentParagraphWidth = appendNewLine(currentParagraphWidth, fixText, word);
        currentParagraphWidth = appendSpace(fixText, currentParagraphWidth);
        fixText.append(word);
        return currentParagraphWidth + word.length();
    }

    /**
     * Appends a space to the given StringBuilder if the currentParagraphWidth is
     * greater than 0.
     *
     * @param fixText               the StringBuilder to which the space is
     *                              appended.
     * @param currentParagraphWidth the current width of the paragraph.
     * @return the updated paragraph width.
     */
    private int appendNewLine(int currentParagraphWidth, StringBuilder fixText, String word) {
        if (currentParagraphWidth + (currentParagraphWidth > 0 ? 1 : 0) + word.length() > maxWidth) {
            currentParagraphWidth = resetParagraphWidth(fixText);
        }
        return currentParagraphWidth;
    }

    /**
     * Appends a space to the given StringBuilder if the currentParagraphWidth is
     * greater than 0.
     *
     * @param fixText               the StringBuilder to which the space is
     *                              appended.
     * @param currentParagraphWidth the current width of the paragraph.
     * @return the updated paragraph width.
     */
    private int appendSpace(StringBuilder fixText, int currentParagraphWidth) {
        if (currentParagraphWidth > 0) {
            fixText.append(" ");
            currentParagraphWidth++;
        }
        return currentParagraphWidth;
    }

    /**
     * Checks if the maxWidth is valid.
     *
     * @return true if the maxWidth is valid, false otherwise.
     */
    private boolean isMaxWidthValid() {
        return maxWidth != null;
    }

    /**
     * Resets the paragraph width and appends a new line to the given StringBuilder.
     *
     * @param fixFormatted the StringBuilder to which the new line is appended.
     * @return 0.
     */
    private int resetParagraphWidth(StringBuilder fixFormatted) {
        fixFormatted.append("\n");
        return 0;
    }

    /**
     * Prints the text to the console.
     */
    void printText() {
        if (isFormatterRaw) {
            formatTextRaw();
        } else {
            formatTextFix();
        }
        OutputManager.logAndPrintInfoMessage(formattedText);
    }

    /**
     * Replaces a word in a specific paragraph of the text.
     *
     * @param index           the index of the paragraph in the text.
     * @param originalWord    the word to be replaced.
     * @param replacementWord the word to replace the original word with.
     * @return true if the replacement was successful, false otherwise.
     */
    boolean replaceWordInParagraph(Integer index, String originalWord, String replacementWord) {
        String paragraph = text.get(index);
        paragraph = paragraph.replace(originalWord, replacementWord);
        boolean isReplacementSuccessful = !text.get(index).equalsIgnoreCase(paragraph);
        if (isReplacementSuccessful) {
            text.set(index, paragraph);
        }
        return isReplacementSuccessful;
    }

    /**
     * Checks if the text contains the given word.
     *
     * @param originalWord   the word to be checked.
     * @param paragraphIndex the index of the paragraph in the text.
     * @return true if the text contains the word, false otherwise.
     */
    boolean containsWord(String originalWord, Integer paragraphIndex) {
        boolean isWordEmpty = "".equals(originalWord);
        if (paragraphIndex == null) {
            paragraphIndex = text.size();
        }
        return !isWordEmpty && text.get(paragraphIndex - 1).contains(originalWord);
    }

    /**
     * Replaces the paragraphs in the specified range with the given text.
     */
    boolean replaceParagraphSection(boolean isIndexNull, String originalWord, String replacingWord,
            Integer paragraphIndex) {
        if (isIndexNull) {
            return replaceWordInParagraph(text.size() - 1, originalWord, replacingWord);
        } else {
            return replaceWordInParagraph(paragraphIndex - 1, originalWord, replacingWord);
        }
    }

    /**
     * Checks if the text is empty.
     *
     * @return true if the text is empty, false otherwise.
     */
    boolean isTextEmpty() {
        return text.isEmpty();
    }

    /**
     * Getter for the text. It is used for testing.
     *
     * @return the text
     */
    List<String> getText() {
        return Collections.unmodifiableList(text);
    }

    /**
     * Getter for the formatted text. It is used for testing.
     *
     * @return the formatted text
     */
    String getFormattedText() {
        return this.formattedText;
    }

    /**
     * Setter for the max width.
     *
     * @return the max width
     */
    void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * Getter for the isFormatterRaw variable.
     *
     * @return the isFormatterRaw variable
     */
    boolean getIsFormatterFixSuccessful() {
        return isFormatterFixSuccessful;
    }

    /**
     * Setter for the isFormatterRaw variable.
     */
    void setIsFormatterRaw(boolean isFormatterRaw) {
        this.isFormatterRaw = isFormatterRaw;
    }

    /**
     * Setter for the formatted text. It is used for testing.
     *
     * @param formattedText the formatted text
     */
    private void setFormattedText(String formattedText) {
        this.formattedText = formattedText;
    }

}
