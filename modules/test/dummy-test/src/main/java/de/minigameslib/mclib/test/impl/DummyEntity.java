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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEntityDamageEvent;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.McPlayerInteractAtEntityEvent;
import de.minigameslib.mclib.api.event.McPlayerInteractEntityEvent;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;

/**
 * @author mepeisen
 *
 */
public class DummyEntity extends AnnotatedDataFragment implements EntityHandlerInterface, McListener
{
    
    @McEventHandler
    public void onDamage(McEntityDamageEvent evt)
    {
        evt.getBukkitEvent().setCancelled(true);
        System.out.println("LEFT CLICK BY " + evt.getBukkitEvent().getEntity());
    }
    
    @McEventHandler
    public void onDamage(McPlayerInteractAtEntityEvent evt)
    {
        evt.getBukkitEvent().setCancelled(true);
        System.out.println("RIGHT CLICK BY " + evt.getBukkitEvent().getPlayer());
    }
    
    @Override
    public void onCreate(EntityInterface entity) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onResume(EntityInterface entity) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onPause(EntityInterface entity)
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
    
}
