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
 * @author mepeisen
 *
 */
public interface EntityHelperInterface
{
    
    /**
     * Spawns a dummy villager, not being able to move around
     * @param loc
     * @param profession
     * @return villager
     */
    Villager spawnDummyVillager(Location loc, Profession profession);
    
    /**
     * Spawns a dummy human, not being able to move around
     * @param loc
     * @param name
     * @param skinTexture
     * @return human
     */
    HumanEntity spawnDummyHuman(Location loc, String name, String skinTexture);

    /**
     * @param entity
     * @return string texture
     */
    String getSkin(HumanEntity entity);
    
    /**
     * Loads skin from players
     * @param player
     * @return skin textures
     * @throws ExecutionException
     */
    String loadSkinTexture(Player player) throws ExecutionException;
    
    /**
     * Sets skin
     * @param entity
     * @param texture
     */
    void setSkin(HumanEntity entity, String texture);
    
    /**
     * Sets profile in skull meta data with given textures.
     * @param meta
     * @param textures
     */
    void setProfileWithSkull(SkullMeta meta, String textures);
    
    /**
     * Updates visibility list for given player; hides dummy humans
     * @param player
     */
    void updateVisibilityList(Player player);

    /**
     * @param entity
     */
    void delete(Villager entity);

    /**
     * @param entity
     */
    void delete(HumanEntity entity);
    
}
