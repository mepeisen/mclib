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
package de.minigameslib.mclib.snakeyaml.partialconstruct;

import de.minigameslib.mclib.snakeyaml.composer.Composer;
import de.minigameslib.mclib.snakeyaml.nodes.MappingNode;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.NodeTuple;
import de.minigameslib.mclib.snakeyaml.nodes.ScalarNode;
import de.minigameslib.mclib.snakeyaml.parser.Parser;
import de.minigameslib.mclib.snakeyaml.resolver.Resolver;

class FragmentComposer extends Composer {
    String nodeName;

    public FragmentComposer(Parser parser, Resolver resolver, String nodeName) {
        super(parser, resolver);
        this.nodeName = nodeName;
    }

    @Override
    public Node getSingleNode() {
        Node node = super.getSingleNode();
        if (!MappingNode.class.isAssignableFrom(node.getClass())) {
            throw new RuntimeException(
                    "Document is not structured as expected.  Root element should be a map!");
        }
        MappingNode root = (MappingNode) node;
        for (NodeTuple tuple : root.getValue()) {
            Node keyNode = tuple.getKeyNode();
            if (ScalarNode.class.isAssignableFrom(keyNode.getClass())) {
                if (((ScalarNode) keyNode).getValue().equals(nodeName)) {
                    return tuple.getValueNode();
                }
            }
        }
        throw new RuntimeException("Did not find key \"" + nodeName + "\" in document-level map");
    }
}