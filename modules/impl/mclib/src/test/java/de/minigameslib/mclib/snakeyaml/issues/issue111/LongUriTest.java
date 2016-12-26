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
package de.minigameslib.mclib.snakeyaml.issues.issue111;

import java.io.StringReader;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.ScalarNode;
import junit.framework.TestCase;

public class LongUriTest extends TestCase {
    /**
     * Try loading a tag with a very long escaped URI section (over 256 bytes'
     * worth).
     */
    public void testLongURIEscape() {
        Yaml loader = new Yaml();
        // Create a long escaped string by exponential growth...
        String longEscURI = "%41"; // capital A...
        for (int i = 0; i < 10; ++i) {
            longEscURI = longEscURI + longEscURI;
        }
        assertEquals(1024 * 3, longEscURI.length());
        String yaml = "!" + longEscURI + " www";

        Node node = loader.compose(new StringReader(yaml));
        ScalarNode scalar = (ScalarNode) node;
        String etalon = "!";
        for (int i = 0; i < 1024; i++) {
            etalon += "A";
        }
        assertEquals(1025, etalon.length());
        assertEquals(etalon, scalar.getTag().toString());
    }
}
