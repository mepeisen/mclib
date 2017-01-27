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

package de.minigameslib.mclib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;
import de.minigameslib.mclib.shared.api.com.UniqueEnumerationValue;

/**
 * @author mepeisen
 *
 */
class EnumServiceImpl implements EnumServiceInterface
{
    
    /** logging. */
    private static final Logger LOGGER = Logger.getLogger(EnumServiceImpl.class.getName());
    
    /** enumeration values, listed by plugin. */
    private final Map<Plugin, Set<Enum<?>>> enumsByPlugin = new HashMap<>();
    
    /** enumeration values, listed by plugin and name. */
    private final Map<String, Map<Class<? extends UniqueEnumerationValue>, Map<String, UniqueEnumerationValue>>> uniqueEnums = new HashMap<>();
    
    /** map from enumeration valur oto registering plugin. */
    private final Map<Enum<?>, Plugin> pluginsByEnum = new HashMap<>();

    @Override
    public <T extends Enum<?> & EnumerationValue> void registerEnumClass(Plugin plugin, Class<T> clazz)
    {
        this.registerEnumClass0(plugin, clazz);
    }

    /**
     * Register enum
     * @param plugin
     * @param clazz
     */
    private <T extends Enum<?>> void registerEnumClass0(Plugin plugin, Class<T> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> set = this.enumsByPlugin.computeIfAbsent(plugin, (p) -> new HashSet<>());
            final List<Map<String, UniqueEnumerationValue>> uniqueMaps = this.getUniqueMap(plugin, clazz);
            for (final Enum<?> ev : clazz.getEnumConstants())
            {
                if (this.pluginsByEnum.containsKey(ev))
                {
                    LOGGER.log(Level.SEVERE, "Duplicate registration of enum " + clazz.getName() + ":" + ev.name()); //$NON-NLS-1$ //$NON-NLS-2$
                    throw new IllegalStateException("Duplicate registration of enum " + clazz.getName() + ":" + ev.name()); //$NON-NLS-1$ //$NON-NLS-2$
                }
                this.pluginsByEnum.put(ev, plugin);
                set.add(ev);
                
                for (final Map<String, UniqueEnumerationValue> map : uniqueMaps)
                {
                    if (map.containsKey(ev.name()))
                    {
                        LOGGER.log(Level.SEVERE, "Duplicate registration of unique enum " + clazz.getName() + ":" + ev.name()); //$NON-NLS-1$ //$NON-NLS-2$
                        throw new IllegalStateException("Duplicate registration of unique enum " + clazz.getName() + ":" + ev.name()); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    map.put(ev.name(), (UniqueEnumerationValue) ev);
                }
            }
        }
        if (clazz.getAnnotation(ChildEnum.class) != null)
        {
            for (final Class<? extends Enum<?>> childClazz : clazz.getAnnotation(ChildEnum.class).value())
            {
                this.registerEnumClass0(plugin, childClazz);
            }
        }
    }

    /**
     * @param plugin
     * @param clazz
     * @return unique enumeration map
     */
    private List<Map<String, UniqueEnumerationValue>> getUniqueMap(Plugin plugin, Class<?> clazz)
    {
        final List<Map<String, UniqueEnumerationValue>> result = new ArrayList<>();
        final Map<Class<? extends UniqueEnumerationValue>, Map<String, UniqueEnumerationValue>> map = this.uniqueEnums.computeIfAbsent(plugin.getName(), n -> new HashMap<>());
        for (final Class<?> child : clazz.getInterfaces())
        {
            if (UniqueEnumerationValue.class.isAssignableFrom(child))
            {
                final Class<? extends UniqueEnumerationValue> child2 = child.asSubclass(UniqueEnumerationValue.class);
                result.add(map.computeIfAbsent(child2, k -> new HashMap<>()));
            }
        }
        return result;
    }

    @Override
    public void unregisterAllEnumerations(Plugin plugin)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> set = this.enumsByPlugin.get(plugin);
            if (set != null)
            {
                set.forEach(this.pluginsByEnum::remove);
                this.enumsByPlugin.remove(set);
            }
        }
    }

    @Override
    public Plugin getPlugin(EnumerationValue enumValue)
    {
        synchronized (this.enumsByPlugin)
        {
            return this.pluginsByEnum.get(enumValue);
        }
    }

    @Override
    public Set<Enum<?>> getEnumValues(Plugin plugin)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> s = this.enumsByPlugin.get(plugin);
            return s == null ? Collections.emptySet() : Collections.unmodifiableSet(s);
        }
    }

    @Override
    public <T extends EnumerationValue> Set<T> getEnumValues(Plugin plugin, Class<T> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> s = this.enumsByPlugin.get(plugin);
            return s == null ? Collections.emptySet() : s.stream().filter(o -> clazz.isInstance(o)).map(o -> clazz.cast(o)).collect(Collectors.toSet());
        }
    }

    @Override
    public <T extends EnumerationValue> Set<T> getEnumValues(Class<T> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<T> result = new HashSet<>();
            this.enumsByPlugin.values().forEach(s -> s.stream().filter(e -> clazz.isInstance(e)).map(e -> clazz.cast(e)).forEach(result::add));
            return result;
        }
    }
    
    /**
     * enum value factory method
     * @param plugin
     * @param name
     * @param clazz
     * @return enumeration value or {@code null} if it does not exist
     */
    public <T extends UniqueEnumerationValue> T create(String plugin, String name, Class<T> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Map<Class<? extends UniqueEnumerationValue>, Map<String, UniqueEnumerationValue>> map1 = this.uniqueEnums.get(plugin);
            if (map1 != null)
            {
                final Map<String, UniqueEnumerationValue> map2 = map1.get(clazz);
                if (map2 != null)
                {
                    return clazz.cast(map2.get(name));
                }
            }
        }
        return null;
    }
    
}
