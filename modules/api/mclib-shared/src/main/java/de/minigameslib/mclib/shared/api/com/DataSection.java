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

package de.minigameslib.mclib.shared.api.com;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base interface for data sections.
 * 
 * <p>
 * Data sections are able to store and receive data by keys. This interface is inspired by bukkits ConfigurationSection.
 * </p>
 * 
 * @author mepeisen
 */
public interface DataSection
{
    
    /**
     * Returns the keys of data values.
     * 
     * @param deep
     * @return keys of data
     */
    Set<String> getKeys(boolean deep);
    
    /**
     * Returns all data values.
     * 
     * @param deep
     * @return data values.
     */
    Map<String, Object> getValues(boolean deep);
    
    /**
     * Checks if the section contains given key.
     * 
     * @param key
     * @return {@code true} if key is contained.
     */
    boolean contains(String key);
    
    /**
     * Returns the current path of this section.
     * 
     * @return path name; empty string if this is the root section.
     */
    String getCurrentPath();
    
    /**
     * Returns the name of this section within parent section.
     * 
     * @return section name; empty string if this is the root section.
     */
    String getName();
    
    /**
     * Returns the root section.
     * 
     * @return root section.
     */
    DataSection getRoot();
    
    /**
     * Returns the parent section
     * 
     * @return parent section or {@code null} if this is the root section.
     */
    DataSection getParent();
    
    /**
     * Returns the object with given key.
     * 
     * @param key
     * @return value or {@code null} if no value was found.
     */
    Object get(String key);
    
