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

package de.minigames.mclib.nms.v194;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import net.minecraft.server.v1_9_R2.EntityPlayer;

/**
 * Inventory manager implementation.
 * 
 * @author mepeisen
 */
public class InventoryManager1_9_4 implements InventoryManagerInterface
{
    
    /**
     * inventory helpers by uuid.
     */
    private final Map<UUID, Helper> playerInventories = new HashMap<>();
    
    @Override
    public InventoryHelper openInventory(Player player, String name, ItemStack[] items, InventoryListener listener)
    {
        final Inventory inventory = Bukkit.createInventory(null, items.length, name);
        inventory.setContents(items);
        final EntityPlayer entity = ((CraftPlayer) player).getHandle();
        if (entity.activeContainer != entity.defaultContainer)
        {
            CraftEventFactory.handleInventoryCloseEvent(entity);
            entity.activeContainer = entity.defaultContainer;
        }
        player.openInventory(inventory);
        final Helper helper = new Helper(entity, inventory, listener);
        this.playerInventories.put(player.getUniqueId(), helper);
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
            final ItemStack stack = evt.getCurrentItem();
            this.playerInventories.get(uuid).listener.onClick(stack);
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
    private static final class Helper implements InventoryHelper
    {
        
        /** bukkit inventory. */
        private Inventory    inv;
        /** craft entity. */
        private EntityPlayer entity;
        /** the inventory listener. */
        InventoryListener    listener;
        
        /**
         * @param entity
         * @param inventory
         * @param listener 
         */
        public Helper(EntityPlayer entity, Inventory inventory, InventoryListener listener)
        {
            this.inv = inventory;
            this.entity = entity;
            this.listener = listener;
        }
        
        @Override
        public void close()
        {
            CraftEventFactory.handleInventoryCloseEvent(this.entity);
        }
        
        @Override
        public Inventory getBukkitInventory()
        {
            return this.inv;
        }
        
        @Override
        public void setNewPage(String name, ItemStack[] items)
        {
            this.inv.setContents(items);
            // TODO Change the name
        }
        
    }
    
}
