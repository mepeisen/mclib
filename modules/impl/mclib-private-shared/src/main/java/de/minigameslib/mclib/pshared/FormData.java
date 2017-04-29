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
 * Data fragments for form data entries.
 * 
 * @author mepeisen
 */
public class FormData extends AnnotatedDataFragment
{
    
    /**
     * Form key.
     */
    @PersistentField
    protected String key;
    
    /**
     * Form value as string.
     */
    @PersistentField
    protected String value;

    /**
     * Returns the key.
     * @return the key
     */
    public String getKey()
    {
        return this.key;
    }

    /**
     * Sets the key.
     * @param key the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * Returns the value.
     * @return the value
     */
    public String getValue()
    {
        return this.value;
    }

    /**
     * Sets the value.
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    

}
