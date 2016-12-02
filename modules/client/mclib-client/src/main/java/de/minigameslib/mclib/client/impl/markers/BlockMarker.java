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
 * A marker for highlight blocks.
 * 
 * @author mepeisen
 */
public class BlockMarker implements MarkerInterface
{
    
    /** x coordinate. */
    private int x;
    /** y coordinate. */
    private int y;
    /** z coordinate. */
    private int z;
    /** the color used by this marker. */
    private MarkerColor color;

    /**
     * Cponstructor.
     * @param x
     * @param y
     * @param z
     * @param color
     */
    public BlockMarker(int x, int y, int z, MarkerColor color)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }
    
    @Override
    public void render(CameraPos cameraPos)
    {
        double dx1 = this.x - cameraPos.getX();
        double dy1 = this.y - cameraPos.getY();
        double dz1 = this.z - cameraPos.getZ();
        
        double dx2 = this.x - cameraPos.getX() + 1;
        double dy2 = this.y - cameraPos.getY() + 1;
        double dz2 = this.z - cameraPos.getZ() + 1;
        
        {
            GL11.glPushMatrix();
            GL11.glLineWidth(6.0f);
            
            GL11.glColor4f(this.color.getR(), this.color.getG(), this.color.getB(), this.color.getA());
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3d(dx1, dy1, dz1);
            GL11.glVertex3d(dx2, dy1, dz1);
            GL11.glVertex3d(dx2, dy2, dz1);
            GL11.glVertex3d(dx1, dy2, dz1);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3d(dx1, dy1, dz2);
            GL11.glVertex3d(dx2, dy1, dz2);
            GL11.glVertex3d(dx2, dy2, dz2);
            GL11.glVertex3d(dx1, dy2, dz2);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx1, dy1, dz1);
            GL11.glVertex3d(dx1, dy1, dz2);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx2, dy1, dz1);
            GL11.glVertex3d(dx2, dy1, dz2);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx2, dy2, dz1);
            GL11.glVertex3d(dx2, dy2, dz2);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx1, dy2, dz1);
            GL11.glVertex3d(dx1, dy2, dz2);
            GL11.glEnd();

            GL11.glLineWidth(12.0f);
            GL11.glColor4f(this.color.getR() * 0.9f, this.color.getG() * 0.9f, this.color.getB() * 0.9f, this.color.getA() * 0.5f);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
            GL11.glBegin(GL11.GL_LINE_LOOP);
            final double dx1b = dx1 + 0.02d;
            final double dx2b = dx2 - 0.02d;
            final double dy1b = dy1 + 0.02d;
            final double dy2b = dy2 - 0.02d;
            final double dz1b = dz1 + 0.02d;
            final double dz2b = dz2 - 0.02d;
            GL11.glVertex3d(dx1b, dy1b, dz1b);
            GL11.glVertex3d(dx2b, dy1b, dz1b);
            GL11.glVertex3d(dx2b, dy2b, dz1b);
            GL11.glVertex3d(dx1b, dy2b, dz1b);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3d(dx1b, dy1b, dz2b);
            GL11.glVertex3d(dx2b, dy1b, dz2b);
            GL11.glVertex3d(dx2b, dy2b, dz2b);
            GL11.glVertex3d(dx1b, dy2b, dz2b);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx1b, dy1b, dz1b);
            GL11.glVertex3d(dx1b, dy1b, dz2b);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx2b, dy1b, dz1b);
            GL11.glVertex3d(dx2b, dy1b, dz2b);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx2b, dy2b, dz1b);
            GL11.glVertex3d(dx2b, dy2b, dz2b);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(dx1b, dy2b, dz1b);
            GL11.glVertex3d(dx1b, dy2b, dz2b);
            GL11.glEnd();
            
            GL11.glPopMatrix();
        }
    }
    
}
