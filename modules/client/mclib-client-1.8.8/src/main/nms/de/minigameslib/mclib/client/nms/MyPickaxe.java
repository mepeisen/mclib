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

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemPickaxe;

/**
 * @author mepeisen
 *
 */
public class MyPickaxe extends ItemPickaxe
{

    private String name;
    private double damage;
    private double speed;
    
    public MyPickaxe(String name)
    {
        super(ToolMaterial.WOOD);
        setUnlocalizedName(name);
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param damage
     * @param speed
     */
    public void setDmgData(double damage, double speed)
    {
        this.damage = damage;
        this.speed = speed;
    }
    
//    /**
//     * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
//     */
//    public float getDamageVsEntity()
//    {
//        return (float) this.damage;
//    }
    public Multimap<String, AttributeModifier> getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        if (this.damage != 0 && this.speed != 0)
        {
            multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.damage, 0));
        }
        return multimap;
    }
    
}
