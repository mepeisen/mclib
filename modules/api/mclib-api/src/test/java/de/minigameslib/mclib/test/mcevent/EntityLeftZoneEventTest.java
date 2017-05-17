/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-Entityed commercial license from minigames.de
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

package de.minigameslib.mclib.test.mcevent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.mcevent.EntityLeftZoneEvent;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;

/**
 * Test for {@link EntityLeftZoneEvent}.
 * 
 * @author mepeisen
 */
public class EntityLeftZoneEventTest
{
    
    /**
     * simple test.
     * @throws ClassNotFoundException 
     *             thrown on error
     */
    @Test
    public void testMe() throws ClassNotFoundException
    {
        final ObjectServiceInterface osi = mock(ObjectServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.objects.ObjectServiceCache"), "SERVICES", osi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final ZoneInterface zone = mock(ZoneInterface.class);
        final Entity entity = mock(Entity.class);
        final EntityInterface mcentity = mock(EntityInterface.class);
        when(osi.findEntities(entity)).thenReturn(Collections.singletonList(mcentity));
        final EntityLeftZoneEvent evt = new EntityLeftZoneEvent(zone, entity);
        
        assertSame(evt, evt.getBukkitEvent());
        assertSame(evt.getHandlers(), EntityLeftZoneEvent.getHandlerList());
        final Iterator<EntityInterface> iter = evt.getEntities().iterator();
        assertTrue(iter.hasNext());
        assertSame(mcentity, iter.next());
        assertFalse(iter.hasNext());
        
        final Iterator<ZoneInterface> iter2 = evt.getZones().iterator();
        assertTrue(iter2.hasNext());
        assertSame(zone, iter2.next());
        assertFalse(iter2.hasNext());
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     * @throws ClassNotFoundException 
     *             thrown on error
     */
    @Test(expected = McException.class)
    public void testTrueStub() throws McException, ClassNotFoundException
    {
        final ObjectServiceInterface osi = mock(ObjectServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.objects.ObjectServiceCache"), "SERVICES", osi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final ZoneInterface zone = mock(ZoneInterface.class);
        final Entity entity = mock(Entity.class);
        final EntityLeftZoneEvent evt = new EntityLeftZoneEvent(zone, entity);
        evt.when(p -> true).thenThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     * @throws ClassNotFoundException 
     *             thrown on error
     */
    @Test
    public void testTrueStub2() throws McException, ClassNotFoundException
    {
        final ObjectServiceInterface osi = mock(ObjectServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.objects.ObjectServiceCache"), "SERVICES", osi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final ZoneInterface zone = mock(ZoneInterface.class);
        final Entity entity = mock(Entity.class);
        final EntityLeftZoneEvent evt = new EntityLeftZoneEvent(zone, entity);
        evt.when(p -> true)._elseThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     * @throws ClassNotFoundException
     *             thrown on error
     */
    @Test(expected = McException.class)
    public void testFalseStub() throws McException, ClassNotFoundException
    {
        final ObjectServiceInterface osi = mock(ObjectServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.objects.ObjectServiceCache"), "SERVICES", osi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final ZoneInterface zone = mock(ZoneInterface.class);
        final Entity entity = mock(Entity.class);
        final EntityLeftZoneEvent evt = new EntityLeftZoneEvent(zone, entity);
        evt.when(p -> false)._elseThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     * @throws ClassNotFoundException
     *             thrown on error
     */
    @Test
    public void testFalseStub2() throws McException, ClassNotFoundException
    {
        final ObjectServiceInterface osi = mock(ObjectServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.objects.ObjectServiceCache"), "SERVICES", osi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final ZoneInterface zone = mock(ZoneInterface.class);
        final Entity entity = mock(Entity.class);
        final EntityLeftZoneEvent evt = new EntityLeftZoneEvent(zone, entity);
        evt.when(p -> false).thenThrow(CommonMessages.InvokeIngame);
    }
    
}
