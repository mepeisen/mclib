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

import java.io.Serializable;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * A stubbing interface for outgoing answers.
 * 
 * @author mepeisen
 * @param <T>
 *            consumed object class
 */
public interface McOutgoingStubbing<T>
{
    
    /**
     * Let the given consumer be invoked if the condition meets the criteria.
     * 
     * @param consumer
     *            to be invoked if stubbing results to {@code true}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             rethrown from consumer
     */
    McOutgoingStubbing<T> then(McConsumer<T> consumer) throws McException;
    
    /**
     * Let us throw an exception if the condition meets the criteria.
     * 
     * @param consumer
     *            to be invoked if stubbing results to {@code true}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             rethrown from consumer
     */
    McOutgoingStubbing<T> thenThrow(McFunction<T, McException> consumer) throws McException;
    
    /**
     * Let us throw an exception if the condition meets the criteria.
     * 
     * @param msg
     *            to be thrown if stubbing results to {@code true}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             thrown with given error code
     */
    McOutgoingStubbing<T> thenThrow(LocalizedMessageInterface msg) throws McException;
    
    /**
     * Let us throw an exception if the condition meets the criteria.
     * 
     * @param msg
     *            to be thrown if stubbing results to {@code true}
     * @param args
     *            to be thrown if stubbing results to {@code true}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             thrown with given error code
     */
    McOutgoingStubbing<T> thenThrow(LocalizedMessageInterface msg, McFunction<T, Serializable[]> args) throws McException;
    
    /**
     * Let the given consumer be invoked if the condition does not meet the criteria.
     * 
     * @param consumer
     *            to be invoked if stubbing results to {@code false}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             rethrown from consumer
     */
    McOutgoingStubbing<T> _else(McConsumer<T> consumer) throws McException;
    
    /**
     * Let us throw an exception if the condition does not meet the criteria.
     * 
     * @param consumer
     *            to be invoked if stubbing results to {@code false}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             rethrown from consumer
     */
    McOutgoingStubbing<T> _elseThrow(McFunction<T, McException> consumer) throws McException;
    
    /**
     * Let us throw an exception if the condition does not meet the criteria.
     * 
     * @param msg
     *            to be thrown if stubbing results to {@code false}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             thrown with given error code
     */
    McOutgoingStubbing<T> _elseThrow(LocalizedMessageInterface msg) throws McException;
    
    /**
     * Let us throw an exception if the condition does not meet the criteria.
     * 
     * @param msg
     *            to be thrown if stubbing results to {@code false}
     * @param args
     *            to be thrown if stubbing results to {@code false}
     * @return this object for chaining additional then or else consumers.
     * @throws McException
     *             thrown with given error code
     */
    McOutgoingStubbing<T> _elseThrow(LocalizedMessageInterface msg, McFunction<T, Serializable[]> args) throws McException;
    
}
