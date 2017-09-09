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
import de.minigameslib.mclib.api.util.function.McFunction;

/**
 * Tests case for {@link McFunction}.
 * 
 * @author mepeisen
 */
public class McFunctionTest
{
    
    /**
     * Tests method {@link McFunction#andThen(de.minigameslib.mclib.api.util.function.McFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws McException
    {
        final McFunction<String, Integer> func = Integer::valueOf;
        final McFunction<String, String> func2 = (a1) -> a1.concat("0"); //$NON-NLS-1$
        
        final McFunction<String, Integer> func3 = func2.andThen(func);
        
        assertEquals(10, func3.apply("1").intValue()); //$NON-NLS-1$
    }
    
    /**
     * Tests method {@link McFunction#compose(McFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testCompose() throws McException
    {
        final McFunction<String, Integer> func = Integer::valueOf;
        final McFunction<String, String> func2 = (a1) -> a1.concat("0"); //$NON-NLS-1$
        
        final McFunction<String, Integer> func3 = func.compose(func2);
        
        assertEquals(10, func3.apply("1").intValue()); //$NON-NLS-1$
    }
    
    /**
     * Tests method {@link McFunction#identity()}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIdentity() throws McException
    {
        final McFunction<Integer, Integer> func = McFunction.identity();
        assertEquals(10, func.apply(10).intValue());
    }
    
}
