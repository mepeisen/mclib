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
 * A default dig rule implementation for shovels.
 * 
 * @author mepeisen
 *
 */
public class DefaultShovelDigRule implements ItemDigInterface
{
    
    /**
     * effecieny on proper tooling material.
     */
    protected float efficiencyOnProperMaterial = 4.0f;
    
    /**
     * efficient materials.
     */
    protected Set<Material> efficientMaterials = new HashSet<>();
    
    /**
     * efficient blocks.
     */
    protected Set<BlockId> efficientBlocks = new HashSet<>();
    
    /**
     * Constructor.
     */
    public DefaultShovelDigRule()
    {
        this.efficientMaterials.add(Material.CLAY);
        this.efficientMaterials.add(Material.DIRT);
        this.efficientMaterials.add(Material.SOIL);
        this.efficientMaterials.add(Material.GRASS);
        this.efficientMaterials.add(Material.GRAVEL);
        this.efficientMaterials.add(Material.MYCEL);
        this.efficientMaterials.add(Material.SAND);
        this.efficientMaterials.add(Material.SNOW);
        this.efficientMaterials.add(Material.SNOW_BLOCK);
        this.efficientMaterials.add(Material.SOUL_SAND);
        this.efficientMaterials.add(Material.GRASS_PATH);
    }

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
        return material == Material.SNOW_BLOCK || material == Material.SNOW;
    }

    @Override
    public boolean canHarvest(BlockId block, BlockVariantId variant)
    {
        return false;
    }
    
}
