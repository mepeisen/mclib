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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectIdInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.schem.SchemataBuilderInterface;
import de.minigameslib.mclib.api.schem.SchemataPartInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.DataSectionTools;
import de.minigameslib.mclib.impl.obj.ObjectsManager;
import de.minigameslib.mclib.nms.api.ChunkDataImpl;
import de.minigameslib.mclib.nms.api.ItemHelperInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * A schemata implementation.
 * 
 * @author mepeisen
 */
public class SchemataImpl implements SchemataBuilderInterface
{
    
    // TODO check if common objects/zones etc. have links to others in their data section. Once rebuild they need special treetment.
    // TODO absolute locations on handler data need to be fixed on reload.
    
    /**
     * A bukkit task to set blocks.
     */
    static final class BlockSetter extends BukkitRunnable
    {
        
        /** the setter tasks. */
        private final Iterator<Runnable> tasks;
        
        /** success function. */
        private final Runnable           success;
        
        /**
         * Constructor.
         * 
         * @param tasks
         *            setter tasks.
         * @param success
         *            success function.
         */
        public BlockSetter(Iterator<Runnable> tasks, Runnable success)
        {
            this.tasks = tasks;
            this.success = success;
        }
        
        @Override
        public void run()
        {
            if (this.tasks.hasNext())
            {
                this.tasks.next().run();
                new BlockSetter(this.tasks, this.success).runTaskLater((Plugin) McLibInterface.instance(), 1);
            }
            else
            {
                this.success.run();
            }
        }
    }
    
    /**
     * A bukkit task to load chunks.
     */
    private static final class ChunkLoader extends BukkitRunnable
    {
        /**
         * nms helper.
         */
        private final ItemHelperInterface      helper;
        /**
         * resulting chunk data.
         */
        private final List<ChunkDataImpl> allChunks;
        
        /** the world reference. */
        private final World                    world;
        
        /**
         * index.
         */
        private final int                      index;
        
        /** chunks to be processed. */
        private List<int[]>                    chunks;
        
        /** success function. */
        private Runnable                       success;
        
        /**
         * Constructor.
         * 
         * @param helper
         *            nms helper
         * @param world
         *            the target world
         * @param chunks
         *            chunks to load
         * @param allChunks
         *            result list
         * @param index
         *            current index of {@code chunks}
         * @param success
         *            success function
         */
        ChunkLoader(ItemHelperInterface helper, World world, List<int[]> chunks, List<ChunkDataImpl> allChunks, int index, Runnable success)
        {
            this.helper = helper;
            this.chunks = chunks;
            this.allChunks = allChunks;
            this.index = index;
            this.world = world;
            this.success = success;
        }
        
        @Override
        public void run()
        {
            final List<int[]> chunksToRead = this.chunks.stream().skip(this.index).limit(READ_CHUNKS_PER_TICK).collect(Collectors.toList());
            for (final int[] chunkCoordPair : chunksToRead)
            {
                final Chunk chunk = this.world.getChunkAt(chunkCoordPair[0], chunkCoordPair[1]);
                this.allChunks.add(this.helper.getChunkSnapshot(chunk));
            }
            int j = this.index + READ_CHUNKS_PER_TICK;
            if (j < this.chunks.size())
            {
                new ChunkLoader(this.helper, this.world, this.chunks, this.allChunks, j, this.success).runTaskLater((Plugin) McLibInterface.instance(), 1);
            }
            else
            {
                this.success.run();
            }
        }
    }
    
    /**
     * A bukkit task to load objects.
     */
    private static final class ObjectLoader extends BukkitRunnable
    {
        /**
         * resulting chunk data.
         */
        private final List<DataSection> allData;
        
        /**
         * index.
         */
        private final int               index;
        
        /** Objects to be processed. */
        private List<Object>            objects;
        
        /** success function. */
        private Runnable                success;
        
        /** location. */
        private Location                location;
        
        /**
         * Constructor.
         * 
         * @param location
         *            location
         * @param objects
         *            objects to load
         * @param allData
         *            result list
         * @param index
         *            current index of {@code chunks}
         * @param success
         *            success function
         */
        ObjectLoader(Location location, List<Object> objects, List<DataSection> allData, int index, Runnable success)
        {
            this.objects = objects;
            this.allData = allData;
            this.index = index;
            this.success = success;
            this.location = location;
        }
        
