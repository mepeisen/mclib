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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
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
    private final Map<Plugin, MessagesConfigInterface> messages = new HashMap<>();
    
    /** placeholders. */
    private final Map<String, List<BiFunction<Locale, String[], String>>> placeholders = new HashMap<>();
    
    /** placeholder pattern. */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[{]([\\p{L}\\p{Alnum}_-]+)[}]"); //$NON-NLS-1$
    
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
            final List<BiFunction<Locale, String[], String>> list = this.placeholders.get(prefix);
            if (list != null)
            {
                for (final BiFunction<Locale, String[], String> func : list)
                {
                    final String res = func.apply(locale, args);
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
    public void registerPlaceholders(String prefix, BiFunction<Locale, String[], String> placeholder)
    {
        this.placeholders.computeIfAbsent(prefix, p -> new ArrayList<>()).add(placeholder);
    }
    
}
