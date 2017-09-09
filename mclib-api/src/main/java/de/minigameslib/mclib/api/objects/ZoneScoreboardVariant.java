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

import java.util.Set;
import java.util.UUID;

/**
 * A scoreboard variant created for a list of users.
 * 
 * @author mepeisen
 */
public interface ZoneScoreboardVariant extends ScoreboardVariant
{
    
    /**
     * Returns the name of the scorboard variant.
     * 
     * @return scoreboard variant name
     */
    String getName();
    
    /**
     * Adds players to whitelist.
     * 
     * <p>
     * Whitelisted players see scoreboards that are hidden.
     * </p>
     * 
     * @param uuid
     *            player to whitelist
     */
    void addPlayerToWhiteList(UUID uuid);
    
    /**
     * Remove player from whitelist.
     * 
     * @param uuid
     *            player to be removed.
     */
    void removePlayerFromWhiteList(UUID uuid);
    
    /**
     * Returns the players from whitelist.
     * 
     * @return whitelisted players.
     */
    Set<UUID> getWhitelist();
    
    /**
     * Checks if scoreboard is visible to public.
     * 
     * @return {@code true} if only the players from whitelist may see the scoreboard; {@code false} if all players see the scoreboard.
     */
    boolean isHidden();
    
    /**
     * Hides the scoreboard except for players on the whitelist.
     */
    void hide();
    
    /**
     * Shows scoreboard for all players.
     */
    void show();
    
}
