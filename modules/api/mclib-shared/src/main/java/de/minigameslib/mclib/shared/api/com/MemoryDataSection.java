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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A memory implementation of a data section.
 * 
 * @author mepeisen
 */
public class MemoryDataSection implements DataSection
{
    
    /** path of this section. */
    private String path;
    /** name of this section. */
    private String name;
    /** parent reference. */
    private MemoryDataSection parent;
    
    /** map contents. */
    private final Map<String, Object> contents = new HashMap<>();
    
    /** the primitive types directly supported by DataSection. */
    static final Set<Class<?>> PRIM_TYPES = new HashSet<>();
    
    {
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
     * Constructor
     */
    public MemoryDataSection()
    {
        // empty
    }
    
    /**
     * Constructor
     * @param path path of this section.
     * @param name name of this section.
     * @param parent parent
     */
    private MemoryDataSection(String path, String name, MemoryDataSection parent)
    {
        this.path = path;
        this.name = name;
        this.parent = parent;
    }
    
    @Override
    public Set<String> getKeys(boolean deep)
    {
        final Set<String> result = new HashSet<>();
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
                    ((MemoryDataSection)entry.getValue()).getValues(true).forEach((key, value) -> result.put(entry.getKey() + '.' + key, value));
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
            return ((MemoryDataSection)obj).contains(key.substring(indexof) + 1);
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
        return this.contents.get(key);
    }
    
    @Override
    public Object get(String key, Object defaultValue)
    {
        return this.contents.getOrDefault(key, defaultValue);
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
        else
        {
            throw new IllegalArgumentException("Invalid type detected: " + newValue.getClass()); //$NON-NLS-1$
        }
    }
    
    /**
     * @param key
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
            ((MemoryDataSection)obj).clear(key.substring(indexof) + 1);
        }
    }
    
    /**
     * @param key
     * @param value
     */
    private void doSet(String key, Object value)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            this.contents.put(key, value);
            return;
        }
        final Object obj = this.contents.get(key.substring(0, indexof));
        if (obj instanceof MemoryDataSection)
        {
            ((MemoryDataSection)obj).doSet(key.substring(indexof) + 1, value);
        }
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
    public void setPrimitiveMapList(String key, List<Map<String, ?>> newValue)
    {
        final DataSection child = this.createSection(key);
        int i = 0;
        for (final Map<String, ?> elm : newValue)
        {
            final DataSection mapchild = child.createSection("map" + i); //$NON-NLS-1$
            elm.forEach(mapchild::set);
            i++;
        }
    }
    
