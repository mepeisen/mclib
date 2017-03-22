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

package de.minigameslib.mclib.api.gui;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * A clickable gui item.
 * 
 * @author mepeisen
 */
public class ClickGuiItem
{
    
    /** the item stack used to display the gui item */
    private final ItemStack                 itemStack;
    
    /** the items name/ title. */
    private final LocalizedMessageInterface displayName;
    
    /** the items name/ title. */
    private final Serializable[]            displayNameArgs;
    
    /** the click handler. */
    private GuiItemHandler                  handler;
    
    /**
     * Constructor to create a click item.
     * 
     * @param itemStack
     *            the item stack to display the gui item
     * @param displayName
     *            the display name
     * @param handler
     *            the action handler
     * @param displayNameArgs
     *            arguments to build the display name
     */
    public ClickGuiItem(ItemStack itemStack, LocalizedMessageInterface displayName, GuiItemHandler handler, Serializable... displayNameArgs)
    {
        this.itemStack = itemStack.clone();
        this.displayName = displayName;
        this.handler = handler;
        this.displayNameArgs = displayNameArgs;
    }
    
    /**
     * @return the itemStack
     */
    public ItemStack getItemStack()
    {
        return this.itemStack.clone();
    }
    
    /**
     * @return the displayName
     */
    public LocalizedMessageInterface getDisplayName()
    {
        return this.displayName;
    }
    
    /**
     * @return the displayName
     */
    public Serializable[] getDisplayNameArgs()
    {
        return this.displayNameArgs;
    }
    
    /**
     * Handle gui event.
     * 
     * @param player
     *            player that clicked the item
     * @param session
     *            gui session.
     * @param guiInterface
     *            gui interface.
     * @throws McException
     *             thrown if there are errors.
     */
    public void handle(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws McException
    {
        this.handler.handle(player, session, guiInterface);
    }
    
    /**
     * Gui item handler.
     * 
     * @author mepeisen
     */
    @FunctionalInterface
    public interface GuiItemHandler
    {
        /**
         * Handle gui event.
         * 
         * @param player
         *            player that clicked the item
         * @param session
         *            gui session.
         * @param guiInterface
         *            gui interface.
         * @throws McException
         *             thrown if there are errors.
         */
        void handle(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws McException;
    }
    
}
