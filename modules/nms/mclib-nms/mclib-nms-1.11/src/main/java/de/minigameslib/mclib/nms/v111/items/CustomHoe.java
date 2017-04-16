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

package de.minigameslib.mclib.nms.v111.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;

import com.google.common.collect.Multimap;

import de.minigameslib.mclib.nms.api.NmsItemRuleInterface;
import de.minigameslib.mclib.nms.v111.blocks.CustomBlock;
import net.minecraft.server.v1_11_R1.AttributeModifier;
import net.minecraft.server.v1_11_R1.Block;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.EnumItemSlot;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.Item;
import net.minecraft.server.v1_11_R1.ItemHoe;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.World;

/**
 * @author mepeisen
 */
public class CustomHoe extends ItemHoe
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
    public CustomHoe()
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
    public int c()
    {
        return this.itemEnchantability;
    }
    
    @Override
    public Item setMaxDurability(int paramInt)
    {
        return super.setMaxDurability(paramInt);
    }
    
    @Override
    public Multimap<String, AttributeModifier> a(EnumItemSlot paramEnumItemSlot)
    {
        Multimap<String, AttributeModifier> localMultimap = super.a(paramEnumItemSlot);
        
        if (this.attackModifiersUsed && paramEnumItemSlot == EnumItemSlot.MAINHAND)
        {
            localMultimap.put(GenericAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(h, "Tool modifier", this.attackDmg, 0)); //$NON-NLS-1$
            localMultimap.put(GenericAttributes.f.getName(), new AttributeModifier(i, "Tool modifier", this.attackSpeed, 0)); //$NON-NLS-1$
        }
        
        return localMultimap;
    }
    
    @Override
    public float getDestroySpeed(ItemStack paramItemStack, IBlockData paramIBlockData)
    {
        final Block localBlock = paramIBlockData.getBlock();
        return this.itemRule.getHarvestSpeed(CraftItemStack.asCraftMirror(paramItemStack), Block.getId(localBlock), localBlock.toLegacyData(paramIBlockData));
    }
    
    @Override
    public boolean a(ItemStack paramItemStack, World paramWorld, IBlockData paramIBlockData, BlockPosition paramBlockPosition, EntityLiving paramEntityLiving)
    {
        if (paramIBlockData.b(paramWorld, paramBlockPosition) != 0.0D)
        {
            final Block localBlock = paramIBlockData.getBlock();
            final Location loc = new Location(paramWorld.getWorld(), paramBlockPosition.getX(), paramBlockPosition.getY(), paramBlockPosition.getZ());
            paramItemStack.damage(this.itemRule.getDamageByBlock(CraftItemStack.asCraftMirror(paramItemStack), Block.getId(localBlock), localBlock.toLegacyData(paramIBlockData), loc,
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
    public boolean canDestroySpecialBlock(IBlockData paramIBlockData)
    {
        final Block localBlock = paramIBlockData.getBlock();
        if (localBlock instanceof CustomBlock)
        {
            return this.itemRule.canHarvest(Block.getId(localBlock), localBlock.toLegacyData(paramIBlockData));
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
