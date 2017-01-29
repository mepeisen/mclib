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

package de.minigameslib.mclib.api.items;

import java.io.File;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * A service to register custom items.
 * 
 * @author mepeisen
 */
public interface ItemServiceInterface
{
    
    /**
     * Returns the item services instance.
     * 
     * @return item services instance.
     */
    static ItemServiceInterface instance()
    {
        return ItemServiceCache.get();
    }
    
    /**
     * Changes the resource pack download url.
     * @param url
     */
    void setDownloadUrl(String url);
    
    /**
     * Returns the download url.
     * @return download url.
     */
    String getDownloadUrl();
    
    /**
     * Checks if the player has accepted and installed the resource pack.
     * @param player 
     * @return {@code true} if player has installed the resource pack.
     */
    boolean hasResourcePack(McPlayerInterface player);
    
    /**
     * Creates a new item stack for given item id
     * @param item item id
     * @param name the custom name (already localized)
     * @return item stack
     */
    ItemStack createItem(ItemId item, String name);
    
    /**
     * Returns item id from item stack
     * @param stack
     * @return item id or {@code null} if stack does not represent custom items
     */
    ItemId getItemId(ItemStack stack);
    
    /**
     * Creates a resource pack file for all installed items.
     * @param target
     * @throws IOException
     */
    void createResourcePack(File target) throws IOException;
    
}
