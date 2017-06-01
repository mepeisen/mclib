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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.mcevent.SignDeleteEvent;
import de.minigameslib.mclib.api.mcevent.SignDeletedEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocateEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocatedEvent;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignHandlerInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.nms.api.SignHelperInterface;
import de.minigameslib.mclib.nms.api.SignHelperInterface.SignNmsInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Implementation of signs.
 * 
 * @author mepeisen
 *
 */
public class SignImpl extends AbstractLocationComponent implements SignInterface, MgEventListener
{
    
    /** the bukkit sign. */
    private Sign                       sign;
    
    /** the sign id. */
    private final SignId               id;
    
    /** the sign handler. */
    private final SignHandlerInterface handler;
    
    /** nms sign impl. */
    private SignNmsInterface           nmsSign;
    
    /** an event bus to handle events. */
    private final EventBus             eventBus = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    
    /** serializable lines. */
    private final List<Serializable>   lines = new ArrayList<>();
    
    /**
     * Constructor.
     * 
     * @param plugin
     *            the owning plugin.
     * @param registry
     *            the registry.
     * @param sign
     *            the bukkit sign
     * @param id
     *            the id
     * @param handler
     *            the handler managing the component
     * @param config
     *            the config file
     * @param owner
     *            thw owning service
     * @throws McException
     *             thrown on config errors
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
        if (this.sign != null)
        {
            this.nmsSign = Bukkit.getServicesManager().load(NmsFactory.class).create(SignHelperInterface.class).create(this.getLocation());
            this.lines.addAll(Arrays.asList(this.sign.getLines()));
            this.nmsSign.updateLines(this.lines);
        }
    }
    
    @Override
    public SignId getSignId()
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
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(SignInterface.class, this);
            this.handler.canDelete();
        });
        
        final SignDeleteEvent deleteEvent = new SignDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        delete0();
    }
    
    /**
     * Really destroy the sign; f.e. invoked upon SignDestroyEvent.
     * 
     * @throws McException
     *             thrown on deletion problems; io errors etc.
     */
    public void delete0() throws McException
    {
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(SignInterface.class, this);
            super.delete();
            this.handler.onDelete();
        });
        this.eventBus.clear();
        
        if (this.nmsSign != null)
        {
            Bukkit.getServicesManager().load(NmsFactory.class).create(SignHelperInterface.class).delete(this.nmsSign);
            this.nmsSign = null;
        }
        
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
     * Sets the sign.
     * 
     * @param block
     *            bukkit sign
     */
    public void setSign(Sign block)
    {
        if (this.nmsSign != null)
        {
            Bukkit.getServicesManager().load(NmsFactory.class).create(SignHelperInterface.class).delete(this.nmsSign);
            this.nmsSign = null;
        }
        
        this.sign = block;

        if (this.sign != null)
        {
            this.nmsSign = Bukkit.getServicesManager().load(NmsFactory.class).create(SignHelperInterface.class).create(this.getLocation());
            if (this.lines.size() == 0)
            {
                // initial setup
                this.lines.addAll(Arrays.asList(this.sign.getLines()));
            }
            this.nmsSign.updateLines(this.lines);
        }
    }
    
    @Override
    public Sign getBukkitSign()
    {
        // return the current state
        return (Sign) this.sign.getBlock().getState();
    }
    
    @Override
    public void setLocation(Location loc) throws McException
    {
        final BlockState block = loc.getBlock().getState();
        if (block instanceof Sign)
        {
            final Location old = this.location;
            McLibInterface.instance().runInCopiedContext(() ->
            {
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
            
            McLibInterface.instance().runInCopiedContext(() ->
            {
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
     * Clears all event registrations.
     */
    public void clearEventRegistrations()
    {
        this.eventBus.clear();
    }
    
    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void registerHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> h)
    {
        this.eventBus.registerHandler(plugin, clazz, h);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.registerHandlers(plugin, listener);
    }
    
    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void unregisterHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> h)
    {
        this.eventBus.unregisterHandler(plugin, clazz, h);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public <T extends Event, EVT extends MinecraftEvent<T, EVT>> void handle(Class<EVT> eventClass, EVT event)
    {
        this.eventBus.handle(eventClass, event);
    }
    
    /**
     * Plugin disable.
     * 
     * @param plugin
     *            plugin that was disabled.
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
    
    @Override
    public void setLine(int index, String content)
    {
        final Sign s = this.getBukkitSign();
        s.setLine(index, content);
        s.update();
        
        this.lines.set(index, content);

        if (this.nmsSign != null)
        {
            this.nmsSign.updateLines(this.lines);
        }
    }
    
    @Override
    public void setLine(int index, LocalizedMessageInterface content, Serializable... args)
    {
        final String scontent = content.toUserMessage(McLibInterface.instance().getDefaultLocale(), args);
        
        final Sign s = this.getBukkitSign();
        s.setLine(index, scontent);
        s.update();
        
        this.lines.set(index, content);

        if (this.nmsSign != null)
        {
            this.nmsSign.updateLines(this.lines);
        }
    }
    
    /**
     * Copies configuration for storing in schemata.
     * 
     * @param lowloc
     *            starting position of schemata part
     * @return data section holding configuration needed for restoring the zone.
     */
    public DataSection copyAndSaveConfig(Location lowloc)
    {
        final DataSection result = new MemoryDataSection();
        result.set("otype", "sign"); //$NON-NLS-1$ //$NON-NLS-2$
        result.set("tplugin", this.getTypeId().getPluginName()); //$NON-NLS-1$
        result.set("tname", this.getTypeId().name()); //$NON-NLS-1$
        result.set("x", this.getLocation().getBlockX() - lowloc.getBlockX()); //$NON-NLS-1$
        result.set("y", this.getLocation().getBlockY() - lowloc.getBlockY()); //$NON-NLS-1$
        result.set("z", this.getLocation().getBlockZ() - lowloc.getBlockZ()); //$NON-NLS-1$
        if (this.config != null)
        {
            for (final Map.Entry<String, Object> entry : this.config.getValues(true).entrySet())
            {
                result.set("file." + entry.getKey(), entry.getValue()); //$NON-NLS-1$
            }
        }
        return result;
    }
    
}
