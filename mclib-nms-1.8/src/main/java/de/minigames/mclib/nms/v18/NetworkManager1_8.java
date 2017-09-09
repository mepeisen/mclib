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

package de.minigames.mclib.nms.v18;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.items.ResourceServiceInterface.ResourcePackStatus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R1.EnumResourcePackStatus;
import net.minecraft.server.v1_8_R1.NetworkManager;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PacketPlayInResourcePackStatus;

/**
 * A special network manager for 1.8
 * 
 * @author mepeisen
 *
 */
public class NetworkManager1_8
{
    
    /**
     * reflection nms field
     */
    private static Field channelField;
    
    /**
     * reflection nms field
     */
    static Field         enumField;
    
    static
    {
        try
        {
            channelField = NetworkManager.class.getDeclaredField("i"); //$NON-NLS-1$
            channelField.setAccessible(true);
            enumField = PacketPlayInResourcePackStatus.class.getDeclaredField("b"); //$NON-NLS-1$
            enumField.setAccessible(true);
        }
        catch (NoSuchFieldException ex)
        {
            // TODO logging
        }
    }
    
    /**
     * Hook into network traffic and listen for resource pack status changes. Spigot 1.8 does not have the proper event to listen for this packet.
     * 
     * @param player
     * @param consumer
     */
    public static void hookResourcePackStatus(Player player, BiConsumer<Player, ResourcePackStatus> consumer)
    {
        final NetworkManager manager = ((CraftPlayer) player).getHandle().playerConnection.networkManager;
        Channel channel = null;
        try
        {
            channel = (Channel) channelField.get(manager);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            // TODO logging
            return;
        }
        
        final ChannelPipeline pipe = channel.pipeline();
        
        final ChannelHandler handler = new MessageToMessageDecoder<Packet>() {
            @Override
            protected void decode(ChannelHandlerContext chc, Packet packet, List<Object> out) throws Exception
            {
                if (packet instanceof PacketPlayInResourcePackStatus)
                {
                    try
                    {
                        final EnumResourcePackStatus status = (EnumResourcePackStatus) enumField.get(packet);
                        switch (status)
                        {
                            case ACCEPTED:
                                consumer.accept(player, ResourcePackStatus.ACCEPTED);
                                break;
                            case DECLINED:
                                consumer.accept(player, ResourcePackStatus.DECLINED);
                                break;
                            case FAILED_DOWNLOAD:
                                consumer.accept(player, ResourcePackStatus.FAILED_DOWNLOAD);
                                break;
                            case SUCCESSFULLY_LOADED:
                                consumer.accept(player, ResourcePackStatus.SUCCESSFULLY_LOADED);
                                break;
                            default:
                                break;
                        }
                    }
                    catch (IllegalArgumentException | IllegalAccessException e)
                    {
                        // TODO logging
                    }
                }
                out.add(packet);
            }
        };
        
        pipe.addAfter(
            "decoder", //$NON-NLS-1$
            "mclib", //$NON-NLS-1$
            handler);
    }
    
}
