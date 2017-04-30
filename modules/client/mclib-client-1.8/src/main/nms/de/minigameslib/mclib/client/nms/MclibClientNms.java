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
import de.minigameslib.mclib.pshared.PingData.ItemMetaData;
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
            field.set(Block.blockRegistry, Integer.valueOf(MclibConstants.MAX_ITEM_ID));

            field = Item.itemRegistry.getClass().getDeclaredField("maxId");
            field.setAccessible(true);
            field.set(Item.itemRegistry, Integer.valueOf(MclibConstants.MAX_ITEM_ID));

            final Method itemAdd = GameData.class.getDeclaredMethod("registerItem", Item.class, String.class, int.class); //$NON-NLS-1$
            itemAdd.setAccessible(true);
            final Method blockAdd = GameData.class.getDeclaredMethod("registerBlock", Block.class, String.class, int.class); //$NON-NLS-1$
            blockAdd.setAccessible(true);
            
            final Method getGameData = GameData.class.getDeclaredMethod("getMain"); //$NON-NLS-1$
            getGameData.setAccessible(true);
            final GameData gameData = (GameData) getGameData.invoke(null);
            
            for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
            {
                final Block block = new MyBlock("custom-" + i); //$NON-NLS-1$
                blockAdd.invoke(gameData, block, "mclib:custom-" + i, i); //$NON-NLS-1$
                
                final ItemBlock item = new MyItemBlock(block);
                itemAdd.invoke(gameData, item, "mclib:custom-" + i, i); //$NON-NLS-1$
            }
            
            for (int i = MclibConstants.MIN_ITEM_ID; i <= MclibConstants.MAX_ITEM_ID; i++)
            {
                final Item item = new MyItem("custom-" + i); //$NON-NLS-1$
                itemAdd.invoke(gameData, item, "mclib:custom-" + i, i); //$NON-NLS-1$
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
            final Block block = (Block) Block.blockRegistry.getObjectById(i);
            final Item item = Item.getItemFromBlock(block);
            for (final MyBlock.EnumType type : MyBlock.EnumType.values())
            {
                final ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation("mclib", "custom-" + i), "variant=" + type.toString());
                ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), model);
            }
        }
        
        for (int i = MclibConstants.MIN_ITEM_ID; i <= MclibConstants.MAX_ITEM_ID; i++)
        {
            final Item item = Item.getItemById(i);
            final ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation("mclib", "custom-"+i), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, model);
        }
    }

    public static void setBlockMeta(List<BlockMetaData> meta)
    {
        for (final BlockMetaData data : meta)
        {
            final MyBlock block = (MyBlock) Block.getBlockById(data.getId());
            block.setHardness(data.getHardness()).setResistance(data.getResistance());
            data.getOpaqueness().forEach(block::setOpaqueCube);
        }
    }
    
    private static void replaceItem(int id, Item item)
    {
        try
        {
            final Method itemAdd = GameData.getItemRegistry().getClass().getDeclaredMethod("addObjectRaw", int.class, ResourceLocation.class, Object.class); //$NON-NLS-1$
            itemAdd.setAccessible(true);
            
            final Method setName = item.delegate.getClass().getDeclaredMethod("setName", String.class); //$NON-NLS-1$
            setName.setAccessible(true);
            setName.invoke(item.delegate, "mclib:custom-" + id);
            
            itemAdd.invoke(GameData.getItemRegistry(), id, new ResourceLocation("mclib", "custom-" + id), item);
            
            final ModelResourceLocation model = new ModelResourceLocation("mclib:custom-"+id, "inventory");
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, model);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * @param items
     */
    public static void setItemMeta(List<ItemMetaData> items)
    {
        for (final ItemMetaData data : items)
        {
            switch (data.getCls())
            {
                case Shovel:
                    replaceItem(data.getId(), new MyShovel("custom-" + data.getId()));
                    ((MyShovel)Item.getItemById(data.getId()).setMaxDamage(data.getDurability())).setDmgData(data.getDamage(), data.getSpeed());
                    break;
                case Pickaxe:
                    replaceItem(data.getId(), new MyPickaxe("custom-" + data.getId()));
                    ((MyPickaxe)Item.getItemById(data.getId()).setMaxDamage(data.getDurability())).setDmgData(data.getDamage(), data.getSpeed());
                    break;
                case Hoe:
                    replaceItem(data.getId(), new MyHoe("custom-" + data.getId()));
                    ((MyHoe)Item.getItemById(data.getId()).setMaxDamage(data.getDurability())).setDmgData(data.getDamage(), data.getSpeed());
                    break;
                case Axe:
                    replaceItem(data.getId(), new MyAxe("custom-" + data.getId()));
                    ((MyAxe)Item.getItemById(data.getId()).setMaxDamage(data.getDurability())).setDmgData(data.getDamage(), data.getSpeed());
                    break;
                case Sword:
                    replaceItem(data.getId(), new MySword("custom-" + data.getId()));
                    ((MySword)Item.getItemById(data.getId()).setMaxDamage(data.getDurability())).setDmgData(data.getDamage(), data.getSpeed());
                    break;
                default:
                    ((MyItem)Item.getItemById(data.getId()).setMaxDamage(data.getDurability())).setDmgData(data.getDamage(), data.getSpeed());
                    break;
                case Boots:
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 1, 3));
                    break;
                case Chestplate:
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 1, 1));
                    break;
                case Helmet:
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 1, 0));
                    break;
                case Leggins:
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 2, 2));
                    break;
                
            }
        }
    }
    
}
