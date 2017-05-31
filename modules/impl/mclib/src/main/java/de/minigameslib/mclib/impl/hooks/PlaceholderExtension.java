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

package de.minigameslib.mclib.impl.hooks;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.ext.ExtensionInterface;
import de.minigameslib.mclib.api.ext.ExtensionPointInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface.Placeholder;

/**
 * Extension for placeholders.
 * 
 * @author mepeisen
 *
 */
public interface PlaceholderExtension extends ExtensionInterface
{
    
    /**
     * the extension point.
     */
    ExtensionPointInterface<PlaceholderExtension> POINT = new ExtensionPointInterface<PlaceholderExtension>() {
        
        @Override
        public String name()
        {
            return "mclib/placeholder"; //$NON-NLS-1$
        }
        
        @Override
        public Class<PlaceholderExtension> extensionClass()
        {
            return PlaceholderExtension.class;
        }
    };
    
    /**
     * Notify about registering a placeholder.
     * 
     * <p>
     * The prefix string is the string starting befroe first underscore. For example the placeholder "{foo_bar}" leads to prefix "foo".
     * </p>
     * 
     * <p>
     * The placeholder function is reposible for converting a placeholder to result string. It may return {@code null} if it does not know how to convert a string. String string array passed to the
     * function is the placeholder splitted by underscore.
     * </p>
     * 
     * @param plugin
     *            plugin owning the placeholder.
     * @param prefix
     *            a string prefix.
     * @param placeholder
     *            placeholder function.
     * 
     * @see MessageServiceInterface#registerPlaceholders(Plugin, String, Placeholder)
     */
    void registerPlaceholders(Plugin plugin, String prefix, Placeholder placeholder);
    
    /**
     * Notifies about unregistering a placeholder witrh given prefix.
     * 
     * @param plugin
     *            plugin owning the placeholder.
     * @param prefix
     *            a string prefix.
     * @param placeholder
     *            placeholder function.
     * 
     * @see MessageServiceInterface#unregisterPlaceholders(Plugin, String, Placeholder)
     * @see MessageServiceInterface#unregisterPlaceholders(Plugin)
     */
    void unregisterPlaceholders(Plugin plugin, String prefix, Placeholder placeholder);
    
    /**
     * Notifies about changes on placeholders.
     * 
     * @param placeholders
     *            the list of changed placeholders
     * @see MessageServiceInterface#notifyPlaceholderChanges(String[][])
     */
    void notifyPlaceholderChanges(String[][] placeholders);
    
    /**
     * Replace the placeholder.
     * 
     * @param placeholder
     *            complete placeholder string
     * @param prefix
     *            prefix (everything before first '_' character)
     * @param args
     *            the arguments (characters beyond prefix and devided by '_').
     * @return resulting string or {code null} if placeholder was not found
     */
    String replacePlaceholder(String placeholder, String prefix, String[] args);
    
}
