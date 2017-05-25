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

package de.minigames.mclib.nms.v19.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.nms.api.HologramHelperInterface;
import net.minecraft.server.v1_9_R1.AxisAlignedBB;
import net.minecraft.server.v1_9_R1.DamageSource;
import net.minecraft.server.v1_9_R1.EntityArmorStand;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.EnumHand;
import net.minecraft.server.v1_9_R1.EnumInteractionResult;
import net.minecraft.server.v1_9_R1.EnumItemSlot;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_9_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_9_R1.PlayerConnection;
import net.minecraft.server.v1_9_R1.SoundEffect;
import net.minecraft.server.v1_9_R1.Vec3D;
import net.minecraft.server.v1_9_R1.World;

/**
 * Hologram helper.
 * 
 * @author mepeisen
 *
 */
public class HologramHelper1_9 implements HologramHelperInterface
{

    /**
     * The line height.
     */
    static final double LineHeight = 0.25;
    
    /** dummy humans. */
    private static final EntityWithWhitelistHelper<HologramEntityImpl> LINES      = new EntityWithWhitelistHelper<>();
    
    @Override
    public HologramEntityInterface create(Location location)
    {
        final HologramEntityImpl hologram = new HologramEntityImpl(location);
        LINES.add(hologram);
        return hologram;
    }
    
    @Override
    public void playerOnline(Player player)
    {
        LINES.playerOnline(player);
    }
    
    @Override
    public void playerOffline(Player player)
    {
        LINES.playerOffline(player);
    }
    
    @Override
    public void delete(HologramEntityInterface entity)
    {
        LINES.delete((HologramEntityImpl) entity);
    }
    
    /**
     * A single hologram line.
     */
    private static final class HologramLine extends EntityArmorStand
    {
        
        /** lock tick. */
        private boolean lockTick;
        
        /**
         * Constructor.
         * 
         * @param world
         *            the world.
         */
        public HologramLine(World world)
        {
            super(world);
            super.a(new NullBoundingBox()); // Forces the bounding box
            setInvisible(true);
            setSmall(true);
            setArms(false);
            setGravity(true);
            setBasePlate(true);
            setLockTick(true);
            setMarker(true);
        }
        
        @Override
        public void b(NBTTagCompound nbttagcompound)
        {
            // no persistence
        }
        
        @Override
        public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, ItemStack itemstack, EnumHand enumhand)
        {
            // rename/armor
            return EnumInteractionResult.FAIL;
        }
        
        @Override
        public void a(NBTTagCompound nbttagcompound)
        {
            // no persistence
        }
        
        @Override
        public void a(AxisAlignedBB axisalignedbb)
        {
            // ignore setBoundingBox
        }
        
        @Override
        public void a(SoundEffect sound, float f1, float f2)
        {
            // Remove sounds.
        }
        
        @Override
        public boolean c(NBTTagCompound nbttagcompound)
        {
            // no persistence
            return false;
        }
        
        @Override
        public boolean c(int ix, ItemStack itemstack)
        {
            // equipment
            return false;
        }
        
        @Override
        public boolean d(NBTTagCompound nbttagcompound)
        {
            // no persistence
            return false;
        }
        
        @Override
        public boolean isInvulnerable(DamageSource source)
        {
            return true;
        }
        
        @Override
        public void setEquipment(EnumItemSlot enumitemslot, ItemStack itemstack)
        {
            // ignore
        }
        
