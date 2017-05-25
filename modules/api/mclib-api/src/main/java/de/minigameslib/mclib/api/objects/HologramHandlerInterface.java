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

package de.minigameslib.mclib.api.objects;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.shared.api.com.DataFragment;

/**
 * A hologram handler is declared by another plugin; while hologram creation it is used to store custom data.
 * 
 * <p>
 * To fetch hologram events the handler has to implement {@link McListener} and tag event handler methods with {@link McEventHandler}.
 * </p>
 * 
 * @author mepeisen
 */
public interface HologramHandlerInterface extends DataFragment
{
    
    /**
     * Invoked upon creation of a hologram. It is safe to store the object within instance fields. To referr other components or zones within config you must only use the ids.
     * During creation the handler must set the line text using {@link HologramInterface#setLines(java.io.Serializable...)}.
     * 
     * @param hologram
     *            hologram that is created.
     * @throws McException
     *             thrown if the hologram has errors.
     */
    void onCreate(HologramInterface hologram) throws McException;
    
    /**
     * Invoked upon re-loading a hologram from config (after server restart). It is invoked after the config was loaded.
     * During resume the handler must set the line text using {@link HologramInterface#setLines(java.io.Serializable...)}.
     * 
     * @param hologram
     *            hologram that is resumed
     * @throws McException
     *             thrown if the object has errors.
     */
    void onResume(HologramInterface hologram) throws McException;
    
    /**
     * Invoked upon pausing (plugin disabling, shutdown)
     * 
     * @param hologram
     *            hologram hat is paused.
     */
    void onPause(HologramInterface hologram);
    
    /**
     * Checks if the hologram can be deleted
     * 
     * @throws McException
     *             thrown to veto the deletion.
     */
    void canDelete() throws McException;
    
    /**
     * Invoked upon deletion of the hologram.
     */
    void onDelete();
    
    /**
     * Checks if the location can be changed.
     * 
     * @param newValue
     *            the new location
     * @throws McException
     *             thrown to veto the location change.
     */
    void canChangeLocation(Location newValue) throws McException;
    
    /**
     * Invoked upon change of location.
     * 
     * @param newValue
     *            the new location
     */
    void onLocationChange(Location newValue);
    
}
