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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    static final Map<Class<?>, PrimitiveFieldType>     FIELD_TYPES = new HashMap<>();
    
    /** the primitive types directly supported by DataSection. */
    static final Set<Class<?>>                         PRIM_TYPES  = new HashSet<>();
    
    {
        FIELD_TYPES.put(UUID.class, PrimitiveFieldType.Uuid);
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
                    if (fdesc.isList)
                    {
                        value = section.getFragmentList(fdesc.elementType, name);
                    }
                    else if (fdesc.isMap)
                    {
                        value = section.getFragmentMap(fdesc.elementType, name);
                    }
                    else if (fdesc.isSet)
                    {
                        value = new HashSet<>(section.getFragmentList(fdesc.elementType, name));
                    }
                    else
                    {
                        value = section.getFragment(fdesc.elementType, name);
                    }
                }
                else if (fdesc.isJavaEnum)
                {
                    if (fdesc.isList)
                    {
                        value = section.getEnumList(fdesc.javaEnumType, name);
                    }
                    else if (fdesc.isMap)
                    {
                        value = section.getEnumMap(fdesc.javaEnumType, name);
                    }
                    else if (fdesc.isSet)
                    {
                        value = new HashSet<>(section.getEnumList(fdesc.javaEnumType, name));
                    }
                    else
                    {
                        value = section.getEnum(fdesc.javaEnumType, name);
                    }
                }
                else if (fdesc.isEnum)
                {
                    if (fdesc.isList)
                    {
                        value = section.getEnumValueList(fdesc.normalEnumType, name);
                    }
                    else if (fdesc.isMap)
                    {
                        value = section.getEnumValueMap(fdesc.normalEnumType, name);
                    }
                    else if (fdesc.isSet)
                    {
                        value = new HashSet<>(section.getEnumValueList(fdesc.normalEnumType, name));
                    }
                    else
                    {
                        value = section.getEnumValue(fdesc.normalEnumType, name);
                    }
                }
                else if (fdesc.isUniqueEnum)
                {
                    if (fdesc.isList)
                    {
                        value = section.getEnumValueList(fdesc.uniqueEnumType, name);
                    }
                    else if (fdesc.isMap)
                    {
                        value = section.getEnumValueMap(fdesc.uniqueEnumType, name);
                    }
                    else if (fdesc.isSet)
                    {
                        value = new HashSet<>(section.getEnumValueList(fdesc.uniqueEnumType, name));
                    }
                    else
                    {
                        value = section.getEnumValue(fdesc.uniqueEnumType, name);
                    }
                }
                else
                {
                    if (fdesc.isSet)
                    {
                        value = new HashSet<>((List<?>) fdesc.primitiveType.get(name, section));
                    }
                    else
                    {
                        value = fdesc.primitiveType.get(name, section);
                    }
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
                    if (fdesc.isList)
                    {
                        section.setFragmentList(name, (List<? extends DataFragment>) value);
                    }
                    else if (fdesc.isMap)
                    {
                        section.setFragmentMap(name, (Map<String, ? extends DataFragment>) value);
                    }
                    else if (fdesc.isSet)
                    {
                        section.setFragmentList(name, new ArrayList<>((Set<? extends DataFragment>) value));
                    }
                    else
                    {
                        section.set(name, value);
                    }
                }
                else if (fdesc.isJavaEnum || fdesc.isEnum || fdesc.isUniqueEnum)
                {
                    if (fdesc.isList)
                    {
                        section.setPrimitiveList(name, (List<?>) value);
                    }
                    else if (fdesc.isMap)
                    {
                        section.setPrimitiveMap(name, (Map<String, ?>) value);
                    }
                    else if (fdesc.isSet)
                    {
                        section.setPrimitiveList(name, new ArrayList<>((Set<?>) value));
                    }
                    else
                    {
                        section.set(name, value);
                    }
                }
                else
                {
                    if (fdesc.isMap)
                    {
                        fdesc.primitiveType.set(name, section, value);
                    }
                    else if (fdesc.isSet)
                    {
                        fdesc.primitiveType.set(name, section, new ArrayList<>((Set<?>) value));
                    }
                    else
                    {
                        fdesc.primitiveType.set(name, section, value);
                    }
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
                    if (fdesc.isList || fdesc.isSet || fdesc.isMap)
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
                    else if (!section.isFragment(fdesc.elementType, name))
                    {
                        return false;
                    }
                }
                else if (fdesc.isJavaEnum)
                {
                    if (fdesc.isList || fdesc.isSet || fdesc.isMap)
                    {
                        if (!PrimitiveFieldType.PrimitiveList.test(name, section))
                        {
                            return false;
                        }
                    }
                    else if (!PrimitiveFieldType.Str.test(name, section))
                    {
                        return false;
                    }
                }
                else if (fdesc.primitiveType != null)
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
         * 
         * @param clazz
         *            class to be represented
         */
        public DataDescriptor(Class<?> clazz)
        {
            addFieldDesc(clazz);
        }
        
        /**
         * Adds field descriptors for given classes.
         * 
         * @param clazz
         *            class to be analyzed
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
        final Field                             field;
        
        /** primitive type. */
        PrimitiveFieldType                      primitiveType = null;
        
        /** the target field type. */
        Class<?>                                clazz;
        
        /** true for list. */
        boolean                                 isList;
        
        /** true for map. */
        boolean                                 isMap;
        
        /** true for set. */
        boolean                                 isSet;
        
        /** the list or map element types. */
        Class<? extends DataFragment>           elementType;
        
        /** the list or map element types. */
        Class<? extends UniqueEnumerationValue> uniqueEnumType;
        
        /** the list or map element types. */
        @SuppressWarnings("rawtypes")
        Class<? extends Enum>                   javaEnumType;
        
        /** the list or map element types. */
        Class<? extends EnumerationValue>       normalEnumType;
        
        /** true if this is a data fragment. */
        boolean                                 isFragment;
        
        /** true for java enums. */
        boolean                                 isJavaEnum;
        
        /** true for unique enums. */
        boolean                                 isUniqueEnum;
        
        /** true for normal enums. */
        boolean                                 isEnum;
        
        /**
         * Constructor.
         * 
         * @param field
         *            field to be represented
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
                    final Class<?> listType = getClassFromTypes(ptype, 0);
                    this.isList = true;
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
                        this.isFragment = true;
                        this.elementType = listType.asSubclass(DataFragment.class);
                    }
                    else if (UniqueEnumerationValue.class.isAssignableFrom(listType))
                    {
                        this.isUniqueEnum = true;
                        this.uniqueEnumType = listType.asSubclass(UniqueEnumerationValue.class);
                    }
                    else if (EnumerationValue.class.isAssignableFrom(listType))
                    {
                        this.isEnum = true;
                        this.normalEnumType = listType.asSubclass(EnumerationValue.class);
                    }
                    else if (Enum.class.isAssignableFrom(listType))
                    {
                        this.isJavaEnum = true;
                        this.javaEnumType = listType.asSubclass(Enum.class);
                    }
                    else if (PRIM_TYPES.contains(listType))
                    {
                        if (listType == UUID.class)
                        {
                            this.primitiveType = PrimitiveFieldType.UuidList;
                        }
                        else
                        {
                            this.primitiveType = PrimitiveFieldType.PrimitiveList;
                        }
                    }
                    // TODO support map list etc.
                    // else if (Map.class.isAssignableFrom(elementType)) { }
                    else
                    {
                        throw new IllegalStateException("Unsupported list type detected. " + listType); //$NON-NLS-1$
                    }
                }
                else if (this.clazz.equals(Set.class))
                {
                    final ParameterizedType ptype = (ParameterizedType) field.getGenericType();
                    final Class<?> listType = getClassFromTypes(ptype, 0);
                    this.isSet = true;
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
                        this.isFragment = true;
                        this.elementType = listType.asSubclass(DataFragment.class);
                    }
                    else if (UniqueEnumerationValue.class.isAssignableFrom(listType))
                    {
                        this.isUniqueEnum = true;
                        this.uniqueEnumType = listType.asSubclass(UniqueEnumerationValue.class);
                    }
                    else if (EnumerationValue.class.isAssignableFrom(listType))
                    {
                        this.isEnum = true;
                        this.normalEnumType = listType.asSubclass(EnumerationValue.class);
                    }
                    else if (Enum.class.isAssignableFrom(listType))
                    {
                        this.isJavaEnum = true;
                        this.javaEnumType = listType.asSubclass(Enum.class);
                    }
                    else if (PRIM_TYPES.contains(listType))
                    {
                        if (listType == UUID.class)
                        {
                            this.primitiveType = PrimitiveFieldType.UuidList;
                        }
                        else
                        {
                            this.primitiveType = PrimitiveFieldType.PrimitiveList;
                        }
                    }
                    // TODO support map list etc.
                    // else if (Map.class.isAssignableFrom(elementType)) { }
                    else
                    {
                        throw new IllegalStateException("Unsupported list type detected. " + listType); //$NON-NLS-1$
                    }
                }
                else if (this.clazz.equals(Map.class))
                {
                    final ParameterizedType ptype = (ParameterizedType) field.getGenericType();
                    final Class<?> keyType = getClassFromTypes(ptype, 0);
                    final Class<?> valueType = getClassFromTypes(ptype, 1);
                    this.isMap = true;
                    if (!PRIM_TYPES.contains(keyType))
                    {
                        throw new IllegalStateException("Unsupported map key type detected. " + keyType); //$NON-NLS-1$
                    }
                    if (PRIM_TYPES.contains(valueType))
                    {
                        if (valueType == UUID.class)
                        {
                            this.primitiveType = PrimitiveFieldType.UuidMap;
                        }
                        else
                        {
                            this.primitiveType = PrimitiveFieldType.Map;
                        }
                    }
                    else if (DataFragment.class.isAssignableFrom(valueType))
                    {
                        this.isFragment = true;
                        this.elementType = valueType.asSubclass(DataFragment.class);
                    }
                    else if (UniqueEnumerationValue.class.isAssignableFrom(valueType))
                    {
                        this.isUniqueEnum = true;
                        this.uniqueEnumType = valueType.asSubclass(UniqueEnumerationValue.class);
                    }
                    else if (EnumerationValue.class.isAssignableFrom(valueType))
                    {
                        this.isEnum = true;
                        this.normalEnumType = valueType.asSubclass(EnumerationValue.class);
                    }
                    else if (Enum.class.isAssignableFrom(valueType))
                    {
                        this.isJavaEnum = true;
                        this.javaEnumType = valueType.asSubclass(Enum.class);
                    }
                    // TODO support list map etc.
                    // else if (List.class.isAssignableFrom(valueType)) { }
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
                    this.isEnum = true;
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
        
        /**
         * Returns class from java paremetrized types.
         * 
         * @param ptype
         *            parametrized type
         * @param index
         *            type index
         * @return clazz
         */
        private Class<?> getClassFromTypes(final ParameterizedType ptype, int index)
        {
            final Type type = ptype.getActualTypeArguments()[index];
            if (type instanceof Class<?>)
            {
                return (Class<?>) type;
            }
            if (type instanceof TypeVariable<?>)
            {
                return (Class<?>) ((TypeVariable<?>) type).getBounds()[0];
            }
            return null;
        }
        
    }
    
    /**
     * the type binding.
     */
    private interface FieldTypeBinding
    {
        /**
         * Gets object.
         * 
         * @param name
         *            field name
         * @param section
         *            data section
         * @return object value.
         */
        Object get(String name, DataSection section);
        
        /**
         * Sets object.
         * 
         * @param name
         *            field name
         * @param section
         *            data section
         * @param value
         *            new value
         */
        void set(String name, DataSection section, Object value);
        
        /**
         * Tests object type.
         * 
         * @param name
         *            field name
         * @param section
         *            data section
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
         * 
         * @param name
         *            field name
         * @param section
         *            data section
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
         * 
         * @param name
         *            field name
         * @param section
         *            data section
         * @param value
         *            new value
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
         * 
         * @param name
         *            field name
         * @param section
         *            data section
         * @return true for valid values
         */
        boolean test(String name, DataSection section);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getStr(String name, DataSection section)
    {
        return section.getString(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testStr(String name, DataSection section)
    {
        return section.isString(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getInt(String name, DataSection section)
    {
        return section.getInt(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testInt(String name, DataSection section)
    {
        return section.isInt(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getByte(String name, DataSection section)
    {
        return section.getByte(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testByte(String name, DataSection section)
    {
        return section.isByte(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getShort(String name, DataSection section)
    {
        return section.getShort(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testShort(String name, DataSection section)
    {
        return section.isShort(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getLong(String name, DataSection section)
    {
        return section.getLong(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testLong(String name, DataSection section)
    {
        return section.isLong(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getFloat(String name, DataSection section)
    {
        return section.getFloat(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testFloat(String name, DataSection section)
    {
        return section.isFloat(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getDouble(String name, DataSection section)
    {
        return section.getDouble(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testDouble(String name, DataSection section)
    {
        return section.isDouble(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getBool(String name, DataSection section)
    {
        return section.getBoolean(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testBool(String name, DataSection section)
    {
        return section.isBoolean(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getChar(String name, DataSection section)
    {
        return section.getCharacter(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testChar(String name, DataSection section)
    {
        return section.isCharacter(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getDateTime(String name, DataSection section)
    {
        return section.getDateTime(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testDateTime(String name, DataSection section)
    {
        return section.isDateTime(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getDate(String name, DataSection section)
    {
        return section.getDate(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testDate(String name, DataSection section)
    {
        return section.isDate(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getTime(String name, DataSection section)
    {
        return section.getTime(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testTime(String name, DataSection section)
    {
        return section.isTime(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getVector(String name, DataSection section)
    {
        return section.getVector(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testVector(String name, DataSection section)
    {
        return section.isVector(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getItemStack(String name, DataSection section)
    {
        return section.getItemStack(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testItemStack(String name, DataSection section)
    {
        return section.isItemStack(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getColor(String name, DataSection section)
    {
        return section.getColor(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testColor(String name, DataSection section)
    {
        return section.isColor(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getPlayer(String name, DataSection section)
    {
        return section.getPlayer(name);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testPlayer(String name, DataSection section)
    {
        return section.isPlayer(name);
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    static void setPrim(String name, DataSection section, Object value)
    {
        section.set(name, value);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getPrimList(String name, DataSection section)
    {
        return section.getPrimitiveList(name);
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    static void setPrimList(String name, DataSection section, Object value)
    {
        section.setPrimitiveList(name, (List<?>) value);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testPrimList(String name, DataSection section)
    {
        return section.isList(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getColorList(String name, DataSection section)
    {
        return section.getColorList(name);
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    @SuppressWarnings("unchecked")
    static void setColorList(String name, DataSection section, Object value)
    {
        section.setFragmentList(name, (List<ColorDataFragment>) value);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testColorList(String name, DataSection section)
    {
        // TODO check deep for color components
        return section.isList(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getPlayerList(String name, DataSection section)
    {
        return section.getPlayerList(name);
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    @SuppressWarnings("unchecked")
    static void setPlayerList(String name, DataSection section, Object value)
    {
        section.setFragmentList(name, (List<PlayerDataFragment>) value);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testPlayerList(String name, DataSection section)
    {
        // TODO check deep for player components
        return section.isList(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getItemStackList(String name, DataSection section)
    {
        return section.getItemList(name);
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    @SuppressWarnings("unchecked")
    static void setItemStackList(String name, DataSection section, Object value)
    {
        section.setFragmentList(name, (List<ItemStackDataFragment>) value);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testItemStackList(String name, DataSection section)
    {
        // TODO check deep for item stack components
        return section.isList(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getVectorList(String name, DataSection section)
    {
        return section.getVectorList(name);
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    @SuppressWarnings("unchecked")
    static void setVectorList(String name, DataSection section, Object value)
    {
        section.setFragmentList(name, (List<VectorDataFragment>) value);
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testVectorList(String name, DataSection section)
    {
        // TODO check deep for vector components
        return section.isList(name);
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getUuid(String name, DataSection section)
    {
        return UUID.fromString(section.getString(name));
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testUuid(String name, DataSection section)
    {
        if (!section.isString(name))
        {
            return false;
        }
        try
        {
            UUID.fromString(section.getString(name));
        }
        catch (@SuppressWarnings("unused") RuntimeException ex)
        {
            return false;
        }
        return true;
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    static void setUuid(String name, DataSection section, Object value)
    {
        section.set(name, value.toString());
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getUuidList(String name, DataSection section)
    {
        return section.getStringList(name).stream().map(UUID::fromString).collect(Collectors.toList());
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    @SuppressWarnings("unchecked")
    static void setUuidList(String name, DataSection section, Object value)
    {
        section.setPrimitiveList(name, ((List<UUID>) value).stream().map(UUID::toString).collect(Collectors.toList()));
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testUuidList(String name, DataSection section)
    {
        if (!section.isList(name))
        {
            return false;
        }
        for (final String str : section.getStringList(name))
        {
            try
            {
                UUID.fromString(str);
            }
            catch (@SuppressWarnings("unused") RuntimeException ex)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * get helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return resulting object
     */
    static Object getUuidMap(String name, DataSection section)
    {
        return section.getPrimitiveMap(name).entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> UUID.fromString(e.getValue().toString())));
    }
    
    /**
     * setter helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @param value
     *            new value
     */
    @SuppressWarnings("unchecked")
    static void setUuidMap(String name, DataSection section, Object value)
    {
        section.setPrimitiveMap(name, ((Map<String, UUID>) value).entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toString())));
    }
    
    /**
     * test helper.
     * 
     * @param name
     *            object key
     * @param section
     *            data section
     * @return test result
     */
    static boolean testUuidMap(String name, DataSection section)
    {
        if (!section.isList(name))
        {
            return false;
        }
        for (final Object str : section.getPrimitiveMap(name).values())
        {
            try
            {
                UUID.fromString(str.toString());
            }
            catch (@SuppressWarnings("unused") RuntimeException ex)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * The field types.
     */
    private enum PrimitiveFieldType implements FieldTypeBinding
    {
        
        /** string type. */
        Str(AnnotatedDataFragment::getStr, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testStr),
        
        /** uuid type. */
        Uuid(AnnotatedDataFragment::getUuid, AnnotatedDataFragment::setUuid, AnnotatedDataFragment::testUuid),
        
        /** int type. */
        Integer(AnnotatedDataFragment::getInt, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testInt),
        
        /** byte type. */
        Byte(AnnotatedDataFragment::getByte, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testByte),
        
        /** short type. */
        Short(AnnotatedDataFragment::getShort, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testShort),
        
        /** character type. */
        Character(AnnotatedDataFragment::getChar, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testChar),
        
        /** date/time type. */
        DateTime(AnnotatedDataFragment::getDateTime, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testDateTime),
        
        /** date type. */
        Date(AnnotatedDataFragment::getDate, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testDate),
        
        /** time type. */
        Time(AnnotatedDataFragment::getTime, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testTime),
        
        /** boolean type. */
        Boolean(AnnotatedDataFragment::getBool, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testBool),
        
        /** float type. */
        Float(AnnotatedDataFragment::getFloat, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testFloat),
        
        /** double type. */
        Double(AnnotatedDataFragment::getDouble, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testDouble),
        
        /** long type. */
        Long(AnnotatedDataFragment::getLong, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testLong),
        
        /** primitive list type. */
        PrimitiveList(AnnotatedDataFragment::getPrimList, AnnotatedDataFragment::setPrimList, AnnotatedDataFragment::testPrimList),
        
        /** uuid list type. */
        UuidList(AnnotatedDataFragment::getUuidList, AnnotatedDataFragment::setUuidList, AnnotatedDataFragment::testUuidList),
        
        /** uuid map type. */
        UuidMap(AnnotatedDataFragment::getUuidMap, AnnotatedDataFragment::setUuidMap, AnnotatedDataFragment::testUuidMap),
        
        /** vector type. */
        Vector(AnnotatedDataFragment::getVector, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testVector),
        
        /** player type. */
        Player(AnnotatedDataFragment::getPlayer, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testPlayer),
        
        /** item stack type. */
        ItemStack(AnnotatedDataFragment::getItemStack, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testItemStack),
        
        /** color data type. */
        Color(AnnotatedDataFragment::getColor, AnnotatedDataFragment::setPrim, AnnotatedDataFragment::testColor),
        
        /** vector list type. */
        VectorList(AnnotatedDataFragment::getVectorList, AnnotatedDataFragment::setVectorList, AnnotatedDataFragment::testVectorList),
        
        /** color list type. */
        ColorList(AnnotatedDataFragment::getColorList, AnnotatedDataFragment::setColorList, AnnotatedDataFragment::testColorList),
        
        /** player list type. */
        PlayerList(AnnotatedDataFragment::getPlayerList, AnnotatedDataFragment::setPlayerList, AnnotatedDataFragment::testPlayerList),
        
        /** item stack list type. */
        ItemStackList(AnnotatedDataFragment::getItemStackList, AnnotatedDataFragment::setItemStackList, AnnotatedDataFragment::testItemStackList),
        
        /** primitive map type. */
        @SuppressWarnings("unchecked")
        Map((name, section) -> section.getPrimitiveMap(name), (name, section, value) -> section.setPrimitiveMap(name, (Map<String, ?>) value), (name, section) -> section.isSection(name)),
        
        /** data section type. */
        DataSection((name, section) -> section.getSection(name), (name, section, value) -> section.setSection(name, (DataSection) value), (name, section) -> section.isSection(name)),;
        
        /** getter to receive values. */
        private final Getter getter;
        
        /** setter to store values. */
        private final Setter setter;
        
        /** tester to checking values. */
        private final Tester tester;
        
        /**
         * Constructor.
         * 
         * @param getter
         *            the getter to be used
         * @param setter
         *            the setter to be used
         * @param tester
         *            the tester to be used
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
