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
public interface EditableValue extends GenericValue
{
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setObject(DataFragment value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setObjectList(DataFragment[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setEnum(EnumerationValue value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setJavaEnum(Enum<?> value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setEnumList(EnumerationValue[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setJavaEnumList(Enum<?>[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setBoolean(boolean value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setBooleanList(boolean[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setByte(byte value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setByteList(byte[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setCharacter(char value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setCharacterList(char[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setColor(ConfigColorData value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setColorList(ConfigColorData[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setDouble(double value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setDoubleList(double[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setFloat(float value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setFloatList(float[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setInt(int value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setIntList(int[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setItemStack(ConfigItemStackData value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setItemStackList(ConfigItemStackData[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setLong(long value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setLongList(long[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setPlayer(McPlayerInterface value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setPlayerList(McPlayerInterface[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setShort(short value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setShortList(short[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setString(String value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setStringList(String[] value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setVector(ConfigVectorData value);
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     */
    void setVectorList(ConfigVectorData[] value);
    
    /**
     * Saves the configuration file this option belongs to.
     */
    void saveConfig();
    
    /**
     * Verifies the configuration this option belongs to.
     * 
     * @throws McException
     *             thrown if configuration contains invalid values
     */
    void verifyConfig() throws McException;
    
    /**
     * Rollback changes/ re-read config from file.
     */
    void rollbackConfig();
    
    /**
     * Default validation of this single configuration value; to validate the whole config file invoke {@link #verifyConfig()}.
     * 
     * @throws McException thrown if a validation fails
     */
    void validate() throws McException;
    
}
