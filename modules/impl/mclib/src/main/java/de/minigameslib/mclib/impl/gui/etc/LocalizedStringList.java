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

package de.minigameslib.mclib.impl.gui.etc;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiServiceInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.PagableClickGuiPage;
import de.minigameslib.mclib.api.gui.SimpleClickGui;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.gui.ClickGuis;
import de.minigameslib.mclib.impl.gui.cfg.QueryText;

/**
 * Click gui for editing localized strings.
 * 
 * @author mepeisen
 */
public class LocalizedStringList extends PagableClickGuiPage<Locale>
{
    
    /** title. */
    private Serializable                      title;
    
    /** config string. */
    private LocalizedConfigString             string;
    
    /** save function. */
    private McConsumer<LocalizedConfigString> save;
    
    /** previous page. */
    private ClickGuiPageInterface             prevPage;
    
    /**
     * Constructor.
     * 
     * @param title
     *            title
     * @param string
     *            config string to be edited
     * @param save
     *            save function
     * @param prevPage
     *            prev page
     */
    public LocalizedStringList(Serializable title, LocalizedConfigString string, McConsumer<LocalizedConfigString> save, ClickGuiPageInterface prevPage)
    {
        this.title = title;
        this.string = string;
        this.save = save;
        this.prevPage = prevPage;
    }
    
    @Override
    protected int count()
    {
        return this.string.getLanguages().size();
    }
    
    @Override
    protected List<Locale> getElements(int start, int limit)
    {
        return this.string.getLanguages().stream().sorted((a, b) -> a.toString().compareTo(b.toString())).skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    protected ClickGuiItem map(int line, int col, int index, Locale elm)
    {
        return new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Text), Messages.IconEdit, (p, s, g) -> onEdit(p, s, g, elm), elm.toString());
    }
    
    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[] {
            null,
            GuiServiceInterface.itemRefresh(this::onRefresh),
            this.itemPrevPage(),
            this.itemNextPage(),
            GuiServiceInterface.itemBack(this::onBack, Messages.IconBack),
            GuiServiceInterface.itemNew(this::onNew, Messages.IconCreate),
            null,
            null,
            GuiServiceInterface.itemCloseGui(),
        };
    }
    
    @Override
    public ClickGuiItem[][] getItems()
    {
        return ClickGuiPageInterface.withFillers(super.getItems(), 6);
    }
    
    /**
     * new language.
     * 
     * @param player
     *            player
     * @param session
     *            session
     * @param gui
     *            gui
     * @throws McException
     *             thrown on problems
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        player.nestAnvilGui(new QueryLocale(
            null,
            (l) -> this.onNew(player, session, gui, l),
            player.encodeMessage(Messages.CreateLocaleDescription, this.title)));
    }
    
    /**
     * new language.
     * 
     * @param player
     *            player
     * @param session
     *            session
     * @param gui
     *            gui
     * @param locale
     *            locale to be used.
     * @throws McException
     *             thrown on problems
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, Locale locale) throws McException
    {
        session.close();
        player.nestAnvilGui(new QueryText(
            "<text>", //$NON-NLS-1$
            null,
            (str2) -> this.onNew(player, session, gui, locale, str2),
            player.encodeMessage(Messages.CreateTextDescription, this.title, locale.toString()),
            false));
    }
    
    /**
     * new language.
     * 
     * @param player
     *            player
     * @param session
     *            session
     * @param gui
     *            gui
     * @param locale
     *            locale to be used.
     * @param content
     *            new content
     * @throws McException
     *             thrown on problems
     */
    private void onNew(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, Locale locale, String content) throws McException
    {
        this.string.setUserMessage(locale, content);
        player.nestClickGui(new SimpleClickGui(ClickGuis.List, this, 6));
        this.save.accept(this.string);
    }
    
    /**
     * edit language.
     * 
     * @param player
     *            player
     * @param session
     *            session
     * @param gui
     *            gui
     * @param locale
     *            locale to be edited
     */
    private void onEdit(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, Locale locale)
    {
        session.setNewPage(new LocalizedStringEditor(
            this.title,
            locale.toString(),
            this.string.getUnformattedUserMessage(locale),
            this.string.getUnformattedAdminMessage(locale),
            (s) ->
            {
                this.string.setUserMessage(locale, s);
                this.save.accept(this.string);
            },
            (s) ->
            {
                this.string.setAdminMessage(locale, s);
                this.save.accept(this.string);
            },
            this::onRefresh,
            (p, s, g) ->
            {
                this.string.setUserMessage(locale, null);
                this.string.setAdminMessage(locale, null);
                this.onRefresh(p, s, g);
            }));
    }
    
    /**
     * back.
     * 
     * @param player
     *            player
     * @param session
     *            session
     * @param gui
     *            gui
     */
    private void onBack(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(this.prevPage);
    }
    
    @Override
    public Serializable getPageName()
    {
        return this.title;
    }
    
    /**
     * Editor to create strings.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "gui.string_editor")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Back icon.
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment("back icon")
        IconBack,
        
        /**
         * Create icon.
         */
        @LocalizedMessage(defaultMessage = "New language")
        @MessageComment("Create icon (new language)")
        IconCreate,
        
        /**
         * Edit icon.
         */
        @LocalizedMessage(defaultMessage = "Edit %1$s")
        @MessageComment(value = "Edit icon", args = @Argument("locale name"))
        IconEdit,
        
        /**
         * Create new: locale description.
         */
        @LocalizedMessageList({ "Enter a new language for %1$s.", "Use 2 or 3 letter iso codes.", "For example: 'en' or 'de'." })
        @MessageComment(value = "Create new: locale description", args = @Argument("title"))
        CreateLocaleDescription,
        
        /**
         * Create new: text description.
         */
        @LocalizedMessageList({ "Enter a new user text for %1$s - %2$s.", "Use percent sign to create minecraft color codes.", "For example: '%%0' for black." })
        @MessageComment(value = "Create new: text description", args = { @Argument("title"), @Argument("locale") })
        CreateTextDescription,
        
    }
    
}
