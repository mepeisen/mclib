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

package de.minigameslib.mclib.api;

import de.minigameslib.mclib.api.config.ConfigColorData;
import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.config.ConfigVectorData;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * Base interface for values that can be validated.
 * 
 * @author mepeisen
 *
 */
public interface GenericValue
{
    
    /**
     * Returns the enumeration value name.
     * 
     * @return enumeration value name.
     */
    String name();
    
    /**
     * Checks if this configuration variable is an enum.
     * 
     * @return {@code true} if this is an enum
     */
    boolean isEnum();
    
    /**
     * Checks if this configuration variable is an enum.
     * 
     * @return {@code true} if this is an enum
     */
    boolean isJavaEnum();
    
    /**
     * Checks if this configuration variable is an enum list.
     * 
     * @return {@code true} if this is an enum list
     */
    boolean isEnumList();
    
    /**
     * Checks if this configuration variable is an enum list.
     * 
     * @return {@code true} if this is an enum list
     */
    boolean isJavaEnumList();
    
    /**
     * Checks if this configuration variable is a boolean.
     * 
     * @return {@code true} if this is a boolean
     */
    boolean isBoolean();
    
    /**
     * Checks if this configuration variable is a boolean list.
     * 
     * @return {@code true} if this is a boolean list
     */
    boolean isBooleanList();
    
    /**
     * Checks if this configuration variable is a byte.
     * 
     * @return {@code true} if this is a byte
     */
    boolean isByte();
    
    /**
     * Checks if this configuration variable is a byte list.
     * 
     * @return {@code true} if this is a byte list
     */
    boolean isByteList();
    
    /**
     * Checks if this configuration variable is a character.
     * 
     * @return {@code true} if this is a character
     */
    boolean isCharacter();
    
    /**
     * Checks if this configuration variable is a character list.
     * 
     * @return {@code true} if this is a character list
     */
    boolean isCharacterList();
    
    /**
     * Checks if this configuration variable is a color list.
     * 
     * @return {@code true} if this is a color list
     */
    boolean isColorList();
    
    /**
     * Checks if this configuration variable is a color.
     * 
     * @return {@code true} if this is a color
     */
    boolean isColor();
    
    /**
     * Checks if this configuration variable is a double list.
     * 
     * @return {@code true} if this is a double list
     */
    boolean isDoubleList();
    
    /**
     * Checks if this configuration variable is a double.
     * 
     * @return {@code true} if this is a double
     */
    boolean isDouble();
    
    /**
     * Checks if this configuration variable is a float list.
     * 
     * @return {@code true} if this is a float list
     */
    boolean isFloatList();
    
    /**
     * Checks if this configuration variable is a float.
     * 
     * @return {@code true} if this is a float
     */
    boolean isFloat();
    
    /**
     * Checks if this configuration variable is a int list.
     * 
     * @return {@code true} if this is a int list
     */
    boolean isIntList();
    
    /**
     * Checks if this configuration variable is a int.
     * 
     * @return {@code true} if this is a int
     */
    boolean isInt();
    
    /**
     * Checks if this configuration variable is an item stack list.
     * 
     * @return {@code true} if this is an item stack list
     */
    boolean isItemStackList();
    
    /**
     * Checks if this configuration variable is an item stack.
     * 
     * @return {@code true} if this is an item stack
     */
    boolean isItemStack();
    
    /**
     * Checks if this configuration variable is a section.
     * 
     * @return {@code true} if this is a section
     */
    boolean isSection();
    
    /**
     * Checks if this configuration variable is a long list.
     * 
     * @return {@code true} if this is a long list
     */
    boolean isLongList();
    
    /**
     * Checks if this configuration variable is a long.
     * 
     * @return {@code true} if this is a long
     */
    boolean isLong();
    
    /**
     * Checks if this configuration variable is an object list.
     * 
     * @return {@code true} if this is an object list
     */
    boolean isObjectList();
    
    /**
     * Checks if this configuration variable is an object.
     * 
     * @return {@code true} if this is an object
     */
    boolean isObject();
    
    /**
     * Checks if this configuration variable is a player list.
     * 
     * @return {@code true} if this is a player list
     */
    boolean isPlayerList();
    
    /**
     * Checks if this configuration variable is a player.
     * 
     * @return {@code true} if this is a player
     */
    boolean isPlayer();
    
    /**
     * Checks if this configuration variable is a short list.
     * 
     * @return {@code true} if this is a short list
     */
    boolean isShortList();
    
    /**
     * Checks if this configuration variable is a short.
     * 
     * @return {@code true} if this is a short
     */
    boolean isShort();
    
    /**
     * Checks if this configuration variable is a string list.
     * 
     * @return {@code true} if this is a string list
     */
    boolean isStringList();
    
    /**
     * Checks if this configuration variable is a string.
     * 
     * @return {@code true} if this is a string
     */
    boolean isString();
    
    /**
     * Checks if this configuration variable is a vector list.
     * 
     * @return {@code true} if this is a vector list
     */
    boolean isVectorList();
    
    /**
     * Checks if this configuration variable is a vector.
     * 
     * @return {@code true} if this is a vector
     */
    boolean isVector();
    
    /**
     * Returns the configuration path of this option.
     * 
     * @return configuration path
     */
    String path();
    
    /**
     * Checks if this configuration value is set.
     * 
     * @return {@code true} if this configuraiton value is set.
     */
    boolean isset();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     * @param <T>
     *            DataFragment object class
     */
    <T extends DataFragment> T getObject();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    byte getByte();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumeration class
     * @param clazz
     *            enumeration class
     * @return value.
     */
    <T extends EnumerationValue> T getEnum(Class<T> clazz);
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumeration class
     * @param clazz
     *            enumeration class
     * @return value.
     */
    <T extends Enum<?>> T getJavaEnum(Class<T> clazz);
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    ConfigColorData getColor();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    ConfigItemStackData getItemStack();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    ConfigVectorData getVector();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    McPlayerInterface getPlayer();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    char getCharacter();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    boolean getBoolean();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    boolean[] getBooleanList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumeration class
     * @param clazz
     *            enumeration class
     * @return value.
     */
    <T extends EnumerationValue> T[] getEnumList(Class<T> clazz);
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumeration class
     * @param clazz
     *            enumeration class
     * @return value.
     */
    <T extends Enum<?>> T[] getJavaEnumList(Class<T> clazz);
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    byte[] getByteList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    char[] getCharacterList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    double getDouble();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    float getFloat();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    double[] getDoubleList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    float[] getFloatList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    int getInt();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    short getShort();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    int[] getIntList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    long getLong();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    long[] getLongList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    short[] getShortList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    String getString();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    String[] getStringList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    ConfigVectorData[] getVectorList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    ConfigItemStackData[] getItemStackList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            DataFragment object class
     * @param clazz
     *            DataFragment object class
     * @return value.
     */
    <T extends DataFragment> T[] getObjectList(Class<T> clazz);
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    ConfigColorData[] getColorList();
    
    /**
     * Returns the value of given configuration value.
     * 
     * @return value.
     */
    McPlayerInterface[] getPlayerList();
    
    /**
     * Returns the comment.
     * 
     * @return config comment
     */
    String[] getComment();
    
    /**
     * Returns the enum class hint for enum values.
     * 
     * @return enum class hint
     */
    Class<? extends EnumerationValue> getEnumClass();
    
    /**
     * Returns the enum class hint for enum values.
     * 
     * @return enum class hint
     */
    Class<? extends Enum<?>> getJavaEnumClass();
    
}
