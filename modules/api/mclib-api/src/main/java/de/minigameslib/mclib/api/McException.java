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

package de.minigameslib.mclib.api;

import java.io.Serializable;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * A mclib exception.
 * 
 * @author mepeisen
 */
public class McException extends Exception
{
    
    /**
     * serial version uid.
     */
    private static final long       serialVersionUID = 4482601783924644721L;
    
    /** the error message. */
    private final LocalizedMessageInterface msg;
    
    /** the message arguments. */
    private final Serializable[]    args;
    
    /**
     * Constructor.
     * 
     * @param msg
     *            the error message
     * @param args
     *            the arguments for building the message.
     */
    public McException(LocalizedMessageInterface msg, Serializable... args)
    {
        super(toString(msg, args));
        this.msg = msg;
        this.args = args;
    }
    
    /**
     * Constructor.
     * 
     * @param msg
     *            the error message
     * @param cause
     *            the underlying exception (cause)
     * @param args
     *            the arguments for building the message.
     */
    public McException(LocalizedMessageInterface msg, Throwable cause, Serializable... args)
    {
        super(toString(msg, args), cause);
        this.msg = msg;
        this.args = args;
    }
    
    /**
     * The error message
     * 
     * @return the message
     */
    public LocalizedMessageInterface getErrorMessage()
    {
        return this.msg;
    }
    
    /**
     * the arguments to build a human readable string
     * 
     * @return the args
     */
    public Serializable[] getArgs()
    {
        return this.args;
    }
    
    /**
     * Converts given code and args to a loggable text.
     * 
     * @param msg2
     * @param args2
     * @return exception string for logging.
     */
    private static String toString(LocalizedMessageInterface msg2, Serializable[] args2)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(msg2.id());
        builder.append(", args: ["); //$NON-NLS-1$
        if (args2 != null)
        {
            for (final Serializable arg : args2)
            {
                builder.append("\n  "); //$NON-NLS-1$
                builder.append(arg);
            }
        }
        builder.append("]"); //$NON-NLS-1$
        return builder.toString();
    }
    
}
