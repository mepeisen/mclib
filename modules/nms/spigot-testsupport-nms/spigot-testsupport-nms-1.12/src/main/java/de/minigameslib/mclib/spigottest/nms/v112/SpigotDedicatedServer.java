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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.Metrics;
import org.spigotmc.SpigotConfig;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import de.minigameslib.mclib.spigottest.nms.ConsoleThread;
import io.netty.util.concurrent.EventExecutorGroup;
import net.minecraft.server.v1_12_R1.DataConverterManager;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import net.minecraft.server.v1_12_R1.PlayerList;
import net.minecraft.server.v1_12_R1.RegionFileCache;
import net.minecraft.server.v1_12_R1.ServerConnection;
import net.minecraft.server.v1_12_R1.UserCache;

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
     * @param dataconvertermanager
     * @param yggdrasilauthenticationservice
     * @param minecraftsessionservice
     * @param gameprofilerepository
     * @param usercache
     * @param in 
     * @param out 
     * @param consoleThread 
     * @throws IOException 
     */
    public SpigotDedicatedServer(OptionSet options, DataConverterManager dataconvertermanager, YggdrasilAuthenticationService yggdrasilauthenticationservice,
            MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, UserCache usercache, InputStream in, OutputStream out, ConsoleThread consoleThread) throws IOException
    {
        super(options, dataconvertermanager, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache);
        this.reader = new ConsoleReader(in, out);
        this.consoleThread = consoleThread;
    }

    @Override
    public boolean init() throws IOException
    {
        final boolean result = super.init();
        synchronized (this)
        {
            this.isMainLoop = true;
            this.notifyAll();
        }
        return result;
    }
    
    @Override
    public void B()
    {
        synchronized (this)
        {
            try
            {
                ((EventExecutorGroup)ServerConnection.a.c()).shutdownGracefully().await();
            }
            catch (Throwable ex)
            {
                ex.printStackTrace();
            }
            try
            {
                ((EventExecutorGroup)ServerConnection.b.c()).shutdownGracefully().await();
            }
            catch (Throwable ex)
            {
                ex.printStackTrace();
            }
            
            final ExecutorService craftScheduler = this.getPrivateField(Bukkit.getScheduler(), "executor"); //$NON-NLS-1$
            craftScheduler.shutdown();
            try
            {
                craftScheduler.awaitTermination(2000, TimeUnit.SECONDS);
            }
            catch (Throwable ex)
            {
                ex.printStackTrace();
            }
            
            final Metrics metrics = this.getPrivateStaticField(SpigotConfig.class, "metrics"); //$NON-NLS-1$
            try
            {
                metrics.disable();
            }
            catch (Throwable ex)
            {
                ex.printStackTrace();
            }
            
            this.consoleThread.done();
            this.worlds.clear();
            try
            {
                final Field field = CraftServer.class.getDeclaredField("worlds"); //$NON-NLS-1$
                field.setAccessible(true);
                final Map<?, ?> map = (Map<?, ?>) field.get(Bukkit.getServer());
                map.clear();
            }
            catch (Throwable ex)
            {
                ex.printStackTrace();
            }
            synchronized (RegionFileCache.class)
            {
                RegionFileCache.a();
            }
            System.gc();
            
            this.isRunning = false;
            this.notifyAll();
        }
    }

    /**
     * returns a private field
     * @param obj
     * @param name
     * @return private field
     */
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String name)
    {
        try
        {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(obj);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * returns a private field
     * @param clazz
     * @param name
     * @return private field
     */
    @SuppressWarnings("unchecked")
    private <T> T getPrivateStaticField(Class<?> clazz, String name)
    {
        try
        {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(null);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
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
