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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.EditableValue;
import de.minigameslib.mclib.api.config.EditableDataFragment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
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
     * @return editable.
     */
    private EditableValue value(Editable obj, String name)
    {
        for (final EditableValue edit : obj.getEditableValues(null, null))
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
        public List<MessageSeverityType>   someJavaEnumList   = new ArrayList<>();
        
        /** data field. */
        @PersistentField
        public List<CommonMessages>        someEnumList       = new ArrayList<>();
        
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
