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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.McException;
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

/**
 * A list page being able to select enum values.
 * 
 * @author mepeisen
 */
public class JavaEnumEditor extends PagableClickGuiPage<Enum<?>> implements ClickGuiInterface
{
    
    /** save function. */
    private McConsumer<Enum<?>>            onSave;
    
    /** enum class. */
    private final Class<? extends Enum<?>> clazz;
    
    /** enumeration value. */
    private Enum<?>                        value;
    
    /** sorted list. */
    private List<? extends Enum<?>>        values;
    
    /**
     * Constructor.
     * 
     * @param clazz
     *            enum class
     * @param value
     *            current value
     * @param save
     *            handler to accept a new value
     */
    public JavaEnumEditor(Class<? extends Enum<?>> clazz, Enum<?> value, McConsumer<Enum<?>> save)
    {
        this.onSave = save;
        this.clazz = clazz;
        this.value = value;
        
        this.values = Arrays.stream(clazz.getEnumConstants())
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
    protected List<Enum<?>> getElements(int start, int limit)
    {
        return this.values.stream().skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    protected ClickGuiItem map(int line, int col, int index, Enum<?> elm)
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
     * Select handler.
     * 
     * @param player
     *            target player.
     * @param session
     *            gui session
     * @param guiInterface
     *            click gui
     * @param elm
     *            selected element
     * @throws McException
     *             thrown on validation errors
     */
    private void select(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface guiInterface, Enum<?> elm) throws McException
    {
        this.value = elm;
        this.onSave.accept(elm);
        session.refreshClickGui();
    }
    
    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[] {
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
    @LocalizedMessages(value = "gui.config.javaenum")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Back icon.
         */
        @LocalizedMessage(defaultMessage = "Back")
        @MessageComment("back icon")
        IconBack,
        
        /**
         * dialog title.
         */
        @LocalizedMessage(defaultMessage = "Edit enumeration value")
        @MessageComment("dialog title")
        Title,
        
        /**
         * enum value.
         */
        @LocalizedMessage(defaultMessage = "%1$s")
        @MessageComment(value = "enum value", args = { @MessageComment.Argument("enum value name") })
        EnumValue,
        
    }
    
}
