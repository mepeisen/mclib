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

package de.minigameslib.mclib.api.event;

import org.bukkit.event.Event;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;

/**
 * Mc event helper.
 * 
 * @author mepeisen
 * 
 * @param <Evt> Event class
 * @param <MgEvt> Minigame event class
 */
public interface MinecraftEvent<Evt extends Event, MgEvt extends MinecraftEvent<Evt, MgEvt>>
{
    
    /**
     * Returns the original event
     * @return original event this rule 
     */
    Evt getBukkitEvent();
    
    /**
     * Returns the zone causing this event.
     * @return zone causing this event or {@code null} if this event was outside any zone.
     */
    ZoneInterface getZone();
    
    /**
     * Returns the player causing this event.
     * @return player causing this event or {@code null} if this event was not caused by any player.
     */
    McPlayerInterface getPlayer();
    
    // stubbing
    
    /**
     * Checks this event for given criteria and invokes either then or else statements.
     * 
     * <p>
     * NOTICE: If the test function throws an exception it will be re thrown and no then or else statement will be invoked.
     * </p>
     * 
     * @param test
     *            test functions for testing the event matching any criteria.
     * 
     * @return the outgoing stub to apply then or else consumers.
     * 
     * @throws McException
     *             will be thrown if either the test function or then/else consumers throw the exception.
     */
    McOutgoingStubbing<MgEvt> when(McPredicate<MgEvt> test) throws McException;
    
}
