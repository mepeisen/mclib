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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data fragment build by annotated fields.
 * 
 * @author mepeisen
 */
public abstract class AnnotatedDataFragment implements DataFragment
{
    
    /**
     * data fragment descriptors.
     */
    private static final Map<Class<?>, DataDescriptor> DESCRIPTORS = new ConcurrentHashMap<>();
    
    /** simple field types. */
    static final Map<Class<?>, PrimitiveFieldType> FIELD_TYPES = new HashMap<>();
    
    /** the primitive types directly supported by DataSection. */
    static final Set<Class<?>> PRIM_TYPES = new HashSet<>();
    
    {
        FIELD_TYPES.put(String.class, PrimitiveFieldType.Str);
        FIELD_TYPES.put(Boolean.class, PrimitiveFieldType.Boolean);
        FIELD_TYPES.put(boolean.class, PrimitiveFieldType.Boolean);
        FIELD_TYPES.put(Byte.class, PrimitiveFieldType.Byte);
        FIELD_TYPES.put(byte.class, PrimitiveFieldType.Byte);
        FIELD_TYPES.put(Short.class, PrimitiveFieldType.Short);
        FIELD_TYPES.put(short.class, PrimitiveFieldType.Short);
        FIELD_TYPES.put(Integer.class, PrimitiveFieldType.Integer);
        FIELD_TYPES.put(int.class, PrimitiveFieldType.Integer);
        FIELD_TYPES.put(Long.class, PrimitiveFieldType.Long);
        FIELD_TYPES.put(long.class, PrimitiveFieldType.Long);
        FIELD_TYPES.put(Float.class, PrimitiveFieldType.Float);
        FIELD_TYPES.put(float.class, PrimitiveFieldType.Float);
        FIELD_TYPES.put(Double.class, PrimitiveFieldType.Double);
        FIELD_TYPES.put(double.class, PrimitiveFieldType.Double);
        FIELD_TYPES.put(LocalDateTime.class, PrimitiveFieldType.DateTime);
        FIELD_TYPES.put(LocalDate.class, PrimitiveFieldType.Date);
        FIELD_TYPES.put(LocalTime.class, PrimitiveFieldType.Time);
        FIELD_TYPES.put(Character.class, PrimitiveFieldType.Character);
        FIELD_TYPES.put(char.class, PrimitiveFieldType.Character);
        
        PRIM_TYPES.addAll(FIELD_TYPES.keySet());
        
        FIELD_TYPES.put(VectorDataFragment.class, PrimitiveFieldType.Vector);
        FIELD_TYPES.put(PlayerDataFragment.class, PrimitiveFieldType.Player);
        FIELD_TYPES.put(ItemStackDataFragment.class, PrimitiveFieldType.ItemStack);
        FIELD_TYPES.put(ColorDataFragment.class, PrimitiveFieldType.Color);
        FIELD_TYPES.put(DataSection.class, PrimitiveFieldType.DataSection);
    }

