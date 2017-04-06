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
public class DefaultAxeDigRule implements ItemDigInterface
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
     * Constructor
     */
    public DefaultAxeDigRule()
    {
        this.efficientMaterials.add(Material.WOOD);
        this.efficientMaterials.add(Material.BOOKSHELF);
        this.efficientMaterials.add(Material.LOG);
        this.efficientMaterials.add(Material.LOG_2);
        this.efficientMaterials.add(Material.CHEST);
        this.efficientMaterials.add(Material.PUMPKIN);
        this.efficientMaterials.add(Material.JACK_O_LANTERN);
        this.efficientMaterials.add(Material.MELON_BLOCK);
        this.efficientMaterials.add(Material.LADDER);
        this.efficientMaterials.add(Material.WOOD_BUTTON);
        this.efficientMaterials.add(Material.WOOD_PLATE);
    }

    @Override
    public float getHarvestSpeed(ItemStack stack, Material material)
    {
        final BlockServiceInterface bsi = BlockServiceInterface.instance();
        if (bsi.isWood(material)
                || bsi.isPlant(material)
                || bsi.isVine(material))
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
        if (bsi.isWood(block)
                || bsi.isPlant(block)
                || bsi.isVine(block))
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
        return false;
    }

    @Override
    public boolean canHarvest(BlockId block, BlockVariantId variant)
    {
        return false;
    }
    
}