    /**
     * Returns the object with given key.
     * 
     * @param key
     * @param defaultValue
     * @return value or {@code defaultValue} if no value was found.
     */
    Object get(String key, Object defaultValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    void set(String key, Object newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    void setPrimitiveList(String key, List<?> newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    <T> void setPrimitiveMapList(String key, List<Map<String, T>> newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    <T extends DataFragment> void setFragmentList(String key, List<T> newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    void setSection(String key, DataSection newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    <T extends DataFragment> void setFragmentMapList(String key, List<Map<String, T>> newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    void setPrimitiveMap(String key, Map<String, ?> newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    <T> void setPrimitiveListMap(String key, Map<String, List<T>> newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    <T extends DataFragment> void setFragmentMap(String key, Map<String, T> newValue);
    
    /**
     * Sets object for given key
     * 
     * @param key
     * @param newValue
     */
    <T extends DataFragment> void setFragmentListMap(String key, Map<String, List<T>> newValue);
    
    /**
     * returns a section by key; creates it on demand.
     * @param key
     * @return data section.
     */
    DataSection createSection(String key);
    
    /**
     * returns a section by key with given values; creates it on demand.
     * @param key
     * @param values
     * @return data section.
     */
    DataSection createSection(String key, Map<String, ?> values);
    
    /**
     * Returns string value by key
     * @param key
     * @return value
     */
    String getString(String key);
    
    /**
     * Returns string value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    String getString(String key, String defaultValue);
    
    /**
     * Checks if given key has a string value.
     * @param key
     * @return true if key has a string value
     */
    boolean isString(String key);
    
    /**
     * Returns int value by key
     * @param key
     * @return value
     */
    int getInt(String key);
    
    /**
     * Returns int value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    int getInt(String key, int defaultValue);
    
    /**
     * Checks if given key has an int value.
     * @param key
     * @return true if key has an int value
     */
    boolean isInt(String key);
    
    /**
     * Returns byte value by key
     * @param key
     * @return value
     */
    byte getByte(String key);
    
    /**
     * Returns byte value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    byte getByte(String key, byte defaultValue);
    
    /**
     * Checks if given key has a byte value.
     * @param key
     * @return true if key has a byte value
     */
    boolean isByte(String key);
    
    /**
     * Returns short value by key
     * @param key
     * @return value
     */
    short getShort(String key);
    
    /**
     * Returns short value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    short getShort(String key, short defaultValue);
    
    /**
     * Checks if given key has a short value.
     * @param key
     * @return true if key has a short value
     */
    boolean isShort(String key);
    
    /**
     * Returns char value by key
     * @param key
     * @return value
     */
    char getCharacter(String key);
    
    /**
     * Returns char value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    char getCharacter(String key, char defaultValue);
    
    /**
     * Checks if given key has a character value.
     * @param key
     * @return true if key has a character value
     */
    boolean isCharacter(String key);
    
    /**
     * Returns date time value by key
     * @param key
     * @return value
     */
    LocalDateTime getDateTime(String key);
    
    /**
     * Returns date time value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    LocalDateTime getDateTime(String key, LocalDateTime defaultValue);
    
    /**
     * Checks if given key has a date time value.
     * @param key
     * @return true if key has a date time value
     */
    boolean isDateTime(String key);
    
    /**
     * Returns date value by key
     * @param key
     * @return value
     */
    LocalDate getDate(String key);
    
    /**
     * Returns date value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    LocalDate getDate(String key, LocalDate defaultValue);
    
    /**
     * Checks if given key has a date value.
     * @param key
     * @return true if key has a date value
     */
    boolean isDate(String key);
    
    /**
     * Returns time value by key
     * @param key
     * @return value
     */
    LocalTime getTime(String key);
    
    /**
     * Returns time value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    LocalTime getTime(String key, LocalTime defaultValue);
    
    /**
     * Checks if given key has a time value.
     * @param key
     * @return true if key has a time value
     */
    boolean isTime(String key);
    
    /**
     * Returns boolean value by key
     * @param key
     * @return value
     */
    boolean getBoolean(String key);
    
    /**
     * Returns boolean value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    boolean getBoolean(String key, boolean defaultValue);
    
    /**
     * Checks if given key has a boolean value.
     * @param key
     * @return true if key has a boolean value
     */
    boolean isBoolean(String key);
    
    /**
     * Returns double value by key
     * @param key
     * @return value
     */
    double getDouble(String key);
    
    /**
     * Returns double value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    double getDouble(String key, double defaultValue);
    
    /**
     * Checks if given key has a double value.
     * @param key
     * @return true if key has a double value
     */
    boolean isDouble(String key);
    
    /**
     * Returns float value by key
     * @param key
     * @return value
     */
    float getFloat(String key);
    
    /**
     * Returns float value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    float getFloat(String key, float defaultValue);
    
    /**
     * Checks if given key has a float value.
     * @param key
     * @return true if key has a float value
     */
    boolean isFloat(String key);
    
    /**
     * Returns long value by key
     * @param key
     * @return value
     */
    long getLong(String key);
    
    /**
     * Returns long value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    long getLong(String key, long defaultValue);
    
    /**
     * Checks if given key has a long value.
     * @param key
     * @return true if key has a long value
     */
    boolean isLong(String key);
    
    /**
     * Returns list value by key
     * @param key
     * @return value
     */
    List<?> getList(String key);
    
    /**
     * Returns list value with default by key
     * @param key
     * @param defaultValue
     * @return value
     */
    List<?> getList(String key, List<?> defaultValue);
    
    /**
     * Checks if given key has a list value.
     * @param key
     * @return true if key has a list value
     */
    boolean isList(String key);
    
    /**
     * Returns string list value by key
     * @param key
     * @return value
     */
    List<String> getStringList(String key);
    
    /**
     * Returns int list value by key
     * @param key
     * @return value
     */
    List<Integer> getIntegerList(String key);
    
    /**
     * Returns bool list value by key
     * @param key
     * @return value
     */
    List<Boolean> getBooleanList(String key);
    
    /**
     * Returns double list value by key
     * @param key
     * @return value
     */
    List<Double> getDoubleList(String key);
    
    /**
     * Returns float list value by key
     * @param key
     * @return value
     */
    List<Float> getFloatList(String key);
    
    /**
     * Returns long list value by key
     * @param key
     * @return value
     */
    List<Long> getLongList(String key);
    
    /**
     * Returns byte list value by key
     * @param key
     * @return value
     */
    List<Byte> getByteList(String key);
    
    /**
     * Returns character list value by key
     * @param key
     * @return value
     */
    List<Character> getCharacterList(String key);
    
    /**
     * Returns short list value by key
     * @param key
     * @return value
     */
    List<Short> getShortList(String key);
    
    /**
     * Returns vector list value by key
     * @param key
     * @return value
     */
    List<VectorDataFragment> getVectorList(String key);
    
    /**
     * Returns player list value by key
     * @param key
     * @return value
     */
    List<PlayerDataFragment> getPlayerList(String key);
    
    /**
     * Returns item list value by key
     * @param key
     * @return value
     */
    List<ItemStackDataFragment> getItemList(String key);
    
    /**
     * Returns color list value by key
     * @param key
     * @return value
     */
    List<ColorData> getColorList(String key);
    
    /**
     * Returns map list value by key
     * @param key
     * @return value
     */
    List<Map<String, ?>> getMapList(String key);
    
    /**
     * Returns map value by key
     * @param key
     * @return value
     */
    Map<String, ?> getMap(String key);
    
    /**
     * Returns map value by key
     * @param key
     * @return value
     */
    Map<String, List<?>> getListMap(String key);
    
    /**
     * Returns map value by key
     * @param clazz 
     * @param key
     * @return value
     */
    <T extends DataFragment> Map<String, T> getFragmentMap(Class<T> clazz, String key);
    
    /**
     * Returns map list value by key
     * @param clazz 
     * @param key
     * @return value
     */
    <T extends DataFragment> List<Map<String, T>> getFragmentMapList(Class<T> clazz, String key);
    
    /**
     * Returns list map value by key
     * @param clazz 
     * @param key
     * @return value
     */
    <T extends DataFragment> Map<String, List<T>> getFragmentListMap(Class<T> clazz, String key);
    
    /**
     * Returns list of fragments
     * @param clazz
     * @param key
     * @return value
     */
    <T extends DataFragment> List<T> getFragmentList(Class<T> clazz, String key);
    
    /**
     * returns vector by key
     * @param key
     * @return value
     */
    VectorDataFragment getVector(String key);
    
    /**
     * returns vector by key and default value
     * @param key
     * @param defaultValue
     * @return value
     */
    VectorDataFragment getVector(String key, VectorDataFragment defaultValue);
    
    /**
     * Checks if given key has a vector value.
     * @param key
     * @return true if key has a vector value
     */
    boolean isVectorData(String key);
    
    /**
     * returns player by key
     * @param key
     * @return value
     */
    PlayerDataFragment getPlayer(String key);
    
    /**
     * returns vector by key and default value
     * @param key
     * @param defaultValue
     * @return value
     */
    PlayerDataFragment getPlayer(String key, PlayerDataFragment defaultValue);
    
    /**
     * Checks if given key has a player value.
     * @param key
     * @return true if key has a player value
     */
    boolean isPlayer(String key);
    
    /**
     * returns item stack by key
     * @param key
     * @return value
     */
    ItemStackDataFragment getItemStack(String key);
    
    /**
     * returns item stack by key and default value
     * @param key
     * @param defaultValue
     * @return value
     */
    ItemStackDataFragment getItemStack(String key, ItemStackDataFragment defaultValue);
    
    /**
     * Checks if given key has an item stack value.
     * @param key
     * @return true if key has an item stack value
     */
    boolean isItemStack(String key);
    
    /**
     * returns color by key
     * @param key
     * @return value
     */
    ColorData getColor(String key);
    
    /**
     * returns color by key and default value
     * @param key
     * @param defaultValue
     * @return value
     */
    ColorData getColor(String key, ColorData defaultValue);
    
    /**
     * Checks if given key has a color value.
     * @param key
     * @return true if key has a color value
     */
    boolean isColor(String key);
    
    /**
     * returns fragment by key
     * @param clazz fragment class
     * @param key
     * @return value
     */
    <T extends DataFragment> T getFragment(Class<T> clazz, String key);
    
    /**
     * Checks if the given path can be read by given fragment class
     * @param clazz fragment class
     * @param key
     * @return true if the given key is a valid fragment of given type.
     */
    <T extends DataFragment> boolean isFragment(Class<T> clazz, String key);
    
    /**
     * returns fragment by key and default value
     * @param clazz fragment class
     * @param key
     * @param defaultValue
     * @return value
     */
    <T extends DataFragment> T getFragment(Class<T> clazz, String key, T defaultValue);
    
    /**
     * Returns the sub section with given key
     * @param key
     * @return sub section.
     */
    DataSection getSection(String key);
    
    /**
     * Checks if given key is a sub section.
     * @param key
     * @return sub section.
     */
    boolean isSection(String key);
    
}
