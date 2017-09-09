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

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockInventoryMeta;
import de.minigameslib.mclib.api.items.BlockInventoryMeta.BlockInventoryInterface;
import de.minigameslib.mclib.api.items.BlockVariantId;
import de.minigameslib.mclib.api.items.InventoryId;
import de.minigameslib.mclib.api.items.InventoryServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface;

/**
 * Implementation of nms inventory callback handler.
 * 
 * @author mepeisen
 *
 */
public class NmsInventoryHandler implements NmsInventoryHandlerInterface
{
    
    /** the initial size. */
    private int                     initialSize;
    
    /** block id. */
    private BlockId                 blockId;
    /** block variant id. */
    private BlockVariantId          variantId;
    
    /** fixed inventory size. */
    private boolean                 fixed;
    
    /** shared inventory. */
    private boolean                 shared;
    
    /** helper interface. */
    private BlockInventoryInterface helper;
    
    /**
     * Constructor.
     * 
     * @param blockId
     *            block to be used for inventories
     * @param variantId
     *            variant to be used for inventories
     * @param variantInv
     *            annotation data for this inventory
     */
    public NmsInventoryHandler(BlockId blockId, BlockVariantId variantId, BlockInventoryMeta variantInv)
    {
        this.blockId = blockId;
        this.variantId = variantId;
        this.initialSize = variantInv.size();
        this.fixed = variantInv.fixed();
        this.shared = variantInv.shared();
        try
        {
            this.helper = variantInv.blockInventory().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new IllegalStateException("Unable to create block inventory helper", e); //$NON-NLS-1$
        }
    }
    
    @Override
    public void onPlace(Location location)
    {
        // do nothing for the moment, everything is done in postPlace
    }
    
    @Override
    public void onPostPlace(Location location, ItemStack stack, Player player)
    {
        final McPlayerInterface mcplayer = ObjectServiceInterface.instance().getPlayer(player);
        try
        {
            this.helper.createInventory(this.blockId, this.variantId, location, mcplayer, this.initialSize, this.fixed, this.shared);
            this.helper.getInventory(this.blockId, this.variantId, location, mcplayer);
        }
        catch (McException e)
        {
            mcplayer.sendMessage(e.getErrorMessage(), e.getArgs());
        }
        
    }
    
    @Override
    public void onBreak(Location location)
    {
        this.helper.onBreak(this.blockId, this.variantId, location);
    }
    
    @Override
    public boolean onInteract(Location location, HumanEntity bukkitEntity, boolean mainHand, BlockFace blockFace, float hitX, float hitY, float hitZ)
    {
        if (bukkitEntity instanceof Player)
        {
            final McPlayerInterface mcplayer = ObjectServiceInterface.instance().getPlayer((Player) bukkitEntity);
            try
            {
                InventoryId inv = this.helper.getInventory(this.blockId, this.variantId, location, mcplayer);
                if (inv != null)
                {
                    InventoryServiceInterface.instance().getInventory(inv).openInventory(mcplayer);
                }
            }
            catch (McException e)
            {
                mcplayer.sendMessage(e.getErrorMessage(), e.getArgs());
            }
        }
        return true;
    }
    
}
