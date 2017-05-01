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

package de.minigameslib.mclib.api.mcevent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;

/**
 * Event to show up that player closes a gui.
 * 
 * @author mepeisen
 *
 */
public class PlayerCloseGuiEvent extends Event implements MinecraftEvent<PlayerCloseGuiEvent, PlayerCloseGuiEvent>
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the gui the player closed. */
    private final ClickGuiInterface  gui;
    
    /** the player. */
    private final McPlayerInterface  player;
    
    /**
     * Constructor.
     * 
     * @param gui
     *            the closed gui
     * @param player
     *            the target player
     */
    public PlayerCloseGuiEvent(ClickGuiInterface gui, McPlayerInterface player)
    {
        this.gui = gui;
        this.player = player;
    }
    
    /**
     * Returns the gui that the player closed
     * 
     * @return the gui the player closed.
     */
    public ClickGuiInterface getGui()
    {
        return this.gui;
    }
    
    @Override
    public McPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    /**
     * Returns the handlers list.
     * 
     * @return handlers
     */
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
    
    /**
     * Returns the handlers list.
     * 
     * @return handlers
     */
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    @Override
    public PlayerCloseGuiEvent getBukkitEvent()
    {
        return this;
    }
    
    @Override
    public McOutgoingStubbing<PlayerCloseGuiEvent> when(McPredicate<PlayerCloseGuiEvent> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
