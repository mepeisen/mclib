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

package de.minigameslib.mclib.impl.items;

import org.bukkit.Material;

/**
 * @author mepeisen
 *
 */
enum CustomItemTypes
{
    /**
     * Diamond Axe items
     */
    DiamondAxe(Material.DIAMOND_AXE, "models/item/diamond_axe.json", "item/handheld", "item/diamond_axe", "items/diamond_axe", DiamondDurability.VALUES), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    
    /**
     * Diamond Hoe items
     */
    DiamondHoe(Material.DIAMOND_HOE, "models/item/diamond_hoe.json", "item/handheld", "item/diamond_hoe", "items/diamond_hoe", DiamondDurability.VALUES), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    
    /**
     * Diamond Pickaxe items
     */
    DiamondPickaxe(Material.DIAMOND_PICKAXE, "models/item/diamond_pickaxe.json", "item/handheld", "item/diamond_pickaxe", "items/diamond_pickaxe", DiamondDurability.VALUES), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    
    /**
     * Diamond Shovel items
     */
    DiamondShovel(Material.DIAMOND_SPADE, "models/item/diamond_shovel.json", "item/handheld", "item/diamond_shovel", "items/diamond_shovel", DiamondDurability.VALUES), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    ;
    
    /** models filename */
    private final String       modelsFilename;
    
    /** parent */
    private final String       parent;
    
    /** default model */
    private final String       defaultModel;
    
    /** default texture */
    private final String       defaultTexture;
    
    /** durability */
    private final Durability[] durabilities;
    
    /** the material. */
    private final Material     material;
    
    /**
     * @param material
     * @param modelsFilename
     * @param parent
     * @param defaultModel
     * @param defaultTexture
     * @param durabilities
     */
    private CustomItemTypes(Material material, String modelsFilename, String parent, String defaultModel, String defaultTexture, Durability[] durabilities)
    {
        this.modelsFilename = modelsFilename;
        this.parent = parent;
        this.defaultModel = defaultModel;
        this.defaultTexture = defaultTexture;
        this.durabilities = durabilities;
        this.material = material;
    }
    
    /**
     * @return models file name
     */
    public String getModelsFilename()
    {
        return this.modelsFilename;
    }
    
    /**
     * @return parent
     */
    public String getParent()
    {
        return this.parent;
    }
    
    /**
     * @return default model
     */
    public String getDefaultModel()
    {
        return this.defaultModel;
    }
    
    /**
     * @return default texture
     */
    public String getDefaultTexture()
    {
        return this.defaultTexture;
    }
    
    /**
     * @return durabilities
     */
    public Durability[] getDurabilities()
    {
        return this.durabilities;
    }
    
    /**
     * @return the material
     */
    public Material getMaterial()
    {
        return this.material;
    }
    
}
