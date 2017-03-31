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

package de.minigameslib.mclib.test.impl;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.BlockData;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * @author mepeisen
 *
 */
public class ClickGui implements ClickGuiInterface, ClickGuiPageInterface
{
    
    @Override
    public ClickGuiId getUniqueId()
    {
        return GuiIds2.Sample;
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

    @Override
    public Serializable getPageName()
    {
        return CommonMessages.InvokeIngame;
    }

    @Override
    public ClickGuiItem[][] getItems()
    {
        return new ClickGuiItem[][]{{
            new ClickGuiItem(new ItemStack(Material.RED_SANDSTONE), CommonMessages.InvokeOnConsole, this::foo),
            new ClickGuiItem(BlockServiceInterface.instance().createItem(MyBlocks.CopperOre, BlockData.CustomVariantId.DEFAULT, CommonMessages.InvokeIngame), null, this::bar)
        }};
    }
    
    private void foo(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.openAnvilGui(new MyAnvilGui(s -> {
            System.out.println("1: " + s);
            player.openAnvilGui(new MyAnvilGui(s2 -> {
                System.out.println("2: " + s2);
            }));
        }));
    }
    
    private void bar(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        //
    }
    
}
