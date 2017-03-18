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

package de.minigameslib.mclib.pshared;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Data fragments for block markers.
 * 
 * @author mepeisen
 */
public class MarkerData extends AnnotatedDataFragment
{
    
    /**
     * block marker data.
     */
    @PersistentField
    protected BlockMarkerData block;
    
    /**
     * cuboid marker data.
     */
    @PersistentField
    protected CuboidMarkerData cuboid;
    
    /**
     * The plugin declaring the marker.
     */
    @PersistentField
    protected String plugin;
    
    /**
     * The type id  of the marker.
     */
    @PersistentField
    protected String typeId;
    
    /**
     * @return the block
     */
    public BlockMarkerData getBlock()
    {
        return this.block;
    }

    /**
     * @param block the block to set
     */
    public void setBlock(BlockMarkerData block)
    {
        this.block = block;
    }

    /**
     * @return the cuboid
     */
    public CuboidMarkerData getCuboid()
    {
        return this.cuboid;
    }

    /**
     * @param cuboid the cuboid to set
     */
    public void setCuboid(CuboidMarkerData cuboid)
    {
        this.cuboid = cuboid;
    }

    /**
     * @return the plugin
     */
    public String getPlugin()
    {
        return this.plugin;
    }

    /**
     * @param plugin the plugin to set
     */
    public void setPlugin(String plugin)
    {
        this.plugin = plugin;
    }

    /**
     * @return the typeId
     */
    public String getTypeId()
    {
        return this.typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
    }

    /**
     * Data fragments for marker colors.
     */
    public static class MarkerColorData extends AnnotatedDataFragment
    {
        
        /**
         * red color
         */
        @PersistentField
        protected int r;
        /**
         * green color
         */
        @PersistentField
        protected int g;
        /**
         * blue color
         */
        @PersistentField
        protected int b;
        /**
         * alpha color
         */
        @PersistentField
        protected int alpha;

        /**
         * @return the r
         */
        public int getR()
        {
            return this.r;
        }

        /**
         * @param r the r to set
         */
        public void setR(int r)
        {
            this.r = r;
        }

        /**
         * @return the g
         */
        public int getG()
        {
            return this.g;
        }

        /**
         * @param g the g to set
         */
        public void setG(int g)
        {
            this.g = g;
        }

        /**
         * @return the b
         */
        public int getB()
        {
            return this.b;
        }

        /**
         * @param b the b to set
         */
        public void setB(int b)
        {
            this.b = b;
        }

        /**
         * @return the alpha
         */
        public int getAlpha()
        {
            return this.alpha;
        }

        /**
         * @param alpha the alpha to set
         */
        public void setAlpha(int alpha)
        {
            this.alpha = alpha;
        }
    }
    
    /**
     * Data fragments for block markers.
     */
    public static class CuboidMarkerData extends AnnotatedDataFragment
    {
        
        /**
         * x coordinate.
         */
        @PersistentField
        protected int x1;
        
        /**
         * y coordinate.
         */
        @PersistentField
        protected int y1;
        
        /**
         * z coordinate.
         */
        @PersistentField
        protected int z1;
        
        /**
         * x coordinate.
         */
        @PersistentField
        protected int x2;
        
        /**
         * y coordinate.
         */
        @PersistentField
        protected int y2;
        
        /**
         * z coordinate.
         */
        @PersistentField
        protected int z2;
        
        /**
         * marker color
         */
        @PersistentField
        protected MarkerColorData color;

        /**
         * @return the x1
         */
        public int getX1()
        {
            return this.x1;
        }

        /**
         * @param x1 the x1 to set
         */
        public void setX1(int x1)
        {
            this.x1 = x1;
        }

        /**
         * @return the y1
         */
        public int getY1()
        {
            return this.y1;
        }

        /**
         * @param y1 the y1 to set
         */
        public void setY1(int y1)
        {
            this.y1 = y1;
        }

        /**
         * @return the z1
         */
        public int getZ1()
        {
            return this.z1;
        }

        /**
         * @param z1 the z1 to set
         */
        public void setZ1(int z1)
        {
            this.z1 = z1;
        }

        /**
         * @return the x2
         */
        public int getX2()
        {
            return this.x2;
        }

        /**
         * @param x2 the x2 to set
         */
        public void setX2(int x2)
        {
            this.x2 = x2;
        }

        /**
         * @return the y2
         */
        public int getY2()
        {
            return this.y2;
        }

        /**
         * @param y2 the y2 to set
         */
        public void setY2(int y2)
        {
            this.y2 = y2;
        }

        /**
         * @return the z2
         */
        public int getZ2()
        {
            return this.z2;
        }

        /**
         * @param z2 the z2 to set
         */
        public void setZ2(int z2)
        {
            this.z2 = z2;
        }

        /**
         * @return the color
         */
        public MarkerColorData getColor()
        {
            return this.color;
        }

        /**
         * @param color the color to set
         */
        public void setColor(MarkerColorData color)
        {
            this.color = color;
        }

    }
    
    /**
     * Data fragments for block markers.
     */
    public static class BlockMarkerData extends AnnotatedDataFragment
    {
        
        /**
         * x coordinate.
         */
        @PersistentField
        protected int x;
        
        /**
         * y coordinate.
         */
        @PersistentField
        protected int y;
        
        /**
         * z coordinate.
         */
        @PersistentField
        protected int z;
        
        /**
         * marker color
         */
        @PersistentField
        protected MarkerColorData color;
        

        /**
         * @return the x
         */
        public int getX()
        {
            return this.x;
        }

        /**
         * @param x the x to set
         */
        public void setX(int x)
        {
            this.x = x;
        }

        /**
         * @return the y
         */
        public int getY()
        {
            return this.y;
        }

        /**
         * @param y the y to set
         */
        public void setY(int y)
        {
            this.y = y;
        }

        /**
         * @return the z
         */
        public int getZ()
        {
            return this.z;
        }

        /**
         * @param z the z to set
         */
        public void setZ(int z)
        {
            this.z = z;
        }

        /**
         * @return the color
         */
        public MarkerColorData getColor()
        {
            return this.color;
        }

        /**
         * @param color the color to set
         */
        public void setColor(MarkerColorData color)
        {
            this.color = color;
        }

    }

}
