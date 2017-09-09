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
 * Outgoing stub for false checks of minigames predicates.
 * 
 * @author mepeisen
 * @param <T>
 *            argument class
 */
public final class FalseStub<T> implements McOutgoingStubbing<T>
{
    
    /**
     * stubbed element.
     */
    private final T elm;
    
    /**
     * Constructor to create the stub.
     * 
     * @param elm
     *            stubbed element
     */
    public FalseStub(T elm)
    {
        this.elm = elm;
    }
    
    @Override
    public McOutgoingStubbing<T> _else(McConsumer<T> consumer) throws McException
    {
        consumer.accept(this.elm);
        return this;
    }
    
    @Override
    public McOutgoingStubbing<T> _elseThrow(McFunction<T, McException> consumer) throws McException
    {
        throw consumer.apply(this.elm);
    }
    
    @Override
    public McOutgoingStubbing<T> _elseThrow(LocalizedMessageInterface msg) throws McException
    {
        throw new McException(msg);
    }
    
    @Override
    public McOutgoingStubbing<T> _elseThrow(LocalizedMessageInterface msg, McFunction<T, Serializable[]> args2) throws McException
    {
        throw new McException(msg, args2.apply(this.elm));
    }
    
    @Override
    public McOutgoingStubbing<T> then(McConsumer<T> consumer) throws McException
    {
        // does nothing
        return this;
    }
    
    @Override
    public McOutgoingStubbing<T> thenThrow(McFunction<T, McException> consumer) throws McException
    {
        // does nothing
        return this;
    }
    
    @Override
    public McOutgoingStubbing<T> thenThrow(LocalizedMessageInterface msg) throws McException
    {
        // does nothing
        return this;
    }
    
    @Override
    public McOutgoingStubbing<T> thenThrow(LocalizedMessageInterface msg, McFunction<T, Serializable[]> args2) throws McException
    {
        // does nothing
        return this;
    }
    
}
