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
import java.util.List;
import java.util.function.Function;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.AbstractListPage;
import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McFunction;
import de.minigameslib.mclib.impl.gui.ClickGuis;

/**
 * An abstract list page being able to insert and remove list elements.
 * 
 * @author mepeisen
 * @param <T>
 *            element type
 */
public class TextListPage<T> extends AbstractListPage<T>
{
    
    /** converter from string. */
    private McFunction<String, T> fromString;
    
    /** initial value for new elements. */
    private T                     initialValue;
    
    /** save function. */
    private McConsumer<List<T>>   onSave;
    
    /**
     * Constructor.
     * 
     * @param title
     *            page title
     * @param lines
     *            existing values
     * @param onPrev
     *            handler to display previous page/ the calling page
     * @param onDelete
     *            function to invoke on deleting all values
     * @param save
     *            handler to save the values.
     * @param fromString
     *            converter function to parse string input to element type
     * @param toString
     *            converter function to generate string input from element type
     * @param initialValue
     *            initial value for new elements
     */
    public TextListPage(Serializable title, T[] lines, GuiItemHandler onPrev, GuiItemHandler onDelete, McConsumer<List<T>> save, McFunction<String, T> fromString, Function<T, String> toString,
        T initialValue)
    {
        super(title, lines, onPrev, onDelete, toString);
        this.onSave = save;
        this.fromString = fromString;
        this.initialValue = initialValue;
    }
    
    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(super.getItems(), 6);
    }
    
    @Override
    protected void onCreateBefore(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        this.lines.add(realLine, this.initialValue);
        this.onSave.accept(this.lines);
        this.onEdit(player, session, gui, realLine);
    }
    
    @Override
    protected void onCreateAfter(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        this.lines.add(realLine + 1, this.initialValue);
        this.onSave.accept(this.lines);
        this.onEdit(player, session, gui, realLine + 1);
    }
    
    @Override
    protected void onDeleteLine(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        this.lines.remove(realLine);
        this.onRefresh(player, session, gui);
    }
    
    @Override
    protected void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        final String text = this.toString.apply(this.lines.get(realLine));
        
        player.nestAnvilGui(new QueryText(
            text,
            null,
            (s) -> this.onEdit(player, session, gui, realLine, s),
            player.encodeMessage(Messages.EditTextDescription, this.title, realLine),
            false));
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
     *            real line index
     * @param content
     *            the string content
     * @throws McException
     *             thrown on input errors.
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine, String content) throws McException
    {
        this.lines.set(realLine, this.fromString.apply(content));
        this.onSave.accept(this.lines);
    }

    @Override
    public ClickGuiId getUniqueId()
    {
        return ClickGuis.List;
    }
    
}
