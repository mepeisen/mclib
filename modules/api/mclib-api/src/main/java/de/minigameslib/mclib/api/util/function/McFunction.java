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

import java.util.Objects;

import de.minigameslib.mclib.api.McException;

/**
 * Similar to {@link java.util.function.Function} but is able to throw McExceptions.
 * 
 * @author mepeisen
 * @param <T>
 *            the type of the input to the function
 * @param <R>
 *            the type of the result of the function
 */
@FunctionalInterface
public interface McFunction<T, R>
{
    
    /**
     * Applies this function to the given argument.
     *
     * @param arg
     *            the function argument
     * @return the function result
     * @throws McException
     *             thrown on problems, f.e. networking errors.
     */
    R apply(T arg) throws McException;
    
    /**
     * Returns a composed function that first applies the {@code before} function to its input, and then applies this function to the result. If evaluation of either function throws an exception, it
     * is relayed to the caller of the composed function.
     *
     * @param <V>
     *            the type of input to the {@code before} function, and to the composed function
     * @param before
     *            the function to apply before this function is applied
     * @return a composed function that first applies the {@code before} function and then applies this function
     * @throws NullPointerException
     *             if before is null
     *
     * @see #andThen(McFunction)
     */
    default <V> McFunction<V, R> compose(McFunction<? super V, ? extends T> before)
    {
        Objects.requireNonNull(before);
        return (V arg) -> apply(before.apply(arg));
    }
    
    /**
     * Returns a composed function that first applies this function to its input, and then applies the {@code after} function to the result. If evaluation of either function throws an exception, it is
     * relayed to the caller of the composed function.
     *
     * @param <V>
     *            the type of output of the {@code after} function, and of the composed function
     * @param after
     *            the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws NullPointerException
     *             if after is null
     *
     * @see #compose(McFunction)
     */
    default <V> McFunction<T, V> andThen(McFunction<? super R, ? extends V> after)
    {
        Objects.requireNonNull(after);
        return (T arg) -> after.apply(apply(arg));
    }
    
    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T>
     *            the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> McFunction<T, T> identity()
    {
        return t -> t;
    }
    
}
