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

package de.minigames.mclib.nms.v194.blocks;

import net.minecraft.server.v1_9_R2.Block;
import net.minecraft.server.v1_9_R2.BlockStateEnum;
import net.minecraft.server.v1_9_R2.BlockStateList;
import net.minecraft.server.v1_9_R2.CreativeModeTab;
import net.minecraft.server.v1_9_R2.IBlockData;
import net.minecraft.server.v1_9_R2.IBlockState;
import net.minecraft.server.v1_9_R2.INamable;
import net.minecraft.server.v1_9_R2.Material;

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
        w(this.blockStateList.getBlockData().set(VARIANT, EnumCustomVariant.VARIANT_0));
        a(CreativeModeTab.b);
    }

    public int getDropData(IBlockData paramIBlockData) {
        return ((EnumCustomVariant) paramIBlockData.get(VARIANT)).ordinal();
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