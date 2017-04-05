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

package de.minigames.mclib.nms.v185;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Function;

import de.minigames.mclib.nms.v185.blocks.CustomBlock;
import de.minigames.mclib.nms.v185.items.CustomItem;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.NmsDropRuleInterface;
import de.minigameslib.mclib.nms.api.NmsItemRuleInterface;
import de.minigameslib.mclib.pshared.MclibConstants;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.CraftingManager;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemMultiTexture;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.RecipesFurnace;
import net.minecraft.server.v1_8_R3.WorldGenMinable;

/**
 * @author mepeisen
 *
 */
public class ItemHelper1_8_5 implements ItemHelperInterface
{
    
    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(ItemHelper1_8_5.class.getName());
    
    @Override
    public void addCustomData(ItemStack stack, String plugin, String key, String value)
    {
        if (stack instanceof CraftItemStack)
        {
            try
            {
                final Field field = stack.getClass().getDeclaredField("handle"); //$NON-NLS-1$
                field.setAccessible(true);
                final net.minecraft.server.v1_8_R3.ItemStack nms = (net.minecraft.server.v1_8_R3.ItemStack) field.get(stack);
                NBTTagCompound tag = nms.getTag();
                if (tag == null)
                {
                    tag = new NBTTagCompound();
                    nms.setTag(tag);
                }
                tag.setString("mclib:customdata:" + plugin + ":" + key, value); //$NON-NLS-1$//$NON-NLS-2$
            }
            catch (Exception ex)
            {
                LOGGER.log(Level.WARNING, "Problems adding custom data to itemstack", ex); //$NON-NLS-1$
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
                final net.minecraft.server.v1_8_R3.ItemStack nms = (net.minecraft.server.v1_8_R3.ItemStack) field.get(stack);
                NBTTagCompound tag = nms.getTag();
                if (tag != null)
                {
                    final String value = tag.getString("mclib:customdata:" + plugin + ":" + key); //$NON-NLS-1$//$NON-NLS-2$
                    if (value != null && value.length() > 0)
                    {
                        return value;
                    }
                }
            }
            catch (Exception ex)
            {
                LOGGER.log(Level.WARNING, "Problems receiving custom data from itemstack", ex); //$NON-NLS-1$
            }
        }
        return null;
    }
    
