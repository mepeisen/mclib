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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.HologramDeleteEvent;
import de.minigameslib.mclib.api.mcevent.HologramDeletedEvent;
import de.minigameslib.mclib.api.mcevent.HologramRelocateEvent;
import de.minigameslib.mclib.api.mcevent.HologramRelocatedEvent;
import de.minigameslib.mclib.api.objects.HologramHandlerInterface;
import de.minigameslib.mclib.api.objects.HologramInterface;
import de.minigameslib.mclib.api.objects.HologramTypeId;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.HologramHelperInterface;
import de.minigameslib.mclib.nms.api.HologramHelperInterface.HologramEntityInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Hologram implementation.
 * 
 * @author mepeisen
 *
 */
public class HologramImpl extends AbstractLocationComponent implements HologramInterface, MgEventListener
{
    
    /** hologram id. */
    private final HologramId               id;
    
    /** hologram handler. */
    private final HologramHandlerInterface handler;
    
    /** an event bus to handle events. */
    private final EventBus                 eventBus  = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    
    /** hiffenflag. */
    private boolean                        hidden;
    
    /** player whitelist. */
    private final Set<UUID>                whitelist = new HashSet<>();
    
    /** the current lines. */
    private final List<Serializable>       lines     = new ArrayList<>();
    
    /** the nms helper to display the hologram. */
    private HologramEntityInterface        nms;
    
    /**
     * Constructor.
     * 
     * @param plugin
     *            the owning plugin.
     * @param registry
     *            the registry.
     * @param location
     *            the holograms location
     * @param id
     *            the id
     * @param handler
     *            the handler managing the hologram
     * @param config
     *            the config file
     * @param owner
     *            thw owning service
     * @throws McException
     *             thrown on config errors
     */
    public HologramImpl(Plugin plugin, ComponentRegistry registry, Location location, HologramId id, HologramHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(registry, location, config, owner);
        this.id = id;
        this.handler = handler;
        if (this.handler instanceof McListener)
        {
            this.eventBus.registerHandlers(plugin, (McListener) this.handler);
        }
    }
    
    @Override
    public HologramId getHologramId()
    {
        return this.id;
    }
    
    @Override
    public void readData(DataSection coreSection)
    {
        // TODO should we re-read the id?
        // the id is already read from registry.yml
        this.handler.read(coreSection.getSection("handler")); //$NON-NLS-1$
        this.hidden = coreSection.getBoolean("hidden"); //$NON-NLS-1$
        final List<String> list = coreSection.getStringList("whitelist"); //$NON-NLS-1$
        this.whitelist.clear();
        if (list != null)
        {
            list.stream().map(UUID::fromString).forEach(this.whitelist::add);
        }
    }
    
    @Override
    protected void saveData(DataSection coreSection)
    {
        this.id.write(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.write(coreSection.createSection("handler")); //$NON-NLS-1$
        coreSection.set("hidden", this.hidden); //$NON-NLS-1$
        coreSection.set("whitelist", this.whitelist.stream().map(UUID::toString).collect(Collectors.toList())); //$NON-NLS-1$
    }
    
    @Override
    public void setLocation(Location loc) throws McException
    {
        final Location old = this.location;
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(HologramInterface.class, this);
            this.handler.canChangeLocation(loc);
        });
        final HologramRelocateEvent relocateEvent = new HologramRelocateEvent(this, old, loc);
        Bukkit.getPluginManager().callEvent(relocateEvent);
        if (relocateEvent.isCancelled())
        {
            throw new McException(relocateEvent.getVetoReason(), relocateEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(HologramInterface.class, this);
            super.setLocation(loc);
            this.handler.onLocationChange(loc);
        });
        
