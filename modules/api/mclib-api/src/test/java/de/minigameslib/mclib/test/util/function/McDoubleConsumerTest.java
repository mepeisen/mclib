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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McDoubleConsumer;
import com.google.common.util.concurrent.AtomicDouble;

/**
 * Tests case for {@link McDoubleConsumer}
 * 
 * @author mepeisen
 */
public class McDoubleConsumerTest
{
    
    /**
     * Tests method {@link McDoubleConsumer#andThen(McDoubleConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws McException
    {
        final AtomicDouble result1 = new AtomicDouble(0);
        final AtomicDouble result2 = new AtomicDouble(0);
        final McDoubleConsumer func = (l) -> result1.set(l);
        final McDoubleConsumer func2 = (l) -> result2.set(l + result1.get());
        
        func.andThen(func2).accept(5);
        
        assertEquals(5, result1.get(), 0);
        assertEquals(10, result2.get(), 0);
    }
    
}
