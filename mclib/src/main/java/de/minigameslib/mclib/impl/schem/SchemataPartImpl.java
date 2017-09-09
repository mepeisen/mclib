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

package de.minigameslib.mclib.impl.schem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.items.BlockServiceInterface;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.schem.SchemataPartInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.DataSectionTools;
import de.minigameslib.mclib.impl.items.ItemServiceImpl;
import de.minigameslib.mclib.nms.api.ChunkDataImpl;
import de.minigameslib.mclib.nms.api.ChunkDataImpl.TileEntityData;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Schemata part impl.
 * 
 * @author mepeisen
 */
public class SchemataPartImpl implements SchemataPartInterface
{
    
    /** part name. */
    private String               name;
    
    /** x size. */
    private int                  xsize;
    
    /** y size. */
    private int                  ysize;
    
    /** z size. */
    private int                  zsize;
    
    /** some additional data. */
    private DataSection          additionalData;
    
    /** with air flag. */
    private boolean              withAir;
    
    /**
     * additional part objects (f.e. zones).
     */
    private List<DataSection>    objects       = new ArrayList<>();
    
    /**
     * the block data.
     */
    private int[][][]            blockData;
    
    /**
     * mappings from block names/ keys to block ids.
     */
    private Map<String, Integer> blockMappings = new HashMap<>();
    
    /**
     * the tile entities.
     */
    private List<TileEntityData> tileEntities  = new ArrayList<>();
    
    /**
     * Constructor to create from data stream.
     * 
     * @param data
     *            input stream
     * @throws IOException
     *             thrown on io problems
     */
    public SchemataPartImpl(DataInputStream data) throws IOException
    {
        this.name = data.readUTF();
        this.xsize = data.readInt();
        this.ysize = data.readInt();
        this.zsize = data.readInt();
        this.additionalData = DataSectionTools.read(data);
        this.withAir = data.readBoolean();
        final int objCount = data.readInt();
        for (int i = 0; i < objCount; i++)
        {
            this.objects.add(DataSectionTools.read(data));
        }
        
        // mappings
        int size = data.readInt();
        for (int i = 0; i < size; i++)
        {
            this.blockMappings.put(data.readUTF(), data.readInt());
        }
        
        // tile entities
        size = data.readInt();
        for (int i = 0; i < size; i++)
        {
            final byte[] tiledata = new byte[data.readInt()];
            data.read(tiledata);
            this.tileEntities.add(new TileEntityData(
                tiledata,
                data.readUTF(),
                data.readInt(),
                data.readInt(),
                data.readInt()));
        }
        
        // block data
        this.blockData = new int[this.xsize][][];
        for (int x = 0; x < this.xsize; x++)
        {
            this.blockData[x] = new int[this.ysize][];
            for (int y = 0; y < this.ysize; y++)
            {
                this.blockData[x][y] = new int[this.zsize];
                for (int z = 0; z < this.zsize; z++)
                {
                    this.blockData[x][y][z] = data.readInt();
                }
            }
        }
    }
    
    /**
     * Constructor to create a new part.
     * 
     * @param name
     *            part name.
     */
    public SchemataPartImpl(String name)
    {
        this.name = name;
        this.additionalData = new MemoryDataSection();
    }
    
    @Override
    public String getPartName()
    {
        return this.name;
    }
    
    @Override
    public int getXSize()
    {
        return this.xsize;
    }
    
    @Override
    public int getYSize()
    {
        return this.ysize;
    }
    
    @Override
    public int getZSize()
    {
        return this.zsize;
    }
    
    /**
     * Saves this part to stream.
     * 
     * @param buffer
     *            output stream
     * @throws IOException
     *             thrown on io problems
     */
    public void saveToStream(DataOutputStream buffer) throws IOException
    {
        buffer.writeUTF(this.name);
        buffer.writeInt(this.xsize);
        buffer.writeInt(this.ysize);
        buffer.writeInt(this.zsize);
        DataSectionTools.toBytes(buffer, this.additionalData);
        buffer.writeBoolean(this.withAir);
        buffer.writeInt(this.objects.size());
        for (final DataSection data : this.objects)
        {
            DataSectionTools.toBytes(buffer, data);
        }
        
        // mappings
        buffer.writeInt(this.blockMappings.size());
        for (final Map.Entry<String, Integer> entry : this.blockMappings.entrySet())
        {
            buffer.writeUTF(entry.getKey());
            buffer.writeInt(entry.getValue());
        }
        
        // tile entities
        buffer.writeInt(this.tileEntities.size());
        for (final TileEntityData tile : this.tileEntities)
        {
            buffer.writeInt(tile.getData().length);
            buffer.write(tile.getData());
            buffer.writeUTF(tile.getType());
            buffer.writeInt(tile.getXcoord());
            buffer.writeInt(tile.getYcoord());
            buffer.writeInt(tile.getZcoord());
        }
        
        // block data
        for (int x = 0; x < this.xsize; x++)
        {
            for (int y = 0; y < this.ysize; y++)
            {
                for (int z = 0; z < this.zsize; z++)
                {
                    buffer.writeInt(this.blockData[x][y][z]);
                }
            }
        }
    }
    
    /**
     * Sets thw with air flag.
     * 
     * @param b
     *            {@code true} to set air blocks during restore; {@code false} to leave air blocks untouched.
     */
    public void setWithAir(boolean b)
    {
        this.withAir = b;
    }
    
