/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.minigameslib.mclib.snakeyaml.reader;

import junit.framework.TestCase;

public class ReaderStringTest extends TestCase {

    public void testCheckPrintable() {
        StreamReader reader = new StreamReader("test");
        reader.checkPrintable("test");
        assertTrue(StreamReader.isPrintable("test"));
    }

    public void testCheckNonPrintable() {
        assertFalse(StreamReader.isPrintable("test\u0005 fail"));
        try {
            new StreamReader("test\u0005 fail");
            fail("Non printable Unicode code points must not be accepted.");
        } catch (ReaderException e) {
            assertEquals(
                    "unacceptable code point '' (0x5) special characters are not allowed\nin \"'string'\", position 4",
                    e.toString());
        }
    }

    /**
     * test that regular expression and array check work the same
     */
    public void testCheckAll() {
        StreamReader streamReader = new StreamReader("");
        for (char i = 0; i < 256 * 256 - 1; i++) {
            char[] chars = new char[1];
            chars[0] = i;
            String str = new String(chars);
            boolean regularExpressionResult = StreamReader.isPrintable(str);

            boolean charsArrayResult = true;
            try {
                streamReader.checkPrintable(str);
            } catch (Exception e) {
                String error = e.getMessage();
                assertTrue(
                        error,
                        error.startsWith("unacceptable character")
                                || error.equals("special characters are not allowed"));
                charsArrayResult = false;
            }
            assertEquals("Failed for #" + i, regularExpressionResult, charsArrayResult);
        }
    }

    public void testForward() {
        StreamReader reader = new StreamReader("test");
        while (reader.peek() != '\u0000') {
            reader.forward(1);
        }
        reader = new StreamReader("test");
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('e', reader.peek());
        reader.forward(1);
        assertEquals('s', reader.peek());
        reader.forward(1);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('\u0000', reader.peek());
    }

    public void testPeekInt() {
        StreamReader reader = new StreamReader("test");
        assertEquals('t', reader.peek(0));
        assertEquals('e', reader.peek(1));
        assertEquals('s', reader.peek(2));
        assertEquals('t', reader.peek(3));
        reader.forward(1);
        assertEquals('e', reader.peek(0));
        assertEquals('s', reader.peek(1));
        assertEquals('t', reader.peek(2));
    }

}
