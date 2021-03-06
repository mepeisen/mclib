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
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * An interface for a gui session.
 * 
 * @author mepeisen
 */
public interface GuiSessionInterface extends DataFragment
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
     * refresh contents of current click gui.
     */
    void refreshClickGui();
    
    /**
     * Returns the current anvil gui reference.
     * 
     * @return current anvil gui.
     */
    AnvilGuiInterface getAnvilGui();
    
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
    
    // smart gui
    
    /**
     * Displays an error message
     * 
     * @param title
     *            the title to be displayed.
     * @param titleArgs
     *            arguments to build the title string
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     *            arguments to build the messages string
     * @param okButton
     *            the ok button or {@code null} to use a default ok button.
     * @return smart gui dialog reference.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiInterface sguiDisplayError(LocalizedMessageInterface title, Serializable[] titleArgs, LocalizedMessageInterface message, Serializable[] messageArgs, GuiButton okButton) throws McException;
    
    /**
     * Displays an info button
     * 
     * @param title
     *            the title to be displayed.
     * @param titleArgs
     *            arguments to build the title string
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     *            arguments to build the messages string
     * @param okButton
     *            the ok button or {@code null} to use a default ok button.
     * @return smart gui dialog reference.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiInterface sguiDisplayInfo(LocalizedMessageInterface title, Serializable[] titleArgs, LocalizedMessageInterface message, Serializable[] messageArgs, GuiButton okButton) throws McException;
    
    /**
     * Creates a new gui button with custom label.
     * 
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @return smart gui button
     * @throws McException
     *             thrown if player has no smart gui.
     */
    GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable[] labelArgs) throws McException;
    
    /**
     * Creates a new gui button with custom label.
     * 
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @param action
     *            callback to be invoked once the user clicks the button in gui
     * @return smart gui button
     * @throws McException
     *             thrown if player has no smart gui.
     */
    GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action) throws McException;
    
    /**
     * Creates a new gui button with custom label.
     * 
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @param action
     *            callback to be invoked once the user clicks the button in gui
     * @param closeAction
     *            {@code true} to let the button close the dialog
     * @return smart gui button
     * @throws McException
     *             thrown if player has no smart gui.
     */
    GuiButton sguiCreateButton(LocalizedMessageInterface label, Serializable[] labelArgs, McBiConsumer<SGuiInterface, DataSection> action, boolean closeAction) throws McException;
    
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
     *            arguments to build the title string
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     *            arguments to build the messages string
     * @param yesButton
     *            the yes button or {@code null} to use a default yes button.
     * @param noButton
     *            the no button or {@code null} to use a default no button.
     * @return smart gui dialog reference.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiInterface sguiDisplayYesNo(LocalizedMessageInterface title, Serializable[] titleArgs, LocalizedMessageInterface message, Serializable[] messageArgs, GuiButton yesButton, GuiButton noButton)
        throws McException;
    
    /**
     * Displays a yes/no/cancel confirmation dialog
     * 
     * @param title
     *            the title to be displayed.
     * @param titleArgs
     *            arguments to build the title string
     * @param message
     *            the message to be displayed.
     * @param messageArgs
     *            arguments to build the messages string
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
    SGuiInterface sguiDisplayYesNoCancel(LocalizedMessageInterface title, Serializable[] titleArgs, LocalizedMessageInterface message, Serializable[] messageArgs, GuiButton yesButton,
        GuiButton noButton, GuiButton cancelButton) throws McException;
    
    /**
     * Creates a form builder to produce forms.
     * 
     * @param title
     *            the title to be used in form.
     * @param titleArgs
     *            arguments to build the titles string
     * @param closable
     *            {@code true} if fom is closable
     * @param closeAction
     *            action to be invoked on form close
     * @return form builder
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiFormBuilderInterface sguiForm(LocalizedMessageInterface title, Serializable[] titleArgs, boolean closable, McRunnable closeAction) throws McException;
    
    /**
     * Creates a new marker and displays it on client.
     * 
     * @param component
     *            the component to display.
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @return marker interface to control the marker.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiMarkerInterface sguiShowMarker(ComponentInterface component, LocalizedMessageInterface label, Serializable... labelArgs) throws McException;
    
    /**
     * Creates a new marker and displays it on client.
     * 
     * @param sign
     *            the sign to display.
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @return marker interface to control the marker.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiMarkerInterface sguiShowMarker(SignInterface sign, LocalizedMessageInterface label, Serializable... labelArgs) throws McException;
    
    /**
     * Creates a new marker and displays it on client.
     * 
     * @param zone
     *            the component to display.
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @return marker interface to control the marker.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiMarkerInterface sguiShowMarker(ZoneInterface zone, LocalizedMessageInterface label, Serializable... labelArgs) throws McException;
    
    /**
     * Creates a new marker and displays it on client with given color code.
     * 
     * @param component
     *            the component to display.
     * @param r
     *            red color
     * @param g
     *            green color
     * @param b
     *            blue color
     * @param alpha
     *            alpha channel
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @return marker interface to control the marker.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiMarkerInterface sguiShowMarker(ComponentInterface component, int r, int g, int b, int alpha, LocalizedMessageInterface label, Serializable... labelArgs) throws McException;
    
    /**
     * Creates a new marker and displays it on client.
     * 
     * @param sign
     *            the sign to display.
     * @param r
     *            red color
     * @param g
     *            green color
     * @param b
     *            blue color
     * @param alpha
     *            alpha channel
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @return marker interface to control the marker.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiMarkerInterface sguiShowMarker(SignInterface sign, int r, int g, int b, int alpha, LocalizedMessageInterface label, Serializable... labelArgs) throws McException;
    
    /**
     * Creates a new marker and displays it on client.
     * 
     * @param zone
     *            the component to display.
     * @param r
     *            red color
     * @param g
     *            green color
     * @param b
     *            blue color
     * @param alpha
     *            alpha channel
     * @param label
     *            custom gui label.
     * @param labelArgs
     *            arguments to build the label string
     * @return marker interface to control the marker.
     * @throws McException
     *             thrown if player has no smart gui.
     */
    SGuiMarkerInterface sguiShowMarker(ZoneInterface zone, int r, int g, int b, int alpha, LocalizedMessageInterface label, Serializable... labelArgs) throws McException;
    
    /**
     * Removes all markers from sgui
     * 
     * @throws McException
     *             thrown if player has no smart gui.
     */
    void sguiRemoveAllMarkers() throws McException;
    
    /**
     * Common sgui marker interface.
     */
    interface SGuiMarkerInterface
    {
        
        /**
         * Removes the marker
         * 
         * @throws McException
         *             thrown if player has no smart gui.
         */
        void remove() throws McException;
        
        /**
         * Updates the marker label
         * 
         * @param label
         *            custom gui label.
         * @param args
         *            arguments to build the label string
         * 
         * @throws McException
         *             thrown if player has no smart gui.
         */
        void updateLabel(LocalizedMessageInterface label, Serializable... args) throws McException;
        
    }
    
}
