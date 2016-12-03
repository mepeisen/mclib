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

/**
 * An entity handler is declared by another plugin; while entity creation it is used to store custom data.
 * 
 * @author mepeisen
 */
public interface EntityHandlerInterface
{
    
    /**
     * Invoked upon creation of an entity. It is safe to store the entity object within instance fields. To referr other components or zones within config you must only use the ids.
     * 
     * @param entity
     *            entity that is created.
     * @throws McException thrown if the entity has errors.
     */
    void onCreate(EntityInterface entity) throws McException;
    
    /**
     * Invoked upon re-loading an entity from config (after server restart). It is invoked after the config was loaded.
     * 
     * @param entity
     *            entity that is resumed
     * @throws McException thrown if the entity has errors.
     */
    void onResume(EntityInterface entity) throws McException;
    
    /**
     * Invoked upon pausing (plugin disabling, shutdown)
     * 
     * @param entity
     *            entity hat is paused.
     */
    void onPause(EntityInterface entity);
    
    /**
     * Checks if the entity can be deleted
     * 
     * @throws McException
     *             thrown to veto the deletion.
     */
    void canDelete() throws McException;
    
    /**
     * Invoked upon deletion of the entity.
     */
    void onDelete();
}
