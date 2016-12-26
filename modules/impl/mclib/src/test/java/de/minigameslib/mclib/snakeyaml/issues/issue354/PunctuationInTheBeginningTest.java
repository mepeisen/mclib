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
package de.minigameslib.mclib.snakeyaml.issues.issue354;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import de.minigameslib.mclib.snakeyaml.Util;
import de.minigameslib.mclib.snakeyaml.Yaml;

/**
 * The test does not fix anything. It just proves that SnakeYAML works as it should according to the spec 1.1
 */
public class PunctuationInTheBeginningTest {

    @Test
    public void testBacktickAndAtSign() throws IOException {
        String input = Util.getLocalResource("issues/issue354.yaml");
        Yaml yaml = new Yaml();
        Map<String, Object> bean = (Map<String, Object>) yaml.load(input);
        assertEquals("This is\n`a literal\n", bean.get("foo"));
        assertEquals("And\n@this\n", bean.get("bar"));
    }
}
