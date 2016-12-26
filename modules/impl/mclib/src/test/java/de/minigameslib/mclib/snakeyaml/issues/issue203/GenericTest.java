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
package de.minigameslib.mclib.snakeyaml.issues.issue203;

import de.minigameslib.mclib.snakeyaml.Yaml;
import junit.framework.TestCase;

public class GenericTest extends TestCase {

    public void testGenericInterface() {
        Yaml yaml = new Yaml();
        String uuu = "!!de.minigameslib.mclib.snakeyaml.issues.issue203.DataBean\n"
                + "content: !!de.minigameslib.mclib.snakeyaml.issues.issue203.ContentIdentifierImpl 33\n"
                + "id: 555";
        DataBean obj = (DataBean) yaml.load(uuu);
        assertEquals(33, obj.getContent().getId().intValue());
    }
}
