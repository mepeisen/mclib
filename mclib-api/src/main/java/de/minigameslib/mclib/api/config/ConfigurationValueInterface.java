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

package de.minigameslib.mclib.api.config;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Color;

import de.minigameslib.mclib.api.EditableValue;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.validate.ValidateFMax;
import de.minigameslib.mclib.api.validate.ValidateFMin;
import de.minigameslib.mclib.api.validate.ValidateIsset;
import de.minigameslib.mclib.api.validate.ValidateLMax;
import de.minigameslib.mclib.api.validate.ValidateLMin;
import de.minigameslib.mclib.api.validate.ValidateListMax;
import de.minigameslib.mclib.api.validate.ValidateListMin;
import de.minigameslib.mclib.api.validate.ValidateStrMax;
import de.minigameslib.mclib.api.validate.ValidateStrMin;
import de.minigameslib.mclib.api.validate.Validator;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * An interface for enumerations that represent entries in configuration files.
 * 
 * @author mepeisen
 */
public interface ConfigurationValueInterface extends EnumerationValue, EditableValue
{
    
    @Override
    default boolean isEnum()
    {
        return ConfigurationTool.isType(this, ConfigurationEnum.class);
    }
    
    @Override
    default boolean isJavaEnum()
    {
        return ConfigurationTool.isType(this, ConfigurationJavaEnum.class);
    }
    
    @Override
    default boolean isEnumList()
    {
        return ConfigurationTool.isType(this, ConfigurationEnumList.class);
    }
    
    @Override
    default boolean isJavaEnumList()
    {
        return ConfigurationTool.isType(this, ConfigurationJavaEnumList.class);
    }
    
    @Override
    default boolean isBoolean()
    {
        return ConfigurationTool.isType(this, ConfigurationBool.class);
    }
    
    @Override
    default boolean isBooleanList()
    {
        return ConfigurationTool.isType(this, ConfigurationBoolList.class);
    }
    
    @Override
    default boolean isByte()
    {
        return ConfigurationTool.isType(this, ConfigurationByte.class);
    }
    
    @Override
    default boolean isByteList()
    {
        return ConfigurationTool.isType(this, ConfigurationByteList.class);
    }
    
    @Override
    default boolean isCharacter()
    {
        return ConfigurationTool.isType(this, ConfigurationCharacter.class);
    }
    
    @Override
    default boolean isCharacterList()
    {
        return ConfigurationTool.isType(this, ConfigurationCharacterList.class);
    }
    
    @Override
    default boolean isColorList()
    {
        return ConfigurationTool.isType(this, ConfigurationColorList.class);
    }
    
    @Override
    default boolean isColor()
    {
        return ConfigurationTool.isType(this, ConfigurationColor.class);
    }
    
    @Override
    default boolean isDoubleList()
    {
        return ConfigurationTool.isType(this, ConfigurationDoubleList.class);
    }
    
    @Override
    default boolean isDouble()
    {
        return ConfigurationTool.isType(this, ConfigurationDouble.class);
    }
    
    @Override
    default boolean isFloatList()
    {
        return ConfigurationTool.isType(this, ConfigurationFloatList.class);
    }
    
    @Override
    default boolean isFloat()
    {
        return ConfigurationTool.isType(this, ConfigurationFloat.class);
    }
    
    @Override
    default boolean isIntList()
    {
        return ConfigurationTool.isType(this, ConfigurationIntList.class);
    }
    
    @Override
    default boolean isInt()
    {
        return ConfigurationTool.isType(this, ConfigurationInt.class);
    }
    
    @Override
    default boolean isItemStackList()
    {
        return ConfigurationTool.isType(this, ConfigurationItemStackList.class);
    }
    
    @Override
    default boolean isItemStack()
    {
        return ConfigurationTool.isType(this, ConfigurationItemStack.class);
    }
    
    @Override
    default boolean isSection()
    {
        return ConfigurationTool.isType(this, ConfigurationSection.class);
    }
    
    @Override
    default boolean isLongList()
    {
        return ConfigurationTool.isType(this, ConfigurationLongList.class);
    }
    
    @Override
    default boolean isLong()
    {
        return ConfigurationTool.isType(this, ConfigurationLong.class);
    }
    
    @Override
    default boolean isObjectList()
    {
        return ConfigurationTool.isType(this, ConfigurationObjectList.class);
    }
    
    @Override
    default boolean isObject()
    {
        return ConfigurationTool.isType(this, ConfigurationObject.class);
    }
    
    @Override
    default boolean isPlayerList()
    {
        return ConfigurationTool.isType(this, ConfigurationPlayerList.class);
    }
    
    @Override
    default boolean isPlayer()
    {
        return ConfigurationTool.isType(this, ConfigurationPlayer.class);
    }
    
    @Override
    default boolean isShortList()
    {
        return ConfigurationTool.isType(this, ConfigurationShortList.class);
    }
    
