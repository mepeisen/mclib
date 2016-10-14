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

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;

import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NetworkManager;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayInAbilities;
import net.minecraft.server.v1_10_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_10_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_10_R1.PacketPlayInBlockPlace;
import net.minecraft.server.v1_10_R1.PacketPlayInBoatMove;
import net.minecraft.server.v1_10_R1.PacketPlayInChat;
import net.minecraft.server.v1_10_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_10_R1.PacketPlayInCloseWindow;
import net.minecraft.server.v1_10_R1.PacketPlayInCustomPayload;
import net.minecraft.server.v1_10_R1.PacketPlayInEnchantItem;
import net.minecraft.server.v1_10_R1.PacketPlayInEntityAction;
import net.minecraft.server.v1_10_R1.PacketPlayInFlying;
import net.minecraft.server.v1_10_R1.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_10_R1.PacketPlayInKeepAlive;
import net.minecraft.server.v1_10_R1.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_10_R1.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_10_R1.PacketPlayInSettings;
import net.minecraft.server.v1_10_R1.PacketPlayInSpectate;
import net.minecraft.server.v1_10_R1.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_10_R1.PacketPlayInTabComplete;
import net.minecraft.server.v1_10_R1.PacketPlayInTeleportAccept;
import net.minecraft.server.v1_10_R1.PacketPlayInTransaction;
import net.minecraft.server.v1_10_R1.PacketPlayInUpdateSign;
import net.minecraft.server.v1_10_R1.PacketPlayInUseEntity;
import net.minecraft.server.v1_10_R1.PacketPlayInUseItem;
import net.minecraft.server.v1_10_R1.PacketPlayInVehicleMove;
import net.minecraft.server.v1_10_R1.PacketPlayInWindowClick;
import net.minecraft.server.v1_10_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_10_R1.PlayerConnection;

/**
 * @author mepeisen
 *
 */
class FakePlayerConnection extends PlayerConnection
{
//    
//    /**
//     * disconnect flag.
//     */
//    private boolean disconnected = false;

    /**
     * @param minecraftserver
     * @param networkmanager
     * @param entityplayer
     */
    public FakePlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer)
    {
        super(minecraftserver, networkmanager, entityplayer);
    }

    @Override
    public CraftPlayer getPlayer()
    {
        return super.getPlayer();
    }

    @Override
    public void E_()
    {
        // empty
    }

    @Override
    public NetworkManager a()
    {
        return super.a();
    }

    @Override
    public void disconnect(String s)
    {
        super.disconnect(s);
    }

    @Override
    public void a(PacketPlayInSteerVehicle packetplayinsteervehicle)
    {
        super.a(packetplayinsteervehicle);
    }

    @Override
    public void a(PacketPlayInVehicleMove packetplayinvehiclemove)
    {
        super.a(packetplayinvehiclemove);
    }

    @Override
    public void a(PacketPlayInTeleportAccept packetplayinteleportaccept)
    {
        super.a(packetplayinteleportaccept);
    }

    @Override
    public void a(PacketPlayInFlying packetplayinflying)
    {
        super.a(packetplayinflying);
    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1)
    {
        super.a(d0, d1, d2, f, f1);
    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1, Set<EnumPlayerTeleportFlags> set)
    {
        super.a(d0, d1, d2, f, f1, set);
    }

    @Override
    public void teleport(Location dest)
    {
        super.teleport(dest);
    }

    @Override
    public void a(PacketPlayInBlockDig packetplayinblockdig)
    {
        super.a(packetplayinblockdig);
    }

    @Override
    public void a(PacketPlayInUseItem packetplayinuseitem)
    {
        super.a(packetplayinuseitem);
    }

    @Override
    public void a(PacketPlayInBlockPlace packetplayinblockplace)
    {
        super.a(packetplayinblockplace);
    }

    @Override
    public void a(PacketPlayInSpectate packetplayinspectate)
    {
        super.a(packetplayinspectate);
    }

    @Override
    public void a(PacketPlayInResourcePackStatus packetplayinresourcepackstatus)
    {
        super.a(packetplayinresourcepackstatus);
    }

    @Override
    public void a(PacketPlayInBoatMove packetplayinboatmove)
    {
        super.a(packetplayinboatmove);
    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent)
    {
        super.a(ichatbasecomponent);
    }

    @Override
    public void sendPacket(Packet<?> packet)
    {
        super.sendPacket(packet);
    }

    @Override
    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot)
    {
        super.a(packetplayinhelditemslot);
    }

    @Override
    public void a(PacketPlayInChat packetplayinchat)
    {
        super.a(packetplayinchat);
    }

    @Override
    public void chat(String s, boolean async)
    {
        super.chat(s, async);
    }

    @Override
    public void a(PacketPlayInArmAnimation packetplayinarmanimation)
    {
        super.a(packetplayinarmanimation);
    }

    @Override
    public void a(PacketPlayInEntityAction packetplayinentityaction)
    {
        super.a(packetplayinentityaction);
    }

    @Override
    public void a(PacketPlayInUseEntity packetplayinuseentity)
    {
        super.a(packetplayinuseentity);
    }

    @Override
    public void a(PacketPlayInClientCommand packetplayinclientcommand)
    {
        super.a(packetplayinclientcommand);
    }

    @Override
    public void a(PacketPlayInCloseWindow packetplayinclosewindow)
    {
        super.a(packetplayinclosewindow);
    }

    @Override
    public void a(PacketPlayInWindowClick packetplayinwindowclick)
    {
        super.a(packetplayinwindowclick);
    }

    @Override
    public void a(PacketPlayInEnchantItem packetplayinenchantitem)
    {
        super.a(packetplayinenchantitem);
    }

    @Override
    public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot)
    {
        super.a(packetplayinsetcreativeslot);
    }

    @Override
    public void a(PacketPlayInTransaction packetplayintransaction)
    {
        super.a(packetplayintransaction);
    }

    @Override
    public void a(PacketPlayInUpdateSign packetplayinupdatesign)
    {
        super.a(packetplayinupdatesign);
    }

    @Override
    public void a(PacketPlayInKeepAlive packetplayinkeepalive)
    {
        super.a(packetplayinkeepalive);
    }

    @Override
    public void a(PacketPlayInAbilities packetplayinabilities)
    {
        super.a(packetplayinabilities);
    }

    @Override
    public void a(PacketPlayInTabComplete packetplayintabcomplete)
    {
        super.a(packetplayintabcomplete);
    }

    @Override
    public void a(PacketPlayInSettings packetplayinsettings)
    {
        super.a(packetplayinsettings);
    }

    @Override
    public void a(PacketPlayInCustomPayload packetplayincustompayload)
    {
        super.a(packetplayincustompayload);
    }
    
}