        @Override
        public void run()
        {
            final ObjectsManager manager = (ObjectsManager) ObjectServiceInterface.instance();
            final List<Object> objectsToRead = this.objects.stream().skip(this.index).limit(READ_OBJECTS_PER_TICK).collect(Collectors.toList());
            for (final Object object : objectsToRead)
            {
                final DataSection section = manager.toDataSection(this.location, object);
                if (section != null)
                {
                    this.allData.add(section);
                }
            }
            int j = this.index + READ_OBJECTS_PER_TICK;
            if (j < this.objects.size())
            {
                new ObjectLoader(this.location, this.objects, this.allData, j, this.success).runTaskLater((Plugin) McLibInterface.instance(), 1);
            }
            else
            {
                this.success.run();
            }
        }
    }
    
    /**
     * A bukkit task to write objects.
     */
    static final class ObjectWriter extends BukkitRunnable
    {
        /**
         * chunk data.
         */
        private final List<DataSection> allData;
        
        /**
         * index.
         */
        private final int               index;
        
        /** success function. */
        private Runnable                success;
        
        /** part location. */
        private Location                location;
        
        /**
         * Constructor.
         * 
         * @param location
         *            location
         * @param allData
         *            result list
         * @param index
         *            current index of {@code chunks}
         * @param success
         *            success function
         */
        ObjectWriter(Location location, List<DataSection> allData, int index, Runnable success)
        {
            this.allData = allData;
            this.index = index;
            this.success = success;
            this.location = location;
        }
        
        @Override
        public void run()
        {
            if (this.index < this.allData.size())
            {
                this.success.run();
            }
            else
            {
                final ObjectsManager manager = (ObjectsManager) ObjectServiceInterface.instance();
                final List<DataSection> objectsToWrite = this.allData.stream().skip(this.index).limit(WRITE_OBJECTS_PER_TICK).collect(Collectors.toList());
                for (final DataSection section : objectsToWrite)
                {
                    try
                    {
                        manager.createFromDataSection(this.location, section);
                    }
                    catch (IOException | McException ex)
                    {
                        // TODO Logging and report failure
                    }
                }
                new ObjectWriter(this.location, this.allData, this.index + WRITE_BLOCKS_PER_TICK, this.success).runTaskLater((Plugin) McLibInterface.instance(), 1);
            }
        }
    }
    
    /**
     * the title.
     */
    private LocalizedConfigString                 title;
    
    /**
     * the schemata author.
     */
    private String                                author;
    
    /**
     * schemata url.
     */
    private String                                url;
    
    /**
     * the schemata version.
     */
    private int                                   version;
    
    /**
     * the schemata api version.
     */
    private int                                   apiVersion;
    
    /**
     * the schemata parts.
     */
    List<SchemataPartImpl>                        parts                  = new ArrayList<>();
    
    /**
     * the schemata objects.
     */
    List<DataSection>                             objects                = new ArrayList<>();
    
    /**
     * the additional schemata data by plugin and unique data key.
     */
    private Map<String, Map<String, DataSection>> additionalData         = new HashMap<>();
    
    /**
     * the executor service for asynchronous tasks.
     */
    ExecutorService                               executorService;
    
    /** number of chunks that are read in each tick. */
    private static final int                      READ_CHUNKS_PER_TICK   = 10;                                            // TODO let us configure it.
    
    /** number of objects that are read in each tick. */
    private static final int                      READ_OBJECTS_PER_TICK  = 10;                                            // TODO let us configure it.
    
    /** number of blocks that are changed in each tick. */
    static final int                              WRITE_BLOCKS_PER_TICK  = 100;                                           // TODO let us configure it.
    
    /** number of objects that are changed in each tick. */
    private static final int                      WRITE_OBJECTS_PER_TICK = 10;                                            // TODO let us configure it.
    
    /** logger. */
    static final Logger                           LOGGER                 = Logger.getLogger(SchemataImpl.class.getName());
    
