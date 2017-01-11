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

package de.minigameslib.mclib;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.locale.Messages;

/**
 * Showcase plugin class.
 * @author mepeisen
 */
public class MclibShowcasePlugin extends JavaPlugin implements Listener
{

    /**
     * Constructor.
     */
    public MclibShowcasePlugin()
    {
        // empty
    }

    @Override
    public void onEnable()
    {
        EnumServiceInterface.instance().registerEnumClass(this, Messages.class);
    }

    @Override
    public void onDisable()
    {
        EnumServiceInterface.instance().unregisterAllEnumerations(this);
    }
    
    /**
     * User online event to set display message.
     * @param evt
     */
    @EventHandler
    public void onUserOnline(PlayerJoinEvent evt)
    {
    	final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer(evt.getPlayer());
    	evt.setJoinMessage(player.encodeMessage(Messages.WelcomeMessage)[0]);
    }

}

