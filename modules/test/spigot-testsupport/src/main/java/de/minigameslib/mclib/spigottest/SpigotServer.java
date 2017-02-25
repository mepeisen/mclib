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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import de.minigameslib.mclib.spigottest.SpigotServerConfig.Plugin;
import de.minigameslib.mclib.spigottest.nms.FilterableClassLoader;
import de.minigameslib.mclib.spigottest.nms.v1102.Nms1102ServerManager;
import de.minigameslib.mclib.spigottest.nms.v111.Nms111ServerManager;
import de.minigameslib.mclib.spigottest.nms.v18.Nms18ServerManager;
import de.minigameslib.mclib.spigottest.nms.v183.Nms183ServerManager;
import de.minigameslib.mclib.spigottest.nms.v185.Nms185ServerManager;
import de.minigameslib.mclib.spigottest.nms.v19.Nms19ServerManager;
import de.minigameslib.mclib.spigottest.nms.v194.Nms194ServerManager;

/**
 * Helper to create and manage spigot test servers.
 * 
 * @author mepeisen
 */
public class SpigotServer
{
    
    /** logger. */
    static final Logger LOGGER = Logger.getLogger(SpigotServer.class.getName());

    /**
     * Server starting runnable for configured server types.
     */
    private ServerManager manager;
    
    /**
     * Constructor
     * @param spigotServerConfig configuration.
     * @throws IOException 
     */
    SpigotServer(SpigotServerConfig spigotServerConfig) throws IOException
    {
        // check server type.
        final String[] injectedClasses = {ServerManager.class.getName(), SpigotServer.class.getName(), FilterableClassLoader.class.getName()};
        switch (spigotServerConfig.getServerType())
        {
            case Local:
                switch (spigotServerConfig.getServerVersion())
                {
                    case V1_11_2:
                    case Latest:
                        this.manager = new Nms111ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_11:
                        this.manager = new Nms111ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_10_2:
                        this.manager = new Nms1102ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_10:
                        this.manager = new Nms1102ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_8:
                        this.manager = new Nms18ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_8_3:
                        this.manager = new Nms183ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_8_4:
                        this.manager = new Nms185ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_8_5:
                        this.manager = new Nms185ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_8_6:
                        this.manager = new Nms185ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_8_7:
                        this.manager = new Nms185ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_8_8:
                        this.manager = new Nms185ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_9:
                        this.manager = new Nms19ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_9_2:
                        this.manager = new Nms19ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    case V1_9_4:
                        this.manager = new Nms194ServerManager().createLocalServerManager(spigotServerConfig.getTempDirectory(), this.installRuntime(spigotServerConfig), injectedClasses);
                        break;
                    default:
                        throw new IllegalStateException("Unknown server version"); //$NON-NLS-1$
                }
                break;
            case Remote:
                // TODO add remote support
                throw new IllegalStateException("Remote server type not yet supported"); //$NON-NLS-1$
            case Standalone:
                // TODO add standalone support
                throw new IllegalStateException("Standalone server type not yet supported"); //$NON-NLS-1$
            default:
                throw new IllegalStateException("Unknown server type"); //$NON-NLS-1$
        }
        
        // prepare plugins
        final File pluginDir = new File(spigotServerConfig.getTempDirectory(), "plugins"); //$NON-NLS-1$
        if (!pluginDir.exists())
        {
            pluginDir.mkdirs();
        }
        for (final Plugin plugin : spigotServerConfig.getPlugins())
        {
            plugin.prepare(pluginDir);
        }
        
        // server.properties
        final File serverProperties = new File(spigotServerConfig.getTempDirectory(), "server.properties"); //$NON-NLS-1$
        final Properties properties = new Properties();
        if (serverProperties.exists())
        {
            try (final FileInputStream fis = new FileInputStream(serverProperties))
            {
                properties.load(fis);
            }
        }
        properties.setProperty("server-port", String.valueOf(spigotServerConfig.getMainPort())); //$NON-NLS-1$
        properties.setProperty("level-type", spigotServerConfig.getDefaultWorldType()); //$NON-NLS-1$
        for (final Map.Entry<String, String> entry : spigotServerConfig.getServerProperties().entrySet())
        {
            properties.put(entry.getKey(), entry.getValue());
        }
        try (final FileOutputStream fos = new FileOutputStream(serverProperties))
        {
            properties.store(fos, ""); //$NON-NLS-1$
        }
        
        // current work dir
        System.setProperty("com.mojang.eula.agree", "true");  //$NON-NLS-1$  //$NON-NLS-2$
    }
    