    /**
     * Construct from reader.
     * 
     * @param executorService
     *            the executor service for asynchronous tasks
     * @param data
     *            input
     * @throws IOException
     *             thrown on io problems
     */
    public SchemataImpl(ExecutorService executorService, DataInputStream data) throws IOException
    {
        this.executorService = executorService;
        this.apiVersion = data.readInt();
        
        final DataSection section = DataSectionTools.read(data);
        this.title = section.getFragment(LocalizedConfigString.class, "title"); //$NON-NLS-1$
        if (this.title == null)
        {
            this.title = new LocalizedConfigString();
        }
        this.author = section.getString("author"); //$NON-NLS-1$
        this.url = section.getString("url"); //$NON-NLS-1$
        this.version = section.getInt("version"); //$NON-NLS-1$
        
        final int partSize = data.readInt();
        for (int i = 0; i < partSize; i++)
        {
            this.parts.add(new SchemataPartImpl(data));
        }
        
        final int objectSize = data.readInt();
        for (int i = 0; i < objectSize; i++)
        {
            this.objects.add(DataSectionTools.read(data));
        }
        
        final int additionalDataSize = data.readInt();
        for (int i = 0; i < additionalDataSize; i++)
        {
            final String plugin = data.readUTF();
            final int contentSize = data.readInt();
            final Map<String, DataSection> content = new HashMap<>();
            this.additionalData.put(plugin, content);
            for (int j = 0; j < contentSize; j++)
            {
                content.put(data.readUTF(), DataSectionTools.read(data));
            }
        }
    }
    
    /**
     * Constructor for new schemata.
     * 
     * @param executorService
     *            the executor service for asynchronous tasks
     */
    public SchemataImpl(ExecutorService executorService)
    {
        this.apiVersion = McLibInterface.instance().getApiVersion();
        this.url = RANDOM_SCHEMATA;
        this.title = new LocalizedConfigString();
        this.executorService = executorService;
    }
    
    @Override
    public LocalizedConfigString getTitle()
    {
        return this.title;
    }
    
    @Override
    public String getAuthor()
    {
        return this.author;
    }
    
    @Override
    public String getSchemataUrl()
    {
        return this.url;
    }
    
    @Override
    public int getSchemataVersion()
    {
        return this.version;
    }
    
    @Override
    public int getApiVersion()
    {
        return this.apiVersion;
    }
    
    @Override
    public int getPartCount()
    {
        return this.parts.size();
    }
    
    @Override
    public SchemataPartInterface getPart(int index)
    {
        return index < 0 || index >= this.parts.size() ? null : this.parts.get(index);
    }
    
    @Override
    public void saveToStream(OutputStream os) throws IOException
    {
        try (final GZIPOutputStream gzos = new GZIPOutputStream(os))
        {
            try (final DataOutputStream buffer = new DataOutputStream(gzos))
            {
                buffer.writeUTF(SchemataServiceImpl.STREAM_MAGIC);
                
                buffer.writeInt(this.apiVersion);
                
                final DataSection section = new MemoryDataSection();
                section.set("title", this.title); //$NON-NLS-1$
                section.set("author", this.author); //$NON-NLS-1$
                section.set("url", this.url); //$NON-NLS-1$
                section.set("version", this.version); //$NON-NLS-1$
                DataSectionTools.toBytes(buffer, section);
                
                buffer.writeInt(this.parts.size());
                for (final SchemataPartImpl part : this.parts)
                {
                    part.saveToStream(buffer);
                }
                
                buffer.writeInt(this.objects.size());
                for (final DataSection obj : this.objects)
                {
                    DataSectionTools.toBytes(buffer, obj);
                }
                
                buffer.writeInt(this.additionalData.size());
                for (final Map.Entry<String, Map<String, DataSection>> pluginEntry : this.additionalData.entrySet())
                {
                    buffer.writeUTF(pluginEntry.getKey());
                    buffer.writeInt(pluginEntry.getValue().size());
                    for (final Map.Entry<String, DataSection> dataEntry : pluginEntry.getValue().entrySet())
                    {
                        buffer.writeUTF(dataEntry.getKey());
                        DataSectionTools.toBytes(buffer, dataEntry.getValue());
                    }
                }
            }
        }
    }
    