    @Override
    default boolean isShort()
    {
        return ConfigurationTool.isType(this, ConfigurationShort.class);
    }
    
    @Override
    default boolean isStringList()
    {
        return ConfigurationTool.isType(this, ConfigurationStringList.class);
    }
    
    @Override
    default boolean isString()
    {
        return ConfigurationTool.isType(this, ConfigurationString.class);
    }
    
    @Override
    default boolean isVectorList()
    {
        return ConfigurationTool.isType(this, ConfigurationVectorList.class);
    }
    
    @Override
    default boolean isVector()
    {
        return ConfigurationTool.isType(this, ConfigurationVector.class);
    }
    
    @Override
    default String path()
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(this.name());
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            
            {
                final ConfigurationEnum config = field.getAnnotation(ConfigurationEnum.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationEnumList config = field.getAnnotation(ConfigurationEnumList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationJavaEnum config = field.getAnnotation(ConfigurationJavaEnum.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationJavaEnumList config = field.getAnnotation(ConfigurationJavaEnumList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationBool config = field.getAnnotation(ConfigurationBool.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationBoolList config = field.getAnnotation(ConfigurationBoolList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationByte config = field.getAnnotation(ConfigurationByte.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationByteList config = field.getAnnotation(ConfigurationByteList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationCharacter config = field.getAnnotation(ConfigurationCharacter.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationCharacterList config = field.getAnnotation(ConfigurationCharacterList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationColor config = field.getAnnotation(ConfigurationColor.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationColorList config = field.getAnnotation(ConfigurationColorList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationDouble config = field.getAnnotation(ConfigurationDouble.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationDoubleList config = field.getAnnotation(ConfigurationDoubleList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationFloat config = field.getAnnotation(ConfigurationFloat.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationFloatList config = field.getAnnotation(ConfigurationFloatList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationInt config = field.getAnnotation(ConfigurationInt.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationIntList config = field.getAnnotation(ConfigurationIntList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationItemStack config = field.getAnnotation(ConfigurationItemStack.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationItemStackList config = field.getAnnotation(ConfigurationItemStackList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationLong config = field.getAnnotation(ConfigurationLong.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationLongList config = field.getAnnotation(ConfigurationLongList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationObject config = field.getAnnotation(ConfigurationObject.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationObjectList config = field.getAnnotation(ConfigurationObjectList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationPlayer config = field.getAnnotation(ConfigurationPlayer.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationPlayerList config = field.getAnnotation(ConfigurationPlayerList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationSection config = field.getAnnotation(ConfigurationSection.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.value().length() == 0 ? this.name() : config.value());
                    return path;
                }
            }
            {
                final ConfigurationShort config = field.getAnnotation(ConfigurationShort.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationShortList config = field.getAnnotation(ConfigurationShortList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationString config = field.getAnnotation(ConfigurationString.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationStringList config = field.getAnnotation(ConfigurationStringList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationVector config = field.getAnnotation(ConfigurationVector.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            {
                final ConfigurationVectorList config = field.getAnnotation(ConfigurationVectorList.class);
                if (config != null)
                {
                    final String path = configs.path() + '.' + (config.name().length() == 0 ? this.name() : config.name());
                    return path;
                }
            }
            throw new IllegalStateException("Invalid configuration option"); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    @Override
    default boolean isset()
    {
        final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
        final ConfigServiceInterface lib = ConfigServiceInterface.instance();
        final ConfigInterface minigame = lib.getConfigFromCfg(this);
        
        return minigame.getConfig(configs.file()).contains(this.path());
    }
    
    /**
     * Checks if this configuration value is set.
     * 
     * @param path
     *            sub path of configuration section
     * @return {@code true} if this configuraiton value is set.
     */
    default boolean isset(String path)
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(this.name());
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigServiceInterface lib = ConfigServiceInterface.instance();
            final ConfigInterface minigame = lib.getConfigFromCfg(this);
            final ConfigurationSection config = field.getAnnotation(ConfigurationSection.class);
            if (config != null)
            {
                final String mpath = configs.path() + '.' + (config.value().length() == 0 ? this.name() : config.value()) + '.' + path;
                return minigame.getConfig(configs.file()).contains(mpath);
            }
            throw new IllegalStateException("Invalid configuration option"); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setObject(DataFragment value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            DataSection section = minigame.getConfig(configs.file()).getSection(path);
            if (section == null)
            {
                section = minigame.getConfig(configs.file()).createSection(path);
            }
            value.write(section);
        });
    }
    
    @Override
    default void setObject(DataFragment value)
    {
        ConfigurationTool.consume(this, ConfigurationObject.class, ConfigurationTool.objectPath(), (val, configs, config, lib, minigame, path) ->
        {
            DataSection section = minigame.getConfig(configs.file()).getSection(path);
            if (section == null)
            {
                section = minigame.getConfig(configs.file()).createSection(path);
            }
            value.write(section);
        });
    }
    
    @Override
    default void setObjectList(DataFragment[] value)
    {
        ConfigurationTool.consumeList(this, ConfigurationObjectList.class, ConfigurationTool.objectListPath(), value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            final DataSection configurationSection = section.createSection(path);
            element.write(configurationSection);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setObjectList(DataFragment[] value, String subpath)
    {
        ConfigurationTool.consumeList(this, subpath, value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            final DataSection section2 = section.createSection(path);
            element.write(section2);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setEnum(EnumerationValue value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setEnum(EnumerationValue value)
    {
        ConfigurationTool.consume(this, ConfigurationEnum.class, ConfigurationTool.enumPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setJavaEnum(Enum<?> value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setJavaEnum(Enum<?> value)
    {
        ConfigurationTool.consume(this, ConfigurationJavaEnum.class, ConfigurationTool.javaEnumPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setEnumList(EnumerationValue[] value)
    {
        final List<EnumerationValue> list = Arrays.asList(value);
        ConfigurationTool.consume(this, ConfigurationEnumList.class, ConfigurationTool.enumListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setEnumList(EnumerationValue[] value, String subpath)
    {
        final List<EnumerationValue> list = Arrays.asList(value);
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    @Override
    default void setJavaEnumList(Enum<?>[] value)
    {
        final List<Enum<?>> list = Arrays.asList(value);
        ConfigurationTool.consume(this, ConfigurationJavaEnumList.class, ConfigurationTool.javaEnumListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setJavaEnumList(Enum<?>[] value, String subpath)
    {
        final List<Enum<?>> list = Arrays.asList(value);
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setBoolean(boolean value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Boolean.valueOf(value));
        });
    }
    
    @Override
    default void setBoolean(boolean value)
    {
        ConfigurationTool.consume(this, ConfigurationBool.class, ConfigurationTool.boolPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Boolean.valueOf(value));
        });
    }
    
    @Override
    default void setBooleanList(boolean[] value)
    {
        final List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationBoolList.class, ConfigurationTool.boolListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setBooleanList(boolean[] value, String subpath)
    {
        final List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setByte(byte value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Byte.valueOf(value));
        });
    }
    
    @Override
    default void setByte(byte value)
    {
        ConfigurationTool.consume(this, ConfigurationByte.class, ConfigurationTool.bytePath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Byte.valueOf(value));
        });
    }
    
    @Override
    default void setByteList(byte[] value)
    {
        final List<Byte> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationByteList.class, ConfigurationTool.byteListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setByteList(byte[] value, String subpath)
    {
        final List<Byte> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setCharacter(char value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, String.valueOf(value));
        });
    }
    
    @Override
    default void setCharacter(char value)
    {
        ConfigurationTool.consume(this, ConfigurationCharacter.class, ConfigurationTool.charPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, String.valueOf(value));
        });
    }
    
    @Override
    default void setCharacterList(char[] value)
    {
        final List<Character> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationCharacterList.class, ConfigurationTool.charListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setCharacterList(char[] value, String subpath)
    {
        final List<Character> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setColor(ConfigColorData value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setColor(ConfigColorData value)
    {
        ConfigurationTool.consume(this, ConfigurationColor.class, ConfigurationTool.colorPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setColorList(ConfigColorData[] value)
    {
        ConfigurationTool.consumeList(this, ConfigurationColorList.class, ConfigurationTool.colorListPath(), value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setColorList(ConfigColorData[] value, String subpath)
    {
        ConfigurationTool.consumeList(this, subpath, value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setDouble(double value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Double.valueOf(value));
        });
    }
    
    @Override
    default void setDouble(double value)
    {
        ConfigurationTool.consume(this, ConfigurationDouble.class, ConfigurationTool.doublePath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Double.valueOf(value));
        });
    }
    
    @Override
    default void setDoubleList(double[] value)
    {
        final List<Double> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationDoubleList.class, ConfigurationTool.doubleListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setDoubleList(double[] value, String subpath)
    {
        final List<Double> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setFloat(float value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Float.valueOf(value));
        });
    }
    
    @Override
    default void setFloat(float value)
    {
        ConfigurationTool.consume(this, ConfigurationFloat.class, ConfigurationTool.floatPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Float.valueOf(value));
        });
    }
    
    @Override
    default void setFloatList(float[] value)
    {
        final List<Float> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationFloatList.class, ConfigurationTool.floatListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setFloatList(float[] value, String subpath)
    {
        final List<Float> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setInt(int value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Integer.valueOf(value));
        });
    }
    
    @Override
    default void setInt(int value)
    {
        ConfigurationTool.consume(this, ConfigurationInt.class, ConfigurationTool.intPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Integer.valueOf(value));
        });
    }
    
    @Override
    default void setIntList(int[] value)
    {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationIntList.class, ConfigurationTool.intListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setIntList(int[] value, String subpath)
    {
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setItemStack(ConfigItemStackData value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setItemStack(ConfigItemStackData value)
    {
        ConfigurationTool.consume(this, ConfigurationItemStack.class, ConfigurationTool.itemStackPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setItemStackList(ConfigItemStackData[] value)
    {
        ConfigurationTool.consumeList(this, ConfigurationItemStackList.class, ConfigurationTool.itemStackListPath(), value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setItemStackList(ConfigItemStackData[] value, String subpath)
    {
        ConfigurationTool.consumeList(this, subpath, value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setLong(long value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Long.valueOf(value));
        });
    }
    
    @Override
    default void setLong(long value)
    {
        ConfigurationTool.consume(this, ConfigurationLong.class, ConfigurationTool.longPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Long.valueOf(value));
        });
    }
    
    @Override
    default void setLongList(long[] value)
    {
        final List<Long> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationLongList.class, ConfigurationTool.longListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setLongList(long[] value, String subpath)
    {
        final List<Long> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setPlayer(McPlayerInterface value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setPlayer(McPlayerInterface value)
    {
        ConfigurationTool.consume(this, ConfigurationPlayer.class, ConfigurationTool.playerPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setPlayerList(McPlayerInterface[] value)
    {
        ConfigurationTool.consumeList(this, ConfigurationPlayerList.class, ConfigurationTool.playerListPath(), value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setPlayerList(McPlayerInterface[] value, String subpath)
    {
        ConfigurationTool.consumeList(this, subpath, value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setShort(short value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Short.valueOf(value));
        });
    }
    
    @Override
    default void setShort(short value)
    {
        ConfigurationTool.consume(this, ConfigurationShort.class, ConfigurationTool.shortPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, Short.valueOf(value));
        });
    }
    
    @Override
    default void setShortList(short[] value)
    {
        final List<Short> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(Short.valueOf(value[i]));
        }
        ConfigurationTool.consume(this, ConfigurationShortList.class, ConfigurationTool.shortListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setShortList(short[] value, String subpath)
    {
        final List<Short> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(Short.valueOf(value[i]));
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setString(String value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setString(String value)
    {
        ConfigurationTool.consume(this, ConfigurationString.class, ConfigurationTool.stringPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setStringList(String[] value, String subpath)
    {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    @Override
    default void setStringList(String[] value)
    {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < value.length; i++)
        {
            list.add(value[i]);
        }
        ConfigurationTool.consume(this, ConfigurationStringList.class, ConfigurationTool.stringListPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).setPrimitiveList(path, list);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setVector(ConfigVectorData value, String subpath)
    {
        ConfigurationTool.consume(this, subpath, (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setVector(ConfigVectorData value)
    {
        ConfigurationTool.consume(this, ConfigurationVector.class, ConfigurationTool.vectorPath(), (val, configs, config, lib, minigame, path) ->
        {
            minigame.getConfig(configs.file()).set(path, value);
        });
    }
    
    @Override
    default void setVectorList(ConfigVectorData[] value)
    {
        ConfigurationTool.consumeList(this, ConfigurationVectorList.class, ConfigurationTool.vectorListPath(), value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    /**
     * Sets the value to this configuration variable.
     * 
     * @param value
     *            value to set.
     * @param subpath
     *            the sub path
     */
    default void setVectorList(ConfigVectorData[] value, String subpath)
    {
        ConfigurationTool.consumeList(this, subpath, value, (val, configs, config, lib, minigame, section, path, element) ->
        {
            section.set(path, element);
        });
    }
    
    @Override
    @SuppressWarnings("unchecked")
    default <T extends DataFragment> T getObject()
    {
        return (T) ConfigurationTool.calculate(this, ConfigurationObject.class, ConfigurationTool.objectPath(), (val, configs, config, lib, minigame, path) ->
        {
            DataSection section = minigame.getConfig(configs.file()).getSection(path);
            if (section == null)
            {
                return null;
            }
            final DataFragment result = config.clazz().newInstance();
            result.read(section);
            return result;
        });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param clazz
     *            DataFragment object class
     * @param path
     *            sub path of configuration section
     * @return value.
     * @param <T>
     *            DataFragment object class
     */
    @SuppressWarnings("unchecked")
    default <T extends DataFragment> T getObject(Class<T> clazz, String path)
    {
        return (T) ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) ->
        {
            final DataSection section = minigame.getConfig(configs.file()).getSection(spath);
            if (section != null)
            {
                final DataFragment result = clazz.newInstance();
                result.read(section);
                return result;
            }
            return null;
        });
    }
    
    @Override
    default byte getByte()
    {
        return ConfigurationTool.calculate(this, ConfigurationByte.class, ConfigurationTool.bytePath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getInt(path, config.defaultValue())).byteValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default byte getByte(String path, byte defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getInt(spath, defaultValue)).byteValue();
    }
    
    @Override
    default <T extends EnumerationValue> T getEnum(Class<T> clazz)
    {
        return ConfigurationTool.calculate(
            this, ConfigurationEnum.class, ConfigurationTool.enumPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getEnumValue(clazz, path));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumration class
     * @param clazz
     *            enumeration class
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default <T extends EnumerationValue> T getEnum(Class<T> clazz, String path)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getEnumValue(clazz, spath));
    }
    
    @Override
    default <T extends Enum<?>> T getJavaEnum(Class<T> clazz)
    {
        return ConfigurationTool.calculate(
            this, ConfigurationJavaEnum.class, ConfigurationTool.javaEnumPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getEnum(clazz, path));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumration class
     * @param clazz
     *            enumeration class
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default <T extends Enum<?>> T getJavaEnum(Class<T> clazz, String path)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getEnum(clazz, spath));
    }
    
    @Override
    default ConfigColorData getColor()
    {
        return ConfigurationTool.calculate(this, ConfigurationColor.class, ConfigurationTool.colorPath(), (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file())
            .getFragment(ConfigColorData.class, path, ConfigColorData.fromBukkitColor(Color.fromRGB(config.defaultRgb()))));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default ConfigColorData getColor(String path, ConfigColorData defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) ->
        {
            final ConfigColorData col = minigame.getConfig(configs.file()).getFragment(ConfigColorData.class, spath);
            return col == null ? defaultValue : col;
        });
    }
    
    @Override
    default ConfigItemStackData getItemStack()
    {
        return ConfigurationTool.calculate(this, ConfigurationItemStack.class, ConfigurationTool.itemStackPath(), (val, configs, config, lib, minigame, path) ->
        {
            final ConfigItemStackData stack = minigame.getConfig(configs.file()).getFragment(ConfigItemStackData.class, path);
            return stack == null ? null : stack;
        });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ConfigItemStackData getItemStack(String path)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) ->
        {
            final ConfigItemStackData stack = minigame.getConfig(configs.file()).getFragment(ConfigItemStackData.class, spath);
            return stack == null ? null : stack;
        });
    }
    
    @Override
    default ConfigVectorData getVector()
    {
        return ConfigurationTool.calculate(this, ConfigurationVector.class, ConfigurationTool.vectorPath(), (val, configs, config, lib, minigame, path) ->
        {
            final ConfigVectorData vector = minigame.getConfig(configs.file()).getFragment(ConfigVectorData.class, path);
            return vector == null ? null : vector;
        });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ConfigVectorData getVector(String path)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) ->
        {
            final ConfigVectorData result = minigame.getConfig(configs.file()).getFragment(ConfigVectorData.class, spath);
            return result == null ? null : result;
        });
    }
    
    @Override
    default McPlayerInterface getPlayer()
    {
        return ConfigurationTool.calculate(this, ConfigurationPlayer.class, ConfigurationTool.playerPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getFragment(McPlayerInterface.class, path));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default McPlayerInterface getPlayer(String path)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getFragment(McPlayerInterface.class, spath));
    }
    
    @Override
    default char getCharacter()
    {
        return ConfigurationTool.calculate(this, ConfigurationCharacter.class, ConfigurationTool.charPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getString(path, "" + config.defaultValue())).charAt(0); //$NON-NLS-1$
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default char getCharacter(String path, char defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getString(spath, "" + defaultValue)).charAt(0); //$NON-NLS-1$
    }
    
    @Override
    default boolean getBoolean()
    {
        return ConfigurationTool.calculate(this, ConfigurationBool.class, ConfigurationTool.boolPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getBoolean(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default boolean getBoolean(String path, boolean defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getBoolean(spath, defaultValue));
    }
    
    @Override
    default boolean[] getBooleanList()
    {
        final List<Boolean> list = ConfigurationTool.calculate(this, ConfigurationBoolList.class, ConfigurationTool.boolListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getBooleanList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final boolean[] result = new boolean[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default boolean[] getBooleanList(String path, boolean[] defaultValue)
    {
        final List<Boolean> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getBooleanList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final boolean[] result = new boolean[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default <T extends EnumerationValue> T[] getEnumList(Class<T> clazz)
    {
        final List<T> list = ConfigurationTool.calculate(this, ConfigurationEnumList.class, ConfigurationTool.enumListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getEnumValueList(clazz, path), null);
        @SuppressWarnings("unchecked")
        final T[] result = (T[]) Array.newInstance(clazz, list == null ? 0 : list.size());
        if (list != null)
        {
            for (int i = 0; i < result.length; i++)
            {
                result[i] = list.get(i);
            }
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumeration class
     * @param clazz
     *            enumeration class
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default <T extends EnumerationValue> T[] getEnumList(Class<T> clazz, String path)
    {
        final List<T> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getEnumValueList(clazz, spath));
        @SuppressWarnings("unchecked")
        final T[] result = (T[]) Array.newInstance(clazz, list == null ? 0 : list.size());
        if (list != null)
        {
            for (int i = 0; i < result.length; i++)
            {
                result[i] = list.get(i);
            }
        }
        return result;
    }
    
    @Override
    default <T extends Enum<?>> T[] getJavaEnumList(Class<T> clazz)
    {
        final List<T> list = ConfigurationTool.calculate(this, ConfigurationJavaEnumList.class, ConfigurationTool.javaEnumListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getEnumList(clazz, path), null);
        @SuppressWarnings("unchecked")
        final T[] result = (T[]) Array.newInstance(clazz, list == null ? 0 : list.size());
        if (list != null)
        {
            for (int i = 0; i < result.length; i++)
            {
                result[i] = list.get(i);
            }
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            enumeration class
     * @param clazz
     *            enumeration class
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default <T extends Enum<?>> T[] getJavaEnumList(Class<T> clazz, String path)
    {
        final List<T> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getEnumList(clazz, spath));
        @SuppressWarnings("unchecked")
        final T[] result = (T[]) Array.newInstance(clazz, list == null ? 0 : list.size());
        if (list != null)
        {
            for (int i = 0; i < result.length; i++)
            {
                result[i] = list.get(i);
            }
        }
        return result;
    }
    
    @Override
    default byte[] getByteList()
    {
        final List<Byte> list = ConfigurationTool.calculate(this, ConfigurationByteList.class, ConfigurationTool.byteListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getByteList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final byte[] result = new byte[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default byte[] getByteList(String path, byte[] defaultValue)
    {
        final List<Byte> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getByteList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final byte[] result = new byte[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default char[] getCharacterList()
    {
        final List<Character> list = ConfigurationTool.calculate(this, ConfigurationCharacterList.class, ConfigurationTool.charListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getCharacterList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final char[] result = new char[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default char[] getCharacterList(String path, char[] defaultValue)
    {
        final List<Character> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getCharacterList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final char[] result = new char[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default double getDouble()
    {
        return ConfigurationTool.calculate(this, ConfigurationDouble.class, ConfigurationTool.doublePath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getDouble(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default double getDouble(String path, double defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getDouble(spath, defaultValue));
    }
    
    @Override
    default float getFloat()
    {
        return ConfigurationTool.calculate(this, ConfigurationFloat.class, ConfigurationTool.floatPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getDouble(path, config.defaultValue())).floatValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default float getFloat(String path, float defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getDouble(spath, defaultValue)).floatValue();
    }
    
    @Override
    default double[] getDoubleList()
    {
        final List<Double> list = ConfigurationTool.calculate(this, ConfigurationDoubleList.class, ConfigurationTool.doubleListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getDoubleList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final double[] result = new double[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default double[] getDoubleList(String path, double[] defaultValue)
    {
        final List<Double> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getDoubleList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final double[] result = new double[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default float[] getFloatList()
    {
        final List<Float> list = ConfigurationTool.calculate(this, ConfigurationFloatList.class, ConfigurationTool.floatListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getFloatList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final float[] result = new float[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default float[] getFloatList(String path, float[] defaultValue)
    {
        final List<Float> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getFloatList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final float[] result = new float[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default int getInt()
    {
        return ConfigurationTool.calculate(this, ConfigurationInt.class, ConfigurationTool.intPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getInt(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default int getInt(String path, int defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getInt(spath, defaultValue));
    }
    
    @Override
    default short getShort()
    {
        return ConfigurationTool.calculate(this, ConfigurationShort.class, ConfigurationTool.shortPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getInt(path, config.defaultValue())).shortValue();
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default short getShort(String path, short defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getInt(spath, defaultValue)).shortValue();
    }
    
    @Override
    default int[] getIntList()
    {
        final List<Integer> list = ConfigurationTool.calculate(this, ConfigurationIntList.class, ConfigurationTool.intListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getIntegerList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default int[] getIntList(String path, int[] defaultValue)
    {
        final List<Integer> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getIntegerList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default long getLong()
    {
        return ConfigurationTool.calculate(this, ConfigurationLong.class, ConfigurationTool.longPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getLong(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default long getLong(String path, long defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getLong(spath, defaultValue));
    }
    
    @Override
    default long[] getLongList()
    {
        final List<Long> list = ConfigurationTool.calculate(this, ConfigurationLongList.class, ConfigurationTool.longListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getLongList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final long[] result = new long[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default long[] getLongList(String path, long[] defaultValue)
    {
        final List<Long> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getLongList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final long[] result = new long[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default short[] getShortList()
    {
        final List<Short> list = ConfigurationTool.calculate(this, ConfigurationShortList.class, ConfigurationTool.shortListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getShortList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(ArrayUtils.toObject(config.defaultValue())));
        final short[] result = new short[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default short[] getShortList(String path, short[] defaultValue)
    {
        final List<Short> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getShortList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        final short[] result = new short[list.size()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = list.get(i);
        }
        return result;
    }
    
    @Override
    default String getString()
    {
        return ConfigurationTool.calculate(this, ConfigurationString.class, ConfigurationTool.stringPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getString(path, config.defaultValue()));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default String getString(String path, String defaultValue)
    {
        return ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getString(spath, defaultValue));
    }
    
    @Override
    default String[] getStringList()
    {
        final List<String> list = ConfigurationTool.calculate(this, ConfigurationStringList.class, ConfigurationTool.stringListPath(),
            (val, configs, config, lib, minigame, path) -> minigame.getConfig(configs.file()).getStringList(path),
            (val, configs, config, lib, minigame, path) -> Arrays.asList(config.defaultValue()));
        return list.toArray(new String[list.size()]);
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * @param defaultValue
     *            the default value to return
     * 
     * @return value.
     */
    default String[] getStringList(String path, String[] defaultValue)
    {
        final List<String> list = ConfigurationTool.calculate(this, path, (val, configs, config, lib, minigame, spath) -> minigame.getConfig(configs.file()).getStringList(spath));
        if (list == null || list.size() == 0)
        {
            return defaultValue;
        }
        return list.toArray(new String[list.size()]);
    }
    
    @Override
    default ConfigVectorData[] getVectorList()
    {
        return ConfigurationTool.calculateList(this, ConfigurationVectorList.class, ConfigVectorData.class, ConfigurationTool.vectorListPath(),
            (val, configs, config, lib, minigame, section, key) -> section.getFragment(ConfigVectorData.class, key));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ConfigVectorData[] getVectorList(String path)
    {
        return ConfigurationTool.calculateList(this, path, ConfigVectorData.class, (val, configs, config, lib, minigame, section, key) -> section.getFragment(ConfigVectorData.class, key));
    }
    
    @Override
    default ConfigItemStackData[] getItemStackList()
    {
        return ConfigurationTool.calculateList(this, ConfigurationItemStackList.class, ConfigItemStackData.class, ConfigurationTool.itemStackListPath(),
            (val, configs, config, lib, minigame, section, key) -> section.getFragment(ConfigItemStackData.class, key));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ConfigItemStackData[] getItemStackList(String path)
    {
        return ConfigurationTool.calculateList(this, path, ConfigItemStackData.class, (val, configs, config, lib, minigame, section, key) -> section.getFragment(ConfigItemStackData.class, key));
    }
    
    @Override
    default <T extends DataFragment> T[] getObjectList(Class<T> clazz)
    {
        return ConfigurationTool.calculateList(this, ConfigurationObjectList.class, clazz, ConfigurationTool.objectListPath(), (val, configs, config, lib, minigame, section, key) ->
        {
            final T ret = clazz.newInstance();
            ret.read(section.getSection(key));
            return ret;
        });
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param <T>
     *            DataFragment object class
     * @param clazz
     *            DataFragment object class
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default <T extends DataFragment> T[] getObjectList(Class<T> clazz, String path)
    {
        return ConfigurationTool.calculateList(this, path, clazz, (val, configs, config, lib, minigame, section, key) ->
        {
            final T result = clazz.newInstance();
            result.read(section.getSection(key));
            return result;
        });
    }
    
    @Override
    default ConfigColorData[] getColorList()
    {
        return ConfigurationTool.calculateList(this, ConfigurationColorList.class, ConfigColorData.class, ConfigurationTool.colorListPath(),
            (val, configs, config, lib, minigame, section, key) -> section.getFragment(ConfigColorData.class, key));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default ConfigColorData[] getColorList(String path)
    {
        return ConfigurationTool.calculateList(this, path, ConfigColorData.class, (val, configs, config, lib, minigame, section, key) -> section.getFragment(ConfigColorData.class, key));
    }
    
    @Override
    default McPlayerInterface[] getPlayerList()
    {
        return ConfigurationTool.calculateList(this, ConfigurationPlayerList.class, McPlayerInterface.class, ConfigurationTool.playerListPath(),
            (val, configs, config, lib, minigame, section, key) -> section.getFragment(McPlayerInterface.class, key));
    }
    
    /**
     * Returns the value of given configuration value.
     * 
     * @param path
     *            sub path of configuration section
     * 
     * @return value.
     */
    default McPlayerInterface[] getPlayerList(String path)
    {
        return ConfigurationTool.calculateList(this, path, McPlayerInterface.class, (val, configs, config, lib, minigame, section, key) -> section.getFragment(McPlayerInterface.class, key));
    }
    
    /**
     * Returns the keys of given configuration section.
     * 
     * @param deep
     *            true for deep keys
     * 
     * @return value.
     */
    default String[] getKeys(boolean deep)
    {
        try
        {
            final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
            final ConfigurationSection config = this.getClass().getDeclaredField(this.name()).getAnnotation(ConfigurationSection.class);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final ConfigServiceInterface lib = ConfigServiceInterface.instance();
            final ConfigInterface minigame = lib.getConfigFromCfg(this);
            final String path = configs.path() + '.' + (config.value().length() == 0 ? this.name() : config.value());
            final Set<String> result = minigame.getConfig(configs.file()).getSection(path).getKeys(deep);
            return result.toArray(new String[result.size()]);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    @Override
    default void saveConfig()
    {
        final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
        final ConfigServiceInterface lib = ConfigServiceInterface.instance();
        final ConfigInterface minigame = lib.getConfigFromCfg(this);
        minigame.saveConfig(configs.file());
    }
    
    @Override
    default void verifyConfig() throws McException
    {
        final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
        final ConfigServiceInterface lib = ConfigServiceInterface.instance();
        final ConfigInterface minigame = lib.getConfigFromCfg(this);
        minigame.verifyConfig(configs.file());
    }
    
    @Override
    default void rollbackConfig()
    {
        final ConfigurationValues configs = this.getClass().getAnnotation(ConfigurationValues.class);
        final ConfigServiceInterface lib = ConfigServiceInterface.instance();
        final ConfigInterface minigame = lib.getConfigFromCfg(this);
        minigame.rollbackConfig(configs.file());
    }
    
    @Override
    default String[] getComment()
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(this.name());
            
            {
                final ConfigComment config = field.getAnnotation(ConfigComment.class);
                if (config != null)
                {
                    return config.value();
                }
            }
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore
        }
        return new String[0];
    }
    
    @Override
    default Class<? extends EnumerationValue> getEnumClass()
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(this.name());
            
            {
                final ConfigurationEnum config = field.getAnnotation(ConfigurationEnum.class);
                if (config != null)
                {
                    return config.clazz();
                }
            }
            
            {
                final ConfigurationEnumList config = field.getAnnotation(ConfigurationEnumList.class);
                if (config != null)
                {
                    return config.clazz();
                }
            }
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore
        }
        return null;
    }
    
    @Override
    default Class<? extends Enum<?>> getJavaEnumClass()
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(this.name());
            
            {
                final ConfigurationJavaEnum config = field.getAnnotation(ConfigurationJavaEnum.class);
                if (config != null)
                {
                    return config.clazz();
                }
            }
            
            {
                final ConfigurationJavaEnumList config = field.getAnnotation(ConfigurationJavaEnumList.class);
                if (config != null)
                {
                    return config.clazz();
                }
            }
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore
        }
        return null;
    }
    
    @Override
    default void validate() throws McException
    {
        try
        {
            final Field field = this.getClass().getDeclaredField(this.name());
            final ValidateFMax fmax = field.getAnnotation(ValidateFMax.class);
            if (fmax != null)
            {
                ValidateFMax.ValidatorInstance.validate(fmax, this);
            }
            final ValidateFMin fmin = field.getAnnotation(ValidateFMin.class);
            if (fmin != null)
            {
                ValidateFMin.ValidatorInstance.validate(fmin, this);
            }
            final ValidateIsset isset = field.getAnnotation(ValidateIsset.class);
            if (isset != null)
            {
                ValidateIsset.ValidatorInstance.validate(isset, this);
            }
            final ValidateListMax listmax = field.getAnnotation(ValidateListMax.class);
            if (listmax != null)
            {
                ValidateListMax.ValidatorInstance.validate(listmax, this);
            }
            final ValidateListMin listmin = field.getAnnotation(ValidateListMin.class);
            if (listmin != null)
            {
                ValidateListMin.ValidatorInstance.validate(listmin, this);
            }
            final ValidateLMax lmax = field.getAnnotation(ValidateLMax.class);
            if (lmax != null)
            {
                ValidateLMax.ValidatorInstance.validate(lmax, this);
            }
            final ValidateLMin lmin = field.getAnnotation(ValidateLMin.class);
            if (lmin != null)
            {
                ValidateLMin.ValidatorInstance.validate(lmin, this);
            }
            final ValidateStrMax strmax = field.getAnnotation(ValidateStrMax.class);
            if (strmax != null)
            {
                ValidateStrMax.ValidatorInstance.validate(strmax, this);
            }
            final ValidateStrMin strmin = field.getAnnotation(ValidateStrMin.class);
            if (strmin != null)
            {
                ValidateStrMin.ValidatorInstance.validate(strmin, this);
            }
            final Validator validator = field.getAnnotation(Validator.class);
            if (validator != null)
            {
                validator.value().newInstance().validate(this);
            }
        }
        catch (McException ex)
        {
            // rethrow validation errors
            throw ex;
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // silently ignore
        }
    }
    
}
