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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeletedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneRelocateEvent;
import de.minigameslib.mclib.api.mcevent.ZoneRelocatedEvent;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneScoreboardVariant;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Implementation of zones.
 * 
 * @author mepeisen
 */
public class ZoneImpl extends AbstractCuboidComponent implements ZoneInterface, MgEventListener
{
    
    /** zone id. */
    private final ZoneId                      id;
    
    /** zone handler. */
    private final ZoneHandlerInterface        handler;
    
    /** an event bus to handle events. */
    private final EventBus                    eventBus    = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    
    /** the zone scoreboard. */
    private final Map<String, ZoneScoreboard> scoreboards = new HashMap<>();
    
    /**
     * Constructor.
     * 
     * @param plugin
     *            the owning plugin.
     * @param registry
     *            the registry.
     * @param cuboid
     *            the zones cuboid
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
    public ZoneImpl(Plugin plugin, ComponentRegistry registry, Cuboid cuboid, ZoneId id, ZoneHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(registry, cuboid, config, owner);
        this.id = id;
        this.handler = handler;
        if (this.handler instanceof McListener)
        {
            this.eventBus.registerHandlers(plugin, (McListener) this.handler);
        }
    }
    
    @Override
    public ZoneId getZoneId()
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
            McLibInterface.instance().setContext(ZoneInterface.class, this);
            this.handler.canDelete();
        });
        
        final ZoneDeleteEvent deleteEvent = new ZoneDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ZoneInterface.class, this);
            super.delete();
            this.handler.onDelete();
        });
        this.eventBus.clear();
        
        final ZoneDeletedEvent deletedEvent = new ZoneDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
        
        final ZoneScoreboard[] sboards = this.scoreboards.values().toArray(new ZoneScoreboard[this.scoreboards.size()]);
        this.scoreboards.clear();
        for (final ZoneScoreboard sb : sboards)
        {
            sb.delete();
        }
    }
    
    @Override
    public void setCuboid(Cuboid cub) throws McException
    {
        final Cuboid old = this.cuboid;
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ZoneInterface.class, this);
            this.handler.canChangeCuboid(cub);
        });
        final ZoneRelocateEvent relocateEvent = new ZoneRelocateEvent(this, old, cub);
        Bukkit.getPluginManager().callEvent(relocateEvent);
        if (relocateEvent.isCancelled())
        {
            throw new McException(relocateEvent.getVetoReason(), relocateEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ZoneInterface.class, this);
            super.setCuboid(cub);
            this.handler.onCuboidChange(cub);
        });
        
        final ZoneRelocatedEvent relocatedEvent = new ZoneRelocatedEvent(this, old, cub);
        Bukkit.getPluginManager().callEvent(relocatedEvent);
        
        this.scoreboards.values().forEach(sb -> sb.changeCuboid(cub));
    }
    
    @Override
    protected void saveData(DataSection coreSection)
    {
        this.id.write(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.write(coreSection.createSection("handler")); //$NON-NLS-1$
    }
    
    @Override
    public void readData(DataSection coreSection)
    {
        // TODO should we re-read the id?
        // the id is already read from registry.yml
        this.handler.read(coreSection.getSection("handler")); //$NON-NLS-1$
    }
    
    @Override
    public ZoneHandlerInterface getHandler()
    {
        return this.handler;
    }
    
    @Override
    public Collection<ZoneInterface> getChildZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindChildren);
    }
    
    @Override
    public Collection<ZoneInterface> getChildZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindChildren, type);
    }
    
    @Override
    public Collection<ZoneInterface> getParentZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindParents);
    }
    
    @Override
    public Collection<ZoneInterface> getParentZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindParents, type);
    }
    
    @Override
    public Collection<ZoneInterface> getMatchingZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindMatching);
    }
    
    @Override
    public Collection<ZoneInterface> getMatchingZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindMatching, type);
    }
    
    @Override
    public Collection<ZoneInterface> getOverlappingZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindOverlapping);
    }
    
    @Override
    public Collection<ZoneInterface> getOverlappingZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindOverlapping, type);
    }
    
    @Override
    public Collection<ZoneInterface> getSharedZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindShared);
    }
    
    @Override
    public Collection<ZoneInterface> getSharedZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindShared, type);
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
     *            Plugin that was disabled.
     */
    public void onDisable(Plugin plugin)
    {
        this.eventBus.onDisable(plugin);
    }
    
    @Override
    public ZoneTypeId getTypeId()
    {
        return ObjectServiceInterface.instance().getType(this.getZoneId());
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
        result.set("otype", "zone"); //$NON-NLS-1$ //$NON-NLS-2$
        result.set("tplugin", this.getTypeId().getPluginName()); //$NON-NLS-1$
        result.set("tname", this.getTypeId().name()); //$NON-NLS-1$
        result.set("x1", this.getCuboid().getLowLoc().getBlockX() - lowloc.getBlockX()); //$NON-NLS-1$
        result.set("y1", this.getCuboid().getLowLoc().getBlockY() - lowloc.getBlockY()); //$NON-NLS-1$
        result.set("z1", this.getCuboid().getLowLoc().getBlockZ() - lowloc.getBlockZ()); //$NON-NLS-1$
        result.set("x2", this.getCuboid().getHighLoc().getBlockX() - lowloc.getBlockX()); //$NON-NLS-1$
        result.set("y2", this.getCuboid().getHighLoc().getBlockY() - lowloc.getBlockY()); //$NON-NLS-1$
        result.set("z2", this.getCuboid().getHighLoc().getBlockZ() - lowloc.getBlockZ()); //$NON-NLS-1$
        if (this.config != null)
        {
            for (final Map.Entry<String, Object> entry : this.config.getValues(true).entrySet())
            {
                result.set("file." + entry.getKey(), entry.getValue()); //$NON-NLS-1$
            }
        }
        return result;
    }
    
    /**
     * Returns the best matching scoreboard for this zone.
     * @param player target player.
     * @return best matching scoreboard.
     */
    public ZoneScoreboard getBestMatchingScoreboard(UUID player)
    {
        // first check for a hidden scoreboard with this player being whitelisted.
        for (final ZoneScoreboard sb : this.scoreboards.values())
        {
            if (sb.isHidden() && sb.getWhitelist().contains(player))
            {
                return sb;
            }
        }
        // check for visibiel variant
        for (final ZoneScoreboard sb : this.scoreboards.values())
        {
            if (!sb.isHidden())
            {
                return sb;
            }
        }
        // no one found
        return null;
    }
    
    @Override
    public List<ZoneScoreboardVariant> getScoreboards()
    {
        return new ArrayList<>(this.scoreboards.values());
    }
    
    @Override
    public ZoneScoreboardVariant getScoreboardVariant(String name)
    {
        return this.scoreboards.get(name);
    }
    
    @Override
    public ZoneScoreboardVariant createScoreboardVariant(String name)
    {
        return this.scoreboards.computeIfAbsent(name, n -> new ZoneScoreboard(n, this.cuboid));
    }

    @Override
    public void deleteScoreboardVariant(String name)
    {
        final ZoneScoreboard sb = this.scoreboards.remove(name);
        if (sb != null)
        {
            sb.delete();
        }
    }
    
}
