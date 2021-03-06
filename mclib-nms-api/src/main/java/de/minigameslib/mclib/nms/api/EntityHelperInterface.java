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

package de.minigameslib.mclib.nms.api;

import java.util.concurrent.ExecutionException;

import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * NMS Helper for entites.
 * 
 * @author mepeisen
 *
 */
public interface EntityHelperInterface
{
    
    /**
     * Spawns a dummy villager, not being able to move around.
     * 
     * @param loc
     *            spawn location
     * @param profession
     *            villager profession
     * @return villager
     */
    Villager spawnDummyVillager(Location loc, Profession profession);
    
    /**
     * checks if given villager was created by method {@link #spawnDummyVillager(Location, Profession)}.
     * 
     * @param villager
     *            villager to test
     * @return {@code true} for dummy villager
     */
    boolean isDummyVillager(Villager villager);
    
    /**
     * Spawns a dummy human, not being able to move around.
     * 
     * @param loc
     *            spawn location.
     * @param name
     *            humans name
     * @param skinTexture
     *            the texture to use
     * @return human
     */
    HumanEntity spawnDummyHuman(Location loc, String name, String skinTexture);
    
    /**
     * checks if given human was created by method {@link #spawnDummyHuman(Location, String, String)}.
     * 
     * @param human
     *            human to test
     * @return {@code true} for dummy human
     */
    boolean isDummyHuman(HumanEntity human);
    
    /**
     * Returns skin from entity.
     * 
     * @param entity
     *            target entity
     * @return string texture
     */
    String getSkin(HumanEntity entity);
    
    /**
     * Loads skin from players.
     * 
     * @param player
     *            target player.
     * @return skin textures
     * @throws ExecutionException
     *             thrown if there was a problem fetching the skin
     */
    String loadSkinTexture(Player player) throws ExecutionException;
    
    /**
     * Sets skin.
     * 
     * @param entity
     *            target entity
     * @param texture
     *            skin texture.
     */
    void setSkin(HumanEntity entity, String texture);
    
    /**
     * Sets profile in skull meta data with given textures.
     * 
     * @param meta
     *            skull meta data
     * @param textures
     *            skin texture
     */
    void setProfileWithSkull(SkullMeta meta, String textures);
    
    /**
     * Updates visibility list for given player; hides dummy humans.
     * 
     * @param player
     *            target player.
     */
    void playerOnline(Player player);
    
    /**
     * Updates visibility list for given player; hides dummy humans.
     * 
     * @param player
     *            target player
     */
    void playerOffline(Player player);
    
    /**
     * Deletes given villager that was created by {@link #spawnDummyVillager(Location, Profession)}.
     * 
     * @param entity
     *            entity to be deleted
     */
    void delete(Villager entity);
    
    /**
     * Deletes given entity that was created by {@link #spawnDummyHuman(Location, String, String)}.
     * 
     * @param entity
     *            entity to be deleted
     */
    void delete(HumanEntity entity);
    
    /**
     * Clears skin cache for given player.
     * 
     * @param player
     *            target player
     */
    void clearSkinCache(Player player);
    
}
