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
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.NpcServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.nms.v110.entity.DummyHuman1_10_1;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntity;

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
            final Location loc = ((Player)sender).getLocation().add(0, 3, 0);
            try
            {
                final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer((Player) sender);
                // NpcServiceInterface.instance().villager().location(loc).profession(Profession.FARMER).handler(MyEntitites.Dummy, new DummyEntity()).create();
                // NpcServiceInterface.instance().human().location(loc).persistent().handler(MyEntitites.Dummy, new DummyEntity()).name("JACK").skin(player).create();
                
                final EntityInterface entity = ObjectServiceInterface.instance().findEntities(MyEntitites.Dummy).iterator().next();
                final Entity bukkit = entity.getBukkitEntity();
                final EntityHuman human = (EntityHuman) ((CraftEntity)bukkit).getHandle();
                bukkit.teleport(player.getBukkitPlayer());
//                System.out.println(human.yaw);
//                final byte look = (byte) 32;
//                ((CraftPlayer) player.getBukkitPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(human.getId(), look, (byte) 0, true));
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return super.onCommand(sender, command, label, args);
    }
    
    @EventHandler
    public void onConnect(PlayerJoinEvent evt)
    {
        if (evt.getPlayer().getClass() == CraftPlayer.class)
            evt.getPlayer().setGameMode(GameMode.CREATIVE);
    }
    
}
