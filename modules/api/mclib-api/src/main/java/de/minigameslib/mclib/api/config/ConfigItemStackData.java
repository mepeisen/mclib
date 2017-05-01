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

package de.minigameslib.mclib.api.config;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.shared.api.com.ItemStackDataFragment;

/**
 * Item Stack object for storing and reloading from config.
 * 
 * @author mepeisen
 */
public interface ConfigItemStackData extends ItemStackDataFragment
{
    
    /**
     * Converts given item stack to a bukkit item stack.
     * 
     * @return bukkit item stack.
     */
    ItemStack toBukkit();
    
    /**
     * Converts given bukkit item stack to saveable item stack.
     * 
     * @param stack
     *            item stack.
     * @return saveable item stack
     */
    static ConfigItemStackData fromBukkit(ItemStack stack)
    {
        return ItemServiceInterface.instance().toConfigData(stack);
    }
    
}
