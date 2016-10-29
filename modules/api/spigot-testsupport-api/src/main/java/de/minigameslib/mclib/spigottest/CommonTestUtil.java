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

/**
 * Some shared test utility.
 * 
 * @author mepeisen
 */
public class CommonTestUtil
{
    
    /**
     * tests some things on enumerations to satisfy code coverage.
     * 
     * @param clazz
     *            enum class to test
     */
    public static void testEnumClass(Class<? extends Enum<?>> clazz)
    {
        try
        {
            for (Object o : (Object[]) clazz.getMethod("values").invoke(null)) //$NON-NLS-1$
            {
                clazz.getMethod("valueOf", String.class).invoke(null, o.toString()); //$NON-NLS-1$
            }
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
        
    }
    
}
