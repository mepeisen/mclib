/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General License for more details.

    You should have received a copy of the GNU General License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mclib.api.gui;

import java.io.Serializable;
import java.time.LocalDateTime;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * A raw chat message.
 * 
 * @author mepeisen
 */
public interface RawMessageInterface
{
    
    /**
     * Adds a localized and preformatted message.
     * @param message
     * @param args
     * @return this object for chaining.
     */
    RawMessageInterface addMsg(LocalizedMessageInterface message, Serializable... args);

    /**
     * Adds a localized and preformatted message including a hover
     * @param message
     * @param messageArgs
     * @param hover
     * @param hoverArgs
     * @return this object for chaining.
     */
    RawMessageInterface addHover(LocalizedMessageInterface message, Serializable[] messageArgs, LocalizedMessageInterface hover, Serializable... hoverArgs);
    
    /**
     * Adds a clickable hover
     * @param message
     * @param messageArgs
     * @param hover
     * @param hoverArgs
     * @param command
     * @param execute {@code true} to execute the command on click; {@code false} to print the command in chat
     * @return this object for chaining.
     */
    RawMessageInterface addClickableHover(LocalizedMessageInterface message, Serializable[] messageArgs, LocalizedMessageInterface hover, Serializable[] hoverArgs, String command, boolean execute);
    
    /**
     * Adds a clickable hover
     * @param message
     * @param messageArgs
     * @param hover
     * @param hoverArgs
     * @param handler
     * @param expires the date/time the click link expires
     * @return this object for chaining.
     */
    RawMessageInterface addClickableHover(LocalizedMessageInterface message, Serializable[] messageArgs, LocalizedMessageInterface hover, Serializable[] hoverArgs, McRunnable handler, LocalDateTime expires);
    
    /**
     * Adds clickable text
     * @param message
     * @param messageArgs
     * @param command
     * @param execute {@code true} to execute the command on click; {@code false} to print the command in chat
     * @return this object for chaining.
     */
    RawMessageInterface addCommand(LocalizedMessageInterface message, Serializable[] messageArgs, String command, boolean execute);
    
    /**
     * Adds clickable text
     * @param message
     * @param messageArgs
     * @param handler
     * @param expires the date/time the click link expires
     * @return this object for chaining.
     */
    RawMessageInterface addHandler(LocalizedMessageInterface message, Serializable[] messageArgs, McRunnable handler, LocalDateTime expires);
    
    /**
     * Adds a clickable url to open with browser
     * @param message
     * @param messageArgs
     * @param url
     * @return this object for chaining.
     */
    RawMessageInterface addUrl(LocalizedMessageInterface message, Serializable[] messageArgs, String url);
    
    /**
     * Adds a raw json text (be careful with this method)
     * @param rawJson
     * @return this object for chaining.
     */
    RawMessageInterface add(String rawJson);
    
    // TODO addBookPageLink, see http://minecraft-de.gamepedia.com/JSON-Text
    // TODO scoreboardLink, see http://minecraft-de.gamepedia.com/JSON-Text
    
}
