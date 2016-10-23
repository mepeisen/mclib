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

import java.util.logging.Logger;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author mepeisen
 *
 */
public class SpigotJunit4Runner extends BlockJUnit4ClassRunner
{
    
    /** logger. */
    private static final Logger logger = Logger.getLogger(SpigotJunit4Runner.class.getName());

    /**
     * @param klass
     * @throws InitializationError
     */
    public SpigotJunit4Runner(Class<?> klass) throws InitializationError
    {
        super(klass);
        System.out.println("Testing " + klass);
    }

    @Override
    public Object createTest() throws Exception
    {
        System.out.println("Create test ");
        return super.createTest();
    }
    
}
