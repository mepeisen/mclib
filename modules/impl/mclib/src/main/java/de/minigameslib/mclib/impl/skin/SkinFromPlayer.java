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

package de.minigameslib.mclib.impl.skin;

import java.util.UUID;

import de.minigameslib.mclib.api.skin.SkinInterface;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * A skin represented from player uuid.
 * 
 * @author mepeisen
 */
public class SkinFromPlayer extends AnnotatedDataFragment implements SkinInterface
{
    
    /**
     * the player uuid as string.
     */
    @PersistentField
    protected String playerUuid;
    
    /**
     * The player uuid as uuid.
     */
    protected UUID   uuid;
    
    /**
     * Constructor.
     */
    public SkinFromPlayer()
    {
        // empty
    }
    
    /**
     * Constructor.
     * 
     * @param uuid
     *            player uuid.
     */
    public SkinFromPlayer(UUID uuid)
    {
        this.uuid = uuid;
        this.playerUuid = uuid.toString();
    }
    
    /**
     * Returns the player uuid.
     * 
     * @return the playerUuid
     */
    public UUID getPlayerUuid()
    {
        return this.uuid;
    }
    
    @Override
    public void read(DataSection section)
    {
        super.read(section);
        this.uuid = UUID.fromString(this.playerUuid);
    }
    
}
