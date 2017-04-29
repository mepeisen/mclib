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

package de.minigameslib.mclib.pshared;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Data Fragment for the {@link CoreMessages#SendError} message.
 * 
 * @author mepeisen
 */
public class SendErrorData extends AnnotatedDataFragment
{
    
    /**
     * Text content of the error message.
     */
    @PersistentField
    protected String message;
    
    /**
     * The window id.
     */
    @PersistentField
    protected String id;

    /**
     * Returns the window id.
     * @return the id
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Sets the window id.
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Returns the error message text.
     * @return the message
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * Sets the error message text.
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
}
