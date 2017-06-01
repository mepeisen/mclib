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

package de.minigames.mclib.nms.v185.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.nms.api.MessageUtil;
import de.minigameslib.mclib.nms.api.SignHelperInterface;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTileEntityData;
import net.minecraft.server.v1_8_R3.PlayerConnection;

/**
 * Sign helper.
 * 
 * @author mepeisen
 *
 */
public class SignHelper1_8_5 implements SignHelperInterface
{
    
    /** dummy signs. */
    private static final EntityWithWhitelistHelper<SignImpl> SIGNS      = new EntityWithWhitelistHelper<>();
    
    @Override
    public SignNmsInterface create(Location location)
    {
        final SignImpl sign = new SignImpl(location);
        SIGNS.add(sign);
        return sign;
    }
    
    @Override
    public void playerOnline(Player player)
    {
        SIGNS.playerOnline(player);
    }
    
    @Override
    public void playerOffline(Player player)
    {
        SIGNS.playerOffline(player);
    }
    
    @Override
    public void delete(SignNmsInterface entity)
    {
        SIGNS.delete((SignImpl) entity);
    }
    
    /**
     * nms impl of hologram entity.
     */
    private static final class SignImpl extends AbstractWhitelistableEntityHelper implements SignNmsInterface
    {
        
        /** location. */
        private Location           location;
        
        /** lines content. */
        private Serializable[] strings = new Serializable[0];
        
        /**
         * Constructor.
         * 
         * @param location
         *            initial location.
         */
        public SignImpl(Location location)
        {
            this.location = location;
        }
        
        @Override
        public void setNewLocation(Location newLocation)
        {
            this.location = newLocation;
            this.respawn();
        }
        
        @Override
        public void updateLines(List<Serializable> list)
        {
            this.strings = list.toArray(new Serializable[list.size()]);
            this.respawn();
        }
        
        @Override
        protected Location getLocation()
        {
            return this.location;
        }
        
        @Override
        protected void sendInRangePackages(Player player, PlayerConnection con)
        {
            final String[] text = MessageUtil.convert(player, this.strings);
            final List<Packet<?>> packets = new ArrayList<>();
            
            final NBTTagCompound tag = new NBTTagCompound();
            tag.setString("id", "Sign"); //$NON-NLS-1$ //$NON-NLS-2$
            tag.setInt("x", this.location.getBlockX()); //$NON-NLS-1$
            tag.setInt("y", this.location.getBlockY()); //$NON-NLS-1$
            tag.setInt("z", this.location.getBlockZ()); //$NON-NLS-1$
            for (int i = 0; i < 4; i++)
            {
                final String s = IChatBaseComponent.ChatSerializer.a(new ChatComponentText(text.length > i ? text[i] : "")); //$NON-NLS-1$
                tag.setString("Text" + (i + 1), s); //$NON-NLS-1$
            }
            packets.add(new PacketPlayOutTileEntityData(
                new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ()),
                9, 
                tag));
            
            sendPackages(con, 1, packets.toArray(new Packet[packets.size()]));
        }
        
        @Override
        protected void sendOutOfRangePackages(Player player, PlayerConnection con)
        {
            // empty
        }
        
        @Override
        protected void sendUntrackPackages(Player player, PlayerConnection con)
        {
            // empty
        }
        
        @Override
        protected void sendTrackPackages(Player player, PlayerConnection con)
        {
            // empty
        }
        
    }
    
}
