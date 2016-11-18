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

package de.minigameslib.mclib.shared.api.com;

import java.util.UUID;

/**
 * Player data.
 * 
 * @author mepeisen
 */
public class PlayerData implements DataFragment
{
    
    /** player uuid. */
    private UUID playerUuid;
    /** player name. */
    private String playerName;

    /**
     * @param playerUuid
     * @param playerName
     */
    public PlayerData(UUID playerUuid, String playerName)
    {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.playerName == null) ? 0 : this.playerName.hashCode());
        result = prime * result + ((this.playerUuid == null) ? 0 : this.playerUuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerData other = (PlayerData) obj;
        if (this.playerName == null)
        {
            if (other.playerName != null)
                return false;
        }
        else if (!this.playerName.equals(other.playerName))
            return false;
        if (this.playerUuid == null)
        {
            if (other.playerUuid != null)
                return false;
        }
        else if (!this.playerUuid.equals(other.playerUuid))
            return false;
        return true;
    }

    @Override
    public void read(DataSection section)
    {
        this.playerUuid = UUID.fromString(section.getString("uuid", "")); //$NON-NLS-1$ //$NON-NLS-2$
        this.playerName = section.getString("name", "?"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void write(DataSection section)
    {
        section.set("uuid", this.playerUuid.toString()); //$NON-NLS-1$
        section.set("name", this.playerName); //$NON-NLS-1$
    }

    /**
     * @return the playerUuid
     */
    public UUID getPlayerUuid()
    {
        return this.playerUuid;
    }

    /**
     * @return the playerName
     */
    public String getPlayerName()
    {
        return this.playerName;
    }
    
}
