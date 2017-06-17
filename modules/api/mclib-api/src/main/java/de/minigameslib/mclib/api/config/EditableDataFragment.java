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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * Data fragment that can be edited in gui.
 * 
 * @author mepeisen
 */
public class EditableDataFragment extends AnnotatedDataFragment
{
    
    /** logging. */
    protected static final Logger LOGGER = Logger.getLogger(EditableDataFragment.class.getName());
    
    /**
     * Wrapper for editable values from java fields.
     * 
     * @author mepeisen
     */
    private class EditableValueImpl implements EditableValue
    {
        
        /** the field. */
        final FieldDescriptor field;
        
        /** save function. */
        final Runnable        save;
        
        /** rollback function. */
        final Runnable        rollback;
        
        /**
         * Constructor.
         * 
         * @param field
         *            the java field to wrap.
         * @param save
         *            save function
         * @param rollback
         *            rollback function
         */
        public EditableValueImpl(FieldDescriptor field, Runnable save, Runnable rollback)
        {
            this.field = field;
            this.save = save;
            this.rollback = rollback;
        }
        
        @Override
        public String name()
        {
            return this.field.field.getName();
        }
        
        @Override
        public boolean isEnum()
        {
            return (this.field.isEnum || this.field.isUniqueEnum) && !this.field.isList && !this.field.isMap;
        }
        
        @Override
        public boolean isJavaEnum()
        {
            return this.field.isJavaEnum && !this.field.isList && !this.field.isMap;
        }
        
        @Override
        public boolean isEnumList()
        {
            return (this.field.isEnum || this.field.isUniqueEnum) && this.field.isList && !this.field.isMap;
        }
        
        @Override
        public boolean isJavaEnumList()
        {
            return this.field.isJavaEnum && this.field.isList && !this.field.isMap;
        }
        
        @Override
        public boolean isBoolean()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Boolean;
        }
        
        @Override
        public boolean isBooleanList()
        {
            return this.field.isList && this.field.primitiveListMapType == Boolean.class;
        }
        
