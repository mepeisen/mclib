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

import de.minigameslib.mclib.pshared.MclibConstants;
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
import net.minecraftforge.fml.common.registry.GameData;

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

            final Method itemAdd = GameData.class.getDeclaredMethod("registerItem", Item.class, ResourceLocation.class, int.class); //$NON-NLS-1$
            itemAdd.setAccessible(true);
            final Method blockAdd = GameData.class.getDeclaredMethod("registerBlock", Block.class, ResourceLocation.class, int.class); //$NON-NLS-1$
            blockAdd.setAccessible(true);
            
            final Method getGameData = GameData.class.getDeclaredMethod("getMain"); //$NON-NLS-1$
            getGameData.setAccessible(true);
            final GameData gameData = (GameData) getGameData.invoke(null);
            
            for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
            {
                final Block block = new MyBlock("custom-" + i); //$NON-NLS-1$
                blockAdd.invoke(gameData, block, new ResourceLocation("mclib", "custom-" + i), i); //$NON-NLS-1$ //$NON-NLS-2$
                
                final ItemBlock item = new MyItemBlock(block);
                itemAdd.invoke(gameData, item, new ResourceLocation("mclib", "custom-" + i), i); //$NON-NLS-1$ //$NON-NLS-2$
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
                final ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation("mclib", "custom-" + i), "variant=" + type.toString());
                ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), model);
            }
        }
    }
    
}
