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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.nms.api.ChatSystemInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;

/**
 * A raw chat message.
 * 
 * @author mepeisen
 */
public class RawMessage implements RawMessageInterface
{
    
    /**
     * The underlying string builder.
     */
    private List<Function<McPlayerInterface, String>> buffer  = new ArrayList<>();
    
    /**
     * Raw actions.
     */
    protected Map<UUID, RawAction>                    actions = new HashMap<>();
    
    /**
     * Converts this message to raw json text.
     * 
     * @param player
     *            target player for translating localized messages
     * @return json
     */
    public String toJson(McPlayerInterface player)
    {
        return this.buffer.stream().map(f -> f.apply(player)).collect(Collectors.joining(", ", "[ ", " ]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * converts a multi line message to json.
     * 
     * @param encodedMessage
     *            encoded multi line message
     * @return json
     */
    private String toJson(String[] encodedMessage)
    {
        if (encodedMessage.length > 1)
        {
            final String joined = String.join("\n", encodedMessage); //$NON-NLS-1$
            return Bukkit.getServicesManager().load(NmsFactory.class).create(ChatSystemInterface.class).toJson(joined);
        }
        return Bukkit.getServicesManager().load(NmsFactory.class).create(ChatSystemInterface.class).toJson(encodedMessage[0]);
    }
    
    @Override
    public RawMessage addMsg(LocalizedMessageInterface message, Serializable... args)
    {
        this.buffer.add(p -> toJson(p.encodeMessage(message, args)));
        return this;
    }
    
    @Override
    public RawMessageInterface addHover(LocalizedMessageInterface message, Serializable[] messageArgs, LocalizedMessageInterface hover, Serializable... hoverArgs)
    {
        this.buffer.add(p ->
        {
            final String text = this.toJson(p.encodeMessage(message, messageArgs));
            final String hoverText = this.toJson(p.encodeMessage(hover, hoverArgs));
            return "{ \"text\": \"\", \"hoverEvent\":{ \"action\":\"show_text\", \"value\": [ " + hoverText + " ] }, extra: [ " + text + " ] }"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        });
        return this;
    }
    
    @Override
    public RawMessageInterface addCommand(LocalizedMessageInterface message, Serializable[] messageArgs, String command, boolean execute)
    {
        this.buffer.add(p ->
        {
            final String text = this.toJson(p.encodeMessage(message, messageArgs));
            return "{ \"text\": \"\", \"clickEvent\":{ \"action\":\"" + //$NON-NLS-1$
                (execute ? "run_command" : "suggest_command") + //$NON-NLS-1$ //$NON-NLS-2$
                "\", \"value\": [ " + command + " ] }, extra: [ " + text + " ] }"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        });
        return this;
    }
    
    @Override
    public RawMessageInterface addHandler(LocalizedMessageInterface message, Serializable[] messageArgs, McRunnable handler, LocalDateTime expires)
    {
        final UUID uuid = UUID.randomUUID();
        this.actions.put(uuid, new RawAction(expires, handler));
        return this.addCommand(message, messageArgs, "/mclib rawcommand " + uuid.toString(), true); //$NON-NLS-1$
    }
    
    @Override
    public RawMessageInterface addClickableHover(LocalizedMessageInterface message, Serializable[] messageArgs, LocalizedMessageInterface hover, Serializable[] hoverArgs, String command,
        boolean execute)
    {
        this.buffer.add(p ->
        {
            final String text = this.toJson(p.encodeMessage(message, messageArgs));
            final String hoverText = this.toJson(p.encodeMessage(hover, hoverArgs));
            return "{ \"text\": \"\", \"hoverEvent\":{ \"action\":\"show_text\", \"value\": [ " + //$NON-NLS-1$
                hoverText + " ] }, \"clickEvent\":{ \"action\":\"" + (execute ? "run_command" : "suggest_command") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "\", \"value\": [ " + command + " ] }, extra: [ " + text + " ] }"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        });
        return this;
    }
    
    @Override
    public RawMessageInterface addClickableHover(LocalizedMessageInterface message, Serializable[] messageArgs, LocalizedMessageInterface hover, Serializable[] hoverArgs, McRunnable handler,
        LocalDateTime expires)
    {
        final UUID uuid = UUID.randomUUID();
        this.actions.put(uuid, new RawAction(expires, handler));
        return this.addClickableHover(message, messageArgs, hover, hoverArgs, "/mclib rawcommand " + uuid.toString(), true); //$NON-NLS-1$
    }
    
    @Override
    public RawMessageInterface addUrl(LocalizedMessageInterface message, Serializable[] messageArgs, String url)
    {
        this.buffer.add(p ->
        {
            final String text = this.toJson(p.encodeMessage(message, messageArgs));
            return "{ \"text\": \"\", \"clickEvent\":{ \"action\":\"open_url\", \"value\": \"" + url + "\" }, extra: [ " + text + " ] }"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        });
        return this;
    }
    
    @Override
    public RawMessageInterface add(String rawJson)
    {
        this.buffer.add(p -> rawJson);
        return this;
    }
    
    /**
     * A helper class for raw actions.
     * 
     * @author mepeisen
     */
    protected static final class RawAction
    {
        /** expiration timestamp. */
        private final LocalDateTime expires;
        /** the handler code. */
        private final McRunnable    handler;
        
        /**
         * Constructor to create a raw action.
         * 
         * @param expires
         *            date/time the action expires.
         * @param handler
         *            the action handler to invoke.
         */
        public RawAction(LocalDateTime expires, McRunnable handler)
        {
            this.expires = expires;
            this.handler = handler;
        }
        
        /**
         * Returns the expires timestamp.
         * 
         * @return the expires
         */
        public LocalDateTime getExpires()
        {
            return this.expires;
        }
        
        /**
         * Returns the action/click handler.
         * 
         * @return the handler
         */
        public McRunnable getHandler()
        {
            return this.handler;
        }
    }
    
}
