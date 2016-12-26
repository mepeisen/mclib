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
package de.minigameslib.mclib.snakeyaml.issues.issue139;

import java.util.List;
import java.util.Map;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.constructor.Constructor;
import de.minigameslib.mclib.snakeyaml.error.YAMLException;
import de.minigameslib.mclib.snakeyaml.nodes.MappingNode;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.NodeTuple;
import junit.framework.TestCase;

public class UniqueKeyTest extends TestCase {

    public void testNotUnique() {
        String data = "{key: 1, key: 2}";
        Yaml yaml = new Yaml(new UniqueKeyConstructor());
        try {
            yaml.load(data);
            fail("The same key must be rejected");
        } catch (Exception e) {
            assertEquals("The key is not unique key", e.getMessage());
        }
    }

    private class UniqueKeyConstructor extends Constructor {

        @Override
        protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
            List<NodeTuple> nodeValue = (List<NodeTuple>) node.getValue();
            for (NodeTuple tuple : nodeValue) {
                Node keyNode = tuple.getKeyNode();
                Node valueNode = tuple.getValueNode();
                Object key = constructObject(keyNode);
                if (key != null) {
                    key.hashCode();// check circular dependencies
                }
                Object value = constructObject(valueNode);
                Object old = mapping.put(key, value);
                if (old != null) {
                    throw new YAMLException("The key is not unique " + key);
                }
            }
        }
    }

}
