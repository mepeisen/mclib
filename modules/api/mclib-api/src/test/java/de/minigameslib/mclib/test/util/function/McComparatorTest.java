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
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McComparator;
import de.minigameslib.mclib.spigottest.CommonTestUtil;

import com.google.common.util.concurrent.AtomicDouble;

/**
 * Tests case for {@link McComparator}
 * 
 * @author mepeisen
 */
public class McComparatorTest
{
    
    /**
     * Tests method {@link McComparator#reversed()}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testReversed() throws McException
    {
        final McComparator<Integer> func = Integer::compareTo;
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.reversed().reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link McComparator#reverseOrder()}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testReverseOrder() throws McException
    {
        final McComparator<Integer> func = McComparator.reverseOrder();
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link McComparator#naturalOrder()}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNaturalOrder() throws McException
    {
        final McComparator<Integer> func = McComparator.naturalOrder();
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link McComparator#nullsFirst(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst() throws McException
    {
        final McComparator<Integer> func = McComparator.nullsFirst(McComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#nullsLast(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast() throws McException
    {
        final McComparator<Integer> func = McComparator.nullsLast(McComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#nullsFirst(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst2() throws McException
    {
        final McComparator<Integer> func = McComparator.nullsFirst(null);
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#nullsLast(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast2() throws McException
    {
        final McComparator<Integer> func = McComparator.nullsLast(null);
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#nullsFirst(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst3() throws McException
    {
        McComparator<Integer> func = McComparator.nullsFirst(McComparator.naturalOrder());
        func = func.thenComparing(McComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#nullsLast(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast3() throws McException
    {
        McComparator<Integer> func = McComparator.nullsLast(McComparator.naturalOrder());
        func = func.thenComparing(McComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(20), Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(20)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#nullsFirst(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsFirst4() throws McException
    {
        McComparator<Integer> func = McComparator.nullsFirst(null);
        func = func.thenComparing(McComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(-1, func.compare(null, Integer.valueOf(10)));
        assertEquals(1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#nullsLast(McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testNullsLast4() throws McException
    {
        McComparator<Integer> func = McComparator.nullsLast(null);
        func = func.thenComparing(McComparator.naturalOrder());
        
        assertEquals(0, func.compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.reversed().compare(Integer.valueOf(10), Integer.valueOf(10)));
        
        assertEquals(0, func.compare(null, null));
        assertEquals(1, func.compare(null, Integer.valueOf(10)));
        assertEquals(-1, func.compare(Integer.valueOf(10), null));
        
        assertEquals(0, func.reversed().compare(null, null));
        assertEquals(-1, func.reversed().compare(null, Integer.valueOf(10)));
        assertEquals(1, func.reversed().compare(Integer.valueOf(10), null));
    }
    
    /**
     * Tests method {@link McComparator#comparingLong(de.minigameslib.mclib.api.util.function.McToLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testComparingLong() throws McException
    {
        final McComparator<AtomicLong> func = McComparator.comparingLong((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicLong(10), new AtomicLong(10)));
        assertEquals(1, func.compare(new AtomicLong(20), new AtomicLong(10)));
        assertEquals(-1, func.compare(new AtomicLong(10), new AtomicLong(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicLong(10), new AtomicLong(10)));
        assertEquals(-1, func.reversed().compare(new AtomicLong(20), new AtomicLong(10)));
        assertEquals(1, func.reversed().compare(new AtomicLong(10), new AtomicLong(20)));
    }
    
    /**
     * Tests method {@link McComparator#comparingInt(de.minigameslib.mclib.api.util.function.McToIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testComparingInt() throws McException
    {
        final McComparator<AtomicInteger> func = McComparator.comparingInt((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicInteger(10), new AtomicInteger(10)));
        assertEquals(1, func.compare(new AtomicInteger(20), new AtomicInteger(10)));
        assertEquals(-1, func.compare(new AtomicInteger(10), new AtomicInteger(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicInteger(10), new AtomicInteger(10)));
        assertEquals(-1, func.reversed().compare(new AtomicInteger(20), new AtomicInteger(10)));
        assertEquals(1, func.reversed().compare(new AtomicInteger(10), new AtomicInteger(20)));
    }
    
    /**
     * Tests method {@link McComparator#comparingDouble(de.minigameslib.mclib.api.util.function.McToDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testComparingDouble() throws McException
    {
        final McComparator<AtomicDouble> func = McComparator.comparingDouble((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(1, func.compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(-1, func.compare(new AtomicDouble(10), new AtomicDouble(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(-1, func.reversed().compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(1, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(20)));
    }
    
    /**
     * Tests method {@link McComparator#comparing(de.minigameslib.mclib.api.util.function.McFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testComparing1() throws McException
    {
        final McComparator<AtomicDouble> func = McComparator.comparing((d) -> d.get());
        
        assertEquals(0, func.compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(1, func.compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(-1, func.compare(new AtomicDouble(10), new AtomicDouble(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(-1, func.reversed().compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(1, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(20)));
    }
    
    /**
     * Tests method {@link McComparator#comparing(de.minigameslib.mclib.api.util.function.McFunction, McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testComparing2() throws McException
    {
        final McComparator<AtomicDouble> func = McComparator.comparing((d) -> d.get(), McComparator.naturalOrder());
        
        assertEquals(0, func.compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(1, func.compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(-1, func.compare(new AtomicDouble(10), new AtomicDouble(20)));
        
        assertEquals(0, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(10)));
        assertEquals(-1, func.reversed().compare(new AtomicDouble(20), new AtomicDouble(10)));
        assertEquals(1, func.reversed().compare(new AtomicDouble(10), new AtomicDouble(20)));
    }
    
    /**
     * Tests method {@link McComparator#thenComparing(de.minigameslib.mclib.api.util.function.McFunction, McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testThenComparing1() throws McException
    {
        final McComparator<FooInt> func = McComparator.comparing((d) -> d.getA());
        final McComparator<FooInt> func2 = func.thenComparing((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func2.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func2.compare(new FooInt(10, 10), new FooInt(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
    }
    
    /**
     * Tests method {@link McComparator#thenComparing(de.minigameslib.mclib.api.util.function.McFunction, McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testThenComparing2() throws McException
    {
        final McComparator<FooInt> func = McComparator.comparing((d) -> d.getA());
        final McComparator<FooInt> func2 = func.thenComparing((d) -> d.getB(), McComparator.naturalOrder());
        
        assertEquals(0, func2.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func2.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func2.compare(new FooInt(10, 10), new FooInt(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
    }
    
    /**
     * Tests method {@link McComparator#thenComparing(de.minigameslib.mclib.api.util.function.McFunction, McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testThenComparing3() throws McException
    {
        final McComparator<FooInt> func = McComparator.comparing((d) -> d.getA());
        final McComparator<FooInt> func2 = (d, e) -> Integer.compare(d.getB(), e.getB());
        final McComparator<FooInt> func3 = func.thenComparing(func2);
        
        assertEquals(0, func3.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func3.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func3.compare(new FooInt(10, 10), new FooInt(10, 20)));
        assertEquals(1, func3.compare(new FooInt(20, 10), new FooInt(10, 10)));
        assertEquals(-1, func3.compare(new FooInt(10, 10), new FooInt(20, 10)));

        assertEquals(0, func3.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func3.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func3.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
        assertEquals(-1, func3.reversed().compare(new FooInt(20, 10), new FooInt(10, 10)));
        assertEquals(1, func3.reversed().compare(new FooInt(10, 10), new FooInt(20, 10)));
    }
    
    /**
     * Tests method {@link McComparator#thenComparingInt(de.minigameslib.mclib.api.util.function.McToIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testThenComparingInt() throws McException
    {
        final McComparator<FooInt> func = McComparator.comparing((d) -> d.getA());
        final McComparator<FooInt> func2 = func.thenComparingInt((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(1, func2.compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(-1, func2.compare(new FooInt(10, 10), new FooInt(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooInt(10, 20), new FooInt(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooInt(10, 10), new FooInt(10, 20)));
    }
    
    /**
     * Tests method {@link McComparator#thenComparingLong(de.minigameslib.mclib.api.util.function.McToLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testThenComparingLong() throws McException
    {
        final McComparator<FooLong> func = McComparator.comparing((d) -> d.getA());
        final McComparator<FooLong> func2 = func.thenComparingLong((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooLong(10, 10), new FooLong(10, 10)));
        assertEquals(1, func2.compare(new FooLong(10, 20), new FooLong(10, 10)));
        assertEquals(-1, func2.compare(new FooLong(10, 10), new FooLong(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooLong(10, 10), new FooLong(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooLong(10, 20), new FooLong(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooLong(10, 10), new FooLong(10, 20)));
    }
    
    /**
     * Tests method {@link McComparator#thenComparingDouble(de.minigameslib.mclib.api.util.function.McToDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testThenComparingDouble() throws McException
    {
        final McComparator<FooDouble> func = McComparator.comparing((d) -> d.getA());
        final McComparator<FooDouble> func2 = func.thenComparingDouble((d) -> d.getB());
        
        assertEquals(0, func2.compare(new FooDouble(10, 10), new FooDouble(10, 10)));
        assertEquals(1, func2.compare(new FooDouble(10, 20), new FooDouble(10, 10)));
        assertEquals(-1, func2.compare(new FooDouble(10, 10), new FooDouble(10, 20)));

        assertEquals(0, func2.reversed().compare(new FooDouble(10, 10), new FooDouble(10, 10)));
        assertEquals(-1, func2.reversed().compare(new FooDouble(10, 20), new FooDouble(10, 10)));
        assertEquals(1, func2.reversed().compare(new FooDouble(10, 10), new FooDouble(10, 20)));
    }
    
    /**
     * Tests the enums
     */
    @Test
    public void enumTest()
    {
        CommonTestUtil.testEnumClass(McComparator.ReverseComparator.class);
        CommonTestUtil.testEnumClass(McComparator.NaturalOrderComparator.class);
    }
    
