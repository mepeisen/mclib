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

package de.minigameslib.mclib3p.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Thirdparty bridges from/to mclib.
 * 
 * @author mepeisen
 */
public class Mclib3pPlugin extends JavaPlugin implements Listener
{
    
    /** loaded third party plugins. */
    private final Set<ThirdpartyPlugins> plugins = new HashSet<>();
    
    /** plugin instance. */
    public static Mclib3pPlugin INSTANCE;
    
    @Override
    public void onEnable()
    {
        INSTANCE = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        for (final ThirdpartyPlugins plugin : ThirdpartyPlugins.values())
        {
            final String pluginName = plugin.getPluginName();
            final Plugin javaPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (javaPlugin == null)
            {
                this.getLogger().log(Level.INFO, "plugin " + pluginName + " not found. Bridge not available."); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else if (!javaPlugin.isEnabled())
            {
                this.getLogger().log(Level.INFO, "plugin " + pluginName + " found but disabled. Bridge not available."); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else
            {
                this.getLogger().log(Level.INFO, "plugin " + pluginName + " found and enabled. Doing some checks."); //$NON-NLS-1$ //$NON-NLS-2$
                tryLoad(plugin, pluginName);
            }
        }
    }
    
    @Override
    public void onDisable()
    {
        for (final ThirdpartyPlugins plugin : this.plugins)
        {
            this.getLogger().log(Level.INFO, "disable bridge for " + plugin.getPluginName() + " (mclib-thirdparty is unloaded)."); //$NON-NLS-1$ //$NON-NLS-2$
            try
            {
                plugin.getOnDisableMclib().run();
                this.getLogger().log(Level.INFO, "plugin " + plugin.getPluginName() + " bridge disabled."); //$NON-NLS-1$ //$NON-NLS-2$
            }
            catch (Throwable t)
            {
                this.getLogger().log(Level.INFO, "plugin bridge for " + plugin.getPluginName() + " failed.", t); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        this.plugins.clear();
    }
    
    /**
     * On enabled event.
     * 
     * @param evt
     *            event
     */
    @EventHandler
    public void onPluginEnabled(PluginEnableEvent evt)
    {
        for (final ThirdpartyPlugins plugin : ThirdpartyPlugins.values())
        {
            if (!this.plugins.contains(plugin))
            {
                final String pluginName = plugin.getPluginName();
                if (evt.getPlugin().getName().equals(pluginName))
                {
                    this.getLogger().log(Level.INFO, "plugin " + pluginName + " was enabled. Doing some checks."); //$NON-NLS-1$ //$NON-NLS-2$
                    tryLoad(plugin, pluginName);
                }
            }
        }
    }
    
    /**
     * Trying to load plugin.
     * 
     * @param plugin
     *            plugin
     * @param pluginName
     *            name of the plugin
     */
    private void tryLoad(final ThirdpartyPlugins plugin, final String pluginName)
    {
        try
        {
            if (!plugin.getTester().getAsBoolean())
            {
                this.getLogger().log(Level.INFO, "plugin " + pluginName + " checks failed."); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else
            {
                plugin.getOnSetup().run();
                this.getLogger().log(Level.INFO, "plugin " + pluginName + " bridge enabled."); //$NON-NLS-1$ //$NON-NLS-2$
                this.plugins.add(plugin);
            }
        }
        catch (Throwable t)
        {
            this.getLogger().log(Level.INFO, "plugin bridge for " + pluginName + " failed.", t); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    /**
     * On disabled event.
     * 
     * @param evt
     *            event
     */
    @EventHandler
    public void onPluginDisabled(PluginDisableEvent evt)
    {
        // working on array copy so that we do not get concurrent modifications
        for (final ThirdpartyPlugins plugin : this.plugins.toArray(new ThirdpartyPlugins[this.plugins.size()]))
        {
            final String pluginName = plugin.getPluginName();
            if (evt.getPlugin().getName().equals(pluginName))
            {
                this.getLogger().log(Level.INFO, "disable bridge for " + plugin.getPluginName() + " (target plugin is unloaded)."); //$NON-NLS-1$ //$NON-NLS-2$
                try
                {
                    plugin.getOnDisablePlugin().run();
                    this.getLogger().log(Level.INFO, "plugin " + plugin.getPluginName() + " bridge disabled."); //$NON-NLS-1$ //$NON-NLS-2$
                }
                catch (Throwable t)
                {
                    this.getLogger().log(Level.INFO, "plugin bridge for " + plugin.getPluginName() + " failed.", t); //$NON-NLS-1$ //$NON-NLS-2$
                }
                this.plugins.remove(plugin);
            }
        }
    }
    
}
