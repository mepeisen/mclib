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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.Configurable;

/**
 * A sign handler is declared by another plugin; while sign creation it is used to store custom data.
 * 
 * @author mepeisen
 */
public interface SignHandlerInterface extends Configurable
{
    
    /**
     * Invoked upon creation of a sign. It is safe to store the sign object within instance fields. To referr other signs or zones within config you must only use the ids.
     * 
     * @param sign
     *            sign that was created.
     * @throws McException thrown if the sign has errors.
     */
    void onCreate(SignInterface sign) throws McException;
    
    /**
     * Invoked upon re-loading a sign from config (after server restart). It is invoked after the config was loaded.
     * 
     * @param sign
     *            sign that was resumed
     * @throws McException thrown if the sign has errors.
     */
    void onResume(SignInterface sign) throws McException;
    
    /**
     * Invoked upon pausing (plugin disabling, shutdown)
     * 
     * @param sign
     *            sign hat is paused.
     */
    void onPause(SignInterface sign);
    
    /**
     * Checks if the sign can be deleted
     * 
     * @throws McException
     *             thrown to veto the deletion.
     */
    void canDelete() throws McException;
    
    /**
     * Invoked upon deletion of the sign.
     */
    void onDelete();
    
}
