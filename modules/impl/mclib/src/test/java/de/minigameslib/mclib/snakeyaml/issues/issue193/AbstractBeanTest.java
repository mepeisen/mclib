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
package de.minigameslib.mclib.snakeyaml.issues.issue193;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.constructor.Constructor;
import de.minigameslib.mclib.snakeyaml.introspector.BeanAccess;
import de.minigameslib.mclib.snakeyaml.introspector.PropertyUtils;
import de.minigameslib.mclib.snakeyaml.representer.Representer;
import junit.framework.TestCase;

public class AbstractBeanTest extends TestCase {

    public void testErrorMessage() throws Exception {

        BeanA1 b = new BeanA1();
        b.setId(2l);
        b.setName("name1");

        Constructor c = new Constructor();
        Representer r = new Representer();

        PropertyUtils pu = new PropertyUtils();
        c.setPropertyUtils(pu);
        r.setPropertyUtils(pu);

        pu.getProperties(BeanA1.class, BeanAccess.FIELD);

        Yaml yaml = new Yaml(c, r);
        // yaml.setBeanAccess(BeanAccess.FIELD);
        String dump = yaml.dump(b);
        BeanA1 b2 = (BeanA1) yaml.load(dump);
        assertEquals(b.getId(), b2.getId());
        assertEquals(b.getName(), b2.getName());
    }
}
