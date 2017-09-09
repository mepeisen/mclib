/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General License for more details.

    You should have received a copy of the GNU General License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mclib.api.gui;

import java.io.Serializable;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * Base interface for gui service.
 * 
 * @author mepeisen
 */
public interface GuiServiceInterface
{
    
    /**
     * Returns the gui service.
     * 
     * @return gui service.
     */
    static GuiServiceInterface instance()
    {
        return GuiServiceCache.get();
    }
    
    /**
     * Lets the player opening a new anvil gui session for querying text.
     * 
     * @param player
     *            the target player
     * @param src
     *            original text.
     * @param onCancel
     *            runnable for cancelling the input.
     * @param onInput
     *            runnable for commiting the input.
     * @param preUseInput
     *            {@code true} for pre selecting the input.
     * @param description
     *            the description
     * @param descriptionArgs
     *            the description arguments
     * 
     * @return gui session
     * @throws McException
     *             thrown if the player is not online.
     */
    GuiSessionInterface openTextEditor(McPlayerInterface player, String src, McRunnable onCancel, McConsumer<String> onInput, boolean preUseInput, LocalizedMessageInterface description,
        Serializable... descriptionArgs)
        throws McException;
    
    /**
     * Lets the player opening a new anvil gui session for querying text; nests current gui to be re-used upon close of current gui.
     * 
     * @param player
     *            the target player
     * @param src
     *            original text.
     * @param onCancel
     *            runnable for cancelling the input.
     * @param onInput
     *            runnable for commiting the input.
     * @param preUseInput
     *            {@code true} for pre selecting the input.
     * @param description
     *            the description
     * @param descriptionArgs
     *            the description arguments
     * 
     * @return gui session
     * @throws McException
     *             thrown if the player is not online.
     */
    GuiSessionInterface nestTextEditor(McPlayerInterface player, String src, McRunnable onCancel, McConsumer<String> onInput, boolean preUseInput, LocalizedMessageInterface description,
        Serializable... descriptionArgs)
        throws McException;
    
    /**
     * home icon.
     * 
     * @param handler
     *            action handler.
     * @return home icon
     */
    static ClickGuiItem itemHome(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Home), Messages.IconBackToMainMenu, handler);
    }
    
    /**
     * close gui icon.
     * 
     * @return close gui icon
     */
    static ClickGuiItem itemCloseGui()
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Close), Messages.IconClose, (p, s, g) -> s.close());
    }
    
    /**
     * delete icon.
     * 
     * @param handler
     *            action handler
     * @param name
     *            icon name
     * @param nameargs
     *            arguments to build item name
     * @return new icon
     */
    static ClickGuiItem itemDelete(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Erase), name, handler, nameargs);
    }
    
    /**
     * cancel icon.
     * 
     * @param handler
     *            action handler
     * @param name
     *            icon name
     * @param nameargs
     *            arguments to build item name
     * @return new icon
     */
    static ClickGuiItem itemCancel(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Erase), name, handler, nameargs);
    }
    
    /**
     * new icon.
     * 
     * @param handler
     *            action handler
     * @param name
     *            icon name
     * @param nameargs
     *            arguments to build item name
     * @return new icon
     */
    static ClickGuiItem itemNew(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_New), name, handler, nameargs);
    }
    
    /**
     * back icon.
     * 
     * @param handler
     *            action handler
     * @param name
     *            icon name
     * @param nameargs
     *            arguments to build item name
     * @return back icon
     */
    static ClickGuiItem itemBack(ClickGuiItem.GuiItemHandler handler, LocalizedMessageInterface name, Serializable... nameargs)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Back), name, handler, nameargs);
    }
    
    /**
     * back icon.
     * 
     * @param handler
     *            action handler
     * @return back icon
     */
    static ClickGuiItem itemRefresh(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Refresh), Messages.IconRefresh, handler);
    }
    
    /**
     * prev page icon.
     * 
     * @param handler
     *            action handler
     * @return prev page icon
     */
    static ClickGuiItem itemPrevPage(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Previous), Messages.IconPreviousPage, handler);
    }
    
    /**
     * next page icon.
     * 
     * @param handler
     *            action handler
     * @return next page icon
     */
    static ClickGuiItem itemNextPage(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Next), Messages.IconNextPage, handler);
    }
    
    /**
     * search icon.
     * 
     * @param handler
     *            action handler
     * @return next page icon
     */
    static ClickGuiItem itemSearch(ClickGuiItem.GuiItemHandler handler)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Search), Messages.IconSearch, handler);
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "gui.common")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * The main menu icon.
         */
        @LocalizedMessage(defaultMessage = "Back to main menu")
        @MessageComment({ "main menu icon" })
        IconBackToMainMenu,
        
        /**
         * The close gui icon.
         */
        @LocalizedMessage(defaultMessage = "Close GUI")
        @MessageComment({ "close icon" })
        IconClose,
        
        /**
         * The prev page icon.
         */
        @LocalizedMessage(defaultMessage = "Previous page")
        @MessageComment({ "prev page icon" })
        IconPreviousPage,
        
        /**
         * The next page icon.
         */
        @LocalizedMessage(defaultMessage = "Next page")
        @MessageComment({ "next page icon" })
        IconNextPage,
        
        /**
         * The refresh icon.
         */
        @LocalizedMessage(defaultMessage = "Refresh")
        @MessageComment({ "refresh icon" })
        IconRefresh,
        
        /**
         * The search icon.
         */
        @LocalizedMessage(defaultMessage = "Search")
        @MessageComment({ "search icon" })
        IconSearch,
        
    }
    
}
