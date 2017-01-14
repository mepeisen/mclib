/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mclib.impl.yml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.minigameslib.mclib.snakeyaml.DumperOptions.FlowStyle;
import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.constructor.Construct;
import de.minigameslib.mclib.snakeyaml.constructor.Constructor;
import de.minigameslib.mclib.snakeyaml.error.YAMLException;
import de.minigameslib.mclib.snakeyaml.nodes.MappingNode;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.NodeTuple;
import de.minigameslib.mclib.snakeyaml.nodes.ScalarNode;
import de.minigameslib.mclib.snakeyaml.nodes.Tag;
import de.minigameslib.mclib.snakeyaml.representer.Represent;
import de.minigameslib.mclib.snakeyaml.representer.Representer;

/**
 * File config.
 * 
 * @author mepeisen
 */
public class YmlFile extends YmlCommentableSection
{
    
    /** yaml. */
    private final Yaml yml = new Yaml(new MyConstructor(), new MyRepresenter());
    
    /**
     * Constructor to create an empty root section.
     */
    public YmlFile()
    {
        this.yml.setSkipComments(false);
    }
    
    /**
     * Constructor to read from existing file.
     * 
     * @param file
     * @throws IOException
     */
    public YmlFile(File file) throws IOException
    {
        this();
        try (final FileReader io = new FileReader(file))
        {
            this.load(io);
        }
    }
    
    /**
     * Reads a file into this config.
     * 
     * @param fr
     * @throws IOException
     */
    public void load(FileReader fr) throws IOException
    {
        final MyCommentMap map = (MyCommentMap) this.yml.load(fr);
        this.load(map);
    }
    
    /**
     * Saves config to file.
     * 
     * @param file
     * @throws IOException
     */
    public void saveFile(File file) throws IOException
    {
        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        try (final FileWriter fow = new FileWriter(file))
        {
            final MyCommentMap data = new MyCommentMap();
            this.store(data);
            this.yml.dump(data, fow);
        }
    }
    
    /**
     * Customized constructor for analyzing the yaml nodes.
     * 
     * @author mepeisen
     */
    private static final class MyConstructor extends Constructor
    {
        
        /**
         * Constructor
         */
        public MyConstructor()
        {
            this.yamlConstructors.put(Tag.MAP, new MyMapConstruct());
        }
        
        @Override
        protected MyCommentMap createDefaultMap()
        {
            return new MyCommentMap();
        }
        
        @Override
        protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping)
        {
            super.constructMapping2ndStep(node, mapping);
            
            List<NodeTuple> nodeValue = node.getValue();
            for (NodeTuple tuple : nodeValue)
            {
                Node keyNode = tuple.getKeyNode();
                Object key = constructObject(keyNode);
                if (keyNode.getPreComments() != null && !keyNode.getPreComments().isEmpty())
                {
                    if (keyNode.isTwoStepsConstruction())
                    {
                        throw new YAMLException("Comments on 2steps-construction not yet supported."); //$NON-NLS-1$
                    }
                    if (((MyCommentMap)mapping).keyComments == null)
                    {
                        ((MyCommentMap)mapping).keyComments = new HashMap<>();
                    }
                    ((MyCommentMap) mapping).keyComments.put(key, keyNode.getPreComments().toArray(new String[keyNode.getPreComments().size()]));
                }
            }
        }
        
        /**
         * Constructor for maps.
         * 
         * @author mepeisen
         */
        public class MyMapConstruct implements Construct
        {
            
            @SuppressWarnings("synthetic-access")
            @Override
            public Object construct(Node node)
            {
                MyCommentMap result = null;
                if (node.isTwoStepsConstruction())
                {
                    result = createDefaultMap();
                }
                else
                {
                    result = (MyCommentMap) constructMapping((MappingNode) node);
                }
                if (node.getPreComments() != null && node.getPreComments().size() > 0)
                {
                    result.mapLevelComments = node.getPreComments().toArray(new String[node.getPreComments().size()]);
                }
                return result;
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public void construct2ndStep(Node node, Object object)
            {
                if (node.isTwoStepsConstruction())
                {
                    constructMapping2ndStep((MappingNode) node, (Map<Object, Object>) object);
                }
                else
                {
                    throw new YAMLException("Unexpected recursive mapping structure. Node: " + node); //$NON-NLS-1$
                }
            }
        }
        
    }
    
    /**
     * Representer to build yaml with comments.
     * @author mepeisen
     */
    private static final class MyRepresenter extends Representer
    {
        
        /**
         * Constructor.
         */
        public MyRepresenter()
        {
            this.representers.put(MyCommentMap.class, new RepresentCommentMap());
        }
        
        /**
         * Helper to represent the comment map.
         */
        public class RepresentCommentMap implements Represent
        {
            
            @Override
            public Node representData(Object data)
            {
                MyCommentMap map = (MyCommentMap) data;
                final MappingNode mappingNode = (MappingNode) representMapping(Tag.MAP, map, Boolean.FALSE);
                if (map.mapLevelComments != null)
                {
                    mappingNode.setPreComments(Arrays.asList(map.mapLevelComments));
                }
                return mappingNode;
            }
        }
        
        @Override
        protected Node representMapping(Tag tag, Map<?, ?> mapping, Boolean flowStyle)
        {
            List<NodeTuple> value = new ArrayList<>(mapping.size());
            MappingNode node = new MappingNode(tag, value, flowStyle);
            this.representedObjects.put(this.objectToRepresent, node);
            boolean bestStyle = true;
            for (Map.Entry<?, ?> entry : mapping.entrySet())
            {
                Node nodeKey = representData(entry.getKey());
                Node nodeValue = representData(entry.getValue());
                if (!(nodeKey instanceof ScalarNode && ((ScalarNode) nodeKey).getStyle() == null))
                {
                    bestStyle = false;
                }
                if (!(nodeValue instanceof ScalarNode && ((ScalarNode) nodeValue).getStyle() == null))
                {
                    bestStyle = false;
                }
                value.add(new NodeTuple(nodeKey, nodeValue));
                
                if (mapping instanceof MyCommentMap && ((MyCommentMap) mapping).keyComments != null)
                {
                    final String[] comment = ((MyCommentMap) mapping).keyComments.get(entry.getKey());
                    if (comment != null)
                    {
                        nodeKey.setPreComments(Arrays.asList(comment));
                    }
                }
            }
            if (flowStyle == null)
            {
                if (this.defaultFlowStyle != FlowStyle.AUTO)
                {
                    node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
                }
                else
                {
                    node.setFlowStyle(bestStyle);
                }
            }
            return node;
        }
        
    }
    
    /**
     * Sample class for saving comments.
     */
    protected static final class MyCommentMap extends LinkedHashMap<Object, Object>
    {
        /** the comments preceding the map. */
        public String[]              mapLevelComments;
        
        /** the comments on key level. */
        public Map<Object, String[]> keyComments;
        
        /**
         * serial version uid.
         */
        private static final long    serialVersionUID = -8759792473880471350L;
        
        /**
         * Constructor
         */
        public MyCommentMap()
        {
            // empty
        }
        
    }
    
}
