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

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.libs.jline.TerminalFactory;
import org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal;
import org.bukkit.craftbukkit.libs.joptsimple.OptionException;
import org.bukkit.craftbukkit.libs.joptsimple.OptionParser;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.spigottest.ServerManager;
import de.minigameslib.mclib.spigottest.nms.ConsoleThread;
import net.minecraft.server.v1_8_R1.DispenserRegistry;

/**
 * Starting local servers in same vm.
 * @author mepeisen
 */
class LocalServerStarter implements ServerManager
{
    
    /** logger. */
    static final Logger LOGGER = Logger.getLogger(LocalServerStarter.class.getName());
    
    /** the config directory. */
    private File configDirectory;

    /** the spigot dedicated server. */
    private SpigotDedicatedServer dedicatedserver;

    /** piped output stream to send commands to the console. */
    private PipedOutputStream pipedOut;

    /** piped input stream to receive the console output. */
    private PipedInputStream pipedIn;
    
    /** console lines. */
    private List<String> console = new ArrayList<>();
    
    /**
     * console thread.
     */
    private ConsoleThread consoleThread;

    /**
     * Constructor
     * @param configDirectory  
     */
    public LocalServerStarter(File configDirectory)
    {
        this.configDirectory = configDirectory;
    }
    
    @Override
    public void run()
    {
        if (this.dedicatedserver != null)
        {
            throw new IllegalStateException("Already started."); //$NON-NLS-1$
        }
        
        final OptionParser parser = new OptionParser() {
            {
                acceptsAll(asList("?", "help"), "Show the help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                acceptsAll(asList("c", "config"), "Properties file to use").withRequiredArg().ofType(File.class).defaultsTo(new File("server.properties")).describedAs("Properties file"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("P", "plugins"), "Plugin directory to use").withRequiredArg().ofType(File.class).defaultsTo(new File("plugins")).describedAs("Plugin directory"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("h", "host", "server-ip"), "Host to listen on").withRequiredArg().ofType(String.class).describedAs("Hostname or IP"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("W", "world-dir", "universe", "world-container"), "World container").withRequiredArg().ofType(File.class).describedAs("Directory containing worlds"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                
                acceptsAll(asList("w", "world", "level-name"), "World name").withRequiredArg().ofType(String.class).describedAs("World name"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("p", "port", "server-port"), "Port to listen on").withRequiredArg().ofType(Integer.class).describedAs("Port"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("o", "online-mode"), "Whether to use online authentication").withRequiredArg().ofType(Boolean.class).describedAs("Authentication"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                
                acceptsAll(asList("s", "size", "max-players"), "Maximum amount of players").withRequiredArg().ofType(Integer.class).describedAs("Server size"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("d", "date-format"), "Format of the date to display in the console (for log entries)").withRequiredArg().ofType(SimpleDateFormat.class) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        .describedAs("Log date format"); //$NON-NLS-1$
                
                acceptsAll(asList("log-pattern"), "Specfies the log filename pattern").withRequiredArg().ofType(String.class).defaultsTo("server.log").describedAs("Log filename"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                
                acceptsAll(asList("log-limit"), "Limits the maximum size of the log file (0 = unlimited)").withRequiredArg().ofType(Integer.class).defaultsTo(0).describedAs("Max log size"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                acceptsAll(asList("log-count"), "Specified how many log files to cycle through").withRequiredArg().ofType(Integer.class).defaultsTo(1).describedAs("Log count"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                acceptsAll(asList("log-append"), "Whether to append to the log file").withRequiredArg().ofType(Boolean.class).defaultsTo(true).describedAs("Log append"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                acceptsAll(asList("log-strip-color"), "Strips color codes from log file"); //$NON-NLS-1$ //$NON-NLS-2$
                
                acceptsAll(asList("b", "bukkit-settings"), "File for bukkit settings").withRequiredArg().ofType(File.class).defaultsTo(new File("bukkit.yml")).describedAs("Yml file"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("C", "commands-settings"), "File for command settings").withRequiredArg().ofType(File.class).defaultsTo(new File("commands.yml")).describedAs("Yml file"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                
                acceptsAll(asList("nojline"), "Disables jline and emulates the vanilla console"); //$NON-NLS-1$ //$NON-NLS-2$
                
                acceptsAll(asList("noconsole"), "Disables the console"); //$NON-NLS-1$ //$NON-NLS-2$
                
                acceptsAll(asList("v", "version"), "Show the CraftBukkit Version"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                acceptsAll(asList("demo"), "Demo mode"); //$NON-NLS-1$ //$NON-NLS-2$
                
                acceptsAll(asList("S", "spigot-settings"), "File for spigot settings") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                .withRequiredArg()
                .ofType(File.class)
                .defaultsTo(new File("spigot.yml")) //$NON-NLS-1$
                .describedAs("Yml file"); //$NON-NLS-1$
            }
        };
        
        OptionSet options = null;
        try
        {
            options = parser.parse(new String[0]);
        }
        catch (OptionException ex)
        {
            LocalServerStarter.LOGGER.log(Level.SEVERE, ex.getLocalizedMessage());
        }
        
        // Do you love Java using + and ! as string based identifiers? I sure do!
        String path = this.configDirectory.getAbsolutePath();
        if (path.contains("!") || path.contains("+")) //$NON-NLS-1$ //$NON-NLS-2$
        {
            throw new IllegalStateException("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again."); //$NON-NLS-1$
        }
        
        try
        {
            @SuppressWarnings("resource")
            final PipedInputStream sysin = new PipedInputStream();
            this.pipedOut = new PipedOutputStream(sysin);
            
            @SuppressWarnings("resource")
            final PipedOutputStream sysout = new PipedOutputStream();
            this.pipedIn = new PipedInputStream(sysout);

            this.consoleThread = new ConsoleThread(this.pipedIn, sysout, this.console);
            this.consoleThread.start();
            
            // This trick bypasses Maven Shade's clever rewriting of our getProperty call when using String literals
            String jline_UnsupportedTerminal = new String(
                    new char[] { 'j', 'l', 'i', 'n', 'e', '.', 'U', 'n', 's', 'u', 'p', 'p', 'o', 'r', 't', 'e', 'd', 'T', 'e', 'r', 'm', 'i', 'n', 'a', 'l' });
            String jline_terminal = new String(new char[] { 'j', 'l', 'i', 'n', 'e', '.', 't', 'e', 'r', 'm', 'i', 'n', 'a', 'l' });
            
            System.setProperty(jline_terminal, jline_UnsupportedTerminal);
            Main.useJline = false;
            System.setProperty(TerminalFactory.JLINE_TERMINAL, UnsupportedTerminal.class.getName());
            
            DispenserRegistry.c();
            this.dedicatedserver = new SpigotDedicatedServer(options, sysin, sysout, this.consoleThread);
            this.dedicatedserver.universe = this.configDirectory;
            this.dedicatedserver.primaryThread.start();
        }
        catch (Throwable t)
        {
            throw new IllegalStateException(t);
        }
    }

    @Override
    public boolean waitForConsole(String pattern, int timeoutMillis)
    {
        final Pattern regex = Pattern.compile(pattern);
        final long start = System.currentTimeMillis();
        synchronized (this.console)
        {
            for (final String line : this.console)
            {
                if (regex.matcher(line).matches()) return true;
            }
            while (System.currentTimeMillis() - start < timeoutMillis)
            {
                try
                {
                    this.console.wait(timeoutMillis - (System.currentTimeMillis() - start));
                }
                catch (@SuppressWarnings("unused") InterruptedException ex)
                {
                    // silently ignore
                }
                for (final String line : this.console)
                {
                    if (regex.matcher(line).matches()) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean waitGameCycle(int timeoutMillis)
    {
        synchronized (this.dedicatedserver)
        {
            if (!this.dedicatedserver.isMainLoop)
            {
                try
                {
                    this.dedicatedserver.wait(timeoutMillis);
                }
                catch (@SuppressWarnings("unused") InterruptedException ex)
                {
                    // ignore
                }
            }
            return this.dedicatedserver.isMainLoop;
        }
    }

    @Override
    public void clearConsole()
    {
        synchronized (this.console)
        {
            this.console.clear();
        }
    }

    @Override
    public void sendCommand(String command) throws IOException
    {
        this.pipedOut.write((command + "\n").getBytes()); //$NON-NLS-1$
    }

    @Override
    public boolean waitShutdown(int timeoutMillis)
    {
        synchronized (this.dedicatedserver)
        {
            if (this.dedicatedserver.isRunning)
            {
                try
                {
                    this.dedicatedserver.wait(timeoutMillis);
                }
                catch (@SuppressWarnings("unused") InterruptedException ex)
                {
                    // ignore
                }
            }
            if (this.dedicatedserver.isRunning)
            {
                return false;
            }

            if (this.consoleThread != null)
            {
                this.consoleThread.done();
                this.consoleThread = null;
            }
            return true;
        }
    }

    @Override
    public boolean isRunning()
    {
        return this.dedicatedserver != null && this.dedicatedserver.isRunning;
    }

    @Override
    public boolean isStopped()
    {
        return !this.isRunning();
    }

    @Override
    public void destroy()
    {
        try
        {
            this.sendCommand("stop"); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            LocalServerStarter.LOGGER.log(Level.SEVERE, "Problems stopping the server", e); //$NON-NLS-1$
        }
        if (this.consoleThread != null)
        {
            this.consoleThread.done();
            this.consoleThread = null;
        }
        // TODO force the server to shut down
    }

    @Override
    public Object createInstance(String javaClass)
    {
        try
        {
            Class<?> clazz = null;
            for (final Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins())
            {
                try
                {
                    clazz = plugin.getClass().getClassLoader().loadClass(javaClass);
                    break;
                }
                catch (@SuppressWarnings("unused") ClassNotFoundException ex)
                {
                    // silently ignore
                }
            }
            if (clazz == null)
            {
                clazz = this.getClass().getClassLoader().loadClass(javaClass);
            }
            return clazz.newInstance();
        }
        catch (@SuppressWarnings("unused") Exception ex)
        {
            // TODO logging
            return null;
        }
    }
    
}