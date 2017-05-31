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

package de.minigameslib.mclib3p.impl.placeholder;

import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib3p.impl.Mclib3pPlugin;
import me.clip.placeholderapi.external.EZPlaceholderHook;

/**
 * Placeholders from mclib, injected to placeholder api.
 * 
 * @author mepeisen
 *
 */
public class MclibPlaceholder extends EZPlaceholderHook
{
    
    /**
     * Constructor.
     * 
     * @param prefix
     *            the prefix to be used.
     */
    public MclibPlaceholder(String prefix)
    {
        super(Mclib3pPlugin.INSTANCE, prefix);
        this.hook();
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        try
        {
            return McLibInterface.instance().calculateInCopiedContext(() -> {
                final McPlayerInterface mcplayer = ObjectServiceInterface.instance().getPlayer(player);
                McLibInterface.instance().setContext(McPlayerInterface.class, mcplayer);
                return MessageServiceInterface.instance().replacePlaceholders(
                    mcplayer.getPreferredLocale(),
                    "{" + this.getPlaceholderName() + "_" + identifier + "}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            });
        }
        catch (McException e)
        {
            // TODO logging
            return null;
        }
    }
    
}
