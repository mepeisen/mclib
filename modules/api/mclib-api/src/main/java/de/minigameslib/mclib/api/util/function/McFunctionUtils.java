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

package de.minigameslib.mclib.api.util.function;

import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

import de.minigameslib.mclib.api.McException;

/**
 * A helper for mg functions.
 * 
 * @author mepeisen
 */
public class McFunctionUtils
{
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     * @param <U>
     *            class param
     */
    public static <T, U> BiConsumer<T, U> wrap(McBiConsumer<T, U> orig)
    {
        return (T arg1, U arg2) -> {
            try
            {
                orig.accept(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     * @param <U>
     *            class param
     * @param <R>
     *            class param
     */
    public static <T, U, R> BiFunction<T, U, R> wrap(McBiFunction<T, U, R> orig)
    {
        return (T arg1, U arg2) -> {
            try
            {
                return orig.apply(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> McBinaryOperator<T> wrap(McBinaryOperator<T> orig)
    {
        return (T arg1, T arg2) -> {
            try
            {
                return orig.apply(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     * @param <U>
     *            class param
     */
    public static <T, U> BiPredicate<T, U> wrap(McBiPredicate<T, U> orig)
    {
        return (T arg1, U arg2) -> {
            try
            {
                return orig.test(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static BooleanSupplier wrap(McBooleanSupplier orig)
    {
        return () -> {
            try
            {
                return orig.getAsBoolean();
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> Comparator<T> wrap(McComparator<T> orig)
    {
        return (T arg1, T arg2) -> {
            try
            {
                return orig.compare(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> Consumer<T> wrap(McConsumer<T> orig)
    {
        return (T arg) -> {
            try
            {
                orig.accept(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static DoubleConsumer wrap(McDoubleConsumer orig)
    {
        return (double arg) -> {
            try
            {
                orig.accept(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static IntConsumer wrap(McIntConsumer orig)
    {
        return (int arg) -> {
            try
            {
                orig.accept(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static LongConsumer wrap(McLongConsumer orig)
    {
        return (long arg) -> {
            try
            {
                orig.accept(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> DoubleFunction<T> wrap(McDoubleFunction<T> orig)
    {
        return (double arg) -> {
            try
            {
                return orig.apply(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static DoubleSupplier wrap(McDoubleSupplier orig)
    {
        return () -> {
            try
            {
                return orig.getAsDouble();
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static DoublePredicate wrap(McDoublePredicate orig)
    {
        return (double arg) -> {
            try
            {
                return orig.test(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static DoubleToIntFunction wrap(McDoubleToIntFunction orig)
    {
        return (double arg) -> {
            try
            {
                return orig.applyAsInt(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static DoubleToLongFunction wrap(McDoubleToLongFunction orig)
    {
        return (double arg) -> {
            try
            {
                return orig.applyAsLong(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static DoubleUnaryOperator wrap(McDoubleUnaryOperator orig)
    {
        return (double arg) -> {
            try
            {
                return orig.applyAsDouble(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static DoubleBinaryOperator wrap(McDoubleBinaryOperator orig)
    {
        return (double arg1, double arg2) -> {
            try
            {
                return orig.applyAsDouble(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     * @param <R>
     *            class param
     */
    public static <T, R> Function<T, R> wrap(McFunction<T, R> orig)
    {
        return (T arg) -> {
            try
            {
                return orig.apply(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> IntFunction<T> wrap(McIntFunction<T> orig)
    {
        return (int arg) -> {
            try
            {
                return orig.apply(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static IntSupplier wrap(McIntSupplier orig)
    {
        return () -> {
            try
            {
                return orig.getAsInt();
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static IntPredicate wrap(McIntPredicate orig)
    {
        return (int arg) -> {
            try
            {
                return orig.test(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static IntToDoubleFunction wrap(McIntToDoubleFunction orig)
    {
        return (int arg) -> {
            try
            {
                return orig.applyAsDouble(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static IntToLongFunction wrap(McIntToLongFunction orig)
    {
        return (int arg) -> {
            try
            {
                return orig.applyAsLong(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static IntUnaryOperator wrap(McIntUnaryOperator orig)
    {
        return (int arg) -> {
            try
            {
                return orig.applyAsInt(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static IntBinaryOperator wrap(McIntBinaryOperator orig)
    {
        return (int arg1, int arg2) -> {
            try
            {
                return orig.applyAsInt(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> LongFunction<T> wrap(McLongFunction<T> orig)
    {
        return (long arg) -> {
            try
            {
                return orig.apply(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static LongSupplier wrap(McLongSupplier orig)
    {
        return () -> {
            try
            {
                return orig.getAsLong();
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static LongPredicate wrap(McLongPredicate orig)
    {
        return (long arg) -> {
            try
            {
                return orig.test(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static LongToIntFunction wrap(McLongToIntFunction orig)
    {
        return (long arg) -> {
            try
            {
                return orig.applyAsInt(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static LongToDoubleFunction wrap(McLongToDoubleFunction orig)
    {
        return (long arg) -> {
            try
            {
                return orig.applyAsDouble(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static LongUnaryOperator wrap(McLongUnaryOperator orig)
    {
        return (long arg) -> {
            try
            {
                return orig.applyAsLong(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     */
    public static LongBinaryOperator wrap(McLongBinaryOperator orig)
    {
        return (long arg1, long arg2) -> {
            try
            {
                return orig.applyAsLong(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> ObjDoubleConsumer<T> wrap(McObjDoubleConsumer<T> orig)
    {
        return (T arg1, double arg2) -> {
            try
            {
                orig.accept(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> ObjIntConsumer<T> wrap(McObjIntConsumer<T> orig)
    {
        return (T arg1, int arg2) -> {
            try
            {
                orig.accept(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> ObjLongConsumer<T> wrap(McObjLongConsumer<T> orig)
    {
        return (T arg1, long arg2) -> {
            try
            {
                orig.accept(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> Predicate<T> wrap(McPredicate<T> orig)
    {
        return (T arg) -> {
            try
            {
                return orig.test(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> Supplier<T> wrap(McSupplier<T> orig)
    {
        return () -> {
            try
            {
                return orig.get();
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     * @param <U>
     *            class param
     */
    public static <T, U> ToDoubleBiFunction<T, U> wrap(McToDoubleBiFunction<T, U> orig)
    {
        return (T arg1, U arg2) -> {
            try
            {
                return orig.applyAsDouble(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> ToDoubleFunction<T> wrap(McToDoubleFunction<T> orig)
    {
        return (T arg) -> {
            try
            {
                return orig.applyAsDouble(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     * @param <U>
     *            class param
     */
    public static <T, U> ToIntBiFunction<T, U> wrap(McToIntBiFunction<T, U> orig)
    {
        return (T arg1, U arg2) -> {
            try
            {
                return orig.applyAsInt(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> ToIntFunction<T> wrap(McToIntFunction<T> orig)
    {
        return (T arg) -> {
            try
            {
                return orig.applyAsInt(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     * @param <U>
     *            class param
     */
    public static <T, U> ToLongBiFunction<T, U> wrap(McToLongBiFunction<T, U> orig)
    {
        return (T arg1, U arg2) -> {
            try
            {
                return orig.applyAsLong(arg1, arg2);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> ToLongFunction<T> wrap(McToLongFunction<T> orig)
    {
        return (T arg) -> {
            try
            {
                return orig.applyAsLong(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Creates a wrapper to use a mg lambda as a java util lambda.
     * 
     * @param orig
     *            original.
     * @return java util variant.
     * @param <T>
     *            class param
     */
    public static <T> UnaryOperator<T> wrap(McUnaryOperator<T> orig)
    {
        return (T arg) -> {
            try
            {
                return orig.apply(arg);
            }
            catch (McException ex)
            {
                throw new WrappedException(ex);
            }
        };
    }
    
    /**
     * Exception wrapping.
     * 
     * @author mepeisen
     */
    public static final class WrappedException extends RuntimeException
    {
        
        /**
         * serial version uid.
         */
        private static final long serialVersionUID = -809099844875692391L;
        
        /**
         * Constructor for wrapping.
         * 
         * @param cause
         *            mg exception.
         */
        public WrappedException(McException cause)
        {
            super(cause);
        }
        
        /**
         * Unraps this exception.
         * 
         * @return nested mg exception.
         */
        public McException unwrap()
        {
            return (McException) this.getCause();
        }
        
    }
    
}
