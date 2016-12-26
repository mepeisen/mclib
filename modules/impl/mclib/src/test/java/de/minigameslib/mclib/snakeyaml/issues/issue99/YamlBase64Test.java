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
package de.minigameslib.mclib.snakeyaml.issues.issue99;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import de.minigameslib.mclib.snakeyaml.Util;
import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.YamlDocument;
import de.minigameslib.mclib.snakeyaml.constructor.AbstractConstruct;
import de.minigameslib.mclib.snakeyaml.constructor.Constructor;
import de.minigameslib.mclib.snakeyaml.external.biz.base64Coder.Base64Coder;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.ScalarNode;
import de.minigameslib.mclib.snakeyaml.nodes.Tag;
import junit.framework.TestCase;

/**
 * Example for issue 99
 * 
 * @see <a href="http://code.google.com/p/snakeyaml/issues/detail?id=99"></a>
 */
public class YamlBase64Test extends TestCase {

    /**
     * test base64 decoding
     */
    public void testBase64() throws IOException {
        String text = Util.getLocalResource("issues/issue99-base64_literal.yaml");
        String[] lines = text.split("\n");
        String all = "";
        for (int i = 1; i < lines.length; i++) {// skip first line
            all = all + lines[i].trim();
        }
        // System.out.println(all);
        byte[] decoded = Base64Coder.decode(all.toCharArray());
        assertEquals(3737, decoded.length);
        checkBytes(decoded);
    }

    @SuppressWarnings("unchecked")
    public void testYamlBase64Loading() throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlBase64Test.class
                .getResourceAsStream("/issues/issue99-base64_double_quoted.yaml");
        Map<String, Object> bean = (Map<String, Object>) yaml.load(inputStream);
        byte[] jpeg = (byte[]) bean.get("jpegPhoto");
        checkBytes(jpeg);
        inputStream.close();
    }

    private void checkBytes(byte[] jpeg) throws IOException {
        InputStream input;
        input = YamlDocument.class.getClassLoader().getResourceAsStream("issues/issue99.jpeg");
        BufferedInputStream is = new BufferedInputStream(input);
        int i = 0;
        while (i < jpeg.length) {
            int etalon = is.read();
            if (jpeg[i] < 0) {
                assertEquals(etalon, jpeg[i] + 256);
            } else {
                assertEquals(etalon, jpeg[i]);
            }
            i++;
        }
        is.close();
    }

    /**
     * In the literal scalar all the line breaks are significant
     * 
     * @throws IOException
     */
    public void testYamlBase64LoadingLiteral() throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlBase64Test.class
                .getResourceAsStream("/issues/issue99-base64_literal.yaml");
        Map<String, Object> bean = (Map<String, Object>) yaml.load(inputStream);
        byte[] jpeg = (byte[]) bean.get("jpegPhoto");
        checkBytes(jpeg);
        inputStream.close();
    }

    /**
     * Redefine the !!binary global tag in a way that it ignores all the white
     * spaces to be able to use literal scalar
     */
    @SuppressWarnings("unchecked")
    public void testRedefineBinaryTag() throws IOException {
        Yaml yaml = new Yaml(new SpecialContructor(Tag.BINARY));
        InputStream inputStream = YamlBase64Test.class
                .getResourceAsStream("/issues/issue99-base64_literal.yaml");
        Map<String, Object> bean = (Map<String, Object>) yaml.load(inputStream);
        byte[] jpeg = (byte[]) bean.get("jpegPhoto");
        checkBytes(jpeg);
        inputStream.close();
    }

    private class SpecialContructor extends Constructor {
        public SpecialContructor(Tag tag) {
            this.yamlConstructors.put(tag, new MyBinaryConstructor());
        }

        private class MyBinaryConstructor extends AbstractConstruct {
            public Object construct(Node node) {
                String contentWithNewLines = constructScalar((ScalarNode) node).toString();
                String noNewLines = contentWithNewLines.replaceAll("\\s", "");
                byte[] decoded = Base64Coder.decode(noNewLines.toCharArray());
                return decoded;
            }
        }
    }

    /**
     * Define a local tag to ignore all the white spaces to be able to use
     * literal scalar
     */
    @SuppressWarnings("unchecked")
    public void testLocalBinaryTag() throws IOException {
        Yaml yaml = new Yaml(new SpecialContructor(new Tag("!beautiful")));
        InputStream inputStream = YamlBase64Test.class
                .getResourceAsStream("/issues/issue99-base64_literal_custom_tag.yaml");
        Map<String, Object> bean = (Map<String, Object>) yaml.load(inputStream);
        byte[] jpeg = (byte[]) bean.get("jpegPhoto");
        checkBytes(jpeg);
        inputStream.close();
    }
}
