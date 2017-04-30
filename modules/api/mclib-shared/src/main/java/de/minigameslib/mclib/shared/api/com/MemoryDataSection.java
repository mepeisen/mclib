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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A memory implementation of a data section.
 * 
 * @author mepeisen
 */
public class MemoryDataSection implements DataSection
{
    
    /** path of this section. */
    private String                               path;
    /** name of this section. */
    private String                               name;
    /** parent reference. */
    private MemoryDataSection                    parent;
    
    /** map contents. */
    private final Map<String, Object>            contents             = new LinkedHashMap<>();
    
    /** 
     * Current fragment implementations for interfaces like {@link PlayerDataFragment}.
     */
    private static final Map<Class<?>, Class<?>> fragmentImpls        = new ConcurrentHashMap<>();
    
    /** security flag to prevent fragmentImpls map from being overridden. */
    private static boolean                       fragmentOverrideLock = false;
    
    /** the primitive types directly supported by DataSection. */
    static final Set<Class<?>>                   PRIM_TYPES           = new HashSet<>();
    
    /** the unique enum value factory. */
    private static UniqueEnumValueFactory uniqueEnumValueFactory;
    
    static
    {
        // default impls
        fragmentImpls.put(PlayerDataFragment.class, PlayerData.class);
        fragmentImpls.put(VectorDataFragment.class, VectorData.class);
        fragmentImpls.put(ColorDataFragment.class, ColorData.class);
        fragmentImpls.put(BlockLocationDataFragment.class, BlockLocationData.class);
        fragmentImpls.put(LocationDataFragment.class, LocationData.class);
        fragmentImpls.put(ServerDataFragment.class, ServerData.class);
        fragmentImpls.put(ServerBlockLocationDataFragment.class, ServerBlockLocationData.class);
        fragmentImpls.put(ServerLocationDataFragment.class, ServerLocationData.class);
        
        // primitive types.
        PRIM_TYPES.add(String.class);
        PRIM_TYPES.add(Boolean.class);
        PRIM_TYPES.add(boolean.class);
        PRIM_TYPES.add(Byte.class);
        PRIM_TYPES.add(byte.class);
        PRIM_TYPES.add(Short.class);
        PRIM_TYPES.add(short.class);
        PRIM_TYPES.add(Integer.class);
        PRIM_TYPES.add(int.class);
        PRIM_TYPES.add(Long.class);
        PRIM_TYPES.add(long.class);
        PRIM_TYPES.add(Float.class);
        PRIM_TYPES.add(float.class);
        PRIM_TYPES.add(Double.class);
        PRIM_TYPES.add(double.class);
        PRIM_TYPES.add(LocalDateTime.class);
        PRIM_TYPES.add(LocalDate.class);
        PRIM_TYPES.add(LocalTime.class);
        PRIM_TYPES.add(Character.class);
        PRIM_TYPES.add(char.class);
    }
    
    /**
     * Initializes an interface with given implementation class.
     * 
     * <p>
     * Warning: This method is not meant to be used by plugins.
     * </p>
     * 
     * @param <T> data fragment class (interface)
     * @param <Q> data fragment class (implementation)
     * 
     * @param interfaz Interface used in API
     * @param impl implementation class to be used by data sections
     */
    public static <T extends DataFragment, Q extends T> void initFragmentImplementation(Class<T> interfaz, Class<Q> impl)
    {
        if (fragmentOverrideLock)
        {
            fragmentImpls.putIfAbsent(interfaz, impl);
        }
        else
        {
            fragmentImpls.put(interfaz, impl);
        }
    }
    
    /**
     * Inits the unique enum value factory.
     * 
     * <p>
     * Warning: This method is not meant to be used by plugins.
     * </p>
     * 
     * @param factory the enum value factory to be used for resolving unique enumerations
     */
    public static void initUniqueEnumValueFactory(UniqueEnumValueFactory factory)
    {
        if (!fragmentOverrideLock)
        {
            uniqueEnumValueFactory = factory;
        }
    }
    
    /**
     * Locks the {@link #initFragmentImplementation(Class, Class)} method to not override already existing implementations.
     * 
     * <p>
     * Warning: This method is not meant to be used by plugins.
     * </p>
     */
    public static void lockFragmentImplementations()
    {
        fragmentOverrideLock = true;
    }
    