    @Override
    public void read(DataSection section)
    {
        final DataDescriptor descriptor = DESCRIPTORS.computeIfAbsent(this.getClass(), DataDescriptor::new);
        for (final Map.Entry<String, FieldDescriptor> entry : descriptor.fields.entrySet())
        {
            final String name = entry.getKey();
            if (section.contains(name))
            {
                final FieldDescriptor fdesc = entry.getValue();
                Object value = null;
                if (fdesc.isFragment)
                {
                    value = section.getFragment(fdesc.elementType, name);
                }
                else if (fdesc.isFragmentList)
                {
                    value = section.getFragmentList(fdesc.elementType, name);
                }
                else if (fdesc.isFragmentMap)
                {
                    value = section.getFragmentMap(fdesc.elementType, name);
                }
                else if (fdesc.isJavaEnum)
                {
                    value = section.getEnum(fdesc.javaEnumType, name);
                }
                else if (fdesc.isJavaEnumList)
                {
                    value = section.getEnumList(fdesc.javaEnumType, name);
                }
                else if (fdesc.isJavaEnumMap)
                {
                    value = section.getEnumMap(fdesc.javaEnumType, name);
                }
                else if (fdesc.isEnum)
                {
                    value = section.getEnumValue(fdesc.normalEnumType, name);
                }
                else if (fdesc.isEnumList)
                {
                    value = section.getEnumValueList(fdesc.normalEnumType, name);
                }
                else if (fdesc.isEnumMap)
                {
                    value = section.getEnumValueMap(fdesc.normalEnumType, name);
                }
                else if (fdesc.isUniqueEnum)
                {
                    value = section.getEnumValue(fdesc.uniqueEnumType, name);
                }
                else if (fdesc.isUniqueEnumList)
                {
                    value = section.getEnumValueList(fdesc.uniqueEnumType, name);
                }
                else if (fdesc.isUniqueEnumMap)
                {
                    value = section.getEnumValueMap(fdesc.uniqueEnumType, name);
                }
                else
                {
                    value = fdesc.primitiveType.get(name, section);
                }
                try
                {
                    fdesc.field.set(this, value);
                }
                catch (Exception e)
                {
                    throw new IllegalStateException("error setting field " + name + " of class " + this.getClass(), e); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(DataSection section)
    {
        final DataDescriptor descriptor = DESCRIPTORS.computeIfAbsent(this.getClass(), DataDescriptor::new);
        for (final Map.Entry<String, FieldDescriptor> entry : descriptor.fields.entrySet())
        {
            final String name = entry.getKey();
            final FieldDescriptor fdesc = entry.getValue();
            try
            {
                final Object value = fdesc.field.get(this);

                if (fdesc.isFragment)
                {
                    section.set(name, value);
                }
                else if (fdesc.isFragmentList)
                {
                    section.setFragmentList(name, (List<? extends DataFragment>) value);
                }
                else if (fdesc.isFragmentMap)
                {
                    section.setFragmentMap(name, (Map<String, ? extends DataFragment>) value);
                }
                else if (fdesc.isJavaEnum)
                {
                    section.set(name, value);
                }
                else if (fdesc.isJavaEnumList)
                {
                    section.setPrimitiveList(name, (List<?>) value);
                }
                else if (fdesc.isJavaEnumMap)
                {
                    section.setPrimitiveMap(name, (Map<String, ?>) value);
                }
                else if (fdesc.isEnum)
                {
                    section.set(name, value);
                }
                else if (fdesc.isEnumList)
                {
                    section.setPrimitiveList(name, (List<?>) value);
                }
                else if (fdesc.isEnumMap)
                {
                    section.setPrimitiveMap(name, (Map<String, ?>) value);
                }
                else if (fdesc.isUniqueEnum)
                {
                    section.set(name, value);
                }
                else if (fdesc.isUniqueEnumList)
                {
                    section.setPrimitiveList(name, (List<?>) value);
                }
                else if (fdesc.isUniqueEnumMap)
                {
                    section.setPrimitiveMap(name, (Map<String, ?>) value);
                }
                else
                {
                    fdesc.primitiveType.set(name, section, value);
                }
            }
            catch (Exception e)
            {
                throw new IllegalStateException("error writing field " + name + " of class " + this.getClass(), e); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }
    
    @Override
    public boolean test(DataSection section)
    {
        final DataDescriptor descriptor = DESCRIPTORS.computeIfAbsent(this.getClass(), DataDescriptor::new);
        for (final Map.Entry<String, FieldDescriptor> entry : descriptor.fields.entrySet())
        {
            final String name = entry.getKey();
            if (section.contains(name))
            {
                final FieldDescriptor fdesc = entry.getValue();
                if (fdesc.isFragment)
                {
                    if (!section.isFragment(fdesc.elementType, name))
                    {
                        return false;
                    }
                }
                else if (fdesc.isFragmentList || fdesc.isFragmentMap)
                {
                    if (!section.isSection(name))
                    {
                        return false;
                    }
                    final DataSection sub = section.getSection(name);
                    for (final String subkey : sub.getKeys(false))
                    {
                        if (!sub.isFragment(fdesc.elementType, subkey))
                        {
                            return false;
                        }
                    }
                }
                else
                {
                    if (!fdesc.primitiveType.test(name, section))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** a data class descriptor. */
    private static final class DataDescriptor
    {
        
        /** the fields. */
        Map<String, FieldDescriptor> fields = new HashMap<>();

        /**
         * Constructor.
         * @param clazz
         */
        public DataDescriptor(Class<?> clazz)
        {
            addFieldDesc(clazz);
        }

        /**
         * @param clazz
         */
        private void addFieldDesc(Class<?> clazz)
        {
            final Class<?> superClazz = clazz.getSuperclass();
            if (superClazz != AnnotatedDataFragment.class)
            {
                addFieldDesc(superClazz);
            }
            for (final Field field : clazz.getDeclaredFields())
            {
                if (field.getAnnotation(PersistentField.class) != null)
                {
                    this.fields.put(field.getName(), new FieldDescriptor(field));
                }
            }
        }
        
    }
    
    /**
     * A descriptor describing a single field.
     */
    private static final class FieldDescriptor
    {
        
        /** the field to be used. */
        final Field field;
        
        /** primitive type. */
        PrimitiveFieldType primitiveType = null;

        /** the target field type. */
        Class<?> clazz;
        
        /** true if this is a list of data fragments. */
        boolean isFragmentList;
        
        /** true if this is a map to data fragments. */
        boolean isFragmentMap;

        /** the list or map element types. */
        Class<? extends DataFragment> elementType;

        /** the list or map element types. */
        Class<? extends UniqueEnumerationValue> uniqueEnumType;

        /** the list or map element types. */
        @SuppressWarnings("rawtypes")
        Class<? extends Enum> javaEnumType;

        /** the list or map element types. */
        Class<? extends EnumerationValue> normalEnumType;

        /** true if this is a data fragment. */
        boolean isFragment;
        
        /** true for java enums */
        boolean isJavaEnum;
        
        /** true for unique enums */
        boolean isUniqueEnum;
        
        /** true for normal enums */
        boolean isEnum;
        
        /** true if this is a list of java enums. */
        boolean isJavaEnumList;
        
        /** true if this is a list of unique enums. */
        boolean isUniqueEnumList;
        
        /** true if this is a list of normal enums. */
        boolean isEnumList;
        
        /** true if this is a map of java enums. */
        boolean isJavaEnumMap;
        
        /** true if this is a map of unique enums. */
        boolean isUniqueEnumMap;
        
        /** true if this is a map of normal enums. */
        boolean isEnumMap;
        
        /**
         * Constructor.
         * @param field
         */
        public FieldDescriptor(Field field)
        {
            this.field = field;
            this.field.setAccessible(true);
            this.clazz = field.getType();
            this.primitiveType = FIELD_TYPES.get(this.clazz);
            if (this.primitiveType == null)
            {
                if (this.clazz.equals(List.class))
                {
                    final ParameterizedType ptype = (ParameterizedType) field.getGenericType();
                    final Class<?> listType = (Class<?>) ptype.getActualTypeArguments()[0];
                    if (VectorDataFragment.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.VectorList;
                    }
                    else if (ItemStackDataFragment.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.ItemStackList;
                    }
                    else if (ColorDataFragment.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.ColorList;
                    }
                    else if (PlayerDataFragment.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.PlayerList;
                    }
                    else if (DataFragment.class.isAssignableFrom(listType))
                    {
                        this.isFragmentList = true;
                        this.elementType = listType.asSubclass(DataFragment.class);
                    }
                    else if (UniqueEnumerationValue.class.isAssignableFrom(listType))
                    {
                        this.isUniqueEnumList = true;
                        this.uniqueEnumType = listType.asSubclass(UniqueEnumerationValue.class);
                    }
                    else if (EnumerationValue.class.isAssignableFrom(listType))
                    {
                        this.isFragmentList = true;
                        this.normalEnumType = listType.asSubclass(EnumerationValue.class);
                    }
                    else if (Enum.class.isAssignableFrom(listType))
                    {
                        this.isJavaEnumList = true;
                        this.javaEnumType = listType.asSubclass(Enum.class);
                    }
                    else if (PRIM_TYPES.contains(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.PrimitiveList;
                    } 
                    // TODO support map list etc.
//                    else if (Map.class.isAssignableFrom(elementType))
//                    {
//                        
//                    }
                    else
                    {
                        throw new IllegalStateException("Unsupported list type detected. " + listType); //$NON-NLS-1$
                    }
                }
                else if (this.clazz.equals(Map.class))
                {
                    final ParameterizedType ptype = (ParameterizedType) field.getGenericType();
                    final Class<?> keyType = (Class<?>) ptype.getActualTypeArguments()[0];
                    final Class<?> valueType = (Class<?>) ptype.getActualTypeArguments()[1];
                    if (!PRIM_TYPES.contains(keyType))
                    {
                        throw new IllegalStateException("Unsupported map key type detected. " + keyType); //$NON-NLS-1$
                    }
                    if (PRIM_TYPES.contains(valueType))
                    {
                        this.primitiveType = PrimitiveFieldType.Map;
                    }
                    else if (DataFragment.class.isAssignableFrom(valueType))
                    {
                        this.isFragmentMap = true;
                        this.elementType = valueType.asSubclass(DataFragment.class);
                    }
                    else if (UniqueEnumerationValue.class.isAssignableFrom(valueType))
                    {
                        this.isUniqueEnumMap = true;
                        this.uniqueEnumType = valueType.asSubclass(UniqueEnumerationValue.class);
                    }
                    else if (EnumerationValue.class.isAssignableFrom(valueType))
                    {
                        this.isFragmentMap = true;
                        this.normalEnumType = valueType.asSubclass(EnumerationValue.class);
                    }
                    else if (Enum.class.isAssignableFrom(valueType))
                    {
                        this.isJavaEnumMap = true;
                        this.javaEnumType = valueType.asSubclass(Enum.class);
                    }
                    // TODO support list map etc.
//                  else if (List.class.isAssignableFrom(valueType))
//                  {
//                      
//                  }
                    else
                    {
                        throw new IllegalStateException("Unsupported list value type detected. " + valueType); //$NON-NLS-1$
                    }
                }
                else if (DataFragment.class.isAssignableFrom(this.clazz))
                {
                    this.isFragment = true;
                    this.elementType = this.clazz.asSubclass(DataFragment.class);
                }
                else if (UniqueEnumerationValue.class.isAssignableFrom(this.clazz))
                {
                    this.isUniqueEnum = true;
                    this.uniqueEnumType = this.clazz.asSubclass(UniqueEnumerationValue.class);
                }
                else if (EnumerationValue.class.isAssignableFrom(this.clazz))
                {
                    this.isFragment = true;
                    this.normalEnumType = this.clazz.asSubclass(EnumerationValue.class);
                }
                else if (Enum.class.isAssignableFrom(this.clazz))
                {
                    this.isJavaEnum = true;
                    this.javaEnumType = this.clazz.asSubclass(Enum.class);
                }
                else
                {
                    throw new IllegalStateException("Unsupported value type detected. " + this.clazz); //$NON-NLS-1$
                }
            }
        }
        
    }
    
    /**
     * the type binding.
     */
    private interface FieldTypeBinding
    {
        /**
         * Gets object
         * @param name
         * @param section
         * @return object value.
         */
        Object get(String name, DataSection section);
        
        /**
         * Sets object
         * @param name
         * @param section
         * @param value
         */
        void set(String name, DataSection section, Object value);
        
        /**
         * Tests pobject type
         * @param name
         * @param section
         * @return true if object is ok
         */
        boolean test(String name, DataSection section);
    }
    
    /**
     * Getter for receiving values.
     */
    @FunctionalInterface
    private interface Getter
    {
        /**
         * Getter.
         * @param name
         * @param section
         * @return Object value.
         */
        Object get(String name, DataSection section);
    }
    
    /**
     * Setter for storing values.
     */
    @FunctionalInterface
    private interface Setter
    {
        /**
         * Setter.
         * @param name
         * @param section
         * @param value
         */
        void set(String name, DataSection section, Object value);
    }
    
    /**
     * Tester for checking values.
     */
    @FunctionalInterface
    private interface Tester
    {
        /**
         * Tester.
         * @param name
         * @param section
         * @return true for valid values
         */
        boolean test(String name, DataSection section);
    }
    
    /**
     * The field types.
     */
    private enum PrimitiveFieldType implements FieldTypeBinding
    {
        
        /** string type. */
        Str(
                (name, section) -> section.getString(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isString(name)),
        
        /** int type. */
        Integer(
                (name, section) -> section.getInt(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isInt(name)),
        
        /** byte type. */
        Byte(
                (name, section) -> section.getByte(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isByte(name)),
        
        /** short type. */
        Short(
                (name, section) -> section.getShort(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isShort(name)),
        
        /** character type. */
        Character(
                (name, section) -> section.getCharacter(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isCharacter(name)),
        
        /** date/time type. */
        DateTime(
                (name, section) -> section.getDateTime(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isDateTime(name)),
        
        /** date type. */
        Date(
                (name, section) -> section.getDate(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isDate(name)),
        
        /** time type. */
        Time(
                (name, section) -> section.getTime(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isTime(name)),
        
        /** boolean type. */
        Boolean(
                (name, section) -> section.getBoolean(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isBoolean(name)),
        
        /** float type. */
        Float(
                (name, section) -> section.getFloat(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isFloat(name)),
        
        /** double type. */
        Double(
                (name, section) -> section.getDouble(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isDouble(name)),
        
        /** long type. */
        Long(
                (name, section) -> section.getLong(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isLong(name)),
        
        /** primitive list type. */
        PrimitiveList(
                (name, section) -> section.getPrimitiveList(name),
                (name, section, value) -> section.setPrimitiveList(name, (List<?>)value),
                (name, section) -> section.isList(name)),
        
        /** vector type. */
        Vector(
                (name, section) -> section.getVector(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isVector(name)),
        
        /** player type. */
        Player(
                (name, section) -> section.getPlayer(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isPlayer(name)),
        
        /** item stack type. */
        ItemStack(
                (name, section) -> section.getItemStack(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isItemStack(name)),
        
        /** color data type. */
        Color(
                (name, section) -> section.getColor(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isColor(name)),
        
        /** vector list type. */
        @SuppressWarnings("unchecked")
        VectorList(
                (name, section) -> section.getVectorList(name),
                (name, section, value) -> section.setFragmentList(name, (List<VectorDataFragment>)value),
                (name, section) -> section.isList(name)),
        
        /** color list type. */
        @SuppressWarnings("unchecked")
        ColorList(
                (name, section) -> section.getColorList(name),
                (name, section, value) -> section.setFragmentList(name, (List<ColorDataFragment>)value),
                (name, section) -> section.isList(name)),
        
        /** player list type. */
        @SuppressWarnings("unchecked")
        PlayerList(
                (name, section) -> section.getPlayerList(name),
                (name, section, value) -> section.setFragmentList(name, (List<PlayerDataFragment>)value),
                (name, section) -> section.isList(name)),
        
        /** item stack list type. */
        @SuppressWarnings("unchecked")
        ItemStackList(
                (name, section) -> section.getItemList(name),
                (name, section, value) -> section.setFragmentList(name, (List<ItemStackDataFragment>)value),
                (name, section) -> section.isList(name)),
        
        /** primitive map type. */
        Map(
                (name, section) -> section.getPrimitiveMap(name),
                (name, section, value) -> section.set(name, value),
                (name, section) -> section.isSection(name)),
        
        /** data section type. */
        DataSection(
                (name, section) -> section.getSection(name),
                (name, section, value) -> section.setSection(name, (DataSection) value),
                (name, section) -> section.isSection(name)),
        ;
        
        /** getter to receive values. */
        private final Getter getter;
        
        /** setter to store values. */
        private final Setter setter;
        
        /** tester to checking values. */
        private final Tester tester;

        /**
         * @param getter
         * @param setter
         * @param tester
         */
        private PrimitiveFieldType(Getter getter, Setter setter, Tester tester)
        {
            this.getter = getter;
            this.setter = setter;
            this.tester = tester;
        }

        @Override
        public Object get(String name, DataSection section)
        {
            return this.getter.get(name, section);
        }

        @Override
        public void set(String name, DataSection section, Object value)
        {
            this.setter.set(name, section, value);
        }

        @Override
        public boolean test(String name, DataSection section)
        {
            return this.tester.test(name, section);
        }
    }
    
}
