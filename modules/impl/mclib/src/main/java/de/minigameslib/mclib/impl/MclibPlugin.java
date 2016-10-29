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

package de.minigameslib.mclib.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.ConfigInterface;
import de.minigameslib.mclib.api.config.ConfigServiceInterface;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.locale.MessagesConfigInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.perms.PermissionServiceInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;

/**
 * Main spigot plugin class for MCLIB.
 * 
 * @author mepeisen
 */
public class MclibPlugin extends JavaPlugin implements Listener, EnumServiceInterface, ConfigServiceInterface, MessageServiceInterface, ObjectServiceInterface, PermissionServiceInterface
{

    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(this, this);
        
        Bukkit.getServicesManager().register(EnumServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ConfigServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(MessageServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(ObjectServiceInterface.class, this, this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(PermissionServiceInterface.class, this, this, ServicePriority.Highest);
    }
    
    @Override
    public void onDisable()
    {
        Bukkit.getServicesManager().unregisterAll(this);
    }
    
    // enum services
    
    /** enumeration values, listed by plugin. */
    private final Map<Plugin, Set<Enum<?>>> enumsByPlugin = new HashMap<>();
    
    /** map from enumeration valur oto registering plugin. */
    private final Map<Enum<?>, Plugin> pluginsByEnum = new HashMap<>();

    @Override
    public void registerEnumClass(Plugin plugin, Class<Enum<?>> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> set = this.enumsByPlugin.computeIfAbsent(plugin, (p) -> new HashSet<>());
            for (final Enum<?> ev : clazz.getEnumConstants())
            {
                if (this.pluginsByEnum.containsKey(ev))
                {
                    // TODO Logging
                    throw new IllegalStateException("Duplicate registration of enum " + clazz.getName() + ":" + ev.name()); //$NON-NLS-1$ //$NON-NLS-2$
                }
                this.pluginsByEnum.put(ev, plugin);
                set.add(ev);
            }
        }
    }

    @Override
    public void unregisterAll(Plugin plugin)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> set = this.enumsByPlugin.get(plugin);
            if (set != null)
            {
                set.forEach(this.pluginsByEnum::remove);
                this.enumsByPlugin.remove(set);
            }
        }
    }

    @Override
    public Plugin getPlugin(Enum<?> enumValue)
    {
        synchronized (this.enumsByPlugin)
        {
            return this.getPlugin(enumValue);
        }
    }

    @Override
    public Set<Enum<?>> getEnumValues(Plugin plugin)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> s = this.enumsByPlugin.get(plugin);
            return s == null ? Collections.emptySet() : Collections.unmodifiableSet(s);
        }
    }

    @Override
    public <T> Set<T> getEnumValues(Plugin plugin, Class<T> clazz)
    {
        synchronized (this.enumsByPlugin)
        {
            final Set<Enum<?>> s = this.enumsByPlugin.get(plugin);
            return s == null ? Collections.emptySet() : s.stream().filter(o -> clazz.isInstance(o)).map(o -> clazz.cast(o)).collect(Collectors.toSet());
        }
    }
    
    // 

//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
//    {
//        if ("mclib".equals(command.getName()))
//        {
//            sender.sendMessage("commands: spawn, say, kill, list, uuid");
//            return false;
//        }
//        return false;
//    }
//
//    @EventHandler
//    public void onPlayerJoined(PlayerJoinEvent evt)
//    {
//        evt.setJoinMessage("HUHU");
//    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.McContext#getContext(java.lang.Class)
     */
    @Override
    public <T> T getContext(Class<T> clazz)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.McContext#setContext(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> void setContext(Class<T> clazz, T value)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.McContext#resolveContextVar(java.lang.String)
     */
    @Override
    public String resolveContextVar(String src)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.McContext#runInNewContext(de.minigameslib.mclib.api.util.function.MgRunnable)
     */
    @Override
    public void runInNewContext(McRunnable runnable) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.McContext#runInCopiedContext(de.minigameslib.mclib.api.util.function.MgRunnable)
     */
    @Override
    public void runInCopiedContext(McRunnable runnable) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.McContext#calculateInNewContext(de.minigameslib.mclib.api.util.function.MgSupplier)
     */
    @Override
    public <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.McContext#calculateInCopiedContext(de.minigameslib.mclib.api.util.function.MgSupplier)
     */
    @Override
    public <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.locale.MessageServiceInterface#getMessagesFromMsg(de.minigameslib.mclib.api.locale.LocalizedMessageInterface)
     */
    @Override
    public MessagesConfigInterface getMessagesFromMsg(LocalizedMessageInterface item)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.config.ConfigServiceInterface#getConfigFromCfg(de.minigameslib.mclib.api.config.ConfigurationValueInterface)
     */
    @Override
    public ConfigInterface getConfigFromCfg(ConfigurationValueInterface item)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.objects.ObjectServiceInterface#getPlayer(org.bukkit.entity.Player)
     */
    @Override
    public McPlayerInterface getPlayer(Player player)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.objects.ObjectServiceInterface#getPlayer(org.bukkit.OfflinePlayer)
     */
    @Override
    public McPlayerInterface getPlayer(OfflinePlayer player)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.objects.ObjectServiceInterface#getPlayer(java.util.UUID)
     */
    @Override
    public McPlayerInterface getPlayer(UUID uuid)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
