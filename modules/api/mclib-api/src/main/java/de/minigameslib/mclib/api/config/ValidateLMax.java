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
 * Validator to check if a numeric config value or numeric list config value contains values not higher than x.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target({ FIELD, ElementType.TYPE })
public @interface ValidateLMax
{
    
    /**
     * minimum value
     * 
     * @return minimum value
     */
    long value();
    
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
        public static void validate(ValidateLMax lmax, ConfigurationValueInterface cvi) throws McException
        {
            if (cvi.isByte())
            {
                if (cvi.isset() && cvi.getByte() > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), cvi.getByte(), lmax.value());
                }
            }
            else if (cvi.isShort())
            {
                if (cvi.isset() && cvi.getShort() > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), cvi.getShort(), lmax.value());
                }
            }
            else if (cvi.isInt())
            {
                if (cvi.isset() && cvi.getInt() > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), cvi.getInt(), lmax.value());
                }
            }
            else if (cvi.isLong())
            {
                if (cvi.isset() && cvi.getLong() > lmax.value())
                {
                    throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), cvi.getLong(), lmax.value());
                }
            }
            else if (cvi.isByteList())
            {
                for (final byte b : cvi.getByteList())
                {
                    if (b < lmax.value())
                    {
                        throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), b, lmax.value());
                    }
                }
            }
            else if (cvi.isShortList())
            {
                for (final short s : cvi.getShortList())
                {
                    if (s < lmax.value())
                    {
                        throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), s, lmax.value());
                    }
                }
            }
            else if (cvi.isIntList())
            {
                for (final int i : cvi.getIntList())
                {
                    if (i < lmax.value())
                    {
                        throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), i, lmax.value());
                    }
                }
            }
            else if (cvi.isLongList())
            {
                for (final long l : cvi.getShortList())
                {
                    if (l < lmax.value())
                    {
                        throw new McException(CommonMessages.ValidateLValueTooHigh, cvi.path(), l, lmax.value());
                    }
                }
            }
        }
    }
    
}