    /**
     * Checks if the fragment override lock was set before.
     * 
     * <p>
     * Warning: This method is not meant to be used by plugins.
     * </p>
     * 
     * @return {@code true} if method {@link #lockFragmentImplementations()} was called before.
     */
    public static boolean isFragmentImplementationLocked()
    {
        return fragmentOverrideLock;
    }
    
    /**
     * Constructor to create a new and empty data section stored into memory.
     */
    public MemoryDataSection()
    {
        // empty
    }
    
    /**
     * Constructor
     * 
     * @param path
     *            path of this section.
     * @param name
     *            name of this section.
     * @param parent
     *            parent
     */
    protected MemoryDataSection(String path, String name, MemoryDataSection parent)
    {
        this.path = path;
        this.name = name;
        this.parent = parent;
    }
    
    @Override
    public DataSection createSection(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            Object obj = this.contents.get(key);
            if (!(obj instanceof MemoryDataSection))
            {
                // override old primitive value or create the sub section if it was not yet created.
                obj = this.createSection(this.path == null ? key : this.path + '.' + key, key, this);
                this.contents.put(key, obj);
            }
            return (DataSection) obj;
        }
        final String subkey = key.substring(0, indexof);
        Object obj = this.contents.get(subkey);
        if (!(obj instanceof MemoryDataSection))
        {
            obj = this.createSection(this.path == null ? subkey : this.path + '.' + subkey, subkey, this);
            this.contents.put(subkey, obj);
        }
        return ((MemoryDataSection) obj).createSection(key.substring(indexof + 1));
    }
    
    @Override
    public DataSection createSection(String key, Map<String, ?> values)
    {
        final DataSection section = this.createSection(key);
        values.forEach(section::set);
        return section;
    }
    
    /**
     * Creates a new sub section.
     * @param path2 path of the new sub section
     * @param name2 name of the new sub section
     * @param parent2 parent section
     * @return sub section.
     */
    protected MemoryDataSection createSection(String path2, String name2, MemoryDataSection parent2)
    {
        return new MemoryDataSection(path2, name2, parent2);
    }
    
    @Override
    public Set<String> getKeys(boolean deep)
    {
        final Set<String> result = new LinkedHashSet<>();
        result.addAll(this.contents.keySet());
        if (deep)
        {
            this.contents.entrySet().stream().filter(o -> o.getValue() instanceof MemoryDataSection).forEach(entry -> {
                final MemoryDataSection child = (MemoryDataSection) entry.getValue();
                child.getKeys(true).stream().map(k -> entry.getKey() + '.' + k).forEach(result::add);
            });
        }
        return result;
    }
    
    @Override
    public Map<String, Object> getValues(boolean deep)
    {
        final Map<String, Object> result = new HashMap<>();
        this.contents.entrySet().forEach(entry -> {
            if (entry.getValue() instanceof MemoryDataSection)
            {
                if (deep)
                {
                    ((MemoryDataSection) entry.getValue()).getValues(true).forEach((key, value) -> result.put(entry.getKey() + '.' + key, value));
                }
            }
            else
            {
                result.put(entry.getKey(), entry.getValue());
            }
        });
        return result;
    }
    
    @Override
    public boolean contains(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            return this.contents.containsKey(key);
        }
        final Object obj = this.contents.get(key.substring(0, indexof));
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).contains(key.substring(indexof + 1));
        }
        return false;
    }
    
    @Override
    public String getCurrentPath()
    {
        return this.path;
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public DataSection getRoot()
    {
        return this.parent == null ? this : this.parent.getRoot();
    }
    
    @Override
    public DataSection getParent()
    {
        return this.parent;
    }
    
    @Override
    public Object get(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            return obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).get(key.substring(indexof + 1));
        }
        return null;
    }
    
    @Override
    public Object get(String key, Object defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (obj == null)
            {
                return defaultValue;
            }
            return obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).get(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public void set(String key, Object newValue)
    {
        if (newValue == null)
        {
            this.clear(key);
        }
        else if (PRIM_TYPES.contains(newValue.getClass()))
        {
            this.doSet(key, newValue);
        }
        else if (newValue instanceof DataFragment)
        {
            final DataFragment fragment = (DataFragment) newValue;
            final DataSection child = this.createSection(key);
            fragment.write(child);
        }
        else if (newValue instanceof UniqueEnumerationValue)
        {
            final UniqueEnumerationValue casted = (UniqueEnumerationValue) newValue;
            this.set(key + ".plugin", casted.getPluginName()); //$NON-NLS-1$
            this.set(key + ".name", casted.name()); //$NON-NLS-1$
        }
        else if (newValue instanceof EnumerationValue)
        {
            final EnumerationValue casted = (EnumerationValue) newValue;
            this.set(key + ".clazz", casted.getClass().getName()); //$NON-NLS-1$
            this.set(key + ".name", casted.name()); //$NON-NLS-1$
        }
        else if (newValue instanceof Enum<?>)
        {
            this.set(key, ((Enum<?>) newValue).name());
        }
        else
        {
            throw new IllegalArgumentException("Invalid type detected: " + newValue.getClass()); //$NON-NLS-1$
        }
    }
    
    /**
     * Clears contents for given key.
     * @param key the key that will be cleared; may contain '.' for nested keys
     */
    private void clear(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            this.contents.remove(key);
            return;
        }
        final Object obj = this.contents.get(key.substring(0, indexof));
        if (obj instanceof MemoryDataSection)
        {
            ((MemoryDataSection) obj).clear(key.substring(indexof + 1));
        }
    }
    
    /**
     * Sets a value for given key.
     * @param key the key to be set
     * @param value the new value
     */
    private void doSet(String key, Object value)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            this.contents.put(key, value);
            return;
        }
        ((MemoryDataSection) this.createSection(key.substring(0, indexof))).doSet(key.substring(indexof + 1), value);
    }
    
    @Override
    public void setPrimitiveList(String key, List<?> newValue)
    {
        final DataSection child = this.createSection(key);
        int i = 0;
        for (final Object elm : newValue)
        {
            child.set("item" + i, elm); //$NON-NLS-1$
            i++;
        }
    }
    
    @Override
    public <T> void setPrimitiveMapList(String key, List<Map<String, T>> newValue)
    {
        final DataSection child = this.createSection(key);
        int i = 0;
        final int size = newValue.size();
        for (final Map<String, ?> elm : newValue)
        {
            final DataSection mapchild = child.createSection(getIndexedKey(i, size, "map")); //$NON-NLS-1$
            elm.forEach(mapchild::set);
            i++;
        }
    }
    
    @Override
    public <T extends DataFragment> void setFragmentList(String key, List<T> newValue)
    {
        final DataSection child = this.createSection(key);
        int i = 0;
        final int size = newValue.size();
        for (final T elm : newValue)
        {
            final DataSection fragmentSection = child.createSection(getIndexedKey(i, size, "item")); //$NON-NLS-1$
            elm.write(fragmentSection);
            i++;
        }
    }
    
    /**
     * Returns an indexed key filled with nulls for better sorting.
     * @param i index number
     * @param size total size of list
     * @param prefix prefix to be used
     * @return index string key
     */
    private String getIndexedKey(int i, int size, String prefix)
    {
        final int digits = String.valueOf(size).length();
        return prefix + String.format("%0" + digits + "d", i); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Override
    public <T extends DataFragment> void setFragmentMapList(String key, List<Map<String, T>> newValue)
    {
        final DataSection child = this.createSection(key);
        int i = 0;
        for (final Map<String, T> elm : newValue)
        {
            final DataSection mapchild = child.createSection("map" + i); //$NON-NLS-1$
            elm.forEach(mapchild::set);
            i++;
        }
    }
    
    @Override
    public void setPrimitiveMap(String key, Map<String, ?> newValue)
    {
        final DataSection child = this.createSection(key);
        newValue.forEach(child::set);
    }
    
    @Override
    public <T> void setPrimitiveListMap(String key, Map<String, List<T>> newValue)
    {
        final DataSection child = this.createSection(key);
        for (final Map.Entry<String, List<T>> elm : newValue.entrySet())
        {
            final DataSection mapchild = child.createSection(elm.getKey());
            int i = 0;
            for (final Object obj : elm.getValue())
            {
                mapchild.set("item" + i, obj); //$NON-NLS-1$
                i++;
            }
        }
    }
    
    @Override
    public <T extends DataFragment> void setFragmentMap(String key, Map<String, T> newValue)
    {
        final DataSection child = this.createSection(key);
        newValue.forEach(child::set);
    }
    
    @Override
    public <T extends DataFragment> void setFragmentListMap(String key, Map<String, List<T>> newValue)
    {
        final DataSection child = this.createSection(key);
        newValue.forEach(child::setFragmentList);
    }
    
    @Override
    public DataSection getSection(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (obj instanceof MemoryDataSection)
            {
                return (DataSection) obj;
            }
            return null;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getSection(key.substring(indexof + 1));
        }
        return null;
    }
    
    @Override
    public boolean isSection(String key)
    {
        return this.getSection(key) != null;
    }
    
    @Override
    public String getString(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            return obj instanceof String ? (String) obj : null;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getString(key.substring(indexof + 1));
        }
        return null;
    }
    
    @Override
    public String getString(String key, String defaultValue)
    {
        final String result = this.getString(key);
        return result == null ? defaultValue : result;
    }
    
    @Override
    public boolean isString(String key)
    {
        return this.get(key) instanceof String;
    }
    
    @Override
    public int getInt(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return 0;
            }
            return ((Number) obj).intValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getInt(key.substring(indexof + 1));
        }
        return 0;
    }
    
    @Override
    public int getInt(String key, int defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return defaultValue;
            }
            return ((Number) obj).byteValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getInt(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isInt(String key)
    {
        return this.get(key) instanceof Number;
    }
    
    @Override
    public byte getByte(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return 0;
            }
            return ((Number) obj).byteValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getByte(key.substring(indexof + 1));
        }
        return 0;
    }
    
    @Override
    public byte getByte(String key, byte defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return defaultValue;
            }
            return ((Number) obj).byteValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getByte(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isByte(String key)
    {
        return this.get(key) instanceof Number;
    }
    
    @Override
    public short getShort(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return 0;
            }
            return ((Number) obj).shortValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getShort(key.substring(indexof + 1));
        }
        return 0;
    }
    
    @Override
    public short getShort(String key, short defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return defaultValue;
            }
            return ((Number) obj).shortValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getShort(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isShort(String key)
    {
        return this.get(key) instanceof Number;
    }
    
    @Override
    public char getCharacter(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Character))
            {
                return 0;
            }
            return (Character) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getCharacter(key.substring(indexof + 1));
        }
        return 0;
    }
    
    @Override
    public char getCharacter(String key, char defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Character))
            {
                return defaultValue;
            }
            return (Character) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getCharacter(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isCharacter(String key)
    {
        return this.get(key) instanceof Character;
    }
    
    @Override
    public LocalDateTime getDateTime(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof LocalDateTime))
            {
                return null;
            }
            return (LocalDateTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getDateTime(key.substring(indexof + 1));
        }
        return null;
    }
    
    @Override
    public LocalDateTime getDateTime(String key, LocalDateTime defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof LocalDateTime))
            {
                return defaultValue;
            }
            return (LocalDateTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getDateTime(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isDateTime(String key)
    {
        return this.get(key) instanceof LocalDateTime;
    }
    
    @Override
    public LocalDate getDate(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof LocalDate))
            {
                return null;
            }
            return (LocalDate) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getDate(key.substring(indexof + 1));
        }
        return null;
    }
    
    @Override
    public LocalDate getDate(String key, LocalDate defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof LocalDate))
            {
                return defaultValue;
            }
            return (LocalDate) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getDate(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isDate(String key)
    {
        return this.get(key) instanceof LocalDate;
    }
    
    @Override
    public LocalTime getTime(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof LocalTime))
            {
                return null;
            }
            return (LocalTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getTime(key.substring(indexof + 1));
        }
        return null;
    }
    
    @Override
    public LocalTime getTime(String key, LocalTime defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof LocalTime))
            {
                return defaultValue;
            }
            return (LocalTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getTime(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isTime(String key)
    {
        return this.get(key) instanceof LocalTime;
    }
    
    @Override
    public boolean getBoolean(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Boolean))
            {
                return false;
            }
            return (Boolean) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getBoolean(key.substring(indexof + 1));
        }
        return false;
    }
    
    @Override
    public boolean getBoolean(String key, boolean defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Boolean))
            {
                return defaultValue;
            }
            return (Boolean) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getBoolean(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isBoolean(String key)
    {
        return this.get(key) instanceof Boolean;
    }
    
    @Override
    public double getDouble(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return 0.0d;
            }
            return ((Number) obj).doubleValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getDouble(key.substring(indexof + 1));
        }
        return 0.0d;
    }
    
    @Override
    public double getDouble(String key, double defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return defaultValue;
            }
            return ((Number) obj).doubleValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getDouble(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isDouble(String key)
    {
        return this.get(key) instanceof Number;
    }
    
    @Override
    public float getFloat(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return 0.0f;
            }
            return ((Number) obj).floatValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getFloat(key.substring(indexof + 1));
        }
        return 0.0f;
    }
    
    @Override
    public float getFloat(String key, float defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return defaultValue;
            }
            return ((Number) obj).floatValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getFloat(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isFloat(String key)
    {
        return this.get(key) instanceof Number;
    }
    
    @Override
    public long getLong(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return 0;
            }
            return ((Number) obj).longValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getLong(key.substring(indexof + 1));
        }
        return 0;
    }
    
    @Override
    public long getLong(String key, long defaultValue)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (!(obj instanceof Number))
            {
                return defaultValue;
            }
            return ((Number) obj).longValue();
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection) obj).getLong(key.substring(indexof + 1), defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isLong(String key)
    {
        return this.get(key) instanceof Number;
    }
    
    @Override
    public List<?> getPrimitiveList(String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<Object> result = new ArrayList<>();
            for (final String ckey : new TreeSet<>(child.getKeys(false)))
            {
                result.add(child.get(ckey));
            }
            return result;
        }
        return null;
    }
    
    @Override
    public List<?> getPrimitiveList(String key, List<?> defaultValue)
    {
        final List<?> result = this.getPrimitiveList(key);
        return result == null ? defaultValue : result;
    }
    
    @Override
    public boolean isList(String key)
    {
        // every section can be interpreted as list
        return this.isSection(key);
    }
    
    /**
     * Safe cast (checks for invalid elements).
     * 
     * @param clazz target class.
     * @param list list of elements to be casted
     * @param <T> element class
     * @return casted list
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> safeListCast(Class<T> clazz, List<?> list)
    {
        if (list != null && list.stream().filter(o -> !clazz.isInstance(o)).findFirst().isPresent())
        {
            throw new ClassCastException("List contains at least one element of wrong type: " + clazz); //$NON-NLS-1$
        }
        return (List<T>) list;
    }
    
    @Override
    public List<String> getStringList(String key)
    {
        return this.safeListCast(String.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Integer> getIntegerList(String key)
    {
        return this.safeListCast(Integer.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Boolean> getBooleanList(String key)
    {
        return this.safeListCast(Boolean.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Double> getDoubleList(String key)
    {
        return this.safeListCast(Double.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Float> getFloatList(String key)
    {
        return this.safeListCast(Float.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Long> getLongList(String key)
    {
        return this.safeListCast(Long.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Byte> getByteList(String key)
    {
        return this.safeListCast(Byte.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Character> getCharacterList(String key)
    {
        return this.safeListCast(Character.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<Short> getShortList(String key)
    {
        return this.safeListCast(Short.class, this.getPrimitiveList(key));
    }
    
    @Override
    public List<VectorDataFragment> getVectorList(String key)
    {
        return this.getFragmentList(VectorDataFragment.class, key);
    }
    
    @Override
    public List<PlayerDataFragment> getPlayerList(String key)
    {
        return this.getFragmentList(PlayerDataFragment.class, key);
    }
    
    @Override
    public List<ItemStackDataFragment> getItemList(String key)
    {
        return this.getFragmentList(ItemStackDataFragment.class, key);
    }
    
    @Override
    public List<ColorDataFragment> getColorList(String key)
    {
        return this.getFragmentList(ColorDataFragment.class, key);
    }
    
    @Override
    public List<ServerDataFragment> getServerList(String key)
    {
        return this.getFragmentList(ServerDataFragment.class, key);
    }
    
    @Override
    public List<LocationDataFragment> getLocationList(String key)
    {
        return this.getFragmentList(LocationDataFragment.class, key);
    }
    
    @Override
    public List<BlockLocationDataFragment> getBlockLocationList(String key)
    {
        return this.getFragmentList(BlockLocationDataFragment.class, key);
    }
    
    @Override
    public List<ServerLocationDataFragment> getServerLocationList(String key)
    {
        return this.getFragmentList(ServerLocationDataFragment.class, key);
    }
    
    @Override
    public List<ServerBlockLocationDataFragment> getServerBlockLocationList(String key)
    {
        return this.getFragmentList(ServerBlockLocationDataFragment.class, key);
    }
    
    @Override
    public List<Map<String, ?>> getPrimitiveMapList(String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<Map<String, ?>> result = new ArrayList<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.add(child.getPrimitiveMap(subkey));
            }
            return result;
        }
        return null;
    }
    
    @Override
    public Map<String, ?> getPrimitiveMap(String key)
    {
        final DataSection section = this.getSection(key);
        return section == null ? null : section.getValues(false);
    }
    
    @Override
    public Map<String, List<?>> getPrimitiveListMap(String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final Map<String,List<?>> result = new HashMap<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.put(subkey, child.getPrimitiveList(subkey));
            }
            return result;
        }
        return null;
    }
    
    @Override
    public <T extends DataFragment> Map<String, T> getFragmentMap(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final Map<String, T> result = new HashMap<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.put(subkey, child.getFragment(clazz, subkey));
            }
            return result;
        }
        return null;
    }
    
    @Override
    public <T extends DataFragment> List<Map<String, T>> getFragmentMapList(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<Map<String, T>> result = new ArrayList<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.add(child.getFragmentMap(clazz, subkey));
            }
            return result;
        }
        return null;
    }
    
    @Override
    public <T extends DataFragment> Map<String, List<T>> getFragmentListMap(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final Map<String,List<T>> result = new HashMap<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.put(subkey, child.getFragmentList(clazz, subkey));
            }
            return result;
        }
        return null;
    }
    
    @Override
    public <T extends DataFragment> List<T> getFragmentList(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<T> result = new ArrayList<>();
            for (final String ckey : new TreeSet<>(child.getKeys(false)))
            {
                final T fragment = child.getFragment(clazz, ckey);
                if (fragment == null)
                {
                    throw new ClassCastException("Invalid data fragment."); //$NON-NLS-1$
                }
                result.add(fragment);
            }
            return result;
        }
        return null;
    }
    
    @Override
    public VectorDataFragment getVector(String key)
    {
        return this.getFragment(VectorDataFragment.class, key);
    }
    
    @Override
    public VectorDataFragment getVector(String key, VectorDataFragment defaultValue)
    {
        return this.getFragment(VectorDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isVector(String key)
    {
        return this.isFragment(VectorDataFragment.class, key);
    }
    
    @Override
    public PlayerDataFragment getPlayer(String key)
    {
        return this.getFragment(PlayerDataFragment.class, key);
    }
    
    @Override
    public PlayerDataFragment getPlayer(String key, PlayerDataFragment defaultValue)
    {
        return this.getFragment(PlayerDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isPlayer(String key)
    {
        return this.isFragment(PlayerDataFragment.class, key);
    }
    
    @Override
    public ItemStackDataFragment getItemStack(String key)
    {
        return this.getFragment(ItemStackDataFragment.class, key);
    }
    
    @Override
    public ItemStackDataFragment getItemStack(String key, ItemStackDataFragment defaultValue)
    {
        return this.getFragment(ItemStackDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isItemStack(String key)
    {
        return this.isFragment(ItemStackDataFragment.class, key);
    }
    
    @Override
    public ColorDataFragment getColor(String key)
    {
        return this.getFragment(ColorDataFragment.class, key);
    }
    
    @Override
    public ColorDataFragment getColor(String key, ColorDataFragment defaultValue)
    {
        return this.getFragment(ColorDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isColor(String key)
    {
        return this.isFragment(ColorDataFragment.class, key);
    }
    
    @Override
    public LocationDataFragment getLocation(String key)
    {
        return this.getFragment(LocationDataFragment.class, key);
    }
    
    @Override
    public LocationDataFragment getLocation(String key, LocationDataFragment defaultValue)
    {
        return this.getFragment(LocationDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isLocation(String key)
    {
        return this.isFragment(LocationDataFragment.class, key);
    }
    
    @Override
    public BlockLocationDataFragment getBlockLocation(String key)
    {
        return this.getFragment(BlockLocationDataFragment.class, key);
    }
    
    @Override
    public BlockLocationDataFragment getBlockLocation(String key, BlockLocationDataFragment defaultValue)
    {
        return this.getFragment(BlockLocationDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isBlockLocation(String key)
    {
        return this.isFragment(BlockLocationDataFragment.class, key);
    }
    
    @Override
    public ServerLocationDataFragment getServerLocation(String key)
    {
        return this.getFragment(ServerLocationDataFragment.class, key);
    }
    
    @Override
    public ServerLocationDataFragment getServerLocation(String key, ServerLocationDataFragment defaultValue)
    {
        return this.getFragment(ServerLocationDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isServerLocation(String key)
    {
        return this.isFragment(ServerLocationDataFragment.class, key);
    }
    
    @Override
    public ServerBlockLocationDataFragment getServerBlockLocation(String key)
    {
        return this.getFragment(ServerBlockLocationDataFragment.class, key);
    }
    
    @Override
    public ServerBlockLocationDataFragment getServerBlockLocation(String key, ServerBlockLocationDataFragment defaultValue)
    {
        return this.getFragment(ServerBlockLocationDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isServerBlockLocation(String key)
    {
        return this.isFragment(ServerBlockLocationDataFragment.class, key);
    }
    
    @Override
    public ServerDataFragment getServer(String key)
    {
        return this.getFragment(ServerDataFragment.class, key);
    }
    
    @Override
    public ServerDataFragment getServer(String key, ServerDataFragment defaultValue)
    {
        return this.getFragment(ServerDataFragment.class, key, defaultValue);
    }
    
    @Override
    public boolean isServer(String key)
    {
        return this.isFragment(ServerDataFragment.class, key);
    }
    
    @Override
    public <T extends DataFragment> T getFragment(Class<T> clazz, String key)
    {
        final DataSection section = this.getSection(key);
        if (section == null)
        {
            return null;
        }
        final T result = this.safeCreate(clazz);
        if (!result.test(section))
        {
            throw new ClassCastException("Invalid data fragment."); //$NON-NLS-1$
        }
        result.read(section);
        return result;
    }
    
    @Override
    public <T extends DataFragment> T getFragment(Class<T> clazz, String key, T defaultValue)
    {
        final DataSection section = this.getSection(key);
        if (section == null)
        {
            return defaultValue;
        }
        final T result = this.safeCreate(clazz);
        if (!result.test(section))
        {
            return defaultValue;
        }
        result.read(section);
        return result;
    }
    
    @Override
    public <T extends DataFragment> boolean isFragment(Class<T> clazz, String key)
    {
        final DataSection section = this.getSection(key);
        if (section == null)
        {
            // reading null is not safe
            return false;
        }
        return this.safeCreate(clazz).test(section);
    }
    
    /**
     * Safe create instances of given class.
     * 
     * @param clazz target class of new instance
     * @param <T> data fragment class
     * @return new instance
     */
    private <T extends DataFragment> T safeCreate(Class<T> clazz)
    {
        try
        {
            final Class<?> clazz2 = fragmentImpls.get(clazz);
            if (clazz2 != null)
            {
                return clazz.cast(clazz2.newInstance());
            }
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public void setSection(String key, DataSection newValue)
    {
        final MemoryDataSection section = (MemoryDataSection) this.createSection(key);
        section.contents.clear();
        newValue.getValues(true).forEach(section::set);
    }
    
    /**
     * Clears all content.
     */
    protected void clearAll()
    {
        this.contents.clear();
    }

    @Override
    public <T extends EnumerationValue> T getEnumValue(Class<T> clazz, String key)
    {
        if (UniqueEnumerationValue.class.isAssignableFrom(clazz))
        {
            final DataSection child = this.getSection(key);
            if (child == null)
            {
                return null;
            }
            final String plugin = child.getString("plugin"); //$NON-NLS-1$
            final String n = child.getString("name"); //$NON-NLS-1$
            final Class<? extends UniqueEnumerationValue> clazz2 = clazz.asSubclass(UniqueEnumerationValue.class);
            return clazz.cast(uniqueEnumValueFactory.create(plugin, n, clazz2));
        }
        final DataSection child = this.getSection(key);
        if (child == null)
        {
            return null;
        }
        final String className = child.getString("clazz"); //$NON-NLS-1$
        if (className == null)
        {
            return null;
        }
        try
        {
            return clazz.cast(child.getEnum(Class.forName(className).asSubclass(Enum.class), "name")); //$NON-NLS-1$
        }
        catch (@SuppressWarnings("unused") ClassNotFoundException | ClassCastException e)
        {
            // silently ignore
            return null;
        }
    }

    @Override
    public <T extends EnumerationValue> T getEnumValue(Class<T> clazz, String key, T defaultValue)
    {
        final T result = this.getEnumValue(clazz, key);
        return result == null ? defaultValue : result;
    }

    @Override
    public <T extends Enum<?>> T getEnum(Class<T> clazz, String key)
    {
        final String n = this.getString(key);
        if (n == null)
        {
            return null;
        }
        for (final T constant : clazz.getEnumConstants())
        {
            if (constant.name().equals(n))
            {
                return constant;
            }
        }
        return null;
    }

    @Override
    public <T extends Enum<?>> T getEnum(Class<T> clazz, String key, T defaultValue)
    {
        final T result = this.getEnum(clazz, key);
        return result == null ? defaultValue : result;
    }

    @Override
    public <T extends EnumerationValue> List<T> getEnumValueList(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<T> result = new ArrayList<>();
            for (final String ckey : new TreeSet<>(child.getKeys(false)))
            {
                final T fragment = child.getEnumValue(clazz, ckey);
                if (fragment == null)
                {
                    throw new ClassCastException("Invalid enum."); //$NON-NLS-1$
                }
                result.add(fragment);
            }
            return result;
        }
        return null;
    }

    @Override
    public <T extends EnumerationValue> Map<String, T> getEnumValueMap(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final Map<String, T> result = new HashMap<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.put(subkey, child.getEnumValue(clazz, subkey));
            }
            return result;
        }
        return null;
    }

    @Override
    public <T extends EnumerationValue> List<Map<String, T>> getEnumValueMapList(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<Map<String, T>> result = new ArrayList<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.add(child.getEnumValueMap(clazz, subkey));
            }
            return result;
        }
        return null;
    }

    @Override
    public <T extends EnumerationValue> Map<String, List<T>> getEnumValueListMap(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final Map<String,List<T>> result = new HashMap<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.put(subkey, child.getEnumValueList(clazz, subkey));
            }
            return result;
        }
        return null;
    }

    @Override
    public <T extends Enum<?>> List<T> getEnumList(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<T> result = new ArrayList<>();
            for (final String ckey : new TreeSet<>(child.getKeys(false)))
            {
                final T fragment = child.getEnum(clazz, ckey);
                if (fragment == null)
                {
                    throw new ClassCastException("Invalid enum."); //$NON-NLS-1$
                }
                result.add(fragment);
            }
            return result;
        }
        return null;
    }

    @Override
    public <T extends Enum<?>> Map<String, T> getEnumMap(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final Map<String, T> result = new HashMap<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.put(subkey, child.getEnum(clazz, subkey));
            }
            return result;
        }
        return null;
    }

    @Override
    public <T extends Enum<?>> List<Map<String, T>> getEnumMapList(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final List<Map<String, T>> result = new ArrayList<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.add(child.getEnumMap(clazz, subkey));
            }
            return result;
        }
        return null;
    }

    @Override
    public <T extends Enum<?>> Map<String, List<T>> getEnumListMap(Class<T> clazz, String key)
    {
        final DataSection child = this.getSection(key);
        if (child != null)
        {
            final Map<String,List<T>> result = new HashMap<>();
            for (final String subkey : new TreeSet<>(child.getKeys(false)))
            {
                result.put(subkey, child.getEnumList(clazz, subkey));
            }
            return result;
        }
        return null;
    }
    
    /**
     * Helper to create unique enums.
     */
    @FunctionalInterface
    public interface UniqueEnumValueFactory
    {
        /**
         * Creates enum value for given plugin/name and class type.
         * @param plugin plugin name
         * @param name enum value name
         * @param clazz target class
         * @param <T> enum class
         * @return enum value or {@code null} if the given enum value was not registered
         */
        <T extends UniqueEnumerationValue> T create(String plugin, String name, Class<T> clazz);
    }
    
}
