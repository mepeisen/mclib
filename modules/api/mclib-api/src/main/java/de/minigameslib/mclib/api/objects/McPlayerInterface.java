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

package de.minigameslib.mclib.api.objects;

import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.api.McContext.ContextRunnable;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.cli.ClientInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.perms.PermissionsInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.PlayerDataFragment;

/**
 * Interface representing players.
 * 
 * @author mepeisen
 */
public interface McPlayerInterface extends PlayerDataFragment
{
    
    // common methods (player info)
    
    /**
     * Returns the bukkit player (if this player is online).
     * 
     * @return bukkit player.
     */
    Player getBukkitPlayer();
    
    /**
     * Returns the name of the player.
     * 
     * @return name of the player.
     */
    String getName();
    
    /**
     * Returns the bukkit offline player.
     * 
     * @return bukkit offline player.
     */
    OfflinePlayer getOfflinePlayer();
    
    /**
     * Returns the players uuid.
     * 
     * @return uuid.
     */
    @Override
    UUID getPlayerUUID();
    
    // localization
    
    /**
     * Sends a message to given player.
     * 
     * @param msg
     *            message to send.
     * @param args
     *            arguments to use for this message.
     */
    void sendMessage(LocalizedMessageInterface msg, Serializable... args);
    
    /**
     * Returns the preferred locale
     * 
     * @return preferred user locale or {@code null} if the player uses server default locale.
     */
    Locale getPreferredLocale();
    
    /**
     * Sets the preferred locale for this user.
     * 
     * @param locale
     *            preferred locale
     * @throws McException
     *             thrown if there are problems saving the data.
     */
    void setPreferredLocale(Locale locale) throws McException;
    
    /**
     * Encodes a message to given player by using the players preferred locale.
     * 
     * @param msg
     *            message to encode.
     * @param args
     *            arguments to use for this message.
     * @return the encoded message as string array
     */
    String[] encodeMessage(LocalizedMessageInterface msg, Serializable... args);
    
    // permissions check
    
    /**
     * Checks if the user has a permission.
     * 
     * @param perm
     *            permission to check.
     * @return {@code true} if the user has a permission.
     */
    boolean checkPermission(PermissionsInterface perm);
    
    // storage
    
    /**
     * Returns a storage only available within the current execution context.
     * 
     * <p>
     * This storage can be useful to temporary add data, for example across multiple events.
     * </p>
     * 
     * @return context storage.
     */
    McStorage getContextStorage();
    
    /**
     * Returns a session storage only hold in memory.
     * 
     * <p>
     * This storage can be useful to temporary add data till the server stops or the user logs out.
     * </p>
     * 
     * @return session storage.
     */
    McStorage getSessionStorage();
    
    /**
     * Returns a persistent storage written to disc.
     * 
     * <p>
     * This storage can be useful to persistent data on the disc.
     * </p>
     * 
     * @return context storage.
     */
    McStorage getPersistentStorage();
    
    // gui
    
    /**
     * Returns information on clients.
     * 
     * @return client information.
     */
    ClientInterface getClient();
    
    /**
     * Checks if the player has a smart gui installed.
     * 
     * @return {@code true} if the player has a smart gui; the mclib client mod.
     */
    boolean hasSmartGui();
    
    /**
     * Returns the current gui session (if any)
     * 
     * @return gui session or {@code null} if the user has no opened gui.
     */
    GuiSessionInterface getGuiSession();
    
    /**
     * Lets the player opening a new gui session.
     * 
     * @param gui
     *            gui to display
     * @return gui session
     * @throws McException
     *             thrown if the player is not online.
     */
    GuiSessionInterface openClickGui(ClickGuiInterface gui) throws McException;
    
    /**
     * Lets the player opening a new anvil gui session.
     * 
     * @param gui
     *            anvil gui to display
     * @return gui session
     * @throws McException
     *             thrown if the player is not online.
     */
    GuiSessionInterface openAnvilGui(AnvilGuiInterface gui) throws McException;
    
    /**
     * Sends a raw message to player; can be used to create chat hovers or clickable chat texts.
     * 
     * @param raw
     *            a raw message
     * 
     * @throws McException
     *             thrown if the player is not online.
     */
    void sendRaw(RawMessageInterface raw) throws McException;
    
