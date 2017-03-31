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
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
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
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McInventoryClickEvent;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.McPlayerDeathEvent;
import de.minigameslib.mclib.api.event.McPlayerDropItemEvent;
import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
import de.minigameslib.mclib.api.items.BlockVariantId;
import de.minigameslib.mclib.api.items.ItemId;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.items.ResourceServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.McCoreConfig;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.pshared.MclibConstants;
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
    private static final Logger LOGGER = Logger.getLogger(ItemServiceImpl.class.getName());
    
    /** the item id to value map. */
    private Map<ItemId, CustomItem> itemIdMap = new HashMap<>();
    
    /** the custom items per material/ damage value */
    private final Map<Material, Map<Short, CustomItem>> itemsPerMaterial = new HashMap<>();
    
    /** the block to numId map. */
    private Map<CustomItem, Object[]> itemMap = new HashMap<>();
    
    /** the block id to value map. */
    private Map<BlockId, CustomBlock> blockIdMap = new HashMap<>();
    
    /** the block id to value map. */
    private Map<Integer, CustomBlock> blockNumIdMap = new HashMap<>();
    
    /** the block to numId map. */
    private Map<CustomBlock, Integer> blockMap = new HashMap<>();
    
    /**
     * Initialized the items from registered enumerations
     */
    public void init()
    {
        initItems();
        initBlocks();
    }

    /**
     * initializes the items
     */
    private void initItems()
    {
        // load from config
        for (final CustomItem item : this.loadItems())
        {
            final Material material = item.getCustomType().getMaterial();
            final short durability = item.getDurability();
            this.itemsPerMaterial.computeIfAbsent(material, m -> new HashMap<>()).put(durability, item);
            this.itemMap.put(item, new Object[]{material, durability});
        }
        
        final Stack<CustomItem> newItems = new Stack<>();
        
        // parse items from plugins
        final List<ItemId> enumValues = EnumServiceInterface.instance().getEnumValues(ItemId.class).stream().sorted((a, b) -> {
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
                final Object[] key = this.itemMap.get(custom);
                final CustomItem custom2 = this.itemsPerMaterial.get(key[0]).get(key[1]);
                custom2.setItemId(item);
                custom2.setCustomDurability(custom2.getCustomType().getDurabilities()[custom2.getDurability() - 1]);
                this.itemIdMap.put(item, custom2);
            }
            else
            {
                // this is a new item
                custom.setItemId(item);
                newItems.add(custom);
                this.itemIdMap.put(item, custom);
            }
        }
        
        if (!newItems.isEmpty())
        {
            for (final CustomItemTypes type : CustomItemTypes.values())
            {
                this.itemsPerMaterial.computeIfAbsent(type.getMaterial(), k -> new HashMap<>());
                for (final Durability dur : type.getDurabilities())
                {
                    if (this.itemsPerMaterial.get(type.getMaterial()).containsKey(dur.getItemStackDurability())) continue;
                    
                    final CustomItem item = newItems.pop();
                    item.setCustomDurability(dur);
                    item.setCustomType(type);
                    this.itemsPerMaterial.get(type.getMaterial()).put(dur.getItemStackDurability(), item);
                    this.itemMap.put(item, new Object[]{type.getMaterial(), dur.getItemStackDurability()});
                    
                    if (newItems.isEmpty())
                    {
                        this.saveItems(this.itemMap.keySet().toArray(new CustomItem[this.itemMap.size()]));
                        return;
                    }
                }
            }
            
            // TODO warn: too much items
        }
            
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
        
        final Stack<CustomBlock> newBlocks = new Stack<>();
        
        // parse blocks from plugins
        final List<BlockId> enumValues = EnumServiceInterface.instance().getEnumValues(BlockId.class).stream().sorted((a, b) -> {
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
                if (this.blockNumIdMap.containsKey(i)) continue;
                
                final CustomBlock block = newBlocks.pop();
                block.setNumId(i);
                this.blockNumIdMap.put(i, block);
                this.blockMap.put(block, i);
                
                if (newBlocks.isEmpty())
                {
                    this.saveBlocks(this.blockMap.keySet().toArray(new CustomBlock[this.blockMap.size()]));
                    return;
                }
            }
            
            // TODO warn: too much blocks
        }
    }
    
    @Override
    public ResourceVersion getResourceVersion(MinecraftVersionsType minecraftVersion)
    {
        if (minecraftVersion.isBelow(MinecraftVersionsType.V1_9)) return ResourceVersion.PACK_FORMAT_1;
        if (minecraftVersion.isBelow(MinecraftVersionsType.V1_11)) return ResourceVersion.PACK_FORMAT_2;
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
        final ItemStack itemStack = new ItemStack(
                custom.getCustomType().getMaterial(),
                1,
                custom.getCustomDurability().getItemStackDurability());
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
    
    @Override
    public ItemId getItemId(ItemStack stack)
    {
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
                buffer.append("{\"predicate\": {\"damaged\": 0, \"damage\": ").append(item.getCustomDurability().getModelDurability()).append("}, \"model\": \"item/").append(item.getPluginName()).append('/').append(item.getEnumName()).append("\"},"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
     * @param jar
     * @param numId
     * @param block
     */
    private void writeBlockstates(JarOutputStream jar, Integer numId, CustomBlock block)
    {
        try
        {
            final BlockId blockId = block.getBlockId();
            final JarEntry blockStatesJson = new JarEntry("assets/mclib/blockstates/custom-" + numId + ".json"); //$NON-NLS-1$ //$NON-NLS-2$
            blockStatesJson.setTime(System.currentTimeMillis());
            jar.putNextEntry(blockStatesJson);
            final StringBuilder buffer = new StringBuilder();
            buffer.append("{ \"variants\": {"); //$NON-NLS-1$
            boolean first = true;
            for (final BlockVariantId variant : blockId.variants())
            {
                if (first) first = false;
                else buffer.append(", "); //$NON-NLS-1$
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
                // TODO logging, missing item
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
            if (helper.getCustomData(item, "mclib", "clearOnJoin") != null)  //$NON-NLS-1$//$NON-NLS-2$
            {
                inventory.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }

    @Override
    public ToolBuilderInterface prepareTool(ItemId item, McPlayerInterface player, LocalizedMessageInterface title, Serializable... titleArgs)
    {
        return new ToolBuilderInterface() {
            
            /** flag for single use */
            private boolean singleUse;
            
            /** right click handler */
            private McBiConsumer<McPlayerInterface, McPlayerInteractEvent> rightClick;
            
            /** left click handler */
            private McBiConsumer<McPlayerInterface, McPlayerInteractEvent> leftClick;

            /** description */
            private LocalizedMessageInterface description;

            /** description arguments */
            private Serializable[] descriptionArgs;
            
            @Override
            public ToolBuilderInterface singleUse()
            {
                this.singleUse = true;
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
                
                ToolMarker marker = player.getSessionStorage().get(ToolMarker.class);
                if (marker == null)
                {
                    marker = new ToolMarker();
                    player.getSessionStorage().set(ToolMarker.class, marker);
                    player.registerHandlers((Plugin) McLibInterface.instance(), ItemServiceImpl.this);
                }
                
                final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
                final PlayerInventory inventory = player.getBukkitPlayer().getInventory();
                
                // search existing tooling
                int slot = -1;
                for (int i = 0; i < inventory.getSize(); i++)
                {
                    final ItemStack invitem = inventory.getItem(i);
                    if (helper.getCustomData(invitem, "mclib", "customTooling") != null)  //$NON-NLS-1$//$NON-NLS-2$
                    {
                        slot = i;
                        break;
                    }
                }
                if (slot == -1)
                {
                    slot = inventory.firstEmpty();
                }
                
                // preplace item stack
                ItemStack stack = ItemServiceImpl.this.createItem(item, player.encodeMessage(title, titleArgs)[0]);
                if (this.description != null)
                {
                    final ItemMeta meta = stack.getItemMeta();
                    meta.setLore(Arrays.asList(player.encodeMessage(this.description, this.descriptionArgs)));
                    stack.setItemMeta(meta);
                }
                inventory.setItem(slot, stack);
                stack = inventory.getItem(slot); // forces to use the correct NMS class

                helper.addCustomData(stack, "mclib", "clearOnJoin", "1");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                helper.addCustomData(stack, "mclib", "customTooling", "1");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                marker.setOneUse(this.singleUse);
                marker.setLeftClickHandler(this.leftClick);
                marker.setRightClickHandler(this.rightClick);
                
                player.getBukkitPlayer().updateInventory();
            }
        };
    }
    
    /**
     * Player death event; cancels the drop of the tooling item...
     * @param evt
     */
    @McEventHandler
    public void onDeath(McPlayerDeathEvent evt)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        final List<ItemStack> stacks = evt.getBukkitEvent().getDrops();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (helper.getCustomData(stacks.get(i), "mclib", "customTooling") != null)  //$NON-NLS-1$//$NON-NLS-2$
            {
                stacks.remove(i);
                break;
            }
        }
    }
    
    /**
     * On drop items; cancels drop of tooling item...
     * @param evt
     */
    @McEventHandler
    public void onPlayerDrop(McPlayerDropItemEvent evt)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        if (helper.getCustomData(evt.getBukkitEvent().getItemDrop().getItemStack(), "mclib", "customTooling") != null)  //$NON-NLS-1$//$NON-NLS-2$
        {
            evt.getBukkitEvent().setCancelled(true);
        }
    }
    
    /**
     * On inventory action; cancels drop of tooling item into other inventories...
     * @param evt
     */
    @McEventHandler
    public void onInventoryClick(McInventoryClickEvent evt)
    {
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        final ItemStack cursor = evt.getBukkitEvent().getCursor();
        if (cursor != null && helper.getCustomData(cursor, "mclib", "customTooling") != null)  //$NON-NLS-1$//$NON-NLS-2$
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
     * @param evt
     */
    @McEventHandler
    public void onClick(McPlayerInteractEvent evt)
    {
        if (evt.getBukkitEvent().hasBlock())
        {
            final ItemStack stack = evt.getBukkitEvent().getItem();
            final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
            if (stack != null && helper.getCustomData(stack, "mclib", "customTooling") != null)//$NON-NLS-1$//$NON-NLS-2$
            {
                evt.getBukkitEvent().setCancelled(true);
                
                final ToolMarker marker = evt.getPlayer().getSessionStorage().get(ToolMarker.class);
                if (marker != null)
                {
                    boolean clear = false;
                    if (evt.getBukkitEvent().getAction() == Action.LEFT_CLICK_BLOCK)
                    {
                        if (marker.getLeftClickHandler() != null)
                        {
                            try
                            {
                                try
                                {
                                    marker.getLeftClickHandler().accept(evt.getPlayer(), evt);
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
                        if (marker.getRightClickHandler() != null)
                        {
                            try
                            {
                                try
                                {
                                    marker.getRightClickHandler().accept(evt.getPlayer(), evt);
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
                    
                    if (clear && marker.isOneUse())
                    {
                        // search and clear existing tooling
                        final PlayerInventory inventory = evt.getPlayer().getBukkitPlayer().getInventory();
                        for (int i = 0; i < inventory.getSize(); i++)
                        {
                            final ItemStack invitem = inventory.getItem(i);
                            if (helper.getCustomData(invitem, "mclib", "customTooling") != null)  //$NON-NLS-1$//$NON-NLS-2$
                            {
                                inventory.setItem(i, new ItemStack(Material.AIR));
                                evt.getPlayer().getBukkitPlayer().updateInventory();
                                evt.getPlayer().unregisterHandlers((Plugin) McLibInterface.instance(), ItemServiceImpl.this);
                                evt.getPlayer().getSessionStorage().set(ToolMarker.class, null);
                                break;
                            }
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
        private McRunnable success;
        /** runnable for failure. */
        private McRunnable failure;
        /** runnable for declined. */
        private McRunnable declined;

        /**
         * @param state
         */
        public ResourcePackMarker(ResourcePackStatus state)
        {
            this.state = state;
        }
        
        /**
         * Constructor
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
         * @param state the state to set
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
     * A marker for players that have installed tools
     */
    public static final class ToolMarker extends AnnotatedDataFragment
    {
        
        /**
         * boolean for destroying of tool upon use
         */
        @PersistentField
        private boolean isOneUse;
        
        /**
         * non persistent left click handler.
         */
        private McBiConsumer<McPlayerInterface, McPlayerInteractEvent> leftClickHandler;
        
        /**
         * non persistent right click handler.
         */
        private McBiConsumer<McPlayerInterface, McPlayerInteractEvent> rightClickHandler;

        /**
         * @return the isOneUse
         */
        public boolean isOneUse()
        {
            return this.isOneUse;
        }

        /**
         * @param isOneUse the isOneUse to set
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
         * @param leftClickHandler the leftClickHandler to set
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
         * @param rightClickHandler the rightClickHandler to set
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
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class).createItemStackForBlock(this.blockIdMap.get(id).getNumId(), variant.ordinal(), name == null ? "" : name); //$NON-NLS-1$
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
        final int typeId = stack.getTypeId();
        final CustomBlock customBlock = this.blockNumIdMap.get(typeId);
        return customBlock == null ? null : customBlock.getBlockId();
    }

    @Override
    public BlockId getBlockId(Block block)
    {
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
     * @param items
     */
    private void saveItems(CustomItem[] items)
    {
        McCoreConfig.CustomItems.setObjectList(items, "list"); //$NON-NLS-1$
        McCoreConfig.CustomItems.saveConfig();
    }
    
    /**
     * Saves the blocks to config
     * @param blocks
     */
    private void saveBlocks(CustomBlock[] blocks)
    {
        McCoreConfig.CustomBlocks.setObjectList(blocks, "list"); //$NON-NLS-1$
        McCoreConfig.CustomBlocks.saveConfig();
    }
    
    /**
     * loads items from config
     * @return items
     */
    private CustomItem[] loadItems()
    {
        if (McCoreConfig.CustomItems.isset("list")) //$NON-NLS-1$
        {
            return McCoreConfig.CustomItems.getObjectList(CustomItem.class, "list"); //$NON-NLS-1$
        }
        return new CustomItem[0];
    }
    
    /**
     * loads blocks from config
     * @return blocks
     */
    private CustomBlock[] loadBlocks()
    {
        if (McCoreConfig.CustomBlocks.isset("list")) //$NON-NLS-1$
        {
            return McCoreConfig.CustomBlocks.getObjectList(CustomBlock.class, "list"); //$NON-NLS-1$
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
    
}
