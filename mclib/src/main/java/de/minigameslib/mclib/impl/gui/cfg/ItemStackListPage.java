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
import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.gui.AbstractListPage;
import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.gui.ClickGuis;

/**
 * A list page being able to insert and remove itemstack list elements.
 * 
 * @author mepeisen
 */
public class ItemStackListPage extends AbstractListPage<ConfigItemStackData>
{
    
    /** save function. */
    private McConsumer<List<ConfigItemStackData>> onSave;
    
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
     */
    public ItemStackListPage(Serializable title, ConfigItemStackData[] lines, GuiItemHandler onPrev, GuiItemHandler onDelete, McConsumer<List<ConfigItemStackData>> save)
    {
        // TODO toString
        super(title, lines, onPrev, onDelete, b -> b.toString());
        this.onSave = save;
    }
    
    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(super.getItems(), 6);
    }
    
    @Override
    protected void onCreateBefore(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        // final ConfigItemStackData data = new ConfigItemStackData((byte) 0, (byte) 0, (byte) 0);
        // this.lines.add(realLine, data);
        // this.onSave.accept(this.lines);
        // this.onEdit(player, session, gui, realLine);
        // TODO implement editor
        player.sendMessage(AbstractConfigOption.Messages.NotImplemented);
    }
    
    @Override
    protected void onCreateAfter(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int realLine) throws McException
    {
        // final ConfigItemStackData data = new ConfigItemStackData((byte) 0, (byte) 0, (byte) 0);
        // this.lines.add(realLine, data);
        // this.onSave.accept(this.lines);
        // this.onEdit(player, session, gui, realLine + 1);
        // TODO implement editor
        player.sendMessage(AbstractConfigOption.Messages.NotImplemented);
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
        // final ConfigItemStackData value = this.lines.get(realLine);
        // player.nestClickGui(new ItemStackEditor(
        // value,
        // c -> {
        // this.lines.set(realLine, c);
        // this.onSave.accept(this.lines);
        // }
        // ));
        // TODO implement editor
        player.sendMessage(AbstractConfigOption.Messages.NotImplemented);
    }

    @Override
    public ClickGuiId getUniqueId()
    {
        return ClickGuis.List;
    }
    
}
