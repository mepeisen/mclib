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

package de.minigameslib.mclib.impl.obj;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McBiFunction;
import de.minigameslib.mclib.impl.yml.YmlFile;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Generic objects container withz utitlity methods on objects.
 * 
 * @param <ID>
 *            interface for ids
 * @param <IDIMPL>
 *            impl class for ids.
 * @param <COMP>
 *            Component type class
 * @param <TYPE>
 *            type id class
 * @param <HANDLER>
 *            handler class
 * 
 * @author mepeisen
 *
 */
public class ObjectsContainer<
    ID extends DataFragment,
    IDIMPL extends ID,
    COMP,
    TYPE,
    HANDLER>
{
    
    /** the components. */
    private final Map<IDIMPL, COMP>                 components         = new HashMap<>();
    
    /** registered components per plugin name. */
    private final Map<String, Map<IDIMPL, Boolean>> componentsByPlugin = new HashMap<>();
    
    /**
     * Puts a new object to container.
     * 
     * @param pluginName
     *            plugin name owning the object.
     * @param id
     *            id of object
     * @param impl
     *            object instance to be stored
     * @param persist
     *            true for persistent objects and false for runtime objects
     */
    public void put(String pluginName, IDIMPL id, COMP impl, boolean persist)
    {
        this.componentsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, persist);
        this.components.put(id, impl);
    }
    
    /**
     * Removes object from container.
     * 
     * @param id
     *            id to be removed.
     * @return value
     */
    public COMP remove(IDIMPL id)
    {
        return this.components.remove(id);
    }
    
    /**
     * Removes object from container.
     * 
     * @param pluginName
     *            owning plugin name
     * @param id
     *            object id to be removed
     * @param folder
     *            data folder to save the id list
     * @return value
     * @throws McException
     *             wrapped io exception
     */
    public COMP remove(String pluginName, IDIMPL id, File folder) throws McException
    {
        final Map<IDIMPL, Boolean> map = this.componentsByPlugin.get(pluginName);
        if (map != null)
        {
            boolean persist = map.remove(id);
            if (persist)
            {
                this.saveIdList(folder);
            }
        }
        return this.components.remove(id);
    }
    
    /**
     * Removes all objects owned by given plugin from container.
     * 
     * @param pluginName
     *            plugin that was disabled.
     * @return ids
     */
    public Map<IDIMPL, Boolean> removePlugin(String pluginName)
    {
        final Map<IDIMPL, Boolean> cmap = this.componentsByPlugin.remove(pluginName);
        return cmap == null ? Collections.emptyMap() : cmap;
    }
    
    /**
     * Returns object from given id.
     * 
     * @param id
     *            id to search for.
     * @return value
     */
    public COMP get(IDIMPL id)
    {
        return this.components.get(id);
    }
    
    /**
     * For each component.
     * 
     * @param consumer
     *            Consumer to invoke for each object.
     */
    public void forEach(Consumer<COMP> consumer)
    {
        this.components.values().forEach(consumer);
    }
    
    /**
     * Checks if given plugin has registered objects.
     * 
     * @param pluginName
     *            plugin name
     * 
     * @return {@code true} if objects of given plugin are registered.
     */
    public boolean containsPluginName(String pluginName)
    {
        return this.componentsByPlugin.containsKey(pluginName);
    }
    
    /**
     * Returns all objects of given plugin.
     * 
     * @param pluginName
     *            plugin name
     * @return ids by plugin name
     */
    public Set<IDIMPL> getByPlugin(String pluginName)
    {
        return this.componentsByPlugin.get(pluginName).keySet();
    }
    
    /**
     * Load registry.
     * 
     * @param factory
     *            Factory to create ids
     * @param folder
     *            folder to read from
     * @throws McException
     *             wrapped io exception
     */
    public void loadRegistry(Supplier<IDIMPL> factory, File folder) throws McException
    {
        final File file = new File(folder, "registry.yml"); //$NON-NLS-1$
        if (file.exists())
        {
            try
            {
                final YmlFile config = new YmlFile(file);
                for (final String pluginName : config.getKeys(false))
                {
                    final Map<IDIMPL, Boolean> idmap = this.componentsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>());
                    final DataSection pluginSection = config.getSection(pluginName);
                    for (final String idkey : pluginSection.getKeys(false))
                    {
                        final DataSection section = pluginSection.getSection(idkey);
                        final IDIMPL id = factory.get();
                        id.read(section);
                        idmap.put(id, Boolean.TRUE);
                    }
                }
            }
            catch (IOException ex)
            {
                throw new McException(CommonMessages.InternalError, ex, ex.getMessage());
            }
        }
    }
    
    /**
     * Saves the id list.
     * 
     * @param folder
     *            folder to save to
     * @throws McException
     *             wrapped io exception
     */
    public void saveIdList(File folder) throws McException
    {
        final YmlFile fileConfig = new YmlFile();
        this.componentsByPlugin.forEach((k, v) ->
        {
            final DataSection section = fileConfig.createSection(k);
            int i = 0;
            for (final Map.Entry<IDIMPL, Boolean> data : v.entrySet())
            {
                if (data.getValue())
                {
                    i++;
                    data.getKey().write(section.createSection("item" + i)); //$NON-NLS-1$
                }
            }
        });
        try
        {
            fileConfig.saveFile(new File(folder, "registry.yml")); //$NON-NLS-1$
        }
        catch (IOException ex)
        {
            throw new McException(CommonMessages.BrokenObjectType, ex);
        }
    }
    
    /**
     * resume objects.
     * 
     * @param pluginName
     *            name of the owning plugin
     * @param typesByPlugin
     *            the types per plugin map
     * @param mapIdToType
     *            the id to type map
     * @param safeCreateHandler
     *            the safe create handler function.
     * @param creator
     *            the object creator function.
     * @param brokenComponents
     *            the map to store broken objects to
     */
    public void resumeObjects(String pluginName, Map<String, Map<String, TYPE>> typesByPlugin, Function<IDIMPL, String> mapIdToType, McBiFunction<String, TYPE, HANDLER> safeCreateHandler,
        McBiFunction<IDIMPL, HANDLER, COMP> creator, Map<ID, McException> brokenComponents)
    {
        if (this.containsPluginName(pluginName))
        {
            for (final IDIMPL id : this.getByPlugin(pluginName))
            {
                if (!typesByPlugin.containsKey(pluginName))
                {
                    brokenComponents.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, mapIdToType.apply(id), "?")); //$NON-NLS-1$
                    continue;
                }
                
                final TYPE type = typesByPlugin.get(pluginName).get(mapIdToType.apply(id));
                if (type == null)
                {
                    brokenComponents.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, mapIdToType.apply(id), "?")); //$NON-NLS-1$
                    continue;
                }
                
                try
                {
                    // init
                    final HANDLER handler = safeCreateHandler.apply(pluginName, type);
                    final COMP impl = creator.apply(id, handler);
                    
                    // store data
                    this.put(pluginName, id, impl, true);
                }
                catch (McException ex)
                {
                    brokenComponents.put(id, ex);
                }
            }
        }
    }
    
}
