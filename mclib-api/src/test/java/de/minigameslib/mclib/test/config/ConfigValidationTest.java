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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.GenericValue;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.config.ConfigColorData;
import de.minigameslib.mclib.api.config.ConfigInterface;
import de.minigameslib.mclib.api.config.ConfigServiceInterface;
import de.minigameslib.mclib.api.config.ConfigVectorData;
import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationBoolList;
import de.minigameslib.mclib.api.config.ConfigurationByte;
import de.minigameslib.mclib.api.config.ConfigurationByteList;
import de.minigameslib.mclib.api.config.ConfigurationCharacterList;
import de.minigameslib.mclib.api.config.ConfigurationColorList;
import de.minigameslib.mclib.api.config.ConfigurationDouble;
import de.minigameslib.mclib.api.config.ConfigurationDoubleList;
import de.minigameslib.mclib.api.config.ConfigurationFloat;
import de.minigameslib.mclib.api.config.ConfigurationFloatList;
import de.minigameslib.mclib.api.config.ConfigurationInt;
import de.minigameslib.mclib.api.config.ConfigurationIntList;
import de.minigameslib.mclib.api.config.ConfigurationItemStackList;
import de.minigameslib.mclib.api.config.ConfigurationLong;
import de.minigameslib.mclib.api.config.ConfigurationLongList;
import de.minigameslib.mclib.api.config.ConfigurationPlayerList;
import de.minigameslib.mclib.api.config.ConfigurationShort;
import de.minigameslib.mclib.api.config.ConfigurationShortList;
import de.minigameslib.mclib.api.config.ConfigurationString;
import de.minigameslib.mclib.api.config.ConfigurationStringList;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mclib.api.config.ConfigurationVectorList;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
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
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test validation or config values.
 * 
 * @author mepeisen
 */
public class ConfigValidationTest
{
    
    /** the config mock. */
    private ConfigInterface        config;
    /** mocked config file. */
    private MemoryDataSection      file;
    /** server. */
    private Server                 server;
    /** library. */
    private ConfigServiceInterface lib;
    /** objects. */
    private ObjectServiceInterface objsrv;
    
