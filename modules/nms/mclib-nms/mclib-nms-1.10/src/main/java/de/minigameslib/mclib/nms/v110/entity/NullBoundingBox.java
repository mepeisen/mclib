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

package de.minigameslib.mclib.nms.v110.entity;

import net.minecraft.server.v1_10_R1.AxisAlignedBB;
import net.minecraft.server.v1_10_R1.Vec3D;

/**
 * A "null" bounding box; no bounds.
 * 
 * @author mepeisen
 */
public class NullBoundingBox extends AxisAlignedBB
{
    
    /**
     * Constructor.
     */
    public NullBoundingBox()
    {
        super(0, 0, 0, 0, 0, 0);
    }
    
    @Override
    public double a()
    {
        return 0.0;
    }
    
    @Override
    public double a(AxisAlignedBB arg0, double arg1)
    {
        return 0.0;
    }
    
    @Override
    public AxisAlignedBB a(AxisAlignedBB arg0)
    {
        return this;
    }
    
    @Override
    public AxisAlignedBB a(double arg0, double arg1, double arg2)
    {
        return this;
    }
    
    @Override
    public boolean a(Vec3D arg0)
    {
        return false;
    }
    
    @Override
    public double b(AxisAlignedBB arg0, double arg1)
    {
        return 0.0;
    }
    
    @Override
    public boolean b(AxisAlignedBB arg0)
    {
        return false;
    }
    
    @Override
    public double c(AxisAlignedBB arg0, double arg1)
    {
        return 0.0;
    }
    
    @Override
    public AxisAlignedBB c(double arg0, double arg1, double arg2)
    {
        return this;
    }
    
    @Override
    public AxisAlignedBB grow(double arg0, double arg1, double arg2)
    {
        return this;
    }
    
    @Override
    public AxisAlignedBB shrink(double arg0)
    {
        return this;
    }
    
}
