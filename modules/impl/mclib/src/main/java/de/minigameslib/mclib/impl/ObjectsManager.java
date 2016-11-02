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

package de.minigameslib.mclib.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.objects.ComponentHandlerInterface;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.SignHandlerInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.impl.comp.ComponentId;
import de.minigameslib.mclib.impl.comp.EntityId;
import de.minigameslib.mclib.impl.comp.SignId;
import de.minigameslib.mclib.impl.comp.ZoneId;

/**
 * A helper class managing plugin objects.
 * 
 * @author mepeisen
 *
 */
class ObjectsManager
{
 
    /** target data folder. */
    private final File dataFolder;
    
    /** registered components per id. */
    private final Map<String, Set<ComponentId>> componentsByPlugin = new HashMap<>();
    
    /** registered entities per id. */
    private final Map<String, Set<EntityId>> entitiesByPlugin = new HashMap<>();
    
    /** registered signs per id. */
    private final Map<String, Set<SignId>> signByPlugin = new HashMap<>();
    
    /** registered zones per id. */
    private final Map<String, Set<ZoneId>> zonesByPlugin = new HashMap<>();

    /**
     * Constructor.
     * @param dataFolder
     */
    public ObjectsManager(File dataFolder)
    {
        this.dataFolder = dataFolder;
    }

    /**
     * @param plugin
     */
    public void onDisable(Plugin plugin)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param plugin
     */
    public void onEnable(Plugin plugin)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param location
     * @return
     */
    public ComponentInterface findComponent(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param id
     * @return
     */
    public ComponentInterface findComponent(ComponentIdInterface id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param type
     * @param location
     * @param handler
     * @return
     */
    public ComponentInterface createComponent(ComponentTypeId type, Location location, ComponentHandlerInterface handler)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param entity
     * @return
     */
    public EntityInterface findEntity(Entity entity)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param id
     * @return
     */
    public EntityInterface findEntity(EntityIdInterface id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param type
     * @param entity
     * @param handler
     * @return
     */
    public EntityInterface createEntity(EntityTypeId type, Entity entity, EntityHandlerInterface handler)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param location
     * @return
     */
    public SignInterface findSign(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param block
     * @return
     */
    public SignInterface findSign(SignIdInterface id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param type
     * @param sign
     * @param handler
     * @return
     */
    public SignInterface createSign(SignTypeId type, Sign sign, SignHandlerInterface handler)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param type
     * @param cuboid
     * @param handler
     * @return
     */
    public ZoneInterface createZone(ZoneTypeId type, Cuboid cuboid, ZoneHandlerInterface handler)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param id
     * @return
     */
    public ZoneInterface findZone(ZoneIdInterface id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param location
     * @return
     */
    public Iterable<ZoneInterface> findZonesWithoutYD(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param location
     * @return
     */
    public ZoneInterface findZoneWithoutYD(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param location
     * @return
     */
    public Iterable<ZoneInterface> findZonesWithoutY(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param location
     * @return
     */
    public ZoneInterface findZoneWithoutY(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param location
     * @return
     */
    public Iterable<ZoneInterface> findZones(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param location
     * @return
     */
    public ZoneInterface findZone(Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    
}
