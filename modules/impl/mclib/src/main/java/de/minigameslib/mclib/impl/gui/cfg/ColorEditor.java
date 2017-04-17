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

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.ConfigColorData;
import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.gui.cfg.AbstractConfigOption.ClickGuis;

/**
 * Edit a color data
 * 
 * @author mepeisen
 *
 */
public class ColorEditor implements ClickGuiInterface, ClickGuiPageInterface
{

    /** color */
    private int red;
    /** color */
    private int blue;
    /** color */
    private int green;
    
    /** save function */
    private McConsumer<ConfigColorData> onSave;
    
    /**
     * @param color
     * @param onSave 
     */
    public ColorEditor(ConfigColorData color, McConsumer<ConfigColorData> onSave)
    {
        this.red = color.getRed();
        this.blue = color.getBlue();
        this.green = color.getGreen();
        this.onSave = onSave;
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public ClickGuiItem[][] getItems()
    {
        return new ClickGuiItem[][]{
            {
                new ClickGuiItem(new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()), Messages.IconRed, this::onRed),
                new ClickGuiItem(new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData()), Messages.IconGreen, this::onGreen),
                new ClickGuiItem(new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData()), Messages.IconBlue, this::onBlue),
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Yes), Messages.IconYes, this::onYes),
                null,
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_No), Messages.IconAbort, this::onAbort),
            }
        };
    }
    
    /**
     * edit red
     * @param player
     * @param session
     * @param guiInterface
     * @throws McException 
     */
    private void onRed(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws McException
    {
        final String src = String.valueOf(this.red);
        
        player.nestAnvilGui(new QueryText(
                src,
                null,
                s -> {
                    try
                    {
                        final int val = Integer.parseInt(s);
                        if (val < 0 || val > 255)
                        {
                            throw new McException(AbstractConfigOption.Messages.NumberOutOfRange, 0, 255);
                        }
                        this.red = val;
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new McException(AbstractConfigOption.Messages.InvalidNumericFormat, ex);
                    }
                },
                player.encodeMessage(Messages.DecriptionRed)));
    }
    
    /**
     * edit green
     * @param player
     * @param session
     * @param guiInterface
     * @throws McException 
     */
    private void onGreen(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws McException
    {
        final String src = String.valueOf(this.green);
        
        player.nestAnvilGui(new QueryText(
                src,
                null,
                s -> {
                    try
                    {
                        final int val = Integer.parseInt(s);
                        if (val < 0 || val > 255)
                        {
                            throw new McException(AbstractConfigOption.Messages.NumberOutOfRange, 0, 255);
                        }
                        this.green = val;
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new McException(AbstractConfigOption.Messages.InvalidNumericFormat, ex);
                    }
                },
                player.encodeMessage(Messages.DecriptionGreen)));
    }
    
    /**
     * edit blue
     * @param player
     * @param session
     * @param guiInterface
     * @throws McException 
     */
    private void onBlue(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws McException
    {
        final String src = String.valueOf(this.blue);
        
        player.nestAnvilGui(new QueryText(
                src,
                null,
                s -> {
                    try
                    {
                        final int val = Integer.parseInt(s);
                        if (val < 0 || val > 255)
                        {
                            throw new McException(AbstractConfigOption.Messages.NumberOutOfRange, 0, 255);
                        }
                        this.blue = val;
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new McException(AbstractConfigOption.Messages.InvalidNumericFormat, ex);
                    }
                },
                player.encodeMessage(Messages.DecriptionBlue)));
    }
    
    /**
     * accept
     * @param player
     * @param session
     * @param guiInterface
     * @throws McException 
     */
    private void onYes(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface) throws McException
    {
        this.onSave.accept(new ConfigColorData(this.red, this.green, this.blue));
        session.close();
    }
    
    /**
     * abort
     * @param player
     * @param session
     * @param guiInterface
     */
    private void onAbort(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface)
    {
        session.close();
    }
    
    @Override
    public ClickGuiId getUniqueId()
    {
        return ClickGuis.Color;
    }
    
    @Override
    public ClickGuiPageInterface getInitialPage()
    {
        return this;
    }
    
    @Override
    public int getLineCount()
    {
        return 1;
    }

    /**
     * Editor to create colors.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "gui.config.color")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * dialog title
         */
        @LocalizedMessage(defaultMessage = "Edit rgb color")
        @MessageComment("dialog title")
        Title,
        
        /**
         * yes icon
         */
        @LocalizedMessage(defaultMessage = "Save")
        @MessageComment("yes icon")
        IconYes,
        
        /**
         * no icon
         */
        @LocalizedMessage(defaultMessage = "Abort")
        @MessageComment("abort icon")
        IconAbort,
        
        /**
         * red icon
         */
        @LocalizedMessage(defaultMessage = "red")
        @MessageComment("red icon")
        IconRed,
        
        /**
         * green icon
         */
        @LocalizedMessage(defaultMessage = "green")
        @MessageComment("green icon")
        IconGreen,
        
        /**
         * blue icon
         */
        @LocalizedMessage(defaultMessage = "blue")
        @MessageComment("blue icon")
        IconBlue,

        /**
         * red component
         */
        @LocalizedMessageList("Edit the red color value. Enter numbers from 0 to 255.")
        @MessageComment("description for red color component")
        DecriptionRed,
        
        /**
         * red component
         */
        @LocalizedMessageList("Edit the green color value. Enter numbers from 0 to 255.")
        @MessageComment("description for green color component")
        DecriptionGreen,
        
        /**
         * red component
         */
        @LocalizedMessageList("Edit the blue color value. Enter numbers from 0 to 255.")
        @MessageComment("description for blue color component")
        DecriptionBlue,
    }
    
}
