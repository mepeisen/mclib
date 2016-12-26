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
package de.minigameslib.mclib.snakeyaml.issues.issue73;

import java.util.Set;
import java.util.TreeSet;

import de.minigameslib.mclib.snakeyaml.DumperOptions;
import de.minigameslib.mclib.snakeyaml.DumperOptions.FlowStyle;
import de.minigameslib.mclib.snakeyaml.Util;
import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.introspector.BeanAccess;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.Tag;
import de.minigameslib.mclib.snakeyaml.representer.Represent;
import de.minigameslib.mclib.snakeyaml.representer.Representer;
import junit.framework.TestCase;

public class DumpSetAsSequenceExampleTest extends TestCase {

    public void testDumpFlow() {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(new SetRepresenter(), options);
        String output = yaml.dump(createBlog());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue73-dump7.txt"), output);
        //
        check(output);
    }

    public void testDumpBlock() {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(new SetRepresenter(), options);
        String output = yaml.dump(createBlog());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue73-dump8.txt"), output);
        //
        check(output);
    }

    private class SetRepresenter extends Representer {
        public SetRepresenter() {
            this.multiRepresenters.put(Set.class, new RepresentIterable());
        }

        private class RepresentIterable implements Represent {
            @SuppressWarnings("unchecked")
            public Node representData(Object data) {
                return representSequence(getTag(data.getClass(), Tag.SEQ), (Iterable<Object>) data,
                        null);

            }
        }
    }

    private Blog createBlog() {
        Blog blog = new Blog("Test Me!");
        blog.addPost(new Post("Title1", "text 1"));
        blog.addPost(new Post("Title2", "text text 2"));
        blog.numbers.add(19);
        blog.numbers.add(17);
        TreeSet<String> labels = new TreeSet<String>();
        labels.add("Java");
        labels.add("YAML");
        labels.add("SnakeYAML");
        blog.setLabels(labels);
        return blog;
    }

    private void check(String doc) {
        Yaml yamlLoader = new Yaml();
        yamlLoader.setBeanAccess(BeanAccess.FIELD);
        Blog blog = (Blog) yamlLoader.load(doc);
        assertEquals("Test Me!", blog.getName());
        assertEquals(2, blog.numbers.size());
        assertEquals(2, blog.getPosts().size());
        for (Post post : blog.getPosts()) {
            assertEquals(Post.class, post.getClass());
        }
    }
}
