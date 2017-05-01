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

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * A simple implementation of anvil gui.
 * 
 * @author mepeisen
 */
public class SimpleAnvilGui implements AnvilGuiInterface
{
    
    /** the gui id. */
    private final AnvilGuiId         guiId;
    
    /** the item stack. */
    private final ItemStack          item;
    
    /** the cancel action. */
    private final Runnable           cancelAction;
    
    /** the input check/ action. */
    private final McConsumer<String> inputAction;
    
    /**
     * Constructor to create the anvil gui.
     * 
     * @param guiId
     *            anvil gui id.
     * @param item
     *            the item to be used
     * @param inputAction
     *            the consumer for commiting values.
     */
    public SimpleAnvilGui(AnvilGuiId guiId, ItemStack item, McConsumer<String> inputAction)
    {
        this.guiId = guiId;
        this.item = item;
        this.inputAction = inputAction;
        this.cancelAction = null;
    }
    
    /**
     * Constructor to create the anvil gui including cancel action.
     * 
     * @param guiId
     *            anvil gui id.
     * @param item
     *            the item to be used
     * @param cancelAction
     *            invoked on cancelling/closing the anvil gui
     * @param inputAction
     *            the consumer for commiting values.
     */
    public SimpleAnvilGui(AnvilGuiId guiId, ItemStack item, Runnable cancelAction, McConsumer<String> inputAction)
    {
        this.guiId = guiId;
        this.item = item;
        this.cancelAction = cancelAction;
        this.inputAction = inputAction;
    }
    
    @Override
    public AnvilGuiId getUniqueId()
    {
        return this.guiId;
    }
    
    @Override
    public ItemStack getItem()
    {
        return this.item;
    }
    
    @Override
    public void onInput(String input) throws McException
    {
        this.inputAction.accept(input);
    }
    
    @Override
    public void onCancel()
    {
        if (this.cancelAction != null)
        {
            this.cancelAction.run();
        }
    }
    
}
