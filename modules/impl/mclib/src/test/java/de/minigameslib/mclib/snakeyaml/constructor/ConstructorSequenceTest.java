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

import java.util.ArrayList;
import java.util.List;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.composer.Composer;
import de.minigameslib.mclib.snakeyaml.parser.Parser;
import de.minigameslib.mclib.snakeyaml.parser.ParserImpl;
import de.minigameslib.mclib.snakeyaml.reader.StreamReader;
import de.minigameslib.mclib.snakeyaml.resolver.Resolver;
import junit.framework.TestCase;

public class ConstructorSequenceTest extends TestCase {

    public void testGetList() {
        String data = "[ 1, 2, 3 ]";
        List<Object> list = construct(new CustomConstructor(), data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof ArrayList<?>);
    }

    public void testGetArrayList() {
        String data = "[ 1, 2, 3 ]";
        List<Object> list = construct(data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof ArrayList<?>);
    }

    public void testDumpList() {
        List<Integer> l = new ArrayList<Integer>(2);
        l.add(1);
        l.add(2);
        Yaml yaml = new Yaml();
        String result = yaml.dump(l);
        assertEquals("[1, 2]\n", result);
    }

    public void testDumpListSameIntegers() {
        List<Integer> l = new ArrayList<Integer>(2);
        l.add(1);
        l.add(1);
        Yaml yaml = new Yaml();
        String result = yaml.dump(l);
        assertEquals("[1, 1]\n", result);
    }

    private List<Object> construct(String data) {
        return construct(new Constructor(), data);
    }

    @SuppressWarnings("unchecked")
    private List<Object> construct(Constructor constructor, String data) {
        StreamReader reader = new StreamReader(data);
        Parser parser = new ParserImpl(reader);
        Resolver resolver = new Resolver();
        Composer composer = new Composer(parser, resolver);
        constructor.setComposer(composer);
        List<Object> result = (List<Object>) constructor.getSingleData(Object.class);
        return result;
    }

    class CustomConstructor extends Constructor {
        @Override
        protected List<Object> createDefaultList(int initSize) {
            return new ArrayList<Object>(initSize);
        }
    }
}
