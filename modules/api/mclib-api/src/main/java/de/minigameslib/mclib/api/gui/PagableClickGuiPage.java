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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * Base class for click gui pages with multiple pages.
 * 
 * <p>
 * Can be used in click gui containers having 6 lines.
 * </p>
 * 
 * @author mepeisen
 * @param <T>
 *            element type
 */
public abstract class PagableClickGuiPage<T> implements ClickGuiPageInterface
{
    
    /** number of items per page. */
    protected static final int ITEMS_PER_LINE = 9;
    
    /** number of items per page. */
    protected static final int ITEMS_PER_PAGE = ITEMS_PER_LINE * 4;
    
    /** numeric page num. */
    private int                pageNum        = 1;
    
    /**
     * Constructor to create the first page.
     */
    public PagableClickGuiPage()
    {
        // empty
    }
    
    /**
     * Constructor to create given page.
     * 
     * @param pageNum
     *            page number to be displayed, starting with 1
     */
    public PagableClickGuiPage(int pageNum)
    {
        this.pageNum = pageNum;
    }
    
    /**
     * Returns the number of elements.
     * 
     * @return count of elements
     */
    protected abstract int count();
    
    /**
     * Returns total number of pages.
     * 
     * @return total page number.
     */
    protected int totalPages()
    {
        return (int) Math.ceil((double) this.count() / ITEMS_PER_PAGE);
    }
    
    /**
     * Returns current page.
     * 
     * @return current page, starting with 1
     */
    protected int page()
    {
        return this.pageNum;
    }
    
    /**
     * Returns the elements for this page.
     * 
     * @param start
     *            start index
     * @param limit
     *            maximum limit
     * @return list of elements to be displayed
     */
    protected abstract List<T> getElements(int start, int limit);
    
    /**
     * Maps elements to click gui item.
     * 
     * @param line
     *            line number
     * @param col
     *            column number
     * @param index
     *            absolute item index
     * @param elm
     *            item element
     * @return click gui item
     */
    protected abstract ClickGuiItem map(int line, int col, int index, T elm);
    
    @Override
    public ClickGuiItem[][] getItems()
    {
        final List<T> list = this.getElements((this.pageNum - 1) * ITEMS_PER_PAGE, ITEMS_PER_PAGE);
        return new ClickGuiItem[][] {
            firstLine(),
            null,
            // items
            itemsLine(list, 0),
            itemsLine(list, ITEMS_PER_LINE),
            itemsLine(list, ITEMS_PER_LINE * 2),
            itemsLine(list, ITEMS_PER_LINE * 3),
        };
    }
    
    /**
     * Returns the first line.
     * 
     * @return first line
     */
    protected abstract ClickGuiItem[] firstLine();
    
    /**
     * Creates a line of items.
     * 
     * @param items
     *            the items collection
     * @param start
     *            starting index
     * @return line of icons
     */
    private ClickGuiItem[] itemsLine(Collection<T> items, int start)
    {
        int col = 0;
        int i = start + (this.pageNum - 1) * ITEMS_PER_PAGE;
        final List<ClickGuiItem> result = new ArrayList<>();
        final Iterator<T> iter = items.stream().skip(start).limit(ITEMS_PER_LINE).iterator();
        while (iter.hasNext())
        {
            result.add(this.map(start / ITEMS_PER_LINE, col, i, iter.next()));
            col++;
            i++;
        }
        return result.toArray(new ClickGuiItem[result.size()]);
    }
    
    /**
     * Refresh gui.
     * 
     * @param player
     *            target player
     * @param session
     *            gui session
     * @param gui
     *            click gui interface
     */
    protected void onRefresh(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(this);
    }
    
    /**
     * Moves to previous page.
     * 
     * @param player
     *            target player
     * @param session
     *            gui session
     * @param gui
     *            click gui interface
     */
    protected void onPrevPage(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        if (this.pageNum > 1)
        {
            this.pageNum--;
            session.setNewPage(this);
        }
    }
    
    /**
     * Moves to next page.
     * 
     * @param player
     *            target player
     * @param session
     *            gui session
     * @param gui
     *            click gui interface
     */
    protected void onNextPage(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        if (this.pageNum < this.totalPages())
        {
            this.pageNum++;
            session.setNewPage(this);
        }
    }
    
    /**
     * prev page icon.
     * 
     * @return prev page icon
     */
    public ClickGuiItem itemPrevPage()
    {
        return this.page() > 1 ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Previous), CommonMessages.IconPreviousPage, this::onPrevPage) : null;
    }
    
    /**
     * next page icon.
     * 
     * @return next page icon
     */
    public ClickGuiItem itemNextPage()
    {
        return this.page() < this.totalPages() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Next), CommonMessages.IconNextPage, this::onNextPage) : null;
    }
    
}
