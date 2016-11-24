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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.config.Configurable;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * An interface for a gui session.
 * 
 * @author mepeisen
 */
public interface GuiSessionInterface extends Configurable
{
    
    /**
     * Returns the type of opened gui.
     * 
     * @return opened gui type.
     */
    GuiType getCurrentType();
    
    /**
     * Returns the player that owns this gui session.
     * 
     * @return player owning the session.
     */
    McPlayerInterface getPlayer();
    
    /**
     * Returns the current click gui reference.
     * 
     * @return current click gui.
     */
    ClickGuiInterface getClickGui();
    
    /**
     * Returns the current anvil gui reference.
     * 
     * @return current anvil gui.
     */
    AnvilGuiInterface getAnvilGui();
    
    /**
     * Returns the smart gui reference.
     * 
     * @return smart gui.
     */
    SGuiInterface getSmartGui();
    
    /**
     * Sets a new gui page or updates the client after changing the gui items of a page.
     * 
     * @param page
     *            new gui page.
     */
    void setNewPage(ClickGuiPageInterface page);
    
    /**
     * Closes the gui session.
     */
    void close();
    
    /**
     * Returns a gui storage for storing data while the gui is open.
     * 
     * @return a gui storage.
     */
    McStorage getGuiStorage();
    
    /**
     * Returns a gui storage for storing data while the player is logged in.
     * 
     * @return gui storage.
     */
    McStorage getPlayerSessionStorage();
    
    /**
     * Returns a gui storage for persistent data stored on disk
     * 
     * @return gui storage.
     */
    McStorage getPlayerPersistentStorage();
    
    // smart gui
    
    /**
     * Displays an error message
     * 
     * @param title
     *            the title to be displayed.
     * @param titleArgs
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     * @param okButton
     *            the ok button or {@code null} to use a default ok button.
     * @return smart gui dialog reference.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiInterface sguiDisplayError(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton okButton) throws McException;
    
    /**
     * Displays an info button
     * 
     * @param title
     *            the title to be displayed.
     * @param titleArgs
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     * @param okButton
     *            the ok button or {@code null} to use a default ok button.
     * @return smart gui dialog reference.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiInterface sguiDisplayInfo(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton okButton) throws McException;
    
    /**
     * Creates a new gui button with custom label.
     * 
     * @param label
     *            custom gui label.
     * @param labelArgs
     * @return smart gui button
     * @throws McException
     *             thrown if player has no smart gui.
     */
    GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable labelArgs[]) throws McException;
    
    /**
     * Creates a new gui button with custom label.
     * 
     * @param label
     *            custom gui label.
     * @param labelArgs
     * @param action
     *            callback to be invoked once the user clicks the button in gui
     * @return smart gui button
     * @throws McException
     *             thrown if player has no smart gui.
     */
    GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable labelArgs[], McRunnable action) throws McException;
    
    /**
     * Creates a new gui button with custom label.
     * 
     * @param label
     *            custom gui label.
     * @param labelArgs
     * @param action
     *            callback to be invoked once the user clicks the button in gui
     * @param closeAction
     *            {@code true} to let the button close the dialog
     * @return smart gui button
     * @throws McException
     *             thrown if player has no smart gui.
     */
    GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable labelArgs[], McRunnable action, boolean closeAction) throws McException;
    
    /**
     * Interface to represent a gui button.
     */
    interface GuiButton
    {
        // empty
    }
    
    /**
     * Displays a yes/no confirmation dialog
     * 
     * @param title
     *            the title to be displayed.
     * @param titleArgs
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     * @param yesButton
     *            the yes button or {@code null} to use a default yes button.
     * @param noButton
     *            the no button or {@code null} to use a default no button.
     * @return smart gui dialog reference.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiInterface sguiDisplayYesNo(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton yesButton, GuiButton noButton)
            throws McException;
    
    /**
     * Displays a yes/no/cancel confirmation dialog
     * 
     * @param title
     *            the title to be displayed.
     * @param titleArgs
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     * @param yesButton
     *            the yes button or {@code null} to use a default yes button.
     * @param noButton
     *            the no button or {@code null} to use a default no button.
     * @param cancelButton
     *            the cancel button or {@code null} to use a default cancel button.
     * @return smart gui dialog reference.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiInterface sguiDisplayYesNoCancel(LocalizedMessageInterface title, Serializable titleArgs[], LocalizedMessageInterface message, Serializable messageArgs[], GuiButton yesButton,
            GuiButton noButton, GuiButton cancelButton) throws McException;
    
    /**
     * Creates a form builder to produce forms.
     * 
     * @param title
     *            the title to be used in form.
     * @param titleArgs
     * @param closable
     * @return form builder
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiFormBuilderInterface sguiForm(LocalizedMessageInterface title, Serializable titleArgs[], boolean closable) throws McException;
    
}
