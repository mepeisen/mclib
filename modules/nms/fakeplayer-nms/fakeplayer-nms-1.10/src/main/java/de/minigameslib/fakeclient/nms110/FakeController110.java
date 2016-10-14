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

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;

import de.minigameslib.mclib.fakeclient.IFakeClient;
import de.minigameslib.mclib.fakeclient.IFakeController;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.EntityHuman.EnumChatVisibility;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.EnumDirection;
import net.minecraft.server.v1_10_R1.EnumHand;
import net.minecraft.server.v1_10_R1.EnumMainHand;
import net.minecraft.server.v1_10_R1.InventoryClickType;
import net.minecraft.server.v1_10_R1.PacketDataSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayInAbilities;
import net.minecraft.server.v1_10_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_10_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_10_R1.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.server.v1_10_R1.PacketPlayInBlockPlace;
import net.minecraft.server.v1_10_R1.PacketPlayInBoatMove;
import net.minecraft.server.v1_10_R1.PacketPlayInChat;
import net.minecraft.server.v1_10_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_10_R1.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_10_R1.PacketPlayInCloseWindow;
import net.minecraft.server.v1_10_R1.PacketPlayInCustomPayload;
import net.minecraft.server.v1_10_R1.PacketPlayInEnchantItem;
import net.minecraft.server.v1_10_R1.PacketPlayInEntityAction;
import net.minecraft.server.v1_10_R1.PacketPlayInEntityAction.EnumPlayerAction;
import net.minecraft.server.v1_10_R1.PacketPlayInFlying.PacketPlayInLook;
import net.minecraft.server.v1_10_R1.PacketPlayInFlying.PacketPlayInPosition;
import net.minecraft.server.v1_10_R1.PacketPlayInFlying.PacketPlayInPositionLook;
import net.minecraft.server.v1_10_R1.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_10_R1.PacketPlayInKeepAlive;
import net.minecraft.server.v1_10_R1.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_10_R1.PacketPlayInResourcePackStatus.EnumResourcePackStatus;
import net.minecraft.server.v1_10_R1.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_10_R1.PacketPlayInSettings;
import net.minecraft.server.v1_10_R1.PacketPlayInSpectate;
import net.minecraft.server.v1_10_R1.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_10_R1.PacketPlayInTabComplete;
import net.minecraft.server.v1_10_R1.PacketPlayInTeleportAccept;
import net.minecraft.server.v1_10_R1.PacketPlayInTransaction;
import net.minecraft.server.v1_10_R1.PacketPlayInUpdateSign;
import net.minecraft.server.v1_10_R1.PacketPlayInUseEntity;
import net.minecraft.server.v1_10_R1.PacketPlayInUseEntity.EnumEntityUseAction;
import net.minecraft.server.v1_10_R1.PacketPlayInUseItem;
import net.minecraft.server.v1_10_R1.PacketPlayInVehicleMove;
import net.minecraft.server.v1_10_R1.PacketPlayInWindowClick;
import net.minecraft.server.v1_10_R1.PacketPlayOutPosition.EnumPlayerTeleportFlags;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.PlayerList;
import net.minecraft.server.v1_10_R1.Vec3D;
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
    public void connect(Location spawn)
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
        this.checkConnected();
        
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

    @Override
    public void disconnect(String quitMessage)
    {
        this.checkConnected();
        
        this.connection.a(new ChatComponentText(quitMessage));
        
        this.connection = null;
        this.entityPlayer = null;
    }

    @Override
    public void teleport(Location location)
    {
        this.checkConnected();
        
        this.connection.teleport(location);
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch)
    {
        this.checkConnected();
        
        this.connection.a(x, y, z, yaw, pitch);
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch, boolean xRel, boolean yRel, boolean zRel, boolean yawRel, boolean pitchRel)
    {
        this.checkConnected();
        
        final Set<EnumPlayerTeleportFlags> flags = new HashSet<>();
        if (xRel) flags.add(EnumPlayerTeleportFlags.X);
        if (yRel) flags.add(EnumPlayerTeleportFlags.Y);
        if (zRel) flags.add(EnumPlayerTeleportFlags.Z);
        if (yawRel) flags.add(EnumPlayerTeleportFlags.Y_ROT);
        if (pitchRel) flags.add(EnumPlayerTeleportFlags.X_ROT);
        this.connection.a(x, y, z, yaw, pitch, flags);
    }
    
    /**
     * sets the field for given object
     * @param target
     * @param clazz
     * @param fieldName
     * @param value
     */
    private <T> void setField(T target, Class<T> clazz, String fieldName, Object value)
    {
        try
        {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void steerVehicle(float f1, float f2, boolean b1, boolean sneaking)
    {
        this.checkConnected();
        
        final PacketPlayInSteerVehicle packet = new PacketPlayInSteerVehicle();
        this.setField(packet, PacketPlayInSteerVehicle.class, "a", f1); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSteerVehicle.class, "b", f2); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSteerVehicle.class, "c", b1); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSteerVehicle.class, "d", sneaking); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void moveVehicle(Entity entity)
    {
        this.checkConnected();
        
        final PacketPlayInVehicleMove packet = new PacketPlayInVehicleMove(((CraftEntity)entity).getHandle());
        this.connection.a(packet);
    }

    @Override
    public void acceptTeleport(int serial)
    {
        this.checkConnected();
        
        final PacketPlayInTeleportAccept packet = new PacketPlayInTeleportAccept();
        this.setField(packet, PacketPlayInTeleportAccept.class, "a", serial); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void flyingChangeLook(float yaw, float pitch)
    {
        this.checkConnected();
        
        final PacketPlayInLook packet = new PacketPlayInLook();
        this.setField(packet, PacketPlayInLook.class, "yaw", yaw); //$NON-NLS-1$
        this.setField(packet, PacketPlayInLook.class, "pitch", pitch); //$NON-NLS-1$
        // TODO boolean f?
        this.connection.a(packet);
    }

    @Override
    public void flyingChangePos(double x, double y, double z)
    {
        this.checkConnected();
        
        final PacketPlayInPosition packet = new PacketPlayInPosition();
        this.setField(packet, PacketPlayInPosition.class, "x", x); //$NON-NLS-1$
        this.setField(packet, PacketPlayInPosition.class, "y", y); //$NON-NLS-1$
        this.setField(packet, PacketPlayInPosition.class, "z", z); //$NON-NLS-1$
        // TODO boolean f?
        this.connection.a(packet);
    }

    @Override
    public void flyingChangeLookPos(float yaw, float pitch, double x, double y, double z)
    {
        this.checkConnected();
        
        final PacketPlayInPositionLook packet = new PacketPlayInPositionLook();
        this.setField(packet, PacketPlayInPositionLook.class, "yaw", yaw); //$NON-NLS-1$
        this.setField(packet, PacketPlayInPositionLook.class, "pitch", pitch); //$NON-NLS-1$
        this.setField(packet, PacketPlayInPositionLook.class, "x", x); //$NON-NLS-1$
        this.setField(packet, PacketPlayInPositionLook.class, "y", y); //$NON-NLS-1$
        this.setField(packet, PacketPlayInPositionLook.class, "z", z); //$NON-NLS-1$
        // TODO boolean f?
        this.connection.a(packet);   
    }

    @Override
    public void digBlock(int blockX, int blockY, int blockZ, Direction direction, DigType type)
    {
        this.checkConnected();
        
        final PacketPlayInBlockDig packet = new PacketPlayInBlockDig();
        final BlockPosition position = new BlockPosition(blockX, blockY, blockZ);
        this.setField(packet, PacketPlayInBlockDig.class, "a", position); //$NON-NLS-1$
        this.setField(packet, PacketPlayInBlockDig.class, "b", convert(direction)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInBlockDig.class, "c", convert(type)); //$NON-NLS-1$
        this.connection.a(packet);
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumChatVisibility convert(ChatVisibility value)
    {
        return EnumChatVisibility.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumMainHand convert(MainHand value)
    {
        return EnumMainHand.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumDirection convert(Direction value)
    {
        return EnumDirection.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumPlayerDigType convert(DigType value)
    {
        return EnumPlayerDigType.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumHand convert(Hand value)
    {
        return EnumHand.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumResourcePackStatus convert(ResourceStatus value)
    {
        return EnumResourcePackStatus.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumPlayerAction convert(PlayerAction value)
    {
        return EnumPlayerAction.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumEntityUseAction convert(EntityUseAction value)
    {
        return EnumEntityUseAction.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private EnumClientCommand convert(ClientCommand value)
    {
        return EnumClientCommand.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private InventoryClickType convert(ClickType value)
    {
        return InventoryClickType.valueOf(value.name());
    }

    /**
     * converts to nms enum
     * @param value
     * @return converted
     */
    private net.minecraft.server.v1_10_R1.ItemStack convert(ItemStack value)
    {
        final net.minecraft.server.v1_10_R1.ItemStack result = value.getTypeId() == 0 ? null : CraftItemStack.asNMSCopy(value);
        return result;
    }

    @Override
    public void useItem(int blockX, int blockY, int blockZ, Direction direction, Hand hand, float f1, float f2, float f3)
    {
        this.checkConnected();
        
        final PacketPlayInUseItem packet = new PacketPlayInUseItem();
        final BlockPosition position = new BlockPosition(blockX, blockY, blockZ);
        this.setField(packet, PacketPlayInUseItem.class, "a", position); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseItem.class, "b", this.convert(direction)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseItem.class, "c", this.convert(hand)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseItem.class, "d", f1); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseItem.class, "e", f2); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseItem.class, "f", f3); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseItem.class, "timestamp", System.currentTimeMillis()); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void blockPlace(Hand hand)
    {
        this.checkConnected();
        
        final PacketPlayInBlockPlace packet = new PacketPlayInBlockPlace();
        this.setField(packet, PacketPlayInBlockPlace.class, "a", convert(hand)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInBlockPlace.class, "timestamp", System.currentTimeMillis()); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void spectate(UUID player)
    {
        this.checkConnected();
        
        final PacketPlayInSpectate packet = new PacketPlayInSpectate(player);
        this.connection.a(packet);
    }

    @Override
    public void resourcePackStatus(ResourceStatus status)
    {
        this.checkConnected();
        
        final PacketPlayInResourcePackStatus packet = new PacketPlayInResourcePackStatus(convert(status));
        this.connection.a(packet);
    }

    @Override
    public void boatMove(boolean b1, boolean b2)
    {
        this.checkConnected();
        
        final PacketPlayInBoatMove packet = new PacketPlayInBoatMove(b1, b2);
        this.connection.a(packet);
    }

    @Override
    public void heldItemSlot(int slotIndex)
    {
        this.checkConnected();
        
        final PacketPlayInHeldItemSlot packet = new PacketPlayInHeldItemSlot();
        this.setField(packet, PacketPlayInHeldItemSlot.class, "a", slotIndex); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void armAnimation(Hand hand)
    {
        this.checkConnected();
        
        final PacketPlayInArmAnimation packet = new PacketPlayInArmAnimation(convert(hand));
        this.connection.a(packet);
    }

    @Override
    public void entityAction(int a, int c, PlayerAction action)
    {
        this.checkConnected();
        
        final PacketPlayInEntityAction packet = new PacketPlayInEntityAction();
        this.setField(packet, PacketPlayInEntityAction.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInEntityAction.class, "c", c); //$NON-NLS-1$
        this.setField(packet, PacketPlayInEntityAction.class, "animation", convert(action)); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void useEntity(int a, EntityUseAction action, double x, double y, double z, Hand hand)
    {
        this.checkConnected();
        
        final PacketPlayInUseEntity packet = new PacketPlayInUseEntity();
        this.setField(packet, PacketPlayInUseEntity.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseEntity.class, "action", convert(action)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseEntity.class, "c", new Vec3D(x, y, z)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUseEntity.class, "d", convert(hand)); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void clientCommand(ClientCommand command)
    {
        this.checkConnected();
        
        final PacketPlayInClientCommand packet = new PacketPlayInClientCommand(convert(command));
        this.connection.a(packet);
    }

    @Override
    public void closeWindow(int id)
    {
        this.checkConnected();
        
        final PacketPlayInCloseWindow packet = new PacketPlayInCloseWindow(id);
        this.connection.a(packet);
    }

    @Override
    public void windowClick(int a, int slot, int button, short d, ItemStack item, ClickType type)
    {
        this.checkConnected();
        
        final PacketPlayInWindowClick packet = new PacketPlayInWindowClick();
        this.setField(packet, PacketPlayInWindowClick.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInWindowClick.class, "slot", slot); //$NON-NLS-1$
        this.setField(packet, PacketPlayInWindowClick.class, "button", button); //$NON-NLS-1$
        this.setField(packet, PacketPlayInWindowClick.class, "d", d); //$NON-NLS-1$
        this.setField(packet, PacketPlayInWindowClick.class, "item", convert(item)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInWindowClick.class, "shift", convert(type)); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void enchantItem(int a, int b)
    {
        this.checkConnected();
        
        final PacketPlayInEnchantItem packet = new PacketPlayInEnchantItem();
        this.setField(packet, PacketPlayInEnchantItem.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInEnchantItem.class, "b", b); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void setCreativeSlot(int slot, ItemStack item)
    {
        this.checkConnected();
        
        final PacketPlayInSetCreativeSlot packet = new PacketPlayInSetCreativeSlot();
        this.setField(packet, PacketPlayInSetCreativeSlot.class, "slot", slot); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSetCreativeSlot.class, "b", convert(item)); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void transaction(int a, short b, boolean c)
    {
        this.checkConnected();
        
        final PacketPlayInTransaction packet = new PacketPlayInTransaction();
        this.setField(packet, PacketPlayInTransaction.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInTransaction.class, "b", b); //$NON-NLS-1$
        this.setField(packet, PacketPlayInTransaction.class, "c", c); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void updateSign(int blockX, int blockY, int blockZ, String[] lines)
    {
        this.checkConnected();
        
        final PacketPlayInUpdateSign packet = new PacketPlayInUpdateSign();
        this.setField(packet, PacketPlayInUpdateSign.class, "a", new BlockPosition(blockX, blockY, blockZ)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInUpdateSign.class, "b", lines); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void keepAlive(int a)
    {
        this.checkConnected();
        
        final PacketPlayInKeepAlive packet = new PacketPlayInKeepAlive();
        this.setField(packet, PacketPlayInKeepAlive.class, "a", a); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void abilities(boolean a, boolean b, boolean c, boolean d, float e, float f)
    {
        this.checkConnected();
        
        final PacketPlayInAbilities packet = new PacketPlayInAbilities();
        this.setField(packet, PacketPlayInAbilities.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInAbilities.class, "b", b); //$NON-NLS-1$
        this.setField(packet, PacketPlayInAbilities.class, "c", c); //$NON-NLS-1$
        this.setField(packet, PacketPlayInAbilities.class, "d", d); //$NON-NLS-1$
        this.setField(packet, PacketPlayInAbilities.class, "e", e); //$NON-NLS-1$
        this.setField(packet, PacketPlayInAbilities.class, "f", f); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void tabComplete(String text, boolean b)
    {
        this.checkConnected();
        
        final PacketPlayInTabComplete packet = new PacketPlayInTabComplete();
        this.setField(packet, PacketPlayInTabComplete.class, "a", text); //$NON-NLS-1$
        this.setField(packet, PacketPlayInTabComplete.class, "b", b); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void tabComplete(String text, boolean b, int blockX, int blockY, int blockZ)
    {
        this.checkConnected();
        
        final PacketPlayInTabComplete packet = new PacketPlayInTabComplete();
        this.setField(packet, PacketPlayInTabComplete.class, "a", text); //$NON-NLS-1$
        this.setField(packet, PacketPlayInTabComplete.class, "b", b); //$NON-NLS-1$
        this.setField(packet, PacketPlayInTabComplete.class, "c", new BlockPosition(blockX, blockY, blockZ)); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void settings(String a, int b, ChatVisibility visibility, boolean c, int e, MainHand hand)
    {
        this.checkConnected();
        
        final PacketPlayInSettings packet = new PacketPlayInSettings();
        this.setField(packet, PacketPlayInSettings.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSettings.class, "b", b); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSettings.class, "c", convert(visibility)); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSettings.class, "d", c); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSettings.class, "e", e); //$NON-NLS-1$
        this.setField(packet, PacketPlayInSettings.class, "f", convert(hand)); //$NON-NLS-1$
        this.connection.a(packet);
    }

    @Override
    public void customPayload(String a, byte[] b)
    {
        this.checkConnected();
        
        final PacketPlayInCustomPayload packet = new PacketPlayInCustomPayload();
        this.setField(packet, PacketPlayInCustomPayload.class, "a", a); //$NON-NLS-1$
        this.setField(packet, PacketPlayInCustomPayload.class, "b", new PacketDataSerializer(Unpooled.copiedBuffer(b))); //$NON-NLS-1$
        this.connection.a(packet);
    }
    
}
