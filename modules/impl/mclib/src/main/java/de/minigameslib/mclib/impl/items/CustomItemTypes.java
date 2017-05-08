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
 * The custom item types to be used for non-modded items.
 * 
 * @author mepeisen
 *
 */
enum CustomItemTypes
{
    /**
     * Diamond Axe items.
     */
    DiamondAxe(
        Material.DIAMOND_AXE,
        "models/item/diamond_axe.json", //$NON-NLS-1$
        "item/handheld", //$NON-NLS-1$
        "item/diamond_axe", //$NON-NLS-1$
        "items/diamond_axe", //$NON-NLS-1$
        DiamondDurability.VALUES),
    
    /**
     * Diamond Hoe items.
     */
    DiamondHoe(
        Material.DIAMOND_HOE,
        "models/item/diamond_hoe.json", //$NON-NLS-1$
        "item/handheld", //$NON-NLS-1$
        "item/diamond_hoe", //$NON-NLS-1$
        "items/diamond_hoe", //$NON-NLS-1$
        DiamondDurability.VALUES),
    
    /**
     * Diamond Pickaxe items.
     */
    DiamondPickaxe(
        Material.DIAMOND_PICKAXE,
        "models/item/diamond_pickaxe.json", //$NON-NLS-1$
        "item/handheld", //$NON-NLS-1$
        "item/diamond_pickaxe", //$NON-NLS-1$
        "items/diamond_pickaxe", //$NON-NLS-1$
        DiamondDurability.VALUES),
    
    /**
     * Diamond Shovel items.
     */
    DiamondShovel(
        Material.DIAMOND_SPADE,
        "models/item/diamond_shovel.json", //$NON-NLS-1$
        "item/handheld", //$NON-NLS-1$
        "item/diamond_shovel", //$NON-NLS-1$
        "items/diamond_shovel", //$NON-NLS-1$
        DiamondDurability.VALUES),
        ;
    
    /** models filename. */
    private final String       modelsFilename;
    
    /** parent. */
    private final String       parent;
    
    /** default model. */
    private final String       defaultModel;
    
    /** default texture. */
    private final String       defaultTexture;
    
    /** durability. */
    private final Durability[] durabilities;
    
    /** the material. */
    private final Material     material;
    
    /**
     * Constructor.
     * 
     * @param material
     *            bukkit material to be used
     * @param modelsFilename
     *            the models file name
     * @param parent
     *            the models parent
     * @param defaultModel
     *            the default model
     * @param defaultTexture
     *            the texture to use
     * @param durabilities
     *            the possible durabilities
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
     * Returns the model filenames.
     * 
     * @return models file name
     */
    public String getModelsFilename()
    {
        return this.modelsFilename;
    }
    
    /**
     * Returns the models parent.
     * 
     * @return parent
     */
    public String getParent()
    {
        return this.parent;
    }
    
    /**
     * Returns the default model.
     * 
     * @return default model
     */
    public String getDefaultModel()
    {
        return this.defaultModel;
    }
    
    /**
     * Returns the default texture.
     * 
     * @return default texture
     */
    public String getDefaultTexture()
    {
        return this.defaultTexture;
    }
    
    /**
     * Returns the possible durabilities.
     * 
     * @return durabilities
     */
    public Durability[] getDurabilities()
    {
        return this.durabilities;
    }
    
    /**
     * Returns the bukkit material.
     * 
     * @return the material
     */
    public Material getMaterial()
    {
        return this.material;
    }
    
}
