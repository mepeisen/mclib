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

package de.minigameslib.mclib.test.config;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.EditableValue;
import de.minigameslib.mclib.api.config.ConfigColorData;
import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.config.ConfigVectorData;
import de.minigameslib.mclib.api.config.EditableDataFragment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.shared.api.com.ColorDataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.ItemStackDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mclib.shared.api.com.PlayerDataFragment;
import de.minigameslib.mclib.shared.api.com.VectorDataFragment;

/**
 * Test for {@link EditableDataFragment}.
 * 
 * @author mepeisen
 *
 */
public class EditableDataFragmentTest
{
    
    /**
     * setup class.
     */
    @BeforeClass
    public static void setup()
    {
        Logger.getLogger(EditableDataFragment.class.getName()).setLevel(Level.OFF);
    }
    
    /**
     * test me.
     */
    @Test
    public void testSave()
    {
        final Editable obj = new Editable();
        final Runnable save = mock(Runnable.class);
        final EditableValue value = value(obj, "someByte", save, null); //$NON-NLS-1$
        value.setByte((byte) 12);
        value.saveConfig();
        
        verify(save, times(1)).run();
    }
    
    /**
     * test me.
     */
    @Test
    public void testRollback()
    {
        final Editable obj = new Editable();
        final Runnable rollback = mock(Runnable.class);
        final EditableValue value = value(obj, "someByte", null, rollback); //$NON-NLS-1$
        value.setByte((byte) 12);
        value.rollbackConfig();
        
        verify(rollback, times(1)).run();
    }
    
    /**
     * test me.
     */
    @Test
    public void testByte()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        assertEquals(0, value.getByte());
        value.setByte((byte) 12);
        assertEquals(12, value.getByte());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testByteInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someBoolean"); //$NON-NLS-1$
        value.setByte((byte) 0);
        assertEquals(0, value.getByte());
        
