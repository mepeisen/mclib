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
import org.bukkit.block.Block;
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
import de.minigameslib.mclib.api.items.BlockData;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
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
        EnumServiceInterface.instance().registerEnumClass(this, GuiIds2.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyMessages.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyEntitites.class);
        EnumServiceInterface.instance().registerEnumClass(this, MyBlocks.class);
        
        try
        {
            ObjectServiceInterface.instance().register(MyEntitites.Dummy, DummyEntity.class);
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
            boolean flg = false;
            if (flg)
            {
                final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer((Player) sender);
                try
                {
                    player.openClickGui(new ClickGui());
                }
                catch (Exception ex)
                {
                    // TODO
                }
            }
            else
            {
                final ItemServiceInterface itemService = ItemServiceInterface.instance();
                final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer((Player) sender);
                itemService.prepareTool(CommonItems.App_Pinion, player, MyMessages.Title)
                    .onLeftClick(this::onLeftClick)
                    .onRightClick(this::onRightClick)
                    //.singleUse()
                    .build();
            }
        }
        return super.onCommand(sender, command, label, args);
    }
    
    private void onLeftClick(McPlayerInterface player, McPlayerInteractEvent evt)
    {
        evt.getBukkitEvent().setCancelled(true);
        final Block clickedBlock = evt.getBukkitEvent().getClickedBlock();
//        BlockServiceInterface.instance().setBlockData(clickedBlock, MyBlocks.CopperOre, BlockData.CustomVariantId.DEFAULT);
//        final BlockPosition position = new BlockPosition(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
//        ((CraftWorld)clickedBlock.getWorld()).getHandle().setTypeAndData(position, Blocks.AIR.getBlockData(), 0);
//        final IBlockData blockData = net.minecraft.server.v1_10_R1.Block.getById(3000).getBlockData();
//        // final IBlockData blockData = Blocks.FURNACE.getBlockData();
//        IBlockData old = ((CraftChunk)clickedBlock.getChunk()).getHandle().getBlockData(position);
//        boolean success = ((CraftChunk)clickedBlock.getChunk()).getHandle().getWorld().setTypeAndData(position, blockData, 2);
//        if (success) {
//            ((CraftChunk)clickedBlock.getChunk()).getHandle().getWorld().notify(position, old, blockData, 3);
//        }

//        System.out.println(blockData);
//        System.out.println(old);
//
//        System.out.println(((CraftWorld)clickedBlock.getWorld()).getHandle().getType(position));
        
        System.out.println("Left clicked at " + clickedBlock);
        
        ItemServiceInterface.instance().prepareTool(CommonItems.App_Pinion, player, MyMessages.Title)
            .onLeftClick(this::onLeftClick2)
            .onRightClick(this::onRightClick2)
            .singleUse()
            .build();
    }
    
    private void onLeftClick2(McPlayerInterface player, McPlayerInteractEvent evt)
    {
        evt.getBukkitEvent().setCancelled(true);
        final Block clickedBlock = evt.getBukkitEvent().getClickedBlock();
        System.out.println("Left (2) clicked at " + clickedBlock);
    }
    
    private void onRightClick(McPlayerInterface player, McPlayerInteractEvent evt)
    {
        evt.getBukkitEvent().setCancelled(true);
        final Block clickedBlock = evt.getBukkitEvent().getClickedBlock();
        System.out.println("Right clicked at " + evt.getBukkitEvent().getClickedBlock());
    }
    
    private void onRightClick2(McPlayerInterface player, McPlayerInteractEvent evt)
    {
        evt.getBukkitEvent().setCancelled(true);
        final Block clickedBlock = evt.getBukkitEvent().getClickedBlock();
        System.out.println("Right (2) clicked at " + evt.getBukkitEvent().getClickedBlock());
    }
    
    @EventHandler
    public void onConnect(PlayerJoinEvent evt)
    {
        if (!ObjectServiceInterface.instance().isHuman(evt.getPlayer()))
            evt.getPlayer().setGameMode(GameMode.CREATIVE);
    }
    
}
