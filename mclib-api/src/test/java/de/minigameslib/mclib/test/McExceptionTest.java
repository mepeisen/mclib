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

package de.minigameslib.mclib.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.Serializable;

import javax.naming.NoPermissionException;

import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * Tests {@link McException}.
 * 
 * @author mepeisen
 *
 */
public class McExceptionTest
{
    
    /**
     * tests constructors.
     */
    @Test
    public void testStandardConstructor()
    {
        McException exc;
        
        // no args
        exc = new McException(Messages.FOO);
        assertSame(Messages.FOO, exc.getErrorMessage());
        assertArrayEquals(new Serializable[0], exc.getArgs());
        assertEquals("de.minigameslib.mclib.test.McExceptionTest$Messages.FOO, args: []", exc.getMessage()); //$NON-NLS-1$
        
        // null array
        exc = new McException(Messages.FOO, (Serializable[])null);
        assertSame(Messages.FOO, exc.getErrorMessage());
        assertNull(exc.getArgs());
        assertEquals("de.minigameslib.mclib.test.McExceptionTest$Messages.FOO, args: []", exc.getMessage()); //$NON-NLS-1$
        
        // with args
        exc = new McException(Messages.FOO, 123, "BAZ"); //$NON-NLS-1$
        assertSame(Messages.FOO, exc.getErrorMessage());
        assertArrayEquals(new Serializable[]{123, "BAZ"}, exc.getArgs()); //$NON-NLS-1$
        assertEquals("de.minigameslib.mclib.test.McExceptionTest$Messages.FOO, args: [\n  123\n  BAZ]", exc.getMessage()); //$NON-NLS-1$
    }
    
    /**
     * tests constructors.
     */
    @Test
    public void testConstructorWithCause()
    {
        McException exc;
        
        // no args
        exc = new McException(Messages.FOO, new NoPermissionException());
        assertSame(Messages.FOO, exc.getErrorMessage());
        assertArrayEquals(new Serializable[0], exc.getArgs());
        assertEquals("de.minigameslib.mclib.test.McExceptionTest$Messages.FOO, args: []", exc.getMessage()); //$NON-NLS-1$
        
        // null array
        exc = new McException(Messages.FOO, new NoPermissionException(), (Serializable[])null);
        assertSame(Messages.FOO, exc.getErrorMessage());
        assertNull(exc.getArgs());
        assertEquals("de.minigameslib.mclib.test.McExceptionTest$Messages.FOO, args: []", exc.getMessage()); //$NON-NLS-1$
        
        // with args
        exc = new McException(Messages.FOO, new NoPermissionException(), 123, "BAZ"); //$NON-NLS-1$
        assertSame(Messages.FOO, exc.getErrorMessage());
        assertArrayEquals(new Serializable[]{123, "BAZ"}, exc.getArgs()); //$NON-NLS-1$
        assertEquals("de.minigameslib.mclib.test.McExceptionTest$Messages.FOO, args: [\n  123\n  BAZ]", exc.getMessage()); //$NON-NLS-1$
    }
    
    /**
     * dummy messages.
     */
    private enum Messages implements LocalizedMessageInterface
    {
        /** foo string. */
        FOO;
    }
    
}
