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
 * Similar to {@link java.util.function.IntPredicate} but is able to throw McExceptions.
 * 
 * @author mepeisen
 *
 */
@FunctionalInterface
public interface McIntPredicate
{
    
    /**
     * Evaluates this predicate on the given argument.
     *
     * @param value
     *            the input argument
     * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
     * @throws McException
     *             thrown on problems, f.e. networking errors.
     */
    boolean test(int value) throws McException;
    
    /**
     * Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another. When evaluating the composed predicate, if this predicate is {@code false}, then the
     * {@code other} predicate is not evaluated.
     *
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be evaluated.
     * </p>
     *
     * @param other
     *            a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the {@code other} predicate
     * @throws NullPointerException
     *             if other is null
     */
    default McIntPredicate and(McIntPredicate other)
    {
        Objects.requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }
    
    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default McIntPredicate negate()
    {
        return (value) -> !test(value);
    }
    
    /**
     * Returns a composed predicate that represents a short-circuiting logical OR of this predicate and another. When evaluating the composed predicate, if this predicate is {@code true}, then the
     * {@code other} predicate is not evaluated.
     *
     * <p>
     * Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be evaluated.
     * </p>
     *
     * @param other
     *            a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the {@code other} predicate
     * @throws NullPointerException
     *             if other is null
     */
    default McIntPredicate or(McIntPredicate other)
    {
        Objects.requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
    
}
