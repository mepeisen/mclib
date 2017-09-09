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
import de.minigameslib.mclib.api.util.function.McIntConsumer;

/**
 * Tests case for {@link McIntConsumer}.
 * 
 * @author mepeisen
 */
public class McIntConsumerTest
{
    
    /**
     * Tests method {@link McIntConsumer#andThen(McIntConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws McException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final AtomicInteger result2 = new AtomicInteger(0);
        final McIntConsumer func = (l) -> result1.set(l);
        final McIntConsumer func2 = (l) -> result2.set(l + result1.get());
        
        func.andThen(func2).accept(5);
        
        assertEquals(5, result1.get());
        assertEquals(10, result2.get());
    }
    
}
