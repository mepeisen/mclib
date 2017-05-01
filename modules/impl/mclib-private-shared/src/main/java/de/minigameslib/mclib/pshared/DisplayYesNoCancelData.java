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
 * Data Fragment for the {@link CoreMessages#DisplayYesNoCancel} message.
 * 
 * @author mepeisen
 */
public class DisplayYesNoCancelData extends AnnotatedDataFragment
{
    
    /**
     * Title of the message box.
     */
    @PersistentField
    protected String     title;
    
    /**
     * Text content of the message box.
     */
    @PersistentField
    protected String     message;
    
    /**
     * Yes-Button.
     */
    @PersistentField
    protected ButtonData yesButton;
    
    /**
     * No-Button.
     */
    @PersistentField
    protected ButtonData noButton;
    
    /**
     * Cancel-Button.
     */
    @PersistentField
    protected ButtonData cancelButton;
    
    /**
     * The window id.
     */
    @PersistentField
    protected String     id;
    
    /**
     * Returns the window id.
     * 
     * @return the id
     */
    public String getId()
    {
        return this.id;
    }
    
    /**
     * Sets the window id.
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }
    
    /**
     * Returns the title.
     * 
     * @return the title
     */
    public String getTitle()
    {
        return this.title;
    }
    
    /**
     * Sets the title.
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * Returns the message text.
     * 
     * @return the message
     */
    public String getMessage()
    {
        return this.message;
    }
    
    /**
     * Sets the message text.
     * 
     * @param message
     *            the message to set
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    /**
     * Returns the yes button.
     * 
     * @return the yesButton
     */
    public ButtonData getYesButton()
    {
        return this.yesButton;
    }
    
    /**
     * Sets the yes button.
     * 
     * @param yesButton
     *            the yesButton to set
     */
    public void setYesButton(ButtonData yesButton)
    {
        this.yesButton = yesButton;
    }
    
    /**
     * Returns the no button.
     * 
     * @return the noButton
     */
    public ButtonData getNoButton()
    {
        return this.noButton;
    }
    
    /**
     * Sets the no button.
     * 
     * @param noButton
     *            the noButton to set
     */
    public void setNoButton(ButtonData noButton)
    {
        this.noButton = noButton;
    }
    
    /**
     * Returns the cancel button.
     * 
     * @return the cancelButton
     */
    public ButtonData getCancelButton()
    {
        return this.cancelButton;
    }
    
    /**
     * Sets the cancel button.
     * 
     * @param cancelButton
     *            the cancelButton to set
     */
    public void setCancelButton(ButtonData cancelButton)
    {
        this.cancelButton = cancelButton;
    }
    
}
