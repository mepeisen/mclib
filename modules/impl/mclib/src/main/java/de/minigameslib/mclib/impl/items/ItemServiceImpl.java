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

package de.minigameslib.mclib.impl.items;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.google.common.io.Files;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.MinecraftVersionsType;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.enums.EnumerationListener;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McInventoryClickEvent;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.McPlayerDeathEvent;
import de.minigameslib.mclib.api.event.McPlayerDropItemEvent;
import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mclib.api.items.BlockDropRuleInterface;
import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockInventoryMeta;
import de.minigameslib.mclib.api.items.BlockMeta;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
import de.minigameslib.mclib.api.items.BlockVariantId;
import de.minigameslib.mclib.api.items.CraftingItemInterface;
import de.minigameslib.mclib.api.items.CraftingRecipes;
import de.minigameslib.mclib.api.items.CraftingShapedItem;
import de.minigameslib.mclib.api.items.CraftingShapedRecipe;
import de.minigameslib.mclib.api.items.CraftingShapelessRecipe;
import de.minigameslib.mclib.api.items.FurnaceRecipeInterface;
import de.minigameslib.mclib.api.items.ItemArmor;
import de.minigameslib.mclib.api.items.ItemAxe;
import de.minigameslib.mclib.api.items.ItemDigInterface;
import de.minigameslib.mclib.api.items.ItemDmgInterface;
import de.minigameslib.mclib.api.items.ItemHoe;
import de.minigameslib.mclib.api.items.ItemId;
import de.minigameslib.mclib.api.items.ItemId.ItemClass;
import de.minigameslib.mclib.api.items.ItemPickaxe;
import de.minigameslib.mclib.api.items.ItemRepairInterface;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.items.ItemShovel;
import de.minigameslib.mclib.api.items.ItemSword;
import de.minigameslib.mclib.api.items.ResourceServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.McCoreConfig;
import de.minigameslib.mclib.impl.McModdedWorldConfig;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.NmsDropRuleInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.nms.api.NmsItemRuleInterface;
import de.minigameslib.mclib.pshared.MclibConstants;
import de.minigameslib.mclib.pshared.PingData;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * The item service implementation.
 * 
 * @author mepeisen
 */
public class ItemServiceImpl implements ItemServiceInterface, BlockServiceInterface, ResourceServiceInterface, McListener
{
    
    /** java logger */
    private static final Logger                         LOGGER           = Logger.getLogger(ItemServiceImpl.class.getName());
    
    /** the item id to value map. */
    protected Map<ItemId, CustomItem>                   itemIdMap        = new HashMap<>();
    
    /** the custom items per material/ damage value */
    private final Map<Material, Map<Short, CustomItem>> itemsPerMaterial = new HashMap<>();
    
    /** the block to numId map. */
    private Map<CustomItem, ItemInfo>                   itemMap          = new HashMap<>();
    
    /** the block id to value map. */
    protected Map<BlockId, CustomBlock>                 blockIdMap       = new HashMap<>();
    
    /** the block id to value map. */
    protected Map<Integer, CustomBlock>                 blockNumIdMap    = new HashMap<>();
    
    /** the item id to value map. */
    private Map<Integer, CustomItem>                    itemNumIdMap     = new HashMap<>();
    
    /** the block to numId map. */
    private Map<CustomBlock, Integer>                   blockMap         = new HashMap<>();
    
    /**
     * a list of lazy initialization runnables; invoked by fetching PluginEnableEvent-
     */
    private Map<String, List<Runnable>>                 lazyPluginInit   = new HashMap<>();
    
    /**
     * Initialized the items from registered enumerations
     */
    public void init()
    {
        EnumServiceInterface.instance().registerEnumerationListener((Plugin) McLibInterface.instance(), BlockId.class, new BlockListener());
        EnumServiceInterface.instance().registerEnumerationListener((Plugin) McLibInterface.instance(), ItemId.class, new ItemListener());
        initNmsItems();
        initItems();
        initBlocks();
    }
    
