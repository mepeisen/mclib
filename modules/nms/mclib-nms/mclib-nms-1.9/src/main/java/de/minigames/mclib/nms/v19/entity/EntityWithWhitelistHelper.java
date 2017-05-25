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

package de.minigames.mclib.nms.v19.entity;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McLibInterface;

/**
 * An abstract entity helper taht supports visibility whitelists.
 * 
 * @author mepeisen
 * @param <T>
 *            entity helper class
 */
public class EntityWithWhitelistHelper<T extends AbstractWhitelistableEntityHelper>
{
    
    /** dummy humans. */
    private final Set<T> entities = new HashSet<>();
    
    /**
     * Adds a new entity.
     * 
     * @param entity
     *            entity that was created.
     */
    public void add(T entity)
    {
        this.entities.add(entity);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) McLibInterface.instance(), new Runnable() {
            
            @Override
            public void run()
            {
                Bukkit.getOnlinePlayers().forEach(entity::track);
            }
        }, 10);
    }
    
    /**
     * Deletes given entity.
     * 
     * @param entity
     *            entity to be deleted.
     */
    public void delete(T entity)
    {
        this.entities.remove(entity);
        entity.delete();
    }
    
    /**
     * Player gets online.
     * 
     * @param player
     *            the player that gets online.
     */
    public void playerOnline(Player player)
    {
        for (final T human : this.entities)
        {
            human.track(player);
        }
    }
    
    /**
     * Player gets offline.
     * 
     * @param player
     *            the player that gets offline.
     */
    public void playerOffline(Player player)
    {
        for (final T human : this.entities)
        {
            human.untrack(player);
        }
    }
    
}
