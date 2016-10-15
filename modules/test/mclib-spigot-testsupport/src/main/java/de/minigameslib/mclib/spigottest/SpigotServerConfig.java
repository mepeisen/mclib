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

import java.io.File;

/**
 * Configuration for spigot test servers.
 * 
 * @author mepeisen
 */
public class SpigotServerConfig
{
    
    /** server version to be used. */
    private SpigotVersion serverVersion;
    
    /** the temporary directory used as server installation. */
    private File tempDirectory;
    
    /**
     * Creates a spigot server for this config.
     * @return spigot server.
     */
    public SpigotServer create()
    {
        final SpigotServer server = new SpigotServer(this);
        return server;
    }

    /**
     * @return the serverVersion
     */
    public SpigotVersion getServerVersion()
    {
        return this.serverVersion;
    }

    /**
     * @param serverVersion the serverVersion to set
     * @return this object for chaining
     */
    public SpigotServerConfig setServerVersion(SpigotVersion serverVersion)
    {
        this.serverVersion = serverVersion;
        return this;
    }

    /**
     * @return the tempDirectory
     */
    public File getTempDirectory()
    {
        return this.tempDirectory;
    }

    /**
     * @param tempDirectory the tempDirectory to set
     * @return this object for chaining
     */
    public SpigotServerConfig setTempDirectory(File tempDirectory)
    {
        this.tempDirectory = tempDirectory;
        return this;
    }
    
}
