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
package de.minigameslib.mclib.snakeyaml.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.minigameslib.mclib.snakeyaml.DumperOptions;
import de.minigameslib.mclib.snakeyaml.DumperOptions.Version;
import de.minigameslib.mclib.snakeyaml.emitter.Emitable;
import de.minigameslib.mclib.snakeyaml.events.AliasEvent;
import de.minigameslib.mclib.snakeyaml.events.DocumentEndEvent;
import de.minigameslib.mclib.snakeyaml.events.DocumentStartEvent;
import de.minigameslib.mclib.snakeyaml.events.ImplicitTuple;
import de.minigameslib.mclib.snakeyaml.events.MappingEndEvent;
import de.minigameslib.mclib.snakeyaml.events.MappingStartEvent;
import de.minigameslib.mclib.snakeyaml.events.ScalarEvent;
import de.minigameslib.mclib.snakeyaml.events.SequenceEndEvent;
import de.minigameslib.mclib.snakeyaml.events.SequenceStartEvent;
import de.minigameslib.mclib.snakeyaml.events.StreamEndEvent;
import de.minigameslib.mclib.snakeyaml.events.StreamStartEvent;
import de.minigameslib.mclib.snakeyaml.nodes.AnchorNode;
import de.minigameslib.mclib.snakeyaml.nodes.CollectionNode;
import de.minigameslib.mclib.snakeyaml.nodes.MappingNode;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.NodeId;
import de.minigameslib.mclib.snakeyaml.nodes.NodeTuple;
import de.minigameslib.mclib.snakeyaml.nodes.ScalarNode;
import de.minigameslib.mclib.snakeyaml.nodes.SequenceNode;
import de.minigameslib.mclib.snakeyaml.nodes.Tag;
import de.minigameslib.mclib.snakeyaml.resolver.Resolver;

public final class Serializer {
    private final Emitable emitter;
    private final Resolver resolver;
    private boolean explicitStart;
    private boolean explicitEnd;
    private Version useVersion;
    private Map<String, String> useTags;
    private Set<Node> serializedNodes;
    private Map<Node, String> anchors;
    private AnchorGenerator anchorGenerator;
    private Boolean closed;
    private Tag explicitRoot;

    public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
        this.emitter = emitter;
        this.resolver = resolver;
        this.explicitStart = opts.isExplicitStart();
        this.explicitEnd = opts.isExplicitEnd();
        if (opts.getVersion() != null) {
            this.useVersion = opts.getVersion();
        }
        this.useTags = opts.getTags();
        this.serializedNodes = new HashSet<Node>();
        this.anchors = new HashMap<Node, String>();
        this.anchorGenerator = opts.getAnchorGenerator();
        this.closed = null;
        this.explicitRoot = rootTag;
    }

    public void open() throws IOException {
        if (closed == null) {
            this.emitter.emit(new StreamStartEvent(null, null));
            this.closed = Boolean.FALSE;
        } else if (Boolean.TRUE.equals(closed)) {
            throw new SerializerException("serializer is closed");
        } else {
            throw new SerializerException("serializer is already opened");
        }
    }

    public void close() throws IOException {
        if (closed == null) {
            throw new SerializerException("serializer is not opened");
        } else if (!Boolean.TRUE.equals(closed)) {
            this.emitter.emit(new StreamEndEvent(null, null));
            this.closed = Boolean.TRUE;
        }
    }

    public void serialize(Node node) throws IOException {
        if (closed == null) {
            throw new SerializerException("serializer is not opened");
        } else if (closed) {
            throw new SerializerException("serializer is closed");
        }
        this.emitter.emit(new DocumentStartEvent(null, null, this.explicitStart, this.useVersion,
                useTags));
        anchorNode(node);
        if (explicitRoot != null) {
            node.setTag(explicitRoot);
        }
        serializeNode(node, null);
        this.emitter.emit(new DocumentEndEvent(null, null, this.explicitEnd));
        this.serializedNodes.clear();
        this.anchors.clear();
    }

    private void anchorNode(Node node2) {
        Node node = node2;
        if (node.getNodeId() == NodeId.anchor) {
            node = ((AnchorNode) node).getRealNode();
        }
        if (this.anchors.containsKey(node)) {
            String anchor = this.anchors.get(node);
            if (null == anchor) {
                anchor = this.anchorGenerator.nextAnchor(node);
                this.anchors.put(node, anchor);
            }
        } else {
            this.anchors.put(node, null);
            switch (node.getNodeId()) {
            case sequence:
                SequenceNode seqNode = (SequenceNode) node;
                List<Node> list = seqNode.getValue();
                for (Node item : list) {
                    anchorNode(item);
                }
                break;
            case mapping:
                MappingNode mnode = (MappingNode) node;
                List<NodeTuple> map = mnode.getValue();
                for (NodeTuple object : map) {
                    Node key = object.getKeyNode();
                    Node value = object.getValueNode();
                    anchorNode(key);
                    anchorNode(value);
                }
                break;
            }
        }
    }

    private void serializeNode(Node node2, Node parent) throws IOException {
        Node node = node2;
        if (node.getNodeId() == NodeId.anchor) {
            node = ((AnchorNode) node).getRealNode();
        }
        String tAlias = this.anchors.get(node);
        if (this.serializedNodes.contains(node)) {
            this.emitter.emit(new AliasEvent(tAlias, null, null));
        } else {
            this.serializedNodes.add(node);
            switch (node.getNodeId()) {
            case scalar:
                ScalarNode scalarNode = (ScalarNode) node;
                Tag detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
                Tag defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
                ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node
                        .getTag().equals(defaultTag));
                ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple,
                        scalarNode.getValue(), null, null, scalarNode.getStyle());
                this.emitter.emit(event);
                break;
            case sequence:
                SequenceNode seqNode = (SequenceNode) node;
                boolean implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence,
                        null, true));
                this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(),
                        implicitS, null, null, seqNode.getFlowStyle()));
                List<Node> list = seqNode.getValue();
                for (Node item : list) {
                    serializeNode(item, node);
                }
                this.emitter.emit(new SequenceEndEvent(null, null));
                break;
            default:// instance of MappingNode
                Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
                boolean implicitM = node.getTag().equals(implicitTag);
                this.emitter.emit(new MappingStartEvent(tAlias, node.getTag().getValue(),
                        implicitM, null, null, ((CollectionNode) node).getFlowStyle()));
                MappingNode mnode = (MappingNode) node;
                List<NodeTuple> map = mnode.getValue();
                for (NodeTuple row : map) {
                    Node key = row.getKeyNode();
                    Node value = row.getValueNode();
                    serializeNode(key, mnode);
                    serializeNode(value, mnode);
                }
                this.emitter.emit(new MappingEndEvent(null, null));
            }
        }
    }
}
