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

package de.minigameslib.mclib.api.validate;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.GenericValue;
import de.minigameslib.mclib.api.McException;

/**
 * Validator to check if a list config value contains at least x entries (or zero entries).
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target({ FIELD, ElementType.TYPE })
public @interface ValidateListMin
{
    
    /**
     * minimum entries
     * 
     * @return minimum entries
     */
    int value();
    
    /**
     * Validation of this annotation.
     */
    public class ValidatorInstance
    {
        /**
         * Validation
         * 
         * @param lmin
         *            annotation value
         * @param cvi
         *            conviruation value
         * @throws McException
         *             thrown on validation errors.
         */
        public static void validate(ValidateListMin lmin, GenericValue cvi) throws McException
        {
            if (cvi.isBooleanList())
            {
                if (cvi.isset() && cvi.getBooleanList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getBooleanList().length, lmin.value());
                }
            }
            else if (cvi.isByteList())
            {
                if (cvi.isset() && cvi.getByteList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getByteList().length, lmin.value());
                }
            }
            else if (cvi.isCharacterList())
            {
                if (cvi.isset() && cvi.getCharacterList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getCharacterList().length, lmin.value());
                }
            }
            else if (cvi.isColorList())
            {
                if (cvi.isset() && cvi.getColorList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getColorList().length, lmin.value());
                }
            }
            else if (cvi.isDoubleList())
            {
                if (cvi.isset() && cvi.getDoubleList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getDoubleList().length, lmin.value());
                }
            }
            else if (cvi.isEnumList())
            {
                // TODO check enum list
                // if (cvi.isset() && cvi.getEnumList().length < lmax.value())
                // {
                // throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getEnumList().length, lmax.value());
                // }
            }
            else if (cvi.isFloatList())
            {
                if (cvi.isset() && cvi.getFloatList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getFloatList().length, lmin.value());
                }
            }
            else if (cvi.isIntList())
            {
                if (cvi.isset() && cvi.getIntList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getIntList().length, lmin.value());
                }
            }
            else if (cvi.isItemStackList())
            {
                if (cvi.isset() && cvi.getItemStackList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getItemStackList().length, lmin.value());
                }
            }
            else if (cvi.isLongList())
            {
                if (cvi.isset() && cvi.getLongList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getLongList().length, lmin.value());
                }
            }
            else if (cvi.isObjectList())
            {
                // TODO check object list
                // if (cvi.isset() && cvi.getObjectList().length < lmax.value())
                // {
                // throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getObjectList().length, lmax.value());
                // }
            }
            else if (cvi.isPlayerList())
            {
                if (cvi.isset() && cvi.getPlayerList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getPlayerList().length, lmin.value());
                }
            }
            else if (cvi.isShortList())
            {
                if (cvi.isset() && cvi.getShortList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getShortList().length, lmin.value());
                }
            }
            else if (cvi.isStringList())
            {
                if (cvi.isset() && cvi.getStringList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getStringList().length, lmin.value());
                }
            }
            else if (cvi.isVectorList())
            {
                if (cvi.isset() && cvi.getVectorList().length < lmin.value())
                {
                    throw new McException(CommonMessages.ValidateListTooSmall, cvi.path(), cvi.getVectorList().length, lmin.value());
                }
            }
        }
    }
    
}
