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

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McIntUnaryOperator;

/**
 * Tests case for {@link McIntUnaryOperator}.
 * 
 * @author mepeisen
 */
public class McIntUnaryOperatorTest
{
    
    /**
     * Tests method {@link McIntUnaryOperator#compose(McIntUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testCompose() throws McException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final McIntUnaryOperator func = (l) -> l * 2;
        final McIntUnaryOperator func2 = (l) ->
        {
            result1.set(l);
            return l * 3;
        };
        
        assertEquals(24, func.compose(func2).applyAsInt(4));
        assertEquals(4, result1.get());
    }
    
    /**
     * Tests method {@link McIntUnaryOperator#andThen(McIntUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws McException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final McIntUnaryOperator func = (l) -> l * 2;
        final McIntUnaryOperator func2 = (l) ->
        {
            result1.set(l);
            return l * 3;
        };
        
        assertEquals(24, func.andThen(func2).applyAsInt(4));
        assertEquals(8, result1.get());
    }
    
    /**
     * Tests method {@link McIntUnaryOperator#identity()}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws McException
    {
        final McIntUnaryOperator func = McIntUnaryOperator.identity();
        assertEquals(10, func.applyAsInt(10));
    }
    
}
