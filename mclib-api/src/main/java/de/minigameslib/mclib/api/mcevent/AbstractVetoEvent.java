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

import java.io.Serializable;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * An event that can be cancelled.
 * 
 * @author mepeisen
 */
public abstract class AbstractVetoEvent extends Event implements Cancellable
{
    
    /** the veto flag. */
    private boolean                   cancelled;
    
    /** the veto reason. */
    private LocalizedMessageInterface vetoReason;
    
    /** message arguments. */
    private Serializable[]            vetoReasonArgs;
    
    /**
     * Returns the veto reason.
     * 
     * @return the vetoReason; maybe null
     */
    public LocalizedMessageInterface getVetoReason()
    {
        return this.vetoReason;
    }
    
    @Override
    public boolean isCancelled()
    {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancelled = true;
        this.vetoReason = null;
        this.vetoReasonArgs = null;
    }
    
    /**
     * Sets the event cancelled.
     * 
     * @param reason
     *            the reason text.
     * @param args
     *            message arguments
     */
    public void setCancelled(LocalizedMessageInterface reason, Serializable... args)
    {
        this.cancelled = true;
        this.vetoReason = reason;
        this.vetoReasonArgs = args;
    }
    
    /**
     * Returns the message arguments to format the veto reason message.
     * 
     * @return the vetoReasonArgs
     */
    public Serializable[] getVetoReasonArgs()
    {
        return this.vetoReasonArgs;
    }
    
}
