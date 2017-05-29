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

import de.minigameslib.mclib3p.impl.Mclib3pPlugin;
import me.clip.placeholderapi.external.EZPlaceholderHook;

/**
 * Placeholders from mclib, injected to placeholder api.
 * 
 * @author mepeisen
 *
 */
public class MclibPlaceholders extends EZPlaceholderHook
{

    /**
     * Constructor.
     */
    public MclibPlaceholders()
    {
        super(Mclib3pPlugin.INSTANCE, "mclib"); //$NON-NLS-1$
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
