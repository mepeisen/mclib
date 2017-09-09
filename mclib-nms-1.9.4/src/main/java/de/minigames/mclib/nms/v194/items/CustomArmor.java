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

package de.minigames.mclib.nms.v194.items;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;

import de.minigameslib.mclib.nms.api.NmsItemRuleInterface;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.Item;
import net.minecraft.server.v1_9_R2.ItemArmor;
import net.minecraft.server.v1_9_R2.ItemStack;

/**
 * @author mepeisen
 */
public class CustomArmor extends ItemArmor
{
    
    /**
     * item enchantability
     */
    private int                  itemEnchantability;
    
    /**
     * the item rules.
     */
    private NmsItemRuleInterface itemRule;
    
    /**
     * @param slot
     */
    public CustomArmor(EnumItemSlot slot)
    {
        super(EnumArmorMaterial.IRON, 2, slot);
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
    public Item setMaxDurability(int paramInt)
    {
        return super.setMaxDurability(paramInt);
    }
    
    @Override
    public int c()
    {
        return this.itemEnchantability;
    }
    
    @Override
    public boolean a(ItemStack paramItemStack1, ItemStack paramItemStack2)
    {
        return this.itemRule.getIsRepairable(CraftItemStack.asCraftMirror(paramItemStack1), CraftItemStack.asCraftMirror(paramItemStack2));
    }
    
    /**
     * @param nmsItemRule
     */
    public void setItemRules(NmsItemRuleInterface nmsItemRule)
    {
        this.itemRule = nmsItemRule;
    }
    
}
