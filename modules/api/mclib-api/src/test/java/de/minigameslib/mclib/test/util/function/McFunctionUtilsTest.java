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
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.google.common.util.concurrent.AtomicDouble;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mclib.api.util.function.McBiFunction;
import de.minigameslib.mclib.api.util.function.McBiPredicate;
import de.minigameslib.mclib.api.util.function.McBinaryOperator;
import de.minigameslib.mclib.api.util.function.McBooleanSupplier;
import de.minigameslib.mclib.api.util.function.McComparator;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McDoubleBinaryOperator;
import de.minigameslib.mclib.api.util.function.McDoubleConsumer;
import de.minigameslib.mclib.api.util.function.McDoubleFunction;
import de.minigameslib.mclib.api.util.function.McDoublePredicate;
import de.minigameslib.mclib.api.util.function.McDoubleSupplier;
import de.minigameslib.mclib.api.util.function.McDoubleToIntFunction;
import de.minigameslib.mclib.api.util.function.McDoubleToLongFunction;
import de.minigameslib.mclib.api.util.function.McDoubleUnaryOperator;
import de.minigameslib.mclib.api.util.function.McFunction;
import de.minigameslib.mclib.api.util.function.McFunctionUtils;
import de.minigameslib.mclib.api.util.function.McIntBinaryOperator;
import de.minigameslib.mclib.api.util.function.McIntConsumer;
import de.minigameslib.mclib.api.util.function.McIntFunction;
import de.minigameslib.mclib.api.util.function.McIntPredicate;
import de.minigameslib.mclib.api.util.function.McIntSupplier;
import de.minigameslib.mclib.api.util.function.McIntToDoubleFunction;
import de.minigameslib.mclib.api.util.function.McIntToLongFunction;
import de.minigameslib.mclib.api.util.function.McIntUnaryOperator;
import de.minigameslib.mclib.api.util.function.McLongBinaryOperator;
import de.minigameslib.mclib.api.util.function.McLongConsumer;
import de.minigameslib.mclib.api.util.function.McLongFunction;
import de.minigameslib.mclib.api.util.function.McLongPredicate;
import de.minigameslib.mclib.api.util.function.McLongSupplier;
import de.minigameslib.mclib.api.util.function.McLongToDoubleFunction;
import de.minigameslib.mclib.api.util.function.McLongToIntFunction;
import de.minigameslib.mclib.api.util.function.McLongUnaryOperator;
import de.minigameslib.mclib.api.util.function.McObjDoubleConsumer;
import de.minigameslib.mclib.api.util.function.McObjIntConsumer;
import de.minigameslib.mclib.api.util.function.McObjLongConsumer;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.api.util.function.McToDoubleBiFunction;
import de.minigameslib.mclib.api.util.function.McToDoubleFunction;
import de.minigameslib.mclib.api.util.function.McToIntBiFunction;
import de.minigameslib.mclib.api.util.function.McToIntFunction;
import de.minigameslib.mclib.api.util.function.McToLongBiFunction;
import de.minigameslib.mclib.api.util.function.McToLongFunction;
import de.minigameslib.mclib.api.util.function.McUnaryOperator;

/**
 * Tests case for {@link McFunctionUtils}.
 * 
 * @author mepeisen
 */
public class McFunctionUtilsTest
{
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBiConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testBiConsumerOk() throws McException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final AtomicInteger result2 = new AtomicInteger(0);
        final McBiConsumer<Integer, Integer> func = (i, j) ->
        {
            result1.set(i);
            result2.set(j + 10);
        };
        
        McFunctionUtils.wrap(func).accept(5, 7);
        
