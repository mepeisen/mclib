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

package de.minigames.mclib.nms.v185.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import de.minigameslib.mclib.nms.api.NmsDropRuleInterface;
import de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockStateEnum;
import net.minecraft.server.v1_8_R3.BlockStateList;
import net.minecraft.server.v1_8_R3.CreativeModeTab;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.IBlockState;
import net.minecraft.server.v1_8_R3.INamable;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.World;

/**
 * @author mepeisen
 *
 */
public class CustomBlock extends Block
{
    // the block variant
    public static final BlockStateEnum<EnumCustomVariant> VARIANT = BlockStateEnum.of("variant", EnumCustomVariant.class); //$NON-NLS-1$

    public CustomBlock() {
        super(Material.STONE);
        j(this.blockStateList.getBlockData().set(VARIANT, EnumCustomVariant.VARIANT_0));
        a(CreativeModeTab.b);
        a(i); // stone
    }
    
    /** the nms drop rule. */
    private NmsDropRuleInterface dropRule;

    
    private Map<Integer, NmsInventoryHandlerInterface> inventoryHandler = new HashMap<>();
    
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
            net.minecraft.server.v1_8_R3.EntityLiving entityliving, net.minecraft.server.v1_8_R3.ItemStack itemstack)
    {
        final NmsInventoryHandlerInterface handler = this.inventoryHandler.get(this.toLegacyData(iblockdata));
        if (handler != null)
        {
            handler.onPostPlace(new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ()),
                    CraftItemStack.asCraftMirror(itemstack),
                    entityliving instanceof EntityPlayer ? ((EntityPlayer)entityliving).getBukkitEntity() : null);
        }
        super.postPlace(world, blockposition, iblockdata, entityliving, itemstack);
    }
    
    @Override
    public void remove(World paramWorld, BlockPosition paramBlockPosition, IBlockData paramIBlockData) {
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
            net.minecraft.server.v1_8_R3.EntityHuman paramEntityHuman, net.minecraft.server.v1_8_R3.EnumDirection paramEnumDirection, float paramFloat1,
            float paramFloat2, float paramFloat3) {

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
        return super.interact(paramWorld, paramBlockPosition, paramIBlockData, paramEntityHuman, paramEnumDirection, paramFloat1, paramFloat2, paramFloat3);
    }
    
    /**
     * @param paramEnumDirection
     * @return block face
     */
    private BlockFace toBlockFace(net.minecraft.server.v1_8_R3.EnumDirection paramEnumDirection)
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
    public void setMeta(float hardness, float resistence, NmsDropRuleInterface dropRule)
    {
        this.c(hardness);
        this.b(resistence);
        this.dropRule = dropRule;
    }

    @Override
    public net.minecraft.server.v1_8_R3.Item getDropType(IBlockData iblockdata, Random random, int i) {
        return this.dropRule == null ? super.getDropType(iblockdata, random, i) : net.minecraft.server.v1_8_R3.Item.getById(this.dropRule.getDropType(this.toLegacyData(iblockdata), random, i));
    }

    @Override
    public int getDropCount(int i, Random random) {
        return this.dropRule == null ? super.getDropCount(i, random) : this.dropRule.getDropCount(random, i);
    }

    @Override
    public int getExpDrop(net.minecraft.server.v1_8_R3.World world, IBlockData data, int i) {
        return this.dropRule == null ? super.getExpDrop(world, data, i) : this.dropRule.getExpDrop(this.toLegacyData(data), world.random, i);
    }

    @Override
    public int getDropData(IBlockData paramIBlockData) {
        return this.dropRule == null ? super.getDropData(paramIBlockData) : this.dropRule.getDropVariant(this.toLegacyData(paramIBlockData));
    }

    public IBlockData fromLegacyData(int paramInt) {
        return getBlockData().set(VARIANT, EnumCustomVariant.values()[paramInt]);
    }

    public int toLegacyData(IBlockData paramIBlockData) {
        return ((EnumCustomVariant) paramIBlockData.get(VARIANT)).ordinal();
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { VARIANT });
    }
    
    public enum EnumCustomVariant implements INamable {
        VARIANT_0, VARIANT_1, VARIANT_2, VARIANT_3, VARIANT_4, VARIANT_5, VARIANT_6, VARIANT_7, VARIANT_8, VARIANT_9, VARIANT_10, VARIANT_11, VARIANT_12, VARIANT_13, VARIANT_14, VARIANT_15;

        public int a() {
            return this.ordinal();
        }

        public String toString() {
            return this.name().toLowerCase();
        }

        public String getName() {
            return this.toString();
        }

        public String c() {
            return this.toString();
        }
    }
    
}
