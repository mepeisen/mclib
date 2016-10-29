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
import de.minigameslib.mclib.api.util.function.McBinaryOperator;

/**
 * Tests case for {@link McBinaryOperator}
 * 
 * @author mepeisen
 */
public class McBinaryOperatorTest
{
    
    /**
     * Tests method {@link McBinaryOperator#minBy(de.minigameslib.mclib.api.util.function.McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testMinBy() throws McException
    {
        final McBinaryOperator<Integer> func = McBinaryOperator.minBy(Integer::compareTo);
        assertEquals(Integer.valueOf(10), func.apply(Integer.valueOf(10), Integer.valueOf(20)));
        assertEquals(Integer.valueOf(10), func.apply(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(Integer.valueOf(10), func.apply(Integer.valueOf(10), Integer.valueOf(10)));
    }
    
    /**
     * Tests method {@link McBinaryOperator#maxBy(de.minigameslib.mclib.api.util.function.McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testMaxBy() throws McException
    {
        final McBinaryOperator<Integer> func = McBinaryOperator.maxBy(Integer::compareTo);
        assertEquals(Integer.valueOf(20), func.apply(Integer.valueOf(10), Integer.valueOf(20)));
        assertEquals(Integer.valueOf(20), func.apply(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(Integer.valueOf(20), func.apply(Integer.valueOf(20), Integer.valueOf(20)));
    }
    
}
