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

package de.minigameslib.mclib.client.impl.markers;

import org.lwjgl.opengl.GL11;

import de.minigameslib.mclib.client.impl.util.CameraPos;

/**
 * A marker for highlight cuboids.
 * 
 * @author mepeisen
 */
public class CuboidMarker implements MarkerInterface
{
    
    /** x1 coordinate. */
    private int x1;
    /** y1 coordinate. */
    private int y1;
    /** z1 coordinate. */
    private int z1;
    /** x2 coordinate. */
    private int x2;
    /** y2 coordinate. */
    private int y2;
    /** z2 coordinate. */
    private int z2;
    /** the color used by this marker. */
    private MarkerColor color;
    
    /** cuboid pos 1. */
    private BlockMarker pos1;
    
    /** cuboid pos 2. */
    private BlockMarker pos2;

    /**
     * Cponstructor.
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @param color
     */
    public CuboidMarker(int x1, int y1, int z1, int x2, int y2, int z2, MarkerColor color)
    {
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.z2 = Math.max(z1, z2);
        this.color = color;
        this.pos1 = new BlockMarker(x1, y1, z1, color);
        this.pos2 = new BlockMarker(x2, y2, z2, color);
    }
    
    @Override
    public void render(CameraPos cameraPos)
    {
        double dx1 = this.x1 - cameraPos.getX();
        double dy1 = this.y1 - cameraPos.getY();
        double dz1 = this.z1 - cameraPos.getZ();
        
        double dx2 = this.x2 - cameraPos.getX();
        double dy2 = this.y2 - cameraPos.getY();
        double dz2 = this.z2 - cameraPos.getZ();
        
        int diffx = this.x2 - this.x1 + 1;
        int diffy = this.y2 - this.y1 + 1;
        int diffz = this.z2 - this.z1 + 1;
        
        {
            GL11.glPushMatrix();
            
            GL11.glLineWidth(6.0f);
            GL11.glColor4f(this.color.getR() * 0.8f, this.color.getG() * 0.8f, this.color.getB() * 0.8f, this.color.getA() * 0.5f);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
            for (int x = 0; x <= diffx; x++)
            {
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex3d(dx1 + x, dy1, dz1);
                GL11.glVertex3d(dx1 + x, dy2 + 1, dz1);
                GL11.glVertex3d(dx1 + x, dy2 + 1, dz2 + 1);
                GL11.glVertex3d(dx1 + x, dy1, dz2 + 1);
                GL11.glEnd();
            }
            for (int y = 0; y <= diffy; y++)
            {
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex3d(dx1, dy1 + y, dz1);
                GL11.glVertex3d(dx2 + 1, dy1 + y, dz1);
                GL11.glVertex3d(dx2 + 1, dy1 + y, dz2 + 1);
                GL11.glVertex3d(dx1, dy1 + y, dz2 + 1);
                GL11.glEnd();
            }
            for (int z = 0; z <= diffz; z++)
            {
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex3d(dx1, dy1, dz1 + z);
                GL11.glVertex3d(dx2 + 1, dy1, dz1 + z);
                GL11.glVertex3d(dx2 + 1, dy2 + 1, dz1 + z);
                GL11.glVertex3d(dx1, dy2 + 1, dz1 + z);
                GL11.glEnd();
            }
            
            GL11.glLineWidth(12.0f);
            GL11.glColor4f(this.color.getR() * 0.6f, this.color.getG() * 0.6f, this.color.getB() * 0.6f, this.color.getA() * 0.25f);
            for (int x = 0; x <= diffx; x++)
            {
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex3d(dx1 + x, dy1 + 0.02d, dz1 + 0.02d);
                GL11.glVertex3d(dx1 + x, dy2 + 0.98d, dz1 + 0.02d);
                GL11.glVertex3d(dx1 + x, dy2 + 0.98d, dz2 + 0.98d);
                GL11.glVertex3d(dx1 + x, dy1 + 0.02d, dz2 + 0.98d);
                GL11.glEnd();
            }
            for (int y = 0; y <= diffy; y++)
            {
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex3d(dx1 + 0.02d, dy1 + y, dz1 + 0.02d);
                GL11.glVertex3d(dx2 + 0.98d, dy1 + y, dz1 + 0.02d);
                GL11.glVertex3d(dx2 + 0.98d, dy1 + y, dz2 + 0.98d);
                GL11.glVertex3d(dx1 + 0.02d, dy1 + y, dz2 + 0.98d);
                GL11.glEnd();
            }
            for (int z = 0; z <= diffz; z++)
            {
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex3d(dx1 + 0.02d, dy1 + 0.02d, dz1 + z);
                GL11.glVertex3d(dx2 + 0.98d, dy1 + 0.02d, dz1 + z);
                GL11.glVertex3d(dx2 + 0.98d, dy2 + 0.98d, dz1 + z);
                GL11.glVertex3d(dx1 + 0.02d, dy2 + 0.98d, dz1 + z);
                GL11.glEnd();
            }
            
            GL11.glPopMatrix();
        }
        
        this.pos1.render(cameraPos);
        this.pos2.render(cameraPos);
    }
    
}
