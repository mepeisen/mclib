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

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author mepeisen
 *
 */
public class DefaultHoeDigRule implements ItemDigInterface
{
    
    /**
     * effecieny on proper tooling material
     */
    protected float efficiencyOnProperMaterial = 4.0f;
    
    /**
     * efficient materials
     */
    protected Set<Material> efficientMaterials = new HashSet<>();
    
    /**
     * efficient blocks
     */
    protected Set<Material> efficientBlocks = new HashSet<>();

    @Override
    public float getHarvestSpeed(ItemStack stack, Material material)
    {
        if (this.efficientMaterials.contains(material))
        {
            return this.efficiencyOnProperMaterial;
        }
        return 1.0f;
    }

    @Override
    public float getHarvestSpeed(ItemStack stack, BlockId block, BlockVariantId variant)
    {
        if (this.efficientBlocks.contains(block))
        {
            return this.efficiencyOnProperMaterial;
        }
        return 1.0f;
    }

    @Override
    public int getDamageByBlock(ItemStack stack, Block block, Player player)
    {
        return 2;
    }

    @Override
    public boolean canHarvest(Material material)
    {
        return false;
    }

    @Override
    public boolean canHarvest(BlockId block, BlockVariantId variant)
    {
        return false;
    }
    
}