        @Override
        public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack)
        {
            // ignore
        }
        
        /**
         * Sets the line text.
         * 
         * @param text
         *            line text.
         */
        public void setLine(String text)
        {
            if (text.length() > 256)
            {
                super.setCustomName(text.substring(0, 256));
            }
            else
            {
                super.setCustomName(text);
            }
            super.setCustomNameVisible(!text.isEmpty());
        }
        
        @Override
        public String getName()
        {
            return super.getCustomName();
        }
        
        @Override
        public void setCustomNameVisible(boolean visible)
        {
            // ignore (do not allow changes)
        }
        
        // ?????
        // @Override
        // public int getId() {
        // StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        // if (elements.length > 2 && elements[2] != null && elements[2].getFileName().equals("EntityTrackerEntry.java") && elements[2].getLineNumber() > 137 && elements[2].getLineNumber() < 147) {
        // return -1;
        // }
        //
        // return super.getId();
        // }
        
        @Override
        public void m()
        {
            if (!this.lockTick)
            {
                super.m();
            }
        }
        
        /**
         * setz lock tick flag.
         * 
         * @param lock
         *            flag
         */
        public void setLockTick(boolean lock)
        {
            this.lockTick = lock;
        }
        
        @Override
        public void die()
        {
            setLockTick(false);
            super.die();
        }
        
        @Override
        public CraftEntity getBukkitEntity()
        {
            if (super.bukkitEntity == null)
            {
                this.bukkitEntity = new CraftHologram(this.world.getServer(), this);
            }
            return this.bukkitEntity;
        }
        
        @Override
        public void setPosition(double x, double y, double z)
        {
            super.setPosition(x, y, z);
            
            // TODO send update to old players?
            
            // Send a packet near to update the position.
            PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);
            List<EntityHuman> players = this.world.players;
            players.stream()
                .filter(obj -> obj instanceof EntityPlayer)
                .forEach(obj ->
                {
                    EntityPlayer nmsPlayer = (EntityPlayer) obj;
                    
                    double distanceSquared = Math.pow(nmsPlayer.locX - this.locX, 2) + Math.pow(nmsPlayer.locZ - this.locZ, 2);
                    if (distanceSquared < 8192 && nmsPlayer.playerConnection != null)
                    {
                        nmsPlayer.playerConnection.sendPacket(teleportPacket);
                    }
                });
        }
        
    }
    
    /**
     * hologram.
     */
    private static final class CraftHologram extends CraftArmorStand
    {
        
        /**
         * Constructor.
         * 
         * @param server
         *            server
         * @param entity
         *            entity
         */
        public CraftHologram(CraftServer server, HologramLine entity)
        {
            super(server, entity);
        }
        
        @Override
        public void remove()
        {
            // empty
        }
        
        @Override
        public void setArms(boolean arms)
        {
            // empty
        }
        
        @Override
        public void setBasePlate(boolean basePlate)
        {
            // empty
        }
        
        @Override
        public void setBodyPose(EulerAngle pose)
        {
            // empty
        }
        
        @Override
        public void setBoots(org.bukkit.inventory.ItemStack item)
        {
            // empty
        }
        
        @Override
        public void setChestplate(org.bukkit.inventory.ItemStack item)
        {
            // empty
        }
        
        @Override
        public void setGravity(boolean gravity)
        {
            // empty
        }
        
        @Override
        public void setHeadPose(EulerAngle pose)
        {
            // empty
        }
        
        @Override
        public void setHelmet(org.bukkit.inventory.ItemStack item)
        {
            // empty
        }
        
        @Override
        public void setItemInHand(org.bukkit.inventory.ItemStack item)
        {
            // empty
        }
        
        @Override
        public void setLeftArmPose(EulerAngle pose)
        {
            // empty
        }
        
        @Override
        public void setLeftLegPose(EulerAngle pose)
        {
            // empty
        }
        
        @Override
        public void setLeggings(org.bukkit.inventory.ItemStack item)
        {
            // empty
        }
        
        @Override
        public void setRightArmPose(EulerAngle pose)
        {
            // empty
        }
        
        @Override
        public void setRightLegPose(EulerAngle pose)
        {
            // empty
        }
        
        @Override
        public void setSmall(boolean small)
        {
            // empty
        }
        
        @Override
        public void setVisible(boolean visible)
        {
            // empty
        }
        
        @Override
        public boolean addPotionEffect(PotionEffect effect)
        {
            return false;
        }
        
        @Override
        public boolean addPotionEffect(PotionEffect effect, boolean param)
        {
            return false;
        }
        
        @Override
        public boolean addPotionEffects(Collection<PotionEffect> effects)
        {
            return false;
        }
        
        @Override
        public void setRemoveWhenFarAway(boolean remove)
        {
            // empty
        }
        
        @Override
        public void setVelocity(Vector vel)
        {
            // empty
        }
        
        @Override
        public void setFireTicks(int ticks)
        {
            // empty
        }
        
        @Override
        public boolean setPassenger(Entity entity)
        {
            return false;
        }
        
        @Override
        public boolean eject()
        {
            return false;
        }
        
        @Override
        public boolean leaveVehicle()
        {
            return false;
        }
        
        @Override
        public void playEffect(EntityEffect effect)
        {
            // empty
        }
        
        @Override
        public void setCustomName(String name)
        {
            // empty
        }
        
        @Override
        public void setCustomNameVisible(boolean flag)
        {
            // empty
        }
        
    }
    
    /**
     * nms impl of hologram entity.
     */
    private static final class HologramEntityImpl extends AbstractWhitelistableEntityHelper implements HologramEntityInterface
    {
        
        /** location. */
        private Location           location;
        
        /** the hologram lines. */
        private List<HologramLine> lines = new ArrayList<>();
        
        /**
         * Constructor.
         * 
         * @param location
         *            initial location.
         */
        public HologramEntityImpl(Location location)
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
            this.delete();
            Location loc = this.location;
            for (final Serializable txt : list)
            {
                if (txt instanceof LocalizedMessageInterface)
                {
                    // localized message
                    final LocalizedMessageInterface lmi = (LocalizedMessageInterface) txt;
                    if (lmi.isSingleLine())
                    {
                        final String text = lmi.toUserMessage(Locale.ENGLISH);
                        final HologramLine line = new HologramLine(((CraftWorld) loc.getWorld()).getHandle());
                        line.setLine(text);
                        this.lines.add(line);
                        line.setPosition(loc.getX(), loc.getY(), loc.getZ());
                        ((CraftWorld) loc.getWorld()).addEntity(line, SpawnReason.CUSTOM);
                        loc = loc.add(0, -LineHeight, 0);
                    }
                    else
                    {
                        for (final String text : lmi.toUserMessageLine(Locale.ENGLISH))
                        {
                            final HologramLine line = new HologramLine(((CraftWorld) loc.getWorld()).getHandle());
                            line.setLine(text);
                            this.lines.add(line);
                            line.setPosition(loc.getX(), loc.getY(), loc.getZ());
                            ((CraftWorld) loc.getWorld()).addEntity(line, SpawnReason.CUSTOM);
                            loc = loc.add(0, -LineHeight, 0);
                        }
                    }
                }
                else
                {
                    for (final String text : txt.toString().split("\n")) //$NON-NLS-1$
                    {
                        final HologramLine line = new HologramLine(((CraftWorld) loc.getWorld()).getHandle());
                        line.setLine(text);
                        this.lines.add(line);
                        line.setPosition(loc.getX(), loc.getY(), loc.getZ());
                        ((CraftWorld) loc.getWorld()).addEntity(line, SpawnReason.CUSTOM);
                        loc = loc.add(0, -LineHeight, 0);
                    }
                }
            }
        }
        
        @Override
        public void delete()
        {
            this.lines.forEach(HologramLine::die);
            this.lines.clear();
            super.delete();
        }

        @Override
        protected Location getLocation()
        {
            return this.location;
        }

        @Override
        protected void sendInRangePackages(PlayerConnection con)
        {
            final List<Packet<?>> packets = new ArrayList<>();
            this.lines.stream().map(l -> new PacketPlayOutEntityDestroy(l.getId())).forEach(packets::add);
            this.lines.stream().map(l -> new PacketPlayOutSpawnEntity(l, 78)).forEach(packets::add);
            sendPackages(con, 1, packets.toArray(new Packet[packets.size()]));
        }

        @Override
        protected void sendOutOfRangePackages(PlayerConnection con)
        {
            sendPackages(con, 1,
                this.lines.stream().map(l -> new PacketPlayOutEntityDestroy(l.getId())).toArray(Packet[]::new));
        }

        @Override
        protected void sendUntrackPackages(PlayerConnection con)
        {
            // empty
        }

        @Override
        protected void sendTrackPackages(PlayerConnection con)
        {
            // empty
        }
        
    }
    
}
