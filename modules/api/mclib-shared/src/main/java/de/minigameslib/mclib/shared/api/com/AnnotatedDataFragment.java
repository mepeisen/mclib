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
        FIELD_TYPES.put(ColorData.class, PrimitiveFieldType.Color);
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
                    throw new IllegalStateException("error reading field " + name + " of class " + this.getClass(), e); //$NON-NLS-1$ //$NON-NLS-2$
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
        // TODO Auto-generated method stub
        return false;
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
        
        /** true if this is a list f data fragments. */
        boolean isFragmentList;
        
        /** true if this is a map to data fragments. */
        boolean isFragmentMap;

        /** the list or map element types. */
        Class<? extends DataFragment> elementType;

        /** true if this is a data fragment. */
        boolean isFragment;
        
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
                    if (PRIM_TYPES.contains(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.PrimitiveList;
                    }
                    else if (VectorData.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.VectorList;
                    }
                    else if (ItemStackDataFragment.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.ItemStackList;
                    }
                    else if (ColorData.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.ColorList;
                    }
                    else if (PlayerData.class.equals(listType))
                    {
                        this.primitiveType = PrimitiveFieldType.PlayerList;
                    }
                    else if (DataFragment.class.isAssignableFrom(listType))
                    {
                        this.isFragmentList = true;
                        this.elementType = listType.asSubclass(DataFragment.class);
                    }
                    // TODO support map list
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
                    // TODO support list map
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
     * The field types.
     */
    private enum PrimitiveFieldType implements FieldTypeBinding
    {
        
        /** string type. */
        Str((name, section) -> section.getString(name), (name, section, value) -> section.set(name, value)),
        
        /** int type. */
        Integer((name, section) -> section.getInt(name), (name, section, value) -> section.set(name, value)),
        
        /** byte type. */
        Byte((name, section) -> section.getByte(name), (name, section, value) -> section.set(name, value)),
        
        /** short type. */
        Short((name, section) -> section.getShort(name), (name, section, value) -> section.set(name, value)),
        
        /** character type. */
        Character((name, section) -> section.getCharacter(name), (name, section, value) -> section.set(name, value)),
        
        /** date/time type. */
        DateTime((name, section) -> section.getDateTime(name), (name, section, value) -> section.set(name, value)),
        
        /** date type. */
        Date((name, section) -> section.getDate(name), (name, section, value) -> section.set(name, value)),
        
        /** time type. */
        Time((name, section) -> section.getTime(name), (name, section, value) -> section.set(name, value)),
        
        /** boolean type. */
        Boolean((name, section) -> section.getBoolean(name), (name, section, value) -> section.set(name, value)),
        
        /** float type. */
        Float((name, section) -> section.getFloat(name), (name, section, value) -> section.set(name, value)),
        
        /** double type. */
        Double((name, section) -> section.getDouble(name), (name, section, value) -> section.set(name, value)),
        
        /** long type. */
        Long((name, section) -> section.getLong(name), (name, section, value) -> section.set(name, value)),
        
        /** primitive list type. */
        PrimitiveList((name, section) -> section.getList(name), (name, section, value) -> section.setPrimitiveList(name, (List<?>)value)),
        
        /** vector type. */
        Vector((name, section) -> section.getVector(name), (name, section, value) -> section.set(name, value)),
        
        /** player type. */
        Player((name, section) -> section.getPlayer(name), (name, section, value) -> section.set(name, value)),
        
        /** item stack type. */
        ItemStack((name, section) -> section.getItemStack(name), (name, section, value) -> section.set(name, value)),
        
        /** color data type. */
        Color((name, section) -> section.getColor(name), (name, section, value) -> section.set(name, value)),
        
        /** vector list type. */
        @SuppressWarnings("unchecked")
        VectorList((name, section) -> section.getVectorList(name), (name, section, value) -> section.setFragmentList(name, (List<VectorData>)value)),
        
        /** color list type. */
        @SuppressWarnings("unchecked")
        ColorList((name, section) -> section.getColorList(name), (name, section, value) -> section.setFragmentList(name, (List<ColorData>)value)),
        
        /** player list type. */
        @SuppressWarnings("unchecked")
        PlayerList((name, section) -> section.getPlayerList(name), (name, section, value) -> section.setFragmentList(name, (List<PlayerData>)value)),
        
        /** item stack list type. */
        @SuppressWarnings("unchecked")
        ItemStackList((name, section) -> section.getItemList(name), (name, section, value) -> section.setFragmentList(name, (List<ItemStackDataFragment>)value)),
        
        /** primitive map type. */
        Map((name, section) -> section.getMap(name), (name, section, value) -> section.set(name, value)),
        
        /** data section type. */
        DataSection((name, section) -> section.getSection(name), (name, section, value) -> section.setSection(name, (DataSection) value)),
        ;
        
        /** getter to receive values. */
        private final Getter getter;
        
        /** setter to store values. */
        private final Setter setter;

        /**
         * @param getter
         * @param setter
         */
        private PrimitiveFieldType(Getter getter, Setter setter)
        {
            this.getter = getter;
            this.setter = setter;
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
    }
    
}
