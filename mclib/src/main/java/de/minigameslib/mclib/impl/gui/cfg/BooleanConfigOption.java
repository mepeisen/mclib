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

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.EditableValue;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * Config option to edit boolean variables.
 * 
 * @author mepeisen
 *
 */
public class BooleanConfigOption extends AbstractConfigOption
{
    
    /**
     * Constructor.
     * 
     * @param value
     *            value to be edited.
     */
    public BooleanConfigOption(EditableValue value)
    {
        super(value);
    }
    
    @Override
    public ClickGuiItem getItem(Runnable onChange, GuiItemHandler back, GuiItemHandler home, McRunnable contextProvider) throws McException
    {
        final boolean isset = this.calculate(contextProvider, this.getValue()::getBoolean);
        final ItemStack stack = ItemServiceInterface.instance().createItem(isset ? CommonItems.App_Yes : CommonItems.App_No);
        ItemServiceInterface.instance().setDescription(stack, this.getValue().getComment());
        return new ClickGuiItem(stack, AbstractConfigOption.Messages.ConfigName, (p, s, g) -> this.select(p, s, g, onChange, contextProvider), this.getValue().path());
    }
    
    /**
     * selector.
     * 
     * @param player
     *            target player clicking the edit item
     * @param session
     *            gui session
     * @param guiInterface
     *            click gui
     * @param onChange
     *            function to invoke once the config value is changed; may be {@code null}
     * @param contextProvider
     *            runnable to setup context sensitive execution; may be {@code null}
     * @throws McException
     *             thrown on errors
     */
    private void select(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface, Runnable onChange, McRunnable contextProvider) throws McException
    {
        final boolean isset = this.calculate(contextProvider, this.getValue()::getBoolean);
        this.run(contextProvider, () ->
        {
            this.getValue().setBoolean(!isset);
            try
            {
                this.getValue().validate();
                this.getValue().saveConfig();
            }
            catch (McException ex)
            {
                // rollback
                this.getValue().rollbackConfig();
                throw ex;
            }
        });
        session.refreshClickGui();
        onChange.run();
    }
    
}
