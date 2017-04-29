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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

/**
 * Nms helper class.
 * 
 * @author mepeisen
 */
public class MclibClientNms
{
    
    public static GuiScreen getGui(GuiOpenEvent evt)
    {
        return evt.getGui();
    }
    
    public static float getPartialTicks(RenderWorldLastEvent evt)
    {
        return evt.getPartialTicks();
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

            final Method itemAdd = Item.itemRegistry.getClass().getDeclaredMethod("add", int.class, ResourceLocation.class, IForgeRegistryEntry.class); //$NON-NLS-1$
            itemAdd.setAccessible(true);
            final Method blockAdd = Block.blockRegistry.getClass().getDeclaredMethod("add", int.class, ResourceLocation.class, IForgeRegistryEntry.class); //$NON-NLS-1$
            blockAdd.setAccessible(true);
            
            for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
            {
                final Block block = new MyBlock("custom-" + i); //$NON-NLS-1$
                blockAdd.invoke(Block.blockRegistry, i, block.getRegistryName(), block);
                
                final ItemBlock item = new MyItemBlock(block);
                item.setRegistryName(block.getRegistryName());
                itemAdd.invoke(Item.itemRegistry, i, item.getRegistryName(), item);
            }
            
            for (int i = MclibConstants.MIN_ITEM_ID; i <= MclibConstants.MAX_ITEM_ID; i++)
            {
                final Item item = new MyItem("custom-" + i); //$NON-NLS-1$
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
        
        for (int i = MclibConstants.MIN_ITEM_ID; i <= MclibConstants.MAX_ITEM_ID; i++)
        {
            final Item item = Item.getItemById(i);
            final ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, model);
        }
    }

    public static void setBlockMeta(List<BlockMetaData> meta)
    {
        for (final BlockMetaData data : meta)
        {
            Block.getBlockById(data.getId()).setHardness(data.getHardness()).setResistance(data.getResistance());
        }
    }
    
    private static void replaceItem(int id, Item item)
    {
        try
        {
            final Method itemAdd = Item.itemRegistry.getClass().getDeclaredMethod("addObjectRaw", int.class, ResourceLocation.class, IForgeRegistryEntry.class); //$NON-NLS-1$
            itemAdd.setAccessible(true);
            
            final Method setName = item.delegate.getClass().getDeclaredMethod("setName", ResourceLocation.class); //$NON-NLS-1$
            setName.setAccessible(true);
            setName.invoke(item.delegate, item.getRegistryName());
            
            itemAdd.invoke(Item.itemRegistry, id, item.getRegistryName(), item);
            
            final ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
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
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 1, EntityEquipmentSlot.FEET));
                    break;
                case Chestplate:
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 1, EntityEquipmentSlot.CHEST));
                    break;
                case Helmet:
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 1, EntityEquipmentSlot.HEAD));
                    break;
                case Leggins:
                    replaceItem(data.getId(), new MyArmor("custom-" + data.getId(), 2, EntityEquipmentSlot.LEGS));
                    break;
                
            }
        }
    }
    
}
