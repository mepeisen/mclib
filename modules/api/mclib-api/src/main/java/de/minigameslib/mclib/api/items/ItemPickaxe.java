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

/**
 * Item representing a pickaxe.
 * 
 * @author mepeisen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface ItemPickaxe
{
    
    /**
     * the durability of this item.
     * 
     * @return item durability
     */
    int durability();
    
    /**
     * the attack damage modifier.
     * 
     * @return attack damage modifier
     */
    double damage();
    
    /**
     * the attack speed modifier.
     * 
     * @return attack speed modifier
     */
    double speed();
    
    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     * 
     * @return Return the enchantability factor of the item, most of the time is based on material
     */
    int getItemEnchantability();
    
    /**
     * Returns the repair rule.
     * 
     * @return repair rule
     */
    Class<? extends ItemRepairInterface> repairRule();
    
    /**
     * Returns the damage rule.
     * 
     * @return damage rule
     */
    Class<? extends ItemDmgInterface> dmgRule();
    
    /**
     * Returns the dig rule.
     * 
     * @return dig rule
     */
    Class<? extends ItemDigInterface> digRule();
    
}