    /**
     * Opens a smart gui session.
     * 
     * @return gui session; use the sgui commands to control the client smart gui.
     * 
     * @throws McException
     *             thrown if the player is not online or has no smart gui.
     */
    GuiSessionInterface openSmartGui() throws McException;
    
    // zone
    
    /**
     * Returns the zone this player is currently in.
     * 
     * <p>
     * This will always be the last zone the player entered if the player is within multiple zones. If the players leaves this zones the framework will check if he is still inside another zone and
     * will return that zone. If the player is still inside multiple zones when leaving the returned zone will be random.
     * </p>
     * 
     * @return zone or {@code null} if this player is currently not within any zone.
     */
    ZoneInterface getZone();
    
    /**
     * Checks if the player is within given zone.
     * 
     * @param zone
     * @return {@code true} if player is inside given zone.
     */
    boolean isInsideZone(ZoneInterface zone);
    
    /**
     * Checks if the player is inside at least one of the given zones.
     * 
     * @param zone
     * @return {@code true} if player is at least inside one of the given zones.
     */
    boolean isInsideRandomZone(ZoneInterface... zone);
    
    /**
     * Checks if the player is inside every of the given zones.
     * 
     * @param zone
     * @return {@code true} if player is inside of the given zones.
     */
    boolean isInsideAllZones(ZoneInterface... zone);
    
    /**
     * Returns a zone the player is within and that is of given type.
     * 
     * <p>
     * This method returns the "primary zone". If the zones are overlapping this may be a random
     * </p>
     * 
     * @param type
     * @return the zone or {@code null} if no matching zone was found.
     */
    ZoneInterface getZone(ZoneTypeId... type);
    
    /**
     * Checks if the player is inside at least one of the given zones of given type.
     * 
     * @param type
     * @return {@code true} if player is at least inside one of the given zones.
     */
    boolean isInsideRandomZone(ZoneTypeId... type);
    
    /**
     * Returns the zone the player is within.
     * 
     * @return zone list
     */
    Collection<ZoneInterface> getZones();
    
    /**
     * Returns the zone of given types the player is within
     * 
     * @param type
     * @return zone list
     */
    Collection<ZoneInterface> getZones(ZoneTypeId... type);
    
    // stubbing
    
    /**
     * Checks this player for given criteria and invokes either then or else statements.
     * 
     * <p>
     * NOTICE: If the test function throws an exception it will be re thrown and no then or else statement will be invoked.
     * </p>
     * 
     * @param test
     *            test functions for testing the player matching any criteria.
     * 
     * @return the outgoing stub to apply then or else consumers.
     * 
     * @throws McException
     *             will be thrown if either the test function or then/else consumers throw the exception.
     */
    McOutgoingStubbing<McPlayerInterface> when(McPredicate<McPlayerInterface> test) throws McException;
    
    /**
     * Returns a test function to check if the user is online on the current server.
     * 
     * @return predicate to return {@code true} if the arena player is online.
     */
    default boolean isOnline()
    {
        return this.getBukkitPlayer() != null;
    }
    
    /**
     * Returns a test function to check if the user is inside any arena on the current server.
     * 
     * @return predicate to return {@code true} if the player is inside any arena on the current server.
     */
    default boolean isInZone()
    {
        return this.getZone() != null;
    }
    
    // communication
    
    /**
     * Sends given data to client side of this player.
     *
     * @param endpoint
     * @param data
     * 
     * @throws IllegalStateException
     */
    void sendToClient(CommunicationEndpointId endpoint, DataSection... data);
    
    // event system
    
