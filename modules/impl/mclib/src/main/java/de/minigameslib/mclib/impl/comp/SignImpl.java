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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.SignHandlerInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;

/**
 * @author mepeisen
 *
 */
public class SignImpl extends AbstractLocationComponent implements SignInterface
{
    
    /** the bukkit sign. */
    private Sign sign;
    
    /** the sign id. */
    private final SignId id;
    
    /** the sign handler. */
    private final SignHandlerInterface handler;
    
    /**
     * @param registry
     * @param sign 
     * @param id 
     * @param handler 
     * @param config 
     * @param owner 
     */
    public SignImpl(ComponentRegistry registry, Sign sign, SignId id, SignHandlerInterface handler, File config, ComponentOwner owner)
    {
        super(registry, sign == null ? null : sign.getLocation(), config, owner);
        this.sign = sign;
        this.id = id;
        this.handler = handler;
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
        this.handler.canDelete();
        super.delete();
        this.handler.onDelete();
    }
    
    @Override
    public void readData(ConfigurationSection coreSection)
    {
        // TODO should we re-read the id?
        // the id is already read from registry.yml
        this.handler.readFromConfig(coreSection.getConfigurationSection("handler")); //$NON-NLS-1$
    }

    @Override
    protected void saveData(ConfigurationSection coreSection)
    {
        this.id.writeToConfig(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.writeToConfig(coreSection.createSection("handler")); //$NON-NLS-1$
    }

    /**
     * Returns the handler.
     * @return handler.
     */
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
            this.sign = (Sign) block;
            super.setLocation(loc);
        }
        else
        {
            throw new McException(CommonMessages.SignNotFoundError);
        }
    }
    
}