        assertEquals(5, result1.get());
        assertEquals(17, result2.get());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBiConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testBiConsumerFailed() throws McException
    {
        final McBiConsumer<Integer, Integer> func = (i, j) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(5, 7);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBiFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testBiFunctionOk() throws McException
    {
        final McBiFunction<String, String, String> func = (a1, a2) -> a1.concat(a2);
        
        assertEquals("10", McFunctionUtils.wrap(func).apply("1", "0")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBiFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testBiFunctionFailed() throws McException
    {
        final McBiFunction<String, String, String> func = (a1, a2) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).apply("1", "0"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testBinaryOperatorOk() throws McException
    {
        final McBinaryOperator<Integer> func = McBinaryOperator.minBy(Integer::compareTo);
        
        assertEquals(Integer.valueOf(10), McFunctionUtils.wrap(func).apply(Integer.valueOf(10), Integer.valueOf(20)));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testBinaryOperatorFailed() throws McException
    {
        final McBinaryOperator<Integer> func = (a, b) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).apply(Integer.valueOf(10), Integer.valueOf(20));
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBiPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testBiPredicateOk() throws McException
    {
        final McBiPredicate<Integer, Integer> func = (i, j) -> i > 10 && j > 10;
        
        assertTrue(McFunctionUtils.wrap(func).test(15, 15));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBiPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testBiPredicateFailed() throws McException
    {
        final McBiPredicate<Integer, Integer> func = (a, b) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).test(15, 15);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBooleanSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testBooleanSupplierOk() throws McException
    {
        final McBooleanSupplier func = () -> true;
        
        assertTrue(McFunctionUtils.wrap(func).getAsBoolean());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McBooleanSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testBooleanSupplierFailed() throws McException
    {
        final McBooleanSupplier func = () ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).getAsBoolean();
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testComparatorOk() throws McException
    {
        final McComparator<Integer> func = Integer::compareTo;
        
        assertEquals(0, McFunctionUtils.wrap(func).compare(Integer.valueOf(10), Integer.valueOf(10)));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McComparator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testComparatorFailed() throws McException
    {
        final McComparator<Integer> func = (a, b) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).compare(Integer.valueOf(10), Integer.valueOf(10));
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testConsumerOk() throws McException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final McConsumer<Integer> func = (l) -> result1.set(l);
        
        McFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testConsumerFailed() throws McException
    {
        final McConsumer<Integer> func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoubleConsumerOk() throws McException
    {
        final AtomicDouble result1 = new AtomicDouble(0);
        final McDoubleConsumer func = (l) -> result1.set(l);
        
        McFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get(), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoubleConsumerFailed() throws McException
    {
        final McDoubleConsumer func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntConsumerOk() throws McException
    {
        final AtomicInteger result1 = new AtomicInteger(0);
        final McIntConsumer func = (l) -> result1.set(l);
        
        McFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntConsumerFailed() throws McException
    {
        final McIntConsumer func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongConsumerOk() throws McException
    {
        final AtomicLong result1 = new AtomicLong(0);
        final McLongConsumer func = (l) -> result1.set(l);
        
        McFunctionUtils.wrap(func).accept(10);
        assertEquals(10, result1.get());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongConsumerFailed() throws McException
    {
        final McLongConsumer func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoubleFunctionOk() throws McException
    {
        final McDoubleFunction<Long> func = (d) -> Math.round(d);
        
        assertEquals(10, McFunctionUtils.wrap(func).apply(9.5d).longValue());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoubleFunctionFailed() throws McException
    {
        final McDoubleFunction<Long> func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).apply(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoubleSupplierOk() throws McException
    {
        final McDoubleSupplier func = () -> 9.5d;
        
        assertEquals(9.5d, McFunctionUtils.wrap(func).getAsDouble(), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoubleSupplierFailed() throws McException
    {
        final McDoubleSupplier func = () ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).getAsDouble();
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoublePredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoublePredicateOk() throws McException
    {
        final McDoublePredicate func = (d) -> d == 9.5d;
        
        assertTrue(McFunctionUtils.wrap(func).test(9.5));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoublePredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoublePredicateFailed() throws McException
    {
        final McDoublePredicate func = (d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).test(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleToIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoubleToIntFunctionOk() throws McException
    {
        final McDoubleToIntFunction func = (d) -> (int) Math.round(d);
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsInt(9.5d));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleToIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoubleToIntFunctionFailed() throws McException
    {
        final McDoubleToIntFunction func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsInt(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleToLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoubleToLongFunctionOk() throws McException
    {
        final McDoubleToLongFunction func = (d) -> Math.round(d);
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsLong(9.5d));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleToLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoubleToLongFunctionFailed() throws McException
    {
        final McDoubleToLongFunction func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsLong(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoubleUnaryOperatorOk() throws McException
    {
        final McDoubleUnaryOperator func = (d) -> Math.round(d);
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsDouble(9.5d), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoubleUnaryOperatorFailed() throws McException
    {
        final McDoubleUnaryOperator func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsDouble(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testDoubleBinaryOperatorOk() throws McException
    {
        final McDoubleBinaryOperator func = (d, e) -> d + e;
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testDoubleBinaryOperatorFailed() throws McException
    {
        final McDoubleBinaryOperator func = (a, b) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testFunctionOk() throws McException
    {
        final McFunction<Double, Double> func = (d) -> (double) Math.round(d);
        
        assertEquals(10, McFunctionUtils.wrap(func).apply(9.5d), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testFunctionFailed() throws McException
    {
        final McFunction<Double, Double> func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).apply(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntFunctionOk() throws McException
    {
        final McIntFunction<Long> func = (d) -> (long) d;
        
        assertEquals(10, McFunctionUtils.wrap(func).apply(10).longValue());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntFunctionFailed() throws McException
    {
        final McIntFunction<Long> func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).apply(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntSupplierOk() throws McException
    {
        final McIntSupplier func = () -> 10;
        
        assertEquals(10, McFunctionUtils.wrap(func).getAsInt());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntSupplierFailed() throws McException
    {
        final McIntSupplier func = () ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).getAsInt();
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntPredicateOk() throws McException
    {
        final McIntPredicate func = (d) -> d == 10;
        
        assertTrue(McFunctionUtils.wrap(func).test(10));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntPredicateFailed() throws McException
    {
        final McIntPredicate func = (d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).test(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntToDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntToDoubleFunctionOk() throws McException
    {
        final McIntToDoubleFunction func = (d) -> d + 0.5d;
        
        assertEquals(9.5d, McFunctionUtils.wrap(func).applyAsDouble(9), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntToDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntToDoubleFunctionFailed() throws McException
    {
        final McIntToDoubleFunction func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsDouble(9);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntToLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntToLongFunctionOk() throws McException
    {
        final McIntToLongFunction func = (d) -> d;
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsLong(10));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntToLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntToLongFunctionFailed() throws McException
    {
        final McIntToLongFunction func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsLong(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntUnaryOperatorOk() throws McException
    {
        final McIntUnaryOperator func = (d) -> d + 1;
        
        assertEquals(11, McFunctionUtils.wrap(func).applyAsInt(10));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntUnaryOperatorFailed() throws McException
    {
        final McIntUnaryOperator func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsInt(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testIntBinaryOperatorOk() throws McException
    {
        final McIntBinaryOperator func = (d, e) -> d + e;
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsInt(9, 1));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testIntBinaryOperatorFailed() throws McException
    {
        final McIntBinaryOperator func = (a, b) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsInt(9, 1);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongFunctionOk() throws McException
    {
        final McLongFunction<Long> func = (d) -> (long) d;
        
        assertEquals(10, McFunctionUtils.wrap(func).apply(10).longValue());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongFunctionFailed() throws McException
    {
        final McLongFunction<Long> func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).apply(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongSupplierOk() throws McException
    {
        final McLongSupplier func = () -> 10;
        
        assertEquals(10, McFunctionUtils.wrap(func).getAsLong());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongSupplierFailed() throws McException
    {
        final McLongSupplier func = () ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).getAsLong();
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongPredicateOk() throws McException
    {
        final McLongPredicate func = (d) -> d == 10;
        
        assertTrue(McFunctionUtils.wrap(func).test(10));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongPredicateFailed() throws McException
    {
        final McLongPredicate func = (d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).test(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongToDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongToDoubleFunctionOk() throws McException
    {
        final McLongToDoubleFunction func = (d) -> d + 0.5d;
        
        assertEquals(9.5d, McFunctionUtils.wrap(func).applyAsDouble(9), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongToDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongToDoubleFunctionFailed() throws McException
    {
        final McLongToDoubleFunction func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsDouble(9);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongToIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongToLongFunctionOk() throws McException
    {
        final McLongToIntFunction func = (d) -> (int) d;
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsInt(10));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongToIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongToLongFunctionFailed() throws McException
    {
        final McLongToIntFunction func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsInt(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongUnaryOperatorOk() throws McException
    {
        final McLongUnaryOperator func = (d) -> d + 1;
        
        assertEquals(11, McFunctionUtils.wrap(func).applyAsLong(10));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongUnaryOperatorFailed() throws McException
    {
        final McLongUnaryOperator func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsLong(10);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testLongBinaryOperatorOk() throws McException
    {
        final McLongBinaryOperator func = (d, e) -> d + e;
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsLong(9, 1));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongBinaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testLongBinaryOperatorFailed() throws McException
    {
        final McLongBinaryOperator func = (a, b) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsLong(9, 1);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testPredicateOk() throws McException
    {
        final McPredicate<Double> func = (d) -> d == 9.5d;
        
        assertTrue(McFunctionUtils.wrap(func).test(9.5));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McPredicate)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testPredicateFailed() throws McException
    {
        final McPredicate<Double> func = (d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).test(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testSupplierOk() throws McException
    {
        final McSupplier<Double> func = () -> 9.5d;
        
        assertEquals(9.5d, McFunctionUtils.wrap(func).get(), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testSupplierFailed() throws McException
    {
        final McSupplier<Double> func = () ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).get();
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McToDoubleFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testToDoubleFunctionOk() throws McException
    {
        final McToDoubleFunction<Double> func = (d) -> d;
        
        assertEquals(9.5d, McFunctionUtils.wrap(func).applyAsDouble(9.5d), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testToDoubleFunctionFailed() throws McException
    {
        final McToDoubleFunction<Double> func = (d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsDouble(9.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McToIntFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testToIntFunctionOk() throws McException
    {
        final McToIntFunction<Integer> func = (d) -> d;
        
        assertEquals(9, McFunctionUtils.wrap(func).applyAsInt(9));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testToIntFunctionFailed() throws McException
    {
        final McToIntFunction<Integer> func = (d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsInt(9);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McToLongFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testToLongFunctionOk() throws McException
    {
        final McToLongFunction<Long> func = (d) -> d;
        
        assertEquals(9, McFunctionUtils.wrap(func).applyAsLong(9L));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testToLongFunctionFailed() throws McException
    {
        final McToLongFunction<Long> func = (d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsLong(9L);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McToDoubleBiFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testToDoubleBiFunctionOk() throws McException
    {
        final McToDoubleBiFunction<Double, Double> func = (d, e) -> d + e;
        
        assertEquals(10d, McFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testToDoubleBiFunctionFailed() throws McException
    {
        final McToDoubleBiFunction<Double, Double> func = (d, e) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsDouble(9.5d, 0.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McToIntBiFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testToIntBiFunctionOk() throws McException
    {
        final McToIntBiFunction<Integer, Integer> func = (d, e) -> d + e;
        
        assertEquals(9, McFunctionUtils.wrap(func).applyAsInt(8, 1));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testToIntBiFunctionFailed() throws McException
    {
        final McToIntBiFunction<Integer, Integer> func = (d, e) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsInt(9, 1);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McToLongBiFunction)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testToLongBiFunctionOk() throws McException
    {
        final McToLongBiFunction<Long, Long> func = (d, e) -> d + e;
        
        assertEquals(10, McFunctionUtils.wrap(func).applyAsLong(9L, 1L));
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testToLongBiFunctionFailed() throws McException
    {
        final McToLongBiFunction<Long, Long> func = (d, e) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).applyAsLong(9L, 1L);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McObjDoubleConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testObjDoubleConsumerOk() throws McException
    {
        final McObjDoubleConsumer<AtomicDouble> func = (a, d) -> a.set(d + a.get());
        final AtomicDouble val = new AtomicDouble(9d);
        McFunctionUtils.wrap(func).accept(val, 0.5d);
        
        assertEquals(9.5d, val.get(), 0);
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McDoubleSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testObjDoubleConsumerFailed() throws McException
    {
        final McObjDoubleConsumer<AtomicDouble> func = (a, d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(new AtomicDouble(), 0.5d);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McObjIntConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testObjIntConsumerOk() throws McException
    {
        final McObjIntConsumer<AtomicInteger> func = (a, d) -> a.set(d + a.get());
        final AtomicInteger val = new AtomicInteger(9);
        McFunctionUtils.wrap(func).accept(val, 1);
        
        assertEquals(10, val.get());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McIntSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testObjIntConsumerFailed() throws McException
    {
        final McObjIntConsumer<AtomicInteger> func = (a, d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(new AtomicInteger(), 1);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McObjLongConsumer)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testObjLongConsumerOk() throws McException
    {
        final McObjLongConsumer<AtomicLong> func = (a, d) -> a.set(d + a.get());
        final AtomicLong val = new AtomicLong(9);
        McFunctionUtils.wrap(func).accept(val, 1);
        
        assertEquals(10, val.get());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testObjLongConsumerFailed() throws McException
    {
        final McObjLongConsumer<AtomicLong> func = (a, d) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).accept(new AtomicLong(), 1);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McUnaryOperator)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test
    public void testUnaryOperatorOk() throws McException
    {
        final McUnaryOperator<Long> func = (a) -> a + 1;
        
        assertEquals(10, McFunctionUtils.wrap(func).apply(9L).longValue());
    }
    
    /**
     * Tests method {@link McFunctionUtils#wrap(de.minigameslib.mclib.api.util.function.McLongSupplier)}
     * 
     * @throws McException
     *             thrown on errors.
     */
    @Test(expected = McException.class)
    public void testUnaryOperatorFailed() throws McException
    {
        final McUnaryOperator<Long> func = (a) ->
        {
            throw new McException(CommonMessages.InvokeIngame);
        };
        
        try
        {
            McFunctionUtils.wrap(func).apply(9L);
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw ex.unwrap();
        }
    }
    
    /**
     * Constructor test for code coverage.
     */
    @SuppressWarnings("unused")
    @Test
    public void testConstructor()
    {
        new McFunctionUtils();
    }
    
}
