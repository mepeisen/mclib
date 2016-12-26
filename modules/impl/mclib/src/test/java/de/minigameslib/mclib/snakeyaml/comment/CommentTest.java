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
package de.minigameslib.mclib.snakeyaml.comment;

import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import de.minigameslib.mclib.snakeyaml.Yaml;
import de.minigameslib.mclib.snakeyaml.constructor.Construct;
import de.minigameslib.mclib.snakeyaml.constructor.Constructor;
import de.minigameslib.mclib.snakeyaml.error.YAMLException;
import de.minigameslib.mclib.snakeyaml.nodes.MappingNode;
import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.Tag;
import junit.framework.TestCase;

public class CommentTest extends TestCase {

	/** the yaml string to test. */
	private static final String YAML_STRING =
			"# this is a top level comment.\n"
			+ "foo:\n"
			+ "# some nested comment\n"
			+ "  bar: 123\n";
    
	/**
	 * Tests the parsing and returning of comments is not influencing the return value.
	 */
	public static void testCommentParsingIgnored()
	{
		final Yaml yml = new Yaml();
		
		// defaults to true
		assertTrue(yml.isSkipComments());
		
		// change to false
		yml.setSkipComments(false);
		assertFalse(yml.isSkipComments());
		
		final Object obj = yml.load(YAML_STRING);
		assertTrue(obj instanceof Map);
		final Object foo = ((Map<Object, Object>)obj).get("foo");
		assertTrue(foo instanceof Map);
		final Object bar = ((Map<Object, Object>)foo).get("bar");
		assertTrue(bar instanceof Integer);
		assertEquals(123, ((Integer)bar).intValue());
	}
    
	/**
	 * Tests the parsing and returning of comments caugth by custom constructor.
	 */
	public static void testCommentParsing()
	{
		final Yaml yml = new Yaml(new MyConstructor());
		yml.setSkipComments(false);
		
		final Object obj = yml.load(YAML_STRING);
		assertTrue(obj instanceof MyCommentMap);
		final Object foo = ((MyCommentMap)obj).get("foo");
		assertTrue(foo instanceof MyCommentMap);
		final Object bar = ((MyCommentMap)foo).get("bar");
		assertTrue(bar instanceof Integer);
		assertEquals(123, ((Integer)bar).intValue());
		
		assertArrayEquals(new String[]{"# this is a top level comment."}, ((MyCommentMap)obj).mapLevelComments);
		assertArrayEquals(new String[]{"# some nested comment"}, ((MyCommentMap)foo).mapLevelComments);
	}
	
	private static final class MyConstructor extends Constructor
	{

		/**
		 * Constructor
		 */
		public MyConstructor()
		{
			super();
	        this.yamlConstructors.put(Tag.MAP, new MyMapConstruct());
		}

		@Override
		protected MyCommentMap createDefaultMap() {
			return new MyCommentMap();
		}

		public class MyMapConstruct implements Construct {
	        public Object construct(Node node) {
	        	MyCommentMap result = null;
	            if (node.isTwoStepsConstruction()) {
	                result = createDefaultMap();
	            } else {
	                result = (MyCommentMap) constructMapping((MappingNode) node);
	            }
	            if (node.getPreComments() != null && node.getPreComments().size() > 0)
	            {
	            	result.mapLevelComments = node.getPreComments().toArray(new String[node.getPreComments().size()]);
	            }
	            return result;
	        }

	        @SuppressWarnings("unchecked")
	        public void construct2ndStep(Node node, Object object) {
	            if (node.isTwoStepsConstruction()) {
	                constructMapping2ndStep((MappingNode) node, (Map<Object, Object>) object);
	            } else {
	                throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
	            }
	        }
	    }
		
	}
	
	/**
	 * Sample class for saving comments.
	 */
	private static final class MyCommentMap extends LinkedHashMap<Object, Object>
	{
		
		public String[] mapLevelComments;
		
	}
	
}
