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

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Some item meta data
 * 
 * @author mepeisen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface ItemDurability
{
    
    /**
     * the durability of this item
     * @return item durability
     */
    int durability();
    
    /**
     * Returns the dig rule
     * @return dig rule
     */
    Class<? extends ItemDigInterface> digRule();
    
    /**
     * Returns the damage rule
     * @return damage rule
     */
    Class<? extends ItemDmgInterface> dmgRule();
    
    /**
     * Returns the repair rule
     * @return repair rule
     */
    Class<? extends ItemRepairInterface> repairRule();
    
    /**
     * Item rule for dig/ harvest
     * @author mepeisen
     *
     */
    public interface ItemDigInterface
    {
        
        /**
         * Returns the harvest speed modified
         * @param stack
         * @param material
         * @return harvest
         */
        float getHarvestSpeed(ItemStack stack, Material material);
        
        /**
         * Returns the harvest speed modified
         * @param stack
         * @param block
         * @param variant
         * @return harvest
         */
        float getHarvestSpeed(ItemStack stack, BlockId block, BlockVariantId variant);
        
        /**
         * Returns the damage by harvesting a block
         * @param stack
         * @param block
         * @param player
         * @return harvest block modifier
         */
        int getDamageByBlock(ItemStack stack, Block block, Player player);
        
        /**
         * Checks if block can be harvested
         * @param material
         * @return true for harvest block
         */
        boolean canHarvest(Material material);
        
        /**
         * Checks if block can be harvested
         * @param block
         * @param variant
         * @return true for harvest block
         */
        boolean canHarvest(BlockId block, BlockVariantId variant);
    }
    
    /**
     * A rule for damage/ attacking
     * @author mepeisen
     */
    public interface ItemDmgInterface
    {
        
        /**
         * Returns the amount of damage for damaging an entity
         * @param stack
         * @param target
         * @param player
         * @return damage for entity attack
         */
        int getDamageByEntity(ItemStack stack, Entity target, Player player);
        
    }

    /**
     * Rule return the default entity hit damage
     * @author mepeisen
     */
    public static class DefaultWeaponDmg implements ItemDmgInterface
    {

        @Override
        public int getDamageByEntity(ItemStack stack, Entity target, Player player)
        {
            return 1;
        }
        
    }

    /**
     * Rule return the default entity hit damage
     * @author mepeisen
     */
    public static class DefaultToolDmg implements ItemDmgInterface
    {

        @Override
        public int getDamageByEntity(ItemStack stack, Entity target, Player player)
        {
            return 2;
        }
        
    }
    
    /**
     * A rule for reparing
     * @author mepeisen
     */
    public interface ItemRepairInterface
    {
        
        /**
         * Checks if item is repairable
         * @param toRepair
         * @param repair
         * @return {@code true} if repairable
         */
        boolean getIsRepairable(ItemStack toRepair, ItemStack repair);
        
    }

    /**
     * Rule allowing repairing the item itself
     * @author mepeisen
     */
    public static class SelfRepair implements ItemRepairInterface
    {

        @SuppressWarnings("deprecation")
        @Override
        public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
        {
            return toRepair.getTypeId() == repair.getTypeId();
        }
        
    }
    
}
