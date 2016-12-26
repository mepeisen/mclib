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
package de.minigameslib.mclib.snakeyaml.issues.issue138;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.reader.ReaderException;
import junit.framework.TestCase;

public class ReaderExceptionTest extends TestCase {

    public void testGetters() {
        try {
            new Yaml().load("012\u0019");
            fail();
        } catch (ReaderException e) {
            assertEquals(3, e.getPosition());
            assertEquals("'string'", e.getName());
            assertEquals(0x19, e.getCodePoint());
        }
    }
}
