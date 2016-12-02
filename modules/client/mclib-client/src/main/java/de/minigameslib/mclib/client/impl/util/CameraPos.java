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

package de.minigameslib.mclib.client.impl.util;

import net.minecraft.entity.Entity;

/**
 * Minecraft camera position (translated)
 * 
 * @author mepeisen
 */
public class CameraPos
{
    
    /** x coordinate. */
    private final double x;
    /** y coordinate. */
    private final double y;
    /** z coordinate. */
    private final double z;
    
    /**
     * Constructs camera pos
     * 
     * @param entity
     * @param partialTicks
     */
    public CameraPos(Entity entity, double partialTicks)
    {
        this.x = entity.prevPosX + ((entity.posX - entity.prevPosX) * partialTicks);
        this.y = entity.prevPosY + ((entity.posY - entity.prevPosY) * partialTicks);
        this.z = entity.prevPosZ + ((entity.posZ - entity.prevPosZ) * partialTicks);
    }

    /**
     * @return the x
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * @return the y
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * @return the z
     */
    public double getZ()
    {
        return this.z;
    }
    
}
