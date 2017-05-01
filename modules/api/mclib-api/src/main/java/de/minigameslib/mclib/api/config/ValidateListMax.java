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

package de.minigameslib.mclib.api.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;

/**
 * Validator to check if a list config value contains a mximum of x entries.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target({ FIELD, ElementType.TYPE })
public @interface ValidateListMax
{
    
    /**
     * maximum entries
     * 
     * @return maximum entries
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
         * @param lmax
         *            annotation value
         * @param cvi
         *            configuration value
         * @throws McException
         *             thrown on validation errors.
         */
        public static void validate(ValidateListMax lmax, ConfigurationValueInterface cvi) throws McException
        {
            if (cvi.isBooleanList())
            {
                if (cvi.isset() && cvi.getBooleanList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getBooleanList().length, lmax.value());
                }
            }
            else if (cvi.isByteList())
            {
                if (cvi.isset() && cvi.getByteList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getByteList().length, lmax.value());
                }
            }
            else if (cvi.isCharacterList())
            {
                if (cvi.isset() && cvi.getCharacterList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getCharacterList().length, lmax.value());
                }
            }
            else if (cvi.isColorList())
            {
                if (cvi.isset() && cvi.getColorList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getColorList().length, lmax.value());
                }
            }
            else if (cvi.isDoubleList())
            {
                if (cvi.isset() && cvi.getDoubleList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getDoubleList().length, lmax.value());
                }
            }
            else if (cvi.isEnumList())
            {
                // TODO check enum list
                // if (cvi.isset() && cvi.getEnumList().length > lmax.value())
                // {
                // throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getEnumList().length, lmax.value());
                // }
            }
            else if (cvi.isFloatList())
            {
                if (cvi.isset() && cvi.getFloatList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getFloatList().length, lmax.value());
                }
            }
            else if (cvi.isIntList())
            {
                if (cvi.isset() && cvi.getIntList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getIntList().length, lmax.value());
                }
            }
            else if (cvi.isItemStackList())
            {
                if (cvi.isset() && cvi.getItemStackList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getItemStackList().length, lmax.value());
                }
            }
            else if (cvi.isLongList())
            {
                if (cvi.isset() && cvi.getLongList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getLongList().length, lmax.value());
                }
            }
            else if (cvi.isObjectList())
            {
                // TODO check object list
                // if (cvi.isset() && cvi.getObjectList().length > lmax.value())
                // {
                // throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getObjectList().length, lmax.value());
                // }
            }
            else if (cvi.isPlayerList())
            {
                if (cvi.isset() && cvi.getPlayerList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getPlayerList().length, lmax.value());
                }
            }
            else if (cvi.isShortList())
            {
                if (cvi.isset() && cvi.getShortList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getShortList().length, lmax.value());
                }
            }
            else if (cvi.isStringList())
            {
                if (cvi.isset() && cvi.getStringList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getStringList().length, lmax.value());
                }
            }
            else if (cvi.isVectorList())
            {
                if (cvi.isset() && cvi.getVectorList().length > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateListTooBig, cvi.path(), cvi.getVectorList().length, lmax.value());
                }
            }
        }
    }
    
}