        final EditableValue v2 = invalidValue();
        assertEquals(0, v2.getByte());
    }
    
    /**
     * test me.
     */
    @Test
    public void testByteList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByteList"); //$NON-NLS-1$
        assertArrayEquals(new byte[0], value.getByteList());
        value.setByteList(new byte[] { (byte) 12, (byte) 13 });
        assertArrayEquals(new byte[] { (byte) 12, (byte) 13 }, value.getByteList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testByteListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someBoolean"); //$NON-NLS-1$
        value.setByteList(new byte[] { (byte) 1 });
        assertNull(value.getByteList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getByteList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testBoolean()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someBoolean"); //$NON-NLS-1$
        assertFalse(value.getBoolean());
        value.setBoolean(true);
        assertTrue(value.getBoolean());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testBooleanInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setBoolean(false);
        assertFalse(value.getBoolean());
        
        final EditableValue v2 = invalidValue();
        assertFalse(v2.getBoolean());
    }
    
    /**
     * test me.
     */
    @Test
    public void testBooleanList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someBooleanList"); //$NON-NLS-1$
        assertArrayEquals(new boolean[0], value.getBooleanList());
        value.setBooleanList(new boolean[] { true, false });
        assertArrayEquals(new boolean[] { true, false }, value.getBooleanList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testBooleanListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setBooleanList(new boolean[] { true });
        assertNull(value.getBooleanList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getBooleanList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testCharacter()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someCharacter"); //$NON-NLS-1$
        assertEquals('\0', value.getCharacter());
        value.setCharacter('A');
        assertEquals('A', value.getCharacter());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testCharacterInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setCharacter('A');
        assertEquals('\0', value.getCharacter());
        
        final EditableValue v2 = invalidValue();
        assertEquals('\0', v2.getCharacter());
    }
    
    /**
     * test me.
     */
    @Test
    public void testCharacterList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someCharacterList"); //$NON-NLS-1$
        assertArrayEquals(new char[0], value.getCharacterList());
        value.setCharacterList(new char[] { 'a', 'b' });
        assertArrayEquals(new char[] { 'a', 'b' }, value.getCharacterList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testCharacterListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setCharacterList(new char[] { 'A' });
        assertNull(value.getCharacterList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getCharacterList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testColor()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someColor"); //$NON-NLS-1$
        assertNull(value.getColor());
        final ConfigColorData data = new ConfigColorData(1, 2, 3);
        value.setColor(data);
        assertSame(data, value.getColor());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testColorInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setColor(new ConfigColorData(1, 2, 3));
        assertNull(value.getColor());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getColor());
    }
    
    /**
     * test me.
     */
    @Test
    public void testColorList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someColorList"); //$NON-NLS-1$
        final ConfigColorData data1 = new ConfigColorData(1, 2, 3);
        final ConfigColorData data2 = new ConfigColorData(2, 3, 1);
        assertArrayEquals(new ConfigColorData[0], value.getColorList());
        value.setColorList(new ConfigColorData[] { data1, data2 });
        assertArrayEquals(new ConfigColorData[] { data1, data2 }, value.getColorList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testColorListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3) });
        assertNull(value.getColorList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getColorList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testDouble()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someDouble"); //$NON-NLS-1$
        assertEquals(0d, value.getDouble(), 0);
        value.setDouble(123d);
        assertEquals(123d, value.getDouble(), 0);
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testDoubleInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setDouble(123d);
        assertEquals(0d, value.getDouble(), 0);
        
        final EditableValue v2 = invalidValue();
        assertEquals(0d, v2.getDouble(), 0);
    }
    
    /**
     * test me.
     */
    @Test
    public void testDoubleList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someDoubleList"); //$NON-NLS-1$
        assertArrayEquals(new double[0], value.getDoubleList(), 0);
        value.setDoubleList(new double[] { 12d, 23d });
        assertArrayEquals(new double[] { 12d, 23d }, value.getDoubleList(), 0);
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testDoubleListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setDoubleList(new double[] { 12d, 23d });
        assertNull(value.getDoubleList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getDoubleList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testEnum()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someEnum"); //$NON-NLS-1$
        assertNull(value.getEnum(CommonMessages.class));
        value.setEnum(CommonMessages.AlreadyDeletedError);
        assertEquals(CommonMessages.AlreadyDeletedError, value.getEnum(CommonMessages.class));
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testEnumInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setEnum(CommonMessages.AlreadyDeletedError);
        assertNull(value.getEnum(CommonMessages.class));
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getEnum(CommonMessages.class));
    }
    
    /**
     * test me.
     */
    @Test
    public void testEnumList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someEnumList"); //$NON-NLS-1$
        final CommonMessages data1 = CommonMessages.AlreadyDeletedError;
        final CommonMessages data2 = CommonMessages.BrokenObjectType;
        assertArrayEquals(new CommonMessages[0], value.getEnumList(CommonMessages.class));
        value.setEnumList(new CommonMessages[] { data1, data2 });
        assertArrayEquals(new CommonMessages[] { data1, data2 }, value.getEnumList(CommonMessages.class));
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testEnumListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final CommonMessages data1 = CommonMessages.AlreadyDeletedError;
        final CommonMessages data2 = CommonMessages.BrokenObjectType;
        value.setEnumList(new CommonMessages[] { data1, data2 });
        assertNull(value.getEnumList(CommonMessages.class));
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getEnumList(CommonMessages.class));
    }
    
    /**
     * test me.
     */
    @Test
    public void testFloat()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someFloat"); //$NON-NLS-1$
        assertEquals(0f, value.getFloat(), 0);
        value.setFloat(123f);
        assertEquals(123f, value.getFloat(), 0);
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testFloatInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setFloat(123f);
        assertEquals(0f, value.getFloat(), 0);
        
        final EditableValue v2 = invalidValue();
        assertEquals(0f, v2.getFloat(), 0);
    }
    
    /**
     * test me.
     */
    @Test
    public void testFloatList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someFloatList"); //$NON-NLS-1$
        assertArrayEquals(new float[0], value.getFloatList(), 0);
        value.setFloatList(new float[] { 12f, 23f });
        assertArrayEquals(new float[] { 12f, 23f }, value.getFloatList(), 0);
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testFloatListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setFloatList(new float[] { 12f, 23f });
        assertNull(value.getFloatList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getFloatList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testInt()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someInt"); //$NON-NLS-1$
        assertEquals(0, value.getInt());
        value.setInt(123);
        assertEquals(123, value.getInt());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testIntInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setInt(123);
        assertEquals(0, value.getInt());
        
        final EditableValue v2 = invalidValue();
        assertEquals(0, v2.getInt());
    }
    
    /**
     * test me.
     */
    @Test
    public void testIntList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someIntList"); //$NON-NLS-1$
        assertArrayEquals(new int[0], value.getIntList());
        value.setIntList(new int[] { 12, 23 });
        assertArrayEquals(new int[] { 12, 23 }, value.getIntList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testIntListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setIntList(new int[] { 12, 23 });
        assertNull(value.getIntList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getIntList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testItemStack()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someItemStack"); //$NON-NLS-1$
        assertNull(value.getItemStack());
        final ConfigItemStackData data = mock(ConfigItemStackData.class);
        value.setItemStack(data);
        assertSame(data, value.getItemStack());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testItemStackInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final ConfigItemStackData data = mock(ConfigItemStackData.class);
        value.setItemStack(data);
        assertNull(value.getItemStack());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getItemStack());
    }
    
    /**
     * test me.
     */
    @Test
    public void testItemStackList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someItemStackList"); //$NON-NLS-1$
        final ConfigItemStackData data1 = mock(ConfigItemStackData.class);
        final ConfigItemStackData data2 = mock(ConfigItemStackData.class);
        assertArrayEquals(new ConfigItemStackData[0], value.getItemStackList());
        value.setItemStackList(new ConfigItemStackData[] { data1, data2 });
        assertArrayEquals(new ConfigItemStackData[] { data1, data2 }, value.getItemStackList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testItemStackListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final ConfigItemStackData data1 = mock(ConfigItemStackData.class);
        final ConfigItemStackData data2 = mock(ConfigItemStackData.class);
        value.setItemStackList(new ConfigItemStackData[] { data1, data2 });
        assertNull(value.getItemStackList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getItemStackList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testJavaEnum()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someJavaEnum"); //$NON-NLS-1$
        assertNull(value.getJavaEnum(MessageSeverityType.class));
        value.setJavaEnum(MessageSeverityType.Error);
        assertEquals(MessageSeverityType.Error, value.getJavaEnum(MessageSeverityType.class));
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testJavaEnumInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setJavaEnum(MessageSeverityType.Error);
        assertNull(value.getJavaEnum(MessageSeverityType.class));
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getJavaEnum(MessageSeverityType.class));
    }
    
    /**
     * test me.
     */
    @Test
    public void testJavaEnumList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someJavaEnumList"); //$NON-NLS-1$
        final MessageSeverityType data1 = MessageSeverityType.Error;
        final MessageSeverityType data2 = MessageSeverityType.Information;
        assertArrayEquals(new MessageSeverityType[0], value.getJavaEnumList(MessageSeverityType.class));
        value.setJavaEnumList(new MessageSeverityType[] { data1, data2 });
        assertArrayEquals(new MessageSeverityType[] { data1, data2 }, value.getJavaEnumList(MessageSeverityType.class));
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testJavaEnumListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final MessageSeverityType data1 = MessageSeverityType.Error;
        final MessageSeverityType data2 = MessageSeverityType.Information;
        value.setJavaEnumList(new MessageSeverityType[] { data1, data2 });
        assertNull(value.getJavaEnumList(MessageSeverityType.class));
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getJavaEnumList(MessageSeverityType.class));
    }
    
    /**
     * test me.
     */
    @Test
    public void testLong()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someLong"); //$NON-NLS-1$
        assertEquals(0L, value.getLong());
        value.setLong(123L);
        assertEquals(123L, value.getLong());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testLongInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setLong(123L);
        assertEquals(0L, value.getLong());
        
        final EditableValue v2 = invalidValue();
        assertEquals(0L, v2.getLong());
    }
    
    /**
     * test me.
     */
    @Test
    public void testLongList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someLongList"); //$NON-NLS-1$
        assertArrayEquals(new long[0], value.getLongList());
        value.setLongList(new long[] { 12L, 23L });
        assertArrayEquals(new long[] { 12L, 23L }, value.getLongList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testLongListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setLongList(new long[] { 12L, 23L });
        assertNull(value.getLongList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getLongList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testObject()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someObject"); //$NON-NLS-1$
        assertNull(value.getObject());
        final Editable data = new Editable();
        value.setObject(data);
        assertSame(data, value.getObject());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testObjectInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final Editable data = new Editable();
        value.setObject(data);
        assertNull(value.getObject());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getObject());
    }
    
    /**
     * test me.
     */
    @Test
    public void testObjectList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someObjectList"); //$NON-NLS-1$
        final Editable data1 = new Editable();
        final Editable data2 = new Editable();
        assertArrayEquals(new Editable[0], value.getObjectList(Editable.class));
        value.setObjectList(new Editable[] { data1, data2 });
        assertArrayEquals(new Editable[] { data1, data2 }, value.getObjectList(Editable.class));
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testObjectListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final Editable data1 = new Editable();
        final Editable data2 = new Editable();
        value.setObjectList(new Editable[] { data1, data2 });
        assertNull(value.getObjectList(Editable.class));
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getObjectList(Editable.class));
    }
    
    /**
     * test me.
     */
    @Test
    public void testPlayer()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "somePlayer"); //$NON-NLS-1$
        assertNull(value.getPlayer());
        final McPlayerInterface data = mock(McPlayerInterface.class);
        value.setPlayer(data);
        assertSame(data, value.getPlayer());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testPlayerInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final McPlayerInterface data = mock(McPlayerInterface.class);
        value.setPlayer(data);
        assertNull(value.getPlayer());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getPlayer());
    }
    
    /**
     * test me.
     */
    @Test
    public void testPlayerList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "somePlayerList"); //$NON-NLS-1$
        final McPlayerInterface data1 = mock(McPlayerInterface.class);
        final McPlayerInterface data2 = mock(McPlayerInterface.class);
        assertArrayEquals(new McPlayerInterface[0], value.getPlayerList());
        value.setPlayerList(new McPlayerInterface[] { data1, data2 });
        assertArrayEquals(new McPlayerInterface[] { data1, data2 }, value.getPlayerList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testPlayerListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final McPlayerInterface data1 = mock(McPlayerInterface.class);
        final McPlayerInterface data2 = mock(McPlayerInterface.class);
        value.setPlayerList(new McPlayerInterface[] { data1, data2 });
        assertNull(value.getPlayerList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getPlayerList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testShort()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someShort"); //$NON-NLS-1$
        assertEquals(0, value.getShort());
        value.setShort((short) 123);
        assertEquals(123, value.getShort());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testShortInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setShort((short) 123);
        assertEquals(0L, value.getShort());
        
        final EditableValue v2 = invalidValue();
        assertEquals(0L, v2.getShort());
    }
    
    /**
     * test me.
     */
    @Test
    public void testShortList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someShortList"); //$NON-NLS-1$
        assertArrayEquals(new short[0], value.getShortList());
        value.setShortList(new short[] { (short) 12, (short) 23 });
        assertArrayEquals(new short[] { (short) 12, (short) 23 }, value.getShortList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testShortListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setShortList(new short[] { (short) 12, (short) 23 });
        assertNull(value.getShortList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getShortList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testString()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someString"); //$NON-NLS-1$
        assertNull(value.getString());
        value.setString("FOO"); //$NON-NLS-1$
        assertEquals("FOO", value.getString()); //$NON-NLS-1$
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testStringInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setString("FOO"); //$NON-NLS-1$
        assertNull(value.getString());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getString());
    }
    
    /**
     * test me.
     */
    @Test
    public void testStringList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someStringList"); //$NON-NLS-1$
        assertArrayEquals(new String[0], value.getStringList());
        value.setStringList(new String[] { "FOO", "BAR" }); //$NON-NLS-1$ //$NON-NLS-2$
        assertArrayEquals(new String[] { "FOO", "BAR" }, value.getStringList()); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testStringListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        value.setStringList(new String[] { "FOO", "BAR" }); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(value.getStringList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getStringList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testVector()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someVector"); //$NON-NLS-1$
        assertNull(value.getVector());
        final ConfigVectorData data = mock(ConfigVectorData.class);
        value.setVector(data);
        assertSame(data, value.getVector());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testVectorInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final ConfigVectorData data = mock(ConfigVectorData.class);
        value.setVector(data);
        assertNull(value.getVector());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getVector());
    }
    
    /**
     * test me.
     */
    @Test
    public void testVectorList()
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someVectorList"); //$NON-NLS-1$
        final ConfigVectorData data1 = mock(ConfigVectorData.class);
        final ConfigVectorData data2 = mock(ConfigVectorData.class);
        assertArrayEquals(new ConfigVectorData[0], value.getVectorList());
        value.setVectorList(new ConfigVectorData[] { data1, data2 });
        assertArrayEquals(new ConfigVectorData[] { data1, data2 }, value.getVectorList());
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testVectorListInvalid() throws Exception
    {
        final Editable obj = new Editable();
        final EditableValue value = value(obj, "someByte"); //$NON-NLS-1$
        final ConfigVectorData data1 = mock(ConfigVectorData.class);
        final ConfigVectorData data2 = mock(ConfigVectorData.class);
        value.setVectorList(new ConfigVectorData[] { data1, data2 });
        assertNull(value.getVectorList());
        
        final EditableValue v2 = invalidValue();
        assertNull(v2.getVectorList());
    }
    
    /**
     * test me.
     */
    @Test
    public void testIsset()
    {
        final Editable obj = new Editable();
        
        assertFalse(value(obj, "someSection").isset()); //$NON-NLS-1$
        
        // isset == true because primitives always have default values...
        assertTrue(value(obj, "someBoolean").isset()); //$NON-NLS-1$
    }
    
    /**
     * test me.
     * 
     * @throws Exception
     *             thrown for problems.
     */
    @Test
    public void testIssetInvalid() throws Exception
    {
        final EditableValue v2 = invalidValue();
        assertFalse(v2.isset());
    }
    
    /**
     * test me.
     */
    @Test
    public void testIsXxx()
    {
        final Editable obj = new Editable();
        
        // boolean
        
        assertTrue(value(obj, "someBoolean").isBoolean()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isBoolean()); //$NON-NLS-1$
        assertFalse(value(obj, "someBooleanList").isBoolean()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someBooleanList").isBooleanList()); //$NON-NLS-1$
        assertFalse(value(obj, "someByteList").isBooleanList()); //$NON-NLS-1$
        assertFalse(value(obj, "someBoolean").isBooleanList()); //$NON-NLS-1$
        
        // byte
        
        assertTrue(value(obj, "someByte").isByte()); //$NON-NLS-1$
        assertFalse(value(obj, "someBoolean").isByte()); //$NON-NLS-1$
        assertFalse(value(obj, "someByteList").isByte()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someByteList").isByteList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isByteList()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isByteList()); //$NON-NLS-1$
        
        // character
        
        assertTrue(value(obj, "someCharacter").isCharacter()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isCharacter()); //$NON-NLS-1$
        assertFalse(value(obj, "someCharacterList").isCharacter()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someCharacterList").isCharacterList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isCharacterList()); //$NON-NLS-1$
        assertFalse(value(obj, "someCharacter").isCharacterList()); //$NON-NLS-1$
        
        // color
        
        assertTrue(value(obj, "someColor").isColor()); //$NON-NLS-1$
        assertFalse(value(obj, "someBoolean").isColor()); //$NON-NLS-1$
        assertFalse(value(obj, "someColorList").isColor()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someColorList").isColorList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isColorList()); //$NON-NLS-1$
        assertFalse(value(obj, "someColor").isColorList()); //$NON-NLS-1$
        
        // double
        
        assertTrue(value(obj, "someDouble").isDouble()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isDouble()); //$NON-NLS-1$
        assertFalse(value(obj, "someDoubleList").isDouble()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someDoubleList").isDoubleList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isDoubleList()); //$NON-NLS-1$
        assertFalse(value(obj, "someDouble").isDoubleList()); //$NON-NLS-1$
        
        // enum
        
        assertTrue(value(obj, "someEnum").isEnum()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isEnum()); //$NON-NLS-1$
        assertFalse(value(obj, "someEnumList").isEnum()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someEnumList").isEnumList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isEnumList()); //$NON-NLS-1$
        assertFalse(value(obj, "someEnum").isEnumList()); //$NON-NLS-1$
        
        // float
        
        assertTrue(value(obj, "someFloat").isFloat()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isFloat()); //$NON-NLS-1$
        assertFalse(value(obj, "someFloatList").isFloat()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someFloatList").isFloatList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isFloatList()); //$NON-NLS-1$
        assertFalse(value(obj, "someFloat").isFloatList()); //$NON-NLS-1$
        
        // int
        
        assertTrue(value(obj, "someInt").isInt()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isInt()); //$NON-NLS-1$
        assertFalse(value(obj, "someIntList").isInt()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someIntList").isIntList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isIntList()); //$NON-NLS-1$
        assertFalse(value(obj, "someInt").isIntList()); //$NON-NLS-1$
        
        // item stack
        
        assertTrue(value(obj, "someItemStack").isItemStack()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isItemStack()); //$NON-NLS-1$
        assertFalse(value(obj, "someItemStackList").isItemStack()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someItemStackList").isItemStackList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isItemStackList()); //$NON-NLS-1$
        assertFalse(value(obj, "someItemStack").isItemStackList()); //$NON-NLS-1$
        
        // java enum
        
        assertTrue(value(obj, "someJavaEnum").isJavaEnum()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isJavaEnum()); //$NON-NLS-1$
        assertFalse(value(obj, "someJavaEnumList").isJavaEnum()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someJavaEnumList").isJavaEnumList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isJavaEnumList()); //$NON-NLS-1$
        assertFalse(value(obj, "someJavaEnum").isJavaEnumList()); //$NON-NLS-1$
        
        // long
        
        assertTrue(value(obj, "someLong").isLong()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isLong()); //$NON-NLS-1$
        assertFalse(value(obj, "someLongList").isLong()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someLongList").isLongList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isLongList()); //$NON-NLS-1$
        assertFalse(value(obj, "someLong").isLongList()); //$NON-NLS-1$
        
        // object
        
        assertTrue(value(obj, "someObject").isObject()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isObject()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isObject()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someObjectList").isObjectList()); //$NON-NLS-1$
        assertFalse(value(obj, "someBooleanList").isObjectList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObject").isObjectList()); //$NON-NLS-1$
        
        // player
        
        assertTrue(value(obj, "somePlayer").isPlayer()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isPlayer()); //$NON-NLS-1$
        assertFalse(value(obj, "somePlayerList").isPlayer()); //$NON-NLS-1$
        
        assertTrue(value(obj, "somePlayerList").isPlayerList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isPlayerList()); //$NON-NLS-1$
        assertFalse(value(obj, "somePlayer").isPlayerList()); //$NON-NLS-1$
        
        // short
        
        assertTrue(value(obj, "someShort").isShort()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isShort()); //$NON-NLS-1$
        assertFalse(value(obj, "someShortList").isShort()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someShortList").isShortList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isShortList()); //$NON-NLS-1$
        assertFalse(value(obj, "someShort").isShortList()); //$NON-NLS-1$
        
        // string
        
        assertTrue(value(obj, "someString").isString()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isString()); //$NON-NLS-1$
        assertFalse(value(obj, "someStringList").isString()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someStringList").isStringList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isStringList()); //$NON-NLS-1$
        assertFalse(value(obj, "someString").isStringList()); //$NON-NLS-1$
        
        // vector
        
        assertTrue(value(obj, "someVector").isVector()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isVector()); //$NON-NLS-1$
        assertFalse(value(obj, "someVectorList").isVector()); //$NON-NLS-1$
        
        assertTrue(value(obj, "someVectorList").isVectorList()); //$NON-NLS-1$
        assertFalse(value(obj, "someObjectList").isVectorList()); //$NON-NLS-1$
        assertFalse(value(obj, "someVector").isVectorList()); //$NON-NLS-1$
        
        // section
        
        assertTrue(value(obj, "someSection").isSection()); //$NON-NLS-1$
        assertFalse(value(obj, "someByte").isSection()); //$NON-NLS-1$
    }
    
    /**
     * returns editable.
     * 
     * @param obj
     *            object
     * @param name
     *            name
     * @param save
     *            save function
     * @param rollback
     *            rollback function
     * @return editable.
     */
    private EditableValue value(Editable obj, String name, Runnable save, Runnable rollback)
    {
        for (final EditableValue edit : obj.getEditableValues(save, rollback))
        {
            if (name.equals(edit.path()))
            {
                return edit;
            }
        }
        fail();
        return null;
    }
    
    /**
     * returns editable.
     * 
     * @param obj
     *            object
     * @param name
     *            name
     * @return editable.
     */
    private EditableValue value(Editable obj, String name)
    {
        return value(obj, name, null, null);
    }
    
    /**
     * returns invalid value.
     * 
     * @return editable.
     * @throws Exception
     *             thrown on problems.
     */
    private EditableValue invalidValue() throws Exception
    {
        final Editable obj = new Editable();
        final Class<?> valueClazz = Class.forName("de.minigameslib.mclib.api.config.EditableDataFragment$EditableValueImpl"); //$NON-NLS-1$
        final Class<?> fieldDescClazz = Class.forName("de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment$FieldDescriptor"); //$NON-NLS-1$
        final Constructor<?> valueCtor = valueClazz.getConstructor(EditableDataFragment.class, fieldDescClazz, Runnable.class, Runnable.class);
        valueCtor.setAccessible(true);
        final Constructor<?> fieldCtor = fieldDescClazz.getConstructor(Field.class);
        fieldCtor.setAccessible(true);
        final Object fieldDesc = fieldCtor.newInstance(Dummy.class.getDeclaredField("field")); //$NON-NLS-1$
        final Object value = valueCtor.newInstance(obj, fieldDesc, null, null);
        return (EditableValue) value;
    }
    
    /** dummy class. */
    private static final class Dummy
    {
        /** the field. */
        @SuppressWarnings("unused")
        public boolean field;
    }
    
    /**
     * editable data fragment.
     */
    private static final class Editable extends EditableDataFragment
    {
        
        /**
         * Constructor.
         */
        public Editable()
        {
            // empty
        }
        
        /** data field. */
        @PersistentField
        public MessageSeverityType         someJavaEnum;
        
        /** data field. */
        @PersistentField
        public CommonMessages              someEnum;
        
        /** data field. */
        @PersistentField
        public boolean                     someBoolean;
        
        /** data field. */
        @PersistentField
        public byte                        someByte;
        
        /** data field. */
        @PersistentField
        public char                        someCharacter;
        
        /** data field. */
        @PersistentField
        public ColorDataFragment           someColor;
        
        /** data field. */
        @PersistentField
        public double                      someDouble;
        
        /** data field. */
        @PersistentField
        public float                       someFloat;
        
        /** data field. */
        @PersistentField
        public int                         someInt;
        
        /** data field. */
        @PersistentField
        public ItemStackDataFragment       someItemStack;
        
        /** data field. */
        @PersistentField
        public DataSection                 someSection;
        
        /** data field. */
        @PersistentField
        public long                        someLong;
        
        /** data field. */
        @PersistentField
        public Editable                    someObject;
        
        /** data field. */
        @PersistentField
        public PlayerDataFragment          somePlayer;
        
        /** data field. */
        @PersistentField
        public short                       someShort;
        
        /** data field. */
        @PersistentField
        public String                      someString;
        
        /** data field. */
        @PersistentField
        public VectorDataFragment          someVector;
        
        /** data field. */
        @PersistentField
        public List<MessageSeverityType>   someJavaEnumList  = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<CommonMessages>        someEnumList      = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Boolean>               someBooleanList   = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Byte>                  someByteList      = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Character>             someCharacterList = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<ColorDataFragment>     someColorList     = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Double>                someDoubleList    = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Float>                 someFloatList     = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Integer>               someIntList       = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<ItemStackDataFragment> someItemStackList = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Long>                  someLongList      = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Editable>              someObjectList    = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<PlayerDataFragment>    somePlayerList    = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<Short>                 someShortList     = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<String>                someStringList    = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<VectorDataFragment>    someVectorList    = new ArrayList<>();
        
    }
    
}
