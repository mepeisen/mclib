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

package de.minigameslib.mclib.impl.comp;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.SignDeleteEvent;
import de.minigameslib.mclib.api.mcevent.SignDeletedEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocateEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocatedEvent;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignHandlerInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.EventBus;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * @author mepeisen
 *
 */
public class SignImpl extends AbstractLocationComponent implements SignInterface, MgEventListener
{
    
    // TODO fetch sign break events
    
    /** the bukkit sign. */
    private Sign sign;
    
    /** the sign id. */
    private final SignId id;
    
    /** the sign handler. */
    private final SignHandlerInterface handler;
    
    /** an event bus to handle events. */
    private final EventBus                      eventBus         = new EventBus();
    
    /**
     * @param plugin 
     * @param registry
     * @param sign 
     * @param id 
     * @param handler 
     * @param config 
     * @param owner 
     * @throws McException 
     */
    public SignImpl(Plugin plugin, ComponentRegistry registry, Sign sign, SignId id, SignHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(registry, sign == null ? null : sign.getLocation(), config, owner);
        this.sign = sign;
        this.id = id;
        this.handler = handler;
        if (this.handler instanceof McListener)
        {
            this.eventBus.registerHandlers(plugin, (McListener) this.handler);
        }
    }

    @Override
    public SignIdInterface getSignId()
    {
        return this.id;
    }

    @Override
    public void delete() throws McException
    {
        if (this.deleted)
        {
            throw new McException(CommonMessages.AlreadyDeletedError);
        }
        McLibInterface.instance().runInCopiedContext(() -> {
            McLibInterface.instance().setContext(SignInterface.class, this);
            this.handler.canDelete();
        });
        
        final SignDeleteEvent deleteEvent = new SignDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }

        McLibInterface.instance().runInCopiedContext(() -> {
            McLibInterface.instance().setContext(SignInterface.class, this);
            super.delete();
            this.handler.onDelete();
        });
        this.eventBus.clear();

        final SignDeletedEvent deletedEvent = new SignDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
    }
    
    @Override
    public void readData(DataSection coreSection)
    {
        // TODO should we re-read the id?
        // the id is already read from registry.yml
        this.handler.read(coreSection.getSection("handler")); //$NON-NLS-1$
    }

    @Override
    protected void saveData(DataSection coreSection)
    {
        this.id.write(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.write(coreSection.createSection("handler")); //$NON-NLS-1$
    }

    @Override
    public SignHandlerInterface getHandler()
    {
        return this.handler;
    }

    /**
     * Sets the sign
     * @param block
     */
    public void setSign(Sign block)
    {
        this.sign = block;
    }

    @Override
    public Sign getBukkitSign()
    {
        return this.sign;
    }

    @Override
    public void setLocation(Location loc) throws McException
    {
        final Block block = loc.getBlock();
        if (block instanceof Sign)
        {
            final Location old = this.location;
            McLibInterface.instance().runInCopiedContext(() -> {
                McLibInterface.instance().setContext(SignInterface.class, this);
                this.handler.canChangeLocation(loc);
            });
            final SignRelocateEvent relocateEvent = new SignRelocateEvent(this, old, loc);
            Bukkit.getPluginManager().callEvent(relocateEvent);
            if (relocateEvent.isCancelled())
            {
                throw new McException(relocateEvent.getVetoReason(), relocateEvent.getVetoReasonArgs());
            }
            
            this.sign = (Sign) block;

            McLibInterface.instance().runInCopiedContext(() -> {
                McLibInterface.instance().setContext(SignInterface.class, this);
                this.handler.onLocationChange(loc);
            });
            super.setLocation(loc);
            
            final SignRelocatedEvent relocatedEvent = new SignRelocatedEvent(this, old, loc);
            Bukkit.getPluginManager().callEvent(relocatedEvent);
        }
        else
        {
            throw new McException(CommonMessages.SignNotFoundError);
        }
    }

    /**
     * Clears all event registrations
     */
    public void clearEventRegistrations()
    {
        this.eventBus.clear();
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> h)
    {
        this.eventBus.registerHandler(plugin, clazz, h);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.registerHandlers(plugin, listener);
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> h)
    {
        this.eventBus.unregisterHandler(plugin, clazz, h);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public <T extends Event, Evt extends MinecraftEvent<T, Evt>> void handle(Class<T> eventClass, Evt event)
    {
        this.eventBus.handle(eventClass, event);
    }
    
    /**
     * Plugin disable
     * @param plugin
     */
    public void onDisable(Plugin plugin)
    {
        this.eventBus.onDisable(plugin);
    }

    @Override
    public SignTypeId getTypeId()
    {
        return ObjectServiceInterface.instance().getType(this.getSignId());
    }
    
}
