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

package de.minigameslib.mclib.fakeclient;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Base interface for controlling the fake player.
 * 
 * @author mepeisen
 */
public interface IFakeController
{
    
    /**
     * Connects to the current local server (must be running in background).
     * 
     * @param spawn
     *            the world and spawn point
     */
    void connect(Location spawn);
    
    /**
     * Disconnects the player.
     */
    void disconnect();
    
    /**
     * Disconnects the player.
     * 
     * @param quitMessage
     */
    void disconnect(String quitMessage);
    
    /**
     * Returns the bukkit player (if being online)
     * 
     * @return bukkit player
     */
    Player getPlayer();
    
    /**
     * Returns the offline player.
     * 
     * @return offline player.
     */
    OfflinePlayer getOfflinePlayer();
    
    // interaction
    
    /**
     * Sends a chat message.
     * 
     * @param msg
     */
    void say(String msg);
    
    /**
     * teleport player to given location.
     * 
     * <p>
     * Does not change the world!
     * </p>
     * 
     * @param location
     */
    void teleport(Location location);
    
    /**
     * teleport player to given location.
     * @param x
     * @param y
     * @param z
     * @param yaw
     * @param pitch
     */
    void teleport(double x, double y, double z, float yaw, float pitch);
    
    /**
     * teleport player to given location.
     * @param x
     * @param y
     * @param z
     * @param yaw
     * @param pitch
     * @param xRel {@code true} to calculate X by adding the current X position
     * @param yRel {@code true} to calculate Y by adding the current Y position
     * @param zRel {@code true} to calculate Z by adding the current Z position
     * @param yawRel {@code true} to calculate yaw by adding the current yaw
     * @param pitchRel {@code true} to calculate pitch by adding the current pitch
     */
    void teleport(double x, double y, double z, float yaw, float pitch, boolean xRel, boolean yRel, boolean zRel, boolean yawRel, boolean pitchRel);
    
    /**
     * Steers a vehicle.
     * @param f1
     * @param f2
     * @param b1
     * @param sneaking
     */
    void steerVehicle(float f1, float f2, boolean b1, boolean sneaking); // TODO what do the args mean?
    
    /**
     * Moves the vehicle by reading the entities position.
     * @param entity
     */
    void moveVehicle(Entity entity);
    
    /**
     * Accepts the teleport
     * @param serial TODO where does it come from?
     */
    void acceptTeleport(int serial);
    
    /**
     * Changes the look during flight
     * @param yaw
     * @param pitch
     */
    void flyingChangeLook(float yaw, float pitch);
    
    /**
     * Changes the position during flight
     * @param x
     * @param y
     * @param z
     */
    void flyingChangePos(double x, double y, double z);
    
    /**
     * Changes the position and look during flight
     * @param yaw
     * @param pitch
     * @param x
     * @param y
     * @param z
     */
    void flyingChangeLookPos(float yaw, float pitch, double x, double y, double z);
    
    /**
     * Sigs the block at given position
     * @param blockX
     * @param blockY
     * @param blockZ
     * @param direction
     * @param type
     */
    void digBlock(int blockX, int blockY, int blockZ, Direction direction, DigType type);
    
    /**
     * Uses an item at a block
     * @param blockX
     * @param blockY
     * @param blockZ
     * @param direction
     * @param hand
     * @param f1
     * @param f2
     * @param f3
     */
    void useItem(int blockX, int blockY, int blockZ, Direction direction, Hand hand, float f1, float f2, float f3); // TODO what do the args mean?
    
    /**
     * Places the block from given hand
     * @param hand
     */
    void blockPlace(Hand hand);
    
    /**
     * Spectates the player with given uuid.
     * @param player
     */
    void spectate(UUID player);
    
    /**
     * Answers a resource pack question
     * @param status
     */
    void resourcePackStatus(ResourceStatus status);
    
    /**
     * Performs boat move
     * @param b1
     * @param b2
     */
    void boatMove(boolean b1, boolean b2); // TODO what do the args mean?
    
    /**
     * Sets the current slot
     * @param slotIndex
     */
    void heldItemSlot(int slotIndex);
    
    /**
     * Plays an arm animation
     * @param hand
     */
    void armAnimation(Hand hand);
    
    /**
     * Performs an entity action
     * @param a currently unused
     * @param c jump power for START_JUMP action
     * @param action
     */
    void entityAction(int a, int c, PlayerAction action);
    
    /**
     * Uses an entity at given position
     * @param entityId TODO where does it come from?
     * @param action
     * @param x
     * @param y
     * @param z
     * @param hand
     */
    void useEntity(int entityId, EntityUseAction action, double x, double y, double z, Hand hand);
    
    /**
     * Sends given client command.
     * @param command
     */
    void clientCommand(ClientCommand command);
    
    /**
     * Closes a window with given id
     * @param windowId TODO where does it come from?
     */
    void closeWindow(int windowId);
    
    /**
     * Performs a click inside given window.
     * @param windowId TODO where does it come from?
     * @param slot
     * @param button
     * @param txid use a unique id to identify the click in later calls
     * @param item
     * @param type
     */
    void windowClick(int windowId, int slot, int button, short txid, ItemStack item, ClickType type);
    
    /**
     * Anchants items
     * @param windowId TODO where does it come from?
     * @param b
     */
    void enchantItem(int windowId, int b); // TODO what do the args mean?
    
    /**
     * Sets creative slot with given item
     * @param slot
     * @param item
     */
    void setCreativeSlot(int slot, ItemStack item);
    
    /**
     * TODO???
     * @param windowId TODO where does it come from?
     * @param txid comes from windowClick
     * @param c not used
     */
    void transaction(int windowId, short txid, boolean c); // TODO what do the args mean?
    
