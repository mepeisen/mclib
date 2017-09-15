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

package de.minigameslib.mclib.nms.v112.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import de.minigameslib.mclib.nms.api.NmsDropRuleInterface;
import de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface;
import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.BlockStateEnum;
import net.minecraft.server.v1_12_R1.BlockStateList;
import net.minecraft.server.v1_12_R1.CreativeModeTab;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.IBlockState;
import net.minecraft.server.v1_12_R1.INamable;
import net.minecraft.server.v1_12_R1.Material;
import net.minecraft.server.v1_12_R1.SoundEffectType;
import net.minecraft.server.v1_12_R1.World;

/**
 * Custom block implementation for modded blocks.
 * 
 * @author mepeisen
 *
 */
public class CustomBlock extends Block
{
    /** the block variants. */
    public static final BlockStateEnum<EnumCustomVariant> VARIANT = BlockStateEnum.of("variant", EnumCustomVariant.class); //$NON-NLS-1$
    
    /**
     * Constructor.
     */
    public CustomBlock()
    {
        super(Material.STONE);
        w(this.blockStateList.getBlockData().set(VARIANT, EnumCustomVariant.VARIANT_0));
        a(CreativeModeTab.b);
        a(SoundEffectType.d); // stone
    }
    
    /** the nms drop rule. */
    private NmsDropRuleInterface                       dropRule;
    
    /** the inventory handler for inventory blocks. */
    private Map<Integer, NmsInventoryHandlerInterface> inventoryHandler = new HashMap<>();
    
    /**
     * Sets the inventory handler.
     * 
     * @param variant
     *            target variant for inventory handler
     * @param inventory
     *            inventory handler
     */
    public void setInventoryHandler(int variant, NmsInventoryHandlerInterface inventory)
    {
        this.inventoryHandler.put(variant, inventory);
    }
    
