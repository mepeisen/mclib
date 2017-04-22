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
import java.util.stream.Collectors;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.PagableClickGuiPage;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.gui.ClickGuis;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * A list page being able to select enum values
 * 
 * @author mepeisen
 */
public class EnumEditor extends PagableClickGuiPage<EnumerationValue> implements ClickGuiInterface
{

    /** save function */
    private McConsumer<EnumerationValue> onSave;
    
    /** enum class */
    private final Class<? extends EnumerationValue> clazz;
    
    /** enumeration value. */
    private EnumerationValue value;

    /** sorted list */
    private List<? extends EnumerationValue> values;

    /**
     * @param clazz
     * @param value   
     * @param save 
     */
    public EnumEditor(Class<? extends EnumerationValue> clazz, EnumerationValue value, McConsumer<EnumerationValue> save)
    {   
        this.onSave = save;
        this.clazz = clazz;
        this.value = value;
        
        this.values = EnumServiceInterface.instance().getEnumValues(this.clazz).stream()
                .sorted((a, b) -> a.name().compareTo(b.name())).collect(Collectors.toList());
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title;
    }

    @Override
    protected int count()
    {
        return this.values.size();
    }

    @Override
    protected List<EnumerationValue> getElements(int start, int limit)
    {
        return this.values.stream().skip(start).limit(limit).collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, EnumerationValue elm)
    {
        final ItemStack stack = ItemServiceInterface.instance().createItem(CommonItems.App_Brush);
        if (elm == this.value)
        {
            final ItemMeta meta = stack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addEnchant(Enchantment.LURE, 1, false);
            stack.setItemMeta(meta);
        }
        return new ClickGuiItem(stack, Messages.EnumValue, (p, s, g) -> this.select(p, s, g, elm), elm.name());
    }
    
    /**
     * Select handler
     * @param player
     * @param session
     * @param guiInterface
     * @param elm
     * @throws McException 
     */
    private void select(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface, EnumerationValue elm) throws McException
    {
        this.value = elm;
        this.onSave.accept(elm);
        session.refreshClickGui();
    }
    
    /**
     * prev page icon
     * @return prev page icon
     */
    public ClickGuiItem itemPrevPage()
    {
        return this.page() > 1 ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Previous), Messages.IconPreviousPage, this::onPrevPage) : null;
    }
    
    /**
     * next page icon
     * @return next page icon
     */
    public ClickGuiItem itemNextPage()
    {
        return this.page() < this.totalPages() ? new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Next), Messages.IconNextPage, this::onNextPage) : null;
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                null,
                null,
                this.itemPrevPage(),
                this.itemNextPage(),
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Back), Messages.IconBack, (c, s, g) -> s.close()),
                null,
                null,
                null,
                null
        };
    }

    @Override
    public ClickGuiId getUniqueId()
    {
        return ClickGuis.Enum;
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

    /**
     * Editor to create colors.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "gui.config.enum")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Back icon
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment("back icon")
        IconBack,
        
        /**
         * dialog title
         */
        @LocalizedMessage(defaultMessage = "Edit enumeration value")
        @MessageComment("dialog title")
        Title,
        
        /**
         * enum value
         */
        @LocalizedMessage(defaultMessage = "%1$s")
        @MessageComment(value = "enum value", args={@MessageComment.Argument("enum value name")})
        EnumValue,

        /**
         * prev page icon
         */
        @LocalizedMessage(defaultMessage = "Previous page")
        @MessageComment(value = "prev page icon")
        IconPreviousPage,

        /**
         * next page icon
         */
        @LocalizedMessage(defaultMessage = "Next page")
        @MessageComment(value = "next page icon")
        IconNextPage,
        
    }
    
}
