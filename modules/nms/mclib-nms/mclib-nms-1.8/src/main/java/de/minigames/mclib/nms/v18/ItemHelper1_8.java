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

package de.minigames.mclib.nms.v18;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import de.minigames.mclib.nms.v18.blocks.CustomBlock;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.pshared.MclibConstants;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.IBlockData;
import net.minecraft.server.v1_8_R1.MinecraftKey;
import net.minecraft.server.v1_8_R1.NBTTagCompound;

/**
 * @author mepeisen
 *
 */
public class ItemHelper1_8 implements ItemHelperInterface
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
                final net.minecraft.server.v1_8_R1.ItemStack nms = (net.minecraft.server.v1_8_R1.ItemStack) field.get(stack);
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
                final net.minecraft.server.v1_8_R1.ItemStack nms = (net.minecraft.server.v1_8_R1.ItemStack) field.get(stack);
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

    @Override
    public int getVariant(Block block)
    {
        final BlockPosition pos = new BlockPosition(block.getX(), block.getY(), block.getZ());
        return ((CustomBlock.EnumCustomVariant) ((CraftWorld)block.getWorld()).getHandle().getType(pos).get(CustomBlock.VARIANT)).ordinal();
    }

    @Override
    public int getVariant(ItemStack stack)
    {
        return stack.getData().getData();
    }

    @Override
    public void setBlockVariant(Block block, int type, int variant)
    {
        final BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
        ((CraftWorld)block.getWorld()).getHandle().setTypeAndData(position, net.minecraft.server.v1_8_R1.Blocks.AIR.getBlockData(), 0);
        final IBlockData blockData = net.minecraft.server.v1_8_R1.Block.getById(type).getBlockData();
        blockData.set(CustomBlock.VARIANT, CustomBlock.EnumCustomVariant.values()[variant]);
        // final IBlockData blockData = Blocks.FURNACE.getBlockData();
        boolean success = ((CraftChunk)block.getChunk()).getHandle().getWorld().setTypeAndData(position, blockData, 2);
        if (success) {
            ((CraftChunk)block.getChunk()).getHandle().getWorld().notify(position);
        }
    }

    @Override
    public ItemStack createItemStackForBlock(int type, int variant)
    {
        return new ItemStack(type, 1, (byte) variant);
    }

    @Override
    public void initBlocks()
    {
        try
        {
            for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
            {
                final CustomBlock myBlock = new CustomBlock();
                net.minecraft.server.v1_8_R1.Block.REGISTRY.a(i, new MinecraftKey("mclib:custom_" + i), myBlock);
                Iterator<IBlockData> iterator2 = myBlock.O().a().iterator();
                while (iterator2.hasNext()) {
                    IBlockData iblockdata = iterator2.next();
                    int k = net.minecraft.server.v1_8_R1.Block.REGISTRY.b(myBlock) << 4 | myBlock.toLegacyData(iblockdata);
                    net.minecraft.server.v1_8_R1.Block.d.a(iblockdata, k);
                }
                // getStaticMethod(TileEntity.class, "a", Class.class, String.class).invoke(null, MyTileEntity.class, "custom");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
