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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.GuiType;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface.AnvilHelper;
import de.minigameslib.mclib.nms.api.AnvilManagerInterface.AnvilListener;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface.InventoryHelper;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface.InventoryListener;

/**
 * Implementation of a gui session
 * 
 * @author mepeisen
 */
public class GuiSessionImpl implements GuiSessionInterface, InventoryListener, AnvilListener
{
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiSessionImpl.class.getName());
    
    /** the current gui type. */
    private GuiType type = GuiType.None;
    
    /** the gui interface. */
    private ClickGuiInterface         gui;
    /** anvil gui interface. */
    private AnvilGuiInterface agui;
    /** the arena player. */
    private McPlayerInterface      player;
    /** the current inventory name. */
    private LocalizedMessageInterface currentName;
    /** the current items. */
    private ClickGuiItem[][]          currentItems;
    /** the line count. */
    private int                       lineCount;

    /** gui instance. */
    private InventoryHelper guiHelper;

    /** anvil gui instance. */
    private AnvilHelper aguiHelper;
    
    /**
     * Constructor
     * 
     * @param gui
     *            the gui to be used
     * @param player
     *            the arena player to be used
     */
    public GuiSessionImpl(ClickGuiInterface gui, McPlayerInterface player)
    {
        this.type = GuiType.ClickGui;
        this.gui = gui;
        this.player = player;
        this.currentName = gui.getInitialPage().getPageName();
        this.currentItems = gui.getInitialPage().getItems();
        this.lineCount = gui.getLineCount();
        
        final String name = player.getBukkitPlayer().isOp() ? this.currentName.toAdminMessage(player.getPreferredLocale()) : this.currentName.toUserMessage(player.getPreferredLocale());
        final ItemStack[] items = this.toItemStack();
        this.guiHelper = Bukkit.getServicesManager().load(InventoryManagerInterface.class).openInventory(player.getBukkitPlayer(), name, items, this);
    }
    
    /**
     * Constructor
     * 
     * @param gui
     *            the gui to be used
     * @param player
     *            the arena player to be used
     */
    public GuiSessionImpl(AnvilGuiInterface gui, McPlayerInterface player)
    {
        this.type = GuiType.AnvilGui;
        this.agui = gui;
        this.player = player;
        
        this.aguiHelper = Bukkit.getServicesManager().load(AnvilManagerInterface.class).openGui(player.getBukkitPlayer(), gui.getItem(), this);
    }
    
    /**
     * Converts the existing items to item stack.
     * @return item stack.
     */
    private ItemStack[] toItemStack()
    {
        final List<ItemStack> result = new ArrayList<>();
        for (int line = 0; line < this.lineCount; line++)
        {
            final ClickGuiItem[] itemline = (this.currentItems == null || this.currentItems.length <= line) ? null : this.currentItems[line];
            for (int column = 0; column < 9; column++)
            {
                if (itemline == null || itemline.length <= column)
                {
                    result.add(new ItemStack(Material.AIR));
                }
                else
                {
                    final ItemStack stack = itemline[column].getItemStack().clone();
                    final ItemMeta meta = stack.getItemMeta();
                    final String displayName = InventoryManagerInterface.toColorsString(
                            this.player.getBukkitPlayer().isOp() ? itemline[column].getDisplayName().toAdminMessage(this.player.getPreferredLocale()) : itemline[column].getDisplayName().toUserMessage(this.player.getPreferredLocale()),
                            line + ":" + column //$NON-NLS-1$
                            );
                    meta.setDisplayName(displayName);
                    stack.setItemMeta(meta);
                    result.add(stack);
                }
            }
        }
        return result.toArray(new ItemStack[result.size()]);
    }
    
    @Override
    public void readFromConfig(ConfigurationSection section)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void writeToConfig(ConfigurationSection section)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public McPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    @Override
    public ClickGuiInterface getClickGui()
    {
        return this.gui;
    }
    
    @Override
    public AnvilGuiInterface getAnvilGui()
    {
        return this.agui;
    }
    
    @Override
    public void setNewPage(ClickGuiPageInterface page)
    {
        this.currentName = page.getPageName();
        this.currentItems = page.getItems();
        final String name = this.player.getBukkitPlayer().isOp() ? this.currentName.toAdminMessage(this.player.getPreferredLocale()) : this.currentName.toUserMessage(this.player.getPreferredLocale());
        final ItemStack[] items = this.toItemStack();
        this.guiHelper.setNewPage(name, items);
    }
    
    @Override
    public void close()
    {
        this.player.getBukkitPlayer().closeInventory();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.gui.GuiSessionInterface#getGuiStorage()
     */
    @Override
    public McStorage getGuiStorage()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.gui.GuiSessionInterface#getPlayerSessionStorage()
     */
    @Override
    public McStorage getPlayerSessionStorage()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.github.mce.minigames.api.gui.GuiSessionInterface#getPlayerPersistentStorage()
     */
    @Override
    public McStorage getPlayerPersistentStorage()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onClose()
    {
        if (this.type == GuiType.AnvilGui)
        {
            // TODO fire event
        }
        else if (this.type == GuiType.ClickGui)
        {
            // TODO fire event
        }
        this.type = GuiType.None;
        this.gui = null;
        this.agui = null;
        this.currentItems = null;
        this.currentName = null;
        this.guiHelper = null;
        this.aguiHelper = null;
        this.player = null;
    }

    @Override
    public boolean onCommit(String name)
    {
        if (this.type == GuiType.AnvilGui)
        {
            try
            {
                this.agui.onInput(name);
            }
            catch (McException ex)
            {
                this.player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                return false;
            }
            // TODO fire event
        }
        return true;
    }

    @Override
    public void onClick(ItemStack stack)
    {
        if (this.type == GuiType.ClickGui)
        {
            if (stack.getItemMeta().hasDisplayName())
            {
                final String item = InventoryManagerInterface.stripColoredString(stack.getItemMeta().getDisplayName());
                final String[] splitted = item.split(":"); //$NON-NLS-1$
                if (splitted.length == 2)
                {
                    try
                    {
                        final int line = Integer.parseInt(splitted[0]);
                        final int col = Integer.parseInt(splitted[1]);
                        final ClickGuiItem guiItem = this.currentItems[line][col];
                        guiItem.handle(this.player, this, this.gui);
                    }
                    catch (McException ex)
                    {
                        this.player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                    }
                    catch (IndexOutOfBoundsException | NumberFormatException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unable to parse item name " + item, ex); //$NON-NLS-1$
                    }
                }
                else
                {
                    LOGGER.log(Level.WARNING, "Unable to parse item name " + item); //$NON-NLS-1$
                }
            }
        }
    }

    @Override
    public GuiType getCurrentType()
    {
        return this.type;
    }
    
}
