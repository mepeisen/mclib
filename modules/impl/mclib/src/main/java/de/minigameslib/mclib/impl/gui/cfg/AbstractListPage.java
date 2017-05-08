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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.PagableClickGuiPage;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.impl.gui.ClickGuis;

/**
 * An abstract list page being able to insert and remove list elements.
 * 
 * @author mepeisen
 * @param <T>
 *            element type
 */
public abstract class AbstractListPage<T> extends PagableClickGuiPage<String> implements ClickGuiInterface
{
    
    /** consumer to display prev page. */
    protected ClickGuiItem.GuiItemHandler onPrev;
    
    /** consumer to delete language. */
    protected ClickGuiItem.GuiItemHandler onDelete;
    
    /** title. */
    protected Serializable                title;
    
    /** the lines. */
    protected List<T>                     lines;
    
    /** converter for output. */
    protected Function<T, String>         toString;
    
    /** string marker. */
    private static final String           DELETE_MARKER        = "\0$DELETE$";        //$NON-NLS-1$
    
    /** string marker. */
    private static final String           CREATE_BEFORE_MARKER = "\0$CREATE-BEFORE$"; //$NON-NLS-1$
    
    /** string marker. */
    private static final String           CREATE_AFTER_MARKER  = "\0$CREATE-AFTER";   //$NON-NLS-1$
    
    /**
     * Constructor.
     * 
     * @param title
     *            the page title.
     * @param lines
     *            the lines
     * @param onPrev
     *            handler to display the calling page
     * @param onDelete
     *            handler for deleting a single item
     * @param toString
     *            handler for converting items to string
     */
    public AbstractListPage(Serializable title, T[] lines, GuiItemHandler onPrev, GuiItemHandler onDelete, Function<T, String> toString)
    {
        this.lines = Arrays.asList(lines);
        this.title = title;
        this.onPrev = onPrev;
        this.onDelete = onDelete;
        this.toString = toString;
    }
    
    @Override
    protected int count()
    {
        return this.lines.size() * ITEMS_PER_LINE;
    }
    
    @Override
    protected List<String> getElements(int start, int limit)
    {
        final List<String> result = new ArrayList<>();
        final int index = start / ITEMS_PER_LINE;
        if (index < this.lines.size())
        {
            result.add(CREATE_BEFORE_MARKER);
            result.add(this.toString.apply(this.lines.get(index)));
            result.add(CREATE_AFTER_MARKER);
            result.add(null);
            result.add(DELETE_MARKER);
        }
        return result;
    }
    
    @Override
    protected ClickGuiItem map(int line, int col, int index, String elm)
    {
        if (elm == null)
        {
            return null;
        }
        int realLine = (index - col) / ITEMS_PER_LINE;
        if (elm.equals(CREATE_BEFORE_MARKER))
        {
            return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_New), Messages.IconCreateBefore, (p, s, g) -> this.onCreateBefore(p, s, g, realLine));
        }
        if (elm.equals(CREATE_AFTER_MARKER))
        {
            return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_New), Messages.IconCreateAfter, (p, s, g) -> this.onCreateAfter(p, s, g, realLine));
        }
        if (elm.equals(DELETE_MARKER))
        {
            return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Erase), Messages.IconDeleteLine, (p, s, g) -> this.onDeleteLine(p, s, g, realLine));
        }
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconEdit, (p, s, g) -> onEdit(p, s, g, realLine), realLine);
    }
    
    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[] {
            null,
            null,
            this.itemPrevPage(),
            this.itemNextPage(),
            new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Back), Messages.IconBack, this.onPrev),
            null,
            new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Erase), Messages.IconDeleteAll, this.onDelete),
            null,
            null
        };
    }
    
    @Override
    public Serializable getPageName()
    {
        return this.title;
    }
    
    /**
     * insert before.
     * 
     * @param player
     *            target player
     * @param session
     *            gui session
     * @param gui
     *            click gui
     * @param realLine
     *            real line index for insert
     * @throws McException
     *             thrown on errors
     */
    protected abstract void onCreateBefore(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException;
    
    /**
     * insert after.
     * 
     * @param player
     *            target player
     * @param session
     *            gui session
     * @param gui
     *            click gui
     * @param realLine
     *            real line index for insert
     * @throws McException
     *             thrown on errors
     */
    protected abstract void onCreateAfter(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException;
    
    /**
     * delete line.
     * 
     * @param player
     *            target player
     * @param session
     *            gui session
     * @param gui
     *            click gui
     * @param realLine
     *            real line index for delete
     * @throws McException
     *             thrown on errors
     */
    protected void onDeleteLine(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        this.lines.remove(realLine);
        this.onRefresh(player, session, gui);
    }
    
    /**
     * edit line.
     * 
     * @param player
     *            target player
     * @param session
     *            gui session
     * @param gui
     *            click gui
     * @param realLine
     *            real line index for edit
     * @throws McException
     *             thrown on errors
     */
    protected abstract void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException;
    
    /**
     * Editor to create strings.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "gui.config.list")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Back icon.
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment("back icon")
        IconBack,
        
        /**
         * Create icon.
         */
        @LocalizedMessage(defaultMessage = "Create new line before #%1$d")
        @MessageComment(value = "Create before icon", args = @MessageComment.Argument("line number"))
        IconCreateBefore,
        
        /**
         * Create icon.
         */
        @LocalizedMessage(defaultMessage = "Create new line after #%1$d")
        @MessageComment(value = "Create after icon", args = @MessageComment.Argument("line number"))
        IconCreateAfter,
        
        /**
         * Delete icon.
         */
        @LocalizedMessage(defaultMessage = "Delete line #%1$d")
        @MessageComment(value = "Delete line", args = @MessageComment.Argument("line number"))
        IconDeleteLine,
        
        /**
         * Delete all.
         */
        @LocalizedMessage(defaultMessage = "Delete all")
        @MessageComment(value = "Delete all")
        IconDeleteAll,
        
        /**
         * Edit icon.
         */
        @LocalizedMessage(defaultMessage = "Edit line #%1$d")
        @MessageComment(value = "Edit icon", args = @MessageComment.Argument("line number"))
        IconEdit,
        
        /**
         * Edit existing: text description.
         */
        @LocalizedMessageList({ "Edit line for %1$s - %2$d." })
        @MessageComment(value = "Edit existing: text description", args = { @MessageComment.Argument("title"), @MessageComment.Argument("line number") })
        EditTextDescription,
        
    }
    
    @Override
    public ClickGuiId getUniqueId()
    {
        return ClickGuis.List;
    }
    
    @Override
    public ClickGuiPageInterface getInitialPage()
    {
        return this;
    }
    
    @Override
    public int getLineCount()
    {
        return 6;
    }
    
}
