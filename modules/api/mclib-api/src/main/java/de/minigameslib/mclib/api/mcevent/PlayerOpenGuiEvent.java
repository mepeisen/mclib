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
 * Fired before the player opens a gui.
 * 
 * <p>
 * Event can be cancelled.
 * </p>
 * 
 * @author mepeisen
 */
public class PlayerOpenGuiEvent extends AbstractVetoEvent implements MinecraftEvent<PlayerOpenGuiEvent, PlayerOpenGuiEvent>
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the gui the player opened. */
    private final ClickGuiInterface  gui;
    
    /** the player. */
    private final McPlayerInterface  player;
    
    /**
     * Constructor.
     * 
     * @param gui
     *            the opened gui
     * @param player
     *            the target player
     */
    public PlayerOpenGuiEvent(ClickGuiInterface gui, McPlayerInterface player)
    {
        this.gui = gui;
        this.player = player;
    }
    
    /**
     * Returns the gui that the player opened
     * 
     * @return the gui the player opened.
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
    public PlayerOpenGuiEvent getBukkitEvent()
    {
        return this;
    }
    
    @Override
    public McOutgoingStubbing<PlayerOpenGuiEvent> when(McPredicate<PlayerOpenGuiEvent> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