    /**
     * Sets the chunk data to internal array.
     * 
     * @param allChunks
     *            all chunk data.
     * @param lowLoc
     *            low location (starting point)
     * @param highLoc
     *            high location (end point)
     */
    public void setChunkData(List<ChunkDataImpl> allChunks, Location lowLoc, Location highLoc)
    {
        this.xsize = highLoc.getBlockX() - lowLoc.getBlockX() + 1;
        this.ysize = highLoc.getBlockY() - lowLoc.getBlockY() + 1;
        this.zsize = highLoc.getBlockZ() - lowLoc.getBlockZ() + 1;
        this.blockMappings = ((ItemServiceImpl) BlockServiceInterface.instance()).getBlockMappings();
        
        // init block data
        this.blockData = new int[this.xsize][][];
        for (int x = 0; x < this.xsize; x++)
        {
            this.blockData[x] = new int[this.ysize][];
            for (int y = 0; y < this.ysize; y++)
            {
                this.blockData[x][y] = new int[this.zsize];
            }
        }
        
        final Cuboid cub = new Cuboid(lowLoc, highLoc);
        
        // parse and overtake chunks
        while (!allChunks.isEmpty())
        {
            final ChunkDataImpl chunk = allChunks.remove(allChunks.size() - 1);
            final int xPos = chunk.getChunkX() << 4;
            final int zPos = chunk.getChunkZ() << 4;
            
            // check tile entities
            for (final TileEntityData tileEntity : chunk.getTileEntities())
            {
                if (cub.containsLoc(new Location(lowLoc.getWorld(), tileEntity.getXcoord(), tileEntity.getYcoord(), tileEntity.getZcoord())))
                {
                    this.tileEntities.add(new TileEntityData(
                        tileEntity.getData(),
                        tileEntity.getType(),
                        tileEntity.getXcoord() - lowLoc.getBlockX(),
                        tileEntity.getYcoord() - lowLoc.getBlockY(),
                        tileEntity.getZcoord() - lowLoc.getBlockZ()));
                }
            }
            
            // check data
            final int[][][] chunkdata = chunk.getBlockData();
            for (int x = 0; x < 16; x++)
            {
                int relx = xPos + x - lowLoc.getBlockX();
                if (relx >= 0 && relx < this.xsize)
                {
                    for (int y = 0; y < chunkdata[x].length; y++)
                    {
                        int rely = y - lowLoc.getBlockY();
                        if (rely >= 0 && rely < this.ysize)
                        {
                            for (int z = 0; z < 16; z++)
                            {
                                int relz = zPos + z - lowLoc.getBlockZ();
                                if (relz >= 0 && relz < this.xsize)
                                {
                                    this.blockData[relx][rely][relz] = chunkdata[x][y][z];
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Sets object data.
     * 
     * @param data
     *            objects data
     */
    public void setObjectData(List<DataSection> data)
    {
        this.objects.clear();
        this.objects.addAll(data);
    }
    
    /**
     * Returns tasks to set blocks.
     * 
     * @param loc
     *            starting location
     * @param writeBlocksPerTick
     *            number of blocks to write each tick/task
     * @return tasks
     * @throws McException
     *             thrown if blocks are unknown
     */
    public List<Runnable> getBlockTasks(Location loc, int writeBlocksPerTick) throws McException
    {
        final Map<Integer, Integer> realMappings = ((ItemServiceImpl) BlockServiceInterface.instance()).calculateIdMapping(this.blockMappings);
        final List<Runnable> result = new ArrayList<>();
        for (int y = 0; y < this.ysize; y++)
        {
            for (int x = 0; x < this.xsize; x++)
            {
                int z = 0;
                while (z < this.zsize)
                {
                    final int x2 = x;
                    final int y2 = y;
                    final int z2 = z;
                    result.add(() -> this.setBlocks(realMappings, loc, x2, y2, z2, writeBlocksPerTick));
                    z += writeBlocksPerTick;
                }
            }
        }
        
        int start = 0;
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        while (start < this.tileEntities.size())
        {
            final List<TileEntityData> data = this.tileEntities.stream().skip(start).limit(writeBlocksPerTick).collect(Collectors.toList());
            result.add(() -> helper.loadTileEntities(loc, data));
        }
        return result;
    }
    
    /**
     * Write blocks along z axis.
     * 
     * @param realMappings
     *            mappings from data int to real server wide integer
     * @param loc
     *            starting location.
     * @param x
     *            relative x pos
     * @param y
     *            relative y pos
     * @param z
     *            relative z pos
     * @param writeBlocksPerTick
     *            maximum blocks to write
     */
    private void setBlocks(Map<Integer, Integer> realMappings, Location loc, int x, int y, int z, int writeBlocksPerTick)
    {
        final int[] blocks = Arrays.stream(this.blockData[x][y]).skip(z).limit(writeBlocksPerTick).toArray();
        ((ItemServiceImpl) BlockServiceInterface.instance()).setCustomBlocks(
            new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z),
            realMappings,
            blocks);
    }
    
    /**
     * Returns the serialized objects data.
     * 
     * @return objects
     */
    public List<DataSection> getObjects()
    {
        return this.objects;
    }
    
    @Override
    public void applyToWorld(Location loc, McRunnable success, McRunnable failure) throws McException
    {
        final List<Runnable> tasks = this.getBlockTasks(loc, SchemataImpl.WRITE_BLOCKS_PER_TICK);
        final List<DataSection> allData = this.getObjects();
        new SchemataImpl.BlockSetter(tasks.iterator(), () ->
        {
            new SchemataImpl.ObjectWriter(loc, allData, 0, () ->
            {
                if (success != null)
                {
                    try
                    {
                        success.run();
                    }
                    catch (McException ex)
                    {
                        SchemataImpl.LOGGER.log(Level.WARNING, "Exception while running success method (apply to world)", ex); //$NON-NLS-1$
                    }
                }
            }).runTaskLater((Plugin) McLibInterface.instance(), 1);
        }).runTaskLater((Plugin) McLibInterface.instance(), 1);
    }
    
}
