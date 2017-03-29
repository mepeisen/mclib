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
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author mepeisen
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface BlockData
{
    
    /**
     * the block variants (max. 16 variants)
     * @return block variants
     */
    Class<? extends BlockVariantId> variants() default CustomVariantId.class;
    
    /**
     * Returns the provider for getting the item name.
     * @return provider for getting the name.
     */
    Class<? extends NameProvider> name() default NullNameProvider.class;
    
    /**
     * A single default variant
     * @author mepeisen
     */
    public enum CustomVariantId implements BlockVariantId
    {
        /** custom block variant */
        @BlockVariantData(textures = {}, modelJson = "{\"parent\": \"block/cube_all\",\"textures\": {\"all\": \"mclib:blocks/custom\"}}")
        DEFAULT;
    }
    
}
