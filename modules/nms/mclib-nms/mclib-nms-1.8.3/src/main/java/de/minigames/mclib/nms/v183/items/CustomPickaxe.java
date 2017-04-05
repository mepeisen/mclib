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

package de.minigames.mclib.nms.v183.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;

import com.google.common.collect.Multimap;

import de.minigames.mclib.nms.v183.blocks.CustomBlock;
import de.minigameslib.mclib.nms.api.NmsItemRuleInterface;
import net.minecraft.server.v1_8_R2.AttributeModifier;
import net.minecraft.server.v1_8_R2.Block;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.EntityLiving;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.GenericAttributes;
import net.minecraft.server.v1_8_R2.Item;
import net.minecraft.server.v1_8_R2.ItemPickaxe;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.World;

/**
 * @author mepeisen
 */
public class CustomPickaxe extends ItemPickaxe
{
    
    /**
     * attack dmg.
     */
    private double               attackDmg;
    
    /**
     * attack speed
     */
    private double               attackSpeed;
    
    /**
     * flag for using the attack modifiers
     */
    private boolean              attackModifiersUsed = false;
    
    /**
     * item enchantability
     */
    private int                  itemEnchantability;
    
    /**
     * the item rules.
     */
    private NmsItemRuleInterface itemRule;
    
    /**
     * 
     */
    public CustomPickaxe()
    {
        super(EnumToolMaterial.WOOD);
    }
    
    /**
     * Sets the attack modifiers
     * 
     * @param dmg
     * @param speed
     */
    public void setAttackModifiers(double dmg, double speed)
    {
        this.attackDmg = dmg;
        this.attackSpeed = speed;
        this.attackModifiersUsed = true;
    }
    
    /**
     * Sets the itemEnchantability
     * 
     * @param itemEnchantability
     */
    public void setItemEnchantability(int itemEnchantability)
    {
        this.itemEnchantability = itemEnchantability;
    }
    
    @Override
    public int b()
    {
        return this.itemEnchantability;
    }
    
    @Override
    public Item setMaxDurability(int paramInt)
    {
        return super.setMaxDurability(paramInt);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Multimap i()
    {
        Multimap localMultimap = super.i();
        
        if (this.attackModifiersUsed)
        {
            localMultimap.put(GenericAttributes.e.getName(), new AttributeModifier(f, "Weapon modifier", this.attackDmg, 0)); //$NON-NLS-1$
        }
        
        return localMultimap;
    }
    
    @Override
    public float getDestroySpeed(ItemStack paramItemStack, Block localBlock)
    {
        return this.itemRule.getHarvestSpeed(CraftItemStack.asCraftMirror(paramItemStack), Block.getId(localBlock), 0);
    }
    
    @Override
    public boolean a(ItemStack paramItemStack, World paramWorld, Block localBlock, BlockPosition paramBlockPosition, EntityLiving paramEntityLiving)
    {
        if (localBlock.g(paramWorld, paramBlockPosition) != 0.0D)
        {
            final Location loc = new Location(paramWorld.getWorld(), paramBlockPosition.getX(), paramBlockPosition.getY(), paramBlockPosition.getZ());
            paramItemStack.damage(this.itemRule.getDamageByBlock(CraftItemStack.asCraftMirror(paramItemStack), Block.getId(localBlock), 0, loc,
                    paramEntityLiving instanceof EntityPlayer ? ((EntityPlayer) paramEntityLiving).getBukkitEntity() : null), paramEntityLiving);
        }
        return true;
    }
    
    @Override
    public boolean a(ItemStack paramItemStack, EntityLiving paramEntityLiving1, EntityLiving paramEntityLiving2)
    {
        paramItemStack.damage(this.itemRule.getDamageByEntity(CraftItemStack.asCraftMirror(paramItemStack), paramEntityLiving1.getBukkitEntity(), paramEntityLiving2 instanceof EntityPlayer ? ((EntityPlayer) paramEntityLiving2).getBukkitEntity() : null), paramEntityLiving2);
        return true;
    }
    
    @Override
    public boolean a(ItemStack paramItemStack1, ItemStack paramItemStack2)
    {
        return this.itemRule.getIsRepairable(CraftItemStack.asCraftMirror(paramItemStack1), CraftItemStack.asCraftMirror(paramItemStack2));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean canDestroySpecialBlock(Block localBlock)
    {
        if (localBlock instanceof CustomBlock)
        {
            return this.itemRule.canHarvest(Block.getId(localBlock), 0);
        }
        return this.itemRule.canHarvest(Material.getMaterial(Block.getId(localBlock)));
    }
    
    /**
     * @param nmsItemRule
     */
    public void setItemRules(NmsItemRuleInterface nmsItemRule)
    {
        this.itemRule = nmsItemRule;
    }
    
}
