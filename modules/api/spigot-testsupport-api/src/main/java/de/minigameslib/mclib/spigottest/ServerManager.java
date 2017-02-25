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

import java.io.IOException;

/**
 * Server manager interface
 * @author mepeisen
 */
public interface ServerManager extends Runnable
{
    
    /**
     * Waits for a specific text in console
     * @param pattern regex pattern
     * @param timeoutMillis timeout in milliseconds; if the pattern did not match the given pattern false is returned
     * @return true if the console text is found; false if it is not found
     */
    boolean waitForConsole(String pattern, int timeoutMillis);
    
    /**
     * Destroys the server
     */
    void destroy();

    /**
     * waits for a clean startup
     * @param timeoutMillis timeout in milliseconds; if the server did not start within this timeout false is returned
     * @return true if the server is started; false if there are problems
     */
    boolean waitGameCycle(int timeoutMillis);
    
    /**
     * Clears the console from previous messages.
     */
    void clearConsole();
    
    /**
     * sendds a command to console.
     * @param command
     * @throws IOException 
     */
    void sendCommand(String command) throws IOException;
    
    /**
     * Waits for a clean shutdown.
     * @param timeoutMillis timeout in milliseconds; if the server did not stop within this timeout false is returned
     * @return true if the server is stopped; false if there are problems
     */
    boolean waitShutdown(int timeoutMillis);
    
    /**
     * Checks if the server is running.
     * @return true if the server is running.
     */
    boolean isRunning();
    
    /**
     * Checks if the server is stopped.
     * @return true if the server is stopped.
     */
    boolean isStopped();

    /**
     * Creates a java object by using the class loader within the server instance.
     * @param javaClass
     * @return instance or {@code null} if there was a problem creating the instance
     */
    Object createInstance(String javaClass);
    
    /**
     * Check if plugin is enabled
     * @param name
     * @return {@code true} if plugin is enabled.
     */
    boolean isPluginEnabled(String name);
    
    /**
     * Returns the plugin object for given plugin name.
     * @param name
     * @return plugin object
     */
    Object getPlugin(String name);
    
    /**
     * Returns the plugin object for given plugin name.
     * @param serviceClass
     * @return plugin object
     */
    Object loadServicePlugin(String serviceClass);
    
}