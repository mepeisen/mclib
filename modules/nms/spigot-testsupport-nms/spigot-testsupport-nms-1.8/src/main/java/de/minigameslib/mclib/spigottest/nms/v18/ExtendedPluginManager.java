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

import java.io.File;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPluginLoader;

/**
 * @author mepeisen
 *
 */
class ExtendedPluginManager implements PluginManager
{
    
    /** the original plugin loader delegate. */
    private final PluginManager delegate;
    
    /**
     * Constructor
     * @param delegate
     */
    public ExtendedPluginManager(final PluginManager delegate)
    {
        this.delegate = delegate;
    }

    /**
     * Returns the delegate.
     * @return the delegate
     */
    public PluginManager getDelegate()
    {
        return this.delegate;
    }

    @Override
    public void registerInterface(Class<? extends PluginLoader> paramClass) throws IllegalArgumentException
    {
        this.delegate.registerInterface(paramClass);
        if (paramClass == JavaPluginLoader.class)
        {
            this.delegate.registerInterface(LocalPluginLoader.class);
        }
    }

    @Override
    public Plugin getPlugin(String paramString)
    {
        return this.delegate.getPlugin(paramString);
    }

    @Override
    public Plugin[] getPlugins()
    {
        return this.delegate.getPlugins();
    }

    @Override
    public boolean isPluginEnabled(String paramString)
    {
        return this.delegate.isPluginEnabled(paramString);
    }

    @Override
    public boolean isPluginEnabled(Plugin paramPlugin)
    {
        return this.delegate.isPluginEnabled(paramPlugin);
    }

    @Override
    public Plugin loadPlugin(File paramFile) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException
    {
        return this.delegate.loadPlugin(paramFile);
    }

    @Override
    public Plugin[] loadPlugins(File paramFile)
    {
        return this.delegate.loadPlugins(paramFile);
    }

    @Override
    public void disablePlugins()
    {
        this.delegate.disablePlugins();
    }

    @Override
    public void clearPlugins()
    {
        this.delegate.clearPlugins();
    }

    @Override
    public void callEvent(Event paramEvent) throws IllegalStateException
    {
        this.delegate.callEvent(paramEvent);
    }

    @Override
    public void registerEvents(Listener paramListener, Plugin paramPlugin)
    {
        this.delegate.registerEvents(paramListener, paramPlugin);
    }

    @Override
    public void registerEvent(Class<? extends Event> paramClass, Listener paramListener, EventPriority paramEventPriority, EventExecutor paramEventExecutor, Plugin paramPlugin)
    {
        this.delegate.registerEvent(paramClass, paramListener, paramEventPriority, paramEventExecutor, paramPlugin);
    }

    @Override
    public void registerEvent(Class<? extends Event> paramClass, Listener paramListener, EventPriority paramEventPriority, EventExecutor paramEventExecutor, Plugin paramPlugin, boolean paramBoolean)
    {
        this.delegate.registerEvent(paramClass, paramListener, paramEventPriority, paramEventExecutor, paramPlugin, paramBoolean);
    }

    @Override
    public void enablePlugin(Plugin paramPlugin)
    {
        this.delegate.enablePlugin(paramPlugin);
    }

    @Override
    public void disablePlugin(Plugin paramPlugin)
    {
        this.delegate.disablePlugin(paramPlugin);
    }

    @Override
    public Permission getPermission(String paramString)
    {
        return this.delegate.getPermission(paramString);
    }

    @Override
    public void addPermission(Permission paramPermission)
    {
        this.delegate.addPermission(paramPermission);
    }

    @Override
    public void removePermission(Permission paramPermission)
    {
        this.delegate.removePermission(paramPermission);
    }

    @Override
    public void removePermission(String paramString)
    {
        this.delegate.removePermission(paramString);
    }

    @Override
    public Set<Permission> getDefaultPermissions(boolean paramBoolean)
    {
        return this.delegate.getDefaultPermissions(paramBoolean);
    }

    @Override
    public void recalculatePermissionDefaults(Permission paramPermission)
    {
        this.delegate.recalculatePermissionDefaults(paramPermission);
    }

    @Override
    public void subscribeToPermission(String paramString, Permissible paramPermissible)
    {
        this.delegate.subscribeToPermission(paramString, paramPermissible);
    }

    @Override
    public void unsubscribeFromPermission(String paramString, Permissible paramPermissible)
    {
        this.delegate.unsubscribeFromPermission(paramString, paramPermissible);
    }

    @Override
    public Set<Permissible> getPermissionSubscriptions(String paramString)
    {
        return this.delegate.getPermissionSubscriptions(paramString);
    }

    @Override
    public void subscribeToDefaultPerms(boolean paramBoolean, Permissible paramPermissible)
    {
        this.delegate.subscribeToDefaultPerms(paramBoolean, paramPermissible);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean paramBoolean, Permissible paramPermissible)
    {
        this.delegate.unsubscribeFromDefaultPerms(paramBoolean, paramPermissible);
    }

    @Override
    public Set<Permissible> getDefaultPermSubscriptions(boolean paramBoolean)
    {
        return this.delegate.getDefaultPermSubscriptions(paramBoolean);
    }

    @Override
    public Set<Permission> getPermissions()
    {
        return this.delegate.getPermissions();
    }

    @Override
    public boolean useTimings()
    {
        return this.delegate.useTimings();
    }
    
}
