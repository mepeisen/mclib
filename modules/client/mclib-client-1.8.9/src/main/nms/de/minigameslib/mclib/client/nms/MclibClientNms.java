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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import de.minigameslib.mclib.pshared.MclibConstants;
import de.minigameslib.mclib.pshared.PingData.BlockMetaData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Nms helper class.
 * 
 * @author mepeisen
 */
public class MclibClientNms
{
    
    public static GuiScreen getGui(GuiOpenEvent evt)
    {
        return evt.gui;
    }
    
    public static float getPartialTicks(RenderWorldLastEvent evt)
    {
        return evt.partialTicks;
    }
    
    public static EntityPlayerSP getPlayer(Minecraft mc)
    {
        return mc.thePlayer;
    }
    
    public static WorldClient getWorld(Minecraft mc)
    {
        return mc.theWorld;
    }
    
    public static void initCustomBlocksAndItems()
    {
        try
        {
            Field field = Block.blockRegistry.getClass().getDeclaredField("maxId");
            field.setAccessible(true);
            field.set(Block.blockRegistry, Integer.valueOf(MclibConstants.MAX_BLOCK_ID));

            field = Item.itemRegistry.getClass().getDeclaredField("maxId");
            field.setAccessible(true);
            field.set(Item.itemRegistry, Integer.valueOf(MclibConstants.MAX_BLOCK_ID));

            final Method itemAdd = Item.itemRegistry.getClass().getDeclaredMethod("add", int.class, ResourceLocation.class, Object.class); //$NON-NLS-1$
            itemAdd.setAccessible(true);
            final Method blockAdd = Block.blockRegistry.getClass().getDeclaredMethod("add", int.class, ResourceLocation.class, Object.class); //$NON-NLS-1$
            blockAdd.setAccessible(true);
            
            for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
            {
                final Block block = new MyBlock("custom-" + i); //$NON-NLS-1$
                blockAdd.invoke(Block.blockRegistry, i, block.getRegistryName(), block);
                
                final ItemBlock item = new MyItemBlock(block);
                item.setRegistryName(block.getRegistryName());
                itemAdd.invoke(Item.itemRegistry, i, item.getRegistryName(), item);
            }
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    public static void registerItemRenderers()
    {
        for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
        {
            final Block block = Block.blockRegistry.getObjectById(i);
            final Item item = Item.getItemFromBlock(block);
            for (final MyBlock.EnumType type : MyBlock.EnumType.values())
            {
                final ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "variant=" + type.toString());
                ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), model);
            }
        }
    }

    public static void setItemMeta(List<BlockMetaData> meta)
    {
        for (final BlockMetaData data : meta)
        {
            Block.getBlockById(data.getId()).setHardness(data.getHardness()).setResistance(data.getResistance());
        }
    }
    
}
