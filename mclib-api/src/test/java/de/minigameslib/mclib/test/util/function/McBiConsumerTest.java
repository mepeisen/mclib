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
import de.minigameslib.mclib.api.util.function.McBiConsumer;

/**
 * Tests case for {@link McBiConsumer}.
 * 
 * @author mepeisen
 */
public class McBiConsumerTest
{
    
    /**
     * Tests method {@link McBiConsumer#andThen(McBiConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws McException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final AtomicInteger result2 = new AtomicInteger(0);
        final AtomicInteger result3 = new AtomicInteger(0);
        final AtomicInteger result4 = new AtomicInteger(0);
        final McBiConsumer<Integer, Integer> func = (i, j) ->
        {
            result1.set(i);
            result2.set(j + 10);
        };
        final McBiConsumer<Integer, Integer> func2 = (i, j) ->
        {
            result3.set(i + result1.get());
            result4.set(j + result2.get());
        };
        
        func.andThen(func2).accept(5, 7);
        
        assertEquals(5, result1.get());
        assertEquals(17, result2.get());
        assertEquals(10, result3.get());
        assertEquals(24, result4.get());
    }
    
}
