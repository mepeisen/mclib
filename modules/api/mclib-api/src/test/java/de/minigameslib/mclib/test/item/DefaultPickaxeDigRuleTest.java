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
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.Material;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
import de.minigameslib.mclib.api.items.DefaultPickaxeDigRule;

/**
 * Tests for {@link DefaultPickaxeDigRule}.
 * 
 * @author mepeisen
 *
 */
public class DefaultPickaxeDigRuleTest
{
    
    /** 
     * test me.
     * @throws ClassNotFoundException thrown on problems.
     */
    @Test
    public void testGetSpeed() throws ClassNotFoundException
    {
        final BlockServiceInterface bsi = mock(BlockServiceInterface.class);
        when(bsi.isRock(Material.STONE)).thenReturn(Boolean.TRUE);
        when(bsi.isOre(Material.IRON_ORE)).thenReturn(Boolean.TRUE);
        when(bsi.isHeavy(Material.ANVIL)).thenReturn(Boolean.TRUE);
        when(bsi.isRock(Blocks.Block1)).thenReturn(Boolean.TRUE);
        when(bsi.isOre(Blocks.Block2)).thenReturn(Boolean.TRUE);
        when(bsi.isHeavy(Blocks.Block3)).thenReturn(Boolean.TRUE);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.items.BlockServiceCache"), "SERVICES", bsi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final DigRule rule = new DigRule();
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.STONE), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.IRON_ORE), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.ANVIL), 0);
        assertEquals(4.0f, rule.getHarvestSpeed(null, Material.COAL_ORE), 0);
        assertEquals(1.0f, rule.getHarvestSpeed(null, Material.ACACIA_DOOR), 0);
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
        final BlockServiceInterface bsi = mock(BlockServiceInterface.class);
        when(bsi.isRock(Material.STONE)).thenReturn(Boolean.TRUE);
        when(bsi.isOre(Material.COAL_ORE)).thenReturn(Boolean.TRUE);
        when(bsi.isHeavy(Material.ANVIL)).thenReturn(Boolean.TRUE);
        when(bsi.isRock(Blocks.Block1)).thenReturn(Boolean.TRUE);
        when(bsi.isOre(Blocks.Block2)).thenReturn(Boolean.TRUE);
        when(bsi.isHeavy(Blocks.Block3)).thenReturn(Boolean.TRUE);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.items.BlockServiceCache"), "SERVICES", bsi); //$NON-NLS-1$ //$NON-NLS-2$
        
        // tool level 3
        final DigRule rule3 = new DigRule();
        assertTrue(rule3.canHarvest(Material.OBSIDIAN));
        
        assertTrue(rule3.canHarvest(Material.DIAMOND_BLOCK));
        assertTrue(rule3.canHarvest(Material.DIAMOND_ORE));
        assertTrue(rule3.canHarvest(Material.EMERALD_BLOCK));
        assertTrue(rule3.canHarvest(Material.EMERALD_ORE));
        assertTrue(rule3.canHarvest(Material.GOLD_BLOCK));
        assertTrue(rule3.canHarvest(Material.GOLD_ORE));
        assertTrue(rule3.canHarvest(Material.REDSTONE_BLOCK));
        assertTrue(rule3.canHarvest(Material.REDSTONE_ORE));

        assertTrue(rule3.canHarvest(Material.IRON_BLOCK));
        assertTrue(rule3.canHarvest(Material.IRON_ORE));
        assertTrue(rule3.canHarvest(Material.LAPIS_BLOCK));
        assertTrue(rule3.canHarvest(Material.LAPIS_ORE));
        
        assertTrue(rule3.canHarvest(Material.STONE));
        assertTrue(rule3.canHarvest(Material.COAL_ORE));
        assertTrue(rule3.canHarvest(Material.ANVIL));
        
        assertFalse(rule3.canHarvest(Material.APPLE));

        assertTrue(rule3.canHarvest(Blocks.Block1, null));
        assertTrue(rule3.canHarvest(Blocks.Block2, null));
        assertTrue(rule3.canHarvest(Blocks.Block3, null));
        
        assertFalse(rule3.canHarvest(Blocks.Block4, null));
        
        // tool level 2
        final DigRule rule2 = new DigRule(2);
        assertFalse(rule2.canHarvest(Material.OBSIDIAN));
        
        assertTrue(rule2.canHarvest(Material.DIAMOND_BLOCK));
        assertTrue(rule2.canHarvest(Material.DIAMOND_ORE));
        assertTrue(rule2.canHarvest(Material.EMERALD_BLOCK));
        assertTrue(rule2.canHarvest(Material.EMERALD_ORE));
        assertTrue(rule2.canHarvest(Material.GOLD_BLOCK));
        assertTrue(rule2.canHarvest(Material.GOLD_ORE));
        assertTrue(rule2.canHarvest(Material.REDSTONE_BLOCK));
        assertTrue(rule2.canHarvest(Material.REDSTONE_ORE));

        assertTrue(rule2.canHarvest(Material.IRON_BLOCK));
        assertTrue(rule2.canHarvest(Material.IRON_ORE));
        assertTrue(rule2.canHarvest(Material.LAPIS_BLOCK));
        assertTrue(rule2.canHarvest(Material.LAPIS_ORE));
        
        assertTrue(rule2.canHarvest(Material.STONE));
        assertTrue(rule2.canHarvest(Material.COAL_ORE));
        assertTrue(rule2.canHarvest(Material.ANVIL));
        
        assertFalse(rule2.canHarvest(Material.APPLE));

        assertTrue(rule2.canHarvest(Blocks.Block1, null));
        assertTrue(rule2.canHarvest(Blocks.Block2, null));
        assertTrue(rule2.canHarvest(Blocks.Block3, null));
        
        assertFalse(rule2.canHarvest(Blocks.Block4, null));
        
        // tool level 1
        final DigRule rule1 = new DigRule(1);
        assertFalse(rule1.canHarvest(Material.OBSIDIAN));
        
        assertFalse(rule1.canHarvest(Material.DIAMOND_BLOCK));
        assertFalse(rule1.canHarvest(Material.DIAMOND_ORE));
        assertFalse(rule1.canHarvest(Material.EMERALD_BLOCK));
        assertFalse(rule1.canHarvest(Material.EMERALD_ORE));
        assertFalse(rule1.canHarvest(Material.GOLD_BLOCK));
        assertFalse(rule1.canHarvest(Material.GOLD_ORE));
        assertFalse(rule1.canHarvest(Material.REDSTONE_BLOCK));
        assertFalse(rule1.canHarvest(Material.REDSTONE_ORE));

        assertTrue(rule1.canHarvest(Material.IRON_BLOCK));
        assertTrue(rule1.canHarvest(Material.IRON_ORE));
        assertTrue(rule1.canHarvest(Material.LAPIS_BLOCK));
        assertTrue(rule1.canHarvest(Material.LAPIS_ORE));
        
        assertTrue(rule1.canHarvest(Material.STONE));
        assertTrue(rule1.canHarvest(Material.COAL_ORE));
        assertTrue(rule1.canHarvest(Material.ANVIL));
        
        assertFalse(rule1.canHarvest(Material.APPLE));

        assertTrue(rule1.canHarvest(Blocks.Block1, null));
        assertTrue(rule1.canHarvest(Blocks.Block2, null));
        assertTrue(rule1.canHarvest(Blocks.Block3, null));
        
        assertFalse(rule1.canHarvest(Blocks.Block4, null));
        
        // tool level 0
        final DigRule rule0 = new DigRule(0);
        assertFalse(rule0.canHarvest(Material.OBSIDIAN));
        
        assertFalse(rule0.canHarvest(Material.DIAMOND_BLOCK));
        assertFalse(rule0.canHarvest(Material.DIAMOND_ORE));
        assertFalse(rule0.canHarvest(Material.EMERALD_BLOCK));
        assertFalse(rule0.canHarvest(Material.EMERALD_ORE));
        assertFalse(rule0.canHarvest(Material.GOLD_BLOCK));
        assertFalse(rule0.canHarvest(Material.GOLD_ORE));
        assertFalse(rule0.canHarvest(Material.REDSTONE_BLOCK));
        assertFalse(rule0.canHarvest(Material.REDSTONE_ORE));

        assertFalse(rule0.canHarvest(Material.IRON_BLOCK));
        assertFalse(rule0.canHarvest(Material.IRON_ORE));
        assertFalse(rule0.canHarvest(Material.LAPIS_BLOCK));
        assertFalse(rule0.canHarvest(Material.LAPIS_ORE));
        
        assertTrue(rule0.canHarvest(Material.STONE));
        assertTrue(rule0.canHarvest(Material.COAL_ORE));
        assertTrue(rule0.canHarvest(Material.ANVIL));
        
        assertFalse(rule0.canHarvest(Material.APPLE));

        assertTrue(rule0.canHarvest(Blocks.Block1, null));
        assertTrue(rule0.canHarvest(Blocks.Block2, null));
        assertTrue(rule0.canHarvest(Blocks.Block3, null));
        
        assertFalse(rule0.canHarvest(Blocks.Block4, null));
    }
    
    /**
     * custom dig rule.
     * @author mepeisen
     *
     */
    private static final class DigRule extends DefaultPickaxeDigRule
    {
        
        /**
         * Constructor.
         */
        public DigRule()
        {
            super();
            this.efficientBlocks.add(Blocks.Block4);
        }
        
        /**
         * Constructor.
         * @param toolLevel tool level
         */
        public DigRule(int toolLevel)
        {
            super();
            this.efficientBlocks.add(Blocks.Block4);
            this.toolLevel = toolLevel;
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
