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
import de.minigameslib.mclib.api.items.DefaultSwordDigRule;

/**
 * Tests for {@link DefaultSwordDigRule}.
 * 
 * @author mepeisen
 *
 */
public class DefaultSwordDigRuleTest
{
    
    /** 
     * test me.
     * @throws ClassNotFoundException thrown on problems.
     */
    @Test
    public void testGetSpeed() throws ClassNotFoundException
    {
        final BlockServiceInterface bsi = mock(BlockServiceInterface.class);
        when(bsi.isWeb(Material.WEB)).thenReturn(Boolean.TRUE);
        when(bsi.isPlant(Material.DOUBLE_PLANT)).thenReturn(Boolean.TRUE);
        when(bsi.isVine(Material.VINE)).thenReturn(Boolean.TRUE);
        when(bsi.isCoral(Material.IRON_ORE)).thenReturn(Boolean.TRUE);
        when(bsi.isLeaves(Material.LEAVES)).thenReturn(Boolean.TRUE);
        when(bsi.isGourd(Material.IRON_BLOCK)).thenReturn(Boolean.TRUE);
        when(bsi.isWeb(Blocks.Block1)).thenReturn(Boolean.TRUE);
        when(bsi.isPlant(Blocks.Block2)).thenReturn(Boolean.TRUE);
        when(bsi.isVine(Blocks.Block3)).thenReturn(Boolean.TRUE);
        when(bsi.isCoral(Blocks.Block4)).thenReturn(Boolean.TRUE);
        when(bsi.isLeaves(Blocks.Block5)).thenReturn(Boolean.TRUE);
        when(bsi.isGourd(Blocks.Block6)).thenReturn(Boolean.TRUE);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.items.BlockServiceCache"), "SERVICES", bsi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final DigRule rule = new DigRule();
        assertEquals(15.0f, rule.getHarvestSpeed(null, Material.WEB), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Material.DOUBLE_PLANT), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Material.VINE), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Material.IRON_ORE), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Material.LEAVES), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Material.IRON_BLOCK), 0);
        assertEquals(1.0f, rule.getHarvestSpeed(null, Material.ACACIA_DOOR), 0);
        assertEquals(15.0f, rule.getHarvestSpeed(null, Blocks.Block1, null), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Blocks.Block2, null), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Blocks.Block3, null), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Blocks.Block4, null), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Blocks.Block5, null), 0);
        assertEquals(1.5f, rule.getHarvestSpeed(null, Blocks.Block6, null), 0);
        assertEquals(1.0f, rule.getHarvestSpeed(null, Blocks.Block7, null), 0);
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
        when(bsi.isWeb(Material.WEB)).thenReturn(Boolean.TRUE);
        when(bsi.isWeb(Blocks.Block1)).thenReturn(Boolean.TRUE);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.items.BlockServiceCache"), "SERVICES", bsi); //$NON-NLS-1$ //$NON-NLS-2$
        
        // tool level 3
        final DigRule rule3 = new DigRule();
        assertTrue(rule3.canHarvest(Material.WEB));
        assertTrue(rule3.canHarvest(Blocks.Block1, null));
        
        assertFalse(rule3.canHarvest(Material.IRON_BLOCK));
        assertFalse(rule3.canHarvest(Blocks.Block2, null));
    }
    
    /**
     * custom dig rule.
     * @author mepeisen
     *
     */
    private static final class DigRule extends DefaultSwordDigRule
    {
        
        /**
         * Constructor.
         */
        public DigRule()
        {
            super();
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
