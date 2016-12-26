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
package de.minigameslib.mclib.snakeyaml.constructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import de.minigameslib.mclib.snakeyaml.composer.Composer;
import de.minigameslib.mclib.snakeyaml.parser.Parser;
import de.minigameslib.mclib.snakeyaml.parser.ParserImpl;
import de.minigameslib.mclib.snakeyaml.reader.StreamReader;
import de.minigameslib.mclib.snakeyaml.resolver.Resolver;
import junit.framework.TestCase;

public class ConstructorMappingTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testGetDefaultMap() {
        String data = "{ one: 1, two: 2, three: 3 }";
        Map<Object, Object> map = (Map<Object, Object>) construct(new CustomConstructor(), data);
        assertNotNull(map);
        assertTrue(map.getClass().toString(), map instanceof TreeMap);
    }

    @SuppressWarnings("unchecked")
    public void testGetArrayList() {
        String data = "{ one: 1, two: 2, three: 3 }";
        Map<Object, Object> map = (Map<Object, Object>) construct(data);
        assertNotNull(map);
        assertTrue(map.getClass().toString(), map instanceof LinkedHashMap);
    }

    private Object construct(String data) {
        return construct(new Constructor(), data);
    }

    private Object construct(Constructor constructor, String data) {
        StreamReader reader = new StreamReader(data);
        Parser parser = new ParserImpl(reader);
        Resolver resolver = new Resolver();
        Composer composer = new Composer(parser, resolver);
        constructor.setComposer(composer);
        return constructor.getSingleData(Object.class);
    }

    class CustomConstructor extends Constructor {
        @Override
        protected Map<Object, Object> createDefaultMap() {
            return new TreeMap<Object, Object>();
        }
    }
}
