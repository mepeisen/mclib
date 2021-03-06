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

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Block representing a hopper.
 * 
 * @author mepeisen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface BlockHopper
{
    
    /**
     * returns the number of slots this hopper supports.
     * @return number of slots.
     */
    int countSlots() default 5;
    
    /**
     * Returns the max stack size for this hopper.
     * @return max stack size.
     */
    int maxStackSize() default 64;
    
    /**
     * Hopper rule class; will be initialized once per hopper.
     * 
     * @return Hopper rule class
     */
    Class<? extends BlockHopperRuleInterface> value() default BlockHopperRuleInterface.Original.class;
    
}
