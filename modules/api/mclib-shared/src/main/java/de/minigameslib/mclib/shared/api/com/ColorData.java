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

package de.minigameslib.mclib.shared.api.com;

/**
 * Vector data.
 * 
 * @author mepeisen
 */
public class ColorData implements DataFragment
{
    
    /** red color component. */
    private byte red;
    /** green color component. */
    private byte green;
    /** blue color component. */
    private byte blue;

    /**
     * @param red
     * @param green
     * @param blue
     */
    public ColorData(byte red, byte green, byte blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.blue;
        result = prime * result + this.green;
        result = prime * result + this.red;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ColorData other = (ColorData) obj;
        if (this.blue != other.blue)
            return false;
        if (this.green != other.green)
            return false;
        if (this.red != other.red)
            return false;
        return true;
    }

    @Override
    public void read(DataSection section)
    {
        this.red = section.getByte("r", (byte) 0); //$NON-NLS-1$
        this.green = section.getByte("g", (byte) 0); //$NON-NLS-1$
        this.blue = section.getByte("b", (byte) 0); //$NON-NLS-1$
    }

    @Override
    public void write(DataSection section)
    {
        section.set("r", this.red); //$NON-NLS-1$
        section.set("g", this.green); //$NON-NLS-1$
        section.set("b", this.blue); //$NON-NLS-1$
    }

    /**
     * @return the red
     */
    public byte getRed()
    {
        return this.red;
    }

    /**
     * @return the green
     */
    public byte getGreen()
    {
        return this.green;
    }

    /**
     * @return the blue
     */
    public byte getBlue()
    {
        return this.blue;
    }
    
}
