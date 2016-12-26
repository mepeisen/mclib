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
package de.minigameslib.mclib.snakeyaml.emitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.minigameslib.mclib.snakeyaml.DumperOptions.Version;
import de.minigameslib.mclib.snakeyaml.constructor.AbstractConstruct;
import de.minigameslib.mclib.snakeyaml.constructor.Constructor;
import de.minigameslib.mclib.snakeyaml.error.YAMLException;
import de.minigameslib.mclib.snakeyaml.events.AliasEvent;
import de.minigameslib.mclib.snakeyaml.events.DocumentEndEvent;
import de.minigameslib.mclib.snakeyaml.events.DocumentStartEvent;
import de.minigameslib.mclib.snakeyaml.events.Event;
import de.minigameslib.mclib.snakeyaml.events.ImplicitTuple;
import de.minigameslib.mclib.snakeyaml.events.MappingEndEvent;
import de.minigameslib.mclib.snakeyaml.events.MappingStartEvent;
import de.minigameslib.mclib.snakeyaml.events.ScalarEvent;
import de.minigameslib.mclib.snakeyaml.events.SequenceEndEvent;
import de.minigameslib.mclib.snakeyaml.events.SequenceStartEvent;
import de.minigameslib.mclib.snakeyaml.events.StreamEndEvent;
import de.minigameslib.mclib.snakeyaml.events.StreamStartEvent;
import de.minigameslib.mclib.snakeyaml.nodes.MappingNode;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.ScalarNode;

public class EventConstructor extends Constructor {

    public EventConstructor() {
        this.yamlConstructors.put(null, new ConstructEvent());
    }

    private class ConstructEvent extends AbstractConstruct {

        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
            Map<Object, Object> mapping;
            if (node instanceof ScalarNode) {
                mapping = new HashMap<Object, Object>();
            } else {
                mapping = constructMapping((MappingNode) node);
            }
            String className = node.getTag().getValue().substring(1) + "Event";
            Event value;
            if (className.equals("AliasEvent")) {
                value = new AliasEvent((String) mapping.get("anchor"), null, null);
            } else if (className.equals("ScalarEvent")) {
                String tag = (String) mapping.get("tag");
                String v = (String) mapping.get("value");
                if (v == null) {
                    v = "";
                }
                List<Boolean> implicitList = (List<Boolean>) mapping.get("implicit");
                ImplicitTuple implicit;
                if (implicitList == null) {
                    implicit = new ImplicitTuple(false, true);
                } else {
                    implicit = new ImplicitTuple((Boolean) implicitList.get(0),
                            (Boolean) implicitList.get(1));
                }
                value = new ScalarEvent((String) mapping.get("anchor"), tag, implicit, v, null,
                        null, null);
            } else if (className.equals("SequenceStartEvent")) {
                String tag = (String) mapping.get("tag");
                Boolean implicit = (Boolean) mapping.get("implicit");
                if (implicit == null) {
                    implicit = true;
                }
                value = new SequenceStartEvent((String) mapping.get("anchor"), tag, implicit, null,
                        null, false);
            } else if (className.equals("MappingStartEvent")) {
                String tag = (String) mapping.get("tag");
                Boolean implicit = (Boolean) mapping.get("implicit");
                if (implicit == null) {
                    implicit = true;
                }
                value = new MappingStartEvent((String) mapping.get("anchor"), tag, implicit, null,
                        null, false);
            } else if (className.equals("DocumentEndEvent")) {
                value = new DocumentEndEvent(null, null, false);
            } else if (className.equals("DocumentStartEvent")) {
                Map<String, String> tags = (Map<String, String>) mapping.get("tags");
                List<Integer> versionList = (List<Integer>) mapping.get("version");
                Version version = null;
                // TODO ???
                if (versionList != null) {
                    Integer major = versionList.get(0).intValue();
                    if (major != 1) {
                        throw new YAMLException("Unsupported version.");
                    }
                    Integer minor = versionList.get(1).intValue();
                    if (minor == 0) {
                        version = Version.V1_0;
                    } else {
                        version = Version.V1_1;
                    }
                }
                value = new DocumentStartEvent(null, null, false, version, tags);
            } else if (className.equals("MappingEndEvent")) {
                value = new MappingEndEvent(null, null);
            } else if (className.equals("SequenceEndEvent")) {
                value = new SequenceEndEvent(null, null);
            } else if (className.equals("StreamEndEvent")) {
                value = new StreamEndEvent(null, null);
            } else if (className.equals("StreamStartEvent")) {
                value = new StreamStartEvent(null, null);
            } else {
                throw new UnsupportedOperationException();
            }
            return value;
        }
    }
}
