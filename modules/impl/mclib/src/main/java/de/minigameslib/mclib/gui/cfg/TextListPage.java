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

package de.minigameslib.mclib.gui.cfg;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McFunction;

/**
 * An abstract list page being able to insert and remove list elements
 * 
 * @author mepeisen
 * @param <T> element type
 */
public class TextListPage<T> extends AbstractListPage<T>
{
    
    /** converter from string */
    private McFunction<String, T> fromString;
    
    /** initial value for new elements */
    private T initialValue;

    /** save function */
    private McConsumer<List<T>> onSave;

    /**
     * @param title
     * @param lines 
     * @param onPrev
     * @param onDelete
     * @param save 
     * @param fromString 
     * @param toString 
     * @param initialValue 
     */
    public TextListPage(Serializable title, T[] lines, GuiItemHandler onPrev, GuiItemHandler onDelete, McConsumer<List<T>> save, McFunction<String, T> fromString, Function<T, String> toString, T initialValue)
    {
        super(title, lines, onPrev, onDelete, toString);
        this.onSave = save;
        this.fromString = fromString;
        this.initialValue = initialValue;
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
                player.encodeMessage(Messages.EditTextDescription, this.title, realLine)));
    }
    
    /**
     * edit line
     * @param player
     * @param session
     * @param gui
     * @param realLine
     * @param content
     * @throws McException 
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine, String content) throws McException
    {
        this.lines.set(realLine, this.fromString.apply(content));
        this.onSave.accept(this.lines);
    }
    
}
