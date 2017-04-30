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

import java.util.Random;

/**
 * Interface representing drop rules.
 * 
 * @author mepeisen
 */
public interface BlockDropRuleInterface
{

    /**
     * returns the type id for drops.
     * @param id block id.
     * @param variant block variant id.
     * @param random the world random.
     * @param fortune the players fortune.
     * @return type id
     */
    BlockId getDropType(BlockId id, BlockVariantId variant, Random random, int fortune);

    /**
     * Returns the amount of drops.
     * @param random the world random.
     * @param fortune the players fortune.
     * @return amount
     */
    int getDropCount(Random random, int fortune);

    /**
     * Returns the experience for drops.
     * @param id the block id.
     * @param variant the block variant id.
     * @param random the world random.
     * @param enchantmentLevel the tooling enchantment level.
     * @return experience
     */
    int getExpDrop(BlockId id, BlockVariantId variant, Random random, int enchantmentLevel);

    /**
     * Returns the variants for drops.
     * @param id the block id.
     * @param variant the variant id.
     * @return variants
     */
    BlockVariantId getDropVariant(BlockId id, BlockVariantId variant);
    
    /**
     * Rule to drop the block itself with amount 1.
     */
    public class DropSelf implements BlockDropRuleInterface
    {

        @Override
        public BlockId getDropType(BlockId id, BlockVariantId variant, Random random, int fortune)
        {
            return id;
        }

        @Override
        public int getDropCount(Random random, int fortune)
        {
            return 1;
        }

        @Override
        public int getExpDrop(BlockId id, BlockVariantId variant, Random random, int enchantmentLevel)
        {
            return 0;
        }

        @Override
        public BlockVariantId getDropVariant(BlockId id, BlockVariantId variant)
        {
            return variant;
        }
        
    }
    
    /**
     * Rule to drop nothing.
     */
    public class DropNull implements BlockDropRuleInterface
    {

        @Override
        public BlockId getDropType(BlockId id, BlockVariantId variant, Random random, int fortune)
        {
            return null;
        }

        @Override
        public int getDropCount(Random random, int fortune)
        {
            return 0;
        }

        @Override
        public int getExpDrop(BlockId id, BlockVariantId variant, Random random, int enchantmentLevel)
        {
            return 0;
        }

        @Override
        public BlockVariantId getDropVariant(BlockId id, BlockVariantId variant)
        {
            return null;
        }
        
    }
    
}
