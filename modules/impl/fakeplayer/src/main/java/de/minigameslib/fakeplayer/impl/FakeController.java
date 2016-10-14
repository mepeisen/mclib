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

package de.minigameslib.fakeplayer.impl;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.fakeclient.IFakeController;

/**
 * Fake controller implementation.
 * 
 * @author mepeisen
 */
class FakeController implements IFakeController
{
    
    /** executors pool. */
    private ExecutorService pool;
    /** original underlying nms class. */
    private IFakeController orig;
    /** owner plugin. */
    private Plugin plugin;

    /**
     * Constructor.
     * @param plugin
     * @param pool
     * @param orig
     */
    public FakeController(Plugin plugin, ExecutorService pool, IFakeController orig)
    {
        this.pool = pool;
        this.orig = orig;
        this.plugin = plugin;
    }

    @Override
    public void connect(Location spawn)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.connect(spawn), 0);
    }
    
    @Override
    public void disconnect()
    {
        this.pool.execute(() -> this.orig.disconnect());
    }
    
    @Override
    public Player getPlayer()
    {
        return this.orig.getPlayer();
    }
    
    @Override
    public OfflinePlayer getOfflinePlayer()
    {
        return this.orig.getOfflinePlayer();
    }
    
    @Override
    public void say(String msg)
    {
        this.pool.execute(() -> this.orig.say(msg));
    }

    @Override
    public void disconnect(String quitMessage)
    {
        this.pool.execute(() -> this.orig.disconnect(quitMessage));
    }

    @Override
    public void teleport(Location location)
    {
        this.pool.execute(() -> this.orig.teleport(location));
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch)
    {
        this.pool.execute(() -> this.orig.teleport(x, y, z, yaw, pitch));
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch, boolean xRel, boolean yRel, boolean zRel, boolean yawRel, boolean pitchRel)
    {
        this.pool.execute(() -> this.orig.teleport(x, y, z, yaw, pitch, xRel, yRel, zRel, yawRel, pitchRel));
    }

    @Override
    public void steerVehicle(float f1, float f2, boolean b1, boolean sneaking)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.steerVehicle(f1, f2, b1, sneaking), 0);
    }

    @Override
    public void moveVehicle(Entity entity)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.moveVehicle(entity), 0);
    }

    @Override
    public void acceptTeleport(int serial)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.acceptTeleport(serial), 0);
    }

    @Override
    public void flyingChangeLook(float yaw, float pitch)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.flyingChangeLook(yaw, pitch), 0);
    }

    @Override
    public void flyingChangePos(double x, double y, double z)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.flyingChangePos(x, y, z), 0);
    }

    @Override
    public void flyingChangeLookPos(float yaw, float pitch, double x, double y, double z)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.flyingChangeLookPos(yaw, pitch, x, y, z), 0);
    }

    @Override
    public void digBlock(int blockX, int blockY, int blockZ, Direction direction, DigType type)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.digBlock(blockX, blockY, blockZ, direction, type), 0);
    }

    @Override
    public void useItem(int blockX, int blockY, int blockZ, Direction direction, Hand hand, float f1, float f2, float f3)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.useItem(blockX, blockY, blockZ, direction, hand, f1, f2, f3), 0);
    }

    @Override
    public void blockPlace(Hand hand)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.blockPlace(hand), 0);
    }

    @Override
    public void spectate(UUID player)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.spectate(player), 0);
    }

    @Override
    public void resourcePackStatus(ResourceStatus status)
    {
        this.pool.execute(() -> this.orig.resourcePackStatus(status));
    }

    @Override
    public void boatMove(boolean b1, boolean b2)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.boatMove(b1, b2), 0);
    }

    @Override
    public void heldItemSlot(int slotIndex)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.heldItemSlot(slotIndex), 0);
    }

    @Override
    public void armAnimation(Hand hand)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.armAnimation(hand), 0);
    }

    @Override
    public void entityAction(int a, int c, PlayerAction action)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.entityAction(a, c, action), 0);
    }

    @Override
    public void useEntity(int a, EntityUseAction action, double x, double y, double z, Hand hand)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.useEntity(a, action, x, y, z, hand), 0);
    }

    @Override
    public void clientCommand(ClientCommand command)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.clientCommand(command), 0);
    }

    @Override
    public void closeWindow(int id)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.closeWindow(id), 0);
    }

    @Override
    public void windowClick(int a, int slot, int button, short d, ItemStack item, ClickType type)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.windowClick(a, slot, button, d, item, type), 0);
    }

    @Override
    public void enchantItem(int a, int b)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.enchantItem(a, b), 0);
    }

    @Override
    public void setCreativeSlot(int slot, ItemStack item)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.setCreativeSlot(slot, item), 0);
    }

    @Override
    public void transaction(int a, short b, boolean c)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.transaction(a, b, c), 0);
    }

    @Override
    public void updateSign(int blockX, int blockY, int blockZ, String[] lines)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.updateSign(blockX, blockY, blockZ, lines), 0);
    }

    @Override
    public void keepAlive(int a)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.keepAlive(a), 0);
    }

    @Override
    public void abilities(boolean a, boolean b, boolean c, boolean d, float e, float f)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.abilities(a, b, c, d, e, f), 0);
    }

    @Override
    public void tabComplete(String text, boolean b)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.tabComplete(text, b), 0);
    }

    @Override
    public void tabComplete(String text, boolean b, int blockX, int blockY, int blockZ)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.tabComplete(text, b, blockX, blockY, blockZ), 0);
    }

    @Override
    public void settings(String a, int b, ChatVisibility visibility, boolean c, int e, MainHand hand)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.settings(a, b, visibility, c, e, hand), 0);
    }

    @Override
    public void customPayload(String a, byte[] b)
    {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.orig.customPayload(a, b), 0);
    }
    
}