    @Override
    public int getVariant(Block block)
    {
        final BlockPosition pos = new BlockPosition(block.getX(), block.getY(), block.getZ());
        return ((CraftWorld) block.getWorld()).getHandle().getType(pos).get(CustomBlock.VARIANT).ordinal();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getVariant(ItemStack stack)
    {
        return stack.getData().getData();
    }
    
    @Override
    public void setBlockVariant(Block block, int type, int variant)
    {
        final BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
        ((CraftWorld) block.getWorld()).getHandle().setTypeAndData(position, net.minecraft.server.v1_8_R3.Blocks.AIR.getBlockData(), 0);
        final IBlockData blockData = net.minecraft.server.v1_8_R3.Block.getById(type).getBlockData();
        blockData.set(CustomBlock.VARIANT, CustomBlock.EnumCustomVariant.values()[variant]);
        // final IBlockData blockData = Blocks.FURNACE.getBlockData();
        boolean success = ((CraftChunk) block.getChunk()).getHandle().getWorld().setTypeAndData(position, blockData, 2);
        if (success)
        {
            ((CraftChunk) block.getChunk()).getHandle().getWorld().notify(position);
        }
    }
    
    @Override
    public ItemStack createItemStackForBlock(int type, int variant, String displayName)
    {
        @SuppressWarnings("deprecation")
        final ItemStack stack = new ItemStack(type, 1, (byte) variant);
        final ItemMeta meta = CraftItemFactory.instance().getItemMeta(Material.APPLE);
        meta.setDisplayName(displayName);
        setMeta(stack, meta);
        return stack;
    }

    @Override
    public ItemStack createItemStackForItem(int numId, String name)
    {
        @SuppressWarnings("deprecation")
        final ItemStack stack = new ItemStack(numId, 1);
        final ItemMeta meta = CraftItemFactory.instance().getItemMeta(Material.APPLE);
        meta.setDisplayName(name);
        setMeta(stack, meta);
        return stack;
    }
    
    /**
     * @param stack
     * @param meta
     */
    public static void setMeta(ItemStack stack, ItemMeta meta)
    {
        try
        {
            final Field field = stack.getClass().getDeclaredField("meta"); //$NON-NLS-1$
            field.setAccessible(true);
            field.set(stack, meta);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    @Override
    public void setDisplayName(ItemStack stack, String displayName)
    {
        ItemMeta meta = getMeta(stack);
        if (meta == null)
        {
            meta = CraftItemFactory.instance().getItemMeta(Material.APPLE);
            setMeta(stack, meta);
        }
        meta.setDisplayName(displayName);
    }
    
    @Override
    public void setDescription(ItemStack stack, String[] description)
    {
        ItemMeta meta = getMeta(stack);
        if (meta == null)
        {
            meta = CraftItemFactory.instance().getItemMeta(Material.APPLE);
            setMeta(stack, meta);
        }
        meta.setLore(Arrays.asList(description));
    }
    
    /**
     * @param stack
     * @return meta
     */
    public static ItemMeta getMeta(ItemStack stack)
    {
        try
        {
            if (stack instanceof CraftItemStack)
            {
                return stack.getItemMeta();
            }
            
            final Field field = stack.getClass().getDeclaredField("meta"); //$NON-NLS-1$
            field.setAccessible(true);
            return (ItemMeta) field.get(stack);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    @Override
    public void initBlocks()
    {
        try
        {
            final Method itemMth = Item.class.getDeclaredMethod("a", net.minecraft.server.v1_8_R3.Block.class, Item.class); //$NON-NLS-1$
            itemMth.setAccessible(true);
            
            final Field byId = Material.class.getDeclaredField("byId"); //$NON-NLS-1$
            byId.setAccessible(true);
            final Material[] materials = Arrays.copyOf((Material[]) byId.get(null), 5000);
            byId.set(null, materials);
            
            for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
            {
                materials[i] = Material.STONE; // may not be clever but otherwise bukkit will not really understand what we are doing; we cannot extend the materials enum here :-(
                
                final CustomBlock myBlock = new CustomBlock();
                net.minecraft.server.v1_8_R3.Block.REGISTRY.a(i, new MinecraftKey("mclib:custom_" + i), myBlock); //$NON-NLS-1$
                Iterator<IBlockData> iterator2 = myBlock.P().a().iterator();
                while (iterator2.hasNext())
                {
                    IBlockData iblockdata = iterator2.next();
                    int k = net.minecraft.server.v1_8_R3.Block.REGISTRY.b(myBlock) << 4 | myBlock.toLegacyData(iblockdata);
                    net.minecraft.server.v1_8_R3.Block.d.a(iblockdata, k);
                }
                
                itemMth.invoke(null, myBlock, new ItemMultiTexture(myBlock, myBlock, new Function<net.minecraft.server.v1_8_R3.ItemStack, String>() {
                    @Override
                    @Nullable
                    public String apply(@Nullable net.minecraft.server.v1_8_R3.ItemStack paramItemStack)
                    {
                        return CustomBlock.EnumCustomVariant.values()[paramItemStack.getData()].getName();
                    }
                }).b("mclib:custom_" + i)); //$NON-NLS-1$
                
                // getStaticMethod(TileEntity.class, "a", Class.class, String.class).invoke(null, MyTileEntity.class, "custom");
            }
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, "Problems initializing modded blocks", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public void initItems()
    {
        try
        {
            final Method itemMth = net.minecraft.server.v1_8_R3.Item.class.getDeclaredMethod("a", int.class, String.class, net.minecraft.server.v1_8_R3.Item.class); //$NON-NLS-1$
            itemMth.setAccessible(true);
            
            final Field byId = Material.class.getDeclaredField("byId"); //$NON-NLS-1$
            byId.setAccessible(true);
            final Material[] materials = Arrays.copyOf((Material[]) byId.get(null), 5000);
            byId.set(null, materials);
            
            for (int i = MclibConstants.MIN_ITEM_ID; i <= MclibConstants.MAX_ITEM_ID; i++)
            {
                materials[i] = Material.DIAMOND_AXE; // may not be clever but otherwise bukkit will not really understand what we are doing; we cannot extend the materials enum here :-(
                
                final CustomItem myItem = new CustomItem();
                itemMth.invoke(null, i, "custom_" + i, myItem.c("mclib:custom_" + i)); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, "Problems initializing modded items", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public String getDisplayName(ItemStack stack)
    {
        final ItemMeta meta = ItemHelper1_8_5.getMeta(stack);
        return meta == null ? null : meta.getDisplayName();
    }
    
    @Override
    public String[] getDescription(ItemStack stack)
    {
        final ItemMeta meta = ItemHelper1_8_5.getMeta(stack);
        return meta == null ? null : meta.getLore().toArray(new String[0]);
    }
    
    @Override
    public void createMinable(Random random, Location location, int blockId, int meta, int size)
    {
        final BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final net.minecraft.server.v1_8_R3.Block block = net.minecraft.server.v1_8_R3.Block.getById(blockId);
        final IBlockData data = block.fromLegacyData(meta);
        final WorldGenMinable minable = new WorldGenMinable(data, size);
        minable.generate(((CraftWorld) location.getWorld()).getHandle(), random, pos);
    }
    
    @Override
    public ItemStack addToInventory(Inventory inventory, ItemStack item)
    {
        @SuppressWarnings("deprecation")
        final int typeId = item.getTypeId();
        final int meta = this.getVariant(item);
        
        while (true)
        {
            int firstPartial = firstPartial(inventory, typeId, meta);
            if (firstPartial == -1)
            {
                int firstFree = inventory.firstEmpty();
                if (firstFree == -1)
                {
                    return item;
                }
                if (item.getAmount() > inventory.getMaxStackSize())
                {
                    @SuppressWarnings("deprecation")
                    final ItemStack stack = new ItemStack(typeId, inventory.getMaxStackSize(), (short) meta);
                    InventoryManager1_8_5.setContents((CraftInventory) inventory, stack, firstFree);
                    item.setAmount(item.getAmount() - inventory.getMaxStackSize());
                }
                InventoryManager1_8_5.setContents((CraftInventory) inventory, item, firstFree);
                break;
            }
            ItemStack partialItem = inventory.getItem(firstPartial);
            int amount = item.getAmount();
            int partialAmount = partialItem.getAmount();
            int maxAmount = partialItem.getMaxStackSize();
            
            if (amount + partialAmount <= maxAmount)
            {
                partialItem.setAmount(amount + partialAmount);
                InventoryManager1_8_5.setContents((CraftInventory) inventory, partialItem, firstPartial);
                break;
            }
            
            partialItem.setAmount(maxAmount);
            
            InventoryManager1_8_5.setContents((CraftInventory) inventory, partialItem, firstPartial);
            item.setAmount(amount + partialAmount - maxAmount);
        }
        
        return null;
    }
    
    /**
     * Returns the first partial
     * 
     * @param inv
     * @param typeId
     * @param meta
     * @return partial index
     */
    @SuppressWarnings("deprecation")
    private int firstPartial(Inventory inv, int typeId, int meta)
    {
        ItemStack[] inventory = inv.getContents();
        for (int i = 0; i < inventory.length; ++i)
        {
            ItemStack item = inventory[i];
            if ((item != null) && (item.getTypeId() == typeId) && (item.getAmount() < item.getMaxStackSize()) && meta == getVariant(item))
            {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void setBlockMeta(int blockId, float hardness, float resistence, NmsDropRuleInterface dropRule)
    {
        ((CustomBlock) net.minecraft.server.v1_8_R3.Block.getById(blockId)).setMeta(hardness, resistence, dropRule);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void installFurnaceRecipe(int blockId, int variant, ItemStack stack, float experience)
    {
        RecipesFurnace.getInstance().a(InventoryManager1_8_5.convertToNms(new ItemStack(blockId, 1, (short) variant)), InventoryManager1_8_5.convertToNms(stack), experience);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void installFurnaceRecipe(int itemId, ItemStack receipe, float experience)
    {
        RecipesFurnace.getInstance().a(InventoryManager1_8_5.convertToNms(new ItemStack(itemId, 1)), InventoryManager1_8_5.convertToNms(receipe), experience);
    }
    
    @Override
    public void installFurnaceRecipe(Material material, short itemStackDurability, ItemStack receipe, float experience)
    {
        LOGGER.log(Level.WARNING, "Problems installing furnace recipe for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @Override
    public void initNmsItem(Material material)
    {
        // silently ignore; may be used in future versions to support unmodded items in recipe etc.
    }
    
    @Override
    public void setStackSize(Material material, short itemStackDurability, int stackSize)
    {
        LOGGER.log(Level.WARNING, "Problems setting stack size for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @Override
    public void setStackSize(int blockId, int stackSize)
    {
        Item.getById(blockId).c(stackSize);
    }
    
    @Override
    public void installShapedRecipe(ItemStack item, int amount, String[] shape, Map<Character, ItemStack> ingred)
    {
        int datalen = shape.length;
        datalen += ingred.size() * 2;
        int i = 0;
        Object[] data = new Object[datalen];
        for (; i < shape.length; ++i)
        {
            data[i] = shape[i];
        }
        for (Iterator<Character> localIterator = ingred.keySet().iterator(); localIterator.hasNext();)
        {
            char c = localIterator.next().charValue();
            ItemStack mdata = ingred.get(Character.valueOf(c));
            if (mdata != null)
            {
                data[i] = Character.valueOf(c);
                ++i;
                @SuppressWarnings("deprecation")
                int id = mdata.getTypeId();
                short dmg = mdata.getDurability();
                data[i] = new net.minecraft.server.v1_8_R3.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
                ++i;
            }
        }
        final net.minecraft.server.v1_8_R3.ItemStack nms = InventoryManager1_8_5.convertToNms(item);
        nms.count = amount;
        CraftingManager.getInstance().registerShapedRecipe(nms, data);
        CraftingManager.getInstance().sort();
    }
    
    @Override
    public void installShapedRecipe(int blockId, int variant, int amount, String[] shape, Map<Character, ItemStack> ingred)
    {
        int datalen = shape.length;
        datalen += ingred.size() * 2;
        int i = 0;
        Object[] data = new Object[datalen];
        for (; i < shape.length; ++i)
        {
            data[i] = shape[i];
        }
        for (Iterator<Character> localIterator = ingred.keySet().iterator(); localIterator.hasNext();)
        {
            char c = localIterator.next().charValue();
            ItemStack mdata = ingred.get(Character.valueOf(c));
            if (mdata != null)
            {
                data[i] = Character.valueOf(c);
                ++i;
                @SuppressWarnings("deprecation")
                int id = mdata.getTypeId();
                short dmg = mdata.getDurability();
                data[i] = new net.minecraft.server.v1_8_R3.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
                ++i;
            }
        }
        final net.minecraft.server.v1_8_R3.ItemStack nms = new net.minecraft.server.v1_8_R3.ItemStack(Item.getById(blockId), amount, (short) variant);
        CraftingManager.getInstance().registerShapedRecipe(nms, data);
        CraftingManager.getInstance().sort();
    }
    
    @Override
    public void installShapelessRecipe(ItemStack item, int amount, ItemStack[] shapelessItems)
    {
        Object[] data = new Object[shapelessItems.length];
        int i = 0;
        for (org.bukkit.inventory.ItemStack mdata : shapelessItems)
        {
            @SuppressWarnings("deprecation")
            int id = mdata.getTypeId();
            short dmg = mdata.getDurability();
            data[i] = new net.minecraft.server.v1_8_R3.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
            ++i;
        }
        final net.minecraft.server.v1_8_R3.ItemStack nms = InventoryManager1_8_5.convertToNms(item);
        nms.count = amount;
        CraftingManager.getInstance().registerShapelessRecipe(nms, data);
        CraftingManager.getInstance().sort();
    }
    
    @Override
    public void installShapelessRecipe(int blockId, int variant, int amount, ItemStack[] shapelessItems)
    {
        Object[] data = new Object[shapelessItems.length];
        int i = 0;
        for (org.bukkit.inventory.ItemStack mdata : shapelessItems)
        {
            @SuppressWarnings("deprecation")
            int id = mdata.getTypeId();
            short dmg = mdata.getDurability();
            data[i] = new net.minecraft.server.v1_8_R3.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
            ++i;
        }
        final net.minecraft.server.v1_8_R3.ItemStack nms = new net.minecraft.server.v1_8_R3.ItemStack(Item.getById(blockId), amount, (short) variant);
        CraftingManager.getInstance().registerShapelessRecipe(nms, data);
        CraftingManager.getInstance().sort();
    }

    @Override
    public void setItemMeta(int itemId, double damage, double speed, float damageVsEntity, int itemEnchantability)
    {
        final CustomItem item = (CustomItem) Item.getById(itemId);
        item.setAttackModifiers(damage, speed);
        item.setDmgVsEntity(damageVsEntity);
        item.setItemEnchantability(itemEnchantability);
    }

    @Override
    public void setItemMeta(Material material, short itemStackDurability, double damage, double speed, float damageVsEntity, int itemEnchantability)
    {
        LOGGER.log(Level.WARNING, "Problems installing item meta for unmodded items; not yet supported"); //$NON-NLS-1$
    }

    @Override
    public void setItemRules(int itemId, int durability, NmsItemRuleInterface nmsItemRule)
    {
        final CustomItem item = (CustomItem) Item.getById(itemId);
        item.setMaxDurability(durability);
        item.setItemRules(nmsItemRule);
    }

    @Override
    public void setItemRules(Material material, short itemStackDurability, int durability, NmsItemRuleInterface nmsItemRule)
    {
        LOGGER.log(Level.WARNING, "Problems installing item rules for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
}