    /**
     * Some setup.
     * 
     * @throws ClassNotFoundException
     *             thrown on problems
     */
    @Before
    public void setup() throws ClassNotFoundException
    {
        this.lib = mock(ConfigServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.config.ConfigServiceCache"), "SERVICES", this.lib); //$NON-NLS-1$ //$NON-NLS-2$
        final McLibInterface mclib = mock(McLibInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.McLibCache"), "SERVICES", mclib); //$NON-NLS-1$ //$NON-NLS-2$
        this.objsrv = mock(ObjectServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.objects.ObjectServiceCache"), "SERVICES", this.objsrv); //$NON-NLS-1$ //$NON-NLS-2$
        this.config = mock(ConfigInterface.class);
        when(this.lib.getConfigFromCfg(anyObject())).thenReturn(this.config);
        this.file = new MemoryDataSection();
        when(this.config.getConfig(anyString())).thenReturn(this.file);
        this.server = mock(Server.class);
        Whitebox.setInternalState(Bukkit.class, "server", this.server); //$NON-NLS-1$
        final ItemFactory itemFactory = mock(ItemFactory.class);
        when(itemFactory.equals(anyObject(), anyObject())).thenReturn(Boolean.TRUE);
        when(this.server.getItemFactory()).thenReturn(itemFactory);
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
        ConfigValues.TestIssetBool.validate();
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
        ConfigValues.TestIssetInt.validate();
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
        ConfigValues.TestIssetBool.setBoolean(false);
        ConfigValues.TestIssetBool.validate();
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
        ConfigValues.TestIssetInt.setInt(4);
        ConfigValues.TestIssetInt.validate();
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
        ConfigValues.TestFMaxFloat.validate();
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
        ConfigValues.TestFMaxFloat.setFloat(-100);
        ConfigValues.TestFMaxFloat.validate();
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
        ConfigValues.TestFMaxFloat.setFloat(5);
        ConfigValues.TestFMaxFloat.validate();
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
        ConfigValues.TestFMaxDouble.validate();
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
        ConfigValues.TestFMaxDouble.setDouble(-100);
        ConfigValues.TestFMaxDouble.validate();
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
        ConfigValues.TestFMaxDouble.setDouble(5);
        ConfigValues.TestFMaxDouble.validate();
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
        ConfigValues.TestFMinFloat.validate();
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
        ConfigValues.TestFMinFloat.setFloat(50);
        ConfigValues.TestFMinFloat.validate();
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
        ConfigValues.TestFMinFloat.setFloat(5);
        ConfigValues.TestFMinFloat.validate();
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
        ConfigValues.TestFMinDouble.validate();
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
        ConfigValues.TestFMinDouble.setDouble(50);
        ConfigValues.TestFMinDouble.validate();
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
        ConfigValues.TestFMinDouble.setDouble(5);
        ConfigValues.TestFMinDouble.validate();
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
        ConfigValues.TestFMaxFloatList.validate();
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
        ConfigValues.TestFMaxFloatList.setFloatList(new float[] { -100, -101, -102 });
        ConfigValues.TestFMaxFloatList.validate();
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
        ConfigValues.TestFMaxFloatList.setFloatList(new float[] { -100, -101, 5 });
        ConfigValues.TestFMaxFloatList.validate();
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
        ConfigValues.TestFMaxDoubleList.validate();
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
        ConfigValues.TestFMaxDoubleList.setDoubleList(new double[] { -100, -101, -102 });
        ConfigValues.TestFMaxDoubleList.validate();
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
        ConfigValues.TestFMaxDoubleList.setDoubleList(new double[] { -100, -101, 5 });
        ConfigValues.TestFMaxDoubleList.validate();
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
        ConfigValues.TestFMinFloatList.validate();
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
        ConfigValues.TestFMinFloatList.setFloatList(new float[] { 30, 40, 50 });
        ConfigValues.TestFMinFloatList.validate();
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
        ConfigValues.TestFMinFloatList.setFloatList(new float[] { 30, 40, 5 });
        ConfigValues.TestFMinFloatList.validate();
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
        ConfigValues.TestFMinDoubleList.validate();
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
        ConfigValues.TestFMinDoubleList.setDoubleList(new double[] { 30, 40, 50 });
        ConfigValues.TestFMinDoubleList.validate();
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
        ConfigValues.TestFMinDoubleList.setDoubleList(new double[] { 30, 40, 5 });
        ConfigValues.TestFMinDoubleList.validate();
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
        ConfigValues.TestLMaxInt.validate();
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
        ConfigValues.TestLMaxInt.setInt(-100);
        ConfigValues.TestLMaxInt.validate();
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
        ConfigValues.TestLMaxInt.setInt(5);
        ConfigValues.TestLMaxInt.validate();
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
        ConfigValues.TestLMaxByte.validate();
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
        ConfigValues.TestLMaxByte.setByte((byte) -100);
        ConfigValues.TestLMaxByte.validate();
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
        ConfigValues.TestLMaxByte.setByte((byte) 5);
        ConfigValues.TestLMaxByte.validate();
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
        ConfigValues.TestLMaxShort.validate();
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
        ConfigValues.TestLMaxShort.setShort((short) -100);
        ConfigValues.TestLMaxShort.validate();
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
        ConfigValues.TestLMaxShort.setShort((short) 5);
        ConfigValues.TestLMaxShort.validate();
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
        ConfigValues.TestLMaxLong.validate();
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
        ConfigValues.TestLMaxLong.setLong(-100);
        ConfigValues.TestLMaxLong.validate();
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
        ConfigValues.TestLMaxLong.setLong(5);
        ConfigValues.TestLMaxLong.validate();
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
        ConfigValues.TestLMinInt.validate();
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
        ConfigValues.TestLMinInt.setInt(50);
        ConfigValues.TestLMinInt.validate();
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
        ConfigValues.TestLMinInt.setInt(5);
        ConfigValues.TestLMinInt.validate();
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
        ConfigValues.TestLMinByte.validate();
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
        ConfigValues.TestLMinByte.setByte((byte) 50);
        ConfigValues.TestLMinByte.validate();
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
        ConfigValues.TestLMinByte.setByte((byte) 5);
        ConfigValues.TestLMinByte.validate();
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
        ConfigValues.TestLMinShort.validate();
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
        ConfigValues.TestLMinShort.setShort((short) 50);
        ConfigValues.TestLMinShort.validate();
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
        ConfigValues.TestLMinShort.setShort((short) 5);
        ConfigValues.TestLMinShort.validate();
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
        ConfigValues.TestLMinLong.validate();
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
        ConfigValues.TestLMinLong.setLong(50);
        ConfigValues.TestLMinLong.validate();
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
        ConfigValues.TestLMinLong.setLong(5);
        ConfigValues.TestLMinLong.validate();
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
        ConfigValues.TestLMaxIntList.validate();
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
        ConfigValues.TestLMaxIntList.setIntList(new int[] { -100, -101, -102 });
        ConfigValues.TestLMaxIntList.validate();
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
        ConfigValues.TestLMaxIntList.setIntList(new int[] { -100, -101, 5 });
        ConfigValues.TestLMaxIntList.validate();
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
        ConfigValues.TestLMaxByteList.validate();
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
        ConfigValues.TestLMaxByteList.setByteList(new byte[] { (byte) -100, (byte) -101, (byte) -102 });
        ConfigValues.TestLMaxByteList.validate();
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
        ConfigValues.TestLMaxByteList.setByteList(new byte[] { (byte) -100, (byte) -101, (byte) 5 });
        ConfigValues.TestLMaxByteList.validate();
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
        ConfigValues.TestLMaxShortList.validate();
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
        ConfigValues.TestLMaxShortList.setShortList(new short[] { (short) -100, (short) -101, (short) -102 });
        ConfigValues.TestLMaxShortList.validate();
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
        ConfigValues.TestLMaxShortList.setShortList(new short[] { (short) -100, (short) -101, (short) 5 });
        ConfigValues.TestLMaxShortList.validate();
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
        ConfigValues.TestLMaxLongList.validate();
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
        ConfigValues.TestLMaxLongList.setLongList(new long[] { -100, -101, -102 });
        ConfigValues.TestLMaxLongList.validate();
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
        ConfigValues.TestLMaxLongList.setLongList(new long[] { -100, -101, 5 });
        ConfigValues.TestLMaxLongList.validate();
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
        ConfigValues.TestLMinIntList.validate();
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
        ConfigValues.TestLMinIntList.setIntList(new int[] { 30, 40, 50 });
        ConfigValues.TestLMinIntList.validate();
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
        ConfigValues.TestLMinIntList.setIntList(new int[] { 30, 40, 5 });
        ConfigValues.TestLMinIntList.validate();
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
        ConfigValues.TestLMinByteList.validate();
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
        ConfigValues.TestLMinByteList.setByteList(new byte[] { (byte) 30, (byte) 40, (byte) 50 });
        ConfigValues.TestLMinByteList.validate();
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
        ConfigValues.TestLMinByteList.setByteList(new byte[] { (byte) 30, (byte) 40, (byte) 5 });
        ConfigValues.TestLMinByteList.validate();
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
        ConfigValues.TestLMinShortList.validate();
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
        ConfigValues.TestLMinShortList.setShortList(new short[] { (short) 30, (short) 40, (short) 50 });
        ConfigValues.TestLMinShortList.validate();
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
        ConfigValues.TestLMinShortList.setShortList(new short[] { (short) 30, (short) 40, (short) 5 });
        ConfigValues.TestLMinShortList.validate();
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
        ConfigValues.TestLMinLongList.validate();
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
        ConfigValues.TestLMinLongList.setLongList(new long[] { 30, 40, 50 });
        ConfigValues.TestLMinLongList.validate();
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
        ConfigValues.TestLMinLongList.setLongList(new long[] { 30, 40, 5 });
        ConfigValues.TestLMinLongList.validate();
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
        ConfigValues.TestStrMax.validate();
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
        ConfigValues.TestStrMax.setString("12"); //$NON-NLS-1$
        ConfigValues.TestStrMax.validate();
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
        ConfigValues.TestStrMax.setString("12345678"); //$NON-NLS-1$
        ConfigValues.TestStrMax.validate();
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
        ConfigValues.TestStrMaxList.validate();
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
        ConfigValues.TestStrMaxList.setStringList(new String[] { "12", "123", "1234" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ConfigValues.TestStrMaxList.validate();
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
        ConfigValues.TestStrMaxList.setStringList(new String[] { "12", "123", "12345678" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ConfigValues.TestStrMaxList.validate();
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
        ConfigValues.TestStrMin.validate();
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
        ConfigValues.TestStrMin.setString("12345678"); //$NON-NLS-1$
        ConfigValues.TestStrMin.validate();
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
        ConfigValues.TestStrMin.setString("12"); //$NON-NLS-1$
        ConfigValues.TestStrMin.validate();
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
        ConfigValues.TestStrMinList.validate();
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
        ConfigValues.TestStrMinList.setStringList(new String[] { "12345678", "12345679", "123456790" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ConfigValues.TestStrMinList.validate();
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
        ConfigValues.TestStrMinList.setStringList(new String[] { "12345678", "12345679", "12" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ConfigValues.TestStrMinList.validate();
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
        ConfigValues.TestListMinInt.validate();
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
        ConfigValues.TestListMinInt.setIntList(new int[] { 1, 2 });
        ConfigValues.TestListMinInt.validate();
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
        ConfigValues.TestListMinInt.setIntList(new int[] { 1 });
        ConfigValues.TestListMinInt.validate();
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
        ConfigValues.TestListMaxInt.validate();
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
        ConfigValues.TestListMaxInt.setIntList(new int[] { 1, 2 });
        ConfigValues.TestListMaxInt.validate();
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
        ConfigValues.TestListMaxInt.setIntList(new int[] { 1, 2, 3 });
        ConfigValues.TestListMaxInt.validate();
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
        ConfigValues.TestListMinShort.validate();
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
        ConfigValues.TestListMinShort.setShortList(new short[] { (short) 1, (short) 2 });
        ConfigValues.TestListMinShort.validate();
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
        ConfigValues.TestListMinShort.setShortList(new short[] { (short) 1 });
        ConfigValues.TestListMinShort.validate();
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
        ConfigValues.TestListMaxShort.validate();
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
        ConfigValues.TestListMaxShort.setShortList(new short[] { (short) 1, (short) 2 });
        ConfigValues.TestListMaxShort.validate();
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
        ConfigValues.TestListMaxShort.setShortList(new short[] { (short) 1, (short) 2, (short) 3 });
        ConfigValues.TestListMaxShort.validate();
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
        ConfigValues.TestListMinLong.setLongList(new long[] { 1, 2 });
        ConfigValues.TestListMinLong.validate();
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
        ConfigValues.TestListMinLong.setLongList(new long[] { 1 });
        ConfigValues.TestListMinLong.validate();
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
        ConfigValues.TestListMaxLong.validate();
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
        ConfigValues.TestListMaxLong.setLongList(new long[] { 1, 2 });
        ConfigValues.TestListMaxLong.validate();
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
        ConfigValues.TestListMaxLong.setLongList(new long[] { 1, 2, 3 });
        ConfigValues.TestListMaxLong.validate();
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
        ConfigValues.TestListMinFloat.setFloatList(new float[] { 1, 2 });
        ConfigValues.TestListMinFloat.validate();
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
        ConfigValues.TestListMinFloat.setFloatList(new float[] { 1 });
        ConfigValues.TestListMinFloat.validate();
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
        ConfigValues.TestListMaxFloat.validate();
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
        ConfigValues.TestListMaxFloat.setFloatList(new float[] { 1, 2 });
        ConfigValues.TestListMaxFloat.validate();
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
        ConfigValues.TestListMaxFloat.setFloatList(new float[] { 1, 2, 3 });
        ConfigValues.TestListMaxFloat.validate();
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
        ConfigValues.TestListMinDouble.setDoubleList(new double[] { 1, 2 });
        ConfigValues.TestListMinDouble.validate();
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
        ConfigValues.TestListMinDouble.setDoubleList(new double[] { 1 });
        ConfigValues.TestListMinDouble.validate();
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
        ConfigValues.TestListMaxDouble.validate();
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
        ConfigValues.TestListMaxDouble.setDoubleList(new double[] { 1, 2 });
        ConfigValues.TestListMaxDouble.validate();
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
        ConfigValues.TestListMaxDouble.setDoubleList(new double[] { 1, 2, 3 });
        ConfigValues.TestListMaxDouble.validate();
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
        ConfigValues.TestListMinBool.validate();
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
        ConfigValues.TestListMinBool.setBooleanList(new boolean[] { true, false });
        ConfigValues.TestListMinBool.validate();
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
        ConfigValues.TestListMinBool.setBooleanList(new boolean[] { true });
        ConfigValues.TestListMinBool.validate();
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
        ConfigValues.TestListMaxBool.validate();
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
        ConfigValues.TestListMaxBool.setBooleanList(new boolean[] { true, false });
        ConfigValues.TestListMaxBool.validate();
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
        ConfigValues.TestListMaxBool.setBooleanList(new boolean[] { true, false, true });
        ConfigValues.TestListMaxBool.validate();
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
        ConfigValues.TestListMinByte.validate();
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
        ConfigValues.TestListMinByte.setByteList(new byte[] { (byte) 1, (byte) 2 });
        ConfigValues.TestListMinByte.validate();
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
        ConfigValues.TestListMinByte.setByteList(new byte[] { (byte) 1 });
        ConfigValues.TestListMinByte.validate();
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
        ConfigValues.TestListMaxByte.validate();
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
        ConfigValues.TestListMaxByte.setByteList(new byte[] { (byte) 1, (byte) 2 });
        ConfigValues.TestListMaxByte.validate();
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
        ConfigValues.TestListMaxByte.setByteList(new byte[] { (byte) 1, (byte) 2, (byte) 3 });
        ConfigValues.TestListMaxByte.validate();
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
        ConfigValues.TestListMinChar.setCharacterList(new char[] { 1, 2 });
        ConfigValues.TestListMinChar.validate();
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
        ConfigValues.TestListMinChar.setCharacterList(new char[] { 1 });
        ConfigValues.TestListMinChar.validate();
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
        ConfigValues.TestListMaxChar.validate();
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
        ConfigValues.TestListMaxChar.setCharacterList(new char[] { 1, 2 });
        ConfigValues.TestListMaxChar.validate();
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
        ConfigValues.TestListMaxChar.setCharacterList(new char[] { 1, 2, 3 });
        ConfigValues.TestListMaxChar.validate();
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
        ConfigValues.TestListMinString.setStringList(new String[] { "1", "2" }); //$NON-NLS-1$ //$NON-NLS-2$
        ConfigValues.TestListMinString.validate();
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
        ConfigValues.TestListMinString.setStringList(new String[] { "1" }); //$NON-NLS-1$
        ConfigValues.TestListMinString.validate();
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
        ConfigValues.TestListMaxString.validate();
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
        ConfigValues.TestListMaxString.setStringList(new String[] { "1", "2" }); //$NON-NLS-1$ //$NON-NLS-2$
        ConfigValues.TestListMaxString.validate();
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
        ConfigValues.TestListMaxString.setStringList(new String[] { "1", "2", "3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ConfigValues.TestListMaxString.validate();
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
        ConfigValues.TestListMinColor.setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3), new ConfigColorData(2, 3, 4) });
        ConfigValues.TestListMinColor.validate();
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
        ConfigValues.TestListMinColor.setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3) });
        ConfigValues.TestListMinColor.validate();
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
        ConfigValues.TestListMaxColor.validate();
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
        ConfigValues.TestListMaxColor.setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3), new ConfigColorData(2, 3, 4) });
        ConfigValues.TestListMaxColor.validate();
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
        ConfigValues.TestListMaxColor.setColorList(new ConfigColorData[] { new ConfigColorData(1, 2, 3), new ConfigColorData(2, 3, 4), new ConfigColorData(3, 4, 5) });
        ConfigValues.TestListMaxColor.validate();
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
        ConfigValues.TestListMinVector.setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3), new ConfigVectorData(2, 3, 4) });
        ConfigValues.TestListMinVector.validate();
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
        ConfigValues.TestListMinVector.setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3) });
        ConfigValues.TestListMinVector.validate();
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
        ConfigValues.TestListMaxVector.validate();
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
        ConfigValues.TestListMaxVector.setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3), new ConfigVectorData(2, 3, 4) });
        ConfigValues.TestListMaxVector.validate();
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
        ConfigValues.TestListMaxVector.setVectorList(new ConfigVectorData[] { new ConfigVectorData(1, 2, 3), new ConfigVectorData(2, 3, 4), new ConfigVectorData(3, 4, 5) });
        ConfigValues.TestListMaxVector.validate();
    }
    
    // etc
    
    /**
     * test constructors for code coverage.
     */
    @SuppressWarnings("unused")
    @Test
    public void testConstructors()
    {
        new ValidateIsset.ValidatorInstance();
        new ValidateLMax.ValidatorInstance();
        new ValidateLMin.ValidatorInstance();
        new ValidateListMax.ValidatorInstance();
        new ValidateListMin.ValidatorInstance();
        new ValidateFMax.ValidatorInstance();
        new ValidateFMin.ValidatorInstance();
        new ValidateStrMax.ValidatorInstance();
        new ValidateStrMin.ValidatorInstance();
    }
    
    /**
     * test invalid config.
     * 
     * @throws McException
     *             thrown for problems.
     */
    @Test
    public void testInvalid() throws McException
    {
        new InvalidConfig2().validate();
    }
    
    /**
     * test no validation value.
     * 
     * @throws McException
     *             thrown for problems.
     */
    @Test
    public void testNoValidation() throws McException
    {
        ConfigValues.NoValidation.validate();
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
        ConfigValues.CustomValidation.setBoolean(true);
        ConfigValues.CustomValidation.validate();
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
        ConfigValues.CustomValidation.setBoolean(false);
        ConfigValues.CustomValidation.validate();
    }
    
    /**
     * helper enum.
     */
    @ConfigurationValues(path = "config")
    private enum ConfigValues implements ConfigurationValueInterface
    {
        /** dummy value. */
        @ValidateIsset
        @ConfigurationBool
        TestIssetBool,
        
        /** dummy value. */
        @ValidateIsset
        @ConfigurationInt
        TestIssetInt,
        
        /** dummy value. */
        @ValidateFMax(-42)
        @ConfigurationFloat
        TestFMaxFloat,
        
        /** dummy value. */
        @ValidateFMin(13)
        @ConfigurationFloat
        TestFMinFloat,
        
        /** dummy value. */
        @ValidateFMax(-42)
        @ConfigurationDouble
        TestFMaxDouble,
        
        /** dummy value. */
        @ValidateFMin(13)
        @ConfigurationDouble
        TestFMinDouble,
        
        /** dummy value. */
        @ValidateFMax(-42)
        @ConfigurationFloatList
        TestFMaxFloatList,
        
        /** dummy value. */
        @ValidateFMin(13)
        @ConfigurationFloatList
        TestFMinFloatList,
        
        /** dummy value. */
        @ValidateFMax(-42)
        @ConfigurationDoubleList
        TestFMaxDoubleList,
        
        /** dummy value. */
        @ValidateFMin(13)
        @ConfigurationDoubleList
        TestFMinDoubleList,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationInt
        TestLMaxInt,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationInt
        TestLMinInt,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationByte
        TestLMaxByte,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationByte
        TestLMinByte,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationShort
        TestLMaxShort,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationShort
        TestLMinShort,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationLong
        TestLMaxLong,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationLong
        TestLMinLong,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationIntList
        TestLMaxIntList,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationIntList
        TestLMinIntList,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationByteList
        TestLMaxByteList,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationByteList
        TestLMinByteList,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationShortList
        TestLMaxShortList,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationShortList
        TestLMinShortList,
        
        /** dummy value. */
        @ValidateLMax(-42)
        @ConfigurationLongList
        TestLMaxLongList,
        
        /** dummy value. */
        @ValidateLMin(13)
        @ConfigurationLongList
        TestLMinLongList,
        
        /** dummy value. */
        @ValidateStrMin(5)
        @ConfigurationString
        TestStrMin,
        
        /** dummy value. */
        @ValidateStrMax(7)
        @ConfigurationString
        TestStrMax,
        
        /** dummy value. */
        @ValidateStrMin(5)
        @ConfigurationStringList
        TestStrMinList,
        
        /** dummy value. */
        @ValidateStrMax(7)
        @ConfigurationStringList
        TestStrMaxList,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationBoolList
        TestListMinBool,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationByteList
        TestListMinByte,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationCharacterList
        TestListMinChar,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationColorList
        TestListMinColor,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationDoubleList
        TestListMinDouble,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationFloatList
        TestListMinFloat,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationIntList
        TestListMinInt,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationItemStackList
        TestListMinItemStack,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationLongList
        TestListMinLong,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationPlayerList
        TestListMinPlayer,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationShortList
        TestListMinShort,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationStringList
        TestListMinString,
        
        /** dummy value. */
        @ValidateListMin(2)
        @ConfigurationVectorList
        TestListMinVector,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationBoolList
        TestListMaxBool,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationByteList
        TestListMaxByte,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationCharacterList
        TestListMaxChar,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationColorList
        TestListMaxColor,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationDoubleList
        TestListMaxDouble,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationFloatList
        TestListMaxFloat,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationIntList
        TestListMaxInt,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationItemStackList
        TestListMaxItemStack,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationLongList
        TestListMaxLong,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationPlayerList
        TestListMaxPlayer,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationShortList
        TestListMaxShort,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationStringList
        TestListMaxString,
        
        /** dummy value. */
        @ValidateListMax(2)
        @ConfigurationVectorList
        TestListMaxVector,
        
        /** no validation. */
        @ConfigurationBool
        NoValidation,
        
        /** custom validation. */
        @ConfigurationBool
        @Validator(BoolValidate.class)
        CustomValidation,
        
    }
    
    /**
     * custom validation.
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
    
    /**
     * Some invalid config.
     * 
     * @author mepeisen
     */
    public static final class InvalidConfig2 implements ConfigurationValueInterface
    {
        
        @Override
        public String name()
        {
            throw new IllegalStateException();
        }
        
    }
    
}
