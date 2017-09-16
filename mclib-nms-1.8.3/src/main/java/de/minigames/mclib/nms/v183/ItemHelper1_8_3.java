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

package de.minigames.mclib.nms.v183;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.google.common.base.Function;

import de.minigames.mclib.nms.v183.blocks.CustomBlock;
import de.minigames.mclib.nms.v183.blocks.CustomDropper;
import de.minigames.mclib.nms.v183.blocks.CustomHopper;
import de.minigames.mclib.nms.v183.items.ConfigItemStackDataImpl;
import de.minigames.mclib.nms.v183.items.CustomArmor;
import de.minigames.mclib.nms.v183.items.CustomAxe;
import de.minigames.mclib.nms.v183.items.CustomHoe;
import de.minigames.mclib.nms.v183.items.CustomItem;
import de.minigames.mclib.nms.v183.items.CustomPickaxe;
import de.minigames.mclib.nms.v183.items.CustomShovel;
import de.minigames.mclib.nms.v183.items.CustomSword;
import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.items.BlockDropperRuleInterface;
import de.minigameslib.mclib.api.items.BlockHopperRuleInterface;
import de.minigameslib.mclib.api.items.ItemArmor.ArmorSlot;
import de.minigameslib.mclib.nms.api.ChunkDataImpl;
import de.minigameslib.mclib.nms.api.ChunkDataImpl.TileEntityData;
import de.minigameslib.mclib.nms.api.EnumFactory;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.NmsDropRuleInterface;
import de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface;
import de.minigameslib.mclib.nms.api.NmsItemRuleInterface;
import de.minigameslib.mclib.pshared.MclibConstants;
import de.minigameslib.mclib.shared.api.com.DataSection;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.CraftingManager;
import net.minecraft.server.v1_8_R2.IBlockData;
import net.minecraft.server.v1_8_R2.Item;
import net.minecraft.server.v1_8_R2.ItemArmor;
import net.minecraft.server.v1_8_R2.ItemMultiTexture;
import net.minecraft.server.v1_8_R2.MinecraftKey;
import net.minecraft.server.v1_8_R2.NBTReadLimiter;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.RecipesFurnace;
import net.minecraft.server.v1_8_R2.RegistryID;
import net.minecraft.server.v1_8_R2.TileEntity;
import net.minecraft.server.v1_8_R2.WorldGenMinable;

/**
 * Item helper impl.
 * 
 * @author mepeisen
 *
 */
public class ItemHelper1_8_3 implements ItemHelperInterface
{
    
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(ItemHelper1_8_3.class.getName());
    
