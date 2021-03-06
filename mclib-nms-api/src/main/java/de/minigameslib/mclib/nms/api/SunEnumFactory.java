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

package de.minigameslib.mclib.nms.api;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

/**
 * Taken from "https://www.niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically"
 * 
 * @author mepeisen
 */
@SuppressWarnings("restriction")
public class SunEnumFactory
{
    
    /** reflection factory. */
    private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
    
    /**
     * Sets field value.
     * 
     * @param field
     *            target field
     * @param target
     *            target object
     * @param value
     *            new value
     * @throws NoSuchFieldException
     *             thrown on internal problems changing the field modifiers (should never happen)
     * @throws IllegalAccessException
     *             thrown on internal problems changing the field modifiers (should never happen)
     */
    private static void setFailsafeFieldValue(Field field, Object target, Object value)
        throws NoSuchFieldException, IllegalAccessException
    {
        
        // let's make the field accessible
        field.setAccessible(true);
        
        // next we change the modifier in the Field instance to
        // not be final anymore, thus tricking reflection into
        // letting us modify the static final field
        Field modifiersField = Field.class.getDeclaredField("modifiers"); //$NON-NLS-1$
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);
        
        // blank out the final bit in the modifiers int
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);
        
        FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
        fa.set(target, value);
    }
    
    /**
     * Clears a field value.
     * 
     * @param enumClass
     *            target class
     * @param fieldName
     *            field name
     * @throws NoSuchFieldException
     *             thrown on internal problems changing the field modifiers (should never happen)
     * @throws IllegalAccessException
     *             thrown on internal problems changing the field modifiers (should never happen)
     */
    private static void blankField(Class<?> enumClass, String fieldName)
        throws NoSuchFieldException, IllegalAccessException
    {
        for (Field field : Class.class.getDeclaredFields())
        {
            if (field.getName().contains(fieldName))
            {
                AccessibleObject.setAccessible(new Field[] { field }, true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }
    
    /**
     * Clears internal enum cache.
     * 
     * @param enumClass
     *            target class
     * @throws NoSuchFieldException
     *             thrown on internal problems changing the field modifiers (should never happen)
     * @throws IllegalAccessException
     *             thrown on internal problems changing the field modifiers (should never happen)
     */
    private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException
    {
        blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6 //$NON-NLS-1$
        blankField(enumClass, "enumConstants"); // IBM JDK //$NON-NLS-1$
    }
    
    /**
     * Gets constructor.
     * 
     * @param enumClass
     *            target class
     * @param additionalParameterTypes
     *            additional parameters
     * @return constructor
     * @throws NoSuchMethodException
     *             thrown if constructor is not found
     */
    private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
        throws NoSuchMethodException
    {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }
    
    /**
     * Creates a new enum value.
     * 
     * @param enumClass
     *            target class
     * @param value
     *            name
     * @param ordinal
     *            ordinal value
     * @param additionalTypes
     *            additional constructor args types
     * @param additionalValues
     *            additional constructor args
     * @return enum value
     * @throws Exception
     *             thrown if there were problems creating the enum.
     */
    private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes,
        Object[] additionalValues) throws Exception
    {
        Object[] parms = new Object[additionalValues.length + 2];
        parms[0] = value;
        parms[1] = Integer.valueOf(ordinal);
        System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
        return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
    }
    
    /**
     * Add an enum instance to the enum class given as argument
     *
     * @param <T>
     *            the type of the enum (implicit)
     * @param enumType
     *            the class of the enum to be modified
     * @param enumName
     *            the name of the new enum instance to be added to the class.
     * @param argTypes
     *            additional constructor args types
     * @param args
     *            additional constructor args
     * @return enum value
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] argTypes, Object[] args)
    {
        
        // 0. Sanity checks
        if (!Enum.class.isAssignableFrom(enumType))
        {
            throw new RuntimeException("class " + enumType + " is not an instance of Enum"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        // 1. Lookup "$VALUES" holder in enum class and get previous enum
        // instances
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields)
        {
            if (field.getName().contains("$VALUES")) //$NON-NLS-1$
            {
                valuesField = field;
                break;
            }
        }
        if (valuesField == null)
        {
            throw new IllegalStateException("$VALUES not found"); //$NON-NLS-1$
        }
        AccessibleObject.setAccessible(new Field[] { valuesField }, true);
        
        try
        {
            
            // 2. Copy it
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<>(Arrays.asList(previousValues));
            
            // 3. build new enum
            T newValue = (T) makeEnum(enumType, // The target enum class
                enumName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
                values.size(), argTypes, // could be used to pass values to the enum constuctor if needed
                args); // could be used to pass values to the enum constuctor if needed
            
            // 4. add new value
            values.add(newValue);
            
            // 5. Set new values field
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));
            
            // 6. Clean enum cache
            cleanEnumCache(enumType);
            
            return newValue;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
