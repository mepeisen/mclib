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

package de.minigameslib.mclib.test.impl;

import java.io.Serializable;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.HologramHandlerInterface;
import de.minigameslib.mclib.api.objects.HologramInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * @author mepeisen
 *
 */
public class DummyHologram implements HologramHandlerInterface
{
    
    @Override
    public void read(DataSection section)
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void write(DataSection section)
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public boolean test(DataSection section)
    {
        return true;
    }
    
    @Override
    public void onCreate(HologramInterface hologram) throws McException
    {
        hologram.setLines(new Serializable[]{"§4line1", "§5line2"}); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Override
    public void onResume(HologramInterface hologram) throws McException
    {
        hologram.setLines(new Serializable[]{"line1", "line2"}); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Override
    public void onPause(HologramInterface hologram)
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void canDelete() throws McException
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void onDelete()
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void canChangeLocation(Location newValue) throws McException
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void onLocationChange(Location newValue)
    {
        // TODO Auto-generated method stub
    }
    
}
