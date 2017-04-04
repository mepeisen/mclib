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

package de.minigameslib.mclib.nms.api;

import java.util.Random;

/**
 * Interface representing drop rules
 * 
 * @author mepeisen
 */
public interface NmsDropRuleInterface
{

    /**
     * returns the type id for drops
     * @param variant 
     * @param random
     * @param fortune
     * @return type id
     */
    int getDropType(int variant, Random random, int fortune);

    /**
     * Returns the amount of drops 
     * @param random
     * @param fortune
     * @return amount
     */
    int getDropCount(Random random, int fortune);

    /**
     * Returns the experience for drops
     * @param variant 
     * @param random
     * @param enchantmentLevel
     * @return experience
     */
    int getExpDrop(int variant, Random random, int enchantmentLevel);

    /**
     * Returns the variants for drops
     * @param variant
     * @return variants
     */
    int getDropVariant(int variant);
    
}