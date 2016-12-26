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
package de.minigameslib.mclib.snakeyaml.tokens;

import de.minigameslib.mclib.snakeyaml.error.Mark;
import de.minigameslib.mclib.snakeyaml.tokens.Token.ID;
import junit.framework.TestCase;

public class FlowMappingStartTokenTest extends TestCase {

    public void testGetTokenId() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        FlowMappingStartToken token = new FlowMappingStartToken(mark, mark);
        assertEquals(ID.FlowMappingStart, token.getTokenId());
    }
}
