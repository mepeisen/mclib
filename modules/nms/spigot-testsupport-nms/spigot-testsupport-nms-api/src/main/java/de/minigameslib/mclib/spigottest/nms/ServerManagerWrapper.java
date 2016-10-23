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

package de.minigameslib.mclib.spigottest.nms;

import java.io.IOException;

import de.minigameslib.mclib.spigottest.ServerManager;

/**
 * @author mepeisen
 *
 */
public class ServerManagerWrapper implements ServerManager
{
    
    /** the context class loader to be used. */
    private final ClassLoader classLoader;
    
    /** server manager delegate. */
    private ServerManager delegate;

    /**
     * @param classLoader
     * @param delegate 
     */
    public ServerManagerWrapper(ClassLoader classLoader, ServerManager delegate)
    {
        this.classLoader = classLoader;
        this.delegate = delegate;
    }

    @Override
    public void run()
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            this.delegate.run();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public boolean waitForConsole(String pattern, int timeoutMillis)
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            return this.delegate.waitForConsole(pattern, timeoutMillis);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public void destroy()
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            this.delegate.destroy();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }   
    }

    @Override
    public boolean waitGameCycle(int timeoutMillis)
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            return this.delegate.waitGameCycle(timeoutMillis);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public void clearConsole()
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            this.delegate.clearConsole();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public void sendCommand(String command) throws IOException
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            this.delegate.sendCommand(command);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public boolean waitShutdown(int timeoutMillis)
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            return this.delegate.waitShutdown(timeoutMillis);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public boolean isRunning()
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            return this.delegate.isRunning();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public boolean isStopped()
    {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            return this.delegate.isStopped();
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public Object createInstance(Class<?> javaClass)
    {
        try
        {
            return this.classLoader.loadClass(javaClass.getName()).newInstance();
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // TODO logging
            return null;
        }
    }
    
}
