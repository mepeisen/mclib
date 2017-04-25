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

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

/**
 * Player data.
 * 
 * @author mepeisen
 */
public class PlayerData implements PlayerDataFragment
{
    
    /** player uuid. */
    private UUID playerUuid;
    /** player name. */
    private String playerName;

    /**
     * Constructor to create with new data.
     * @param playerUuid the players uuid
     * @param playerName the players display name
     */
    public PlayerData(UUID playerUuid, String playerName)
    {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
    }
    
    /**
     * Constructor to read from file.
     */
    public PlayerData()
    {
        // empty
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
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        PlayerData other = (PlayerData) obj;
        if (this.playerName == null)
        {
            if (other.playerName != null)
            {
                return false;
            }
        }
        else if (!this.playerName.equals(other.playerName))
        {
            return false;
        }
        if (this.playerUuid == null)
        {
            if (other.playerUuid != null)
            {
                return false;
            }
        }
        else if (!this.playerUuid.equals(other.playerUuid))
        {
            return false;
        }
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

    @Override
    public boolean test(DataSection section)
    {
        boolean result = true;
        result &= section.isString("uuid"); //$NON-NLS-1$
        result &= section.isString("name"); //$NON-NLS-1$
        result &= isUuid(section.getString("uuid")); //$NON-NLS-1$
        result &= section.getKeys(true).equals(new HashSet<>(Arrays.asList("uuid", "name"))); //$NON-NLS-1$ //$NON-NLS-2$
        return result;
    }

    /**
     * Checks for string being a UUID.
     * @param str string to be tested
     * @return {@code true} if this is a uuid
     */
    private boolean isUuid(String str)
    {
        try
        {
            if (str == null)
            {
                return false;
            }
            UUID.fromString(str);
            return true;
        }
        catch (@SuppressWarnings("unused") IllegalArgumentException ex)
        {
            return false;
        }
    }

    @Override
    public UUID getPlayerUUID()
    {
        return this.playerUuid;
    }

    @Override
    public String getDisplayName()
    {
        return this.playerName;
    }
    
}
