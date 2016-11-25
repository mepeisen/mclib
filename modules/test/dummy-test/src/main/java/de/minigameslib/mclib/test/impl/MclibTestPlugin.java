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

package de.minigameslib.mclib.test.impl;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McContext;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.config.ConfigServiceInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.gui.SGuiFormBuilderInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;

/**
 * @author mepeisen
 *
 */
public class MclibTestPlugin extends JavaPlugin implements PluginMessageListener, Listener
{

    @Override
    public void onEnable()
    {
        EnumServiceInterface.instance().registerEnumClass(this, GuiIds.class); // test
        
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "mclib-channel");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "mclib-channel", this);
        
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable()
    {
        EnumServiceInterface.instance().unregisterAllEnumerations(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equals("mclibt")) //$NON-NLS-1$
        {
            final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer((Player) sender);
            try
            {
                ConfigServiceInterface.instance().runInNewContext(() -> {
                    ConfigServiceInterface.instance().setContext(McPlayerInterface.class, player);
                    
                    final SGuiFormBuilderInterface form = player.openSmartGui().sguiForm(
                            CommonMessages.HelpShortDescription, null,
                            false,
                            null);
                    form.addText(2, CommonMessages.HelpShortDescription)
                        .addTextInput(CommonMessages.HelpShortDescription, null, "foo", "bar", true)
                        .addSubmitButton(CommonMessages.HelpShortDescription, null, data -> System.out.println(data))
                        .addCancelButton(CommonMessages.HelpShortDescription, null, null)
                        .display();
                });
            }
            catch (McException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // MyCommandHandler.onCommand(sender, command, label, args);
        }
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2)
    {
        System.out.println("PONG received.");
    }
    
    @EventHandler
    public void onConnect(PlayerJoinEvent evt)
    {
        evt.getPlayer().setGameMode(GameMode.CREATIVE);
    }
    
}
