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
 * Validator to check if a string config value or string list config value contains values not longer than x.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target({ FIELD, ElementType.TYPE })
public @interface ValidateStrMax
{
    
    /**
     * maximum length
     * 
     * @return maximum length
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
         * @param smax
         *            annotation value
         * @param cvi
         *            configuration value
         * @throws McException
         *             thrown on validation errors.
         */
        public static void validate(ValidateStrMax smax, ConfigurationValueInterface cvi) throws McException
        {
            if (cvi.isString())
            {
                if (cvi.isset() && cvi.getString().length() > smax.value())
                {
                    throw new McException(CommonMessages.ValidateStringTooBig, cvi.path(), cvi.getString().length(), smax.value());
                }
            }
            else if (cvi.isStringList())
            {
                for (final String s : cvi.getStringList())
                {
                    if (s.length() > smax.value())
                    {
                        throw new McException(CommonMessages.ValidateStringTooBig, cvi.path(), s.length(), smax.value());
                    }
                }
            }
        }
    }
    
}