    /**
     * init the nms items
     */
    private void initNmsItems()
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        for (final CustomItemTypes type : CustomItemTypes.values())
        {
            helper.initNmsItem(type.getMaterial());
        }
    }
    
    /**
     * Impl of tool builder.
     * 
     * @author mepeisen
     */
    private class ToolBuilderImpl implements ToolBuilderInterface
    {
        /**
         * tooling id to divide multiple toolings
         */
        private static final String CUSTOMKEY_TOOLING_ID = "toolingId"; //$NON-NLS-1$
        /**
         * marker for clearing tooling on join
         */
        private static final String CUSTOMKEY_CLEAR_ON_JOIN = "clearOnJoin"; //$NON-NLS-1$
        /**
         * tooling marker for custom tooling item
         */
        private static final String CUSTOMKEY_TOOLING_MARKER = "customTooling"; //$NON-NLS-1$
        /**
         * plugin for item custom data
         */
        private static final String CUSTOMDATA_PLUGIN = "mclib"; //$NON-NLS-1$
        
        /** flag for single use */
        private boolean                                                singleUse;
        /** right click handler */
        private McBiConsumer<McPlayerInterface, McPlayerInteractEvent> rightClick;
        /** left click handler */
        private McBiConsumer<McPlayerInterface, McPlayerInteractEvent> leftClick;
        /** description */
        private LocalizedMessageInterface                              description;
        /** description arguments */
        private Serializable[]                                         descriptionArgs;

        /**
         * the player
         */
        private final McPlayerInterface player;
        /**
         * the item factory
         */
        private final Supplier<ItemStack> itemFactory;
        
        /** the target inventory. */
        private Inventory targetInventory;
        /** target slot; -1 for any free slot */
        private int targetSlot = -1;
        /** {@code true} to override existing items in {@code targetSlot} */
        private boolean targetRemoveExisting;
        /** {qcode true} to override any existing tooling; only if {@code targetSlot} if not set */
        private boolean overridePlayerTools = true;
        
        /**
         * @param player
         * @param itemFactory
         */
        public ToolBuilderImpl(McPlayerInterface player, Supplier<ItemStack> itemFactory)
        {
            this.player = player;
            this.itemFactory = itemFactory;
        }

        @Override
        public ToolBuilderInterface singleUse()
        {
            this.singleUse = true;
            return this;
        }
        
        @Override
        public ToolBuilderInterface multiUse()
        {
            this.singleUse = false;
            return this;
        }
        
        @Override
        public ToolBuilderInterface onRightClick(McBiConsumer<McPlayerInterface, McPlayerInteractEvent> handler)
        {
            this.rightClick = handler;
            return this;
        }
        
        @Override
        public ToolBuilderInterface onLeftClick(McBiConsumer<McPlayerInterface, McPlayerInteractEvent> handler)
        {
            this.leftClick = handler;
            return this;
        }
        
        @Override
        public ToolBuilderInterface description(LocalizedMessageInterface desc, Serializable... descargs)
        {
            this.description = desc;
            this.descriptionArgs = descargs;
            return this;
        }
        
        @Override
        public ToolBuilderInterface withInventory(Inventory inventory, int slot, boolean removeExisting, boolean opt)
        {
            this.targetInventory = inventory;
            this.targetSlot = slot;
            this.targetRemoveExisting = removeExisting;
            this.overridePlayerTools = opt;
            return this;
        }
        
        @Override
        public void build()
        {
            ToolMarker marker = this.player.getSessionStorage().get(ToolMarker.class);
            if (marker == null)
            {
                marker = new ToolMarker();
                this.player.getSessionStorage().set(ToolMarker.class, marker);
                this.player.registerHandlers((Plugin) McLibInterface.instance(), ItemServiceImpl.this);
            }
            
            final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
            final Inventory inventory = this.targetInventory == null ? this.player.getBukkitPlayer().getInventory() : this.targetInventory;
            
            if (this.targetSlot >= 0)
            {
                final ItemStack invitem = inventory.getItem(this.targetSlot);
                if (invitem != null)
                {
                    if (!this.targetRemoveExisting)
                    {
                        return;
                    }
                    if (helper.getCustomData(invitem, CUSTOMDATA_PLUGIN, CUSTOMKEY_TOOLING_MARKER) != null)
                    {
                        ItemServiceImpl.this.deleteTooling(inventory, this.targetSlot, invitem, helper, marker);
                    }
                }
                
                this.setTooling(inventory, this.targetSlot, helper, marker);
                return;
            }
            
            // search existing tooling
            int slot = -1;
            if (this.overridePlayerTools)
            {
                for (int i = 0; i < inventory.getSize(); i++)
                {
                    final ItemStack invitem = inventory.getItem(i);
                    if (helper.getCustomData(invitem, CUSTOMDATA_PLUGIN, CUSTOMKEY_TOOLING_MARKER) != null)
                    {
                        slot = i;
                        ItemServiceImpl.this.deleteTooling(inventory, i, invitem, helper, marker);
                        break;
                    }
                }
            }
            if (slot == -1)
            {
                slot = inventory.firstEmpty();
            }

            this.setTooling(inventory, slot, helper, marker);
        }
        
        /**
         * Sets tooling item to given slot
         * @param inventory
         * @param slot
         * @param helper
         * @param marker
         */
        private void setTooling(Inventory inventory, int slot, final ItemHelperInterface helper, ToolMarker marker)
        {
            // preplace item stack
            ItemStack stack = this.itemFactory.get();
            if (this.description != null)
            {
                helper.setDescription(stack, this.player.encodeMessage(this.description, this.descriptionArgs));
            }
            inventory.setItem(slot, stack);
            stack = inventory.getItem(slot); // forces to use the correct NMS class
            
            final String uuid = UUID.randomUUID().toString();
            helper.addCustomData(stack, CUSTOMDATA_PLUGIN, CUSTOMKEY_CLEAR_ON_JOIN, CUSTOMKEY_CLEAR_ON_JOIN);
            helper.addCustomData(stack, CUSTOMDATA_PLUGIN, CUSTOMKEY_TOOLING_MARKER, CUSTOMKEY_TOOLING_MARKER);
            helper.addCustomData(stack, CUSTOMDATA_PLUGIN, CUSTOMKEY_TOOLING_ID, uuid);
            final ToolData data = new ToolData();
            data.setOneUse(this.singleUse);
            data.setLeftClickHandler(this.leftClick);
            data.setRightClickHandler(this.rightClick);
            marker.getTools().put(uuid, data);
            
            this.player.getBukkitPlayer().updateInventory();
        }
    }

    /**
     * @author mepeisen
     *
     */
    private final class NmsItemRule implements NmsItemRuleInterface
    {
        /**
         * 
         */
        private final ItemDmgInterface    dmg;
        /**
         * 
         */
        private final ItemRepairInterface rep;
        /**
         * 
         */
        private final ItemDigInterface    dig;
        
        /**
         * @param dmg
         * @param rep
         * @param dig
         */
        protected NmsItemRule(ItemDmgInterface dmg, ItemRepairInterface rep, ItemDigInterface dig)
        {
            this.dmg = dmg;
            this.rep = rep;
            this.dig = dig;
        }
        
        @Override
        public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
        {
            return this.rep.getIsRepairable(toRepair, repair);
        }
        
        @SuppressWarnings("deprecation")
        @Override
        public float getHarvestSpeed(ItemStack stack, int blockId, int variantId)
        {
            if (blockId < MclibConstants.MIN_BLOCK_ID)
            {
                return this.dig.getHarvestSpeed(stack, Material.getMaterial(blockId));
            }
            final BlockId block = ItemServiceImpl.this.blockNumIdMap.get(blockId).getBlockId();
            return this.dig.getHarvestSpeed(stack, block, block.variants()[variantId]);
        }
        
        @Override
        public int getDamageByEntity(ItemStack stack, Entity target, Player player)
        {
            return this.dmg.getDamageByEntity(stack, target, player);
        }
        
        @Override
        public int getDamageByBlock(ItemStack stack, int blockId, int variantId, Location location, Player player)
        {
            return this.dig.getDamageByBlock(stack, location.getBlock(), player);
        }
        
        @Override
        public boolean canHarvest(int block, int variant)
        {
            final BlockId blockId = ItemServiceImpl.this.blockNumIdMap.get(block).getBlockId();
            return this.dig.canHarvest(blockId, blockId.variants()[variant]);
        }
        
        @Override
        public boolean canHarvest(Material material)
        {
            return this.dig.canHarvest(material);
        }
    }
    
    /**
     * Listener to watch for new block registrations
     * 
     * @author mepeisen
     */
    private class BlockListener implements EnumerationListener<BlockId>
    {
        
        /**
         * Constructor
         */
        public BlockListener()
        {
            // empty
        }
        
        @Override
        public void onEnumRegistered(Plugin plugin, Class<? extends BlockId> clazz, BlockId[] values)
        {
            ItemServiceImpl.this.initBlocks(Arrays.stream(values));
        }
        
        @Override
        public void onEnumRemoved(Plugin plugin, Class<? extends BlockId> clazz, BlockId[] values)
        {
            for (final BlockId blockId : values)
            {
                ItemServiceImpl.this.blockIdMap.remove(blockId).setBlockId(null);
            }
        }
        
    }
    
    /**
     * Listener to watch for new item registrations
     * 
     * @author mepeisen
     */
    private class ItemListener implements EnumerationListener<ItemId>
    {
        
        /**
         * Constructor
         */
        public ItemListener()
        {
            // empty
        }
        
        @Override
        public void onEnumRegistered(Plugin plugin, Class<? extends ItemId> clazz, ItemId[] values)
        {
            ItemServiceImpl.this.initItems(Arrays.stream(values));
        }
        
        @Override
        public void onEnumRemoved(Plugin plugin, Class<? extends ItemId> clazz, ItemId[] values)
        {
            for (final ItemId itemId : values)
            {
                ItemServiceImpl.this.itemIdMap.remove(itemId).setItemId(null);
            }
        }
        
    }
    
    /**
     * initializes the items
     */
    private void initItems()
    {
        // load from config
        for (final CustomItem item : this.loadItems())
        {
            if (item.getNumId() > 0)
            {
                this.itemNumIdMap.put(item.getNumId(), item);
                this.itemMap.put(item, new ItemInfo(null, (short) 0, item.getNumId()));
            }
            else
            {
                final Material material = item.getCustomType().getMaterial();
                final short durability = item.getDurability();
                this.itemsPerMaterial.computeIfAbsent(material, m -> new HashMap<>()).put(durability, item);
                this.itemMap.put(item, new ItemInfo(material, durability, 0));
            }
        }
        
        final Stream<ItemId> stream = EnumServiceInterface.instance().getEnumValues(ItemId.class).stream();
        initItems(stream);
    }
    
    /**
     * @param inventory
     * @param targetSlot
     * @param invitem
     * @param helper 
     * @param marker 
     */
    public void deleteTooling(Inventory inventory, int targetSlot, ItemStack invitem, ItemHelperInterface helper, ToolMarker marker)
    {
        final String id = helper.getCustomData(invitem, ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_ID);
        marker.getTools().remove(id);
        inventory.setItem(targetSlot, null);
    }

    /**
     * @param stream
     */
    protected void initItems(final Stream<ItemId> stream)
    {
        final Stack<CustomItem> newSimpleItems = new Stack<>();
        final Stack<CustomItem> newModdedItems = new Stack<>();
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        
        // parse items from plugins
        final List<ItemId> enumValues = stream.sorted((a, b) -> {
            int result = a.getPluginName().compareTo(b.getPluginName());
            if (result == 0)
            {
                result = a.name().compareTo(b.name());
            }
            return result;
        }).collect(Collectors.toList());
        for (final ItemId item : enumValues)
        {
            final CustomItem custom = new CustomItem(item.getPluginName(), item.name());
            if (this.itemMap.containsKey(custom))
            {
                // we found the item
                final ItemInfo key = this.itemMap.get(custom);
                final CustomItem custom2 = key.getNumId() > 0 ? this.itemNumIdMap.get(key.getNumId()) : this.itemsPerMaterial.get(key.getMaterial()).get(key.getDurability());
                custom2.setItemId(item);
                if (key.getNumId() == 0)
                {
                    custom2.setCustomDurability(custom2.getCustomType().getDurabilities()[custom2.getDurability() - 1]);
                }
                this.itemIdMap.put(item, custom2);
            }
            else
            {
                // this is a new item
                custom.setItemId(item);
                if (item.isModded())
                {
                    newModdedItems.add(custom);
                }
                else
                {
                    newSimpleItems.add(custom);
                }
                this.itemIdMap.put(item, custom);
            }
        }
        
        if (!newSimpleItems.isEmpty())
        {
            outer: for (final CustomItemTypes type : CustomItemTypes.values())
            {
                this.itemsPerMaterial.computeIfAbsent(type.getMaterial(), k -> new HashMap<>());
                for (final Durability dur : type.getDurabilities())
                {
                    if (this.itemsPerMaterial.get(type.getMaterial()).containsKey(dur.getItemStackDurability()))
                        continue;
                    
                    final CustomItem item = newSimpleItems.pop();
                    item.setCustomDurability(dur);
                    item.setCustomType(type);
                    this.itemsPerMaterial.get(type.getMaterial()).put(dur.getItemStackDurability(), item);
                    this.itemMap.put(item, new ItemInfo(type.getMaterial(), dur.getItemStackDurability(), 0));
                    
                    if (newSimpleItems.isEmpty())
                    {
                        this.saveItems(this.itemMap.keySet().toArray(new CustomItem[this.itemMap.size()]));
                        break outer;
                    }
                }
            }
            
            if (!newSimpleItems.isEmpty())
            {
                LOGGER.severe("Too much simple items. Items will be broken"); //$NON-NLS-1$
            }
        }
        
        if (!newModdedItems.isEmpty())
        {
            for (int i = MclibConstants.MIN_ITEM_ID; i < MclibConstants.MAX_ITEM_ID; i++)
            {
                if (this.itemNumIdMap.containsKey(i))
                    continue;
                
                final CustomItem item = newModdedItems.pop();
                item.setNumId(i);
                this.itemNumIdMap.put(i, item);
                this.itemMap.put(item, new ItemInfo(null, (short) 0, i));
                
                if (newModdedItems.isEmpty())
                {
                    this.saveItems(this.itemMap.keySet().toArray(new CustomItem[this.itemMap.size()]));
                    break;
                }
            }
            
            if (!newModdedItems.isEmpty())
            {
                LOGGER.severe("Too much modded items. Items will be broken"); //$NON-NLS-1$
            }
        }
        
        // init data
        for (final ItemId item : enumValues)
        {
            final CustomItem custom = this.itemIdMap.get(item);
            
            // furnaceRecipe
            final FurnaceRecipeInterface furnaceRecipe = item.furnaceRecipe();
            if (furnaceRecipe != null)
            {
                if (custom.getNumId() > 0)
                {
                    this.lazyPluginInit.computeIfAbsent(item.getPluginName(), k -> new ArrayList<>())
                            .add(() -> helper.installFurnaceRecipe(custom.getNumId(), furnaceRecipe.getReceipe(item, null, null), furnaceRecipe.getExperience(item, null, null)));
                }
                else
                {
                    this.lazyPluginInit.computeIfAbsent(item.getPluginName(), k -> new ArrayList<>()).add(() -> helper.installFurnaceRecipe(custom.getCustomType().getMaterial(),
                            custom.getCustomDurability().getItemStackDurability(), furnaceRecipe.getReceipe(item, null, null), furnaceRecipe.getExperience(item, null, null)));
                }
            }
            
            // stack size
            if (item.stackSize() > 1)
            {
                if (custom.getNumId() > 0)
                {
                    helper.setStackSize(custom.getNumId(), item.stackSize());
                }
                else
                {
                    helper.setStackSize(custom.getCustomType().getMaterial(), custom.getCustomDurability().getItemStackDurability(), item.stackSize());
                }
            }
            
            // crafting
            final CraftingRecipes crafting = item.recipes();
            if (crafting != null)
            {
                for (final CraftingShapedRecipe shaped : crafting.shaped())
                {
                    this.lazyPluginInit.computeIfAbsent(item.getPluginName(), k -> new ArrayList<>())
                            .add(() -> helper.installShapedRecipe(createItem(item), shaped.amount(), shaped.shape(), toShapedItems(shaped.items())));
                }
                for (final CraftingShapelessRecipe shapeless : crafting.shapeless())
                {
                    this.lazyPluginInit.computeIfAbsent(item.getPluginName(), k -> new ArrayList<>())
                            .add(() -> helper.installShapelessRecipe(createItem(item), shapeless.amount(), toShapelessItems(shapeless.items())));
                }
            }
            
            // aspects
            final ItemClass[] classes = item.getItemClasses();
            if (classes.length > 0)
            {
                if (classes.length > 1)
                {
                    LOGGER.warning("Multiple item classes used for " + item.getPluginName() + "/" + item.name() + ". Only first item class will be applied."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                try
                {
                    switch (classes[0])
                    {
                        case Armor:
                            this.initArmor(helper, custom, item, item.armor());
                            break;
                        case Axe:
                            this.initAxe(helper, custom, item, item.axe());
                            break;
                        case Hoe:
                            this.initHoe(helper, custom, item, item.hoe());
                            break;
                        case Pickaxe:
                            this.initPickaxe(helper, custom, item, item.pickaxe());
                            break;
                        case Shovel:
                            this.initShovel(helper, custom, item, item.shovel());
                            break;
                        case Sword:
                            this.initSword(helper, custom, item, item.sword());
                            break;
                        default:
                            LOGGER.warning("Unknown item class for " + item.getPluginName() + "/" + item.name() + ": " + classes[0]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            break;
                    }
                }
                catch (Exception ex)
                {
                    LOGGER.log(Level.WARNING, "Problems applying item class", ex); //$NON-NLS-1$
                }
            }
        }
    }
    
    /**
     * @param helper 
     * @param custom
     * @param item
     * @param sword
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private void initSword(ItemHelperInterface helper, CustomItem custom , ItemId item, ItemSword sword) throws InstantiationException, IllegalAccessException
    {
        if (custom.getNumId() > 0)
        {
            helper.initSword(custom.getNumId(), sword.durability(), sword.damageVsEntity(), sword.damage(), sword.getItemEnchantability(), sword.speed(), new NmsItemRule(sword.dmgRule().newInstance(), sword.repairRule().newInstance(), sword.digRule().newInstance()));
        }
        else
        {
            helper.initSword(custom.getCustomType().getMaterial(), custom.getCustomDurability().getItemStackDurability(), sword.durability(), sword.damageVsEntity(), sword.damage(), sword.getItemEnchantability(), sword.speed(), new NmsItemRule(sword.dmgRule().newInstance(), sword.repairRule().newInstance(), sword.digRule().newInstance()));
        }
    }

    /**
     * @param helper 
     * @param custom
     * @param item
     * @param shovel
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private void initShovel(ItemHelperInterface helper, CustomItem custom , ItemId item, ItemShovel shovel) throws InstantiationException, IllegalAccessException
    {
        if (custom.getNumId() > 0)
        {
            helper.initShovel(custom.getNumId(), shovel.durability(), shovel.damage(), shovel.getItemEnchantability(), shovel.speed(), new NmsItemRule(shovel.dmgRule().newInstance(), shovel.repairRule().newInstance(), shovel.digRule().newInstance()));
        }
        else
        {
            helper.initShovel(custom.getCustomType().getMaterial(), custom.getCustomDurability().getItemStackDurability(), shovel.durability(), shovel.damage(), shovel.getItemEnchantability(), shovel.speed(), new NmsItemRule(shovel.dmgRule().newInstance(), shovel.repairRule().newInstance(), shovel.digRule().newInstance()));
        }
    }

    /**
     * @param helper 
     * @param custom
     * @param item
     * @param pickaxe
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private void initPickaxe(ItemHelperInterface helper, CustomItem custom , ItemId item, ItemPickaxe pickaxe) throws InstantiationException, IllegalAccessException
    {
        if (custom.getNumId() > 0)
        {
            helper.initPickaxe(custom.getNumId(), pickaxe.durability(), pickaxe.damage(), pickaxe.getItemEnchantability(), pickaxe.speed(), new NmsItemRule(pickaxe.dmgRule().newInstance(), pickaxe.repairRule().newInstance(), pickaxe.digRule().newInstance()));
        }
        else
        {
            helper.initPickaxe(custom.getCustomType().getMaterial(), custom.getCustomDurability().getItemStackDurability(), pickaxe.durability(), pickaxe.damage(), pickaxe.getItemEnchantability(), pickaxe.speed(), new NmsItemRule(pickaxe.dmgRule().newInstance(), pickaxe.repairRule().newInstance(), pickaxe.digRule().newInstance()));
        }
    }

    /**
     * @param helper 
     * @param custom
     * @param item
     * @param hoe
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private void initHoe(ItemHelperInterface helper, CustomItem custom , ItemId item, ItemHoe hoe) throws InstantiationException, IllegalAccessException
    {
        if (custom.getNumId() > 0)
        {
            helper.initHoe(custom.getNumId(), hoe.durability(), hoe.damage(), hoe.getItemEnchantability(), hoe.speed(), new NmsItemRule(hoe.dmgRule().newInstance(), hoe.repairRule().newInstance(), hoe.digRule().newInstance()));
        }
        else
        {
            helper.initHoe(custom.getCustomType().getMaterial(), custom.getCustomDurability().getItemStackDurability(), hoe.durability(), hoe.damage(), hoe.getItemEnchantability(), hoe.speed(), new NmsItemRule(hoe.dmgRule().newInstance(), hoe.repairRule().newInstance(), hoe.digRule().newInstance()));
        }
    }

    /**
     * @param helper 
     * @param custom
     * @param item
     * @param axe
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private void initAxe(ItemHelperInterface helper, CustomItem custom , ItemId item, ItemAxe axe) throws InstantiationException, IllegalAccessException
    {
        if (custom.getNumId() > 0)
        {
            helper.initAxe(custom.getNumId(), axe.durability(), axe.damage(), axe.getItemEnchantability(), axe.speed(), new NmsItemRule(axe.dmgRule().newInstance(), axe.repairRule().newInstance(), axe.digRule().newInstance()));
        }
        else
        {
            helper.initAxe(custom.getCustomType().getMaterial(), custom.getCustomDurability().getItemStackDurability(), axe.durability(), axe.damage(), axe.getItemEnchantability(), axe.speed(), new NmsItemRule(axe.dmgRule().newInstance(), axe.repairRule().newInstance(), axe.digRule().newInstance()));
        }
    }

    /**
     * @param helper 
     * @param custom
     * @param item
     * @param armor
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private void initArmor(ItemHelperInterface helper, CustomItem custom , ItemId item, ItemArmor armor) throws InstantiationException, IllegalAccessException
    {
        if (custom.getNumId() > 0)
        {
            helper.initArmor(custom.getNumId(), armor.dmgReduceAmount(), armor.durability(), armor.getItemEnchantability(), armor.toughness(), armor.slot(), new NmsItemRule(null, armor.repairRule().newInstance(), null));
        }
        else
        {
            helper.initArmor(custom.getCustomType().getMaterial(), custom.getCustomDurability().getItemStackDurability(), armor.dmgReduceAmount(), armor.durability(), armor.getItemEnchantability(), armor.toughness(), armor.slot(), new NmsItemRule(null, armor.repairRule().newInstance(), null));
        }
    }

    /**
     * @param items
     * @return shapeless items
     */
    private ItemStack[] toShapelessItems(Class<? extends CraftingItemInterface>[] items)
    {
        final ItemStack[] result = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++)
        {
            try
            {
                result[i] = items[i].newInstance().item();
            }
            catch (InstantiationException | IllegalAccessException e)
            {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }
    
    /**
     * @param items
     * @return shaped item map
     */
    private Map<Character, ItemStack> toShapedItems(CraftingShapedItem[] items)
    {
        final Map<Character, ItemStack> result = new HashMap<>();
        for (final CraftingShapedItem item : items)
        {
            try
            {
                result.put(item.shape(), item.item().newInstance().item());
            }
            catch (InstantiationException | IllegalAccessException e)
            {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }
    
    /**
     * initializes the items
     */
    private void initBlocks()
    {
        // load from config
        for (final CustomBlock block : this.loadBlocks())
        {
            this.blockNumIdMap.put(block.getNumId(), block);
            this.blockMap.put(block, block.getNumId());
        }
        
        final Stream<BlockId> stream = EnumServiceInterface.instance().getEnumValues(BlockId.class).stream();
        initBlocks(stream);
    }
    
    /**
     * @param stream
     */
    protected void initBlocks(final Stream<BlockId> stream)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        final Stack<CustomBlock> newBlocks = new Stack<>();
        
        // parse blocks from plugins
        final List<BlockId> enumValues = stream.sorted((a, b) -> {
            int result = a.getPluginName().compareTo(b.getPluginName());
            if (result == 0)
            {
                result = a.name().compareTo(b.name());
            }
            return result;
        }).collect(Collectors.toList());
        for (final BlockId block : enumValues)
        {
            final CustomBlock custom = new CustomBlock(block.getPluginName(), block.name());
            if (this.blockMap.containsKey(custom))
            {
                // we found the block
                final CustomBlock custom2 = this.blockNumIdMap.get(this.blockMap.get(custom));
                custom2.setBlockId(block);
                this.blockIdMap.put(block, custom2);
            }
            else
            {
                // this is a new block
                custom.setBlockId(block);
                newBlocks.add(custom);
                this.blockIdMap.put(block, custom);
            }
        }
        
        if (!newBlocks.isEmpty())
        {
            for (int i = MclibConstants.MIN_BLOCK_ID; i < MclibConstants.MAX_BLOCK_ID; i++)
            {
                if (this.blockNumIdMap.containsKey(i))
                    continue;
                
                final CustomBlock block = newBlocks.pop();
                block.setNumId(i);
                this.blockNumIdMap.put(i, block);
                this.blockMap.put(block, i);
                
                if (newBlocks.isEmpty())
                {
                    this.saveBlocks(this.blockMap.keySet().toArray(new CustomBlock[this.blockMap.size()]));
                    break;
                }
            }
            
            if (!newBlocks.isEmpty())
            {
                LOGGER.severe("Too much modded blocks. Blocks will be broken"); //$NON-NLS-1$
            }
        }
        
        // init data
        for (final BlockId block : enumValues)
        {
            final int blockId = blockToNumId(block);
            
            // meta and drop rule
            final BlockMeta meta = block.meta();
            final BlockDropRuleInterface dropRule = block.dropRule();
            setBlockMeta(helper, block, blockId, meta, dropRule);
            
            // stack size
            final int stackSize = block.stackSize();
            if (stackSize != 64)
            {
                helper.setStackSize(blockId, stackSize);
            }
            
            final BlockInventoryMeta blockInv = block.inventory();
            
            // furnace recipe/ stack sizes / crafting
            final FurnaceRecipeInterface furnaceRecipe = block.furnaceRecipe();
            final CraftingRecipes blockRecipes = block.recipes();
            for (final BlockVariantId variant : block.variants())
            {
                // furnace
                final FurnaceRecipeInterface variantFurnace = variant.furnaceRecipe();
                if (variantFurnace != null)
                {
                    this.lazyPluginInit.computeIfAbsent(block.getPluginName(), k -> new ArrayList<>())
                            .add(() -> helper.installFurnaceRecipe(blockId, variant.ordinal(), variantFurnace.getReceipe(null, block, variant), variantFurnace.getExperience(null, block, variant)));
                }
                else if (furnaceRecipe != null)
                {
                    this.lazyPluginInit.computeIfAbsent(block.getPluginName(), k -> new ArrayList<>())
                            .add(() -> helper.installFurnaceRecipe(blockId, variant.ordinal(), furnaceRecipe.getReceipe(null, block, variant), furnaceRecipe.getExperience(null, block, variant)));
                }
                
                // crafting
                final CraftingRecipes variantRecipes = variant.recipes();
                if (variantRecipes != null)
                {
                    for (final CraftingShapedRecipe shaped : variantRecipes.shaped())
                    {
                        this.lazyPluginInit.computeIfAbsent(block.getPluginName(), k -> new ArrayList<>())
                                .add(() -> helper.installShapedRecipe(blockId, variant.ordinal(), shaped.amount(), shaped.shape(), toShapedItems(shaped.items())));
                    }
                    for (final CraftingShapelessRecipe shapeless : variantRecipes.shapeless())
                    {
                        this.lazyPluginInit.computeIfAbsent(block.getPluginName(), k -> new ArrayList<>())
                                .add(() -> helper.installShapelessRecipe(blockId, variant.ordinal(), shapeless.amount(), toShapelessItems(shapeless.items())));
                    }
                }
                if (blockRecipes != null)
                {
                    for (final CraftingShapedRecipe shaped : blockRecipes.shaped())
                    {
                        this.lazyPluginInit.computeIfAbsent(block.getPluginName(), k -> new ArrayList<>())
                                .add(() -> helper.installShapedRecipe(blockId, variant.ordinal(), shaped.amount(), shaped.shape(), toShapedItems(shaped.items())));
                    }
                    for (final CraftingShapelessRecipe shapeless : blockRecipes.shapeless())
                    {
                        this.lazyPluginInit.computeIfAbsent(block.getPluginName(), k -> new ArrayList<>())
                                .add(() -> helper.installShapelessRecipe(blockId, variant.ordinal(), shapeless.amount(), toShapelessItems(shapeless.items())));
                    }
                }
                
                // inventory
                final BlockInventoryMeta variantInv = variant.inventory();
                if (variantInv != null)
                {
                    helper.initInventory(blockId, variant.ordinal(), new NmsInventoryHandler(block, variant, variantInv));
                }
                else if (blockInv != null)
                {
                    helper.initInventory(blockId, variant.ordinal(), new NmsInventoryHandler(block, variant, blockInv));
                }
            }
        }
    }

    /**
     * @param block
     * @return num id
     */
    private int blockToNumId(final BlockId block)
    {
        final int blockId = this.blockIdMap.get(block).getNumId();
        return blockId;
    }
    
    /**
     * @param helper
     * @param block
     * @param blockId
     * @param meta
     * @param dropRule
     */
    private void setBlockMeta(final ItemHelperInterface helper, final BlockId block, final int blockId, final BlockMeta meta, final BlockDropRuleInterface dropRule)
    {
        helper.setBlockMeta(blockId, meta == null ? 0 : meta.hardness(), meta == null ? 0 : meta.resistance(), dropRule == null ? null : new NmsDropRuleInterface() {
            
            @Override
            public int getExpDrop(int variant, Random random, int enchantmentLevel)
            {
                return dropRule.getExpDrop(block, block.variants()[variant], random, enchantmentLevel);
            }
            
            @Override
            public int getDropVariant(int variant)
            {
                final BlockVariantId v = dropRule.getDropVariant(block, block.variants()[variant]);
                return v == null ? 0 : v.ordinal();
            }
            
            @Override
            public int getDropType(int variant, Random random, int fortune)
            {
                final BlockId b = dropRule.getDropType(block, block.variants()[variant], random, fortune);
                return b == null ? 0 : ItemServiceImpl.this.blockIdMap.get(b).getNumId();
            }
            
            @Override
            public int getDropCount(Random random, int fortune)
            {
                return dropRule.getDropCount(random, fortune);
            }
        });
    }
    
    @Override
    public ResourceVersion getResourceVersion(MinecraftVersionsType minecraftVersion)
    {
        if (minecraftVersion.isBelow(MinecraftVersionsType.V1_9))
            return ResourceVersion.PACK_FORMAT_1;
        if (minecraftVersion.isBelow(MinecraftVersionsType.V1_11))
            return ResourceVersion.PACK_FORMAT_2;
        return ResourceVersion.PACK_FORMAT_3;
    }
    
    @Override
    public void setDownloadUrl(String url)
    {
        this.setDownloadUrl(url, this.getResourceVersion(McLibInterface.instance().getMinecraftVersion()));
    }
    
    @Override
    public void setDownloadUrl(String url, ResourceVersion version)
    {
        switch (version)
        {
            case PACK_FORMAT_1:
                McCoreConfig.ResourcePackDownloadUrlV1.setString(url);
                McCoreConfig.ResourcePackDownloadUrlV1.saveConfig();
                break;
            case PACK_FORMAT_2:
                McCoreConfig.ResourcePackDownloadUrlV2.setString(url);
                McCoreConfig.ResourcePackDownloadUrlV2.saveConfig();
                break;
            default:
            case PACK_FORMAT_3:
                McCoreConfig.ResourcePackDownloadUrlV3.setString(url);
                McCoreConfig.ResourcePackDownloadUrlV3.saveConfig();
                break;
        }
    }
    
    @Override
    public String getDownloadUrl()
    {
        return this.getDownloadUrl(this.getResourceVersion(McLibInterface.instance().getMinecraftVersion()));
    }
    
    @Override
    public String getDownloadUrl(ResourceVersion version)
    {
        switch (version)
        {
            case PACK_FORMAT_1:
                return McCoreConfig.ResourcePackDownloadUrlV1.getString();
            case PACK_FORMAT_2:
                return McCoreConfig.ResourcePackDownloadUrlV2.getString();
            default:
            case PACK_FORMAT_3:
                return McCoreConfig.ResourcePackDownloadUrlV3.getString();
        }
    }
    
    @Override
    public boolean isAutoResourceDownload()
    {
        return McCoreConfig.ResourcePackAutoDownload.getBoolean();
    }
    
    @Override
    public void setAutoResourceDownload(boolean newValue)
    {
        McCoreConfig.ResourcePackAutoDownload.setBoolean(newValue);
        McCoreConfig.ResourcePackAutoDownload.saveConfig();
    }
    
    @Override
    public int getAutoResourceTicks()
    {
        return McCoreConfig.ResourcePackAutoDownloadTicks.getInt();
    }
    
    @Override
    public void setAutoResourceTicks(int ticks)
    {
        McCoreConfig.ResourcePackAutoDownload.setInt(ticks);
        McCoreConfig.ResourcePackAutoDownload.saveConfig();
    }
    
    @Override
    public ResourcePackStatus getState(McPlayerInterface player)
    {
        final ResourcePackMarker marker = player.getSessionStorage().get(ResourcePackMarker.class);
        return marker == null ? null : marker.getState();
    }
    
    @Override
    public void forceDownload(McPlayerInterface player, McRunnable success)
    {
        this.forceDownload(player, this.getDownloadUrl(), success, null, null);
    }
    
    @Override
    public void forceDownload(McPlayerInterface player, String url, McRunnable success, McRunnable failure, McRunnable declined)
    {
        player.getBukkitPlayer().setResourcePack(url);
        player.getSessionStorage().set(ResourcePackMarker.class, new ResourcePackMarker(success, failure, declined));
    }
    
    @Override
    public boolean hasResourcePack(McPlayerInterface player)
    {
        return getState(player) == ResourcePackStatus.SUCCESSFULLY_LOADED;
    }
    
    @Override
    public ItemStack createItem(ItemId item, String name)
    {
        final CustomItem custom = this.itemIdMap.get(item);
        if (custom.getNumId() > 0)
        {
            return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).createItemStackForItem(custom.getNumId(), name);
        }
        
        final ItemStack itemStack = new ItemStack(custom.getCustomType().getMaterial(), 1, custom.getCustomDurability().getItemStackDurability());
        final ItemMeta meta = itemStack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName(name == null ? "" : name); //$NON-NLS-1$
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    @Override
    public ItemStack createItem(ItemId item)
    {
        return createItem(item, this.itemIdMap.get(item).getNameProvider().getName());
    }
    
    @Override
    public ItemStack createItem(ItemId item, LocalizedMessageInterface name, Serializable... nameArgs)
    {
        return createItem(item, name == null ? null : name.toUserMessage(McLibInterface.instance().getDefaultLocale(), nameArgs));
    }
    
    @Override
    public ItemStack createItem(McPlayerInterface player, ItemId item)
    {
        return createItem(player, item, this.itemIdMap.get(item).getNameProvider().getName());
    }
    
    @Override
    public ItemStack createItem(McPlayerInterface player, ItemId item, LocalizedMessageInterface name, Serializable... nameArgs)
    {
        return createItem(item, name == null ? null : player.encodeMessage(name, nameArgs)[0]);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public ItemId getItemId(ItemStack stack)
    {
        if (stack.getTypeId() >= MclibConstants.MIN_ITEM_ID)
        {
            final CustomItem custom = this.itemNumIdMap.get(stack.getTypeId());
            return custom == null ? null : custom.getItemId();
        }
        final Map<Short, CustomItem> map = this.itemsPerMaterial.get(stack.getData().getItemType());
        if (map != null)
        {
            final CustomItem item = map.get(stack.getDurability());
            if (item != null)
            {
                return item.getItemId();
            }
        }
        return null;
    }
    
    @Override
    public void createResourcePack(File target) throws IOException
    {
        this.createResourcePack(target, this.getResourceVersion(McLibInterface.instance().getMinecraftVersion()));
    }
    
    /**
     * write item overrides
     * 
     * @param jar
     * @param material
     * @param map
     */
    private void writeItemOverrides(JarOutputStream jar, Material material, Map<Short, CustomItem> map)
    {
        try
        {
            final CustomItem custom = map.values().iterator().next();
            final JarEntry overridesJson = new JarEntry("assets/minecraft/" + custom.getCustomType().getModelsFilename()); //$NON-NLS-1$
            overridesJson.setTime(System.currentTimeMillis());
            jar.putNextEntry(overridesJson);
            final StringBuilder buffer = new StringBuilder();
            buffer.append("{ \"parent\": \""); //$NON-NLS-1$
            buffer.append(custom.getCustomType().getParent());
            buffer.append("\", \"textures\": { \"layer0\": \""); //$NON-NLS-1$
            buffer.append(custom.getCustomType().getDefaultTexture());
            buffer.append("\"}, \"overrides\": ["); //$NON-NLS-1$
            buffer.append("{\"predicate\": {\"damaged\": 0, \"damage\": 0}, \"model\": \"").append(custom.getCustomType().getDefaultModel()).append("\"},"); //$NON-NLS-1$ //$NON-NLS-2$
            for (CustomItem item : map.values())
            {
                if (item == null)
                    continue;
                buffer.append("{\"predicate\": {\"damaged\": 0, \"damage\": ").append(item.getCustomDurability().getModelDurability()).append("}, \"model\": \"item/").append(item.getPluginName()) //$NON-NLS-1$ //$NON-NLS-2$
                        .append('/').append(item.getEnumName()).append("\"},"); //$NON-NLS-1$
            }
            buffer.append("{\"predicate\": {\"damaged\": 1, \"damage\": 0}, \"model\": \"").append(custom.getCustomType().getDefaultModel()).append("\"}"); //$NON-NLS-1$ //$NON-NLS-2$
            buffer.append("]}"); //$NON-NLS-1$
            writeFile(jar, buffer.toString());
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING, "IOException writing pack.mcmeta", e); //$NON-NLS-1$
        }
    }
    
    /**
     * write blockstates
     * 
     * @param jar
     * @param numId
     * @param block
     */
    private void writeBlockstates(JarOutputStream jar, Integer numId, CustomBlock block)
    {
        try
        {
            final BlockId blockId = block.getBlockId();
            if (blockId == null)
            {
                LOGGER.warning("Missing block " + block.getPluginName() + "/" + block.getEnumName() + "\nThis may not be a problem if the block is no more in use."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                return;
            }
            
            final JarEntry blockStatesJson = new JarEntry("assets/mclib/blockstates/custom-" + numId + ".json"); //$NON-NLS-1$ //$NON-NLS-2$
            blockStatesJson.setTime(System.currentTimeMillis());
            jar.putNextEntry(blockStatesJson);
            final StringBuilder buffer = new StringBuilder();
            buffer.append("{ \"variants\": {"); //$NON-NLS-1$
            boolean first = true;
            for (final BlockVariantId variant : blockId.variants())
            {
                if (first)
                    first = false;
                else
                    buffer.append(", "); //$NON-NLS-1$
                buffer.append("\"variant=variant_").append(variant.ordinal()).append("\": {"); //$NON-NLS-1$ //$NON-NLS-2$
                buffer.append("\"model\": \"mclib:custom-").append(numId).append("-").append(variant.ordinal()).append("\"}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            buffer.append("}}"); //$NON-NLS-1$
            writeFile(jar, buffer.toString());
            
            // add block textures
            final List<String> blockTextures = new ArrayList<>();
            final String[] blockTexturesArray = blockId.getTextures();
            if (blockTexturesArray != null)
            {
                for (final String texture : blockTexturesArray)
                {
                    final File path = new File(texture);
                    final String filename = path.getName();
                    final String file = Files.getNameWithoutExtension(filename);
                    blockTextures.add("items/" + blockId.getPluginName() + '/' + blockId.name() + "_" + file); //$NON-NLS-1$ //$NON-NLS-2$
                    try
                    {
                        final JarEntry textureEntry = new JarEntry("assets/minecraft/textures/items/" + blockId.getPluginName() + '/' + blockId.name() + "_" + filename); //$NON-NLS-1$ //$NON-NLS-2$
                        textureEntry.setTime(System.currentTimeMillis());
                        jar.putNextEntry(textureEntry);
                        copyFile(jar, blockId.getClass().getClassLoader(), texture);
                    }
                    catch (IOException e)
                    {
                        LOGGER.log(Level.WARNING, "IOException writing texture " + blockId.getPluginName() + '/' + blockId.name() + '_' + filename, e); //$NON-NLS-1$
                    }
                }
            }
            
            // parse variants
            for (final BlockVariantId variant : blockId.variants())
            {
                final List<String> textures = new ArrayList<>();
                for (final String texture : variant.getTextures())
                {
                    final File path = new File(texture);
                    final String filename = path.getName();
                    final String file = Files.getNameWithoutExtension(filename);
                    textures.add("items/" + variant.getPluginName() + '/' + variant.name() + "_" + file); //$NON-NLS-1$ //$NON-NLS-2$
                    try
                    {
                        final JarEntry textureEntry = new JarEntry("assets/minecraft/textures/items/" + variant.getPluginName() + '/' + variant.name() + "_" + filename); //$NON-NLS-1$ //$NON-NLS-2$
                        textureEntry.setTime(System.currentTimeMillis());
                        jar.putNextEntry(textureEntry);
                        copyFile(jar, variant.getClass().getClassLoader(), texture);
                    }
                    catch (IOException e)
                    {
                        LOGGER.log(Level.WARNING, "IOException writing texture " + variant.getPluginName() + '/' + variant.name() + '_' + filename, e); //$NON-NLS-1$
                    }
                }
                textures.addAll(blockTextures);
                
                final JarEntry variantJson = new JarEntry("assets/mclib/models/block/custom-" + numId + "-" + variant.ordinal() + ".json"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                variantJson.setTime(System.currentTimeMillis());
                jar.putNextEntry(variantJson);
                final String blockJson = blockId.getModelJson();
                writeFile(jar, String.format(blockJson != null && blockJson.length() > 0 ? blockJson : variant.getModelJson(), textures.toArray()));
            }
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING, "IOException writing pack.mcmeta", e); //$NON-NLS-1$
        }
    }
    
    /**
     * write item overrides
     * 
     * @param jar
     * @param material
     * @param map
     */
    private void writeItemModel(JarOutputStream jar, Material material, Map<Short, CustomItem> map)
    {
        for (CustomItem item : map.values())
        {
            if (item.getItemId() == null)
            {
                LOGGER.warning("Missing item " + item.getPluginName() + "/" + item.getEnumName() + "\nThis may not be a problem if the item is no more in use."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                continue;
            }
            try
            {
                final List<String> textures = new ArrayList<>();
                for (final String texture : item.getItemId().getTextures())
                {
                    final File path = new File(texture);
                    final String filename = path.getName();
                    final String file = Files.getNameWithoutExtension(filename);
                    textures.add("items/" + item.getPluginName() + '/' + item.getEnumName() + "_" + file); //$NON-NLS-1$ //$NON-NLS-2$
                    try
                    {
                        final JarEntry textureEntry = new JarEntry("assets/minecraft/textures/items/" + item.getPluginName() + '/' + item.getEnumName() + "_" + filename); //$NON-NLS-1$ //$NON-NLS-2$
                        textureEntry.setTime(System.currentTimeMillis());
                        jar.putNextEntry(textureEntry);
                        copyFile(jar, item.getItemId().getClass().getClassLoader(), texture);
                    }
                    catch (IOException e)
                    {
                        LOGGER.log(Level.WARNING, "IOException writing texture " + item.getPluginName() + '/' + item.getEnumName() + '_' + filename, e); //$NON-NLS-1$
                    }
                }
                
                final JarEntry modelJson = new JarEntry("assets/minecraft/models/item/" + item.getPluginName() + '/' + item.getEnumName() + ".json"); //$NON-NLS-1$ //$NON-NLS-2$
                modelJson.setTime(System.currentTimeMillis());
                jar.putNextEntry(modelJson);
                writeFile(jar, String.format(item.getItemId().getModelJson(), textures.toArray()));
            }
            catch (IOException e)
            {
                LOGGER.log(Level.WARNING, "IOException writing item " + item.getPluginName() + '/' + item.getEnumName(), e); //$NON-NLS-1$
            }
        }
    }
    
    @Override
    public void createResourcePack(File target, ResourceVersion version) throws IOException
    {
        if (!target.getParentFile().exists())
        {
            target.getParentFile().mkdirs();
        }
        if (target.exists())
        {
            target.delete();
        }
        try (final JarOutputStream jar = new JarOutputStream(new FileOutputStream(target)))
        {
            final JarEntry mcmeta = new JarEntry("pack.mcmeta"); //$NON-NLS-1$
            mcmeta.setTime(System.currentTimeMillis());
            jar.putNextEntry(mcmeta);
            switch (version)
            {
                case PACK_FORMAT_1:
                    copyFile(jar, this.getClass().getClassLoader(), "de/minigameslib/mclib/resources/v1/pack.mcmeta"); //$NON-NLS-1$
                    break;
                case PACK_FORMAT_2:
                    copyFile(jar, this.getClass().getClassLoader(), "de/minigameslib/mclib/resources/v2/pack.mcmeta"); //$NON-NLS-1$
                    break;
                default:
                case PACK_FORMAT_3:
                    copyFile(jar, this.getClass().getClassLoader(), "de/minigameslib/mclib/resources/v3/pack.mcmeta"); //$NON-NLS-1$
                    break;
            }
            jar.closeEntry();
            
            // default model overrides
            this.itemsPerMaterial.forEach((martial, map) -> this.writeItemOverrides(jar, martial, map));
            
            // model files
            this.itemsPerMaterial.forEach((material, map) -> this.writeItemModel(jar, material, map));
            
            this.blockNumIdMap.forEach((numId, block) -> this.writeBlockstates(jar, numId, block));
            
            this.itemNumIdMap.forEach((numId, item) -> this.writeItems(jar, numId, item));
        }
    }
    
    /**
     * @param jar
     * @param numId
     * @param item
     */
    private void writeItems(JarOutputStream jar, Integer numId, CustomItem item)
    {
        try
        {
            // textures
            final List<String> textures = new ArrayList<>();
            for (final String texture : item.getItemId().getTextures())
            {
                final File path = new File(texture);
                final String filename = path.getName();
                final String file = Files.getNameWithoutExtension(filename);
                textures.add("items/" + item.getPluginName() + '/' + item.getEnumName() + "_" + file); //$NON-NLS-1$ //$NON-NLS-2$
                try
                {
                    final JarEntry textureEntry = new JarEntry("assets/minecraft/textures/items/" + item.getPluginName() + '/' + item.getEnumName() + "_" + filename); //$NON-NLS-1$ //$NON-NLS-2$
                    textureEntry.setTime(System.currentTimeMillis());
                    jar.putNextEntry(textureEntry);
                    copyFile(jar, item.getItemId().getClass().getClassLoader(), texture);
                }
                catch (IOException e)
                {
                    LOGGER.log(Level.WARNING, "IOException writing texture " + item.getPluginName() + '/' + item.getEnumName() + '_' + filename, e); //$NON-NLS-1$
                }
            }
            for (final String texture : item.getItemId().getModelTextures())
            {
                final File path = new File(texture);
                final String filename = path.getName();
                try
                {
                    final JarEntry textureEntry = new JarEntry("assets/mclib/textures/models/armor/custom-" + item.getNumId() + "_" + filename); //$NON-NLS-1$ //$NON-NLS-2$
                    textureEntry.setTime(System.currentTimeMillis());
                    jar.putNextEntry(textureEntry);
                    copyFile(jar, item.getItemId().getClass().getClassLoader(), texture);
                }
                catch (IOException e)
                {
                    LOGGER.log(Level.WARNING, "IOException writing texture " + item.getPluginName() + '/' + item.getEnumName() + '_' + filename, e); //$NON-NLS-1$
                }
            }
            
            // model json
            final JarEntry modelJson = new JarEntry("assets/mclib/models/item/custom-" + numId + ".json"); //$NON-NLS-1$ //$NON-NLS-2$
            modelJson.setTime(System.currentTimeMillis());
            jar.putNextEntry(modelJson);
            writeFile(jar, String.format(item.getItemId().getModelJson(), textures.toArray()));
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING, "IOException writing item " + item.getPluginName() + '/' + item.getEnumName(), e); //$NON-NLS-1$
        }
    }

    /**
     * @param jar
     * @param content
     * @throws IOException
     */
    private void writeFile(JarOutputStream jar, String content) throws IOException
    {
        jar.write(content.getBytes("UTF-8")); //$NON-NLS-1$
    }
    
    /**
     * @param jar
     * @param classLoader
     * @param name
     * @throws IOException
     */
    private void copyFile(JarOutputStream jar, ClassLoader classLoader, String name) throws IOException
    {
        try (final InputStream is = classLoader.getResourceAsStream(name))
        {
            try (final BufferedInputStream bis = new BufferedInputStream(is))
            {
                final byte[] buffer = new byte[65536];
                while (true)
                {
                    int count = bis.read(buffer);
                    if (count == -1)
                        break;
                    jar.write(buffer, 0, count);
                }
            }
        }
    }
    
    /**
     * @param inventory
     */
    public void clearTools(PlayerInventory inventory)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        
        // clears tooling items
        for (int i = 0; i < inventory.getSize(); i++)
        {
            final ItemStack item = inventory.getItem(i);
            if (helper.getCustomData(item, ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_CLEAR_ON_JOIN) != null)
            {
                inventory.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }
    
    @Override
    public ToolBuilderInterface prepareTool(ItemId item, McPlayerInterface player, LocalizedMessageInterface title, Serializable... titleArgs)
    {
        if (item.isModded())
            throw new IllegalStateException("modded items not yet supported"); // TODO support modded items (check for client mod) //$NON-NLS-1$
        return new ToolBuilderImpl(player, ()->createItem(player, item, title, titleArgs)){
            @Override
            public void build()
            {
                final ResourcePackStatus state = ItemServiceImpl.this.getState(player);
                if (state == null)
                {
                    ItemServiceImpl.this.forceDownload(player, this::build);
                    return;
                }
                switch (state)
                {
                    case DECLINED:
                        player.sendMessage(CommonMessages.ResourcePackDeclined);
                        return;
                    case FAILED_DOWNLOAD:
                        player.sendMessage(CommonMessages.ResourcePackFailed);
                        return;
                    case SUCCESSFULLY_LOADED:
                        // fall through
                        break;
                    case ACCEPTED:
                        player.sendMessage(CommonMessages.ResourcePackAccepted);
                        return;
                    default:
                        ItemServiceImpl.this.forceDownload(player, this::build);
                        return;
                }
                
                super.build();
            }
        };
    }
    
    @Override
    public ToolBuilderInterface prepareTool(ItemStack stack, McPlayerInterface player, LocalizedMessageInterface title, Serializable... titleArgs)
    {
        return new ToolBuilderImpl(player, ()->{
            this.setDisplayName(stack, player, title, titleArgs);
            return stack;
        });
    }
    
    /**
     * Player death event; cancels the drop of the tooling item...
     * 
     * @param evt
     */
    @McEventHandler
    public void onDeath(McPlayerDeathEvent evt)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        final List<ItemStack> stacks = evt.getBukkitEvent().getDrops();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (helper.getCustomData(stacks.get(i), ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_MARKER) != null)
            {
                stacks.remove(i);
                break;
            }
        }
    }
    
    /**
     * On drop items; cancels drop of tooling item...
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerDrop(McPlayerDropItemEvent evt)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        if (helper.getCustomData(evt.getBukkitEvent().getItemDrop().getItemStack(), ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_MARKER) != null)
        {
            evt.getBukkitEvent().setCancelled(true);
        }
    }
    
    /**
     * On inventory action; cancels drop of tooling item into other inventories...
     * 
     * @param evt
     */
    @McEventHandler
    public void onInventoryClick(McInventoryClickEvent evt)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        final ItemStack cursor = evt.getBukkitEvent().getCursor();
        if (cursor != null && helper.getCustomData(cursor, ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_MARKER) != null)
        {
            switch (evt.getBukkitEvent().getAction())
            {
                case CLONE_STACK:
                case UNKNOWN:
                case COLLECT_TO_CURSOR:
                default:
                    // deny
                    evt.getBukkitEvent().setCancelled(true);
                    break;
                case DROP_ALL_CURSOR:
                case DROP_ALL_SLOT:
                case DROP_ONE_CURSOR:
                case DROP_ONE_SLOT:
                case MOVE_TO_OTHER_INVENTORY:
                case PLACE_ALL:
                case PLACE_ONE:
                case PLACE_SOME:
                case SWAP_WITH_CURSOR:
                    if (evt.getBukkitEvent().getClickedInventory() != evt.getPlayer().getBukkitPlayer().getInventory())
                    {
                        // deny foreign repositories
                        evt.getBukkitEvent().setCancelled(true);
                    }
                    break;
                case HOTBAR_MOVE_AND_READD:
                case HOTBAR_SWAP:
                case NOTHING:
                case PICKUP_ALL:
                case PICKUP_HALF:
                case PICKUP_ONE:
                case PICKUP_SOME:
                    // allow
                    break;
            }
        }
    }
    
    /**
     * Player mouse click
     * 
     * @param evt
     */
    @McEventHandler
    public void onClick(McPlayerInteractEvent evt)
    {
        if (evt.getBukkitEvent().hasBlock())
        {
            final ItemStack stack = evt.getBukkitEvent().getItem();
            final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
            if (stack != null && helper.getCustomData(stack, ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_MARKER) != null)
            {
                evt.getBukkitEvent().setCancelled(true);
                final String id = helper.getCustomData(stack, ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_ID);
                
                final ToolMarker marker = evt.getPlayer().getSessionStorage().get(ToolMarker.class);
                final ToolData data = marker == null ? null : marker.getTools().get(id);
                if (marker != null && data != null)
                {
                    boolean clear = false;
                    if (evt.getBukkitEvent().getAction() == Action.LEFT_CLICK_BLOCK)
                    {
                        if (data.getLeftClickHandler() != null)
                        {
                            try
                            {
                                try
                                {
                                    data.getLeftClickHandler().accept(evt.getPlayer(), evt);
                                }
                                catch (RuntimeException ex)
                                {
                                    throw new McException(CommonMessages.InternalError, ex, ex.getMessage());
                                }
                                clear = true;
                            }
                            catch (McException ex)
                            {
                                LOGGER.log(Level.INFO, "Error invoking left click handler", ex); //$NON-NLS-1$
                                evt.getPlayer().sendMessage(ex.getErrorMessage(), ex.getArgs());
                            }
                        }
                    }
                    else if (evt.getBukkitEvent().getAction() == Action.RIGHT_CLICK_BLOCK)
                    {
                        if (data.getRightClickHandler() != null)
                        {
                            try
                            {
                                try
                                {
                                    data.getRightClickHandler().accept(evt.getPlayer(), evt);
                                }
                                catch (RuntimeException ex)
                                {
                                    throw new McException(CommonMessages.InternalError, ex, ex.getMessage());
                                }
                                clear = true;
                            }
                            catch (McException ex)
                            {
                                LOGGER.log(Level.INFO, "Error invoking right click handler", ex); //$NON-NLS-1$
                                evt.getPlayer().sendMessage(ex.getErrorMessage(), ex.getArgs());
                            }
                        }
                    }
                    
                    if (clear && data.isOneUse())
                    {
                        // search and clear existing tooling
                        final PlayerInventory inventory = evt.getPlayer().getBukkitPlayer().getInventory();
                        for (int i = 0; i < inventory.getSize(); i++)
                        {
                            final ItemStack invitem = inventory.getItem(i);
                            if (id.equals(helper.getCustomData(invitem, ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_ID)) && helper.getCustomData(invitem, ToolBuilderImpl.CUSTOMDATA_PLUGIN, ToolBuilderImpl.CUSTOMKEY_TOOLING_MARKER) != null)
                            {
                                inventory.setItem(i, new ItemStack(Material.AIR));
                                evt.getPlayer().getBukkitPlayer().updateInventory();
                                break;
                            }
                        }
                        marker.getTools().remove(id);
                        if (marker.getTools().size() == 0)
                        {
                            evt.getPlayer().unregisterHandlers((Plugin) McLibInterface.instance(), ItemServiceImpl.this);
                            evt.getPlayer().getSessionStorage().set(ToolMarker.class, null);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * A marker for players that downloaded the resource pack.
     */
    public static final class ResourcePackMarker extends AnnotatedDataFragment
    {
        /** the resource pack state. */
        @PersistentField
        private ResourcePackStatus state;
        /** runnable for success. */
        private McRunnable         success;
        /** runnable for failure. */
        private McRunnable         failure;
        /** runnable for declined. */
        private McRunnable         declined;
        
        /**
         * @param state
         */
        public ResourcePackMarker(ResourcePackStatus state)
        {
            this.state = state;
        }
        
        /**
         * Constructor
         * 
         * @param success
         * @param failure
         * @param declined
         */
        public ResourcePackMarker(McRunnable success, McRunnable failure, McRunnable declined)
        {
            this.success = success;
            this.failure = failure;
            this.declined = declined;
        }
        
        /**
         * @param state
         *            the state to set
         */
        public void setState(ResourcePackStatus state)
        {
            this.state = state;
        }
        
        /**
         * @return the state
         */
        public ResourcePackStatus getState()
        {
            return this.state;
        }
        
        /**
         * @return the success
         */
        public McRunnable getSuccess()
        {
            return this.success;
        }
        
        /**
         * @return the failure
         */
        public McRunnable getFailure()
        {
            return this.failure;
        }
        
        /**
         * @return the declined
         */
        public McRunnable getDeclined()
        {
            return this.declined;
        }
        
    }
    
    /**
     * Tool data helper
     * @author mepeisen
     */
    public static final class ToolMarker extends AnnotatedDataFragment
    {
        
        /**
         * tools
         */
        @PersistentField
        protected Map<String, ToolData> tools = new HashMap<>();

        /**
         * @return the tools
         */
        public Map<String, ToolData> getTools()
        {
            return this.tools;
        }
        
    }
    
    /**
     * A marker for players that have installed tools
     */
    public static final class ToolData extends AnnotatedDataFragment
    {
        
        /**
         * boolean for destroying of tool upon use
         */
        @PersistentField
        protected boolean                                                isOneUse;
        
        /**
         * non persistent left click handler.
         */
        protected McBiConsumer<McPlayerInterface, McPlayerInteractEvent> leftClickHandler;
        
        /**
         * non persistent right click handler.
         */
        protected McBiConsumer<McPlayerInterface, McPlayerInteractEvent> rightClickHandler;
        
        /**
         * @return the isOneUse
         */
        public boolean isOneUse()
        {
            return this.isOneUse;
        }
        
        /**
         * @param isOneUse
         *            the isOneUse to set
         */
        public void setOneUse(boolean isOneUse)
        {
            this.isOneUse = isOneUse;
        }
        
        /**
         * @return the leftClickHandler
         */
        public McBiConsumer<McPlayerInterface, McPlayerInteractEvent> getLeftClickHandler()
        {
            return this.leftClickHandler;
        }
        
        /**
         * @param leftClickHandler
         *            the leftClickHandler to set
         */
        public void setLeftClickHandler(McBiConsumer<McPlayerInterface, McPlayerInteractEvent> leftClickHandler)
        {
            this.leftClickHandler = leftClickHandler;
        }
        
        /**
         * @return the rightClickHandler
         */
        public McBiConsumer<McPlayerInterface, McPlayerInteractEvent> getRightClickHandler()
        {
            return this.rightClickHandler;
        }
        
        /**
         * @param rightClickHandler
         *            the rightClickHandler to set
         */
        public void setRightClickHandler(McBiConsumer<McPlayerInterface, McPlayerInteractEvent> rightClickHandler)
        {
            this.rightClickHandler = rightClickHandler;
        }
        
    }
    
    @Override
    public ItemStack createItem(BlockId id, BlockVariantId variant)
    {
        return this.createItem(id, variant, this.blockIdMap.get(id).getNameProvider().getName());
    }
    
    @Override
    public ItemStack createItem(BlockId id, BlockVariantId variant, LocalizedMessageInterface name, Serializable... nameArgs)
    {
        return this.createItem(id, variant, name == null ? null : name.toUserMessage(McLibInterface.instance().getDefaultLocale(), nameArgs));
    }
    
    @Override
    public ItemStack createItem(BlockId id, BlockVariantId variant, String name)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).createItemStackForBlock(this.blockIdMap.get(id).getNumId(), variant.ordinal(),
                name == null ? "" : name); //$NON-NLS-1$
    }
    
    @Override
    public ItemStack createItem(McPlayerInterface player, BlockId id, BlockVariantId variant)
    {
        return this.createItem(player, id, variant, this.blockIdMap.get(id).getNameProvider().getName());
    }
    
    @Override
    public ItemStack createItem(McPlayerInterface player, BlockId id, BlockVariantId variant, LocalizedMessageInterface name, Serializable... nameArgs)
    {
        return this.createItem(id, variant, name == null ? null : player.encodeMessage(name, nameArgs)[0]);
    }
    
    @Override
    public BlockId getBlockId(ItemStack stack)
    {
        @SuppressWarnings("deprecation")
        final int typeId = stack.getTypeId();
        final CustomBlock customBlock = this.blockNumIdMap.get(typeId);
        return customBlock == null ? null : customBlock.getBlockId();
    }
    
    @Override
    public BlockId getBlockId(Block block)
    {
        @SuppressWarnings("deprecation")
        final int typeId = block.getTypeId();
        final CustomBlock customBlock = this.blockNumIdMap.get(typeId);
        return customBlock == null ? null : customBlock.getBlockId();
    }
    
    @Override
    public BlockVariantId getBlockVariantId(ItemStack stack)
    {
        final BlockId block = this.getBlockId(stack);
        if (block != null)
        {
            final int variantId = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).getVariant(stack);
            final CustomBlock custom = this.blockIdMap.get(block);
            return custom.getVariant(variantId);
        }
        return null;
    }
    
    @Override
    public BlockVariantId getBlockVariantId(Block b)
    {
        final BlockId block = this.getBlockId(b);
        if (block != null)
        {
            final int variantId = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).getVariant(b);
            final CustomBlock custom = this.blockIdMap.get(block);
            return custom.getVariant(variantId);
        }
        return null;
    }
    
    @Override
    public void setBlockData(Block block, BlockId id, BlockVariantId variant)
    {
        Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).setBlockVariant(block, this.blockIdMap.get(id).getNumId(), variant.ordinal());
    }
    
    /**
     * Saves the items to config
     * 
     * @param items
     */
    private void saveItems(CustomItem[] items)
    {
        McModdedWorldConfig.CustomItems.setObjectList(items, "list"); //$NON-NLS-1$
        McModdedWorldConfig.CustomItems.saveConfig();
    }
    
    /**
     * Saves the blocks to config
     * 
     * @param blocks
     */
    private void saveBlocks(CustomBlock[] blocks)
    {
        McModdedWorldConfig.CustomBlocks.setObjectList(blocks, "list"); //$NON-NLS-1$
        McModdedWorldConfig.CustomBlocks.saveConfig();
    }
    
    /**
     * loads items from config
     * 
     * @return items
     */
    private CustomItem[] loadItems()
    {
        if (McModdedWorldConfig.CustomItems.isset("list")) //$NON-NLS-1$
        {
            return McModdedWorldConfig.CustomItems.getObjectList(CustomItem.class, "list"); //$NON-NLS-1$
        }
        return new CustomItem[0];
    }
    
    /**
     * loads blocks from config
     * 
     * @return blocks
     */
    private CustomBlock[] loadBlocks()
    {
        if (McModdedWorldConfig.CustomBlocks.isset("list")) //$NON-NLS-1$
        {
            return McModdedWorldConfig.CustomBlocks.getObjectList(CustomBlock.class, "list"); //$NON-NLS-1$
        }
        return new CustomBlock[0];
    }
    
    @Override
    public void setDisplayName(ItemStack stack, String name)
    {
        Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).setDisplayName(stack, name);
    }
    
    @Override
    public void setDisplayName(ItemStack stack, McPlayerInterface player, LocalizedMessageInterface name, Serializable... nameArgs)
    {
        this.setDisplayName(stack, player.encodeMessage(name, nameArgs)[0]);
    }
    
    @Override
    public void setDescription(ItemStack stack, String[] description)
    {
        Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).setDescription(stack, description);
    }
    
    @Override
    public void setDescription(ItemStack stack, McPlayerInterface player, LocalizedMessageInterface description, Serializable... descriptionArgs)
    {
        this.setDescription(stack, player.encodeMessage(description, descriptionArgs));
    }
    
    @Override
    public String getDisplayName(ItemStack stack)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).getDisplayName(stack);
    }
    
    @Override
    public String[] getDescription(ItemStack stack)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).getDescription(stack);
    }
    
    @Override
    public void createMinable(Random random, Location location, BlockId block, BlockVariantId variant, int size)
    {
        final int blockId = blockToNumId(block);
        final int meta = variant.ordinal();
        Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).createMinable(random, location, blockId, meta, size);
    }
    
    @Override
    public ItemStack addToPlayerInventory(McPlayerInterface player, ItemStack stack)
    {
        @SuppressWarnings("deprecation")
        final int typeId = stack.getTypeId();
        if (typeId < MclibConstants.MIN_BLOCK_ID)
        {
            return player.getBukkitPlayer().getInventory().addItem(stack).get(0);
        }
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).addToInventory(player.getBukkitPlayer().getInventory(), stack);
    }
    
    /**
     * Populate ping data with block/item info
     * 
     * @param ping
     */
    public void populate(PingData ping)
    {
        for (final BlockId block : this.blockIdMap.keySet())
        {
            final int blockId = blockToNumId(block);
            final BlockMeta meta = block.meta();
            ping.addMeta(blockId, meta == null ? 0 : meta.hardness(), meta == null ? 0 : meta.resistance());
        }
        for (final ItemId item : this.itemIdMap.keySet())
        {
            final int itemId = this.itemIdMap.get(item).getNumId();
            if (itemId > 0)
            {
                final ItemClass[] classes = item.getItemClasses();
                if (classes.length > 0)
                {
                    switch (item.getItemClasses()[0])
                    {
                        case Armor:
                            final ItemArmor armor = item.armor();
                            switch (armor.slot())
                            {
                                case Boots:
                                    ping.addMeta(itemId, armor.durability(), 0.0f, 0.0f, PingData.ItemClass.Boots);
                                    break;
                                case Chestplate:
                                    ping.addMeta(itemId, armor.durability(), 0.0f, 0.0f, PingData.ItemClass.Chestplate);
                                    break;
                                case Helmet:
                                    ping.addMeta(itemId, armor.durability(), 0.0f, 0.0f, PingData.ItemClass.Helmet);
                                    break;
                                case Leggins:
                                    ping.addMeta(itemId, armor.durability(), 0.0f, 0.0f, PingData.ItemClass.Leggins);
                                    break;
                                default:
                                    // should never happen
                                    break;
                            }
                            break;
                        case Axe:
                            final ItemAxe axe = item.axe();
                            ping.addMeta(itemId, axe.durability(), axe.speed(), axe.damage(), PingData.ItemClass.Axe);
                            break;
                        case Hoe:
                            final ItemHoe hoe = item.hoe();
                            ping.addMeta(itemId, hoe.durability(), hoe.speed(), hoe.damage(), PingData.ItemClass.Hoe);
                            break;
                        case Pickaxe:
                            final ItemPickaxe pickaxe = item.pickaxe();
                            ping.addMeta(itemId, pickaxe.durability(), pickaxe.speed(), pickaxe.damage(), PingData.ItemClass.Pickaxe);
                            break;
                        case Shovel:
                            final ItemShovel shovel = item.shovel();
                            ping.addMeta(itemId, shovel.durability(), shovel.speed(), shovel.damage(), PingData.ItemClass.Shovel);
                            break;
                        case Sword:
                            final ItemSword sword = item.sword();
                            ping.addMeta(itemId, sword.durability(), sword.speed(), sword.damage(), PingData.ItemClass.Sword);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
    
    /**
     * On plugin enable
     * 
     * @param plugin
     */
    public void onEnable(Plugin plugin)
    {
        final List<Runnable> list = this.lazyPluginInit.remove(plugin.getName());
        if (list != null)
        {
            list.forEach(Runnable::run);
        }
    }
    
    /**
     * the item info
     */
    private static final class ItemInfo
    {
        /** material */
        private final Material material;
        /** durability */
        private final short    durability;
        /** numeric id */
        private final int      numId;
        
        /**
         * @param material
         * @param durability
         * @param numId
         */
        public ItemInfo(Material material, short durability, int numId)
        {
            this.material = material;
            this.durability = durability;
            this.numId = numId;
        }
        
        /**
         * @return the material
         */
        public Material getMaterial()
        {
            return this.material;
        }
        
        /**
         * @return the durability
         */
        public short getDurability()
        {
            return this.durability;
        }
        
        /**
         * @return the numId
         */
        public int getNumId()
        {
            return this.numId;
        }
    }

    @Override
    public boolean isPlant(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPlant(material);
    }

    @Override
    public boolean isPlant(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPlant(blockToNumId(block));
    }

    @Override
    public boolean isGourd(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isGourd(material);
    }

    @Override
    public boolean isGourd(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isGourd(blockToNumId(block));
    }

    @Override
    public boolean isCoral(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCoral(material);
    }

    @Override
    public boolean isCoral(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCoral(blockToNumId(block));
    }

    @Override
    public boolean isGrass(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isGrass(material);
    }

    @Override
    public boolean isGrass(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isGrass(blockToNumId(block));
    }

    @Override
    public boolean isWood(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isWood(material);
    }

    @Override
    public boolean isWood(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isWood(blockToNumId(block));
    }

    @Override
    public boolean isRock(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isRock(material);
    }

    @Override
    public boolean isRock(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isRock(blockToNumId(block));
    }

    @Override
    public boolean isOre(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isOre(material);
    }

    @Override
    public boolean isOre(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isOre(blockToNumId(block));
    }

    @Override
    public boolean isHeavy(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isHeavy(material);
    }

    @Override
    public boolean isHeavy(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isHeavy(blockToNumId(block));
    }

    @Override
    public boolean isWater(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isWater(material);
    }

    @Override
    public boolean isWater(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isWater(blockToNumId(block));
    }

    @Override
    public boolean isLava(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isLava(material);
    }

    @Override
    public boolean isLava(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isLava(blockToNumId(block));
    }

    @Override
    public boolean isLeaves(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isLeaves(material);
    }

    @Override
    public boolean isLeaves(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isLeaves(blockToNumId(block));
    }

    @Override
    public boolean isVine(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isVine(material);
    }

    @Override
    public boolean isVine(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isVine(blockToNumId(block));
    }

    @Override
    public boolean isSponge(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isSponge(material);
    }

    @Override
    public boolean isSponge(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isSponge(blockToNumId(block));
    }

    @Override
    public boolean isCloth(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCloth(material);
    }

    @Override
    public boolean isCloth(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCloth(blockToNumId(block));
    }

    @Override
    public boolean isFire(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isFire(material);
    }

    @Override
    public boolean isFire(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isFire(blockToNumId(block));
    }

    @Override
    public boolean isSand(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isSand(material);
    }

    @Override
    public boolean isSand(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isSand(blockToNumId(block));
    }

    @Override
    public boolean isCircuits(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCircuits(material);
    }

    @Override
    public boolean isCircuits(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCircuits(blockToNumId(block));
    }

    @Override
    public boolean isCarpet(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCarpet(material);
    }

    @Override
    public boolean isCarpet(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCarpet(blockToNumId(block));
    }

    @Override
    public boolean isGlass(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isGlass(material);
    }

    @Override
    public boolean isGlass(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isGlass(blockToNumId(block));
    }

    @Override
    public boolean isRedstoneLight(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isRedstoneLight(material);
    }

    @Override
    public boolean isRedstoneLight(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isRedstoneLight(blockToNumId(block));
    }

    @Override
    public boolean isTnt(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isTnt(material);
    }

    @Override
    public boolean isTnt(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isTnt(blockToNumId(block));
    }

    @Override
    public boolean isIce(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isIce(material);
    }

    @Override
    public boolean isIce(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isIce(blockToNumId(block));
    }

    @Override
    public boolean isPackedIce(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPackedIce(material);
    }

    @Override
    public boolean isPackedIce(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPackedIce(blockToNumId(block));
    }

    @Override
    public boolean isSnow(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isSnow(material);
    }

    @Override
    public boolean isSnow(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isSnow(blockToNumId(block));
    }

    @Override
    public boolean isCraftedSnow(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCraftedSnow(material);
    }

    @Override
    public boolean isCraftedSnow(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCraftedSnow(blockToNumId(block));
    }

    @Override
    public boolean isCactus(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCactus(material);
    }

    @Override
    public boolean isCactus(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCactus(blockToNumId(block));
    }

    @Override
    public boolean isClay(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isClay(material);
    }

    @Override
    public boolean isClay(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isClay(blockToNumId(block));
    }

    @Override
    public boolean isDragonEgg(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isDragonEgg(material);
    }

    @Override
    public boolean isDragonEgg(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isDragonEgg(blockToNumId(block));
    }

    @Override
    public boolean isPortal(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPortal(material);
    }

    @Override
    public boolean isPortal(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPortal(blockToNumId(block));
    }

    @Override
    public boolean isCake(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCake(material);
    }

    @Override
    public boolean isCake(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isCake(blockToNumId(block));
    }

    @Override
    public boolean isWeb(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isWeb(material);
    }

    @Override
    public boolean isWeb(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isWeb(blockToNumId(block));
    }

    @Override
    public boolean isPiston(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPiston(material);
    }

    @Override
    public boolean isPiston(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isPiston(blockToNumId(block));
    }

    @Override
    public boolean isBarrier(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isBarrier(material);
    }

    @Override
    public boolean isBarrier(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isBarrier(blockToNumId(block));
    }

    @Override
    public boolean isStructureVoid(Material material)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isStructureVoid(material);
    }

    @Override
    public boolean isStructureVoid(BlockId block)
    {
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).isStructureVoid(blockToNumId(block));
    }
    
}
