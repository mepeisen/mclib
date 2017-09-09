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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.EditableValue;
import de.minigameslib.mclib.api.GenericValue;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.ConfigColorData;
import de.minigameslib.mclib.api.config.ConfigVectorData;
import de.minigameslib.mclib.api.config.EditableDataFragment;
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
import de.minigameslib.mclib.api.validate.ValidatorInterface;
import de.minigameslib.mclib.shared.api.com.ColorDataFragment;
import de.minigameslib.mclib.shared.api.com.ItemStackDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mclib.shared.api.com.VectorDataFragment;

/**
 * Test validation or config values.
 * 
 * @author mepeisen
 */
public class ConfigValidationEditableTest
{
    
    /**
     * Tests all.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateAll() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testIssetBool").verifyConfig(); //$NON-NLS-1$
    }
    
    /**
     * Tests all.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateAllSuccess() throws McException
    {
        final Editable2 obj = new Editable2();
        value(obj, "var").verifyConfig(); //$NON-NLS-1$
    }
    
    // validate isset
    
    /**
     * Tests {@link ValidateIsset}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateIssetFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testIssetBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateIsset}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateIssetFails2() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testIssetInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateIsset}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateIssetSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testIssetBool").setBoolean(false); //$NON-NLS-1$
        value(obj, "testIssetBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateIsset}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateIssetSuccess2() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testIssetInt").setInt(4); //$NON-NLS-1$
        value(obj, "testIssetInt").validate(); //$NON-NLS-1$
    }
    
    // validate float max
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxFloat").setFloat(-100); //$NON-NLS-1$
        value(obj, "testFMaxFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMaxFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxFloat").setFloat(5); //$NON-NLS-1$
        value(obj, "testFMaxFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccessOnNotSetWithDouble() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccessWithDouble() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxDouble").setDouble(-100); //$NON-NLS-1$
        value(obj, "testFMaxDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMaxFailsWithDouble() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxDouble").setDouble(5); //$NON-NLS-1$
        value(obj, "testFMaxDouble").validate(); //$NON-NLS-1$
    }
    
    // validate float min
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinFloat").setFloat(50); //$NON-NLS-1$
        value(obj, "testFMinFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMinFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinFloat").setFloat(5); //$NON-NLS-1$
        value(obj, "testFMinFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccessOnNotSetWithDouble() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccessWithDouble() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinDouble").setDouble(50); //$NON-NLS-1$
        value(obj, "testFMinDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMinFailsWithDouble() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinDouble").setDouble(5); //$NON-NLS-1$
        value(obj, "testFMinDouble").validate(); //$NON-NLS-1$
    }
    
    // validate float max on lists
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccessOnNotSetList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxFloatList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccessList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxFloatList").setFloatList(new float[] { -100, -101, -102 }); //$NON-NLS-1$
        value(obj, "testFMaxFloatList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMaxFailsList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxFloatList").setFloatList(new float[] { -100, -101, 5 }); //$NON-NLS-1$
        value(obj, "testFMaxFloatList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccessOnNotSetWithDoubleList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxDoubleList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMaxSuccessWithDoubleList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxDoubleList").setDoubleList(new double[] { -100, -101, -102 }); //$NON-NLS-1$
        value(obj, "testFMaxDoubleList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMaxFailsWithDoubleList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMaxDoubleList").setDoubleList(new double[] { -100, -101, 5 }); //$NON-NLS-1$
        value(obj, "testFMaxDoubleList").validate(); //$NON-NLS-1$
    }
    
    // validate float min on lists
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccessOnNotSetList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinFloatList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccessList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinFloatList").setFloatList(new float[] { 30, 40, 50 }); //$NON-NLS-1$
        value(obj, "testFMinFloatList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMinFailsList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinFloatList").setFloatList(new float[] { 30, 40, 5 }); //$NON-NLS-1$
        value(obj, "testFMinFloatList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccessOnNotSetWithDoubleList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinDoubleList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateFMinSuccessWithDoubleList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinDoubleList").setDoubleList(new double[] { 30, 40, 50 }); //$NON-NLS-1$
        value(obj, "testFMinDoubleList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateFMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateFMinFailsWithDoubleList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testFMinDoubleList").setDoubleList(new double[] { 30, 40, 5 }); //$NON-NLS-1$
        value(obj, "testFMinDoubleList").validate(); //$NON-NLS-1$
    }
    
    // validate long max
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxInt").setInt(-100); //$NON-NLS-1$
        value(obj, "testLMaxInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxInt").setInt(5); //$NON-NLS-1$
        value(obj, "testLMaxInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSetWithByte() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessWithByte() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxByte").setByte((byte) -100); //$NON-NLS-1$
        value(obj, "testLMaxByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFailsWithByte() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxByte").setByte((byte) 5); //$NON-NLS-1$
        value(obj, "testLMaxByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSetWithShort() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessWithShort() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxShort").setShort((short) -100); //$NON-NLS-1$
        value(obj, "testLMaxShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFailsWithShort() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxShort").setShort((short) 5); //$NON-NLS-1$
        value(obj, "testLMaxShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSetWithLong() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessWithLong() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxLong").setLong(-100); //$NON-NLS-1$
        value(obj, "testLMaxLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFailsWithLong() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxLong").setLong(5); //$NON-NLS-1$
        value(obj, "testLMaxLong").validate(); //$NON-NLS-1$
    }
    
    // validate long min
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinInt").setInt(50); //$NON-NLS-1$
        value(obj, "testLMinInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinInt").setInt(5); //$NON-NLS-1$
        value(obj, "testLMinInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSetWithByte() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessWithByte() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinByte").setByte((byte) 50); //$NON-NLS-1$
        value(obj, "testLMinByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFailsWithByte() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinByte").setByte((byte) 5); //$NON-NLS-1$
        value(obj, "testLMinByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSetWithShort() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessWithShort() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinShort").setShort((short) 50); //$NON-NLS-1$
        value(obj, "testLMinShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFailsWithShort() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinShort").setShort((short) 5); //$NON-NLS-1$
        value(obj, "testLMinShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSetWithLong() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessWithLong() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinLong").setLong(50); //$NON-NLS-1$
        value(obj, "testLMinLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFailsWithLong() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinLong").setLong(5); //$NON-NLS-1$
        value(obj, "testLMinLong").validate(); //$NON-NLS-1$
    }
    
    // validate long max on lists
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSetList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxIntList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxIntList").setIntList(new int[] { -100, -101, -102 }); //$NON-NLS-1$
        value(obj, "testLMaxIntList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFailsList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxIntList").setIntList(new int[] { -100, -101, 5 }); //$NON-NLS-1$
        value(obj, "testLMaxIntList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSetWithByteList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxByteList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessWithByteList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxByteList").setByteList(new byte[] { (byte) -100, (byte) -101, (byte) -102 }); //$NON-NLS-1$
        value(obj, "testLMaxByteList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFailsWithByteList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxByteList").setByteList(new byte[] { (byte) -100, (byte) -101, (byte) 5 }); //$NON-NLS-1$
        value(obj, "testLMaxByteList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSetWithShortList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxShortList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessWithShortList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxShortList").setShortList(new short[] { (short) -100, (short) -101, (short) -102 }); //$NON-NLS-1$
        value(obj, "testLMaxShortList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFailsWithShortList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxShortList").setShortList(new short[] { (short) -100, (short) -101, (short) 5 }); //$NON-NLS-1$
        value(obj, "testLMaxShortList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessOnNotSetWithLongList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxLongList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMaxSuccessWithLongList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxLongList").setLongList(new long[] { -100, -101, -102 }); //$NON-NLS-1$
        value(obj, "testLMaxLongList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMaxFailsWithLongList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMaxLongList").setLongList(new long[] { -100, -101, 5 }); //$NON-NLS-1$
        value(obj, "testLMaxLongList").validate(); //$NON-NLS-1$
    }
    
    // validate long min on lists
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSetList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinIntList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinIntList").setIntList(new int[] { 30, 40, 50 }); //$NON-NLS-1$
        value(obj, "testLMinIntList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFailsList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinIntList").setIntList(new int[] { 30, 40, 5 }); //$NON-NLS-1$
        value(obj, "testLMinIntList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSetWithByteList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinByteList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessWithByteList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinByteList").setByteList(new byte[] { (byte) 30, (byte) 40, (byte) 50 }); //$NON-NLS-1$
        value(obj, "testLMinByteList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFailsWithByteList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinByteList").setByteList(new byte[] { (byte) 30, (byte) 40, (byte) 5 }); //$NON-NLS-1$
        value(obj, "testLMinByteList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSetWithShortList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinShortList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessWithShortList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinShortList").setShortList(new short[] { (short) 30, (short) 40, (short) 50 }); //$NON-NLS-1$
        value(obj, "testLMinShortList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFailsWithShortList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinShortList").setShortList(new short[] { (short) 30, (short) 40, (short) 5 }); //$NON-NLS-1$
        value(obj, "testLMinShortList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessOnNotSetWithLongList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinLongList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateLMinSuccessWithLongList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinLongList").setLongList(new long[] { 30, 40, 50 }); //$NON-NLS-1$
        value(obj, "testLMinLongList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateLMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateLMinFailsWithLongList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testLMinLongList").setLongList(new long[] { 30, 40, 5 }); //$NON-NLS-1$
        value(obj, "testLMinLongList").validate(); //$NON-NLS-1$
    }
    
    // validate string length
    
    /**
     * Tests {@link ValidateStrMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMaxSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMax").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMaxSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMax").setString("12"); //$NON-NLS-1$ //$NON-NLS-2$
        value(obj, "testStrMax").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateStrMaxFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMax").setString("12345678"); //$NON-NLS-1$ //$NON-NLS-2$
        value(obj, "testStrMax").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMaxSuccessOnNotSetList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMaxList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMaxSuccessList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMaxList").setStringList(new String[] { "12", "123", "1234" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        value(obj, "testStrMaxList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateStrMaxFailsList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMaxList").setStringList(new String[] { "12", "123", "12345678" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        value(obj, "testStrMaxList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMinSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMin").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMinSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMin").setString("12345678"); //$NON-NLS-1$ //$NON-NLS-2$
        value(obj, "testStrMin").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateStrMinFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMin").setString("12"); //$NON-NLS-1$ //$NON-NLS-2$
        value(obj, "testStrMin").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMinSuccessOnNotSetList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMinList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testValidateStrMinSuccessList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMinList").setStringList(new String[] { "12345678", "12345679", "123456790" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        value(obj, "testStrMinList").validate(); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link ValidateStrMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testValidateStrMinFailsList() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testStrMinList").setStringList(new String[] { "12345678", "12345679", "12" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        value(obj, "testStrMinList").validate(); //$NON-NLS-1$
    }
    
    // list min/max
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinIntSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinIntSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinInt").setIntList(new int[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMinInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinIntFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinInt").setIntList(new int[] { 1 }); //$NON-NLS-1$
        value(obj, "testListMinInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxIntSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxIntSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxInt").setIntList(new int[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMaxInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxIntFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxInt").setIntList(new int[] { 1, 2, 3 }); //$NON-NLS-1$
        value(obj, "testListMaxInt").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinShortSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinShortSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinShort").setShortList(new short[] { (short) 1, (short) 2 }); //$NON-NLS-1$
        value(obj, "testListMinShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinShortFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinShort").setShortList(new short[] { (short) 1 }); //$NON-NLS-1$
        value(obj, "testListMinShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxShortSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxShortSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxShort").setShortList(new short[] { (short) 1, (short) 2 }); //$NON-NLS-1$
        value(obj, "testListMaxShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxShortFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxShort").setShortList(new short[] { (short) 1, (short) 2, (short) 3 }); //$NON-NLS-1$
        value(obj, "testListMaxShort").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinLongSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinLong").setLongList(new long[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMinLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinLongFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinLong").setLongList(new long[] { 1 }); //$NON-NLS-1$
        value(obj, "testListMinLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxLongSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxLongSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxLong").setLongList(new long[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMaxLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxLongFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxLong").setLongList(new long[] { 1, 2, 3 }); //$NON-NLS-1$
        value(obj, "testListMaxLong").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinFloatSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinFloat").setFloatList(new float[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMinFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinFloatFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinFloat").setFloatList(new float[] { 1 }); //$NON-NLS-1$
        value(obj, "testListMinFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxFloatSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxFloatSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxFloat").setFloatList(new float[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMaxFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxFloatFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxFloat").setFloatList(new float[] { 1, 2, 3 }); //$NON-NLS-1$
        value(obj, "testListMaxFloat").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinDoubleSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinDouble").setDoubleList(new double[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMinDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinDoubleFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinDouble").setDoubleList(new double[] { 1 }); //$NON-NLS-1$
        value(obj, "testListMinDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxDoubleSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxDoubleSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxDouble").setDoubleList(new double[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMaxDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxDoubleFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxDouble").setDoubleList(new double[] { 1, 2, 3 }); //$NON-NLS-1$
        value(obj, "testListMaxDouble").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinBoolSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinBoolSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinBool").setBooleanList(new boolean[] { true, false }); //$NON-NLS-1$
        value(obj, "testListMinBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinBoolFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinBool").setBooleanList(new boolean[] { true }); //$NON-NLS-1$
        value(obj, "testListMinBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxBoolSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxBoolSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxBool").setBooleanList(new boolean[] { true, false }); //$NON-NLS-1$
        value(obj, "testListMaxBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxBoolFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxBool").setBooleanList(new boolean[] { true, false, true }); //$NON-NLS-1$
        value(obj, "testListMaxBool").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinByteSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinByteSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinByte").setByteList(new byte[] { (byte) 1, (byte) 2 }); //$NON-NLS-1$
        value(obj, "testListMinByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinByteFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinByte").setByteList(new byte[] { (byte) 1 }); //$NON-NLS-1$
        value(obj, "testListMinByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxByteSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxByteSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxByte").setByteList(new byte[] { (byte) 1, (byte) 2 }); //$NON-NLS-1$
        value(obj, "testListMaxByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxByteFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxByte").setByteList(new byte[] { (byte) 1, (byte) 2, (byte) 3 }); //$NON-NLS-1$
        value(obj, "testListMaxByte").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinCharSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinChar").setCharacterList(new char[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMinChar").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinCharFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinChar").setCharacterList(new char[] { 1 }); //$NON-NLS-1$
        value(obj, "testListMinChar").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxCharSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxChar").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxCharSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxChar").setCharacterList(new char[] { 1, 2 }); //$NON-NLS-1$
        value(obj, "testListMaxChar").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxCharFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxChar").setCharacterList(new char[] { 1, 2, 3 }); //$NON-NLS-1$
        value(obj, "testListMaxChar").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinStringSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinString").setStringList(new String[] { "1", "2" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        value(obj, "testListMinString").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinStringFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinString").setStringList(new String[] { "1" }); //$NON-NLS-1$ //$NON-NLS-2$
        value(obj, "testListMinString").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxStringSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxString").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxStringSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxString").setStringList(new String[] { "1", "2" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        value(obj, "testListMaxString").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxStringFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxString").setStringList(new String[] { "1", "2", "3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        value(obj, "testListMaxString").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinColorSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinColor").setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3), new ConfigColorData(2, 3, 4) }); //$NON-NLS-1$
        value(obj, "testListMinColor").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinColorFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinColor").setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3) }); //$NON-NLS-1$
        value(obj, "testListMinColor").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxColorSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxColor").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxColorSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxColor").setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3), new ConfigColorData(2, 3, 4) }); //$NON-NLS-1$
        value(obj, "testListMaxColor").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxColorFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxColor").setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3), new ConfigColorData(2, 3, 4), new ConfigColorData(3, 4, 5) }); //$NON-NLS-1$
        value(obj, "testListMaxColor").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMinVectorSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinVector").setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3), new ConfigVectorData(2, 3, 4) }); //$NON-NLS-1$
        value(obj, "testListMinVector").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMin}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMinVectorFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMinVector").setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3) }); //$NON-NLS-1$
        value(obj, "testListMinVector").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxVectorSuccessOnNotSet() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxVector").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error
     */
    @Test
    public void testListMaxVectorSuccess() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxVector").setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3), new ConfigVectorData(2, 3, 4) }); //$NON-NLS-1$
        value(obj, "testListMaxVector").validate(); //$NON-NLS-1$
    }
    
    /**
     * tests {@link ValidateListMax}.
     * 
     * @throws McException
     *             thrown on validation error (expected)
     */
    @Test(expected = McException.class)
    public void testListMaxVectorFails() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "testListMaxVector").setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3), new ConfigVectorData(2, 3, 4), new ConfigVectorData(3, 4, 5) }); //$NON-NLS-1$
        value(obj, "testListMaxVector").validate(); //$NON-NLS-1$
    }
    
    // etc
    
    /**
     * test no validation value.
     * 
     * @throws McException
     *             thrown for problems.
     */
    @Test
    public void testNoValidation() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "noValidation").validate(); //$NON-NLS-1$
    }
    
    /**
     * test custom validation.
     * 
     * @throws McException
     *             thrown for problems.
     */
    @Test(expected = McException.class)
    public void testCustomTrue() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "customValidation").setBoolean(true); //$NON-NLS-1$
        value(obj, "customValidation").validate(); //$NON-NLS-1$
    }
    
    /**
     * test custom validation.
     * 
     * @throws McException
     *             thrown for problems.
     */
    @Test
    public void testCustomFalse() throws McException
    {
        final Editable obj = new Editable();
        value(obj, "customValidation").setBoolean(false); //$NON-NLS-1$
        value(obj, "customValidation").validate(); //$NON-NLS-1$
    }
    
    /**
     * helper enum.
     */
    private static final class Editable2 extends EditableDataFragment
    {
        
        /**
         * Constructor.
         */
        public Editable2()
        {
            // empty
        }
        
        /** dummy value. */
        @ValidateIsset
        @PersistentField
        public Boolean var = Boolean.TRUE;
        
    }
    
    /**
     * helper enum.
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
        
        /** dummy value. */
        @ValidateIsset
        @PersistentField
        public Boolean                     testIssetBool;
        
        /** dummy value. */
        @ValidateIsset
        @PersistentField
        public Integer                     testIssetInt;
        
        /** dummy value. */
        @ValidateFMax(-42)
        @PersistentField
        public Float                       testFMaxFloat;
        
        /** dummy value. */
        @ValidateFMin(13)
        @PersistentField
        public Float                       testFMinFloat;
        
        /** dummy value. */
        @ValidateFMax(-42)
        @PersistentField
        public Double                      testFMaxDouble;
        
        /** dummy value. */
        @ValidateFMin(13)
        @PersistentField
        public Double                      testFMinDouble;
        
        /** dummy value. */
        @ValidateFMax(-42)
        @PersistentField
        public List<Float>                 testFMaxFloatList    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateFMin(13)
        @PersistentField
        public List<Float>                 testFMinFloatList    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateFMax(-42)
        @PersistentField
        public List<Double>                testFMaxDoubleList   = new ArrayList<>();
        
        /** dummy value. */
        @ValidateFMin(13)
        @PersistentField
        public List<Double>                testFMinDoubleList   = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public Integer                     testLMaxInt;
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public Integer                     testLMinInt;
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public Byte                        testLMaxByte;
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public Byte                        testLMinByte;
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public Short                       testLMaxShort;
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public Short                       testLMinShort;
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public Long                        testLMaxLong;
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public Long                        testLMinLong;
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public List<Integer>               testLMaxIntList      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public List<Integer>               testLMinIntList      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public List<Byte>                  testLMaxByteList     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public List<Byte>                  testLMinByteList     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public List<Short>                 testLMaxShortList    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public List<Short>                 testLMinShortList    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMax(-42)
        @PersistentField
        public List<Long>                  testLMaxLongList     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateLMin(13)
        @PersistentField
        public List<Long>                  testLMinLongList     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateStrMin(5)
        @PersistentField
        public String                      testStrMin;
        
        /** dummy value. */
        @ValidateStrMax(7)
        @PersistentField
        public String                      testStrMax;
        
        /** dummy value. */
        @ValidateStrMin(5)
        @PersistentField
        public List<String>                testStrMinList       = new ArrayList<>();
        
        /** dummy value. */
        @ValidateStrMax(7)
        @PersistentField
        public List<String>                testStrMaxList       = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Boolean>               testListMinBool      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Byte>                  testListMinByte      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Character>             testListMinChar      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<ColorDataFragment>     testListMinColor     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Double>                testListMinDouble    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Float>                 testListMinFloat     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Integer>               testListMinInt       = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<ItemStackDataFragment> testListMinItemStack = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Long>                  testListMinLong      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<McPlayerInterface>     testListMinPlayer    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<Short>                 testListMinShort     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<String>                testListMinString    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMin(2)
        @PersistentField
        public List<VectorDataFragment>    testListMinVector    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Boolean>               testListMaxBool      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Byte>                  testListMaxByte      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Character>             testListMaxChar      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<ColorDataFragment>     testListMaxColor     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Double>                testListMaxDouble    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Float>                 testListMaxFloat     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Integer>               testListMaxInt       = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<ItemStackDataFragment> testListMaxItemStack = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Long>                  testListMaxLong      = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<McPlayerInterface>     testListMaxPlayer    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<Short>                 testListMaxShort     = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<String>                testListMaxString    = new ArrayList<>();
        
        /** dummy value. */
        @ValidateListMax(2)
        @PersistentField
        public List<VectorDataFragment>    testListMaxVector    = new ArrayList<>();
        
        /** no validation. */
        @PersistentField
        public boolean                     noValidation;
        
        /** custom validation. */
        @Validator(BoolValidate.class)
        @PersistentField
        public boolean                     customValidation;
        
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
    private EditableValue value(EditableDataFragment obj, String name, Runnable save, Runnable rollback)
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
    private EditableValue value(EditableDataFragment obj, String name)
    {
        return value(obj, name, null, null);
    }
    
    /**
     * custom validation.
     * 
     * @author mepeisen
     */
    public static final class BoolValidate implements ValidatorInterface
    {
        
        @Override
        public void validate(GenericValue cvi) throws McException
        {
            if (cvi.getBoolean())
            {
                throw new McException(CommonMessages.ValidateNotSet);
            }
        }
        
    }
    
}
