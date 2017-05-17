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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.util.concurrent.AtomicDouble;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McDoubleUnaryOperator;

/**
 * Tests case for {@link McDoubleUnaryOperator}.
 * 
 * @author mepeisen
 */
public class McDoubleUnaryOperatorTest
{
    
    /**
     * Tests method {@link McDoubleUnaryOperator#compose(McDoubleUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testCompose() throws McException
    {
        final AtomicDouble result1 = new AtomicDouble(0);
        final McDoubleUnaryOperator func = (l) -> l * 2;
        final McDoubleUnaryOperator func2 = (l) ->
        {
            result1.set(l);
            return l * 3;
        };
        
        assertEquals(24, func.compose(func2).applyAsDouble(4), 0);
        assertEquals(4, result1.get(), 0);
    }
    
    /**
     * Tests method {@link McDoubleUnaryOperator#andThen(McDoubleUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws McException
    {
        final AtomicDouble result1 = new AtomicDouble(0);
        final McDoubleUnaryOperator func = (l) -> l * 2;
        final McDoubleUnaryOperator func2 = (l) ->
        {
            result1.set(l);
            return l * 3;
        };
        
        assertEquals(24, func.andThen(func2).applyAsDouble(4), 0);
        assertEquals(8, result1.get(), 0);
    }
    
    /**
     * Tests method {@link McDoubleUnaryOperator#identity()}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws McException
    {
        final McDoubleUnaryOperator func = McDoubleUnaryOperator.identity();
        assertEquals(10, func.applyAsDouble(10), 0);
    }
    
}
