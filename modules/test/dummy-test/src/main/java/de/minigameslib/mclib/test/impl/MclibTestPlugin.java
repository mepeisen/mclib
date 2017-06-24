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
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.schem.SchemataBuilderInterface;
import de.minigameslib.mclib.api.schem.SchemataServiceInterface;

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
        EnumServiceInterface.instance().registerEnumClass(this, GuiIds2.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyMessages.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyEntitites.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyBlocks.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyHolograms.class);
        
        try
        {
            ObjectServiceInterface.instance().register(MyEntitites.Dummy, DummyEntity.class);
            ObjectServiceInterface.instance().register(MyHolograms.Dummy, DummyHologram.class);
            ObjectServiceInterface.instance().resumeObjects(this, null);
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
            final ItemServiceInterface itemService = ItemServiceInterface.instance();
            final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer((Player) sender);
            itemService.prepareTool(CommonItems.App_Pinion, player, MyMessages.Lower)
                .onLeftClick(this::onLower)
                .onRightClick(this::onLower)
                .singleUse()
                .build();
        }
        return super.onCommand(sender, command, label, args);
    }
    
    private void onLower(McPlayerInterface player, McPlayerInteractEvent evt)
    {
        evt.getBukkitEvent().setCancelled(true);
        final Location loc = evt.getBukkitEvent().getClickedBlock().getLocation();
        
        ItemServiceInterface.instance().prepareTool(CommonItems.App_Pinion, player, MyMessages.Higher)
            .onLeftClick((p, e) -> this.onHigher(p, e, loc))
            .onRightClick((p, e) -> this.onHigher(p, e, loc))
            .singleUse()
            .build();
    }
    
    private void onHigher(McPlayerInterface player, McPlayerInteractEvent evt, Location low)
    {
        evt.getBukkitEvent().setCancelled(true);
        final Location high = evt.getBukkitEvent().getClickedBlock().getLocation();
        
        final SchemataBuilderInterface schema = SchemataServiceInterface.instance().builder();
        schema.addPart("DUMMY", new Cuboid(low, high), () -> {
            ItemServiceInterface.instance().prepareTool(CommonItems.App_Pinion, player, MyMessages.NewLoc)
            .onLeftClick((p, e) -> this.onPaste(p, e, schema))
            .onRightClick((p, e) -> this.onPaste(p, e, schema))
            .singleUse()
            .build();
        }, null);
    }
    
    private void onPaste(McPlayerInterface player, McPlayerInteractEvent evt, SchemataBuilderInterface builder) throws McException
    {
        final Location loc = evt.getBukkitEvent().getClickedBlock().getLocation();
        builder.applyToWorld(new Location[]{loc}, null, null);
    }
    
    @EventHandler
    public void onConnect(PlayerJoinEvent evt)
    {
        if (!ObjectServiceInterface.instance().isHuman(evt.getPlayer()))
        {
            evt.getPlayer().setOp(true);
            evt.getPlayer().setGameMode(GameMode.CREATIVE);
        }
    }
    
}
