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

import de.minigameslib.mclib.api.EditableValue;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.gui.AbstractListPage;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.impl.gui.AnvilGuis;
import de.minigameslib.mclib.impl.gui.ClickGuis;
import de.minigameslib.mclib.impl.gui.etc.LocalizedLinesEditLocale;
import de.minigameslib.mclib.impl.gui.etc.LocalizedLinesEditLocaleList;
import de.minigameslib.mclib.impl.gui.etc.LocalizedLinesList;
import de.minigameslib.mclib.impl.gui.etc.LocalizedStringEditor;
import de.minigameslib.mclib.impl.gui.etc.LocalizedStringList;

/**
 * Base class for configuration option editors.
 * 
 * @author mepeisen
 *
 */
public abstract class AbstractConfigOption
{
    
    /** the enum value. */
    private final EditableValue value;
    
    /**
     * Constructor.
     * 
     * @param value
     *            the value to be edited.
     */
    public AbstractConfigOption(EditableValue value)
    {
        this.value = value;
    }
    
    /**
     * returns the click gui item.
     * 
     * @param onChange
     *            function to invoke once the config value is changed; may be {@code null}
     * @param back
     *            function to display back page
     * @param home
     *            function to display home page
     * @param contextProvider
     *            runnable to setup context sensitive execution; may be {@code null}
     * @return click gui item; may be {@code null}
     * @throws McException
     *             thrown if there were problems creating an item.
     */
    public abstract ClickGuiItem getItem(Runnable onChange, GuiItemHandler back, GuiItemHandler home, McRunnable contextProvider) throws McException;
    
    /**
     * The option name.
     * 
     * @return option name
     */
    public String name()
    {
        return this.value.name();
    }
    
    /**
     * Returns the value to be edited.
     * 
     * @return the value
     */
    public EditableValue getValue()
    {
        return this.value;
    }
    
    /**
     * Calculate function by providing a context.
     * 
     * @param <T>
     *            return class
     * @param contextProvider
     *            runnable to setup context sensitive execution; may be {@code null}
     * @param supplier
     *            function to execute within context.
     * @return result the result from supplier.
     * @throws McException
     *             passed from supplier
     */
    protected <T> T calculate(McRunnable contextProvider, McSupplier<T> supplier) throws McException
    {
        if (contextProvider != null)
        {
            return McLibInterface.instance().calculateInNewContext(() ->
            {
                contextProvider.run();
                return supplier.get();
            });
        }
        return supplier.get();
    }
    
    /**
     * Run function by providing a context.
     * 
     * @param contextProvider
     *            runnable to setup context sensitive execution; may be {@code null}
     * @param runnable
     *            function to execute within context.
     * @throws McException
     *             passed from runnable
     */
    protected void run(McRunnable contextProvider, McRunnable runnable) throws McException
    {
        if (contextProvider == null)
        {
            runnable.run();
        }
        else
        {
            McLibInterface.instance().runInNewContext(() ->
            {
                contextProvider.run();
                runnable.run();
            });
        }
    }
    
