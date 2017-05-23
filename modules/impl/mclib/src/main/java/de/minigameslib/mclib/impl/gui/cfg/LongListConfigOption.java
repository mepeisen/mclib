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
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * Config option to edit long list variables.
 * 
 * @author mepeisen
 *
 */
public class LongListConfigOption extends AbstractConfigOption
{
    
    /**
     * Constructor.
     * 
     * @param value
     *            value to be edited.
     */
    public LongListConfigOption(EditableValue value)
    {
        super(value);
    }
    
    @Override
    public ClickGuiItem getItem(Runnable onChange, McRunnable contextProvider) throws McException
    {
        final ItemStack stack = ItemServiceInterface.instance().createItem(CommonItems.App_Calculator);
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
        final long[] prim = this.calculate(contextProvider, this.getValue()::getLongList);
        final Long[] arr = new Long[prim.length];
        for (int i = 0; i < prim.length; i++)
        {
            arr[i] = prim[i];
        }
        
        player.nestClickGui(new TextListPage<>(
            AbstractConfigOption.Messages.ConfigName.toArg(this.getValue().path()),
            arr,
            (p, s, g) -> s.close(),
            (p, s, g) ->
            {
                this.run(contextProvider, () ->
                {
                    getValue().setLongList(null);
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
                s.close();
                onChange.run();
            },
            a ->
            {
                this.run(contextProvider, () ->
                {
                    final long[] res = new long[a.size()];
                    for (int i = 0; i < res.length; i++)
                    {
                        res[i] = a.get(i);
                    }
                    getValue().setLongList(res);
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
                onChange.run();
            },
            str ->
            {
                try
                {
                    final long newVal = Long.parseLong(str);
                    return (Long) newVal;
                }
                catch (NumberFormatException ex)
                {
                    throw new McException(AbstractConfigOption.Messages.InvalidNumericFormat, ex);
                }
            },
            b -> String.valueOf(b),
            Long.valueOf(0)));
    }
    
}
