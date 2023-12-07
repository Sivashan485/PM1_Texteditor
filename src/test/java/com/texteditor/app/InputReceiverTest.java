package com.texteditor.app;



import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InputReceiverTest {

    InputReceiver input;

    @Test
    void allowedCharactersUmlauts() {
        System.setIn(new ByteArrayInputStream("äöüÄÖÜ\n".getBytes()));
        input = new InputReceiver();
        String inputText = input.readAndFilterUserInput();
        String expected = "äöüÄÖÜ";
        assertEquals(expected, inputText);
    }

    @Test
    void allowedCharactersAtoZ() {
        System.setIn(new ByteArrayInputStream("AkniecnienTernnvEsflksjSS\n".getBytes()));
        input = new InputReceiver();
        String inputText = input.readAndFilterUserInput();
        String expected = "AkniecnienTernnvEsflksjSS";
        assertEquals(expected, inputText);
    }

    @Test
    void testAllowedCharactersAll() {
        System.setIn(new ByteArrayInputStream("Aadf .,:;-!? '()\"%@+*[]{}&#$ksnkdf23324ä\n".getBytes()));
        input = new InputReceiver();
        String inputText = input.readAndFilterUserInput();
        String expected = "Aadf .,:;-!? '()\"%@+*[]{}&#$ksnkdf23324ä";
        assertEquals(expected, inputText);
    }

    @Test
    void unallowedCharacters() {
        System.setIn(new ByteArrayInputStream("£€¢¬§°¦éà\n".getBytes()));
        input = new InputReceiver();
        String inputText = input.readAndFilterUserInput();
        String expected = "";
        assertEquals(expected, inputText);
    }

    @Test
    void testReadAndFilterUserInput() {
        System.setIn(new ByteArrayInputStream("This is a test input with some disallowed characters: @#%".getBytes()));
        input = new InputReceiver();
        String filteredInput = input.readAndFilterUserInput();
        String expected = "This is a test input with some disallowed characters: @#%";
        assertEquals(expected, filteredInput);
    }

    @Test
    void testFilterInputShouldFilterUnwantedCharacters() {
        System.setIn(new ByteArrayInputStream("Hello123²§~".getBytes()));
        input = new InputReceiver();
        String filteredInput = input.readAndFilterUserInput();
        String expected = "Hello123";
        assertEquals(expected, filteredInput);
    }

    @Test
    void testSplitInputShouldSplitAndIdentifyCommandAdd() {
        System.setIn(new ByteArrayInputStream("add Hey how are you, i'm the test".getBytes()));
        input = new InputReceiver();
        input.parseInput();
        assertEquals("add", input.getUserCommand());
        assertNull(input.getUserIndex());
        assertEquals("Hey how are you, i'm the test", input.getRestPart());
    }

    @Test
    void testSplitInputShouldSplitAndIdentifyCommandReplace() {
        System.setIn(new ByteArrayInputStream(("replace diam horem").getBytes()));
        input = new InputReceiver();
        input.parseInput();
        assertEquals("replace", input.getUserCommand());
        assertNull(input.getUserIndex());
        assertEquals("diam horem", input.getRestPart());
    }

    @Test
    void testSplitInputShouldSplitAndIdentifyCommandExit() {
        System.setIn(new ByteArrayInputStream(("exit 1212").getBytes()));
        input = new InputReceiver();
        input.parseInput();
        assertEquals(ApplicationCommand.UNKNOWN.command, input.getUserCommand());
        assertNull(input.getUserIndex());
        assertEquals("1212", input.getRestPart());
    }

    @Test
    void testSplitInputShouldSplitButNotIdentifyCommandExit() {
        System.setIn(new ByteArrayInputStream(("add exit is the exit For the Exit").getBytes()));
        input = new InputReceiver();
        input.parseInput();
        assertEquals("add", input.getUserCommand());
        assertNull(input.getUserIndex());
        assertEquals("exit is the exit For the Exit", input.getRestPart());
    }

    @Test
    void testSplitInputShouldSplitAndIdentifyCommandDel() {
        System.setIn(new ByteArrayInputStream(("Del is to del some text In The Text Editor.").getBytes()));
        input = new InputReceiver();
        input.parseInput();
        assertEquals("del", input.getUserCommand());
        assertNull(input.getUserIndex());
        assertEquals("is to del some text In The Text Editor.", input.getRestPart());
    }


    @Test
    void testSplitInputShouldHandleEmptyInput() {
        System.setIn(new ByteArrayInputStream("\n".getBytes()));
        input = new InputReceiver();
        input.parseInput();
        assertEquals("", input.getUserCommand());
        assertNull(input.getUserIndex());
        assertEquals("", input.getRestPart());
    }


}