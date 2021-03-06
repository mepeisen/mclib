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

package de.minigameslib.mclib.api.enums;

import java.util.Set;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.shared.api.com.EnumerationValue;
import de.minigameslib.mclib.shared.api.com.UniqueEnumerationValue;

/**
 * A service to register enumerations with plugins.
 * 
 * @author mepeisen
 */
public interface EnumServiceInterface
{
    
    /**
     * Returns the enumeration services instance.
     * 
     * @return enumeration services instance.
     */
    static EnumServiceInterface instance()
    {
        return EnumServiceCache.get();
    }
    
    /**
     * Registers enumeration classes.
     * 
     * @param <T>
     *            enumeration class
     * @param plugin
     *            plugin that registers enum classes.
     * @param clazz
     *            the class to register.
     */
    <T extends Enum<?> & EnumerationValue> void registerEnumClass(Plugin plugin, Class<T> clazz);
    
    /**
     * Unregisters all enumeration classes of given plugin.
     * 
     * <p>
     * Optional method; will be called automatically on each disabled plugin.
     * </p>
     * 
     * @param plugin
     *            plugin that is disabled.
     */
    void unregisterAllEnumerations(Plugin plugin);
    
    /**
     * Returns the plugin that registered given enumeration value.
     * 
     * @param enumValue
     *            enumeration value to search for
     * @return plugin instance or {@code null} if it was not found.
     */
    Plugin getPlugin(EnumerationValue enumValue);
    
    /**
     * Returns the enumeration values implementing given interface class.
     * 
     * @param clazz
     *            class/interface for filtering the enum values
     * @param <T>
     *            class param
     * @return enumeration values
     */
    <T extends EnumerationValue> Set<T> getEnumValues(Class<T> clazz);
    
    /**
     * Returns the enumeration values of given plugin implementing given interface class.
     * 
     * @param plugin
     *            the plugin whose enum values are returned
     * @param clazz
     *            class/interface for filtering the enum values
     * @param <T>
     *            class param
     * @return enumeration values
     */
    <T extends EnumerationValue> Set<T> getEnumValues(Plugin plugin, Class<T> clazz);
    
    /**
     * Returns the enumeration values of given plugin.
     * 
     * @param plugin
     *            the plugin whose enum values are returned
     * @return all registered enumeration values.
     */
    Set<Enum<?>> getEnumValues(Plugin plugin);
    
    /**
     * Returns the enumeration value implementing given interface class from given plugin and with given name.
     * 
     * @param clazz
     *            class/interface for filtering the enum values
     * @param <T>
     *            class param
     * @param plugin
     *            plugin name
     * @param name
     *            enumeration value name
     * @return enumeration value or {@code null} if the enum is not registered
     */
    <T extends UniqueEnumerationValue> T getEnumValue(Class<T> clazz, String plugin, String name);
    
    /**
     * Registers a listener to watch for changes in enumerations
     * 
     * @param <T>
     *            enumeration class
     * @param plugin
     *            the plugin registering the listener
     * @param clazz
     *            enumeration class
     * @param listener
     *            enumeration listener
     */
    <T extends EnumerationValue> void registerEnumerationListener(Plugin plugin, Class<T> clazz, EnumerationListener<T> listener);
    
}
