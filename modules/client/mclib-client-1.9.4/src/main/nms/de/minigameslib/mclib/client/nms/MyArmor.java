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

package de.minigameslib.mclib.client.nms;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

/**
 * @author mepeisen
 *
 */
public class MyArmor extends ItemArmor
{

    private String name;
    
    public MyArmor(String name, int textureLayer, EntityEquipmentSlot slot)
    {
        super(EnumHelper.addArmorMaterial(
                name,
                "mclib:"+name,
                5, // durability
                new int[]{1, 3, 2, 1}, // reduce amount
                15, // enchantability
                SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
                0.0f),
            textureLayer, slot);
        setUnlocalizedName(name);
        setRegistryName("mclib:"+name);
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }
    
}