    @Override
    public void saveToFile(File file) throws IOException
    {
        try (final FileOutputStream fos = new FileOutputStream(file))
        {
            this.saveToStream(fos);
        }
    }
    
    @Override
    public SchemataBuilderInterface setTitle(Locale locale, String title)
    {
        this.title.setUserMessage(locale, title);
        return this;
    }
    
    @Override
    public SchemataBuilderInterface setAuthor(String author)
    {
        this.author = author;
        return this;
    }
    
    @Override
    public SchemataBuilderInterface setSchemataUrl(String url)
    {
        this.url = url;
        return this;
    }
    
    @Override
    public SchemataBuilderInterface setSchemataVersion(int version)
    {
        this.version = version;
        return this;
    }
    
    @Override
    public SchemataBuilderInterface addPart(String name, Cuboid cuboid, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        this.executorService.submit(() -> this.addPartForCuboid(name, cuboid, success, failure, options));
        return this;
    }
    
    @Override
    public SchemataBuilderInterface addPart(String name, ComponentIdInterface component, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        this.executorService.submit(() -> this.addPartForComponent(name, component, success, failure, options));
        return this;
    }
    
    @Override
    public SchemataBuilderInterface addPart(String name, ZoneIdInterface zone, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        this.executorService.submit(() -> this.addPartForZone(name, zone, success, failure, options));
        return this;
    }
    
    @Override
    public SchemataBuilderInterface addPart(String name, SignIdInterface sign, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        this.executorService.submit(() -> this.addPartForSign(name, sign, success, failure, options));
        return this;
    }
    
    /**
     * adds part (async).
     * 
     * @param name
     *            part name
     * @param cuboid
     *            cuboid
     * @param success
     *            success func
     * @param failure
     *            failure func
     * @param options
     *            add options
     */
    private void addPartForCuboid(String name, Cuboid cuboid, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        final SchemataPartImpl impl = new SchemataPartImpl(name);
        
        final Location lowLoc = cuboid.getLowLoc();
        final Location highLoc = cuboid.getHighLoc();
        final List<int[]> chunks = getChunkCoords(lowLoc, highLoc);
        
        this.addChunksToSchemata(impl, lowLoc, highLoc, chunks,
            () -> this.parseOptions(impl, lowLoc, highLoc, chunks, options,
                () -> this.addPart0(impl, success),
                failure),
            failure);
    }
    
