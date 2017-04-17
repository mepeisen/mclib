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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * Config option to edit boolean variables
 * 
 * @author mepeisen
 *
 */
public class FloatConfigOption extends AbstractConfigOption
{
    
    /**
     * @param value
     */
    public FloatConfigOption(ConfigurationValueInterface value)
    {
        super(value);
    }

    @Override
    public ClickGuiItem getItem(Runnable onChange, McRunnable contextProvider) throws McException
    {
        final float num = this.calculate(contextProvider, this.getValue()::getFloat);
        final ItemStack stack = ItemServiceInterface.instance().createItem(CommonItems.App_Calculator);
        ItemServiceInterface.instance().setDescription(stack, this.getValue().getComment());
        return new ClickGuiItem(stack, AbstractConfigOption.Messages.ConfigNameWithFloatValue, (p, s, g) -> this.select(p, s, g, onChange, contextProvider), this.getValue().path(), num);
    }
    
    /**
     * selector
     * @param player
     * @param session
     * @param guiInterface
     * @param onChange 
     * @param contextProvider
     * @throws McException 
     */
    private void select(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface, Runnable onChange, McRunnable contextProvider) throws McException
    {
        final float num = this.calculate(contextProvider, this.getValue()::getFloat);
        player.nestAnvilGui(new QueryText(
                String.valueOf(num),
                null,
                s -> {
                    try
                    {
                        final float newVal = Float.parseFloat(s);
                        this.run(contextProvider, () -> {
                            this.getValue().setFloat(newVal);
                            this.getValue().saveConfig();
                        });
                        onChange.run();
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new McException(AbstractConfigOption.Messages.InvalidNumericFormat, ex);
                    }
                },
                this.getValue().getComment()));
    }
    
}
