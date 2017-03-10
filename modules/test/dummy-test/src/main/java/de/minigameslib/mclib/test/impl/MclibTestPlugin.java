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

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;

/**
 * @author mepeisen
 *
 */
public class MclibTestPlugin extends JavaPlugin implements Listener
{

    @Override
    public void onEnable()
    {
        EnumServiceInterface.instance().registerEnumClass(this, GuiIds.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyMessages.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyEntitites.class);
        
        try
        {
            ObjectServiceInterface.instance().register(MyEntitites.Dummy, DummyEntity.class);
            ObjectServiceInterface.instance().resumeObjects(this);
        }
        catch (McException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Bukkit.getPluginManager().registerEvents(this, this);
        
        try
        {
//            System.out.println("Registering material #500");
//            final Material[] byId = getStaticField(Material.class, "byId");
//            byId[500] = Material.STONE;
//            final Map<String, Material> byName = getStaticField(Material.class, "BY_NAME");
//            byName.put("custom_stone", Material.STONE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private <T> T getStaticField(Class<?> clazz, String field)
    {
        try
        {
            final Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return (T) f.get(clazz);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
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
            final ItemServiceInterface itemService = ItemServiceInterface.instance();
            final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer((Player) sender);
            itemService.prepareTool(CommonItems.App_Pinion, player, MyMessages.Title)
                .onLeftClick(this::onLeftClick)
                .onRightClick(this::onRightClick)
                .singleUse()
                .build();
        }
        return super.onCommand(sender, command, label, args);
    }
    
    private void onLeftClick(McPlayerInterface player, McPlayerInteractEvent evt)
    {
        System.out.println("Left clicked at " + evt.getBukkitEvent().getClickedBlock());
    }
    
    private void onRightClick(McPlayerInterface player, McPlayerInteractEvent evt)
    {
        System.out.println("Right clicked at " + evt.getBukkitEvent().getClickedBlock());
    }
    
    @EventHandler
    public void onConnect(PlayerJoinEvent evt)
    {
        if (!ObjectServiceInterface.instance().isHuman(evt.getPlayer()))
            evt.getPlayer().setGameMode(GameMode.CREATIVE);
    }
    
}
