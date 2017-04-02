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

package de.minigames.mclib.nms.v18.blocks;

import java.util.Random;

import de.minigameslib.mclib.nms.api.NmsDropRuleInterface;
import net.minecraft.server.v1_8_R1.Block;
import net.minecraft.server.v1_8_R1.BlockStateEnum;
import net.minecraft.server.v1_8_R1.BlockStateList;
import net.minecraft.server.v1_8_R1.CreativeModeTab;
import net.minecraft.server.v1_8_R1.IBlockData;
import net.minecraft.server.v1_8_R1.IBlockState;
import net.minecraft.server.v1_8_R1.INamable;
import net.minecraft.server.v1_8_R1.Material;

/**
 * @author mepeisen
 *
 */
public class CustomBlock extends Block
{
    // the block variant
    public static final BlockStateEnum VARIANT = BlockStateEnum.of("variant", EnumCustomVariant.class); //$NON-NLS-1$

    public CustomBlock() {
        super(Material.STONE);
        j(this.blockStateList.getBlockData().set(VARIANT, EnumCustomVariant.VARIANT_0));
        a(CreativeModeTab.b);
        a(i); // stone
    }
    
    /** the nms drop rule. */
    private NmsDropRuleInterface dropRule;
    
    public void setMeta(float hardness, float resistence, NmsDropRuleInterface dropRule)
    {
        this.c(hardness);
        this.b(resistence);
        this.dropRule = dropRule;
    }

    @Override
    public net.minecraft.server.v1_8_R1.Item getDropType(IBlockData iblockdata, Random random, int i) {
        return this.dropRule == null ? super.getDropType(iblockdata, random, i) : net.minecraft.server.v1_8_R1.Item.getById(this.dropRule.getDropType(this.toLegacyData(iblockdata), random, i));
    }

    @Override
    public int getDropCount(int i, Random random) {
        return this.dropRule == null ? super.getDropCount(i, random) : this.dropRule.getDropCount(random, i);
    }

    @Override
    public int getExpDrop(net.minecraft.server.v1_8_R1.World world, IBlockData data, int i) {
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
