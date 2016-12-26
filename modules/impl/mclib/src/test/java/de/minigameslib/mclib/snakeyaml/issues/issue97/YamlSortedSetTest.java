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
package de.minigameslib.mclib.snakeyaml.issues.issue97;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.constructor.Constructor;
import de.minigameslib.mclib.snakeyaml.error.YAMLException;
import de.minigameslib.mclib.snakeyaml.introspector.BeanAccess;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.NodeId;
import de.minigameslib.mclib.snakeyaml.nodes.SequenceNode;

public class YamlSortedSetTest {

    @Test
    public void testYaml() {
        String serialized = "!!de.minigameslib.mclib.snakeyaml.issues.issue97.Blog\n" + "posts:\n"
                + "  - text: Dummy\n" + "    title: Test\n" + "  - text: Creative\n"
                + "    title: Highly\n";
        // System.out.println(serialized);
        Yaml yaml2 = constructYamlParser();
        Blog rehydrated = (Blog) yaml2.load(serialized);
        checkTestBlog(rehydrated);
    }

    protected Yaml constructYamlParser() {
        Yaml yaml = new Yaml(new SetContructor());
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml;
    }

    protected void checkTestBlog(Blog blog) {
        Set<Post> posts = blog.getPosts();
        Assert.assertEquals("Blog contains 2 posts", 2, posts.size());
    }

    private class SetContructor extends Constructor {
        public SetContructor() {
            yamlClassConstructors.put(NodeId.sequence, new ConstructSetFromSequence());
        }

        private class ConstructSetFromSequence extends ConstructSequence {
            @Override
            public Object construct(Node node) {
                if (SortedSet.class.isAssignableFrom(node.getType())) {
                    if (node.isTwoStepsConstruction()) {
                        throw new YAMLException("Set cannot be recursive.");
                    } else {
                        Collection<Object> result = new TreeSet<Object>();
                        SetContructor.this.constructSequenceStep2((SequenceNode) node, result);
                        return result;
                    }
                } else {
                    return super.construct(node);
                }
            }
        }
    }
}
