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

package de.minigameslib.fakeclient.nms111;

import java.lang.reflect.Field;
import java.net.SocketAddress;

import javax.crypto.SecretKey;

import de.minigameslib.mclib.fakeclient.IFakeClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.v1_11_R1.EnumProtocol;
import net.minecraft.server.v1_11_R1.EnumProtocolDirection;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.NetworkManager;
import net.minecraft.server.v1_11_R1.Packet;
import net.minecraft.server.v1_11_R1.PacketListener;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

/**
 * @author mepeisen
 *
 */
class FakePlayerNetworkManager extends NetworkManager
{

    /** i chat base component. */
    private IChatBaseComponent ichatbasecomponent;
    
    /** fake client for event dispatching. */
    private IFakeClient client;

    /**
     * Constructor
     * @param client 
     */
    public FakePlayerNetworkManager(IFakeClient client)
    {
        super(EnumProtocolDirection.CLIENTBOUND);
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelhandlercontext) throws Exception
    {
        // does nothing
    }

    @Override
    public void setProtocol(EnumProtocol enumprotocol)
    {
        // does nothing
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelhandlercontext) throws Exception
    {
        // does nothing
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable) throws Exception
    {
        // does nothing
    }

    @Override
    protected void a(ChannelHandlerContext channelhandlercontext, Packet<?> packet) throws Exception
    {
        // does nothing
    }

    @Override
    public void setPacketListener(PacketListener packetlistener)
    {
        super.setPacketListener(packetlistener);
    }

    @Override
    public void sendPacket(Packet<?> packet)
    {
        try
        {
            if (packet instanceof PacketPlayOutChat)
            {
                final Field field = packet.getClass().getDeclaredField("a"); // IChatBaseComponent //$NON-NLS-1$
                field.setAccessible(true);
                final IChatBaseComponent chat = (IChatBaseComponent) field.get(packet);
                this.client.onChatMessage(chat.getText());
            }
            // does nothing
//            if (!(packet instanceof PacketPlayOutRelEntityMove || packet instanceof PacketPlayOutEntityVelocity || packet instanceof PacketPlayOutRelEntityMoveLook || packet instanceof PacketPlayOutNamedSoundEffect || packet instanceof PacketPlayOutEntityTeleport || packet instanceof PacketPlayOutEntityHeadRotation || packet instanceof PacketPlayOutUpdateTime || packet instanceof PacketPlayOutEntityLook))
//            System.out.println("sendPacket: " + packet);
        }
        catch (Exception ex)
        {
            // TODO Logging
            ex.printStackTrace();
        }
    }

    @Override
    public void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericfuturelistener,
            GenericFutureListener<? extends Future<? super Void>>... agenericfuturelistener)
    {
        // does nothing
    }

    @Override
    public void a()
    {
        // does nothing
    }

    @Override
    public SocketAddress getSocketAddress()
    {
        return new SocketAddress() {
            /**
             * serial version uid
             */
            private static final long serialVersionUID = -1762123857703599664L;
        };
    }

    @Override
    public void close(IChatBaseComponent ichatbasecomponent)
    {
        this.ichatbasecomponent = ichatbasecomponent;
    }

    @Override
    public boolean isLocal()
    {
        return false;
    }

    @Override
    public void a(SecretKey secretkey)
    {
        // empty
    }

    @Override
    public boolean isConnected()
    {
        return true;
    }

    @Override
    public boolean h()
    {
        return false;
    }

    @Override
    public PacketListener i()
    {
        return super.i();
    }

    @Override
    public IChatBaseComponent j()
    {
        return this.ichatbasecomponent;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet object) throws Exception
    {
        // empty
    }

    @Override
    public void stopReading()
    {
        // empty
    }
    
}
