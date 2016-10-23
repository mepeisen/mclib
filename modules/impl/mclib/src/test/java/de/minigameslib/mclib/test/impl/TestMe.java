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

package de.minigameslib.mclib.test.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.minigameslib.mclib.spigottest.SpigotJunit4Runner;
import de.minigameslib.mclib.spigottest.SpigotServer;
import de.minigameslib.mclib.spigottest.SpigotServerConfig;

/**
 * @author mepeisen
 *
 */
@RunWith(SpigotJunit4Runner.class)
public class TestMe
{
    
    @Test
    public void test() throws IOException
    {
        final SpigotServer server = new SpigotServerConfig().create();
        server.start();
        try
        {
            assertTrue(server.waitGameCycle(25000));
            server.sendCommand("FOO"); //$NON-NLS-1$
            assertTrue(server.waitForConsole(".*Unknown command.*", 25000)); //$NON-NLS-1$
        }
        finally
        {
            server.stop();
            server.waitShutdown(25000);
        }
    }
    
}
