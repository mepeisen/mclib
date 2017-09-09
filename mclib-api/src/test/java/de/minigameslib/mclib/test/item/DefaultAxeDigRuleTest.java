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
import static org.junit.Assert.assertFalse;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.Material;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
import de.minigameslib.mclib.api.items.DefaultAxeDigRule;

/**
 * Tests for {@link DefaultAxeDigRule}.
 * 
 * @author mepeisen
 *
 */
public class DefaultAxeDigRuleTest
{
    
    /** 
     * test me.
     * @throws ClassNotFoundException thrown on problems.
     */
    @Test
    public void testGetSpeed() throws ClassNotFoundException
    {
        final BlockServiceInterface bsi = mock(BlockServiceInterface.class);
        when(bsi.isWood(Material.WOOD)).thenReturn(Boolean.TRUE);
        when(bsi.isPlant(Material.DOUBLE_PLANT)).thenReturn(Boolean.TRUE);
        when(bsi.isVine(Material.VINE)).thenReturn(Boolean.TRUE);
        when(bsi.isWood(Blocks.Block1)).thenReturn(Boolean.TRUE);
        when(bsi.isPlant(Blocks.Block2)).thenReturn(Boolean.TRUE);
        when(bsi.isVine(Blocks.Block3)).thenReturn(Boolean.TRUE);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.items.BlockServiceCache"), "SERVICES", bsi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final DigRule rule = new DigRule();
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.WOOD), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.DOUBLE_PLANT), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.VINE), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.LOG), 0);
        assertEquals(1.0f, rule.getHarvestSpeed(null, Material.ANVIL), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Blocks.Block1, null), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Blocks.Block2, null), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Blocks.Block3, null), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Blocks.Block4, null), 0);
        assertEquals(1.0f, rule.getHarvestSpeed(null, Blocks.Block5, null), 0);
    }
    
    /** 
     * test me.
     */
    @Test
    public void testGetDmgByBlock()
    {
        assertEquals(2, new DigRule().getDamageByBlock(null, null, null));
    }
    
    /** 
     * test me.
     * @throws ClassNotFoundException thrown on problems.
     */
    @Test
    public void testCanHarvest() throws ClassNotFoundException
    {
        final DigRule rule = new DigRule();
        assertFalse(rule.canHarvest(Material.IRON_ORE));
        assertFalse(rule.canHarvest(Blocks.Block1, null));
    }
    
    /**
     * custom dig rule.
     * @author mepeisen
     *
     */
    private static final class DigRule extends DefaultAxeDigRule
    {
        
        /**
         * Constructor.
         */
        public DigRule()
        {
            super();
            this.efficientBlocks.add(Blocks.Block4);
        }
        
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
    
}
