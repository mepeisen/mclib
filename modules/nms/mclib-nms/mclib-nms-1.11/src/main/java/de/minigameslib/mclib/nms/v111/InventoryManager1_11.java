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

package de.minigameslib.mclib.nms.v111;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import de.minigameslib.mclib.pshared.MclibConstants;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.Item;
import net.minecraft.server.v1_11_R1.Items;

/**
 * Inventory manager implementation.
 * 
 * @author mepeisen
 */
public class InventoryManager1_11 implements InventoryManagerInterface
{
    
    /**
     * inventory helpers by uuid.
     */
    private final Map<UUID, Helper> playerInventories = new HashMap<>();
    
    @Override
    public InventoryHelper openInventory(Player player, String name, ItemStack[] items, InventoryListener listener)
    {
        final Inventory inventory = Bukkit.createInventory(null, items.length, name);
        setContents((CraftInventoryCustom)inventory, items);
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
     * Sets contents; safe way for blocks that are unknown by bukkit
     * @param inventory
     * @param items
     */
    private void setContents(CraftInventoryCustom inventory, ItemStack[] items)
    {
        for (int i = 0; i < items.length; i++)
        {
            setContents(inventory, items[i], i);
        }
    }
    
    /**
     * Sets contents; safe way for blocks that are unknown by bukkit
     * @param inventory
     * @param item
     * @param index
     */
    public static void setContents(CraftInventory inventory, ItemStack item, int index)
    {
        if (item != null && item.getTypeId() >= MclibConstants.MIN_BLOCK_ID)
        {
            final net.minecraft.server.v1_11_R1.ItemStack nms = convertToNms(item);
            inventory.getInventory().setItem(index, nms);
        }
        else
        {
            inventory.setItem(index, item);
        }
    }

    /**
     * @param item
     * @return nms
     */
    public static net.minecraft.server.v1_11_R1.ItemStack convertToNms(ItemStack item)
    {
        final net.minecraft.server.v1_11_R1.ItemStack nms = new net.minecraft.server.v1_11_R1.ItemStack(
                Item.getById(item.getTypeId()),
                item.getAmount(),
                item.getDurability());
        if (item.hasItemMeta()) {
            final net.minecraft.server.v1_11_R1.ItemStack temp = new net.minecraft.server.v1_11_R1.ItemStack(Items.APPLE);
            CraftItemStack.setItemMeta(temp, item.getItemMeta());
            nms.setTag(temp.getTag());
            
        }
        return nms;
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
            final ItemStack stack = getCurrentItem(evt);
            evt.setCancelled(this.playerInventories.get(uuid).listener.onClick(stack, evt.getRawSlot(), evt.getSlot()));
        }
    }
    
    /**
     * @param evt
     * @return item stack
     */
    private ItemStack getCurrentItem(InventoryClickEvent evt)
    {
        if (evt.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            return evt.getCurrentItem();
        }
        final int rawSlot = evt.getRawSlot();
        if (rawSlot >= evt.getView().getTopInventory().getSize()) {
            return null;
        }
        final net.minecraft.server.v1_11_R1.ItemStack stack = ((CraftInventory)evt.getView().getTopInventory()).getInventory().getItem(rawSlot);
        if (stack == null)
        {
            return null;
        }
        if (Item.getId(stack.getItem()) >= MclibConstants.MIN_BLOCK_ID)
        {
            final ItemStack result = new ItemStack(Item.getId(stack.getItem()), stack.getCount(), (short) stack.getData());
            ItemHelper1_11.setMeta(result, CraftItemStack.getItemMeta(stack));
            return result;
        }
        return CraftItemStack.asCraftMirror(stack);
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
