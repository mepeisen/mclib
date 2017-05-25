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

package de.minigameslib.mclib.api.locale;

import java.util.Locale;

import org.bukkit.plugin.Plugin;

/**
 * Message services.
 * 
 * @author mepeisen
 */
public interface MessageServiceInterface
{
    
    /**
     * Returns the message services instance.
     * 
     * @return message services instance.
     */
    static MessageServiceInterface instance()
    {
        return MessageServiceCache.get();
    }
    
    /**
     * Returns the message api declaring the given message.
     * 
     * @param item
     *            the enumeration value; only works on classes that are returned by a plugin or extension provider during initialization.
     * 
     * @return message api or {@code null} if the class was not declared by any minigame or extension.
     */
    MessagesConfigInterface getMessagesFromMsg(LocalizedMessageInterface item);
    
    /**
     * replaces placeholders in given message.
     * 
     * @param locale
     *            chosen locale
     * @param msg
     *            source message.
     * @return message with replaced placeholders.
     */
    String replacePlaceholders(Locale locale, String msg);
    
    /**
     * Registers the placeholder with given prefix.
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
     */
    void registerPlaceholders(Plugin plugin, String prefix, Placeholder placeholder);
    
    /**
     * Unregisters the placeholder with given prefix.
     * 
     * @param plugin
     *            plugin owning the placeholder.
     * @param prefix
     *            a string prefix.
     * @param placeholder
     *            placeholder function.
     */
    void unregisterPlaceholders(Plugin plugin, String prefix, Placeholder placeholder);
    
    /**
     * Unregisters all placeholders for given plugin
     * 
     * @param plugin
     *            plugin owning the placeholder.
     */
    void unregisterPlaceholders(Plugin plugin);
    
    /**
     * Register a placeholder listener watching for changes on given placeholders
     * 
     * @param plugin
     *            plugin owning the placeholders.
     * @param placeholder
     *            placeholders to listen for changes
     * @param listener
     *            the listener to be incoked once the given placeholders changed
     */
    void registerPlaceholderListener(Plugin plugin, String[][] placeholder, PlaceholderListener listener);
    
    /**
     * Unregister given placeholder listener.
     * 
     * @param plugin
     *            plugin owning the listener.
     * @param placeholder
     *            placeholder registrations
     * @param listener
     *            the placeholder listener to remove
     */
    void unregisterPlaceholderListener(Plugin plugin, String[][] placeholder, PlaceholderListener listener);
    
    /**
     * Unregister all listeners for given plugin.
     * 
     * @param plugin
     *            plugin owning the listeners.
     */
    void unregisterPlaceholderListener(Plugin plugin);
    
    /**
     * Returns the placeholders from given message.
     * 
     * @param msg
     *            source message
     * @return array of placeholders within this message
     */
    String[][] getPlaceholders(String msg);
    
    /**
     * Placeholder listeners for fetching changed placeholder values.
     * 
     * @author mepeisen
     */
    @FunctionalInterface
    public interface PlaceholderListener
    {
        
        /**
         * Handles placeholder changes.
         * 
         * @param placeholders
         *            placeholder that were changed; multiple entries. String devided by '_' character.
         */
        void handleChangedPlaceholder(String[][] placeholders);
        
    }
    
    /**
     * Placeholder interface to resolve placeholders during runtime.
     * 
     * @author mepeisen
     */
    @FunctionalInterface
    public interface Placeholder
    {
        
        /**
         * Resolve placeholders.
         * 
         * @param locale
         *            locale to be used; maybe {@code null} for non localized placeholders.
         * @param placeholder
         *            placeholder to resolve. String devided by '_' character.
         * @return result string. {@code null} if the placeholder does not now hoe to resolve this variable.
         */
        String resolve(Locale locale, String[] placeholder);
        
    }
    
}
