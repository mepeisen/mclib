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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * A list page being able to insert and remove enum list elements
 * 
 * @author mepeisen
 */
public class EnumListPage extends AbstractListPage<EnumerationValue>
{

    /** save function */
    private McConsumer<List<EnumerationValue>> onSave;
    
    /** enum class */
    private final Class<? extends EnumerationValue> clazz;

    /**
     * @param title
     * @param lines 
     * @param onPrev
     * @param onDelete
     * @param save   
     * @param clazz 
     */
    public EnumListPage(Serializable title, EnumerationValue[] lines, GuiItemHandler onPrev, GuiItemHandler onDelete, McConsumer<List<EnumerationValue>> save, Class<? extends EnumerationValue> clazz)
    {
        super(title, lines, onPrev, onDelete, b -> b.name());
        this.onSave = save;
        this.clazz = clazz;
    }
    
    @Override
    protected void onCreateBefore(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        final EnumerationValue data = EnumServiceInterface.instance().getEnumValues(this.clazz).iterator().next();
        this.lines.add(realLine, data);
        this.onSave.accept(this.lines);
        this.onEdit(player, session, gui, realLine);
    }
    
    @Override
    protected void onCreateAfter(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        final EnumerationValue data = EnumServiceInterface.instance().getEnumValues(this.clazz).iterator().next();
        this.lines.add(realLine, data);
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
        final EnumerationValue value = this.lines.get(realLine);
        player.nestClickGui(new EnumEditor(
                this.clazz,
                value,
                c -> {
                    this.lines.set(realLine, c);
                    this.onSave.accept(this.lines);
                }
                ));
    }
    
}
