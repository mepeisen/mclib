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

package de.minigameslib.mclib.spigottest;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for spigot test support.
 * 
 * @author mepeisen
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface SpigotTest
{
    
    /**
     * Returns the version to test against.
     * @return spigot versions for testing.
     */
    SpigotVersion[] versions() default {SpigotVersion.Latest};
    
    /**
     * Test for all known spigot versions.
     * @return {@code true} if all versions will be tested (overrides {@link #versions()}).
     */
    boolean all() default false; 
    
}
