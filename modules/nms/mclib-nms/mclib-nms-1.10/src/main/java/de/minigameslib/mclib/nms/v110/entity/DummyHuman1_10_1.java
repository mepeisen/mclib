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

package de.minigameslib.mclib.nms.v110.entity;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;

import de.minigameslib.mclib.api.McLibInterface;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.EnumProtocolDirection;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NetworkManager;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.WorldServer;

/**
 * @author mepeisen
 *
 */
public class DummyHuman1_10_1 extends EntityPlayer
{
    
    /** update task. */
    private BukkitRunnable updateTask;
    
    /** tracked players. */
    Set<UUID> trackedPlayers = new HashSet<>();
    
    /** players in range. */
    Map<UUID, Boolean> inRange = new HashMap<>();

    /**
     * @param minecraftserver
     * @param worldserver
     * @param gameprofile
     * @param playerinteractmanager
     * @param loc 
     */
    public DummyHuman1_10_1(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager, Location loc)
    {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
        // playerinteractmanager.setGameMode(EnumGamemode.SURVIVAL);
        this.setPosition(loc);
        
        // final Socket socket = new DummySocket();
        final NetworkManager conn = new DummyNetworkManager(EnumProtocolDirection.CLIENTBOUND);
        this.playerConnection = new DummyNetHandler(minecraftserver, conn, this);
        conn.setPacketListener(this.playerConnection);
        this.getBukkitEntity().setSleepingIgnored(true);
        
        this.spawnIn(this.world);
        this.playerInteractManager.a((WorldServer) this.world);
        this.playerInteractManager.b(this.world.getWorldData().getGameType());
        ((CraftServer)Bukkit.getServer()).getHandle().a(conn, this);
        
        this.updateTask = new BukkitRunnable() {
            
            @Override
            public void run()
            {
                for (final UUID uuid : DummyHuman1_10_1.this.trackedPlayers)
                {
                    final Player player = Bukkit.getPlayer(uuid);
                    if (player != null)
                    {
                        boolean newInRange = getDistanceSquared(player) < 64*64;
                        boolean oldInRange = DummyHuman1_10_1.this.inRange.get(uuid);
                        if (newInRange != oldInRange)
                        {
                            if (!oldInRange)
                            {
                                final PlayerConnection con = ((CraftPlayer)player).getHandle().playerConnection;
                                new BukkitRunnable() {
                                    
                                    @Override
                                    public void run()
                                    {
                                        con.sendPacket(new PacketPlayOutEntityDestroy(DummyHuman1_10_1.this.getId()));
                                        con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, DummyHuman1_10_1.this));
                                        con.sendPacket(new PacketPlayOutNamedEntitySpawn(DummyHuman1_10_1.this));
                                    }
                                }.runTaskLater((Plugin) McLibInterface.instance(), 1);
                                new BukkitRunnable() {
                                    
                                    @Override
                                    public void run()
                                    {
                                        con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, DummyHuman1_10_1.this));
                                    }
                                }.runTaskLater((Plugin) McLibInterface.instance(), 3);
                            }
                            DummyHuman1_10_1.this.inRange.put(uuid, newInRange);
                        }
                    }
                }
            }
        };
        this.updateTask.runTaskTimer((Plugin) McLibInterface.instance(), 0, 30);
    }
    
    @Override
    public IChatBaseComponent getPlayerListName()
    {
        return null;
    }

    /**
     * Marks all players to respawn the entity
     */
    public void respawnAll()
    {
        this.trackedPlayers.forEach(u -> this.inRange.put(u, false));
    }
    
    /**
     * Returns the suawred distance between this human and players
     * @param player
     * @return squared distance
     */
    protected double getDistanceSquared(Player player)
    {
        Location loc = player.getLocation();
        double dx = loc.getX() - this.locX;
        double dy = loc.getY() - this.locY;
        double dz = loc.getZ() - this.locZ;
        return dx * dx + dy * dy + dz * dz;
    }
    
    /**
     * untrack given player
     * @param player
     */
    public void untrack(Player player)
    {
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                DummyHuman1_10_1.this.trackedPlayers.remove(player.getUniqueId());
                DummyHuman1_10_1.this.inRange.remove(player.getUniqueId());
            }
        }.runTaskLater((Plugin) McLibInterface.instance(), 1);
    }
    
    /**
     * track given player
     * @param player
     */
    public void track(Player player)
    {
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                DummyHuman1_10_1.this.trackedPlayers.add(player.getUniqueId());
                DummyHuman1_10_1.this.inRange.put(player.getUniqueId(), Boolean.FALSE);
                final PlayerConnection con = ((CraftPlayer)player).getHandle().playerConnection;
                con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, DummyHuman1_10_1.this));
            }
        }.runTaskLater((Plugin) McLibInterface.instance(), 1);
    }
    
    /**
     * Delete/cleanup the human
     */
    public void delete()
    {
        this.updateTask.cancel();
        this.trackedPlayers.clear();
        this.inRange.clear();
    }
    
    @Override
    public void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition)
    {
        // do nothing (falling)
    }

    @Override
    public boolean d(NBTTagCompound save)
    {
        // check if entity will be saved.
        return false;
    }

    @Override
    public void e(float f1, float f2)
    {
        // do nothing (jump)
    }

    @Override
    public void g(double x, double y, double z)
    {
        // do nothing (move)
    }

    @Override
    public void g(float f1, float f2)
    {
        // do nothing (flying)
    }

    @Override
    public boolean m_()
    {
        // do nothing (ladder)
        return false;
    }

    @Override
    public CraftPlayer getBukkitEntity()
    {
        if (this.bukkitEntity == null)
        {
            this.bukkitEntity = new HumanNPC(this);
        }
        return super.getBukkitEntity();
    }

    /**
     * @param loc
     */
    void setPosition(Location loc)
    {
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
    
    /**
     * Bukkit npc class
     */
    private static final class HumanNPC extends CraftPlayer
    {
        
        /** craft server. */
        private CraftServer spigot;
        
        /**
         * Constructor.
         * @param handle
         */
        public HumanNPC(DummyHuman1_10_1 handle)
        {
            super((CraftServer) Bukkit.getServer(), handle);
            this.spigot = (CraftServer) Bukkit.getServer();
        }

        @Override
        public List<MetadataValue> getMetadata(String metadataKey)
        {
            return this.spigot.getEntityMetadata().getMetadata(this, metadataKey);
        }

        @Override
        public boolean hasMetadata(String metadataKey)
        {
            return this.spigot.getEntityMetadata().hasMetadata(this, metadataKey);
        }

        @Override
        public void removeMetadata(String metadataKey, Plugin owningPlugin)
        {
            this.spigot.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
        }

        @Override
        public void setMetadata(String metadataKey, MetadataValue newMetadataValue)
        {
            this.spigot.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
        }
        
    }
    
    /**
     * Dummy net handler
     */
    private static final class DummyNetHandler extends PlayerConnection
    {
        /**
         * Constructor
         * @param minecraftServer
         * @param networkManager
         * @param entityPlayer
         */
        public DummyNetHandler(MinecraftServer minecraftServer, NetworkManager networkManager, EntityPlayer entityPlayer)
        {
            super(minecraftServer, networkManager, entityPlayer);
        }
        
        @Override
        public void sendPacket(Packet<?> packet)
        {
            // do nothing
        }
    }
    
    /**
     * A dummy network manager
     */
    private static final class DummyNetworkManager extends NetworkManager
    {
        
        /**
         * @param direction
         */
        public DummyNetworkManager(EnumProtocolDirection direction)
        {
            super(direction);
            this.l = new SocketAddress() {
                /**
                 * serial version uid.
                 */
                private static final long serialVersionUID = 7457636017308216203L;
            };
            this.channel = new DummyChannel();
        }
        
    }
    
    /**
     * Dummy netty channel
     */
    private static final class DummyChannel extends AbstractChannel
    {
        /**
         * Constructor
         */
        protected DummyChannel()
        {
            super(null);
        }
        
        /** channel config. */
        private final ChannelConfig config = new DefaultChannelConfig(this);
        
        @Override
        public ChannelConfig config()
        {
            this.config.setAutoRead(true);
            return this.config;
        }
        
        @Override
        public boolean isActive()
        {
            // do nothing
            return false;
        }
        
        @Override
        public boolean isOpen()
        {
            // do nothing
            return false;
        }
        
        @Override
        public ChannelMetadata metadata()
        {
            // do nothing
            return null;
        }
        
        @Override
        protected void doBeginRead() throws Exception
        {
            // do nothing
        }
        
        @Override
        protected void doBind(SocketAddress paramSocketAddress) throws Exception
        {
            // do nothing
        }
        
        @Override
        protected void doClose() throws Exception
        {
            // do nothing
        }
        
        @Override
        protected void doDisconnect() throws Exception
        {
            // do nothing
        }
        
        @Override
        protected void doWrite(ChannelOutboundBuffer paramChannelOutboundBuffer) throws Exception
        {
            // do nothing
        }
        
        @Override
        protected boolean isCompatible(EventLoop paramEventLoop)
        {
            // do nothing
            return true;
        }
        
        @Override
        protected SocketAddress localAddress0()
        {
            // do nothing
            return null;
        }
        
        @Override
        protected AbstractUnsafe newUnsafe()
        {
            // do nothing
            return null;
        }
        
        @Override
        protected SocketAddress remoteAddress0()
        {
            // do nothing
            return null;
        }
        
    }
    
}
