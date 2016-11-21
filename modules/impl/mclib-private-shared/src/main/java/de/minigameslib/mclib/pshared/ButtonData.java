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
 * Data fragments for button widgets.
 * 
 * @author mepeisen
 */
public class ButtonData extends AnnotatedDataFragment
{
    
    /**
     * Button label.
     */
    @PersistentField
    private String label;
    
    /**
     * {@code true} if an action listener is installed on server side.
     */
    @PersistentField
    private boolean hasActionListener;
    
    /**
     * Action id; only set if an action listener is present.
     */
    @PersistentField
    private String actionId;
    
    /**
     * {@code true} if this button closes the dialog.
     */
    @PersistentField
    private boolean isCloseAction;

    /**
     * @return the label
     */
    public String getLabel()
    {
        return this.label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label)
    {
        this.label = label;
    }

    /**
     * @return the hasActionListener
     */
    public boolean isHasActionListener()
    {
        return this.hasActionListener;
    }

    /**
     * @param hasActionListener the hasActionListener to set
     */
    public void setHasActionListener(boolean hasActionListener)
    {
        this.hasActionListener = hasActionListener;
    }

    /**
     * @return the actionId
     */
    public String getActionId()
    {
        return this.actionId;
    }

    /**
     * @param actionId the actionId to set
     */
    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }

    /**
     * @return the isCloseAction
     */
    public boolean isCloseAction()
    {
        return this.isCloseAction;
    }

    /**
     * @param isCloseAction the isCloseAction to set
     */
    public void setCloseAction(boolean isCloseAction)
    {
        this.isCloseAction = isCloseAction;
    }

    

}