    @Override
    public <T extends DataFragment> void setFragmentList(String key, List<T> newValue)
    {
        final DataSection child = this.createSection(key);
        int i = 0;
        for (final T elm : newValue)
        {
            final DataSection fragmentSection = child.createSection("item" + i); //$NON-NLS-1$
            elm.write(fragmentSection);
            i++;
        }
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
    public void setPrimitiveListMap(String key, Map<String, List<?>> newValue)
    {
        final DataSection child = this.createSection(key);
        for (final Map.Entry<String, List<?>> elm : newValue.entrySet())
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
    public DataSection createSection(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            Object obj = this.contents.get(key);
            if (!(obj instanceof MemoryDataSection))
            {
                // override old primitive value or create the sub section if it was not yet created.
                obj = new MemoryDataSection(this.path == null ? key : this.path + '.' + key, key, this);
                this.contents.put(key, obj);
            }
            return (DataSection) obj;
        }
        final String subkey = key.substring(0, indexof);
        Object obj = this.contents.get(subkey);
        if (!(obj instanceof MemoryDataSection))
        {
            obj = new MemoryDataSection(this.path == null ? subkey : this.path + '.' + subkey, subkey, this);
            this.contents.put(subkey, obj);
        }
        return ((MemoryDataSection)obj).createSection(key.substring(indexof) + 1);
    }
    
    @Override
    public DataSection createSection(String key, Map<String, ?> values)
    {
        final DataSection section = this.createSection(key);
        values.forEach(section::set);
        return section;
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
            return ((MemoryDataSection)obj).getSection(key.substring(indexof) + 1);
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
            return (String) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getString(key.substring(indexof) + 1);
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
            if (obj == null) return 0;
            return (Integer) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getInt(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Integer) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getInt(key.substring(indexof) + 1, defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isInt(String key)
    {
        return this.get(key) instanceof Integer;
    }
    
    @Override
    public byte getByte(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (obj == null) return 0;
            return (Byte) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getByte(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Byte) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getByte(key.substring(indexof) + 1, defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isByte(String key)
    {
        return this.get(key) instanceof Byte;
    }
    
    @Override
    public short getShort(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (obj == null) return 0;
            return (Short) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getShort(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Short) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getShort(key.substring(indexof) + 1, defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isShort(String key)
    {
        return this.get(key) instanceof Short;
    }
    
    @Override
    public char getCharacter(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (obj == null) return 0;
            return (Character) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getCharacter(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Character) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getCharacter(key.substring(indexof) + 1, defaultValue);
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
            return (LocalDateTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getDateTime(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (LocalDateTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getDateTime(key.substring(indexof) + 1, defaultValue);
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
            return (LocalDate) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getDate(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (LocalDate) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getDate(key.substring(indexof) + 1, defaultValue);
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
            return (LocalTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getTime(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (LocalTime) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getTime(key.substring(indexof) + 1, defaultValue);
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
            if (obj == null) return false;
            return (Boolean) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getBoolean(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Boolean) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getBoolean(key.substring(indexof) + 1, defaultValue);
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
            if (obj == null) return 0.0d;
            return (Double) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getDouble(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Double) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getDouble(key.substring(indexof) + 1, defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isDouble(String key)
    {
        return this.get(key) instanceof Double;
    }
    
    @Override
    public float getFloat(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (obj == null) return 0.0f;
            return (Float) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getFloat(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Float) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getFloat(key.substring(indexof) + 1, defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isFloat(String key)
    {
        return this.get(key) instanceof Float;
    }
    
    @Override
    public long getLong(String key)
    {
        int indexof = key.indexOf('.');
        if (indexof == -1)
        {
            final Object obj = this.contents.get(key);
            if (obj == null) return 0;
            return (Long) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getLong(key.substring(indexof) + 1);
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
            if (obj == null) return defaultValue;
            return (Long) obj;
        }
        final String subkey = key.substring(0, indexof);
        final Object obj = this.contents.get(subkey);
        if (obj instanceof MemoryDataSection)
        {
            return ((MemoryDataSection)obj).getLong(key.substring(indexof) + 1, defaultValue);
        }
        return defaultValue;
    }
    
    @Override
    public boolean isLong(String key)
    {
        return this.get(key) instanceof Long;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getList(java.lang.String)
     */
    @Override
    public List<?> getList(String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getList(java.lang.String, java.util.List)
     */
    @Override
    public List<?> getList(String key, List<?> defaultValue)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean isList(String key)
    {
        // every section can be interpreted as list
        return this.isSection(key);
    }
    
    /**
     * Safe cast (checks for invalid elements)
     * @param clazz
     * @param list
     * @return casted list
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> safeListCast(Class<T> clazz, List<?> list)
    {
        if (list.stream().filter(o -> !clazz.isInstance(o)).findFirst().isPresent())
        {
            throw new ClassCastException("List contains at least one element of wrong type: " + clazz); //$NON-NLS-1$
        }
        return (List<T>) list;
    }
    
    @Override
    public List<String> getStringList(String key)
    {
        return this.safeListCast(String.class, this.getList(key));
    }
    
    @Override
    public List<Integer> getIntegerList(String key)
    {
        return this.safeListCast(Integer.class, this.getList(key));
    }
    
    @Override
    public List<Boolean> getBooleanList(String key)
    {
        return this.safeListCast(Boolean.class, this.getList(key));
    }
    
    @Override
    public List<Double> getDoubleList(String key)
    {
        return this.safeListCast(Double.class, this.getList(key));
    }
    
    @Override
    public List<Float> getFloatList(String key)
    {
        return this.safeListCast(Float.class, this.getList(key));
    }
    
    @Override
    public List<Long> getLongList(String key)
    {
        return this.safeListCast(Long.class, this.getList(key));
    }
    
    @Override
    public List<Byte> getByteList(String key)
    {
        return this.safeListCast(Byte.class, this.getList(key));
    }
    
    @Override
    public List<Character> getCharacterList(String key)
    {
        return this.safeListCast(Character.class, this.getList(key));
    }
    
    @Override
    public List<Short> getShortList(String key)
    {
        return this.safeListCast(Short.class, this.getList(key));
    }
    
    @Override
    public List<VectorData> getVectorList(String key)
    {
        return this.getFragmentList(VectorData.class, key);
    }
    
    @Override
    public List<PlayerData> getPlayerList(String key)
    {
        return this.getFragmentList(PlayerData.class, key);
    }
    
    @Override
    public List<ItemStackData> getItemList(String key)
    {
        return this.getFragmentList(ItemStackData.class, key);
    }
    
    @Override
    public List<ColorData> getColorList(String key)
    {
        return this.getFragmentList(ColorData.class, key);
    }
    
    @Override
    public List<Map<String, ?>> getMapList(String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getMap(java.lang.String)
     */
    @Override
    public Map<String, ?> getMap(String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getListMap(java.lang.String)
     */
    @Override
    public Map<String, List<?>> getListMap(String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getFragmentMap(java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends DataFragment> Map<String, T> getFragmentMap(Class<T> clazz, String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getFragmentMapList(java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends DataFragment> List<Map<String, T>> getFragmentMapList(Class<T> clazz, String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getFragmentListMap(java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends DataFragment> Map<String, List<T>> getFragmentListMap(Class<T> clazz, String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mclib.shared.api.com.DataSection#getFragmentList(java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends DataFragment> List<T> getFragmentList(Class<T> clazz, String key)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public VectorData getVector(String key)
    {
        return this.getFragment(VectorData.class, key);
    }
    
    @Override
    public VectorData getVector(String key, VectorData defaultValue)
    {
        return this.getFragment(VectorData.class, key, defaultValue);
    }
    
    @Override
    public boolean isVectorData(String key)
    {
        return this.isFragment(VectorData.class, key);
    }
    
    @Override
    public PlayerData getPlayer(String key)
    {
        return this.getFragment(PlayerData.class, key);
    }
    
    @Override
    public PlayerData getPlayer(String key, PlayerData defaultValue)
    {
        return this.getFragment(PlayerData.class, key, defaultValue);
    }
    
    @Override
    public boolean isPlayer(String key)
    {
        return this.isFragment(PlayerData.class, key);
    }
    
    @Override
    public ItemStackData getItemStack(String key)
    {
        // TODO This won't work, ItemStackData is an interface :-(
        return this.getFragment(ItemStackData.class, key);
    }
    
    @Override
    public ItemStackData getItemStack(String key, ItemStackData defaultValue)
    {
        // TODO This won't work, ItemStackData is an interface :-(
        return this.getFragment(ItemStackData.class, key, defaultValue);
    }
    
    @Override
    public boolean isItemStack(String key)
    {
        // TODO This won't work, ItemStackData is an interface :-(
        return this.isFragment(ItemStackData.class, key);
    }
    
    @Override
    public ColorData getColor(String key)
    {
        return this.getFragment(ColorData.class, key);
    }
    
    @Override
    public ColorData getColor(String key, ColorData defaultValue)
    {
        return this.getFragment(ColorData.class, key, defaultValue);
    }
    
    @Override
    public boolean isColor(String key)
    {
        return this.isFragment(ColorData.class, key);
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
     * Safe create instances of given class
     * @param clazz
     * @return new instance
     */
    private <T extends DataFragment> T safeCreate(Class<T> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
}
