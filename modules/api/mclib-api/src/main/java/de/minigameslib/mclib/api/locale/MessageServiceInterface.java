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
import java.util.function.BiFunction;

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
     * The placeholder function is reposible for converting a placeholder to result string. It may return {@code null} if it does not know
     * how to convert a string. String string array passed to the function is the placeholder splitted by underscore.
     * </p>
     * 
     * @param prefix
     *            a string prefix.
     * @param placeholder
     *            placeholder function.
     */
    void registerPlaceholders(String prefix, BiFunction<Locale, String[], String> placeholder);
    
}
