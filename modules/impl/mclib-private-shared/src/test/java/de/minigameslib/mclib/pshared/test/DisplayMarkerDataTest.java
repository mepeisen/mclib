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

package de.minigameslib.mclib.pshared.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.minigameslib.mclib.pshared.DisplayMarkerData;
import de.minigameslib.mclib.pshared.MarkerData;
import de.minigameslib.mclib.pshared.MarkerData.BlockMarkerData;
import de.minigameslib.mclib.pshared.MarkerData.CuboidMarkerData;
import de.minigameslib.mclib.pshared.MarkerData.MarkerColorData;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link DisplayMarkerData}.
 * 
 * @author mepeisen
 *
 */
public class DisplayMarkerDataTest
{
    
    /**
     * Simple test case for reading/storing data.
     */
    @Test
    public void testMe()
    {
        final DisplayMarkerData data = new DisplayMarkerData();
        data.setMarkerId("winid"); //$NON-NLS-1$
        final MarkerData marker = new MarkerData();
        marker.setPlugin("plugin"); //$NON-NLS-1$
        marker.setTypeId("type"); //$NON-NLS-1$
        data.setMarker(marker);
        assertEquals("winid", data.getMarkerId()); //$NON-NLS-1$
        assertEquals("plugin", data.getMarker().getPlugin()); //$NON-NLS-1$
        assertEquals("type", data.getMarker().getTypeId()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final DisplayMarkerData data2 = section.getFragment(DisplayMarkerData.class, "FOO"); //$NON-NLS-1$
        assertEquals("winid", data2.getMarkerId()); //$NON-NLS-1$
        assertEquals("plugin", data2.getMarker().getPlugin()); //$NON-NLS-1$
        assertEquals("type", data2.getMarker().getTypeId()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case for reading/storing data.
     */
    @Test
    public void testCuboid()
    {
        final DisplayMarkerData data = new DisplayMarkerData();
        final MarkerData marker = new MarkerData();
        data.setMarker(marker);
        final CuboidMarkerData cub = new CuboidMarkerData();
        marker.setCuboid(cub);
        cub.setX1(1);
        cub.setX2(2);
        cub.setY1(3);
        cub.setY2(4);
        cub.setZ1(5);
        cub.setZ2(6);
        assertEquals(1, data.getMarker().getCuboid().getX1());
        assertEquals(2, data.getMarker().getCuboid().getX2());
        assertEquals(3, data.getMarker().getCuboid().getY1());
        assertEquals(4, data.getMarker().getCuboid().getY2());
        assertEquals(5, data.getMarker().getCuboid().getZ1());
        assertEquals(6, data.getMarker().getCuboid().getZ2());
        
        final MarkerColorData color = new MarkerColorData();
        color.setR(10);
        color.setG(20);
        color.setB(30);
        color.setAlpha(40);
        cub.setColor(color);

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final DisplayMarkerData data2 = section.getFragment(DisplayMarkerData.class, "FOO"); //$NON-NLS-1$
        assertEquals(1, data2.getMarker().getCuboid().getX1());
        assertEquals(2, data2.getMarker().getCuboid().getX2());
        assertEquals(3, data2.getMarker().getCuboid().getY1());
        assertEquals(4, data2.getMarker().getCuboid().getY2());
        assertEquals(5, data2.getMarker().getCuboid().getZ1());
        assertEquals(6, data2.getMarker().getCuboid().getZ2());

        assertEquals(10, data2.getMarker().getCuboid().getColor().getR());
        assertEquals(20, data2.getMarker().getCuboid().getColor().getG());
        assertEquals(30, data2.getMarker().getCuboid().getColor().getB());
        assertEquals(40, data2.getMarker().getCuboid().getColor().getAlpha());
    }
    
    /**
     * Simple test case for reading/storing data.
     */
    @Test
    public void testBlock()
    {
        final DisplayMarkerData data = new DisplayMarkerData();
        final MarkerData marker = new MarkerData();
        data.setMarker(marker);
        final BlockMarkerData block = new BlockMarkerData();
        block.setX(1);
        block.setY(2);
        block.setZ(3);
        marker.setBlock(block);
        assertEquals(1, data.getMarker().getBlock().getX());
        assertEquals(2, data.getMarker().getBlock().getY());
        assertEquals(3, data.getMarker().getBlock().getZ());
        
        final MarkerColorData color = new MarkerColorData();
        color.setR(10);
        color.setG(20);
        color.setB(30);
        color.setAlpha(40);
        block.setColor(color);

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final DisplayMarkerData data2 = section.getFragment(DisplayMarkerData.class, "FOO"); //$NON-NLS-1$
        assertEquals(1, data2.getMarker().getBlock().getX());
        assertEquals(2, data2.getMarker().getBlock().getY());
        assertEquals(3, data2.getMarker().getBlock().getZ());
        
        assertEquals(10, data2.getMarker().getBlock().getColor().getR());
        assertEquals(20, data2.getMarker().getBlock().getColor().getG());
        assertEquals(30, data2.getMarker().getBlock().getColor().getB());
        assertEquals(40, data2.getMarker().getBlock().getColor().getAlpha());
    }
    
}
