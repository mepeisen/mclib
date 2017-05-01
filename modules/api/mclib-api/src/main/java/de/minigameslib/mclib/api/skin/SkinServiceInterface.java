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

package de.minigameslib.mclib.api.skin;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.shared.api.com.DataFragment;

/**
 * Helper for skin support.
 * 
 * @author mepeisen
 */
public interface SkinServiceInterface
{
    
    /**
     * Returns the skin services instance.
     * 
     * @return skin services instance.
     */
    static SkinServiceInterface instance()
    {
        return SkinServiceCache.get();
    }
    
    /**
     * Load skin from data section.
     * 
     * @param section
     *            the data section to load from
     * @param key
     *            data key
     * @return skin
     * @throws McException
     *             thrown if data section does not contain a saved skin
     */
    SkinInterface load(DataFragment section, String key) throws McException;
    
    /**
     * Saves skin to data section.
     * 
     * @param skin
     *            the skin to save
     * @param section
     *            the data section to save to
     * @param key
     *            data key
     * @throws McException
     *             thrown on errors
     */
    void save(SkinInterface skin, DataFragment section, String key) throws McException;
    
    /**
     * Returns the skin for given player.
     * 
     * @param player
     *            target player
     * @return player skin
     */
    SkinInterface get(McPlayerInterface player);
    
    /**
     * Returns the skin for given human entity.
     * 
     * @param entity
     *            target entity
     * @return skin from human
     * @throws McException
     *             thrown if given entity is not a human
     */
    SkinInterface getFromHuman(EntityInterface entity) throws McException;
    
    /**
     * Returns the skin snapshot, freezing the skin.
     * 
     * @param skin
     *            skin
     * @param completion
     *            completion function
     */
    void getSkinSnapshot(SkinInterface skin, McConsumer<SkinInterface> completion);
    
    /**
     * Returns the skin snapshot, freezing the skin.
     * 
     * @param player
     *            target player
     * @param completion
     *            completion function
     */
    void getSkinSnapshot(McPlayerInterface player, McConsumer<SkinInterface> completion);
    
    /**
     * Sets a sking to human entity.
     * 
     * @param entity
     *            target entity
     * @param skin
     *            skin to set
     * @throws McException
     *             thrown if given entity is not a human
     */
    void setToHuman(EntityInterface entity, SkinInterface skin) throws McException;
    
    /**
     * Clears the skin cache for given player; be careful using this, because the Mohjang API does not allow skin queries too often.
     * 
     * @param player
     *            target player
     */
    void clearSkinCache(McPlayerInterface player);
    
    /**
     * Returns an item stack representing a skull for given skin.
     * 
     * @param skin
     *            the skin to use
     * @param name
     *            the item name ot use
     * @return skull
     */
    ItemStack getSkull(SkinInterface skin, String name);
    
}
