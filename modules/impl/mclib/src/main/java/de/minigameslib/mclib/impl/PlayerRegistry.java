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

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.pshared.PongData;

/**
 * A helper class to register player interfaces.
 * 
 * @author mepeisen
 */
public class PlayerRegistry
{
    
    /** the well known players. */
    private final LoadingCache<UUID, McPlayerImpl> players;
    
    /** players work dir. */
    File                                           workDir;
    
    /**
     * Constructor.
     * 
     * @param workDir
     */
    public PlayerRegistry(File workDir)
    {
        this.players = CacheBuilder.newBuilder().maximumSize(McCoreConfig.PlayerRegistrySize.getInt()).expireAfterAccess(McCoreConfig.PlayerRegistryAccessMinutes.getInt(), TimeUnit.MINUTES)
            .build(new CacheLoader<UUID, McPlayerImpl>() {
                @Override
                public McPlayerImpl load(UUID key) throws Exception
                {
                    return new McPlayerImpl(key, new File(PlayerRegistry.this.workDir, key.toString() + ".yml")); //$NON-NLS-1$
                }
            });
        if (!workDir.exists())
        {
            workDir.mkdirs();
        }
        this.workDir = workDir;
    }
    
    /**
     * @param player
     * @param fragment
     */
    void parsePong(McPlayerInterface player, PongData fragment)
    {
        ((McPlayerImpl) player).parsePong(fragment);
    }
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     * @return arena player.
     */
    public McPlayerImpl getPlayer(Player player)
    {
        final UUID uuid = player.getUniqueId();
        try
        {
            return this.players.get(uuid);
        }
        catch (ExecutionException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     * @return arena player.
     */
    public McPlayerImpl getPlayer(OfflinePlayer player)
    {
        final UUID uuid = player.getUniqueId();
        try
        {
            return this.players.get(uuid);
        }
        catch (ExecutionException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Returns the player for given bukkit player uuid.
     * 
     * @param uuid
     * @return arena player.
     */
    public McPlayerImpl getPlayer(UUID uuid)
    {
        try
        {
            return this.players.get(uuid);
        }
        catch (ExecutionException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Player join event
     * 
     * @param evt
     */
    public void onPlayerJoin(PlayerJoinEvent evt)
    {
        if (evt.getPlayer() != null)
        {
            final McPlayerImpl impl = this.players.getIfPresent(evt.getPlayer().getUniqueId());
            if (impl != null)
            {
                impl.onPlayerJoin();
            }
        }
    }
    
    /**
     * Player quit event.
     * 
     * @param evt
     */
    public void onPlayerQuit(PlayerQuitEvent evt)
    {
        if (evt.getPlayer() != null)
        {
            final McPlayerImpl impl = this.players.getIfPresent(evt.getPlayer().getUniqueId());
            if (impl != null)
            {
                impl.onPlayerQuit();
            }
        }
    }
    
    /**
     * Plugin disable
     * 
     * @param plugin
     */
    public void onDisable(Plugin plugin)
    {
        for (final McPlayerImpl player : this.players.asMap().values())
        {
            player.onDisable(plugin);
        }
    }
    
}
