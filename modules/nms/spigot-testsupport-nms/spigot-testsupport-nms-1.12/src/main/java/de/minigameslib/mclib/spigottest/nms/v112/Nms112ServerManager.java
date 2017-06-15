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

package de.minigameslib.mclib.spigottest.nms.v112;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.minigameslib.mclib.spigottest.ServerManager;
import de.minigameslib.mclib.spigottest.nms.NmsServerManager;
import de.minigameslib.mclib.spigottest.nms.ServerManagerWrapper;
import de.minigameslib.mclib.spigottest.nms.SpigotClassLoader;

/**
 * 
 * @author mepeisen
 */
public class Nms112ServerManager implements NmsServerManager
{
    
    @Override
    public ServerManager createLocalServerManager(File serverDirectory, File spigotJar, String[] injectedClasses)
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            final List<URL> urls = new ArrayList<>(Arrays.asList(((URLClassLoader)NmsServerManager.class.getClassLoader()).getURLs()));
            urls.add(0, spigotJar.toURI().toURL());
            final ClassLoader cl = new SpigotClassLoader(
                    urls.toArray(new URL[urls.size()]),
                    injectedClasses
                    );
            Thread.currentThread().setContextClassLoader(cl);
            final Class<?> clazz = cl.loadClass(LocalServerStarter.class.getName());
            // TODO Load local plugin from classpath
            final Constructor<?> constructor = clazz.getConstructor(File.class);
            constructor.setAccessible(true);
            return new ServerManagerWrapper(cl, (ServerManager) constructor.newInstance(serverDirectory));
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }
    
}