    /**
     * Helper class.
     */
    private static final class FooInt
    {
        /** a value. */
        private final int a;
        /** a value. */
        private final int b;
        
        /**
         * Constructor
         * @param a
         * @param b
         */
        public FooInt(int a, int b)
        {
            this.a = a;
            this.b = b;
        }

        /**
         * @return the a
         */
        public int getA()
        {
            return this.a;
        }

        /**
         * @return the b
         */
        public int getB()
        {
            return this.b;
        }
    }
    
    /**
     * Helper class.
     */
    private static final class FooLong
    {
        /** a value. */
        private final long a;
        /** a value. */
        private final long b;
        
        /**
         * Constructor
         * @param a
         * @param b
         */
        public FooLong(long a, long b)
        {
            this.a = a;
            this.b = b;
        }

        /**
         * @return the a
         */
        public long getA()
        {
            return this.a;
        }

        /**
         * @return the b
         */
        public long getB()
        {
            return this.b;
        }
    }
    
    /**
     * Helper class.
     */
    private static final class FooDouble
    {
        /** a value. */
        private final double a;
        /** a value. */
        private final double b;
        
        /**
         * Constructor
         * @param a
         * @param b
         */
        public FooDouble(double a, double b)
        {
            this.a = a;
            this.b = b;
        }

        /**
         * @return the a
         */
        public double getA()
        {
            return this.a;
        }

        /**
         * @return the b
         */
        public double getB()
        {
            return this.b;
        }
    }
    
}
