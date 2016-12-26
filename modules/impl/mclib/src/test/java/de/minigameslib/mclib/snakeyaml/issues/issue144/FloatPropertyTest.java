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
package de.minigameslib.mclib.snakeyaml.issues.issue144;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.extensions.compactnotation.CompactConstructor;
import junit.framework.TestCase;

public class FloatPropertyTest extends TestCase {

    public void testFloatAsJavaBeanProperty() throws Exception {
        BeanData bean = new BeanData();
        bean.setId("id1");
        bean.setNumber(3.5f);
        Yaml yaml = new Yaml();
        String txt = yaml.dump(bean);
        BeanData parsed = yaml.loadAs(txt, BeanData.class);
        assertEquals(3.5f, parsed.getNumber());
    }

    public void testCompact() {
        Yaml yaml = new Yaml(new CompactConstructor());
        BeanData obj = (BeanData) yaml
                .load("de.minigameslib.mclib.snakeyaml.issues.issue144.BeanData(id): { number: 123.4 }");
        assertEquals(123.4f, obj.getNumber());
    }

}