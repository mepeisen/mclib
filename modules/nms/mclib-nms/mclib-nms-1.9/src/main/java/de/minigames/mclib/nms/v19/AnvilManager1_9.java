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

package de.minigames.mclib.nms.v19;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.nms.api.AnvilManagerInterface;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.ContainerAnvil;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutOpenWindow;

/**
 * Anvil gui manager.
 * 
 * @author mepeisen
 */
public class AnvilManager1_9 implements AnvilManagerInterface
{
    
    /**
     * inventory helpers by uuid.
     */
    private final Map<UUID, Helper> playerInventories = new HashMap<>();
    
    @Override
    public AnvilHelper openGui(Player player, ItemStack stack, AnvilListener listener)
    {
        final EntityPlayer entity = ((CraftPlayer) player).getHandle();
        final Helper helper = new Helper(entity, stack, listener);
        int c = entity.nextContainerCounter();
        entity.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("FOO", new Object[]{}), 0));
        entity.activeContainer = helper;
        helper.windowId = c;
        entity.activeContainer.addSlotListener(entity);
        this.playerInventories.put(player.getUniqueId(), helper);
        helper.getBukkitView().getTopInventory().setItem(0, stack);
        return helper;
    }
    
    /**
     * Player click event
     * 
     * @param evt
     *            player click event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt)
    {
        final UUID uuid = evt.getPlayer().getUniqueId();
        if (this.playerInventories.containsKey(uuid))
        {
            this.playerInventories.remove(uuid).close();
        }
    }
    
    /**
     * Inventory click event
     * 
     * @param evt
     *            inventory click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt)
    {
        final UUID uuid = evt.getWhoClicked().getUniqueId();
        if (this.playerInventories.containsKey(uuid))
        {
            evt.setCancelled(true);
            if (evt.getRawSlot() == 2)
            {
                final ItemStack stack = evt.getCurrentItem();
                String name = ""; //$NON-NLS-1$
                if (stack.hasItemMeta())
                {
                    final ItemMeta meta = stack.getItemMeta();
                    if (meta.hasDisplayName())
                    {
                        name = meta.getDisplayName();
                    }
                }
                
                if (this.playerInventories.get(uuid).listener.onCommit(name))
                {
                    evt.getWhoClicked().closeInventory();
                    this.playerInventories.remove(uuid);
                }
            }
        }
    }
    
    /**
     * Inventory drag event
     * 
     * @param evt
     *            inventory drag event
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent evt)
    {
        final UUID uuid = evt.getWhoClicked().getUniqueId();
        if (this.playerInventories.containsKey(uuid))
        {
            evt.setCancelled(true);
        }
    }
    
    /**
     * Inventory close event
     * 
     * @param evt
     *            inventory close event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt)
    {
        final UUID uuid = evt.getPlayer().getUniqueId();
        if (this.playerInventories.containsKey(uuid))
        {
            this.playerInventories.remove(uuid).listener.onClose();
        }
    }
    
    /**
     * Helper class for inventories.
     */
    private static final class Helper extends ContainerAnvil implements AnvilHelper
    {
        
        /** bukkit item stack. */
        private ItemStack    stack;
        /** craft entity. */
        private EntityPlayer entity;
        /** the inventory listener. */
        AnvilListener        listener;
        
        /**
         * @param entity
         * @param stack
         * @param listener
         */
        public Helper(EntityPlayer entity, ItemStack stack, AnvilListener listener)
        {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
            this.stack = stack;
            this.entity = entity;
            this.listener = listener;
        }
        
        @Override
        public boolean a(EntityHuman entityhuman)
        {
            // reachable
            return true;
        }


        @Override
        public void close()
        {
            CraftEventFactory.handleInventoryCloseEvent(this.entity);
        }
        
    }
    
}
