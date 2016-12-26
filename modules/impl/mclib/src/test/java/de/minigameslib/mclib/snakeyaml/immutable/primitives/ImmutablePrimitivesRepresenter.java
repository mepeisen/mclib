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
package de.minigameslib.mclib.snakeyaml.immutable.primitives;

import java.util.Arrays;

import de.minigameslib.mclib.snakeyaml.nodes.Node;
import de.minigameslib.mclib.snakeyaml.nodes.Tag;
import de.minigameslib.mclib.snakeyaml.representer.Represent;
import de.minigameslib.mclib.snakeyaml.representer.Representer;

public class ImmutablePrimitivesRepresenter extends Representer {
    public ImmutablePrimitivesRepresenter() {
        super();
        this.representers.put(BunchOfPrimitives.class, new RepresentPrimitives());
    }

    class RepresentPrimitives implements Represent {
        public Node representData(Object data) {
            BunchOfPrimitives bunch = (BunchOfPrimitives) data;
            return representSequence(
                    getTag(data.getClass(), new Tag(data.getClass())),
                    Arrays.asList(new Object[] { bunch.getPrimitiveInt(),
                            bunch.getPrimitiveDouble(), bunch.primitiveBoolean }), true);
        }
    }
}
