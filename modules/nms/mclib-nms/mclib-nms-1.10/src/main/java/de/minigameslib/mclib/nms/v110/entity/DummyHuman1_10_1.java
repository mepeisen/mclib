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
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;

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
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NetworkManager;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.WorldServer;

/**
 * A dummy human entity.
 * 
 * @author mepeisen
 *
 */
public class DummyHuman1_10_1 extends EntityPlayer
{
    
    /** helper. */
    private EntityHelper helper = null;
    
    /**
     * Constructor.
     * 
     * @param minecraftserver
     *            active minecraft server
     * @param worldserver
     *            target world
     * @param gameprofile
     *            game profile to use
     * @param playerinteractmanager
     *            the player interact manager
     * @param loc
     *            spawn location
     */
    public DummyHuman1_10_1(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager, Location loc)
    {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
        this.helper = new EntityHelper();
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
        ((CraftServer) Bukkit.getServer()).getHandle().a(conn, this);
    }
    
    /**
     * Returns the whitelistable entity helper.
     * 
     * @return entity helper.
     */
    protected EntityHelper getHelper()
    {
        return this.helper;
    }
    
    /**
     * Converts yaw/pitch value to byte degrees.
     * 
     * @param value
     *            float value.
     * @return degrees
     */
    static byte toAngle(float value)
    {
        return (byte) MathHelper.d(value * 256.0F / 360.0F);
    }
    
    @Override
    public IChatBaseComponent getPlayerListName()
    {
        return null;
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
        Location loc = player.getLocation();
        double dx = loc.getX() - this.locX;
        double dy = loc.getY() - this.locY;
        double dz = loc.getZ() - this.locZ;
        return dx * dx + dy * dy + dz * dz;
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
            this.bukkitEntity = new HumanNpc(this);
        }
        return super.getBukkitEntity();
    }
    
    /**
     * Set entity position.
     * 
     * @param loc
     *            target location.
     */
    void setPosition(Location loc)
    {
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
    
    @Override
    public void setPosition(double d0, double d1, double d2)
    {
        super.setPosition(d0, d1, d2);
        if (this.helper != null)
        {
            this.helper.respawn();
        }
    }
    
    /**
     * Bukkit npc class.
     */
    protected static final class HumanNpc extends CraftPlayer
    {
        
        /** craft server. */
        private CraftServer spigot;
        
        /**
         * Constructor.
         * 
         * @param handle
         *            nms handle
         */
        public HumanNpc(DummyHuman1_10_1 handle)
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
     * Dummy net handler.
     */
    private static final class DummyNetHandler extends PlayerConnection
    {
        /**
         * Constructor.
         * 
         * @param minecraftServer
         *            active minecraft server
         * @param networkManager
         *            network manager
         * @param entityPlayer
         *            target entity
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
     * A dummy network manager.
     */
    private static final class DummyNetworkManager extends NetworkManager
    {
        
        /**
         * Constructor.
         * 
         * @param direction
         *            protocol direction
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
     * Dummy netty channel.
     */
    private static final class DummyChannel extends AbstractChannel
    {
        /**
         * Constructor.
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
    
    /**
     * entity helper.
     */
    public final class EntityHelper extends AbstractWhitelistableEntityHelper
    {
        
        /**
         * The constructor for entity helper.
         */
        public EntityHelper()
        {
            // empty
        }
        
        @Override
        protected Location getLocation()
        {
            return DummyHuman1_10_1.this.getBukkitEntity().getLocation();
        }
        
        @Override
        protected void sendInRangePackages(Player player, PlayerConnection con)
        {
            final byte encodedyaw = toAngle(DummyHuman1_10_1.this.yaw);
            float body = DummyHuman1_10_1.this.yaw + 45;
            if (body >= 180)
            {
                body -= 360;
            }
            final byte encodedbody = toAngle(body);
            sendPackages(con, 1,
                new PacketPlayOutEntityDestroy(DummyHuman1_10_1.this.getId()),
                new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, DummyHuman1_10_1.this),
                new PacketPlayOutNamedEntitySpawn(DummyHuman1_10_1.this));
            sendPackages(con, 2,
                new PacketPlayOutEntityHeadRotation(DummyHuman1_10_1.this, encodedyaw),
                new PacketPlayOutEntity.PacketPlayOutEntityLook(DummyHuman1_10_1.this.getId(), encodedbody, toAngle(DummyHuman1_10_1.this.pitch), true));
            sendPackages(con, 4,
                new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, DummyHuman1_10_1.this));
        }
        
        @Override
        protected void sendOutOfRangePackages(Player player, PlayerConnection con)
        {
            sendPackages(con, 1,
                new PacketPlayOutEntityDestroy(DummyHuman1_10_1.this.getId()));
        }
        
        @Override
        protected void sendUntrackPackages(Player player, PlayerConnection con)
        {
            // does nothing
        }
        
        @Override
        protected void sendTrackPackages(Player player, PlayerConnection con)
        {
            con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, DummyHuman1_10_1.this));
        }
        
    }
    
}
