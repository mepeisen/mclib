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

package de.minigameslib.mclib.test.util.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.FalseStub;

/**
 * Tests {@link FalseStub}.
 * 
 * @author mepeisen
 *
 */
public class FalseStubTest
{
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test
    public void testThen() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub.then(b -> b.set(true));
        assertFalse(bool.get());
    }
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test
    public void testThenThrow1() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub.thenThrow(b -> new McException(CommonMessages.InternalError));
    }
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test
    public void testThenThrow2() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub.thenThrow(CommonMessages.InternalError);
    }
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test
    public void testThenThrow3() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub.thenThrow(CommonMessages.InternalError, b -> new Serializable[]{});
    }
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test
    public void testElse() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub._else(b -> b.set(true));
        assertTrue(bool.get());
    }
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test(expected = McException.class)
    public void testElseThrow1() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub._elseThrow(b -> new McException(CommonMessages.InternalError));
    }
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test(expected = McException.class)
    public void testElseThrow2() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub._elseThrow(CommonMessages.InternalError);
    }
    
    /**
     * simple test.
     * @throws McException thrown on errors.
     */
    @Test(expected = McException.class)
    public void testElseThrow3() throws McException
    {
        final AtomicBoolean bool = new AtomicBoolean(false);
        final FalseStub<AtomicBoolean> stub = new FalseStub<>(bool);
        stub._elseThrow(CommonMessages.InternalError, b -> new Serializable[]{});
    }
    
}
