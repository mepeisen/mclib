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

package de.minigameslib.mclib.nms.v110.items;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import net.minecraft.server.v1_10_R1.Item;
import net.minecraft.server.v1_10_R1.MinecraftKey;
import net.minecraft.server.v1_10_R1.NBTReadLimiter;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

/**
 * The item stack data impl
 * 
 * @author mepeisen
 */
public class ConfigItemStackDataImpl extends AnnotatedDataFragment implements ConfigItemStackData
{
    
    /**
     * minecraft id
     */
    @PersistentField
    protected String mcid;
    
    /**
     * stack amount
     */
    @PersistentField
    protected int amount;
    
    /**
     * damage/data value
     */
    @PersistentField
    protected int damage;

    /**
     * base64 encoded tag data
     */
    @PersistentField
    protected String tagdata;
    
    /** logger */
    private static final Logger LOGGER = Logger.getLogger(ConfigItemStackDataImpl.class.getName());
    
    /**
     * constructor for reading from file
     */
    public ConfigItemStackDataImpl()
    {
        // empty
    }
    
    /**
     * constructor for reading from bukkit item stack
     * @param stack
     */
    public ConfigItemStackDataImpl(ItemStack stack)
    {
        final net.minecraft.server.v1_10_R1.ItemStack nms = CraftItemStack.asNMSCopy(stack);
        final MinecraftKey mckey = Item.REGISTRY.b(nms.getItem());
        this.mcid = mckey == null ? "minecraft:air" : mckey.toString(); //$NON-NLS-1$
        
        this.amount = nms.count;
        this.damage = nms.getData();
        
        final NBTTagCompound tag = nms.getTag();
        if (tag != null)
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final DataOutputStream dos = new DataOutputStream(baos);
            try
            {
                final Method mth = NBTTagCompound.class.getDeclaredMethod("write", DataOutput.class); //$NON-NLS-1$
                mth.setAccessible(true);
                mth.invoke(tag, dos);
                this.tagdata = Base64.getEncoder().encodeToString(baos.toByteArray());
            }
            catch (Exception ex)
            {
                LOGGER.log(Level.WARNING, "Problems getting item stack tag data", ex); //$NON-NLS-1$
            }
        }
    }
    
    @Override
    public ItemStack toBukkit()
    {
        final net.minecraft.server.v1_10_R1.ItemStack stack = new net.minecraft.server.v1_10_R1.ItemStack(
                Item.d(this.mcid),
                this.amount,
                this.damage);
        if (this.tagdata != null)
        {
            final NBTTagCompound tag = new NBTTagCompound();
            final ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(this.tagdata));
            final DataInputStream dis = new DataInputStream(bais);
            try
            {
                final Method mth = NBTTagCompound.class.getDeclaredMethod("load", DataInput.class, int.class, NBTReadLimiter.class); //$NON-NLS-1$
                mth.setAccessible(true);
                mth.invoke(tag, dis, 0, NBTReadLimiter.a);
                stack.setTag(tag);
                if (stack.getItem() != null)
                {
                    stack.getItem().a(stack.getTag());
                }
            }
            catch (Exception ex)
            {
                LOGGER.log(Level.WARNING, "Problems setting item stack tag data", ex); //$NON-NLS-1$
            }
        }
        return CraftItemStack.asCraftMirror(stack);
    }
    
}
