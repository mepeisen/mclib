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

package de.minigameslib.mclib.api.preg;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.plugin.Plugin;

/**
 * A registry holding plugin objects by specific keys.
 * 
 * @author mepeisen
 * @param <K>
 *            key class; should implement equals and hashcode
 * @param <V>
 *            value class; should implement equals and hashcode
 */
public class PluginObjectRegistry<K, V>
{
    
    /** registered extensions. */
    private final Map<K, Set<V>>               registry = new HashMap<>();
    
    /** registered extensions by plugin. */
    private final Map<String, Set<Info<K, V>>> byPlugin = new HashMap<>();
    
    /**
     * Register new object.
     * 
     * @param plugin
     *            plugin owning the object.
     * @param key
     *            key
     * @param value
     *            value
     */
    public void register(Plugin plugin, K key, V value)
    {
        this.registry.computeIfAbsent(key, k -> new HashSet<>()).add(value);
        this.byPlugin.computeIfAbsent(plugin.getName(), n -> new HashSet<>()).add(new Info<>(key, value));
    }
    
    /**
     * unregister single object.
     * 
     * @param plugin
     *            plugin owning the object.
     * @param key
     *            key
     * @param value
     *            value
     */
    public void unregister(Plugin plugin, K key, V value)
    {
        final Set<V> values = this.registry.get(key);
        if (values != null)
        {
            values.remove(value);
            if (values.isEmpty())
            {
                this.registry.remove(key);
            }
        }
        final Set<Info<K, V>> infos = this.byPlugin.get(plugin.getName());
        if (infos != null)
        {
            infos.remove(new Info<>(key, value));
            if (infos.isEmpty())
            {
                this.byPlugin.remove(plugin.getName());
            }
        }
    }
    
    /**
     * Unregister all by plugin.
     * 
     * @param plugin
     *            plugin owning the object
     */
    public void unregisterAll(Plugin plugin)
    {
        
        final Set<Info<K, V>> infos = this.byPlugin.remove(plugin.getName());
        if (infos != null)
        {
            for (final Info<K, V> info : infos)
            {
                final Set<V> values = this.registry.get(info.getKey());
                if (values != null)
                {
                    values.remove(info.getValue());
                    if (values.isEmpty())
                    {
                        this.registry.remove(info.getKey());
                    }
                }
            }
        }
    }
    
    /**
     * Returns all elements for given key.
     * 
     * @param key
     *            the key
     * @return elements by key
     */
    public Set<V> get(K key)
    {
        Set<V> result = this.registry.get(key);
        if (result == null)
        {
            result = Collections.emptySet();
        }
        return result;
    }
    
    /**
     * info for registered object.
     * 
     * @author mepeisen
     *
     * @param <K>
     *            key class; should implement equals and hashcode
     * @param <V>
     *            value class; should implement equals and hashcode
     */
    private static final class Info<K, V>
    {
        /** key. */
        private final K key;
        /** value. */
        private final V value;
        
        /**
         * Constructor.
         * 
         * @param key
         *            the key
         * @param value
         *            the value
         */
        public Info(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
            result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            Info<?, ?> other = (Info<?, ?>) obj;
            if (this.key == null)
            {
                if (other.key != null)
                {
                    return false;
                }
            }
            else if (!this.key.equals(other.key))
            {
                return false;
            }
            if (this.value == null)
            {
                if (other.value != null)
                {
                    return false;
                }
            }
            else if (!this.value.equals(other.value))
            {
                return false;
            }
            return true;
        }
        
        /**
         * Returns the key.
         * 
         * @return the key
         */
        public K getKey()
        {
            return this.key;
        }
        
        /**
         * Returns the value.
         * 
         * @return the value
         */
        public V getValue()
        {
            return this.value;
        }
    }
    
}
