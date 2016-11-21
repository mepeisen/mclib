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
import java.util.Locale;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.perms.PermissionsInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;

/**
 * Interface representing players.
 * 
 * @author mepeisen
 */
public interface McPlayerInterface
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
     * Checks if the player has a smart gui installed.
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
     * @return zone or {@code null} if this player is currently not within any zone.
     */
    ZoneInterface getZone();
    
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
    
}
