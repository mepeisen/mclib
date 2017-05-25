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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;

/**
 * The message service impl.
 * 
 * @author mepeisen
 */
class MessageServiceImpl implements MessageServiceInterface
{
    
    /** enum service helper. */
    private final EnumServiceImpl                      enumService;
    
    /**
     * messages configuration per plugin.
     */
    private final Map<Plugin, MessagesConfigInterface> messages             = new HashMap<>();
    
    /** placeholders. */
    private final Map<String, List<Placeholder>>       placeholders         = new HashMap<>();
    
    /** placeholder pattern. */
    private static final Pattern                       PLACEHOLDER_PATTERN  = Pattern.compile("[{]([\\p{L}\\p{Alnum}_-]+)[}]"); //$NON-NLS-1$
    
    /** placeholders by plugin. */
    private final Map<String, Set<PlaceholderInfo>>    placeholdersByPlugin = new HashMap<>();
    
    /**
     * placeholder info.
     */
    private static final class PlaceholderInfo
    {
        /**
         * Constructor.
         */
        public PlaceholderInfo()
        {
            // empty
        }
        
        /** the prefix. */
        public String      prefix;
        /** the placeholder. */
        public Placeholder placeholder;
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.placeholder == null) ? 0 : this.placeholder.hashCode());
            result = prime * result + ((this.prefix == null) ? 0 : this.prefix.hashCode());
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
            PlaceholderInfo other = (PlaceholderInfo) obj;
            if (this.placeholder == null)
            {
                if (other.placeholder != null)
                {
                    return false;
                }
            }
            else if (!this.placeholder.equals(other.placeholder))
            {
                return false;
            }
            if (this.prefix == null)
            {
                if (other.prefix != null)
                {
                    return false;
                }
            }
            else if (!this.prefix.equals(other.prefix))
            {
                return false;
            }
            return true;
        }
    }
    
    /**
     * Constructor.
     * 
     * @param enumService
     *            the enum service
     */
    public MessageServiceImpl(EnumServiceImpl enumService)
    {
        this.enumService = enumService;
    }
    
    @Override
    public MessagesConfigInterface getMessagesFromMsg(LocalizedMessageInterface item)
    {
        final Plugin plugin = this.enumService.getPlugin(item);
        if (plugin == null)
        {
            return null;
        }
        return this.messages.computeIfAbsent(plugin, (key) -> new MessagesConfigImpl(plugin, this.enumService));
    }
    
    @Override
    public String replacePlaceholders(Locale locale, String msg)
    {
        final StringBuilder target = new StringBuilder();
        int index = 0;
        
        final Matcher m = PLACEHOLDER_PATTERN.matcher(msg);
        while (m.find())
        {
            final String format = m.group(1);
            if (index < m.start())
            {
                target.append(msg, index, m.start());
            }
            int indexof = format.indexOf('_');
            final String prefix = indexof == -1 ? format : format.substring(0, indexof);
            final String[] args = format.split("_"); //$NON-NLS-1$
            index = m.end();
            boolean found = false;
            final List<Placeholder> list = this.placeholders.get(prefix);
            if (list != null)
            {
                for (final Placeholder func : list)
                {
                    final String res = func.resolve(locale, args);
                    if (res != null)
                    {
                        target.append(res);
                        found = true;
                        break;
                    }
                }
            }
            if (!found)
            {
                target.append("{" + format + "}"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        if (index < msg.length())
        {
            target.append(msg, index, msg.length());
        }
        return target.toString();
    }
    
    @Override
    public void registerPlaceholders(Plugin plugin, String prefix, Placeholder placeholder)
    {
        this.placeholders.computeIfAbsent(prefix, p -> new ArrayList<>()).add(placeholder);
        final PlaceholderInfo info = new PlaceholderInfo();
        info.prefix = prefix;
        info.placeholder = placeholder;
        this.placeholdersByPlugin.computeIfAbsent(plugin.getName(), p -> new HashSet<>()).add(info);
    }
    
    @Override
    public void unregisterPlaceholders(Plugin plugin, String prefix, Placeholder placeholder)
    {
        final Set<PlaceholderInfo> set = this.placeholdersByPlugin.get(plugin.getName());
        if (set != null)
        {
            final PlaceholderInfo info = new PlaceholderInfo();
            info.prefix = prefix;
            info.placeholder = placeholder;
            set.remove(info);
        }
        final List<Placeholder> list = this.placeholders.get(prefix);
        if (list != null)
        {
            list.remove(placeholder);
        }
    }
    
    @Override
    public void unregisterPlaceholders(Plugin plugin)
    {
        final Set<PlaceholderInfo> set = this.placeholdersByPlugin.remove(plugin.getName());
        if (set != null)
        {
            for (final PlaceholderInfo info : set)
            {
                this.placeholders.get(info.prefix).remove(info.placeholder);
            }
        }
    }
    
}
