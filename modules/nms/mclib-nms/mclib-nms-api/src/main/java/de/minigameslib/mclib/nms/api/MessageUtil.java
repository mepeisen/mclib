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

package de.minigameslib.mclib.nms.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;

/**
 * A simple class for converting Serializable to localized messages.
 * 
 * @author mepeisen
 *
 */
public class MessageUtil
{
    
    /**
     * Converts serializable to single string using given player.
     * 
     * @param player
     *            player for localization
     * @param string
     *            serializable to convert
     * @return result string
     */
    public static String convert(Player player, Serializable string)
    {
        if (string instanceof LocalizedMessageInterface.DynamicArg)
        {
            final McPlayerInterface mcplayer = ObjectServiceInterface.instance().getPlayer(player);
            return ((LocalizedMessageInterface.DynamicArg) string).apply(mcplayer.getPreferredLocale(), player.isOp());
        }
        
        if (string instanceof LocalizedMessageInterface.DynamicListArg)
        {
            final McPlayerInterface mcplayer = ObjectServiceInterface.instance().getPlayer(player);
            return String.join("\n", ((LocalizedMessageInterface.DynamicListArg) string).apply(mcplayer.getPreferredLocale(), player.isOp())); //$NON-NLS-1$
        }
        
        if (string instanceof LocalizedMessageInterface)
        {
            final LocalizedMessageInterface lmi = (LocalizedMessageInterface) string;
            if (lmi.isSingleLine())
            {
                return ObjectServiceInterface.instance().getPlayer(player).encodeMessage(lmi)[0];
            }
            return String.join("\n", ObjectServiceInterface.instance().getPlayer(player).encodeMessage(lmi)); //$NON-NLS-1$
        }
        
        return string.toString();
    }
    
    /**
     * Converts serializable to string list using given player.
     * 
     * @param player
     *            player for localization
     * @param strings
     *            serializable to convert
     * @return result string
     */
    public static String[] convert(Player player, Serializable[] strings)
    {
        final List<String> result = new ArrayList<>();
        for (final Serializable string : strings)
        {
            if (string instanceof LocalizedMessageInterface.DynamicArg)
            {
                final McPlayerInterface mcplayer = ObjectServiceInterface.instance().getPlayer(player);
                result.add(((LocalizedMessageInterface.DynamicArg) string).apply(mcplayer.getPreferredLocale(), player.isOp()));
            }
            else if (string instanceof LocalizedMessageInterface.DynamicListArg)
            {
                final McPlayerInterface mcplayer = ObjectServiceInterface.instance().getPlayer(player);
                result.addAll(Arrays.asList(((LocalizedMessageInterface.DynamicListArg) string).apply(mcplayer.getPreferredLocale(), player.isOp())));
            }
            else if (string instanceof LocalizedMessageInterface)
            {
                final LocalizedMessageInterface lmi = (LocalizedMessageInterface) string;
                result.addAll(Arrays.asList(ObjectServiceInterface.instance().getPlayer(player).encodeMessage(lmi)));
            }
            else
            {
                result.addAll(Arrays.asList(string.toString().split("\n"))); //$NON-NLS-1$
            }
        }
        
        return result.toArray(new String[result.size()]);
    }
    
}
