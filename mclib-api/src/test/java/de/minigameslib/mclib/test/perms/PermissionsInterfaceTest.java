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

package de.minigameslib.mclib.test.perms;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.minigameslib.mclib.api.perms.Permission;
import de.minigameslib.mclib.api.perms.Permissions;
import de.minigameslib.mclib.api.perms.PermissionsInterface;

/**
 * test case for {@link PermissionsInterface}.
 * 
 * @author mepeisen
 */
public class PermissionsInterfaceTest
{
    
    /**
     * Tests {@link PermissionsInterface#fullPath()}.
     */
    @Test
    public void fullPathTest()
    {
        assertEquals("FOO.BAR", PermissionEnum.BAR.fullPath()); //$NON-NLS-1$
        assertEquals("FOO.BAZZER", PermissionEnum.BAZ.fullPath()); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link PermissionsInterface#fullPath()}.
     */
    @Test(expected = IllegalStateException.class)
    public void fullPathTestInvalid1()
    {
        assertEquals("FOO.BAR", InvalidEnum1.BAR.fullPath()); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link PermissionsInterface#fullPath()}.
     */
    @Test(expected = IllegalStateException.class)
    public void fullPathTestInvalid2()
    {
        assertEquals("FOO.BAR", InvalidEnum2.BAR.fullPath()); //$NON-NLS-1$
    }
    
    /**
     * Tests {@link PermissionsInterface#fullPath()}.
     */
    @SuppressWarnings("synthetic-access")
    @Test(expected = IllegalStateException.class)
    public void testInvalid3()
    {
        assertEquals("FOO.BAR", new InvalidEnum3().fullPath()); //$NON-NLS-1$
    }
    
    /**
     * some sample permission.
     */
    @Permissions(value = "FOO")
    private static enum PermissionEnum implements PermissionsInterface
    {
        /** bar permission. */
        @Permission
        BAR,
        /** bazzer permission. */
        @Permission("BAZZER")
        BAZ
    }
    
    /**
     * some sample permission.
     */
    private static enum InvalidEnum1 implements PermissionsInterface
    {
        /** bar permission. */
        @Permission
        BAR
    }
    
    /**
     * some sample permission.
     */
    @Permissions(value = "FOO")
    private static enum InvalidEnum2 implements PermissionsInterface
    {
        /** bar permission. */
        BAR
    }
    
    /**
     * some sample permission.
     */
    @Permissions(value = "FOO")
    private static class InvalidEnum3 implements PermissionsInterface
    {

        @Override
        public String name()
        {
            return "BAR"; //$NON-NLS-1$
        }
        
    }
    
}