    @Override
    public void addCustomData(ItemStack stack, String plugin, String key, String value)
    {
        if (stack instanceof CraftItemStack)
        {
            try
            {
                final Field field = stack.getClass().getDeclaredField("handle"); //$NON-NLS-1$
                field.setAccessible(true);
                final net.minecraft.server.v1_8_R2.ItemStack nms = (net.minecraft.server.v1_8_R2.ItemStack) field.get(stack);
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
                final net.minecraft.server.v1_8_R2.ItemStack nms = (net.minecraft.server.v1_8_R2.ItemStack) field.get(stack);
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
        ((CraftWorld) block.getWorld()).getHandle().setTypeAndData(position, net.minecraft.server.v1_8_R2.Blocks.AIR.getBlockData(), 0);
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(type);
        @SuppressWarnings("deprecation")
        final IBlockData blockData = nms.fromLegacyData(variant);
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
     * sets meta data to item stack.
     * 
     * @param stack
     *            target stack
     * @param meta
     *            meta data
     */
    public static void setMeta(ItemStack stack, ItemMeta meta)
    {
        try
        {
            final Field field = ItemStack.class.getDeclaredField("meta"); //$NON-NLS-1$
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
        }
        meta.setDisplayName(displayName);
        setMeta(stack, meta);
    }
    
    @Override
    public void setDescription(ItemStack stack, String[] description)
    {
        ItemMeta meta = getMeta(stack);
        if (meta == null)
        {
            meta = CraftItemFactory.instance().getItemMeta(Material.APPLE);
        }
        meta.setLore(Arrays.asList(description));
        setMeta(stack, meta);
    }
    
    /**
     * Returns items meta data.
     * 
     * @param stack
     *            target stack
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
            
            final Field field = ItemStack.class.getDeclaredField("meta"); //$NON-NLS-1$
            field.setAccessible(true);
            return (ItemMeta) field.get(stack);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Creates a local bukkit material.
     * 
     * @param materials
     *            materials array
     * @param byName
     *            material by name
     * @param id
     *            numeric id
     * @param name
     *            material name
     * @param ctor
     *            constructor
     * @param maxStack
     *            max stack size
     * @param durability
     *            durability
     * @return new bukkit material.
     */
    private Material createBukkitMaterial(Material[] materials, Map<String, Material> byName, int id, String name, Constructor<? extends MaterialData> ctor, int maxStack, short durability)
    {
        final Material result = EnumFactory.addEnum(Material.class, name, new Class<?>[] { int.class, int.class, int.class }, new Object[] { id, maxStack, durability });
        setPrivateField(result, "ctor", ctor); //$NON-NLS-1$
        return result;
    }
    
    /**
     * Sets a private field.
     * 
     * @param target
     *            target object
     * @param name
     *            field name
     * @param value
     *            new field value
     */
    private void setPrivateField(Object target, String name, Object value)
    {
        try
        {
            final Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(target, value);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, "Problems initializing modded items", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public void initBlocks()
    {
        try
        {
            final Method itemMth = Item.class.getDeclaredMethod("a", net.minecraft.server.v1_8_R2.Block.class, Item.class); //$NON-NLS-1$
            itemMth.setAccessible(true);
            
            final Field byId = Material.class.getDeclaredField("byId"); //$NON-NLS-1$
            byId.setAccessible(true);
            final Material[] materials = Arrays.copyOf((Material[]) byId.get(null), 5000);
            byId.set(null, materials);
            
            final Field byName = Material.class.getDeclaredField("BY_NAME"); //$NON-NLS-1$
            byName.setAccessible(true);
            @SuppressWarnings("unchecked")
            final Map<String, Material> names = (Map<String, Material>) byName.get(null);
            
            final Constructor<? extends MaterialData> ctor = MaterialData.class.getDeclaredConstructor(int.class, byte.class);
            
            for (int i = MclibConstants.MIN_BLOCK_ID; i <= MclibConstants.MAX_BLOCK_ID; i++)
            {
                materials[i] = createBukkitMaterial(materials, names, i, "custom_" + i, ctor, 64, (short) 0); //$NON-NLS-1$
                
                final CustomBlock myBlock = new CustomBlock();
                net.minecraft.server.v1_8_R2.Block.REGISTRY.a(i, new MinecraftKey("mclib:custom_" + i), myBlock); //$NON-NLS-1$
                Iterator<IBlockData> iterator2 = myBlock.P().a().iterator();
                while (iterator2.hasNext())
                {
                    IBlockData iblockdata = iterator2.next();
                    int k = net.minecraft.server.v1_8_R2.Block.REGISTRY.b(myBlock) << 4 | myBlock.toLegacyData(iblockdata);
                    net.minecraft.server.v1_8_R2.Block.d.a(iblockdata, k);
                }
                
                itemMth.invoke(null, myBlock, new ItemMultiTexture(myBlock, myBlock, new Function<net.minecraft.server.v1_8_R2.ItemStack, String>() {
                    @Override
                    @Nullable
                    public String apply(@Nullable net.minecraft.server.v1_8_R2.ItemStack paramItemStack)
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
            final Method itemMth = Item.class.getDeclaredMethod("a", int.class, String.class, Item.class); //$NON-NLS-1$
            itemMth.setAccessible(true);
            
            final Field byId = Material.class.getDeclaredField("byId"); //$NON-NLS-1$
            byId.setAccessible(true);
            final Material[] materials = Arrays.copyOf((Material[]) byId.get(null), 5000);
            byId.set(null, materials);
            
            final Field byName = Material.class.getDeclaredField("BY_NAME"); //$NON-NLS-1$
            byName.setAccessible(true);
            @SuppressWarnings("unchecked")
            final Map<String, Material> names = (Map<String, Material>) byName.get(null);
            
            final Constructor<? extends MaterialData> ctor = MaterialData.class.getDeclaredConstructor(int.class, byte.class);
            
            for (int i = MclibConstants.MIN_ITEM_ID; i <= MclibConstants.MAX_ITEM_ID; i++)
            {
                materials[i] = createBukkitMaterial(materials, names, i, "custom_" + i, ctor, 64, (short) 0); //$NON-NLS-1$
                
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
        final ItemMeta meta = ItemHelper1_8_3.getMeta(stack);
        return meta == null ? null : meta.getDisplayName();
    }
    
    @Override
    public String[] getDescription(ItemStack stack)
    {
        final ItemMeta meta = ItemHelper1_8_3.getMeta(stack);
        return meta == null ? null : meta.getLore().toArray(new String[0]);
    }
    
    @Override
    public void createMinable(Random random, Location location, int blockId, int meta, int size)
    {
        final BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(blockId);
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
                    InventoryManager1_8_3.setContents((CraftInventory) inventory, stack, firstFree);
                    item.setAmount(item.getAmount() - inventory.getMaxStackSize());
                }
                InventoryManager1_8_3.setContents((CraftInventory) inventory, item, firstFree);
                break;
            }
            ItemStack partialItem = inventory.getItem(firstPartial);
            int amount = item.getAmount();
            int partialAmount = partialItem.getAmount();
            int maxAmount = partialItem.getMaxStackSize();
            
            if (amount + partialAmount <= maxAmount)
            {
                partialItem.setAmount(amount + partialAmount);
                InventoryManager1_8_3.setContents((CraftInventory) inventory, partialItem, firstPartial);
                break;
            }
            
            partialItem.setAmount(maxAmount);
            
            InventoryManager1_8_3.setContents((CraftInventory) inventory, partialItem, firstPartial);
            item.setAmount(amount + partialAmount - maxAmount);
        }
        
        return null;
    }
    
    /**
     * Returns the first partial.
     * 
     * @param inv
     *            inventory
     * @param typeId
     *            type id
     * @param meta
     *            meta data
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
        ((CustomBlock) net.minecraft.server.v1_8_R2.Block.getById(blockId)).setMeta(hardness, resistence, dropRule);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void installFurnaceRecipe(int blockId, int variant, ItemStack stack, float experience)
    {
        RecipesFurnace.getInstance().a(InventoryManager1_8_3.convertToNms(new ItemStack(blockId, 1, (short) variant)), InventoryManager1_8_3.convertToNms(stack), experience);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void installFurnaceRecipe(int itemId, ItemStack receipe, float experience)
    {
        RecipesFurnace.getInstance().a(InventoryManager1_8_3.convertToNms(new ItemStack(itemId, 1)), InventoryManager1_8_3.convertToNms(receipe), experience);
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
                data[i] = new net.minecraft.server.v1_8_R2.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
                ++i;
            }
        }
        final net.minecraft.server.v1_8_R2.ItemStack nms = InventoryManager1_8_3.convertToNms(item);
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
                data[i] = new net.minecraft.server.v1_8_R2.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
                ++i;
            }
        }
        final net.minecraft.server.v1_8_R2.ItemStack nms = new net.minecraft.server.v1_8_R2.ItemStack(Item.getById(blockId), amount, (short) variant);
        CraftingManager.getInstance().registerShapedRecipe(nms, data);
        CraftingManager.getInstance().sort();
    }
    
    @Override
    public void installShapelessRecipe(ItemStack item, int amount, ItemStack[] shapelessItems)
    {
        Object[] data = new Object[shapelessItems.length];
        int i = 0;
        for (ItemStack mdata : shapelessItems)
        {
            @SuppressWarnings("deprecation")
            int id = mdata.getTypeId();
            short dmg = mdata.getDurability();
            data[i] = new net.minecraft.server.v1_8_R2.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
            ++i;
        }
        final net.minecraft.server.v1_8_R2.ItemStack nms = InventoryManager1_8_3.convertToNms(item);
        nms.count = amount;
        CraftingManager.getInstance().registerShapelessRecipe(nms, data);
        CraftingManager.getInstance().sort();
    }
    
    @Override
    public void installShapelessRecipe(int blockId, int variant, int amount, ItemStack[] shapelessItems)
    {
        Object[] data = new Object[shapelessItems.length];
        int i = 0;
        for (ItemStack mdata : shapelessItems)
        {
            @SuppressWarnings("deprecation")
            int id = mdata.getTypeId();
            short dmg = mdata.getDurability();
            data[i] = new net.minecraft.server.v1_8_R2.ItemStack(Item.getById(id), mdata.getAmount(), dmg);
            ++i;
        }
        final net.minecraft.server.v1_8_R2.ItemStack nms = new net.minecraft.server.v1_8_R2.ItemStack(Item.getById(blockId), amount, (short) variant);
        CraftingManager.getInstance().registerShapelessRecipe(nms, data);
        CraftingManager.getInstance().sort();
    }
    
    /**
     * Replaces an existing block with new modded block.
     * @param numBlockId numeric block id
     * @param block new block impl
     */
    private void replaceModdedBlock(int numBlockId, net.minecraft.server.v1_8_R2.Block block)
    {
        try
        {
            final Field bMapField = net.minecraft.server.v1_8_R2.RegistryMaterials.class.getDeclaredField("b"); //$NON-NLS-1$
            bMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final Map<net.minecraft.server.v1_8_R2.Block, MinecraftKey> bMap = (Map<net.minecraft.server.v1_8_R2.Block, MinecraftKey>) bMapField.get(net.minecraft.server.v1_8_R2.Block.REGISTRY);
            
            final Field aMapField = net.minecraft.server.v1_8_R2.RegistryMaterials.class.getDeclaredField("a"); //$NON-NLS-1$
            aMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final RegistryID<net.minecraft.server.v1_8_R2.Block> aMap = (RegistryID<net.minecraft.server.v1_8_R2.Block>) aMapField.get(net.minecraft.server.v1_8_R2.Block.REGISTRY);
            
            bMap.put(block, bMap.remove(net.minecraft.server.v1_8_R2.Block.getById(numBlockId)));
            aMap.a(block, numBlockId);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, "Problems initializing modded blocks", ex); //$NON-NLS-1$
        }
    }
    
    /**
     * Replaces a custom item with given modded (=special) item.
     * 
     * @param itemId
     *            numeric item id
     * @param newItem
     *            new item
     */
    private void replaceModdedItem(int itemId, Item newItem)
    {
        try
        {
            final Field bMapField = net.minecraft.server.v1_8_R2.RegistryMaterials.class.getDeclaredField("b"); //$NON-NLS-1$
            bMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final Map<Item, MinecraftKey> bMap = (Map<Item, MinecraftKey>) bMapField.get(Item.REGISTRY);
            
            final Field aMapField = net.minecraft.server.v1_8_R2.RegistryMaterials.class.getDeclaredField("a"); //$NON-NLS-1$
            aMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final RegistryID<Item> aMap = (RegistryID<Item>) aMapField.get(Item.REGISTRY);
            
            bMap.put(newItem, bMap.remove(Item.getById(itemId)));
            aMap.a(newItem, itemId);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, "Problems initializing modded items", ex); //$NON-NLS-1$
        }
    }
    
    /**
     * Converts armor slot to item slot.
     * 
     * @param slot
     *            armor slot from mclib
     * @return item slot
     */
    private int toItemSlot(ArmorSlot slot)
    {
        switch (slot)
        {
            case Boots:
                return 3;
            case Chestplate:
                return 1;
            case Helmet:
                return 0;
            case Leggins:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void setHopperRule(int numBlockId, Class<? extends BlockHopperRuleInterface> hopper)
    {
        final CustomHopper block = new CustomHopper();
        block.setHopperRule(hopper);
        this.replaceModdedBlock(numBlockId, block);
    }

    @Override
    public void setDropperRule(int numBlockId, Class<? extends BlockDropperRuleInterface> hopper)
    {
        final CustomDropper block = new CustomDropper();
        block.setDropperRule(hopper);
        this.replaceModdedBlock(numBlockId, block);
    }
    
    @Override
    public void initArmor(int numId, int dmgReduceAmount, int durability, int itemEnchantability, float toughness, ArmorSlot slot, NmsItemRuleInterface nmsItemRule)
    {
        final CustomArmor armor = new CustomArmor(toItemSlot(slot));
        armor.setItemEnchantability(itemEnchantability);
        armor.setItemRules(nmsItemRule);
        armor.setMaxDurability(durability);
        try
        {
            final Field field1 = ItemArmor.class.getDeclaredField("c"); //$NON-NLS-1$
            field1.setAccessible(true);
            field1.set(armor, dmgReduceAmount);
            
            // not supported by < 1.9.4
            // final Field field2 = ItemArmor.class.getDeclaredField("e"); //$NON-NLS-1$
            // field2.setAccessible(true);
            // field2.set(armor, toughness);
            
            this.replaceModdedItem(numId, armor);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.SEVERE, "Problems initializing modded items", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public void initArmor(Material material, short itemStackDurability, int dmgReduceAmount, int itemEnchantability, float toughness, ArmorSlot slot, NmsItemRuleInterface nmsItemRule)
    {
        LOGGER.log(Level.WARNING, "Problems installing item meta for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @Override
    public void initAxe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        final CustomAxe axe = new CustomAxe();
        axe.setMaxDurability(durability);
        axe.setAttackModifiers(damage, speed);
        axe.setItemEnchantability(itemEnchantability);
        axe.setItemRules(nmsItemRule);
        this.replaceModdedItem(numId, axe);
    }
    
    @Override
    public void initAxe(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        LOGGER.log(Level.WARNING, "Problems installing item meta for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @Override
    public void initPickaxe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        final CustomPickaxe pickaxe = new CustomPickaxe();
        pickaxe.setMaxDurability(durability);
        pickaxe.setAttackModifiers(damage, speed);
        pickaxe.setItemEnchantability(itemEnchantability);
        pickaxe.setItemRules(nmsItemRule);
        this.replaceModdedItem(numId, pickaxe);
    }
    
    @Override
    public void initPickaxe(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        LOGGER.log(Level.WARNING, "Problems installing item meta for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @Override
    public void initHoe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        final CustomHoe hoe = new CustomHoe();
        hoe.setMaxDurability(durability);
        hoe.setAttackModifiers(damage, speed);
        hoe.setItemEnchantability(itemEnchantability);
        hoe.setItemRules(nmsItemRule);
        this.replaceModdedItem(numId, hoe);
    }
    
    @Override
    public void initHoe(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        LOGGER.log(Level.WARNING, "Problems installing item meta for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @Override
    public void initShovel(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        final CustomShovel shovel = new CustomShovel();
        shovel.setMaxDurability(durability);
        shovel.setAttackModifiers(damage, speed);
        shovel.setItemEnchantability(itemEnchantability);
        shovel.setItemRules(nmsItemRule);
        this.replaceModdedItem(numId, shovel);
    }
    
    @Override
    public void initShovel(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        LOGGER.log(Level.WARNING, "Problems installing item meta for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @Override
    public void initSword(int numId, int durability, float damageVsEntity, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        final CustomSword sword = new CustomSword();
        sword.setDmgVsEntity(damageVsEntity);
        sword.setMaxDurability(durability);
        sword.setAttackModifiers(damage, speed);
        sword.setItemEnchantability(itemEnchantability);
        sword.setItemRules(nmsItemRule);
        this.replaceModdedItem(numId, sword);
    }
    
    @Override
    public void initSword(Material material, short itemStackDurability, float damageVsEntity, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule)
    {
        LOGGER.log(Level.WARNING, "Problems installing item meta for unmodded items; not yet supported"); //$NON-NLS-1$
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isPlant(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.PLANT;
    }
    
    @Override
    public boolean isPlant(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.PLANT;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isGourd(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.PUMPKIN;
    }
    
    @Override
    public boolean isGourd(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.PUMPKIN;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCoral(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.CORAL;
    }
    
    @Override
    public boolean isCoral(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.CORAL;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isGrass(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.GRASS;
    }
    
    @Override
    public boolean isGrass(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.GRASS;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isWood(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.WOOD;
    }
    
    @Override
    public boolean isWood(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.WOOD;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isRock(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.STONE;
    }
    
    @Override
    public boolean isRock(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.STONE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isOre(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.ORE;
    }
    
    @Override
    public boolean isOre(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.ORE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isHeavy(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.HEAVY;
    }
    
    @Override
    public boolean isHeavy(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.HEAVY;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isWater(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.WATER;
    }
    
    @Override
    public boolean isWater(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.WATER;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isLava(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.LAVA;
    }
    
    @Override
    public boolean isLava(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.LAVA;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isLeaves(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.LEAVES;
    }
    
    @Override
    public boolean isLeaves(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.LEAVES;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isVine(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.REPLACEABLE_PLANT;
    }
    
    @Override
    public boolean isVine(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.REPLACEABLE_PLANT;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isSponge(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.SPONGE;
    }
    
    @Override
    public boolean isSponge(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.SPONGE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCloth(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.CLOTH;
    }
    
    @Override
    public boolean isCloth(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.CLOTH;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFire(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.FIRE;
    }
    
    @Override
    public boolean isFire(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.FIRE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isSand(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.SAND;
    }
    
    @Override
    public boolean isSand(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.SAND;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCircuits(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.ORIENTABLE;
    }
    
    @Override
    public boolean isCircuits(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.ORIENTABLE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCarpet(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.WOOL;
    }
    
    @Override
    public boolean isCarpet(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.WOOL;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isGlass(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.SHATTERABLE;
    }
    
    @Override
    public boolean isGlass(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.SHATTERABLE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isRedstoneLight(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.BUILDABLE_GLASS;
    }
    
    @Override
    public boolean isRedstoneLight(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.BUILDABLE_GLASS;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isTnt(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.TNT;
    }
    
    @Override
    public boolean isTnt(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.TNT;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isIce(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.ICE;
    }
    
    @Override
    public boolean isIce(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.ICE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isPackedIce(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.PACKED_ICE;
    }
    
    @Override
    public boolean isPackedIce(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.PACKED_ICE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isSnow(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.SNOW_LAYER;
    }
    
    @Override
    public boolean isSnow(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.SNOW_LAYER;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCraftedSnow(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.SNOW_BLOCK;
    }
    
    @Override
    public boolean isCraftedSnow(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.SNOW_BLOCK;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCactus(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.CACTUS;
    }
    
    @Override
    public boolean isCactus(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.CACTUS;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isClay(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.CLAY;
    }
    
    @Override
    public boolean isClay(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.CLAY;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isDragonEgg(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.DRAGON_EGG;
    }
    
    @Override
    public boolean isDragonEgg(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.DRAGON_EGG;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isPortal(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.PORTAL;
    }
    
    @Override
    public boolean isPortal(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.PORTAL;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCake(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.CAKE;
    }
    
    @Override
    public boolean isCake(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.CAKE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isWeb(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.WEB;
    }
    
    @Override
    public boolean isWeb(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.WEB;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isPiston(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.PISTON;
    }
    
    @Override
    public boolean isPiston(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.PISTON;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isBarrier(Material material)
    {
        final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.getById(material.getId());
        return block.getMaterial() == net.minecraft.server.v1_8_R2.Material.BANNER;
    }
    
    @Override
    public boolean isBarrier(int block)
    {
        final net.minecraft.server.v1_8_R2.Block nms = net.minecraft.server.v1_8_R2.Block.getById(block);
        return nms.getMaterial() == net.minecraft.server.v1_8_R2.Material.BANNER;
    }
    
    @Override
    public boolean isStructureVoid(Material material)
    {
        return false;
    }
    
    @Override
    public boolean isStructureVoid(int block)
    {
        return false;
    }
    
    @Override
    public void initInventory(int blockId, int variant, NmsInventoryHandlerInterface nmsInventoryHandler)
    {
        final CustomBlock block = (CustomBlock) net.minecraft.server.v1_8_R2.Block.getById(blockId);
        block.setInventoryHandler(variant, nmsInventoryHandler);
    }
    
    @Override
    public ConfigItemStackData toConfigData(ItemStack stack)
    {
        return new ConfigItemStackDataImpl(stack);
    }
    
    @Override
    public ConfigItemStackData fromConfigData(DataSection section)
    {
        final ConfigItemStackData result = new ConfigItemStackDataImpl();
        result.read(section);
        return result;
    }

    @Override
    public Map<String, Integer> getBlockMappings()
    {
        final Map<String, Integer> result = new HashMap<>();
        for (final MinecraftKey key : net.minecraft.server.v1_8_R2.Block.REGISTRY.keySet())
        {
            final String name = key.toString();
            for (int i = 0; i < 16; i++)
            {
                final net.minecraft.server.v1_8_R2.Block block = net.minecraft.server.v1_8_R2.Block.REGISTRY.get(key);
                final int numid = net.minecraft.server.v1_8_R2.Block.REGISTRY.b(block);
                result.put(name + ":" + i, numid << 4 | i); //$NON-NLS-1$
            }
        }
        return result;
    }

    @Override
    public ChunkDataImpl getChunkSnapshot(Chunk chunk)
    {
        // persist tiles
        final List<TileEntityData> tiles = new ArrayList<>();
        final net.minecraft.server.v1_8_R2.Chunk handle = ((CraftChunk)chunk).getHandle();
        final Map<BlockPosition, TileEntity> origTiles = handle.getTileEntities();
        for (final Map.Entry<BlockPosition, TileEntity> entry : origTiles.entrySet())
        {
            final NBTTagCompound tag = new NBTTagCompound();
            entry.getValue().b(tag);
            byte[] bindata = new byte[0];
            
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final DataOutputStream dos = new DataOutputStream(baos);
            try
            {
                final Method mth = NBTTagCompound.class.getDeclaredMethod("write", DataOutput.class); //$NON-NLS-1$
                mth.setAccessible(true);
                mth.invoke(tag, dos);
                bindata = baos.toByteArray();
            }
            catch (Exception ex)
            {
                LOGGER.log(Level.WARNING, "Problems getting tile entity data", ex); //$NON-NLS-1$
            }
            
            final TileEntityData data = new TileEntityData(bindata, tag.getString("id"), entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ()); //$NON-NLS-1$
            tiles.add(data);
        }
        
        // persist item data
        final int[][][] blockData = new int[16][][];
        for (int x = 0; x < 16; x++)
        {
            blockData[x] = new int[256][];
            for (int y = 0; y < 256; y++)
            {
                blockData[x][y] = new int[16];
                for (int z = 0; z < 16; z++)
                {
                    final IBlockData data = handle.getBlockData(new BlockPosition(x, y, z));
                    blockData[x][y][z] = net.minecraft.server.v1_8_R2.Block.d.b(data);
                }
            }
        }
        
        final ChunkDataImpl result = new ChunkDataImpl(chunk.getX(), chunk.getZ(), tiles, blockData);
        return result;
    }

    @Override
    public void setBlocks(Location location, Map<Integer, Integer> blockMappings, int[] blocks)
    {
        for (int z = 0; z < blocks.length; z++)
        {
            final int targetId = blockMappings.get(blocks[z]);
            final Location newLoc = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ() + z);
            this.setBlockVariant(newLoc.getBlock(), targetId >> 4, targetId & 0xF);
        }
    }

    @Override
    public void loadTileEntities(Location loc, List<TileEntityData> data)
    {
        for (final TileEntityData tiledata : data)
        {
            final Location newLoc = new Location(loc.getWorld(), loc.getBlockX() + tiledata.getXcoord(), loc.getBlockY() + tiledata.getYcoord(), loc.getBlockZ() + tiledata.getZcoord());

            final NBTTagCompound tag = new NBTTagCompound();
            final ByteArrayInputStream bais = new ByteArrayInputStream(tiledata.getData());
            final DataInputStream dis = new DataInputStream(bais);

            try
            {
                final Method mth = NBTTagCompound.class.getDeclaredMethod("load", DataInput.class, int.class, NBTReadLimiter.class); //$NON-NLS-1$
                mth.setAccessible(true);
                mth.invoke(tag, dis, 0, NBTReadLimiter.a);
            }
            catch (Exception ex)
            {
                LOGGER.log(Level.WARNING, "Problems setting tile entity tag data", ex); //$NON-NLS-1$
            }
            
            tag.setInt("x", newLoc.getBlockX()); //$NON-NLS-1$
            tag.setInt("y", newLoc.getBlockY()); //$NON-NLS-1$
            tag.setInt("z", newLoc.getBlockZ()); //$NON-NLS-1$
            
            ((CraftChunk)newLoc.getChunk()).getHandle().tileEntities.put(
                new BlockPosition(newLoc.getBlockX(), newLoc.getBlockY(), newLoc.getBlockZ()),
                TileEntity.c(tag));
        }
    }
    
}
