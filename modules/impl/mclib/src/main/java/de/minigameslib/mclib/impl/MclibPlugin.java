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

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigameslib.fakeplayer.impl.FakeFactory;
import de.minigameslib.mclib.fakeclient.IFakeClient;
import de.minigameslib.mclib.fakeclient.IFakeController;

/**
 * Main spigot plugin class for MCLIB.
 * 
 * @author mepeisen
 */
public class MclibPlugin extends JavaPlugin implements Listener, IFakeClient
{

    private IFakeController fakePlayer;

    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(this, this);
        
        this.fakePlayer = FakeFactory.instance().createFakePlayer(this, this, "Fake"); //$NON-NLS-1$
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if ("mclib".equals(command.getName()))
        {
            if (args == null || args.length == 0)
            {
                sender.sendMessage("commands: spawn, say, kill, list, uuid");
                return false;
            }
            switch (args[0])
            {
                case "list":
                    for (final Player player : Bukkit.getServer().getOnlinePlayers())
                    {
                        sender.sendMessage("player: " + player.getName() + " with uuid " + player.getUniqueId());
                    }
                    break;
                case "spawn":
                    if (this.fakePlayer.getPlayer() != null)
                    {
                        sender.sendMessage("already spawned");
                        break;
                    }
                    sender.sendMessage("Fake player spawned");
                    this.fakePlayer.connect(Bukkit.getWorlds().get(0).getSpawnLocation());
                    break;
                case "say":
                    if (this.fakePlayer.getPlayer() == null)
                    {
                        sender.sendMessage("Not spawned");
                        break;
                    }
                    this.fakePlayer.say("fake player says hello");
                    break;
                case "kill":
                    if (this.fakePlayer.getPlayer() == null)
                    {
                        sender.sendMessage("Not spawned");
                        break;
                    }
                    this.fakePlayer.disconnect();
                    break;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent evt)
    {
        evt.setJoinMessage("HUHU");
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.fakeclient.IFakeClient#onConnect()
     */
    @Override
    public void onConnect()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.fakeclient.IFakeClient#onDiscconect()
     */
    @Override
    public void onDiscconect()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.fakeclient.IFakeClient#onChatMessage(java.lang.String, java.lang.String)
     */
    @Override
    public void onChatMessage(String msg)
    {
        // TODO Auto-generated method stub
        
    }
    
}
