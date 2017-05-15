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

package de.minigameslib.mclib.nms.api;

import java.util.List;

/**
 * Chunk data class.
 * 
 * @author mepeisen
 */
public class ChunkDataImpl
{
    
    /** x chunk coordinate. */
    private int                  chunkX;
    
    /** z chunk coordinate. */
    private int                  chunkZ;
    
    /** til entities. */
    private List<TileEntityData> tiles;
    
    /** block data. */
    private int[][][]            blockData;
    
    /**
     * constructor.
     * 
     * @param chunkX
     *            chunk x coordinate
     * @param chunkZ
     *            chunk z coordinate
     * @param tiles
     *            tile entities data
     * @param blockData
     *            block data
     */
    public ChunkDataImpl(int chunkX, int chunkZ, List<TileEntityData> tiles, int[][][] blockData)
    {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.tiles = tiles;
        this.blockData = blockData;
    }
    
    /**
     * Returns the chunk x coordinate.
     * 
     * @return chunk x
     */
    public int getChunkX()
    {
        return this.chunkX;
    }
    
    /**
     * Returns the chunk z coordinate.
     * 
     * @return chunk z
     */
    public int getChunkZ()
    {
        return this.chunkZ;
    }
    
    /**
     * returns the block data.
     * 
     * @return block data.
     */
    public int[][][] getBlockData()
    {
        return this.blockData;
    }
    
    /**
     * Returns the tile entities.
     * 
     * @return tile entities.
     */
    public List<TileEntityData> getTileEntities()
    {
        return this.tiles;
    }
    
    /**
     * tile entity data.
     */
    public static final class TileEntityData
    {
        /** binary tile entity data. */
        private byte[] data;
        /** string type. */
        private String type;
        /** x coordinate. */
        private int    xcoord;
        /** y coordinate. */
        private int    ycoord;
        /** z coordinate. */
        private int    zcoord;
        
        /**
         * Constructor.
         * 
         * @param data
         *            binary tile entity data.
         * @param type
         *            type name of tile entity
         * @param xcoord
         *            x coordinate
         * @param ycoord
         *            y coordinate
         * @param zcoord
         *            z coordinate.
         */
        public TileEntityData(byte[] data, String type, int xcoord, int ycoord, int zcoord)
        {
            this.data = data;
            this.type = type;
            this.xcoord = xcoord;
            this.ycoord = ycoord;
            this.zcoord = zcoord;
        }
        
        /**
         * the tile entity data.
         * 
         * @return the data
         */
        public byte[] getData()
        {
            return this.data;
        }
        
        /**
         * string type.
         * 
         * @return the type
         */
        public String getType()
        {
            return this.type;
        }
        
        /**
         * x coord.
         * 
         * @return the xcoord
         */
        public int getXcoord()
        {
            return this.xcoord;
        }
        
        /**
         * y coord.
         * 
         * @return the ycoord
         */
        public int getYcoord()
        {
            return this.ycoord;
        }
        
        /**
         * z coord.
         * 
         * @return the zcoord
         */
        public int getZcoord()
        {
            return this.zcoord;
        }
    }
    
}
