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

import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.config.Configurable;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * An interface for a gui session.
 * 
 * @author mepeisen
 */
public interface GuiSessionInterface extends Configurable
{
    
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
    ClickGuiInterface getGui();
    
    /**
     * Sets a new gui page or updates the client after changing the gui items of a page.
     * 
     * @param page new gui page.
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
    
}
