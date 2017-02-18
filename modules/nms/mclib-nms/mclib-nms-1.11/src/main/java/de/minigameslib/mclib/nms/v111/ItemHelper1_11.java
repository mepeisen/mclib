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

package de.minigameslib.mclib.nms.v111;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import net.minecraft.server.v1_11_R1.NBTTagCompound;

/**
 * @author mepeisen
 *
 */
public class ItemHelper1_11 implements ItemHelperInterface
{
    
    @Override
    public void addCustomData(ItemStack stack, String plugin, String key, String value)
    {
        if (stack instanceof CraftItemStack)
        {
            try
            {
                final Field field = stack.getClass().getDeclaredField("handle"); //$NON-NLS-1$
                field.setAccessible(true);
                final net.minecraft.server.v1_11_R1.ItemStack nms = (net.minecraft.server.v1_11_R1.ItemStack) field.get(stack);
                NBTTagCompound tag = nms.getTag();
                if (tag == null)
                {
                    tag = new NBTTagCompound();
                    nms.setTag(tag);
                }
                tag.setString("mclib:customdata:" + plugin + ":" + key, value);  //$NON-NLS-1$//$NON-NLS-2$
            }
            catch (Exception ex)
            {
                // TODO logging
            }
        }
    }
    
    @Override
    public String getCustomData(ItemStack stack, String plugin, String key)
    {
        if (stack instanceof CraftItemStack)
        {
            try
            {
                final Field field = stack.getClass().getDeclaredField("handle"); //$NON-NLS-1$
                field.setAccessible(true);
                final net.minecraft.server.v1_11_R1.ItemStack nms = (net.minecraft.server.v1_11_R1.ItemStack) field.get(stack);
                NBTTagCompound tag = nms.getTag();
                if (tag != null)
                {
                    final String value = tag.getString("mclib:customdata:" + plugin + ":" + key);  //$NON-NLS-1$//$NON-NLS-2$
                    if (value != null && value.length() > 0)
                    {
                        return value;
                    }
                }
            }
            catch (Exception ex)
            {
                // TODO logging
            }
        }
        return null;
    }
    
}