    /**
     * Adds schemata to list and invokes success function.
     * 
     * @param impl
     *            impl to be added
     * @param success
     *            to be called on success
     */
    private void addPart0(SchemataPartImpl impl, McRunnable success)
    {
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                SchemataImpl.this.parts.add(impl);
                if (success != null)
                {
                    try
                    {
                        success.run();
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Exception while running success method (add schemata part)", ex); //$NON-NLS-1$
                    }
                }
            }
        }.runTaskLater((Plugin) McLibInterface.instance(), 1);
    }
    
    /**
     * Parses options for building schemata target.
     * 
     * @param impl
     *            target
     * @param lowLoc
     *            low loc
     * @param highLoc
     *            high loc
     * @param chunks
     *            chunks array
     * @param options
     *            options to be parsed
     * @param success
     *            success function
     * @param failure
     *            failure function
     */
    private void parseOptions(SchemataPartImpl impl, Location lowLoc, Location highLoc, List<int[]> chunks, PartBuildOptions[] options, Runnable success, McRunnable failure)
    {
        if (options == null || options.length == 0)
        {
            success.run();
        }
        else
        {
            for (final PartBuildOptions option : options)
            {
                switch (option)
                {
                    case WithAir:
                        impl.setWithAir(true);
                        success.run();
                        break;
                    case WithObjects:
                        this.addObjectsToSchemata(impl, lowLoc, highLoc, chunks, success, failure);
                        // success will be invoked insode method addObjectsToSchemata
                        break;
                    default:
                        LOGGER.log(Level.WARNING, "Unknown part building option " + option); //$NON-NLS-1$
                        success.run();
                        break;
                }
            }
        }
    }
    
    /**
     * Returns chunk coords from given low loc/ high loc pair.
     * 
     * @param lowLoc
     *            low location
     * @param highLoc
     *            high location
     * @return chunk coords array
     */
    private List<int[]> getChunkCoords(final Location lowLoc, final Location highLoc)
    {
        final int chunkX1 = lowLoc.getBlockX() >> 4;
        final int chunkZ1 = lowLoc.getBlockZ() >> 4;
        final int chunkX2 = highLoc.getBlockX() >> 4;
        final int chunkZ2 = highLoc.getBlockZ() >> 4;
        final List<int[]> chunks = new ArrayList<>();
        for (int chunkX = chunkX1; chunkX <= chunkX2; chunkX++)
        {
            for (int chunkZ = chunkZ1; chunkZ <= chunkZ2; chunkZ++)
            {
                chunks.add(new int[] { chunkX, chunkZ });
            }
        }
        return chunks;
    }
    
    /**
     * Adds chunk data to given schemata (async).
     * 
     * @param target
     *            target schemata
     * @param lowLoc
     *            low location
     * @param highLoc
     *            high location
     * @param chunks
     *            chunk coordinates
     * @param success
     *            success function
     * @param failure
     *            failure function
     */
    private void addChunksToSchemata(SchemataPartImpl target, Location lowLoc, Location highLoc, List<int[]> chunks, Runnable success, McRunnable failure)
    {
        final List<ChunkDataImpl> allChunks = new ArrayList<>();
        final ItemHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(ItemHelperInterface.class);
        new ChunkLoader(helper, lowLoc.getWorld(), chunks, allChunks, 0, () ->
        {
            this.executorService.submit(() ->
            {
                target.setChunkData(allChunks, lowLoc, highLoc);
                success.run();
            });
        }).runTaskLater((Plugin) McLibInterface.instance(), 1);
    }
    
    /**
     * Adds object data to given schemata (async).
     * 
     * @param target
     *            target schemata
     * @param lowLoc
     *            low location
     * @param highLoc
     *            high location
     * @param chunks
     *            chunk coordinates
     * @param success
     *            success function
     * @param failure
     *            failure function
     */
    private void addObjectsToSchemata(SchemataPartImpl target, Location lowLoc, Location highLoc, List<int[]> chunks, Runnable success, McRunnable failure)
    {
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                final Cuboid cub = new Cuboid(lowLoc, highLoc);
                final ObjectServiceInterface osi = ObjectServiceInterface.instance();
                final List<Object> objList = new ArrayList<>();
                objList.addAll(osi.findZones(cub, CuboidMode.FindMatching));
                objList.addAll(osi.findZones(cub, CuboidMode.FindChildren));
                objList.addAll(osi.findSigns(cub));
                objList.addAll(osi.findEntities(cub));
                objList.addAll(osi.findComponents(cub));
                SchemataImpl.this.executorService.submit(() -> addObjectsToSchemata(lowLoc, target, objList, success, failure));
            }
        }.runTaskLater((Plugin) McLibInterface.instance(), 1);
    }
    
    /**
     * Adds object data to given schemata (async).
     * 
     * @param location
     *            starting location
     * @param target
     *            target schemata
     * @param objList
     *            object definitions
     * @param success
     *            success function
     * @param failure
     *            failure function
     */
    protected void addObjectsToSchemata(Location location, SchemataPartImpl target, List<Object> objList, Runnable success, McRunnable failure)
    {
        if (objList.isEmpty())
        {
            success.run();
        }
        else
        {
            final List<DataSection> data = new ArrayList<>();
            new ObjectLoader(location, objList, data, 0, () ->
            {
                target.setObjectData(data);
                success.run();
            }).runTaskLater((Plugin) McLibInterface.instance(), 1);
        }
    }
    
    /**
     * Adds part for cuboid from given zone.
     * 
     * @param name
     *            name
     * @param zone
     *            zone
     * @param success
     *            success
     * @param failure
     *            failure
     * @param options
     *            options
     */
    private void addPartForZone(String name, ZoneIdInterface zone, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        final ZoneInterface zoneImpl = ObjectServiceInterface.instance().findZone(zone);
        this.addPart(
            name,
            zoneImpl.getCuboid(),
            success,
            failure,
            options);
    }
    
    /**
     * Adds part for cuboid from given component.
     * 
     * @param name
     *            name
     * @param component
     *            component
     * @param success
     *            success
     * @param failure
     *            failure
     * @param options
     *            options
     */
    private void addPartForComponent(String name, ComponentIdInterface component, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        final ComponentInterface compImpl = ObjectServiceInterface.instance().findComponent(component);
        this.addPart(
            name,
            new Cuboid(compImpl.getLocation(), compImpl.getLocation()),
            success,
            failure,
            options);
    }
    
    /**
     * Adds part for cuboid from given sign.
     * 
     * @param name
     *            name
     * @param sign
     *            sign
     * @param success
     *            success
     * @param failure
     *            failure
     * @param options
     *            options
     */
    private void addPartForSign(String name, SignIdInterface sign, McRunnable success, McRunnable failure, PartBuildOptions... options)
    {
        final SignInterface signImpl = ObjectServiceInterface.instance().findSign(sign);
        this.addPart(
            name,
            new Cuboid(signImpl.getLocation(), signImpl.getLocation()),
            success,
            failure,
            options);
    }
    
    @Override
    public SchemataBuilderInterface addObject(ObjectIdInterface objectId, McRunnable success, McRunnable failure)
    {
        this.executorService.submit(() -> this.addObject0(objectId, success, failure));
        return this;
    }
    
    /**
     * Adds given object to object list.
     * 
     * @param objectId
     *            object id to be added
     * @param success
     *            success function
     * @param failure
     *            failure function
     */
    private void addObject0(ObjectIdInterface objectId, McRunnable success, McRunnable failure)
    {
        final ObjectsManager manager = (ObjectsManager) ObjectServiceInterface.instance();
        this.objects.add(manager.toDataSection(null, manager.findObject(objectId)));
        try
        {
            success.run();
        }
        catch (McException ex)
        {
            LOGGER.log(Level.WARNING, "Exception while running success method (add schemata object)", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public void applyToWorld(Location[] locations, McRunnable success, McRunnable failure) throws McException
    {
        if (locations.length != this.parts.size())
        {
            throw new McException(CommonMessages.InternalError, new ArrayIndexOutOfBoundsException());
        }
        this.executorService.submit(() -> this.applyPartToWorld(locations, success, failure, 0));
    }
    
    /**
     * Applies a part to world.
     * 
     * @param locations
     *            locations
     * @param success
     *            success function
     * @param failure
     *            failure function
     * @param index
     *            index
     */
    private void applyPartToWorld(Location[] locations, McRunnable success, McRunnable failure, int index)
    {
        final Location loc = locations[index];
        final SchemataPartImpl part = this.parts.get(index);
        List<Runnable> tasks;
        try
        {
            tasks = part.getBlockTasks(loc, WRITE_BLOCKS_PER_TICK);
        }
        catch (McException e)
        {
            LOGGER.log(Level.WARNING, "Unknown blocks detected while restoring schemata", e); //$NON-NLS-1$
            try
            {
                failure.run();
            }
            catch (McException ex)
            {
                LOGGER.log(Level.WARNING, "Exception while invoking fail handler", ex); //$NON-NLS-1$
            }
            return;
        }
        final List<DataSection> allData = part.getObjects();
        new BlockSetter(tasks.iterator(), () ->
        {
            new ObjectWriter(loc, allData, 0, () ->
            {
                int j = index + 1;
                if (j < locations.length)
                {
                    this.executorService.submit(() -> this.applyPartToWorld(locations, success, failure, j));
                }
                else
                {
                    new ObjectWriter(null, this.objects, 0, () ->
                    {
                        if (success != null)
                        {
                            try
                            {
                                success.run();
                            }
                            catch (McException ex)
                            {
                                LOGGER.log(Level.WARNING, "Exception while running success method (apply to world)", ex); //$NON-NLS-1$
                            }
                        }
                    }).runTaskLater((Plugin) McLibInterface.instance(), 1);
                }
            }).runTaskLater((Plugin) McLibInterface.instance(), 1);
        }).runTaskLater((Plugin) McLibInterface.instance(), 1);
    }
    
}
