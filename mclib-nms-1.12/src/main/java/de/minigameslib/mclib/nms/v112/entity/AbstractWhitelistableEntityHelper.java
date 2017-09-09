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

package de.minigameslib.mclib.nms.v112.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.nms.v112.entity.DummyHuman1_12.HumanNpc;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PlayerConnection;

/**
 * Interface for entities that can be whitelisted.
 * 
 * @author mepeisen
 */
public abstract class AbstractWhitelistableEntityHelper
{
    
    /** update task. */
    private BukkitRunnable updateTask;
    
    /** tracked players. */
    Set<UUID>              trackedPlayers = new HashSet<>();
    
    /** players in range. */
    Map<UUID, Boolean>     inRange        = new HashMap<>();
    
    /** respawn flag. */
    boolean                respawn;
    
    /**
     * {@code true} to check the whitelist.
     */
    boolean                hidden;
    
    /** the whitelist. */
    final Set<UUID>        whitelist      = new HashSet<>();
    
    /**
     * Constructor.
     */
    public AbstractWhitelistableEntityHelper()
    {
        this.updateTask = new BukkitRunnable() {
            
            @Override
            public void run()
            {
                for (final UUID uuid : AbstractWhitelistableEntityHelper.this.trackedPlayers)
                {
                    final Player player = Bukkit.getPlayer(uuid);
                    if (player != null)
                    {
                        final PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
                        boolean newInRange = getDistanceSquared(player) < 64 * 64;
                        if (AbstractWhitelistableEntityHelper.this.hidden)
                        {
                            newInRange &= AbstractWhitelistableEntityHelper.this.whitelist.contains(player.getUniqueId());
                        }
                        
                        if (AbstractWhitelistableEntityHelper.this.respawn)
                        {
                            // if forced resapwn (teleport etc.) and it is no more in range we delete the entity so that it disappears
                            if (!newInRange)
                            {
                                AbstractWhitelistableEntityHelper.this.sendOutOfRangePackages(player, con);
                            }
                            // override inrange to false so that players still in range will be forced to get a clean respawn
                            AbstractWhitelistableEntityHelper.this.inRange.put(uuid, Boolean.FALSE);
                        }
                        
                        boolean oldInRange = AbstractWhitelistableEntityHelper.this.inRange.get(uuid);
                        if (newInRange != oldInRange)
                        {
                            if (!oldInRange)
                            {
                                AbstractWhitelistableEntityHelper.this.sendInRangePackages(player, con);
                            }
                            AbstractWhitelistableEntityHelper.this.inRange.put(uuid, newInRange);
                        }
                    }
                }
                AbstractWhitelistableEntityHelper.this.respawn = false;
            }
        };
        this.updateTask.runTaskTimer((Plugin) McLibInterface.instance(), 0, 30);
    }
    
    /**
     * Returns the location of this entity.
     * 
     * @return location
     */
    protected abstract Location getLocation();
    
    /**
     * Send packages for being in range. Do spawn the entity.
     * 
     * @param player
     *            player instance
     * @param con
     *            connection
     */
    protected abstract void sendInRangePackages(Player player, PlayerConnection con);
    
    /**
     * Send packages for being out of range. Do kill the entity.
     * 
     * @param player
     *            player instance
     * @param con
     *            connection
     */
    protected abstract void sendOutOfRangePackages(Player player, PlayerConnection con);
    
    /**
     * Send packages for untracking the entity.
     * 
     * @param player
     *            player instance
     * @param con
     *            connection
     */
    protected abstract void sendUntrackPackages(Player player, PlayerConnection con);
    
    /**
     * Send packages for tracking the entity.
     * 
     * @param player
     *            player instance
     * @param con
     *            connection
     */
    protected abstract void sendTrackPackages(Player player, PlayerConnection con);
    
    /**
     * Marks all players to respawn the entity.
     */
    public void respawnAll()
    {
        this.trackedPlayers.forEach(u -> this.inRange.put(u, false));
    }
    
    /**
     * Hides the entity with given whitelist.
     * 
     * @param wl
     *            the whitelist
     */
    public void hide(List<UUID> wl)
    {
        this.whitelist.clear();
        this.hidden = true;
        this.whitelist.addAll(wl);
        this.respawn = true;
    }
    
    /**
     * Show the entity to all.
     */
    public void show()
    {
        this.hidden = false;
        this.whitelist.clear();
        this.respawn = true;
    }
    
    /**
     * untrack given player.
     * 
     * @param player
     *            target player
     */
    public void untrack(Player player)
    {
        if (!(player instanceof HumanNpc))
        {
            new BukkitRunnable() {
                
                @Override
                public void run()
                {
                    AbstractWhitelistableEntityHelper.this.trackedPlayers.remove(player.getUniqueId());
                    AbstractWhitelistableEntityHelper.this.inRange.remove(player.getUniqueId());
                    final PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
                    AbstractWhitelistableEntityHelper.this.sendUntrackPackages(player, con);
                }
            }.runTaskLater((Plugin) McLibInterface.instance(), 1);
        }
    }
    
    /**
     * track given player.
     * 
     * @param player
     *            target player
     */
    public void track(Player player)
    {
        if (!(player instanceof HumanNpc))
        {
            new BukkitRunnable() {
                
                @Override
                public void run()
                {
                    AbstractWhitelistableEntityHelper.this.trackedPlayers.add(player.getUniqueId());
                    AbstractWhitelistableEntityHelper.this.inRange.put(player.getUniqueId(), Boolean.FALSE);
                    final PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
                    AbstractWhitelistableEntityHelper.this.sendTrackPackages(player, con);
                }
            }.runTaskLater((Plugin) McLibInterface.instance(), 1);
        }
    }
    
    /**
     * Delete/cleanup the human.
     */
    public void delete()
    {
        this.updateTask.cancel();
        this.trackedPlayers.clear();
        this.inRange.clear();
    }
    
    /**
     * do respawn.
     */
    public void respawn()
    {
        this.respawn = true;
    }
    
    /**
     * Returns the squared distance between this human and players.
     * 
     * @param player
     *            target player
     * @return squared distance
     */
    protected double getDistanceSquared(Player player)
    {
        final Location loc = player.getLocation();
        final Location thisloc = this.getLocation();
        double dx = loc.getX() - thisloc.getX();
        double dy = loc.getY() - thisloc.getY();
        double dz = loc.getZ() - thisloc.getZ();
        return dx * dx + dy * dy + dz * dz;
    }
    
    /**
     * Sends packets to given connection with given delay.
     * 
     * @param con
     *            target connection
     * @param delay
     *            delay (ticks)
     * @param packets
     *            packets to send
     */
    protected void sendPackages(PlayerConnection con, int delay, Packet<?>... packets)
    {
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                for (final Packet<?> packet : packets)
                {
                    con.sendPacket(packet);
                }
            }
        }.runTaskLater((Plugin) McLibInterface.instance(), delay);
    }
    
}
