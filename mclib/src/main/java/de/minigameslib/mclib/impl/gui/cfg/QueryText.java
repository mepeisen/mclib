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

package de.minigameslib.mclib.impl.gui.cfg;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.AnvilGuiId;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.gui.AnvilGuis;

/**
 * Query a text.
 * 
 * @author mepeisen
 */
public class QueryText implements AnvilGuiInterface
{
    
    /** cancel func. */
    private McRunnable         onCancel;
    
    /** input func. */
    private McConsumer<String> onInput;
    
    /** source text. */
    private String             src;
    
    /** description. */
    private String[]           description;
    
    /** pre using the input and set it to output slot. */
    private boolean            preUseInput;
    
    /**
     * Constructor.
     * 
     * @param src
     *            source string
     * @param onCancel
     *            handler for cancelling the input
     * @param onInput
     *            handler for accepting a new value
     * @param description
     *            the string description for the queried value
     * @param preUseInput
     *            pre using the input and set it to output slot
     */
    public QueryText(String src, McRunnable onCancel, McConsumer<String> onInput, String[] description, boolean preUseInput)
    {
        this.src = src;
        this.onCancel = onCancel;
        this.onInput = onInput;
        this.description = description;
        this.preUseInput = preUseInput;
    }
    
    @Override
    public ItemStack getItem()
    {
        final ItemStack stack = ItemServiceInterface.instance().createItem(CommonItems.App_Text, this.src);
        final ItemMeta meta = stack.getItemMeta();
        meta.setLore(Arrays.asList(this.description));
        stack.setItemMeta(meta);
        return stack;
    }
    
    @Override
    public ItemStack getOutputItem()
    {
        if (this.preUseInput)
        {
            return this.getItem();
        }
        return null;
    }

    @Override
    public AnvilGuiId getUniqueId()
    {
        return AnvilGuis.QueryText;
    }
    
    @Override
    public void onCancel()
    {
        try
        {
            if (this.onCancel != null)
            {
                this.onCancel.run();
            }
        }
        catch (McException e)
        {
            // TODO logging
        }
    }
    
    @Override
    public void onInput(String arg0) throws McException
    {
        this.onInput.accept(arg0);
    }
    
}
