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
 * Similar to {@link java.util.function.Consumer} but is able to throw McExceptions.
 * 
 * @author mepeisen
 * @param <T>
 *            type of the consumed items
 */
@FunctionalInterface
public interface McConsumer<T>
{
    
    /**
     * Performs this operation on the given argument.
     *
     * @param obj
     *            the input argument
     * @throws McException
     *             thrown on problems, f.e. networking errors.
     */
    void accept(T obj) throws McException;
    
    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this operation followed by the {@code after} operation. If performing either operation throws an exception, it is relayed to the
     * caller of the composed operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after
     *            the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException
     *             if {@code after} is null
     */
    default McConsumer<T> andThen(McConsumer<? super T> after)
    {
        Objects.requireNonNull(after);
        return (T obj) ->
        {
            accept(obj);
            after.accept(obj);
        };
    }
    
}
