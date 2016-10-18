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

package de.minigameslib.mclib.spigottest.nms.v18;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.plugin.PluginManager;

import de.minigameslib.mclib.spigottest.nms.ConsoleThread;
import net.minecraft.server.v1_8_R1.DedicatedServer;
import net.minecraft.server.v1_8_R1.PlayerList;

/**
 * @author mepeisen
 *
 */
class SpigotDedicatedServer extends DedicatedServer
{

    /** running flag. */
    volatile boolean isRunning = true;
    
    /** main loop thread. */
    volatile boolean isMainLoop = false;
    
    /** console thread. */
    ConsoleThread consoleThread;
    
    /**
     * @param options
     * @param in 
     * @param out 
     * @param consoleThread 
     * @throws IOException 
     */
    public SpigotDedicatedServer(OptionSet options, InputStream in, OutputStream out, ConsoleThread consoleThread) throws IOException
    {
        super(options);
        this.reader = new ConsoleReader(in, out);
        this.consoleThread = consoleThread;
    }

    @Override
    public void a(PlayerList playerlist)
    {
        super.a(playerlist);

        try
        {
            final Field field = CraftServer.class.getDeclaredField("pluginManager"); //$NON-NLS-1$
            field.setAccessible(true);
            final PluginManager orig = (PluginManager) field.get(this.server);
            field.set(this.server, new ExtendedPluginManager(orig));
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }
}
