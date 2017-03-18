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

import de.minigameslib.mclib.pshared.MarkerData.MarkerColorData;

/**
 * A color for the markers.
 * 
 * @author mepeisen
 */
public class MarkerColor
{

    /** yellow color. */
    public static final MarkerColor YELLOW = new MarkerColor(1.0f, 1.0f, 0.0f, 0.5f);
    /** bluecolor. */
    public static final MarkerColor BLUE = new MarkerColor(0.0f, 0.0f, 1.0f, 0.5f);
    
    /** rbg color code. */
    private final float r;
    /** rbg color code. */
    private final float g;
    /** rbg color code. */
    private final float b;
    /** rbg color code. */
    private final float a;
    
    /**
     * Constructor.
     * @param r
     * @param g
     * @param b
     * @param a
     */
    private MarkerColor(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    /**
     * Constructor to create marker color from given color data.
     * @param data
     */
    public MarkerColor(MarkerColorData data)
    {
        this.r = data.getR();
        this.g = data.getG();
        this.b = data.getB();
        this.a = data.getAlpha();
    }

    /**
     * @return the r
     */
    public float getR()
    {
        return this.r;
    }

    /**
     * @return the g
     */
    public float getG()
    {
        return this.g;
    }

    /**
     * @return the b
     */
    public float getB()
    {
        return this.b;
    }

    /**
     * @return the a
     */
    public float getA()
    {
        return this.a;
    }
    
}
