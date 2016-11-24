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

package de.minigameslib.mclib.api.gui;

import java.io.Serializable;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * A simple wrapper for gui values.
 * 
 * @author mepeisen
 */
public class SGuiComboValue
{
    
    /** the internal key. */
    private final String key;
    
    /** the value to display. */
    private final LocalizedMessageInterface value;
    
    /** arguments. */
    private final Serializable[] args;

    /**
     * @param key
     * @param value
     * @param args
     */
    public SGuiComboValue(String key, LocalizedMessageInterface value, Serializable... args)
    {
        this.key = key;
        this.value = value;
        this.args = args;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return this.key;
    }

    /**
     * @return the value
     */
    public LocalizedMessageInterface getValue()
    {
        return this.value;
    }

    /**
     * @return the args
     */
    public Serializable[] getArgs()
    {
        return this.args;
    }
    
}
