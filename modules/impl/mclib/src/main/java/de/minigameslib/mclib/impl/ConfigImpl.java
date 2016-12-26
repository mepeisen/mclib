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

package de.minigameslib.mclib.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Color;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.config.ConfigColorData;
import de.minigameslib.mclib.api.config.ConfigInterface;
import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationBoolList;
import de.minigameslib.mclib.api.config.ConfigurationByte;
import de.minigameslib.mclib.api.config.ConfigurationByteList;
import de.minigameslib.mclib.api.config.ConfigurationCharacter;
import de.minigameslib.mclib.api.config.ConfigurationCharacterList;
import de.minigameslib.mclib.api.config.ConfigurationColor;
import de.minigameslib.mclib.api.config.ConfigurationDouble;
import de.minigameslib.mclib.api.config.ConfigurationDoubleList;
import de.minigameslib.mclib.api.config.ConfigurationFloat;
import de.minigameslib.mclib.api.config.ConfigurationFloatList;
import de.minigameslib.mclib.api.config.ConfigurationInt;
import de.minigameslib.mclib.api.config.ConfigurationIntList;
import de.minigameslib.mclib.api.config.ConfigurationLong;
import de.minigameslib.mclib.api.config.ConfigurationLongList;
import de.minigameslib.mclib.api.config.ConfigurationShort;
import de.minigameslib.mclib.api.config.ConfigurationShortList;
import de.minigameslib.mclib.api.config.ConfigurationString;
import de.minigameslib.mclib.api.config.ConfigurationStringList;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mclib.impl.yml.YmlFile;

/**
 * Implementation of configuration file support.
 * 
 * @author mepeisen
 */
public class ConfigImpl implements ConfigInterface
{
    
    /** logging. */
    private static final Logger LOGGER = Logger.getLogger(ConfigImpl.class.getName());
    
    /**
     * The configuration files.
     */
    private final Map<String, YmlFile>           configurations = new HashMap<>();
    
    /**
     * The default configurations.
     */
    private Map<String, List<ConfigurationValueInterface>> defaultConfigs = new HashMap<>();
    
    /** the java plugin. */
    private Plugin                                         plugin;
    
    /** the enumeration services. */
    private EnumServiceImpl                                enumService;
    
    /**
     * Constructor.
     * 
     * @param plugin
     * @param enumService
     */
    public ConfigImpl(Plugin plugin, EnumServiceImpl enumService)
    {
        this.plugin = plugin;
        this.enumService = enumService;
        for (final ConfigurationValueInterface cvi : this.enumService.getEnumValues(plugin, ConfigurationValueInterface.class))
        {
            this.defaultConfigs.computeIfAbsent(cvi.getClass().getAnnotation(ConfigurationValues.class).file(), (key) -> new ArrayList<>()).add(cvi);
        }
    }
    
    @Override
    public YmlFile getConfig(String file)
    {
        return this.getConfigEx(file);
    }
    
    /**
     * Returns the file configuration for given file.
     * @param file
     * @return file config
     */
    YmlFile getConfigEx(String file)
    {
        if (file.contains("/") || file.contains("..") || file.contains(":") || file.contains("\\")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        {
            throw new IllegalArgumentException("Invalid character in file name."); //$NON-NLS-1$
        }
        if (file.equals("messages.yml")) //$NON-NLS-1$
        {
            throw new IllegalArgumentException("Invalid file name."); //$NON-NLS-1$
        }
        
        return this.configurations.computeIfAbsent(file, (f) -> {
            YmlFile fileConfig = null;
            final File fobj = new File(this.plugin.getDataFolder(), file);
            try
            {
                fileConfig = new YmlFile(fobj);
            }
            catch (IOException e1)
            {
                throw new IllegalStateException("error loading config", e1); //$NON-NLS-1$
            }
            // TODO maybe a special behaviour for config.yml to hold it in sync with plugin.getConfig?
//            if (file.equals("config.yml")) //$NON-NLS-1$
//            {
//                fileConfig = this.plugin.getConfig();
//            }
//            else
//            {
//                fileConfig = new YmlFile(fobj);
//            }
            
            final List<ConfigurationValueInterface> list = this.defaultConfigs.get(file);
            if (list != null)
            {
                for (final ConfigurationValueInterface cfg : list)
                {
                    try
                    {
                        final ConfigurationValues clazzDef = cfg.getClass().getAnnotation(ConfigurationValues.class);
                        final Field field = cfg.getClass().getDeclaredField(((Enum<?>) cfg).name());
                        // final ConfigurationValue valueDef = .getAnnotation(LocalizedMessage.class);
                        if (clazzDef == null)
                        {
                            throw new IllegalStateException("Invalid message class."); //$NON-NLS-1$
                        }
                        
                        if (!fileConfig.contains(cfg.path()))
                        {
                            if (field.getAnnotation(ConfigurationBool.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationBool.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationBoolList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationBoolList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationByte.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationByte.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationByteList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationByteList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationCharacter.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationCharacter.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationCharacterList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationCharacterList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationDouble.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationDouble.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationDoubleList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationDoubleList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationFloat.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationFloat.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationFloatList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationFloatList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationInt.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationInt.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationIntList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationIntList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationLong.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationLong.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationLongList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationLongList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationShort.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationShort.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationShortList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationShortList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationString.class) != null)
                            {
                                fileConfig.set(cfg.path(), field.getAnnotation(ConfigurationString.class).defaultValue());
                            }
                            
                            if (field.getAnnotation(ConfigurationStringList.class) != null)
                            {
                                fileConfig.setPrimitiveList(cfg.path(), Arrays.asList(field.getAnnotation(ConfigurationStringList.class).defaultValue()));
                            }
                            
                            if (field.getAnnotation(ConfigurationColor.class) != null)
                            {
                                fileConfig.set(cfg.path(), ConfigColorData.fromBukkitColor(Color.fromRGB((field.getAnnotation(ConfigurationColor.class).defaultRgb()))));
                            }
                        }
                    }
                    catch (NoSuchFieldException ex)
                    {
                        throw new IllegalStateException(ex);
                    }
                }
                try
                {
                    fileConfig.save(fobj);
                }
                catch (IOException e)
                {
                    LOGGER.log(Level.WARNING, "Problems saving config to file " + fobj, e); //$NON-NLS-1$
                }
            }
            
            return fileConfig;
        });
    }
    
    @Override
    public void saveConfig(String file)
    {
        final File fobj = new File(this.plugin.getDataFolder(), file);
        try
        {
            this.getConfigEx(file).save(fobj);
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING, "Problems saving config to file " + fobj, e); //$NON-NLS-1$
        }
    }
    
}
