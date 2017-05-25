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

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Helper class for configuration variables.
 * 
 * @author mepeisen
 */
class ConfigurationTool
{
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <RET>
     *            return class
     * @param <ANNOT>
     *            annotation class
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @param calculator
     *            calculator function
     * @return return value.
     */
    static <RET, ANNOT extends Annotation> RET calculate(ConfigurationValueInterface val, Class<ANNOT> clazz, Calculator<RET, ANNOT> calculator)
    {
        try
        {
            final ConfigurationValues configs = val.getClass().getAnnotation(ConfigurationValues.class);
            final ANNOT config = val.getClass().getDeclaredField(val.name()).getAnnotation(clazz);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final ConfigServiceInterface lib = ConfigServiceCache.get();
            final ConfigInterface minigame = lib.getConfigFromCfg(val);
            return calculator.supply(val, configs, config, McLibInterface.instance(), minigame);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <RET>
     *            return class
     * @param <ANNOT>
     *            annotation class
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @param path
     *            path function
     * @param calculator
     *            calculator function
     * @return return value.
     */
    static <RET, ANNOT extends Annotation> RET calculate(ConfigurationValueInterface val, Class<ANNOT> clazz, PathCalculator<ANNOT> path, ValueCalculator<RET, ANNOT> calculator)
    {
        final Calculator<RET, ANNOT> calc = (val2, configs, config, lib, minigame) -> calculator.supply(val, configs, config, lib, minigame, path.supply(val, configs, config, lib));
        return calculate(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <RET>
     *            return class
     * @param <ANNOT>
     *            annotation class
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @param path
     *            path function
     * @param calculator
     *            calculator function
     * @param defaultValue
     *            default value function
     * @return return value.
     */
    static <RET, ANNOT extends Annotation> RET calculate(ConfigurationValueInterface val, Class<ANNOT> clazz, PathCalculator<ANNOT> path, ValueCalculator<RET, ANNOT> calculator,
            ValueCalculator<RET, ANNOT> defaultValue)
    {
        final Calculator<RET, ANNOT> calc = (val2, configs, config, lib, minigame) ->
        {
            final String spath = path.supply(val, configs, config, lib);
            RET res = minigame.getConfig(configs.file()).contains(spath) ? calculator.supply(val, configs, config, lib, minigame, spath) : null;
            if (res == null && defaultValue != null)
            {
                res = defaultValue.supply(val, configs, config, lib, minigame, spath);
            }
            return res;
        };
        return calculate(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <RET>
     *            return type
     * @param val
     *            config value
     * @param subpath
     *            subpath
     * @param calculator
     *            calculator function
     * @return return value.
     */
    static <RET> RET calculate(ConfigurationValueInterface val, String subpath, ValueCalculator<RET, ConfigurationSection> calculator)
    {
        final Calculator<RET, ConfigurationSection> calc = (val2, configs, config, lib, minigame) -> calculator.supply(val, configs, config, lib, minigame,
                sectionPath().supply(val, configs, config, lib) + '.' + subpath);
        return calculate(val, ConfigurationSection.class, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <RET>
     *            return type
     * @param <ANNOT>
     *            annotation class
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @param retClazz
     *            return class
     * @param path
     *            path function
     * @param calculator
     *            calculator function
     * @return return value
     */
    static <RET, ANNOT extends Annotation> RET[] calculateList(ConfigurationValueInterface val, Class<ANNOT> clazz, Class<RET> retClazz, PathCalculator<ANNOT> path,
            ArrayValueCalculator<RET, ANNOT> calculator)
    {
        @SuppressWarnings("unchecked")
        final Calculator<RET[], ANNOT> calc = (val2, configs, config, lib, minigame) ->
        {
            final DataSection section = minigame.getConfig(configs.file()).getSection(path.supply(val, configs, config, lib));
            final List<RET> list = new ArrayList<>();
            if (section != null)
            {
                for (final String key : section.getKeys(false))
                {
                    list.add(calculator.supply(val, configs, config, lib, minigame, section, key));
                }
            }
            return list.toArray((RET[]) Array.newInstance(retClazz, list.size()));
        };
        return calculate(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <RET>
     *            return type
     * @param val
     *            config value
     * @param retClazz
     *            return class
     * @param subpath
     *            subpath
     * @param calculator
     *            calculator function
     * @return return value
     */
    static <RET> RET[] calculateList(ConfigurationValueInterface val, String subpath, Class<RET> retClazz, ArrayValueCalculator<RET, ConfigurationSection> calculator)
    {
        @SuppressWarnings("unchecked")
        final Calculator<RET[], ConfigurationSection> calc = (val2, configs, config, lib, minigame) ->
        {
            final DataSection section = minigame.getConfig(configs.file()).getSection(sectionPath().supply(val, configs, config, lib) + '.' + subpath);
            final List<RET> list = new ArrayList<>();
            if (section != null)
            {
                for (final String key : section.getKeys(false))
                {
                    list.add(calculator.supply(val, configs, config, lib, minigame, section, key));
                }
            }
            return list.toArray((RET[]) Array.newInstance(retClazz, list.size()));
        };
        return calculate(val, ConfigurationSection.class, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <ANNOT>
     *            annotation class
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @param consumer
     *            consumer function
     */
    static <ANNOT extends Annotation> void consume(ConfigurationValueInterface val, Class<ANNOT> clazz, Consumer<ANNOT> consumer)
    {
        try
        {
            final ConfigurationValues configs = val.getClass().getAnnotation(ConfigurationValues.class);
            final ANNOT config = val.getClass().getDeclaredField(val.name()).getAnnotation(clazz);
            if (configs == null || config == null)
            {
                throw new IllegalStateException("Invalid configuration class."); //$NON-NLS-1$
            }
            final ConfigServiceInterface lib = ConfigServiceCache.get();
            final ConfigInterface minigame = lib.getConfigFromCfg(val);
            consumer.apply(val, configs, config, McLibInterface.instance(), minigame);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <ANNOT>
     *            annotation class
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @param path
     *            path function
     * @param consumer
     *            consumer function
     */
    static <ANNOT extends Annotation> void consume(ConfigurationValueInterface val, Class<ANNOT> clazz, PathCalculator<ANNOT> path, ValueConsumer<ANNOT> consumer)
    {
        final Consumer<ANNOT> calc = (val2, configs, config, lib, minigame) -> consumer.apply(val, configs, config, lib, minigame, path.supply(val, configs, config, lib));
        consume(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param val
     *            config value
     * @param subpath
     *            subpath
     * @param consumer
     *            consumer function
     */
    static void consume(ConfigurationValueInterface val, String subpath, ValueConsumer<ConfigurationSection> consumer)
    {
        final Class<ConfigurationSection> clazz = ConfigurationSection.class;
        final Consumer<ConfigurationSection> calc = (val2, configs, config, lib, minigame) -> consumer.apply(val, configs, config, lib, minigame,
                sectionPath().supply(val, configs, config, lib) + '.' + subpath);
        consume(val, clazz, calc);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <T>
     *            array type
     * @param <ANNOT>
     *            annotation type
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @param path
     *            path function
     * @param value
     *            array value
     * @param consumer
     *            consumer function
     */
    static <T, ANNOT extends Annotation> void consumeList(ConfigurationValueInterface val, Class<ANNOT> clazz, PathCalculator<ANNOT> path, T[] value, ArrayValueConsumer<T, ANNOT> consumer)
    {
        final ValueConsumer<ANNOT> vconsumer = (val2, configs, config, lib, minigame, spath) ->
        {
            DataSection section = minigame.getConfig(configs.file()).getSection(spath);
            if (section == null)
            {
                section = minigame.getConfig(configs.file()).createSection(spath);
            }
            for (final String key : section.getKeys(false))
            {
                section.set(key, null);
            }
            int i = 0;
            for (T v : value)
            {
                consumer.apply(val, configs, config, lib, minigame, section, "e" + i, v); //$NON-NLS-1$
                i++;
            }
        };
        
        consume(val, clazz, path, vconsumer);
    }
    
    /**
     * Calculates a value by using a calculator func.
     * 
     * @param <T>
     *            array type
     * @param val
     *            config value
     * @param subpath
     *            subpath
     * @param value
     *            array value
     * @param consumer
     *            consumer function
     */
    static <T> void consumeList(ConfigurationValueInterface val, String subpath, T[] value, ArrayValueConsumer<T, ConfigurationSection> consumer)
    {
        final ValueConsumer<ConfigurationSection> vconsumer = (val2, configs, config, lib, minigame, spath) ->
        {
            DataSection section = minigame.getConfig(configs.file()).getSection(spath);
            if (section == null)
            {
                section = minigame.getConfig(configs.file()).createSection(spath);
            }
            for (final String key : section.getKeys(false))
            {
                section.set(key, null);
            }
            int i = 0;
            for (T v : value)
            {
                consumer.apply(val, configs, config, lib, minigame, section, "e" + i, v); //$NON-NLS-1$
                i++;
            }
        };
        
        consume(val, subpath, vconsumer);
    }
    
    /**
     * Checks if the given config value has given annotation.
     * 
     * @param val
     *            config value
     * @param clazz
     *            annotation class
     * @return {@code true} if the config value has diven annotation
     */
    static boolean isType(ConfigurationValueInterface val, Class<? extends Annotation> clazz)
    {
        try
        {
            final Field field = val.getClass().getDeclaredField(val.name());
            return field.getAnnotation(clazz) != null;
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationSection> sectionPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.value().length() == 0 ? val.name() : config.value());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationEnum> enumPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationJavaEnum> javaEnumPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationEnumList> enumListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationJavaEnumList> javaEnumListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationBool> boolPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationBoolList> boolListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationByte> bytePath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationByteList> byteListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationCharacter> charPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationCharacterList> charListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationColor> colorPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationColorList> colorListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationDouble> doublePath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationDoubleList> doubleListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationFloat> floatPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationFloatList> floatListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationInt> intPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationIntList> intListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationItemStack> itemStackPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationItemStackList> itemStackListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationLong> longPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationLongList> longListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationObject> objectPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationObjectList> objectListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationPlayer> playerPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationPlayerList> playerListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationString> stringPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationStringList> stringListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationShort> shortPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationShortList> shortListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationVector> vectorPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Returns the path calculator for given type.
     * 
     * @return path calculator
     */
    static PathCalculator<ConfigurationVectorList> vectorListPath()
    {
        return (val, configs, config, lib) -> configs.path() + '.' + (config.name().length() == 0 ? val.name() : config.name());
    }
    
    /**
     * Calculator to fetch data.
     * 
     * @param <RET>
     *            return clazz
     * @param <ANNOT>
     *            annotation clazz
     */
    @FunctionalInterface
    interface Calculator<RET, ANNOT extends Annotation>
    {
        
        /**
         * Calculates the value from config.
         * 
         * @param val
         *            config value
         * @param configs
         *            configuration values annotation
         * @param config
         *            config annotation
         * @param lib
         *            mclib interface
         * @param minigame
         *            config accessor
         * @return return value
         * @throws Exception
         *             thrown for problems accessing the config
         */
        RET supply(ConfigurationValueInterface val, ConfigurationValues configs, ANNOT config, McLibInterface lib, ConfigInterface minigame) throws Exception;
        
    }
    
    /**
     * Calculator to fetch data.
     * 
     * @param <RET>
     *            return clazz
     * @param <ANNOT>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ValueCalculator<RET, ANNOT extends Annotation>
    {
        
        /**
         * Calculates the value from config.
         * 
         * @param val
         *            config value
         * @param configs
         *            configuration values annotation
         * @param config
         *            config annotation
         * @param lib
         *            mclib interface
         * @param minigame
         *            config accessor
         * @param path
         *            config path
         * @return return value
         * @throws Exception
         *             thrown for problems accessing the config
         */
        RET supply(ConfigurationValueInterface val, ConfigurationValues configs, ANNOT config, McLibInterface lib, ConfigInterface minigame, String path) throws Exception;
        
    }
    
    /**
     * Calculator to fetch data.
     * 
     * @param <RET>
     *            return clazz
     * @param <ANNOT>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ArrayValueCalculator<RET, ANNOT extends Annotation>
    {
        
        /**
         * Calculates the value from config.
         * 
         * @param val
         *            config value
         * @param configs
         *            configuration values annotation
         * @param config
         *            config annotation
         * @param lib
         *            mclib interface
         * @param minigame
         *            config accessor
         * @param section
         *            config section
         * @param key
         *            config key
         * @return return value
         * @throws Exception
         *             thrown for problems accessing the config
         */
        RET supply(ConfigurationValueInterface val, ConfigurationValues configs, ANNOT config, McLibInterface lib, ConfigInterface minigame, DataSection section, String key) throws Exception;
        
    }
    
    /**
     * Calculator for config path calculation.
     * 
     * @param <ANNOT>
     *            annotation clazz
     */
    @FunctionalInterface
    interface PathCalculator<ANNOT extends Annotation>
    {
        
        /**
         * Calculates the path from config annotation.
         * 
         * @param val
         *            config value
         * @param configs
         *            configuration values annotation
         * @param config
         *            config annotation
         * @param lib
         *            mclib interface
         * @return return value
         * @throws Exception
         *             thrown for problems accessing the config
         */
        String supply(ConfigurationValueInterface val, ConfigurationValues configs, ANNOT config, McLibInterface lib) throws Exception;
        
    }
    
    /**
     * Calculator to parse data.
     * 
     * @param <ANNOT>
     *            annotation clazz
     */
    @FunctionalInterface
    interface Consumer<ANNOT extends Annotation>
    {
        
        /**
         * Consume config.
         * 
         * @param val
         *            config value
         * @param configs
         *            configuration values annotation
         * @param config
         *            config annotation
         * @param lib
         *            mclib interface
         * @param minigame
         *            config accessor
         * @throws Exception
         *             thrown for problems accessing the config
         */
        void apply(ConfigurationValueInterface val, ConfigurationValues configs, ANNOT config, McLibInterface lib, ConfigInterface minigame) throws Exception;
        
    }
    
    /**
     * Calculator to parse data.
     * 
     * @param <ANNOT>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ValueConsumer<ANNOT extends Annotation>
    {
        
        /**
         * Consume config.
         * 
         * @param val
         *            config value
         * @param configs
         *            configuration values annotation
         * @param config
         *            config annotation
         * @param lib
         *            mclib interface
         * @param minigame
         *            config accessor
         * @param path
         *            config path
         * @throws Exception
         *             thrown for problems accessing the config
         */
        void apply(ConfigurationValueInterface val, ConfigurationValues configs, ANNOT config, McLibInterface lib, ConfigInterface minigame, String path) throws Exception;
        
    }
    
    /**
     * Calculator to parse data.
     * 
     * @param <T>
     *            array element clazz
     * @param <ANNOT>
     *            annotation clazz
     */
    @FunctionalInterface
    interface ArrayValueConsumer<T, ANNOT extends Annotation>
    {
        
        /**
         * Consume config.
         * 
         * @param val
         *            config value
         * @param configs
         *            configuration values annotation
         * @param config
         *            config annotation
         * @param lib
         *            mclib interface
         * @param minigame
         *            config accessor
         * @param section
         *            config section
         * @param path
         *            config path
         * @param element
         *            array element
         * @throws Exception
         *             thrown for problems accessing the config
         */
        void apply(ConfigurationValueInterface val, ConfigurationValues configs, ANNOT config, McLibInterface lib, ConfigInterface minigame, DataSection section, String path, T element)
                throws Exception;
        
    }
    
}