    @Override
    public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata)
    {
        final NmsInventoryHandlerInterface handler = this.inventoryHandler.get(this.toLegacyData(iblockdata));
        if (handler != null)
        {
            handler.onPlace(new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ()));
        }
        super.onPlace(world, blockposition, iblockdata);
    }
    
    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata,
        net.minecraft.server.v1_12_R1.EntityLiving entityliving, net.minecraft.server.v1_12_R1.ItemStack itemstack)
    {
        final NmsInventoryHandlerInterface handler = this.inventoryHandler.get(this.toLegacyData(iblockdata));
        if (handler != null)
        {
            handler.onPostPlace(new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ()),
                CraftItemStack.asCraftMirror(itemstack),
                entityliving instanceof EntityPlayer ? ((EntityPlayer) entityliving).getBukkitEntity() : null);
        }
        super.postPlace(world, blockposition, iblockdata, entityliving, itemstack);
    }
    
    @Override
    public void remove(World paramWorld, BlockPosition paramBlockPosition, IBlockData paramIBlockData)
    {
        // break block
        super.remove(paramWorld, paramBlockPosition, paramIBlockData);
        final NmsInventoryHandlerInterface handler = this.inventoryHandler.get(this.toLegacyData(paramIBlockData));
        if (handler != null)
        {
            handler.onBreak(new Location(paramWorld.getWorld(), paramBlockPosition.getX(), paramBlockPosition.getY(), paramBlockPosition.getZ()));
        }
    }
    
    @Override
    public boolean interact(World paramWorld, BlockPosition paramBlockPosition, IBlockData paramIBlockData,
        net.minecraft.server.v1_12_R1.EntityHuman paramEntityHuman, net.minecraft.server.v1_12_R1.EnumHand paramEnumHand, net.minecraft.server.v1_12_R1.EnumDirection paramEnumDirection,
        float paramFloat1,
        float paramFloat2, float paramFloat3)
    {
        
        final NmsInventoryHandlerInterface handler = this.inventoryHandler.get(this.toLegacyData(paramIBlockData));
        if (handler != null)
        {
            return handler.onInteract(new Location(paramWorld.getWorld(), paramBlockPosition.getX(), paramBlockPosition.getY(), paramBlockPosition.getZ()),
                paramEntityHuman.getBukkitEntity(),
                true,
                toBlockFace(paramEnumDirection),
                paramFloat1,
                paramFloat2,
                paramFloat3);
        }
        return super.interact(paramWorld, paramBlockPosition, paramIBlockData, paramEntityHuman, paramEnumHand, paramEnumDirection, paramFloat1, paramFloat2, paramFloat3);
    }
    
    /**
     * Converts nms direction to bukkit block face.
     * 
     * @param paramEnumDirection
     *            nms direction
     * @return block face
     */
    private BlockFace toBlockFace(net.minecraft.server.v1_12_R1.EnumDirection paramEnumDirection)
    {
        switch (paramEnumDirection)
        {
            case DOWN:
                return BlockFace.DOWN;
            case EAST:
                return BlockFace.EAST;
            case NORTH:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
            case UP:
                return BlockFace.UP;
            case WEST:
                return BlockFace.WEST;
            default:
                return null;
        }
    }
    
    /**
     * Sets block meta data.
     * 
     * @param hardness
     *            block hardness
     * @param resistence
     *            block resistence
     * @param dropRule
     *            drop rule
     */
    public void setMeta(float hardness, float resistence, NmsDropRuleInterface dropRule)
    {
        this.c(hardness);
        this.b(resistence);
        this.dropRule = dropRule;
    }
    
    @Override
    public net.minecraft.server.v1_12_R1.Item getDropType(IBlockData iblockdata, Random random, int i)
    {
        return this.dropRule == null ? super.getDropType(iblockdata, random, i) : net.minecraft.server.v1_12_R1.Item.getById(this.dropRule.getDropType(this.toLegacyData(iblockdata), random, i));
    }
    
    @Override
    public int getDropCount(int i, Random random)
    {
        return this.dropRule == null ? super.getDropCount(i, random) : this.dropRule.getDropCount(random, i);
    }
    
    @Override
    public int getExpDrop(World world, IBlockData data, int i)
    {
        return this.dropRule == null ? super.getExpDrop(world, data, i) : this.dropRule.getExpDrop(this.toLegacyData(data), world.random, i);
    }
    
    @Override
    public int getDropData(IBlockData paramIBlockData)
    {
        return this.dropRule == null ? super.getDropData(paramIBlockData) : this.dropRule.getDropVariant(this.toLegacyData(paramIBlockData));
    }
    
    @Override
    public IBlockData fromLegacyData(int paramInt)
    {
        return getBlockData().set(VARIANT, EnumCustomVariant.values()[paramInt]);
    }
    
    @Override
    public int toLegacyData(IBlockData paramIBlockData)
    {
        return paramIBlockData.get(VARIANT).ordinal();
    }
    
    @Override
    protected BlockStateList getStateList()
    {
        return new BlockStateList(this, new IBlockState[] { VARIANT });
    }
    
    /**
     * generic block variants.
     * 
     * @author mepeisen
     *
     */
    public enum EnumCustomVariant implements INamable
    {
        /** variant #0. */
        VARIANT_0,
        /** variant #1. */
        VARIANT_1,
        /** variant #2. */
        VARIANT_2,
        /** variant #3. */
        VARIANT_3,
        /** variant #4. */
        VARIANT_4,
        /** variant #5. */
        VARIANT_5,
        /** variant #6. */
        VARIANT_6,
        /** variant #7. */
        VARIANT_7,
        /** variant #8. */
        VARIANT_8,
        /** variant #9. */
        VARIANT_9,
        /** variant #10. */
        VARIANT_10,
        /** variant #11. */
        VARIANT_11,
        /** variant #12. */
        VARIANT_12,
        /** variant #13. */
        VARIANT_13,
        /** variant #14. */
        VARIANT_14,
        /** variant #15. */
        VARIANT_15;
        
        @Override
        public String toString()
        {
            return this.name().toLowerCase();
        }
        
        @Override
        public String getName()
        {
            return this.toString();
        }
        
    }
    
}
