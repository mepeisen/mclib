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
 * @author mepeisen
 * @param <IdInterface> 
 * @param <Id> 
 * @param <Comp> 
 * @param <TypeId> 
 * @param <Handler> 
 *
 */
public class ObjectsContainer<IdInterface extends DataFragment, Id extends IdInterface, Comp, TypeId, Handler>
{
    
    /** the components. */
    private final Map<Id, Comp> components = new HashMap<>();
    
    /** registered components per plugin name. */
    private final Map<String, Map<Id, Boolean>>                           componentsByPlugin      = new HashMap<>();

    /**
     * @param pluginName
     * @param id
     * @param impl
     * @param persist
     */
    public void put(String pluginName, Id id, Comp impl, boolean persist)
    {
        this.componentsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, persist);
        this.components.put(id, impl);
    }

    /** 
     * @param id
     * @return value 
     */
    public Comp remove(Id id)
    {
        return this.components.remove(id);
    }

    /**
     * @param pluginName 
     * @param id
     * @param folder
     * @return value
     * @throws McException 
     */
    public Comp remove(String pluginName, Id id, File folder) throws McException
    {
        final Map<Id, Boolean> map = this.componentsByPlugin.get(pluginName);
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
     * @param pluginName
     * @return ids
     */
    public Map<Id, Boolean> removePlugin(String pluginName)
    {
        final Map<Id, Boolean> cmap = this.componentsByPlugin.remove(pluginName);
        return cmap == null ? Collections.emptyMap() : cmap;
    }

    /**
     * @param id
     * @return value
     */
    public Comp get(Id id)
    {
        return this.components.get(id);
    }
    
    /**
     * For each component
     * @param consumer
     */
    public void forEach(Consumer<Comp> consumer)
    {
        this.components.values().forEach(consumer);
    }

    /**
     * @param pluginName
     * @return boolean
     */
    public boolean containsPluginName(String pluginName)
    {
        return this.componentsByPlugin.containsKey(pluginName);
    }
    
    /**
     * @param pluginName
     * @return ids by plugin name
     */
    public Set<Id> getByPlugin(String pluginName)
    {
        return this.componentsByPlugin.get(pluginName).keySet();
    }
    
    /**
     * Load registry
     * @param factory
     * @param folder
     * @throws McException
     */
    public void loadRegistry(Supplier<Id> factory, File folder) throws McException
    {
        final File file = new File(folder, "registry.yml"); //$NON-NLS-1$
        if (file.exists())
        {
            try
            {
                final YmlFile config = new YmlFile(file);
                for (final String pluginName : config.getKeys(false))
                {
                    final Map<Id, Boolean> idmap = this.componentsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>());
                    final DataSection pluginSection = config.getSection(pluginName);
                    for (final String idkey : pluginSection.getKeys(false))
                    {
                        final DataSection section = pluginSection.getSection(idkey);
                        final Id id = factory.get();
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
     * @param folder
     * @throws McException
     */
    public void saveIdList(File folder) throws McException
    {
        final YmlFile fileConfig = new YmlFile();
        this.componentsByPlugin.forEach((k, v) -> {
            final DataSection section = fileConfig.createSection(k);
            int i = 0;
            for (final Map.Entry<Id, Boolean> data : v.entrySet())
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
     * resume objects
     * @param pluginName
     * @param typesByPlugin
     * @param mapIdToType
     * @param safeCreateHandler
     * @param creator
     * @param brokenComponents
     */
    public void resumeObjects(String pluginName, Map<String, Map<String, TypeId>> typesByPlugin, Function<Id, String> mapIdToType, McBiFunction<String, TypeId, Handler> safeCreateHandler, McBiFunction<Id, Handler, Comp> creator, Map<IdInterface, McException> brokenComponents)
    {
        if (this.containsPluginName(pluginName))
        {
            for (final Id id : this.getByPlugin(pluginName))
            {
                if (!typesByPlugin.containsKey(pluginName))
                {
                    brokenComponents.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, mapIdToType.apply(id), "?")); //$NON-NLS-1$
                    continue;
                }
                
                final TypeId type = typesByPlugin.get(pluginName).get(mapIdToType.apply(id));
                if (type == null)
                {
                    brokenComponents.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, mapIdToType.apply(id), "?")); //$NON-NLS-1$
                    continue;
                }
                
                try
                {
                    // init
                    final Handler handler = safeCreateHandler.apply(pluginName, type);
                    final Comp impl = creator.apply(id, handler);
                    
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
