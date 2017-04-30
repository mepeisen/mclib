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

package de.minigameslib.mclib.client.nms;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author mepeisen
 *
 */
public class MyBlock extends Block
{
    
    public static final PropertyEnum<MyBlock.EnumType> VARIANT = PropertyEnum.<MyBlock.EnumType>create("variant", MyBlock.EnumType.class);

    private String name;

    /**
     */
    protected MyBlock(String name)
    {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, MyBlock.EnumType.VARIANT_0));
        setUnlocalizedName(name);
        setRegistryName(name);
        this.name = name;
        // TODO this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    private final Map<Integer, Boolean> opaque = new HashMap<>();
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        final Boolean result = this.opaque.get(this.getMetaFromState(state));
        if (result == null)
        {
            return super.isOpaqueCube(state);
        }
        return result;
    }

    public void setOpaqueCube(int meta, boolean isOpaque)
    {
        this.opaque.put(meta, isOpaque);
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return I18n.translateToLocal(this.getUnlocalizedName() + "." + MyBlock.EnumType.VARIANT_0.getUnlocalizedName() + ".name");
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return ((MyBlock.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (MyBlock.EnumType blockstone$enumtype : MyBlock.EnumType.values())
        {
            list.add(new ItemStack(itemIn, 1, blockstone$enumtype.getMetadata()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, MyBlock.EnumType.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((MyBlock.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    public static enum EnumType implements IStringSerializable
    {
        VARIANT_0,
        VARIANT_1,
        VARIANT_2,
        VARIANT_3,
        VARIANT_4,
        VARIANT_5,
        VARIANT_6,
        VARIANT_7,
        VARIANT_8,
        VARIANT_9,
        VARIANT_10,
        VARIANT_11,
        VARIANT_12,
        VARIANT_13,
        VARIANT_14,
        VARIANT_15;

        /**
         * Returns the EnumType's metadata value.
         */
        public int getMetadata()
        {
            return this.ordinal();
        }

        public String toString()
        {
            return this.name().toLowerCase();
        }

        /**
         * Returns an EnumType for the BlockState from a metadata value.
         */
        public static MyBlock.EnumType byMetadata(int meta)
        {
            return values()[meta];
        }

        public String getName()
        {
            return this.name().toLowerCase();
        }

        public String getUnlocalizedName()
        {
            return this.name().toLowerCase();
        }
        
    }
    
}
