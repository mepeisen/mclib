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

package de.minigameslib.mclib.test.item;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.minigameslib.mclib.api.items.BlockDropRuleInterface.DropSelf;
import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockVariantId;

/**
 * Tests for {@link DropSelf}.
 * 
 * @author mepeisen
 *
 */
public class DropSelfTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMe()
    {
        final DropSelf self = new DropSelf();
        
        assertEquals(Blocks.Block1, self.getDropType(Blocks.Block1, null, null, 0));
        assertEquals(BlockVariants.Block1, self.getDropVariant(Blocks.Block1, BlockVariants.Block1));
        
        assertEquals(1, self.getDropCount(null, 0));
        assertEquals(0, self.getExpDrop(Blocks.Block1, BlockVariants.Block1, null, 0));
    }
    
    /**
     * my blocks.
     */
    private enum Blocks implements BlockId
    {
        
        /** blocks. */
        Block1,
        /** blocks. */
        Block2,
        /** blocks. */
        Block3,
        /** blocks. */
        Block4,
        /** blocks. */
        Block5,
        /** blocks. */
        Block6,
        /** blocks. */
        Block7,
        /** blocks. */
        Block8,
        /** blocks. */
        Block9
        
    }
    
    /**
     * my blocks.
     */
    private enum BlockVariants implements BlockVariantId
    {
        
        /** blocks. */
        Block1,
        /** blocks. */
        Block2,
        /** blocks. */
        Block3,
        /** blocks. */
        Block4,
        /** blocks. */
        Block5,
        /** blocks. */
        Block6,
        /** blocks. */
        Block7,
        /** blocks. */
        Block8,
        /** blocks. */
        Block9
        
    }
    
}
