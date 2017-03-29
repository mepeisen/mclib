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

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;

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
     * Creates a new item stack for given item id and servers default localization
     * @param item item id
     * @return item stack
     */
    ItemStack createItem(ItemId item);
    
    /**
     * Creates a new item stack for given item id
     * @param player the player used for localization
     * @param item item id
     * @return item stack
     */
    ItemStack createItem(McPlayerInterface player, ItemId item);
    
    /**
     * Creates a new item stack for given item id
     * @param item item id
     * @param name the custom name (already localized)
     * @return item stack
     */
    ItemStack createItem(ItemId item, String name);
    
    /**
     * Creates a new item stack for given item id
     * @param item item id
     * @param name the custom name
     * @param nameArgs
     * @return item stack
     */
    ItemStack createItem(ItemId item, LocalizedMessageInterface name, Serializable... nameArgs);
    
    /**
     * Creates a new item stack for given item id
     * @param player the player used for localization
     * @param item item id
     * @param name the custom name
     * @param nameArgs the name arguments
     * @return item stack
     */
    ItemStack createItem(McPlayerInterface player, ItemId item, LocalizedMessageInterface name, Serializable... nameArgs);
    
    /**
     * Returns the display name
     * @param stack
     * @return display name
     */
    String getDisplayName(ItemStack stack);
    
    /**
     * Returns the description
     * @param stack
     * @return description
     */
    String[] getDescription(ItemStack stack);
    
    /**
     * Sets display name of given item stack
     * @param stack stack
     * @param name display name (already localized)
     */
    void setDisplayName(ItemStack stack, String name);
    
    /**
     * Sets display name of given item stack
     * @param stack stack
     * @param player the player used for localization
     * @param name display name
     * @param nameArgs the name arguments
     */
    void setDisplayName(ItemStack stack, McPlayerInterface player, LocalizedMessageInterface name, Serializable... nameArgs);
    
    /**
     * Sets display name of given item stack
     * @param stack stack
     * @param description description(already localized)
     */
    void setDescription(ItemStack stack, String[] description);
    
    /**
     * Sets display name of given item stack
     * @param stack stack
     * @param player the player used for localization
     * @param description description
     * @param descriptionArgs the description arguments
     */
    void setDescription(ItemStack stack, McPlayerInterface player, LocalizedMessageInterface description, Serializable... descriptionArgs);
    
    /**
     * Creates a new item tooling for given item id; a tooling is a single item used by players. Once used a code handler is invoked.
     * @param item item id
     * @param player the player getting the item
     * @param title the items display name
     * @param titleArgs arguments for itemms display name
     * @return item builder
     */
    ToolBuilderInterface prepareTool(ItemId item, McPlayerInterface player, LocalizedMessageInterface title, Serializable... titleArgs);
    
    /**
     * the item builder
     */
    public interface ToolBuilderInterface
    {
        
        /**
         * Builds the item and adds it to player inventory
         */
        void build();
        
        /**
         * Left click handler
         * @param handler
         * @return this object for chaining
         */
        ToolBuilderInterface onLeftClick(McBiConsumer<McPlayerInterface, McPlayerInteractEvent> handler);
        
        /**
         * Right click handler
         * @param handler
         * @return this object for chaining
         */
        ToolBuilderInterface onRightClick(McBiConsumer<McPlayerInterface, McPlayerInteractEvent> handler);
                
        /**
         * sets description
         * @param description
         * @param args
         * @return this object for chaining
         */
        ToolBuilderInterface description(LocalizedMessageInterface description, Serializable... args);
        
        /**
         * marks this tool for single use; destroys it on successful use
         * @return this object for chaining
         */
        ToolBuilderInterface singleUse();
        
    }
    
    /**
     * Returns item id from item stack
     * @param stack
     * @return item id or {@code null} if stack does not represent custom items
     */
    ItemId getItemId(ItemStack stack);
    
}