    /**
     * Register player related event handlers only active if this player is involved in events.
     * 
     * @param plugin
     * @param clazz
     * @param handler
     * @throws IllegalStateException
     *             thrown if player is offline
     */
    <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler);
    
    /**
     * Registers an event handler object for events on this player. Methods tagged with McEventHandler are considered as event handlers.
     * 
     * @param plugin
     * @param listener
     * @throws IllegalStateException
     *             thrown if player is offline
     */
    void registerHandlers(Plugin plugin, McListener listener);
    
    /**
     * Remove a registered event handler.
     * 
     * @param plugin
     * @param clazz
     * @param handler
     */
    <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler);
    
    /**
     * Remove a registered event handler.
     * 
     * @param plugin
     * @param listener
     */
    void unregisterHandlers(Plugin plugin, McListener listener);
    
    /**
     * Schedules this in the Bukkit scheduler to run on next tick.
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * <p>
     *
     * @param plugin
     *            the reference to the plugin scheduling task
     * @param task
     *            the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException
     *             if plugin is null
     */
    default BukkitTask runTask(Plugin plugin, ContextRunnable task) throws IllegalArgumentException
    {
        try
        {
            return McLibInterface.instance().calculateInNewContext(null, null, this, null, null, () -> {
                return McLibInterface.instance().runTask(plugin, task);
            });
        }
        catch (McException ex)
        {
            // should never happen
            // TODO Logging
            return null;
        }
    }
    
    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this in the Bukkit scheduler to run asynchronously.
     * </p>
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * <p>
     *
     * @param plugin
     *            the reference to the plugin scheduling task
     * @param task
     *            the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException
     *             if plugin is null
     * @throws IllegalStateException
     *             if this was already scheduled
     */
    default BukkitTask runTaskAsynchronously(Plugin plugin, ContextRunnable task) throws IllegalArgumentException
    {
        try
        {
            return McLibInterface.instance().calculateInNewContext(null, null, this, null, null, () -> {
                return McLibInterface.instance().runTaskAsynchronously(plugin, task);
            });
        }
        catch (McException ex)
        {
            // should never happen
            // TODO Logging
            return null;
        }
    }
    
    /**
     * Schedules this to run after the specified number of server ticks.
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * <p>
     *
     * @param plugin
     *            the reference to the plugin scheduling task
     * @param delay
     *            the ticks to wait before running the task
     * @param task
     *            the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException
     *             if plugin is null
     */
    default BukkitTask runTaskLater(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException
    {
        try
        {
            return McLibInterface.instance().calculateInNewContext(null, null, this, null, null, () -> {
                return McLibInterface.instance().runTaskLater(plugin, delay, task);
            });
        }
        catch (McException ex)
        {
            // should never happen
            // TODO Logging
            return null;
        }
    }
    
    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to run asynchronously after the specified number of server ticks.
     * </p>
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * <p>
     *
     * @param plugin
     *            the reference to the plugin scheduling task
     * @param delay
     *            the ticks to wait before running the task
     * @param task
     *            the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException
     *             if plugin is null
     */
    default BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay, ContextRunnable task) throws IllegalArgumentException
    {
        try
        {
            return McLibInterface.instance().calculateInNewContext(null, null, this, null, null, () -> {
                return McLibInterface.instance().runTaskLaterAsynchronously(plugin, delay, task);
            });
        }
        catch (McException ex)
        {
            // should never happen
            // TODO Logging
            return null;
        }
    }
    
    /**
     * Schedules this to repeatedly run until cancelled, starting after the specified number of server ticks.
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * <p>
     *
     * @param plugin
     *            the reference to the plugin scheduling task
     * @param delay
     *            the ticks to wait before running the task
     * @param period
     *            the ticks to wait between runs
     * @param task
     *            the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException
     *             if plugin is null
     * @throws IllegalStateException
     *             if this was already scheduled
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    default BukkitTask runTaskTimer(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException
    {
        try
        {
            return McLibInterface.instance().calculateInNewContext(null, null, this, null, null, () -> {
                return McLibInterface.instance().runTaskTimer(plugin, delay, period, task);
            });
        }
        catch (McException ex)
        {
            // should never happen
            // TODO Logging
            return null;
        }
    }
    
    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to repeatedly run asynchronously until cancelled, starting after the specified number of server ticks.
     * </p>
     * 
     * <p>
     * This method will run the new method with a copy of current context.
     * <p>
     *
     * @param plugin
     *            the reference to the plugin scheduling task
     * @param delay
     *            the ticks to wait before running the task for the first time
     * @param period
     *            the ticks to wait between runs
     * @param task
     *            the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException
     *             if plugin is null
     * @throws IllegalStateException
     *             if this was already scheduled
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long, long)
     */
    default BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period, ContextRunnable task) throws IllegalArgumentException
    {
        try
        {
            return McLibInterface.instance().calculateInNewContext(null, null, this, null, null, () -> {
                return McLibInterface.instance().runTaskTimerAsynchronously(plugin, delay, period, task);
            });
        }
        catch (McException ex)
        {
            // should never happen
            // TODO Logging
            return null;
        }
    }
    
}
