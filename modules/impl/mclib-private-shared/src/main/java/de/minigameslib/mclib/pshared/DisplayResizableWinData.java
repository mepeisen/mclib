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

import java.util.ArrayList;
import java.util.List;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Data Fragment for the {@link CoreMessages#DisplayResizableWin} message.
 * 
 * @author mepeisen
 */
public class DisplayResizableWinData extends AnnotatedDataFragment
{
    
    /**
     * Title of the window.
     */
    @PersistentField
    protected String title;
    
    /**
     * {@code true} true if a close button is added.
     */
    @PersistentField
    protected boolean closable;
    
    /**
     * Number of columns.
     */
    @PersistentField
    protected int numColumns;
    
    /**
     * Content widgets.
     */
    @PersistentField
    protected List<WidgetData> widgets = new ArrayList<>();
    
    /**
     * Additional buttons.
     */
    @PersistentField
    protected List<ButtonData> buttons = new ArrayList<>();
    
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
     * Returns the number of columns.
     * @return the numColumns
     */
    public int getNumColumns()
    {
        return this.numColumns;
    }

    /**
     * Sets the number of columns.
     * @param numColumns the numColumns to set
     */
    public void setNumColumns(int numColumns)
    {
        this.numColumns = numColumns;
    }

    /**
     * Returns the widgets list.
     * @return the widgets
     */
    public List<WidgetData> getWidgets()
    {
        return this.widgets;
    }

    /**
     * Returns the title.
     * @return the title
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * Sets the title.
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Returns the closable flag.
     * @return the closable
     */
    public boolean isClosable()
    {
        return this.closable;
    }

    /**
     * Sets the closable flag.
     * @param closable the closable to set
     */
    public void setClosable(boolean closable)
    {
        this.closable = closable;
    }

    /**
     * Returns the list of buttons.
     * @return the buttons
     */
    public List<ButtonData> getButtons()
    {
        return this.buttons;
    }
    
}
