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

package de.minigameslib.fakeclient.nms110;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;

import de.minigameslib.mclib.fakeclient.IFakeClient;
import de.minigameslib.mclib.fakeclient.IFakeController;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayInChat;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.PlayerList;
import net.minecraft.server.v1_10_R1.WorldServer;

/**
 * Implementation of fake controller for 1.10
 * 
 * @author mepeisen
 */
public class FakeController110 implements IFakeController
{
    
    /** client listener. */
    private final IFakeClient client;
    
    /** players name. */
    private final String      name;

    /** the player entity during connections. */
    private EntityPlayer entityPlayer;

    /**
     * the player connection.
     */
    private FakePlayerConnection connection;

    /**
     * the uuid.
     */
    private final UUID uuid;
    
    /**
     * Constructor to create a fake client.
     * 
     * @param client
     *            the client listener.
     * @param name
     *            players name.
     */
    public FakeController110(IFakeClient client, String name)
    {
        this.client = client;
        this.name = name;
        this.uuid = UUID.nameUUIDFromBytes(("FakePlayer:" + this.name).getBytes()); //$NON-NLS-1$
    }
    
    @Override
    public void connect(Plugin plugin, Location spawn)
    {
        final WorldServer world = ((CraftWorld)spawn.getWorld()).getHandle();
        final PlayerList playerList = ((CraftServer) Bukkit.getServer()).getHandle();
        final GameProfile gameProfile = new GameProfile(this.uuid, this.name);
        this.entityPlayer = new EntityPlayer(playerList.getServer(), world, gameProfile, new PlayerInteractManager(world));
        this.connection = new FakePlayerConnection(playerList.getServer(), new FakePlayerNetworkManager(this.client), this.entityPlayer);
        
        this.entityPlayer.spawnIn(world);
        this.entityPlayer.playerInteractManager.a((WorldServer) this.entityPlayer.world);
        this.entityPlayer.playerInteractManager.b(world.getWorldData().getGameType());
        this.entityPlayer.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
        playerList.a(this.connection.networkManager, this.entityPlayer);
    }
    
    @Override
    public void disconnect()
    {
        this.connection.disconnect(""); //$NON-NLS-1$
        this.connection = null;
        this.entityPlayer = null;
    }
    
    @Override
    public Player getPlayer()
    {
        return Bukkit.getPlayer(this.uuid);
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer()
    {
        return Bukkit.getOfflinePlayer(this.uuid);
    }
    
    /**
     * checks for valid connection.
     */
    private void checkConnected()
    {
        if (this.connection == null)
        {
            throw new IllegalStateException("Player disconnected"); //$NON-NLS-1$
        }
    }
    
    @Override
    public void say(String msg)
    {
        this.checkConnected();
        
        this.connection.a(new PacketPlayInChat(msg));
    }
    
}
