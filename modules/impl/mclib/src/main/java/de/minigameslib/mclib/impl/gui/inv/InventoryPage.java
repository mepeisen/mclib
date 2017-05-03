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

package de.minigameslib.mclib.impl.gui.inv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.PagableClickGuiPage;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.impl.gui.cfg.AbstractListPage.Messages;

/**
 * Inventory page
 * 
 * @author mepeisen
 */
public class InventoryPage extends PagableClickGuiPage<ItemStack>
{
    
    /** the inventory. */
    private Inventory inventory;
    
    /**
     * @param inventory
     */
    public InventoryPage(Inventory inventory)
    {
        this.inventory = inventory;
    }
    
    @Override
    public Serializable getPageName()
    {
        return this.inventory.getTitle();
    }
    
    @Override
    protected int count()
    {
        return this.inventory.getSize();
    }
    
    @Override
    protected List<ItemStack> getElements(int start, int limit)
    {
        return Arrays.stream(this.inventory.getStorageContents()).skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public ClickGuiItem[][] getItems()
    {
        final ClickGuiItem[][] items = super.getItems();
        return new ClickGuiItem[][] {
            ClickGuiPageInterface.withFillers(items[0], 0),
            ClickGuiPageInterface.withFillers(items[1], 1),
            items[2],
            items[3],
            items[4],
            items[5]
        };
    }
    
    @Override
    protected ClickGuiItem map(int line, int col, int index, ItemStack elm)
    {
        return new ClickGuiItem(elm == null ? new ItemStack(Material.AIR) : elm, true, (p, s, g, item) -> this.inventory.setItem(index, item));
    }
    
    /**
     * prev page icon.
     * 
     * @return prev page icon
     */
    public ClickGuiItem itemPrevPage()
    {
        return this.page() > 1 ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Previous), Messages.IconPreviousPage, this::onPrevPage) : null;
    }
    
    /**
     * next page icon.
     * 
     * @return next page icon
     */
    public ClickGuiItem itemNextPage()
    {
        return this.page() < this.totalPages() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Next), Messages.IconNextPage, this::onNextPage) : null;
    }
    
    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[] {
            null,
            itemPrevPage(),
            null,
            null,
            null,
            null,
            null,
            itemNextPage(),
            null
        };
    }
    
}
