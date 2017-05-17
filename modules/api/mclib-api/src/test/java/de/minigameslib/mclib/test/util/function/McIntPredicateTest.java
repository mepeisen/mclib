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

import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McIntPredicate;

/**
 * Tests case for {@link McIntPredicate}.
 * 
 * @author mepeisen
 */
public class McIntPredicateTest
{
    
    /**
     * Tests method {@link McIntPredicate#and(McIntPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testAnd() throws McException
    {
        final McIntPredicate func = (l) -> l > 10;
        final McIntPredicate func2 = (l) -> l > 20;
        
        assertFalse(func.and(func2).test(15));
        assertFalse(func2.and(func).test(15));
        assertTrue(func.and(func2).test(25));
    }
    
    /**
     * Tests method {@link McIntPredicate#or(McIntPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testOr() throws McException
    {
        final McIntPredicate func = (l) -> l > 10;
        final McIntPredicate func2 = (l) -> l < -10;
        
        assertTrue(func.or(func2).test(15));
        assertTrue(func.or(func2).test(-15));
        assertFalse(func.or(func2).test(5));
        assertFalse(func.or(func2).test(-5));
    }
    
    /**
     * Tests method {@link McIntPredicate#negate()}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNegate() throws McException
    {
        final McIntPredicate func = (l) -> l > 10;
        
        assertFalse(func.negate().test(15));
        assertTrue(func.negate().test(5));
    }
    
}
