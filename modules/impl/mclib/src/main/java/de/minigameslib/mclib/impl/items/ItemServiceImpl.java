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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.Files;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.MinecraftVersionsType;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.items.ItemId;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.impl.McCoreConfig;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;

/**
 * The item service implementation.
 * 
 * @author mepeisen
 */
public class ItemServiceImpl implements ItemServiceInterface
{
    
    // TODO support version control...
    
    /** java logger */
    private static final Logger LOGGER = Logger.getLogger(ItemServiceImpl.class.getName());
    
    /** the item id to value map. */
    private Map<ItemId, CustomItem> idMap = new HashMap<>();
    
    /** the custom items per material/ damage value */
    private final Map<Material, Map<Short, CustomItem>> items = new HashMap<>();
    
    /**
     * Initialized the items from registered enumerations
     */
    public void init()
    {
        final SortedSet<CustomItem> sorted = new TreeSet<>();
        for (final ItemId item : EnumServiceInterface.instance().getEnumValues(ItemId.class))
        {
            final CustomItem custom = new CustomItem(item.getPluginName(), item.name(), item);
            this.idMap.put(item, custom);
            sorted.add(custom);
        }
        
        for (final CustomItemTypes type : CustomItemTypes.values())
        {
            final Map<Short, CustomItem> map = new HashMap<>();
            this.items.put(type.getMaterial(), map);
            for (final Durability dur : type.getDurabilities())
            {
                final CustomItem item = sorted.first();
                sorted.remove(item);
                item.setCustomDurability(dur);
                item.setCustomType(type);
                map.put(dur.getItemStackDurability(), item);
                
                if (sorted.isEmpty())
                {
                    return;
                }
            }
        }
        
        // TODO warn: too much items
    }
    
    @Override
    public void setDownloadUrl(String url)
    {
        McCoreConfig.RespourcePackDownloadUrl.setString(url);
        McCoreConfig.RespourcePackDownloadUrl.saveConfig();
    }
    
    @Override
    public String getDownloadUrl()
    {
        return McCoreConfig.RespourcePackDownloadUrl.getString();
    }
    
    @Override
    public boolean hasResourcePack(McPlayerInterface player)
    {
        return player.getSessionStorage().get(ResourcePackMarker.class) != null;
    }
    
    @Override
    public ItemStack createItem(ItemId item, String name)
    {
        final CustomItem custom = this.idMap.get(item);
        final ItemStack itemStack = new ItemStack(
                custom.getCustomType().getMaterial(),
                1,
                custom.getCustomDurability().getItemStackDurability());
        final ItemMeta meta = itemStack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    @Override
    public ItemId getItemId(ItemStack stack)
    {
        final Map<Short, CustomItem> map = this.items.get(stack.getData().getItemType());
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
            if (McLibInterface.instance().getMinecraftVersion().isAtLeast(MinecraftVersionsType.V1_11))
            {
                copyFile(jar, this.getClass().getClassLoader(), "de/minigameslib/mclib/resources/v3/pack.mcmeta"); //$NON-NLS-1$
            }
            else
            {
                copyFile(jar, this.getClass().getClassLoader(), "de/minigameslib/mclib/resources/v2/pack.mcmeta"); //$NON-NLS-1$
            }
            jar.closeEntry();
            
            // default model overrides
            this.items.forEach((material, map) -> {
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
            });
            
            // model files
            this.items.forEach((material, map) -> {
                for (CustomItem item : map.values())
                {
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
                                copyFile(jar, item.getClass().getClassLoader(), texture);
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
            });
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
     * A marker for players that downloaded the resource pack.
     */
    public static final class ResourcePackMarker extends AnnotatedDataFragment
    {
        // marker only
    }
    
}
