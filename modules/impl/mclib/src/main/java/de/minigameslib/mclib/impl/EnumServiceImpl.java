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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;

/**
 * @author mepeisen
 *
 */
class EnumServiceImpl implements EnumServiceInterface
{
    
    /** enumeration values, listed by plugin. */
    private final Map<Plugin, Set<Enum<?>>> enumsByPlugin = new HashMap<>();
    
    /** map from enumeration valur oto registering plugin. */
    private final Map<Enum<?>, Plugin> pluginsByEnum = new HashMap<>();

    @Override
    public void registerEnumClass(Plugin plugin, Class<? extends Enum<?>> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> set = this.enumsByPlugin.computeIfAbsent(plugin, (p) -> new HashSet<>());
            for (final Enum<?> ev : clazz.getEnumConstants())
            {
                if (this.pluginsByEnum.containsKey(ev))
                {
                    // TODO Logging
                    throw new IllegalStateException("Duplicate registration of enum " + clazz.getName() + ":" + ev.name()); //$NON-NLS-1$ //$NON-NLS-2$
                }
                this.pluginsByEnum.put(ev, plugin);
                set.add(ev);
            }
        }
        if (clazz.getAnnotation(ChildEnum.class) != null)
        {
            for (final Class<? extends Enum<?>> childClazz : clazz.getAnnotation(ChildEnum.class).value())
            {
                this.registerEnumClass(plugin, childClazz);
            }
        }
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
    public Plugin getPlugin(Enum<?> enumValue)
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
    public <T> Set<T> getEnumValues(Plugin plugin, Class<T> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> s = this.enumsByPlugin.get(plugin);
            return s == null ? Collections.emptySet() : s.stream().filter(o -> clazz.isInstance(o)).map(o -> clazz.cast(o)).collect(Collectors.toSet());
        }
    }

    @Override
    public <T> Set<T> getEnumValues(Class<T> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<T> result = new HashSet<>();
            this.enumsByPlugin.values().forEach(s -> s.stream().filter(e -> clazz.isInstance(e)).map(e -> clazz.cast(e)).forEach(result::add));
            return result;
        }
    }
    
}