    /**
     * Updates the sign at given position
     * @param blockX
     * @param blockY
     * @param blockZ
     * @param lines
     */
    void updateSign(int blockX, int blockY, int blockZ, String[] lines);
    
    /**
     * keep alive/ pong
     * @param serial comes from serial event
     */
    void keepAlive(int serial);
    
    /**
     * changes abilities
     * @param isInvulnerable currently unused
     * @param isFlying
     * @param canFly currently unused
     * @param canInstantlyBuild currently unused
     * @param flySpeed currently unused
     * @param walkSpeed currently unused
     */
    void abilities(boolean isInvulnerable, boolean isFlying, boolean canFly, boolean canInstantlyBuild, float flySpeed, float walkSpeed);
    
    /**
     * Requests tab complete from server
     * @param text
     * @param b currently unused
     */
    void tabComplete(String text, boolean b);
    
    /**
     * Requests tab complete from server
     * @param text
     * @param b currently unused
     * @param blockX
     * @param blockY
     * @param blockZ
     */
    void tabComplete(String text, boolean b, int blockX, int blockY, int blockZ);
    
    /**
     * Changes settings
     * @param locale
     * @param b currently unused
     * @param visibility
     * @param d currently unused
     * @param e TODO what do the args mean?
     * @param hand
     */
    void settings(String locale, int b, ChatVisibility visibility, boolean d, int e, MainHand hand);
    
    /**
     * Sends custom client payload.
     * @param tag
     * @param data
     */
    void customPayload(String tag, byte[] data);
    
    // TODO add custom events
    
    // change book pages
    // void customBookEdit(ItemStack stack);
    
    // change book title/author/pages
    // void customBookSign(ItemStack stack);
    
    // select trade
    // void customSelectTrade(int selectedIndex);
    
    // command block
    // void customCommandBlock(int blockX, int blockY, int blockZ, String command, boolean trackOutput);
    // void customCommandBlock(int entityId, String command, boolean trackOutput);
    // void customAutoCommand(int blockX, int blockY, int blockZ, String command, boolean trackOutput, TileEntityCommand.Type type, boolean conditional, boolean flag3??);
    
    // beacon options
    // void customBeacon(MobEffectList effect1, MobEffectList effect2);
    
    // item name
    // void customItemName(String text);
    
    // struct
    // void customStruct(int blockX, int blockY, int blockZ, byte b1, TileEntityStructure.UsageMode usage, String str, int blockX2, int blockY2, int blockZ2, int blockX3, int blockY3, int blockZ3, EnumBlockMirror mirror, EnumBlockRotation rotation, String str, boolean flg1, boolean flg2, boolean flg3, float f1, long l1);
    
    // pick item
    // void customPickItem(int slotIndex);
    
    // channels
    // void customRegisterChannels(String[] channels);
    // void customUnregisterChannels(String[] channels);

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum ChatVisibility
    {
        /** enum value. */
        FULL,
        /** enum value. */
        SYSTEM,
        /** enum value. */
        HIDDEN
    }

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum MainHand
    {
        /** enum value. */
        LEFT,
        /** enum value. */
        RIGHT
    }

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum Direction
    {
        /** enum value. */
        DOWN,
        /** enum value. */
        UP,
        /** enum value. */
        NORTH,
        /** enum value. */
        SOUTH,
        /** enum value. */
        WEST,
        /** enum value. */
        EAST
    }

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum DigType
    {
        /** enum value. */
        START_DESTROY_BLOCK,
        /** enum value. */
        ABORT_DESTROY_BLOCK,
        /** enum value. */
        STOP_DESTROY_BLOCK,
        /** enum value. */
        DROP_ALL_ITEMS,
        /** enum value. */
        DROP_ITEM,
        /** enum value. */
        RELEASE_USE_ITEM,
        /** enum value. */
        SWAP_HELD_ITEMS
    }

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum Hand
    {
        /** enum value. */
        MAIN_HAND,
        /** enum value. */
        OFF_HAND
    }

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum ResourceStatus
    {
        /** enum value. */
        SUCCESSFULLY_LOADED,
        /** enum value. */
        DECLINED,
        /** enum value. */
        FAILED_DOWNLOAD,
        /** enum value. */
        ACCEPTED
    }

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum PlayerAction
    {
        /** enum value. */
        START_SNEAKING,
        /** enum value. */
        STOP_SNEAKING,
        /** enum value. */
        STOP_SLEEPING,
        /** enum value. */
        START_SPRINTING,
        /** enum value. */
        STOP_SPRINTING,
        /** enum value. */
        START_RIDING_JUMP,
        /** enum value. */
        STOP_RIDING_JUMP,
        /** enum value. */
        OPEN_INVENTORY,
        /** enum value. */
        START_FALL_FLYING
    }

    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum EntityUseAction
    {
        /** enum value. */
        INTERACT,
        /** enum value. */
        ATTACK,
        /** enum value. */
        INTERACT_AT
    }
    
    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum ClientCommand
    {
        /** enum value. */
        PERFORM_RESPAWN,
        /** enum value. */
        REQUEST_STATS,
        /** enum value. */
        OPEN_INVENTORY_ACHIEVEMENT
    }
    
    /**
     * NMS Enumeration from minecraft.
     * 
     * <p>
     * TODO explain.
     * </p>
     * 
     * @author mepeisen
     */
    public enum ClickType
    {
        /** enum value. */
        PICKUP,
        /** enum value. */
        QUICK_MOVE,
        /** enum value. */
        SWAP,
        /** enum value. */
        CLONE,
        /** enum value. */
        THROW,
        /** enum value. */
        QUICK_CRAFT,
        /** enum value. */
        PICKUP_ALL
    }
    
}
