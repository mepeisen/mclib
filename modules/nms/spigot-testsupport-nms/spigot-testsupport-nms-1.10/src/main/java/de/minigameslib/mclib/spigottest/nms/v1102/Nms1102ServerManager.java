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

package de.minigameslib.mclib.spigottest.nms.v1102;

import java.io.File;

import de.minigameslib.mclib.spigottest.ServerManager;
import de.minigameslib.mclib.spigottest.nms.NmsServerManager;

/**
 * 
 * @author mepeisen
 */
public class Nms1102ServerManager implements NmsServerManager
{
    
    @Override
    public ServerManager createLocalServerManager(File serverDirectory, File spigotJar)
    {
        // TODO Classpath wrapper
        // TODO Load local plugin from classpath
        return new LocalServerStarter(serverDirectory, spigotJar);
    }
    
}