        @Override
        public boolean isByte()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Byte;
        }
        
        @Override
        public boolean isByteList()
        {
            return this.field.isList && this.field.primitiveListMapType == Byte.class;
        }
        
        @Override
        public boolean isCharacter()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Character;
        }
        
        @Override
        public boolean isCharacterList()
        {
            return this.field.isList && this.field.primitiveListMapType == Character.class;
        }
        
        @Override
        public boolean isColorList()
        {
            return this.field.isList && this.field.primitiveType == PrimitiveFieldType.ColorList;
        }
        
        @Override
        public boolean isColor()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Color;
        }
        
        @Override
        public boolean isDoubleList()
        {
            return this.field.isList && this.field.primitiveListMapType == Double.class;
        }
        
        @Override
        public boolean isDouble()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Double;
        }
        
        @Override
        public boolean isFloatList()
        {
            return this.field.isList && this.field.primitiveListMapType == Float.class;
        }
        
        @Override
        public boolean isFloat()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Float;
        }
        
        @Override
        public boolean isIntList()
        {
            return this.field.isList && this.field.primitiveListMapType == Integer.class;
        }
        
        @Override
        public boolean isInt()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Integer;
        }
        
        @Override
        public boolean isItemStackList()
        {
            return this.field.isList && this.field.primitiveType == PrimitiveFieldType.ItemStackList;
        }
        
        @Override
        public boolean isItemStack()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.ItemStack;
        }
        
        @Override
        public boolean isSection()
        {
            return this.field.primitiveType == PrimitiveFieldType.DataSection;
        }
        
        @Override
        public boolean isLongList()
        {
            return this.field.isList && this.field.primitiveListMapType == Long.class;
        }
        
        @Override
        public boolean isLong()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Long;
        }
        
        @Override
        public boolean isObjectList()
        {
            return this.field.isList && this.field.isFragment;
        }
        
        @Override
        public boolean isObject()
        {
            return !this.field.isList && this.field.isFragment;
        }
        
        @Override
        public boolean isPlayerList()
        {
            return this.field.isList && this.field.primitiveType == PrimitiveFieldType.PlayerList;
        }
        
        @Override
        public boolean isPlayer()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Player;
        }
        
        @Override
        public boolean isShortList()
        {
            return this.field.isList && this.field.primitiveListMapType == Short.class;
        }
        
        @Override
        public boolean isShort()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Short;
        }
        
        @Override
        public boolean isStringList()
        {
            return this.field.isList && this.field.primitiveListMapType == String.class;
        }
        
        @Override
        public boolean isString()
        {
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Str;
        }
        
        @Override
        public boolean isVectorList()
        {
            return this.field.isList && this.field.primitiveType == PrimitiveFieldType.VectorList;
        }
        
        @Override
        public boolean isVector()
        {
            
            return !this.field.isList && this.field.primitiveType == PrimitiveFieldType.Vector;
        }
        
        @Override
        public String path()
        {
            return this.name();
        }
        
        @Override
        public boolean isset()
        {
            try
            {
                return this.field.field.get(EditableDataFragment.this) != null;
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return false;
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <T extends DataFragment> T getObject()
        {
            try
            {
                return (T) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public byte getByte()
        {
            try
            {
                return (Byte) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return 0;
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <T extends EnumerationValue> T getEnum(Class<T> clazz)
        {
            try
            {
                return (T) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <T extends Enum<?>> T getJavaEnum(Class<T> clazz)
        {
            try
            {
                return (T) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public ConfigColorData getColor()
        {
            try
            {
                return (ConfigColorData) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public ConfigItemStackData getItemStack()
        {
            try
            {
                return (ConfigItemStackData) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public ConfigVectorData getVector()
        {
            try
            {
                return (ConfigVectorData) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public McPlayerInterface getPlayer()
        {
            try
            {
                return (McPlayerInterface) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public char getCharacter()
        {
            try
            {
                return (Character) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return 0;
            }
        }
        
        @Override
        public boolean getBoolean()
        {
            try
            {
                return (Boolean) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return false;
            }
        }
        
        @Override
        public boolean[] getBooleanList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final boolean[] result = new boolean[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Boolean) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <T extends EnumerationValue> T[] getEnumList(Class<T> clazz)
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray((T[])Array.newInstance(clazz, 0));
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <T extends Enum<?>> T[] getJavaEnumList(Class<T> clazz)
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray((T[])Array.newInstance(clazz, 0));
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public byte[] getByteList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final byte[] result = new byte[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Byte) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public char[] getCharacterList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final char[] result = new char[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Character) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public double getDouble()
        {
            try
            {
                return (Double) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return 0;
            }
        }
        
        @Override
        public float getFloat()
        {
            try
            {
                return (Float) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return 0;
            }
        }
        
        @Override
        public double[] getDoubleList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final double[] result = new double[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Double) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public float[] getFloatList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final float[] result = new float[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Float) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public int getInt()
        {
            try
            {
                return (Integer) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return 0;
            }
        }
        
        @Override
        public short getShort()
        {
            try
            {
                return (Short) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return 0;
            }
        }
        
        @Override
        public int[] getIntList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final int[] result = new int[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Integer) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public long getLong()
        {
            try
            {
                return (Long) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return 0;
            }
        }
        
        @Override
        public long[] getLongList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final long[] result = new long[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Long) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public short[] getShortList()
        {
            try
            {
                final List<?> list = (List<?>) this.field.field.get(EditableDataFragment.this);
                final short[] result = new short[list.size()];
                int i = 0;
                for (Object obj : list)
                {
                    result[i] = (Short) obj;
                    i++;
                }
                return result;
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public String getString()
        {
            try
            {
                return (String) this.field.field.get(EditableDataFragment.this);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public String[] getStringList()
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray(new String[0]);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public ConfigVectorData[] getVectorList()
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray(new ConfigVectorData[0]);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public ConfigItemStackData[] getItemStackList()
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray(new ConfigItemStackData[0]);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <T extends DataFragment> T[] getObjectList(Class<T> clazz)
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray((T[])Array.newInstance(clazz, 0));
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public ConfigColorData[] getColorList()
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray(new ConfigColorData[0]);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public McPlayerInterface[] getPlayerList()
        {
            try
            {
                return ((List<?>)this.field.field.get(EditableDataFragment.this)).toArray(new McPlayerInterface[0]);
            }
            catch (ClassCastException | IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems fetching data fragment field " + this.name(), e); //$NON-NLS-1$
                return null;
            }
        }
        
        @Override
        public String[] getComment()
        {
            return new String[0];
        }
        
        @Override
        public Class<? extends EnumerationValue> getEnumClass()
        {
            return this.field.uniqueEnumType == null ? this.field.normalEnumType : this.field.uniqueEnumType;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public Class<? extends Enum<?>> getJavaEnumClass()
        {
            return (Class<? extends Enum<?>>) this.field.javaEnumType;
        }
        
        @Override
        public void setObject(DataFragment value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setObjectList(DataFragment[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setEnum(EnumerationValue value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setJavaEnum(Enum<?> value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setEnumList(EnumerationValue[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setJavaEnumList(Enum<?>[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setBoolean(boolean value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setBooleanList(boolean[] value)
        {
            try
            {
                final List<Boolean> val = new ArrayList<>();
                for (final boolean b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setByte(byte value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setByteList(byte[] value)
        {
            try
            {
                final List<Byte> val = new ArrayList<>();
                for (final byte b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setCharacter(char value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setCharacterList(char[] value)
        {
            try
            {
                final List<Character> val = new ArrayList<>();
                for (final char b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setColor(ConfigColorData value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setColorList(ConfigColorData[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setDouble(double value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setDoubleList(double[] value)
        {
            try
            {
                final List<Double> val = new ArrayList<>();
                for (final double b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setFloat(float value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setFloatList(float[] value)
        {
            try
            {
                final List<Float> val = new ArrayList<>();
                for (final float b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setInt(int value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setIntList(int[] value)
        {
            try
            {
                final List<Integer> val = new ArrayList<>();
                for (final int b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setItemStack(ConfigItemStackData value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setItemStackList(ConfigItemStackData[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setLong(long value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setLongList(long[] value)
        {
            try
            {
                final List<Long> val = new ArrayList<>();
                for (final long b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setPlayer(McPlayerInterface value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setPlayerList(McPlayerInterface[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setShort(short value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setShortList(short[] value)
        {
            try
            {
                final List<Short> val = new ArrayList<>();
                for (final short b : value)
                {
                    val.add(b);
                }
                this.field.field.set(EditableDataFragment.this, val);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setString(String value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setStringList(String[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setVector(ConfigVectorData value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void setVectorList(ConfigVectorData[] value)
        {
            try
            {
                this.field.field.set(EditableDataFragment.this, Arrays.asList(value));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                LOGGER.log(Level.WARNING, "problems setting data fragment field " + this.name(), e); //$NON-NLS-1$
            }
        }
        
        @Override
        public void saveConfig()
        {
            this.save.run();
        }
        
        @Override
        public void verifyConfig() throws McException
        {
            EditableDataFragment.this.verifyConfig(this.save, this.rollback);
        }
        
        @Override
        public void rollbackConfig()
        {
            this.rollback.run();
        }
        
        @Override
        public void validate() throws McException
        {
            try
            {
                final ValidateFMax fmax = this.field.field.getAnnotation(ValidateFMax.class);
                if (fmax != null)
                {
                    ValidateFMax.ValidatorInstance.validate(fmax, this);
                }
                final ValidateFMin fmin = this.field.field.getAnnotation(ValidateFMin.class);
                if (fmin != null)
                {
                    ValidateFMin.ValidatorInstance.validate(fmin, this);
                }
                final ValidateIsset isset = this.field.field.getAnnotation(ValidateIsset.class);
                if (isset != null)
                {
                    ValidateIsset.ValidatorInstance.validate(isset, this);
                }
                final ValidateListMax listmax = this.field.field.getAnnotation(ValidateListMax.class);
                if (listmax != null)
                {
                    ValidateListMax.ValidatorInstance.validate(listmax, this);
                }
                final ValidateListMin listmin = this.field.field.getAnnotation(ValidateListMin.class);
                if (listmin != null)
                {
                    ValidateListMin.ValidatorInstance.validate(listmin, this);
                }
                final ValidateLMax lmax = this.field.field.getAnnotation(ValidateLMax.class);
                if (lmax != null)
                {
                    ValidateLMax.ValidatorInstance.validate(lmax, this);
                }
                final ValidateLMin lmin = this.field.field.getAnnotation(ValidateLMin.class);
                if (lmin != null)
                {
                    ValidateLMin.ValidatorInstance.validate(lmin, this);
                }
                final ValidateStrMax strmax = this.field.field.getAnnotation(ValidateStrMax.class);
                if (strmax != null)
                {
                    ValidateStrMax.ValidatorInstance.validate(strmax, this);
                }
                final ValidateStrMin strmin = this.field.field.getAnnotation(ValidateStrMin.class);
                if (strmin != null)
                {
                    ValidateStrMin.ValidatorInstance.validate(strmin, this);
                }
                final Validator validator = this.field.field.getAnnotation(Validator.class);
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
    
    /**
     * Returns the editable values for this data fragment.
     * 
     * @param save
     *            save function
     * @param rollback
     *            rollback function
     * @return editable values.
     */
    public Collection<EditableValueImpl> getEditableValues(Runnable save, Runnable rollback)
    {
        return this.getFieldDescriptors().stream().map(f -> new EditableValueImpl(f, save, rollback)).collect(Collectors.toList());
    }
    
    /**
     * verify data fragment.
     * 
     * @param save
     *            save function
     * @param rollback
     *            rollback function
     * 
     * @throws McException
     *             thrown if verification failed.
     */
    public void verifyConfig(Runnable save, Runnable rollback) throws McException
    {
        for (final FieldDescriptor field : this.getFieldDescriptors())
        {
            final EditableValueImpl editable = new EditableValueImpl(field, save, rollback);
            editable.validate();
        }
    }
    
}