    /**
     * Installs the runtime/ spigot.jar
     * @param config
     * @return spigot jar file
     * @throws IOException 
     */
    private File installRuntime(final SpigotServerConfig config) throws IOException
    {
        // TODO Read base url from config
        final String baseurl = "http://nexus.xworlds.eu/nexus/service/local/artifact/maven/content?r=mce&g=org.spigotmc&a=spigot&p=jar&v="; //$NON-NLS-1$
        String version = null;
        switch (config.getServerVersion())
        {
            case Latest:
            default:
            case V1_11_2:
                version = "1.11.2-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_11:
                version = "1.11-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_10_2:
                version = "1.10.2-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_10:
                version = "1.10-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_8:
                version = "1.8-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_8_3:
                version = "1.8.3-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_8_4:
                version = "1.8.4-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_8_5:
                version = "1.8.5-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_8_6:
                version = "1.8.6-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_8_7:
                version = "1.8.7-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_8_8:
                version = "1.8.8-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_9:
                version = "1.9-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_9_2:
                version = "1.9.2-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
            case V1_9_4:
                version = "1.9.4-R0.1-SNAPSHOT"; //$NON-NLS-1$
                break;
        }
        final File target = new File(config.getRuntimeDirectory(), "spigot-" + version + ".jar"); //$NON-NLS-1$ //$NON-NLS-2$
        if (!target.exists())
        {
            // download.
            final URL url = new URL(baseurl + version);
            try (final ReadableByteChannel rbc = Channels.newChannel(url.openStream()))
            {
                try (final FileOutputStream fos = new FileOutputStream(target))
                {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
            }
        }
        return target;
    }

    /**
     * Starts the server asynchronous; should be followed by a call to {@link #waitGameCycle}
     */
    public void start()
    {
        this.manager.run();
    }
    
    /**
     * Waits for a specific text in console
     * @param pattern regex pattern
     * @param timeoutMillis timeout in milliseconds; if the pattern did not match the given pattern false is returned
     * @return true if the console text is found; false if it is not found
     */
    public boolean waitForConsole(String pattern, int timeoutMillis)
    {
        return this.manager.waitForConsole(pattern, timeoutMillis);
    }
    
    /**
     * waits for a clean startup
     * @param timeoutMillis timeout in milliseconds; if the server did not start within this timeout false is returned
     * @return true if the server is started; false if there are problems
     */
    public boolean waitGameCycle(int timeoutMillis)
    {
        return this.manager.waitGameCycle(timeoutMillis);
    }
    
    /**
     * Clears the console from previous messages.
     */
    public void clearConsole()
    {
        this.manager.clearConsole();
    }
    
    /**
     * sendds a command to console.
     * @param command
     * @throws IOException 
     */
    public void sendCommand(String command) throws IOException
    {
        this.manager.sendCommand(command);
    }
    
    /**
     * Destroys the server
     */
    public void destroy()
    {
        this.manager.destroy();
    }
    
    /**
     * Stops the server
     * @throws IOException 
     */
    public void stop() throws IOException
    {
        this.sendCommand("stop"); //$NON-NLS-1$
    }
    
    /**
     * Waits for a clean shutdown.
     * @param timeoutMillis timeout in milliseconds; if the server did not stop within this timeout false is returned
     * @return true if the server is stopped; false if there are problems
     */
    public boolean waitShutdown(int timeoutMillis)
    {
        return this.manager.waitShutdown(timeoutMillis);
    }
    
    /**
     * Checks if the server is running.
     * @return true if the server is running.
     */
    public boolean isRunning()
    {
        return this.manager.isRunning();
    }
    
    /**
     * Checks if the server is stopped.
     * @return true if the server is stopped.
     */
    public boolean isStopped()
    {
        return this.manager.isStopped();
    }

    /**
     * Creates a java object by using the class loader within the server instance.
     * @param javaClass
     * @return instance or {@code null} if there was a problem creating the instance
     */
    public Object createInstance(Class<?> javaClass)
    {
        return this.manager.createInstance(javaClass.getName());
    }
    
    /**
     * Check if plugin is enabled
     * @param name
     * @return {@code true} if plugin is enabled.
     */
    public boolean isPluginEnabled(String name)
    {
        return this.manager.isPluginEnabled(name);
    }
    
    /**
     * Returns the plugin object for given plugin name.
     * @param name
     * @return plugin object
     */
    public Object getPlugin(String name)
    {
        return this.manager.getPlugin(name);
    }
    
    /**
     * Returns the plugin object for given plugin name.
     * @param serviceClass
     * @return plugin object
     */
    public Object loadServicePlugin(Class<?> serviceClass)
    {
        return this.manager.loadServicePlugin(serviceClass.getName());
    }
    
}
