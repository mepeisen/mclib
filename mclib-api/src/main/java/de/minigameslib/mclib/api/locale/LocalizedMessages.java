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

package de.minigameslib.mclib.api.locale;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A localized message class used within messages.yml of a plugin; should be used by enumerations that implement the {@link LocalizedMessageInterface}.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface LocalizedMessages
{
    
    /**
     * The default locale this plugin uses.
     * 
     * @return default/fallback locale.
     */
    String defaultLocale() default "en";
    
    /**
     * The default path used within messages.yml.
     * 
     * @return default path for this message.
     */
    String value();
    
}
