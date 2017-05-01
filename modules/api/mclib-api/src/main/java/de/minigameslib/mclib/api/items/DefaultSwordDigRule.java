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

package de.minigameslib.mclib.api.items;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A default dig rule implementation for swords.
 * 
 * @author mepeisen
 *
 */
public class DefaultSwordDigRule implements ItemDigInterface
{
    
    @Override
    public float getHarvestSpeed(ItemStack stack, Material material)
    {
        final BlockServiceInterface bsi = BlockServiceInterface.instance();
        if (bsi.isWeb(material))
        {
            return 15.0f;
        }
        return !bsi.isPlant(material)
            && !bsi.isVine(material)
            && !bsi.isCoral(material)
            && !bsi.isLeaves(material)
            && !bsi.isGourd(material)
                ? 1.0F : 1.5F;
    }
    
    @Override
    public float getHarvestSpeed(ItemStack stack, BlockId block, BlockVariantId variant)
    {
        final BlockServiceInterface bsi = BlockServiceInterface.instance();
        if (bsi.isWeb(block))
        {
            return 15.0f;
        }
        return !bsi.isPlant(block)
            && !bsi.isVine(block)
            && !bsi.isCoral(block)
            && !bsi.isLeaves(block)
            && !bsi.isGourd(block)
                ? 1.0F : 1.5F;
    }
    
    @Override
    public int getDamageByBlock(ItemStack stack, Block block, Player player)
    {
        return 2;
    }
    
    @Override
    public boolean canHarvest(Material material)
    {
        return BlockServiceInterface.instance().isWeb(Material.WEB);
    }
    
    @Override
    public boolean canHarvest(BlockId block, BlockVariantId variant)
    {
        return BlockServiceInterface.instance().isWeb(block);
    }
    
}