    /**
     * Creates config value option for given value.
     * 
     * @param value
     *            config value that will be edited
     * @return config value option
     */
    public static AbstractConfigOption create(EditableValue value)
    {
        if (value.isBoolean())
        {
            return new BooleanConfigOption(value);
        }
        if (value.isBooleanList())
        {
            return new BooleanListConfigOption(value);
        }
        if (value.isByte())
        {
            return new ByteConfigOption(value);
        }
        if (value.isByteList())
        {
            return new ByteListConfigOption(value);
        }
        if (value.isCharacter())
        {
            return new CharacterConfigOption(value);
        }
        if (value.isCharacterList())
        {
            return new CharacterListConfigOption(value);
        }
        if (value.isColor())
        {
            return new ColorConfigOption(value);
        }
        if (value.isColorList())
        {
            return new ColorListConfigOption(value);
        }
        if (value.isDouble())
        {
            return new DoubleConfigOption(value);
        }
        if (value.isDoubleList())
        {
            return new DoubleListConfigOption(value);
        }
        if (value.isEnum())
        {
            return new EnumConfigOption(value);
        }
        if (value.isEnumList())
        {
            return new EnumListConfigOption(value);
        }
        if (value.isJavaEnum())
        {
            return new JavaEnumConfigOption(value);
        }
        if (value.isJavaEnumList())
        {
            return new JavaEnumListConfigOption(value);
        }
        if (value.isFloat())
        {
            return new FloatConfigOption(value);
        }
        if (value.isFloatList())
        {
            return new FloatListConfigOption(value);
        }
        if (value.isInt())
        {
            return new IntConfigOption(value);
        }
        if (value.isIntList())
        {
            return new IntListConfigOption(value);
        }
        if (value.isItemStack())
        {
            return new ItemStackConfigOption(value);
        }
        if (value.isItemStackList())
        {
            return new ItemStackListConfigOption(value);
        }
        if (value.isLong())
        {
            return new LongConfigOption(value);
        }
        if (value.isLongList())
        {
            return new LongListConfigOption(value);
        }
        // if (value.isObject())
        // {
        // // TODO support object editor
        // return new ObjectConfigOption(value);
        // }
        // if (value.isObjectList())
        // {
        // // TODO support object editor
        // return new ObjectListConfigOption(value);
        // }
        if (value.isPlayer())
        {
            return new PlayerConfigOption(value);
        }
        if (value.isPlayerList())
        {
            return new PlayerListConfigOption(value);
        }
        // if (value.isSection())
        // {
        // // TODO support section editor
        // }
        if (value.isShort())
        {
            return new ShortConfigOption(value);
        }
        if (value.isShortList())
        {
            return new ShortListConfigOption(value);
        }
        if (value.isString())
        {
            return new StringConfigOption(value);
        }
        if (value.isStringList())
        {
            return new StringListConfigOption(value);
        }
        if (value.isVector())
        {
            return new VectorConfigOption(value);
        }
        if (value.isVectorList())
        {
            return new VectorListConfigOption(value);
        }
        
        // returns a dummy
        return new AbstractConfigOption(value) {
            
            @Override
            public ClickGuiItem getItem(Runnable onChange, GuiItemHandler back, GuiItemHandler home, McRunnable contextProvider)
            {
                return null;
            }
        };
    }
    
    /**
     * Common messages within gui.
     * 
     * @author mepeisen
     */
    @LocalizedMessages("gui.config")
    @ChildEnum({
        AnvilGuis.class,
        ClickGuis.class,
        AbstractListPage.Messages.class,
        ColorEditor.Messages.class,
        EnumEditor.Messages.class,
        LocalizedLinesEditLocale.Messages.class,
        LocalizedLinesEditLocaleList.Messages.class,
        LocalizedLinesList.Messages.class,
        LocalizedStringEditor.Messages.class,
        LocalizedStringList.Messages.class })
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * gui config name.
         * 
         * <p>
         * No Arguments
         * </p>
         */
        @LocalizedMessage(defaultMessage = "%1$s")
        @MessageComment(value = { "gui config name" }, args = { @MessageComment.Argument("gui config path") })
        ConfigName,
        
        /**
         * gui config name.
         * 
         * <p>
         * No Arguments
         * </p>
         */
        @LocalizedMessage(defaultMessage = "%1$s - value: %2$d")
        @MessageComment(value = { "gui config name" }, args = { @MessageComment.Argument("gui config path"), @MessageComment.Argument("numeric value") })
        ConfigNameWithDecimalValue,
        
        /**
         * gui config name.
         * 
         * <p>
         * No Arguments
         * </p>
         */
        @LocalizedMessage(defaultMessage = "%1$s - value: %2$s")
        @MessageComment(value = { "gui config name" }, args = { @MessageComment.Argument("gui config path"), @MessageComment.Argument("string value") })
        ConfigNameWithStringValue,
        
        /**
         * gui config name.
         * 
         * <p>
         * No Arguments
         * </p>
         */
        @LocalizedMessage(defaultMessage = "%1$s - value: %2$f")
        @MessageComment(value = { "gui config name" }, args = { @MessageComment.Argument("gui config path"), @MessageComment.Argument("float value") })
        ConfigNameWithFloatValue,
        
        /**
         * invalid numeric format.
         */
        @LocalizedMessage(defaultMessage = "The text you entered is not a valid number", severity = MessageSeverityType.Error)
        @MessageComment(value = { "invalid numeric format" })
        InvalidNumericFormat,
        
        /**
         * number out of range.
         */
        @LocalizedMessage(defaultMessage = "The number you entered is out of range (min %1$d, max %2$d)", severity = MessageSeverityType.Error)
        @MessageComment(value = { "number out of range" }, args = { @MessageComment.Argument("min value"), @MessageComment.Argument("max value") })
        NumberOutOfRange,
        
        /**
         * text not a character.
         */
        @LocalizedMessage(defaultMessage = "The text you entered is not a single character", severity = MessageSeverityType.Error)
        @MessageComment(value = { "text not a character" })
        TextNotACharacter,
        
        /**
         * not implemented.
         */
        @LocalizedMessage(defaultMessage = "Editor not implemented", severity = MessageSeverityType.Error)
        @MessageComment(value = { "not implemented" })
        NotImplemented,
    }
    
}