        final HologramRelocatedEvent relocatedEvent = new HologramRelocatedEvent(this, old, loc);
        Bukkit.getPluginManager().callEvent(relocatedEvent);
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
            McLibInterface.instance().setContext(HologramInterface.class, this);
            this.handler.canDelete();
        });
        
        final HologramDeleteEvent deleteEvent = new HologramDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(HologramInterface.class, this);
            super.delete();
            this.handler.onDelete();
        });
        this.eventBus.clear();
        Bukkit.getServicesManager().load(NmsFactory.class).create(HologramHelperInterface.class).delete(this.nms);
        
        final HologramDeletedEvent deletedEvent = new HologramDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
    }
    
    /**
     * Clears all event registrations.
     */
    public void clearEventRegistrations()
    {
        this.eventBus.clear();
    }
    
    @Override
    public HologramHandlerInterface getHandler()
    {
        return this.handler;
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
    public HologramTypeId getTypeId()
    {
        return ObjectServiceInterface.instance().getType(this.getHologramId());
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
        result.set("otype", "hologram"); //$NON-NLS-1$ //$NON-NLS-2$
        result.set("tplugin", this.getTypeId().getPluginName()); //$NON-NLS-1$
        result.set("tname", this.getTypeId().name()); //$NON-NLS-1$
        result.set("x", this.getLocation().getBlockX() - lowloc.getBlockX()); //$NON-NLS-1$
        result.set("y", this.getLocation().getBlockY() - lowloc.getBlockY()); //$NON-NLS-1$
        result.set("z", this.getLocation().getBlockZ() - lowloc.getBlockZ()); //$NON-NLS-1$
        result.set("hidden", this.hidden); //$NON-NLS-1$
        result.set("whitelist", this.whitelist.stream().map(UUID::toString).collect(Collectors.toList())); //$NON-NLS-1$
        if (this.config != null)
        {
            for (final Map.Entry<String, Object> entry : this.config.getValues(true).entrySet())
            {
                result.set("file." + entry.getKey(), entry.getValue()); //$NON-NLS-1$
            }
        }
        return result;
    }
    
    @Override
    public void updateLines()
    {
        if (this.nms != null)
        {
            this.nms.updateLines(this.getLines());
        }
    }
    
    @Override
    public void setLines(Serializable... strings)
    {
        this.lines.clear();
        this.lines.addAll(Arrays.asList(strings));
        this.updateLines();
    }
    
    @Override
    public List<Serializable> getLines()
    {
        return new ArrayList<>(this.lines);
    }
    
    @Override
    public void addLine(Serializable string)
    {
        this.lines.add(string);
        this.updateLines();
    }
    
    @Override
    public void removeLine(int lineNumber)
    {
        this.lines.remove(lineNumber - 1);
        this.updateLines();
    }
    
    @Override
    public void changeLine(int lineNumber, Serializable string)
    {
        this.lines.set(lineNumber - 1, string);
        this.updateLines();
    }
    
    @Override
    public void clearLines()
    {
        this.lines.clear();
        this.updateLines();
    }
    
    @Override
    public void addPlayeToWhiteList(UUID uuid) throws McException
    {
        this.whitelist.add(uuid);
        this.saveConfig();
        if (this.hidden)
        {
            this.nms.hide(this.getWhitelist());
        }
    }
    
    @Override
    public void removePlayeFromWhiteList(UUID uuid) throws McException
    {
        this.whitelist.remove(uuid);
        this.saveConfig();
        if (this.hidden)
        {
            this.nms.hide(this.getWhitelist());
        }
    }
    
    @Override
    public List<UUID> getWhitelist()
    {
        return new ArrayList<>(this.whitelist);
    }
    
    @Override
    public boolean isHidden()
    {
        return this.hidden;
    }
    
    @Override
    public void hide() throws McException
    {
        this.hidden = true;
        this.saveConfig();
        this.nms.hide(this.getWhitelist());
    }
    
    @Override
    public void show() throws McException
    {
        this.hidden = false;
        this.saveConfig();
        this.nms.show();
    }
    
    /**
     * Sets the nms classes.
     * 
     * @param nms
     *            helper.
     */
    public void setNms(HologramEntityInterface nms)
    {
        this.nms = nms;
        if (this.hidden)
        {
            this.nms.hide(this.getWhitelist());
        }
        this.nms.updateLines(this.getLines());
    }
    
}
