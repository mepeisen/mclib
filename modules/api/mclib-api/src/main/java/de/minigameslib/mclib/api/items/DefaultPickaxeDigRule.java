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
public class DefaultPickaxeDigRule implements ItemDigInterface
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
    
    /**
     * the tooling level
     */
    protected int toolLevel = 3;
    
    /**
     * Constructor
     */
    public DefaultPickaxeDigRule()
    {
        this.efficientMaterials.add(Material.ACTIVATOR_RAIL);
        this.efficientMaterials.add(Material.COAL_ORE);
        this.efficientMaterials.add(Material.COBBLESTONE);
        this.efficientMaterials.add(Material.DETECTOR_RAIL);
        this.efficientMaterials.add(Material.DIAMOND_BLOCK);
        this.efficientMaterials.add(Material.DIAMOND_ORE);
        this.efficientMaterials.add(Material.DOUBLE_STONE_SLAB2);
        this.efficientMaterials.add(Material.POWERED_RAIL);
        this.efficientMaterials.add(Material.GOLD_BLOCK);
        this.efficientMaterials.add(Material.GOLD_ORE);
        this.efficientMaterials.add(Material.ICE);
        this.efficientMaterials.add(Material.IRON_BLOCK);
        this.efficientMaterials.add(Material.IRON_ORE);
        this.efficientMaterials.add(Material.LAPIS_BLOCK);
        this.efficientMaterials.add(Material.LAPIS_ORE);
        this.efficientMaterials.add(Material.GLOWING_REDSTONE_ORE);
        this.efficientMaterials.add(Material.MOSSY_COBBLESTONE);
        this.efficientMaterials.add(Material.NETHERRACK);
        this.efficientMaterials.add(Material.PACKED_ICE);
        this.efficientMaterials.add(Material.RAILS);
        this.efficientMaterials.add(Material.REDSTONE_ORE);
        this.efficientMaterials.add(Material.SANDSTONE);
        this.efficientMaterials.add(Material.RED_SANDSTONE);
        this.efficientMaterials.add(Material.STONE);
        this.efficientMaterials.add(Material.STONE_SLAB2);
        this.efficientMaterials.add(Material.STONE_BUTTON);
        this.efficientMaterials.add(Material.STONE_PLATE);
    }

    @Override
    public float getHarvestSpeed(ItemStack stack, Material material)
    {
        final BlockServiceInterface bsi = BlockServiceInterface.instance();
        if (bsi.isRock(material)
                || bsi.isOre(material)
                || bsi.isHeavy(material))
        {
            return this.efficiencyOnProperMaterial;
        }
        if (this.efficientMaterials.contains(material))
        {
            return this.efficiencyOnProperMaterial;
        }
        return 1.0f;
    }

    @Override
    public float getHarvestSpeed(ItemStack stack, BlockId block, BlockVariantId variant)
    {
        final BlockServiceInterface bsi = BlockServiceInterface.instance();
        if (bsi.isRock(block)
                || bsi.isOre(block)
                || bsi.isHeavy(block))
        {
            return this.efficiencyOnProperMaterial;
        }
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
        if (material == Material.OBSIDIAN)
        {
            return this.toolLevel == 3;
        }
        
        if (material == Material.DIAMOND_BLOCK || material == Material.DIAMOND_ORE
                || material == Material.EMERALD_BLOCK || material == Material.EMERALD_ORE
                || material == Material.GOLD_BLOCK || material == Material.GOLD_ORE
                || material == Material.REDSTONE_BLOCK || material == Material.REDSTONE_ORE)
        {
            return this.toolLevel >= 2;
        }
        
        if (material == Material.IRON_BLOCK || material == Material.IRON_ORE
                || material == Material.LAPIS_BLOCK || material == Material.LAPIS_ORE)
        {
            return this.toolLevel >= 1;
        }

        final BlockServiceInterface bsi = BlockServiceInterface.instance();
        if (bsi.isRock(material) || bsi.isOre(material) || bsi.isHeavy(material))
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean canHarvest(BlockId block, BlockVariantId variant)
    {
        final BlockServiceInterface bsi = BlockServiceInterface.instance();
        if (bsi.isRock(block) || bsi.isOre(block) || bsi.isHeavy(block))
        {
            return true;
        }
        return false;
    }
    
}
