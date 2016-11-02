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
import org.bukkit.configuration.ConfigurationSection;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;

/**
 * @author mepeisen
 *
 */
public class SignImpl extends AbstractLocationComponent implements SignInterface
{
    
    /**
     * @param registry
     * @param location
     * @param config 
     * @param owner 
     */
    public SignImpl(ComponentRegistry registry, Location location, File config, ComponentOwner owner)
    {
        super(registry, location, config, owner);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.objects.SignInterface#getSignId()
     */
    @Override
    public SignIdInterface getSignId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.objects.SignInterface#delete()
     */
    @Override
    public void delete() throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.objects.SignInterface#saveConfig()
     */
    @Override
    public void saveConfig() throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.impl.comp.AbstractLocationComponent#saveData(org.bukkit.configuration.ConfigurationSection)
     */
    @Override
    protected void saveData(ConfigurationSection coreSection)
    {
        // TODO Auto-generated method stub
        
    }
    
}
