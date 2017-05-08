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

package de.minigameslib.mclib.impl.comp.builder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.NpcServiceInterface.HumanBuilderInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.skin.SkinServiceInterface;
import de.minigameslib.mclib.impl.comp.DynamicEntityType;
import de.minigameslib.mclib.impl.comp.EntityImpl;
import de.minigameslib.mclib.nms.api.EntityHelperInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;

/**
 * Implementation of HumanBuilderInterface to create dummy humans.
 * 
 * @author mepeisen
 */
public class HumanBuilder implements HumanBuilderInterface
{
    /** persistent flag. */
    private boolean                persistent;
    /** the npc location. */
    private Location               location;
    /** entity type. */
    private EntityTypeId           type;
    /** type handler. */
    private EntityHandlerInterface handler;
    /** name. */
    private String                 name;
    
    /** skin player. */
    private McPlayerInterface      skinPlayer;
    
    @Override
    public HumanBuilderInterface skin(McPlayerInterface player)
    {
        this.skinPlayer = player;
        return this;
    }
    
    @Override
    public HumanBuilderInterface persistent()
    {
        this.persistent = true;
        return this;
    }
    
    @Override
    public HumanBuilderInterface location(Location l)
    {
        this.location = l;
        return this;
    }
    
    @Override
    public HumanBuilderInterface name(String n)
    {
        this.name = n;
        return this;
    }
    
    @Override
    public HumanBuilderInterface handler(EntityTypeId t, EntityHandlerInterface h)
    {
        this.type = t;
        this.handler = h;
        return this;
    }
    
    @Override
    public EntityInterface create() throws McException
    {
        if (this.location == null)
        {
            throw new McException(CommonMessages.InternalError, "location is null"); //$NON-NLS-1$
        }
        if (this.type == null)
        {
            throw new McException(CommonMessages.InternalError, "type is null"); //$NON-NLS-1$
        }
        if (this.name == null)
        {
            throw new McException(CommonMessages.InternalError, "name is null"); //$NON-NLS-1$
        }
        final EntityHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class);
        final HumanEntity bukkitEntity = helper.spawnDummyHuman(this.location, this.name, null);
        final SkinServiceInterface skins = SkinServiceInterface.instance();
        final EntityImpl impl = (EntityImpl) ObjectServiceInterface.instance().createEntity(this.type, bukkitEntity, this.handler, this.persistent);
        if (this.skinPlayer != null)
        {
            skins.getSkinSnapshot(this.skinPlayer, s ->
            {
                skins.setToHuman(impl, s);
                if (this.persistent)
                {
                    impl.saveConfig();
                }
            });
        }
        if (this.persistent)
        {
            impl.setDynamicType(DynamicEntityType.DUMMY_HUMAN);
            impl.saveConfig();
        }
        return impl;
    }
}
