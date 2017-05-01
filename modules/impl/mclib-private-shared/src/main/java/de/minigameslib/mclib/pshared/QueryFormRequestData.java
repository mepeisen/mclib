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
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Data Fragment for the {@link CoreMessages#QueryFormRequest} message.
 * 
 * @author mepeisen
 */
public class QueryFormRequestData extends AnnotatedDataFragment
{
    
    /**
     * form input id.
     */
    @PersistentField
    protected String      inputId;
    
    /**
     * the query arguments.
     */
    @PersistentField
    protected DataSection data = new MemoryDataSection();
    
    /**
     * The window id.
     */
    @PersistentField
    protected String      winId;
    
    /**
     * Returns the window id.
     * 
     * @return the widgetId
     */
    public String getWinId()
    {
        return this.winId;
    }
    
    /**
     * Sets the window id.
     * 
     * @param winId
     *            the winId to set
     */
    public void setWinId(String winId)
    {
        this.winId = winId;
    }
    
    /**
     * Returns the input id.
     * 
     * @return the inputId
     */
    public String getInputId()
    {
        return this.inputId;
    }
    
    /**
     * Sets the input id.
     * 
     * @param inputId
     *            the inputId to set
     */
    public void setInputId(String inputId)
    {
        this.inputId = inputId;
    }
    
    /**
     * Returns the form data.
     * 
     * @return the data
     */
    public DataSection getData()
    {
        return this.data;
    }
    
}
