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
import de.minigameslib.mclib.api.util.function.McBiFunction;
import de.minigameslib.mclib.api.util.function.McFunction;

/**
 * Tests case for {@link McBiFunction}.
 * 
 * @author mepeisen
 */
public class McBiFunctionTest
{
    
    /**
     * Tests method {@link McBiFunction#andThen(de.minigameslib.mclib.api.util.function.McFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAndThen() throws McException
    {
        final McFunction<String, Integer> func = Integer::valueOf;
        final McBiFunction<String, String, String> biFunc = (a1, a2) -> a1.concat(a2);
        
        final McBiFunction<String, String, Integer> biFunc2 = biFunc.andThen(func);
        
        assertEquals(10, biFunc2.apply("1", "0").intValue()); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
